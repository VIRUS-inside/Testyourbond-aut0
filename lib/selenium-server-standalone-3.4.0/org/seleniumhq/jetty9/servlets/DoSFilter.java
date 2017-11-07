package org.seleniumhq.jetty9.servlets;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionEvent;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.annotation.Name;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.ScheduledExecutorScheduler;
import org.seleniumhq.jetty9.util.thread.Scheduler;
import org.seleniumhq.jetty9.util.thread.Scheduler.Task;




















































































@ManagedObject("limits exposure to abuse from request flooding, whether malicious, or as a result of a misconfigured client")
public class DoSFilter
  implements Filter
{
  private static final Logger LOG = Log.getLogger(DoSFilter.class);
  
  private static final String IPv4_GROUP = "(\\d{1,3})";
  private static final Pattern IPv4_PATTERN = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
  private static final String IPv6_GROUP = "(\\p{XDigit}{1,4})";
  private static final Pattern IPv6_PATTERN = Pattern.compile("(\\p{XDigit}{1,4}):(\\p{XDigit}{1,4}):(\\p{XDigit}{1,4}):(\\p{XDigit}{1,4}):(\\p{XDigit}{1,4}):(\\p{XDigit}{1,4}):(\\p{XDigit}{1,4}):(\\p{XDigit}{1,4})");
  private static final Pattern CIDR_PATTERN = Pattern.compile("([^/]+)/(\\d+)");
  
  private static final String __TRACKER = "DoSFilter.Tracker";
  
  private static final String __THROTTLED = "DoSFilter.Throttled";
  
  private static final int __DEFAULT_MAX_REQUESTS_PER_SEC = 25;
  
  private static final int __DEFAULT_DELAY_MS = 100;
  
  private static final int __DEFAULT_THROTTLE = 5;
  private static final int __DEFAULT_MAX_WAIT_MS = 50;
  private static final long __DEFAULT_THROTTLE_MS = 30000L;
  private static final long __DEFAULT_MAX_REQUEST_MS_INIT_PARAM = 30000L;
  private static final long __DEFAULT_MAX_IDLE_TRACKER_MS_INIT_PARAM = 30000L;
  static final String MANAGED_ATTR_INIT_PARAM = "managedAttr";
  static final String MAX_REQUESTS_PER_S_INIT_PARAM = "maxRequestsPerSec";
  static final String DELAY_MS_INIT_PARAM = "delayMs";
  static final String THROTTLED_REQUESTS_INIT_PARAM = "throttledRequests";
  static final String MAX_WAIT_INIT_PARAM = "maxWaitMs";
  static final String THROTTLE_MS_INIT_PARAM = "throttleMs";
  static final String MAX_REQUEST_MS_INIT_PARAM = "maxRequestMs";
  static final String MAX_IDLE_TRACKER_MS_INIT_PARAM = "maxIdleTrackerMs";
  static final String INSERT_HEADERS_INIT_PARAM = "insertHeaders";
  static final String TRACK_SESSIONS_INIT_PARAM = "trackSessions";
  static final String REMOTE_PORT_INIT_PARAM = "remotePort";
  static final String IP_WHITELIST_INIT_PARAM = "ipWhitelist";
  static final String ENABLED_INIT_PARAM = "enabled";
  static final String TOO_MANY_CODE = "tooManyCode";
  private static final int USER_AUTH = 2;
  private static final int USER_SESSION = 2;
  private static final int USER_IP = 1;
  private static final int USER_UNKNOWN = 0;
  private final String _suspended = "DoSFilter@" + Integer.toHexString(hashCode()) + ".SUSPENDED";
  private final String _resumed = "DoSFilter@" + Integer.toHexString(hashCode()) + ".RESUMED";
  private final ConcurrentHashMap<String, RateTracker> _rateTrackers = new ConcurrentHashMap();
  private final List<String> _whitelist = new CopyOnWriteArrayList();
  private int _tooManyCode;
  private volatile long _delayMs;
  private volatile long _throttleMs;
  private volatile long _maxWaitMs;
  private volatile long _maxRequestMs;
  private volatile long _maxIdleTrackerMs;
  private volatile boolean _insertHeaders;
  private volatile boolean _trackSessions;
  private volatile boolean _remotePort;
  private volatile boolean _enabled;
  private Semaphore _passes;
  private volatile int _throttledRequests;
  private volatile int _maxRequestsPerSec;
  private Queue<AsyncContext>[] _queues;
  private AsyncListener[] _listeners;
  private Scheduler _scheduler;
  
  public DoSFilter() {}
  
  public void init(FilterConfig filterConfig) throws ServletException { _queues = new Queue[getMaxPriority() + 1];
    _listeners = new AsyncListener[_queues.length];
    for (int p = 0; p < _queues.length; p++)
    {
      _queues[p] = new ConcurrentLinkedQueue();
      _listeners[p] = new DoSAsyncListener(p);
    }
    
    _rateTrackers.clear();
    
    int maxRequests = 25;
    String parameter = filterConfig.getInitParameter("maxRequestsPerSec");
    if (parameter != null)
      maxRequests = Integer.parseInt(parameter);
    setMaxRequestsPerSec(maxRequests);
    
    long delay = 100L;
    parameter = filterConfig.getInitParameter("delayMs");
    if (parameter != null)
      delay = Long.parseLong(parameter);
    setDelayMs(delay);
    
    int throttledRequests = 5;
    parameter = filterConfig.getInitParameter("throttledRequests");
    if (parameter != null)
      throttledRequests = Integer.parseInt(parameter);
    setThrottledRequests(throttledRequests);
    
    long maxWait = 50L;
    parameter = filterConfig.getInitParameter("maxWaitMs");
    if (parameter != null)
      maxWait = Long.parseLong(parameter);
    setMaxWaitMs(maxWait);
    
    long throttle = 30000L;
    parameter = filterConfig.getInitParameter("throttleMs");
    if (parameter != null)
      throttle = Long.parseLong(parameter);
    setThrottleMs(throttle);
    
    long maxRequestMs = 30000L;
    parameter = filterConfig.getInitParameter("maxRequestMs");
    if (parameter != null)
      maxRequestMs = Long.parseLong(parameter);
    setMaxRequestMs(maxRequestMs);
    
    long maxIdleTrackerMs = 30000L;
    parameter = filterConfig.getInitParameter("maxIdleTrackerMs");
    if (parameter != null)
      maxIdleTrackerMs = Long.parseLong(parameter);
    setMaxIdleTrackerMs(maxIdleTrackerMs);
    
    String whiteList = "";
    parameter = filterConfig.getInitParameter("ipWhitelist");
    if (parameter != null)
      whiteList = parameter;
    setWhitelist(whiteList);
    
    parameter = filterConfig.getInitParameter("insertHeaders");
    setInsertHeaders((parameter == null) || (Boolean.parseBoolean(parameter)));
    
    parameter = filterConfig.getInitParameter("trackSessions");
    setTrackSessions((parameter == null) || (Boolean.parseBoolean(parameter)));
    
    parameter = filterConfig.getInitParameter("remotePort");
    setRemotePort((parameter != null) && (Boolean.parseBoolean(parameter)));
    
    parameter = filterConfig.getInitParameter("enabled");
    setEnabled((parameter == null) || (Boolean.parseBoolean(parameter)));
    
    parameter = filterConfig.getInitParameter("tooManyCode");
    setTooManyCode(parameter == null ? 429 : Integer.parseInt(parameter));
    
    _scheduler = startScheduler();
    
    ServletContext context = filterConfig.getServletContext();
    if ((context != null) && (Boolean.parseBoolean(filterConfig.getInitParameter("managedAttr")))) {
      context.setAttribute(filterConfig.getFilterName(), this);
    }
  }
  
  protected Scheduler startScheduler() throws ServletException
  {
    try {
      Scheduler result = new ScheduledExecutorScheduler();
      result.start();
      return result;
    }
    catch (Exception x)
    {
      throw new ServletException(x);
    }
  }
  
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
  {
    doFilter((HttpServletRequest)request, (HttpServletResponse)response, filterChain);
  }
  
  protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
  {
    if (!isEnabled())
    {
      filterChain.doFilter(request, response);
      return;
    }
    

    RateTracker tracker = (RateTracker)request.getAttribute("DoSFilter.Tracker");
    if (tracker == null)
    {

      if (LOG.isDebugEnabled()) {
        LOG.debug("Filtering {}", new Object[] { request });
      }
      
      tracker = getRateTracker(request);
      

      boolean overRateLimit = tracker.isRateExceeded(System.currentTimeMillis());
      

      if (!overRateLimit)
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Allowing {}", new Object[] { request });
        doFilterChain(filterChain, request, response);
        return;
      }
      



      long delayMs = getDelayMs();
      boolean insertHeaders = isInsertHeaders();
      switch ((int)delayMs)
      {


      case -1: 
        LOG.warn("DOS ALERT: Request rejected ip={}, session={}, user={}", new Object[] { request.getRemoteAddr(), request.getRequestedSessionId(), request.getUserPrincipal() });
        if (insertHeaders)
          response.addHeader("DoSFilter", "unavailable");
        response.sendError(getTooManyCode());
        return;
      


      case 0: 
        LOG.warn("DOS ALERT: Request throttled ip={}, session={}, user={}", new Object[] { request.getRemoteAddr(), request.getRequestedSessionId(), request.getUserPrincipal() });
        request.setAttribute("DoSFilter.Tracker", tracker);
        break;
      



      default: 
        LOG.warn("DOS ALERT: Request delayed={}ms, ip={}, session={}, user={}", new Object[] { Long.valueOf(delayMs), request.getRemoteAddr(), request.getRequestedSessionId(), request.getUserPrincipal() });
        if (insertHeaders)
          response.addHeader("DoSFilter", "delayed");
        request.setAttribute("DoSFilter.Tracker", tracker);
        AsyncContext asyncContext = request.startAsync();
        if (delayMs > 0L)
          asyncContext.setTimeout(delayMs);
        asyncContext.addListener(new DoSTimeoutAsyncListener(null));
        return;
      }
      
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("Throttling {}", new Object[] { request });
    }
    
    boolean accepted = false;
    
    try
    {
      accepted = _passes.tryAcquire(getMaxWaitMs(), TimeUnit.MILLISECONDS);
      if (!accepted)
      {


        Boolean throttled = (Boolean)request.getAttribute("DoSFilter.Throttled");
        long throttleMs = getThrottleMs();
        if ((throttled != Boolean.TRUE) && (throttleMs > 0L))
        {
          int priority = getPriority(request, tracker);
          request.setAttribute("DoSFilter.Throttled", Boolean.TRUE);
          if (isInsertHeaders())
            response.addHeader("DoSFilter", "throttled");
          AsyncContext asyncContext = request.startAsync();
          request.setAttribute(_suspended, Boolean.TRUE);
          if (throttleMs > 0L)
            asyncContext.setTimeout(throttleMs);
          asyncContext.addListener(_listeners[priority]);
          _queues[priority].add(asyncContext);
          if (LOG.isDebugEnabled())
            LOG.debug("Throttled {}, {}ms", new Object[] { request, Long.valueOf(throttleMs) });
          int p; AsyncContext asyncContext; ServletRequest candidate; Boolean suspended; return;
        }
        
        Boolean resumed = (Boolean)request.getAttribute(_resumed);
        if (resumed == Boolean.TRUE)
        {

          _passes.acquire();
          accepted = true;
        }
      }
      

      if (accepted)
      {

        if (LOG.isDebugEnabled())
          LOG.debug("Allowing {}", new Object[] { request });
        doFilterChain(filterChain, request, response);

      }
      else
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Rejecting {}", new Object[] { request });
        if (isInsertHeaders())
          response.addHeader("DoSFilter", "unavailable");
        response.sendError(getTooManyCode());
      } } catch (InterruptedException e) { int p;
      AsyncContext asyncContext;
      ServletRequest candidate;
      Boolean suspended;
      LOG.ignore(e);
      response.sendError(getTooManyCode()); } finally { int p;
      AsyncContext asyncContext;
      ServletRequest candidate;
      Boolean suspended;
      if (accepted)
      {
        try
        {

          for (int p = _queues.length - 1; p >= 0; p--)
          {
            AsyncContext asyncContext = (AsyncContext)_queues[p].poll();
            if (asyncContext != null)
            {
              ServletRequest candidate = asyncContext.getRequest();
              Boolean suspended = (Boolean)candidate.getAttribute(_suspended);
              if (suspended == Boolean.TRUE)
              {
                if (LOG.isDebugEnabled())
                  LOG.debug("Resuming {}", new Object[] { request });
                candidate.setAttribute(_resumed, Boolean.TRUE);
                asyncContext.dispatch();
                break;
              }
            }
          }
        }
        finally
        {
          _passes.release();
        }
      }
    }
  }
  
  protected void doFilterChain(FilterChain chain, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException
  {
    final Thread thread = Thread.currentThread();
    Runnable requestTimeout = new Runnable()
    {

      public void run()
      {
        closeConnection(request, response, thread);
      }
    };
    Scheduler.Task task = _scheduler.schedule(requestTimeout, getMaxRequestMs(), TimeUnit.MILLISECONDS);
    try
    {
      chain.doFilter(request, response);
    }
    finally
    {
      task.cancel();
    }
  }
  









  protected void onRequestTimeout(HttpServletRequest request, HttpServletResponse response, Thread handlingThread)
  {
    try
    {
      if (LOG.isDebugEnabled())
        LOG.debug("Timing out {}", new Object[] { request });
      response.sendError(503);
    }
    catch (Throwable x)
    {
      LOG.info(x);
    }
    
    handlingThread.interrupt();
  }
  



  @Deprecated
  protected void closeConnection(HttpServletRequest request, HttpServletResponse response, Thread thread)
  {
    onRequestTimeout(request, response, thread);
  }
  







  protected int getPriority(HttpServletRequest request, RateTracker tracker)
  {
    if (extractUserId(request) != null)
      return 2;
    if (tracker != null)
      return tracker.getType();
    return 0;
  }
  



  protected int getMaxPriority()
  {
    return 2;
  }
  
















  public RateTracker getRateTracker(ServletRequest request)
  {
    HttpSession session = ((HttpServletRequest)request).getSession(false);
    
    String loadId = extractUserId(request);
    int type;
    int type; if (loadId != null)
    {
      type = 2;
    }
    else {
      int type;
      if ((isTrackSessions()) && (session != null) && (!session.isNew()))
      {
        loadId = session.getId();
        type = 2;
      }
      else
      {
        loadId = isRemotePort() ? request.getRemoteAddr() + request.getRemotePort() : request.getRemoteAddr();
        type = 1;
      }
    }
    
    RateTracker tracker = (RateTracker)_rateTrackers.get(loadId);
    
    if (tracker == null)
    {
      boolean allowed = checkWhitelist(request.getRemoteAddr());
      int maxRequestsPerSec = getMaxRequestsPerSec();
      tracker = allowed ? new FixedRateTracker(loadId, type, maxRequestsPerSec) : new RateTracker(loadId, type, maxRequestsPerSec);
      
      RateTracker existing = (RateTracker)_rateTrackers.putIfAbsent(loadId, tracker);
      if (existing != null) {
        tracker = existing;
      }
      if (type == 1)
      {

        _scheduler.schedule(tracker, getMaxIdleTrackerMs(), TimeUnit.MILLISECONDS);
      }
      else if (session != null)
      {

        session.setAttribute("DoSFilter.Tracker", tracker);
      }
    }
    
    return tracker;
  }
  
  protected boolean checkWhitelist(String candidate)
  {
    for (String address : _whitelist)
    {
      if (address.contains("/"))
      {
        if (subnetMatch(address, candidate)) {
          return true;
        }
        
      }
      else if (address.equals(candidate)) {
        return true;
      }
    }
    return false;
  }
  
  @Deprecated
  protected boolean checkWhitelist(List<String> whitelist, String candidate)
  {
    for (String address : whitelist)
    {
      if (address.contains("/"))
      {
        if (subnetMatch(address, candidate)) {
          return true;
        }
        
      }
      else if (address.equals(candidate)) {
        return true;
      }
    }
    return false;
  }
  
  protected boolean subnetMatch(String subnetAddress, String address)
  {
    Matcher cidrMatcher = CIDR_PATTERN.matcher(subnetAddress);
    if (!cidrMatcher.matches()) {
      return false;
    }
    String subnet = cidrMatcher.group(1);
    
    try
    {
      prefix = Integer.parseInt(cidrMatcher.group(2));
    }
    catch (NumberFormatException x) {
      int prefix;
      LOG.info("Ignoring malformed CIDR address {}", new Object[] { subnetAddress });
      return false;
    }
    int prefix;
    byte[] subnetBytes = addressToBytes(subnet);
    if (subnetBytes == null)
    {
      LOG.info("Ignoring malformed CIDR address {}", new Object[] { subnetAddress });
      return false;
    }
    byte[] addressBytes = addressToBytes(address);
    if (addressBytes == null)
    {
      LOG.info("Ignoring malformed remote address {}", new Object[] { address });
      return false;
    }
    

    int length = subnetBytes.length;
    if (length != addressBytes.length) {
      return false;
    }
    byte[] mask = prefixToBytes(prefix, length);
    
    for (int i = 0; i < length; i++)
    {
      if ((subnetBytes[i] & mask[i]) != (addressBytes[i] & mask[i])) {
        return false;
      }
    }
    return true;
  }
  
  private byte[] addressToBytes(String address)
  {
    Matcher ipv4Matcher = IPv4_PATTERN.matcher(address);
    if (ipv4Matcher.matches())
    {
      byte[] result = new byte[4];
      for (int i = 0; i < result.length; i++)
        result[i] = Integer.valueOf(ipv4Matcher.group(i + 1)).byteValue();
      return result;
    }
    

    Matcher ipv6Matcher = IPv6_PATTERN.matcher(address);
    if (ipv6Matcher.matches())
    {
      byte[] result = new byte[16];
      for (int i = 0; i < result.length; i += 2)
      {
        int word = Integer.valueOf(ipv6Matcher.group(i / 2 + 1), 16).intValue();
        result[i] = ((byte)((word & 0xFF00) >>> 8));
        result[(i + 1)] = ((byte)(word & 0xFF));
      }
      return result;
    }
    
    return null;
  }
  
  private byte[] prefixToBytes(int prefix, int length)
  {
    byte[] result = new byte[length];
    int index = 0;
    while (prefix / 8 > 0)
    {
      result[index] = -1;
      prefix -= 8;
      index++;
    }
    
    if (index == result.length) {
      return result;
    }
    
    result[index] = ((byte)((1 << 8 - prefix) - 1 ^ 0xFFFFFFFF));
    return result;
  }
  
  public void destroy()
  {
    LOG.debug("Destroy {}", new Object[] { this });
    stopScheduler();
    _rateTrackers.clear();
    _whitelist.clear();
  }
  
  protected void stopScheduler()
  {
    try
    {
      _scheduler.stop();
    }
    catch (Exception x)
    {
      LOG.ignore(x);
    }
  }
  







  protected String extractUserId(ServletRequest request)
  {
    return null;
  }
  







  @ManagedAttribute("maximum number of requests allowed from a connection per second")
  public int getMaxRequestsPerSec()
  {
    return _maxRequestsPerSec;
  }
  







  public void setMaxRequestsPerSec(int value)
  {
    _maxRequestsPerSec = value;
  }
  





  @ManagedAttribute("delay applied to all requests over the rate limit (in ms)")
  public long getDelayMs()
  {
    return _delayMs;
  }
  






  public void setDelayMs(long value)
  {
    _delayMs = value;
  }
  






  @ManagedAttribute("maximum time the filter will block waiting throttled connections, (0 for no delay, -1 to reject requests)")
  public long getMaxWaitMs()
  {
    return _maxWaitMs;
  }
  






  public void setMaxWaitMs(long value)
  {
    _maxWaitMs = value;
  }
  






  @ManagedAttribute("number of requests over rate limit")
  public int getThrottledRequests()
  {
    return _throttledRequests;
  }
  






  public void setThrottledRequests(int value)
  {
    int permits = _passes == null ? 0 : _passes.availablePermits();
    _passes = new Semaphore(value - _throttledRequests + permits, true);
    _throttledRequests = value;
  }
  





  @ManagedAttribute("amount of time to async wait for semaphore")
  public long getThrottleMs()
  {
    return _throttleMs;
  }
  





  public void setThrottleMs(long value)
  {
    _throttleMs = value;
  }
  






  @ManagedAttribute("maximum time to allow requests to process (in ms)")
  public long getMaxRequestMs()
  {
    return _maxRequestMs;
  }
  






  public void setMaxRequestMs(long value)
  {
    _maxRequestMs = value;
  }
  







  @ManagedAttribute("maximum time to track of request rates for connection before discarding")
  public long getMaxIdleTrackerMs()
  {
    return _maxIdleTrackerMs;
  }
  







  public void setMaxIdleTrackerMs(long value)
  {
    _maxIdleTrackerMs = value;
  }
  





  @ManagedAttribute("inser DoSFilter headers in response")
  public boolean isInsertHeaders()
  {
    return _insertHeaders;
  }
  





  public void setInsertHeaders(boolean value)
  {
    _insertHeaders = value;
  }
  





  @ManagedAttribute("usage rate is tracked by session if one exists")
  public boolean isTrackSessions()
  {
    return _trackSessions;
  }
  





  public void setTrackSessions(boolean value)
  {
    _trackSessions = value;
  }
  






  @ManagedAttribute("usage rate is tracked by IP+port is session tracking not used")
  public boolean isRemotePort()
  {
    return _remotePort;
  }
  






  public void setRemotePort(boolean value)
  {
    _remotePort = value;
  }
  



  @ManagedAttribute("whether this filter is enabled")
  public boolean isEnabled()
  {
    return _enabled;
  }
  



  public void setEnabled(boolean enabled)
  {
    _enabled = enabled;
  }
  
  public int getTooManyCode()
  {
    return _tooManyCode;
  }
  
  public void setTooManyCode(int tooManyCode)
  {
    _tooManyCode = tooManyCode;
  }
  





  @ManagedAttribute("list of IPs that will not be rate limited")
  public String getWhitelist()
  {
    StringBuilder result = new StringBuilder();
    for (Iterator<String> iterator = _whitelist.iterator(); iterator.hasNext();)
    {
      String address = (String)iterator.next();
      result.append(address);
      if (iterator.hasNext())
        result.append(",");
    }
    return result.toString();
  }
  





  public void setWhitelist(String commaSeparatedList)
  {
    List<String> result = new ArrayList();
    for (String address : StringUtil.csvSplit(commaSeparatedList))
      addWhitelistAddress(result, address);
    clearWhitelist();
    _whitelist.addAll(result);
    LOG.debug("Whitelisted IP addresses: {}", new Object[] { result });
  }
  



  @ManagedOperation("clears the list of IP addresses that will not be rate limited")
  public void clearWhitelist()
  {
    _whitelist.clear();
  }
  








  @ManagedOperation("adds an IP address that will not be rate limited")
  public boolean addWhitelistAddress(@Name("address") String address)
  {
    return addWhitelistAddress(_whitelist, address);
  }
  
  private boolean addWhitelistAddress(List<String> list, String address)
  {
    address = address.trim();
    return (address.length() > 0) && (list.add(address));
  }
  







  @ManagedOperation("removes an IP address that will not be rate limited")
  public boolean removeWhitelistAddress(@Name("address") String address)
  {
    return _whitelist.remove(address);
  }
  

  class RateTracker
    implements Runnable, HttpSessionBindingListener, HttpSessionActivationListener, Serializable
  {
    private static final long serialVersionUID = 3534663738034577872L;
    
    protected final String _id;
    
    protected final int _type;
    
    protected final long[] _timestamps;
    protected int _next;
    
    public RateTracker(String id, int type, int maxRequestsPerSecond)
    {
      _id = id;
      _type = type;
      _timestamps = new long[maxRequestsPerSecond];
      _next = 0;
    }
    





    public boolean isRateExceeded(long now)
    {
      synchronized (this)
      {
        long last = _timestamps[_next];
        _timestamps[_next] = now;
        _next = ((_next + 1) % _timestamps.length);
      }
      long last;
      return (last != 0L) && (now - last < 1000L);
    }
    
    public String getId()
    {
      return _id;
    }
    
    public int getType()
    {
      return _type;
    }
    
    public void valueBound(HttpSessionBindingEvent event)
    {
      if (DoSFilter.LOG.isDebugEnabled()) {
        DoSFilter.LOG.debug("Value bound: {}", new Object[] { getId() });
      }
    }
    
    public void valueUnbound(HttpSessionBindingEvent event)
    {
      _rateTrackers.remove(_id);
      if (DoSFilter.LOG.isDebugEnabled()) {
        DoSFilter.LOG.debug("Tracker removed: {}", new Object[] { getId() });
      }
    }
    
    public void sessionWillPassivate(HttpSessionEvent se)
    {
      _rateTrackers.remove(_id);
    }
    
    public void sessionDidActivate(HttpSessionEvent se)
    {
      RateTracker tracker = (RateTracker)se.getSession().getAttribute("DoSFilter.Tracker");
      if (tracker != null) {
        _rateTrackers.put(tracker.getId(), tracker);
      }
    }
    
    public void run()
    {
      int latestIndex = _next == 0 ? _timestamps.length - 1 : _next - 1;
      long last = _timestamps[latestIndex];
      boolean hasRecentRequest = (last != 0L) && (System.currentTimeMillis() - last < 1000L);
      
      if (hasRecentRequest) {
        _scheduler.schedule(this, getMaxIdleTrackerMs(), TimeUnit.MILLISECONDS);
      } else {
        _rateTrackers.remove(_id);
      }
    }
    
    public String toString()
    {
      return "RateTracker/" + _id + "/" + _type;
    }
  }
  
  class FixedRateTracker extends DoSFilter.RateTracker
  {
    public FixedRateTracker(String id, int type, int numRecentRequestsTracked)
    {
      super(id, type, numRecentRequestsTracked);
    }
    




    public boolean isRateExceeded(long now)
    {
      synchronized (this)
      {
        _timestamps[_next] = now;
        _next = ((_next + 1) % _timestamps.length);
      }
      
      return false;
    }
    

    public String toString()
    {
      return "Fixed" + super.toString();
    }
  }
  
  private class DoSTimeoutAsyncListener
    implements AsyncListener
  {
    private DoSTimeoutAsyncListener() {}
    
    public void onStartAsync(AsyncEvent event)
      throws IOException
    {}
    
    public void onComplete(AsyncEvent event)
      throws IOException
    {}
    
    public void onTimeout(AsyncEvent event) throws IOException
    {
      event.getAsyncContext().dispatch();
    }
    
    public void onError(AsyncEvent event) throws IOException
    {}
  }
  
  private class DoSAsyncListener
    extends DoSFilter.DoSTimeoutAsyncListener
  {
    private final int priority;
    
    public DoSAsyncListener(int priority)
    {
      super(null);
      this.priority = priority;
    }
    
    public void onTimeout(AsyncEvent event)
      throws IOException
    {
      _queues[priority].remove(event.getAsyncContext());
      super.onTimeout(event);
    }
  }
}
