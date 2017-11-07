package org.seleniumhq.jetty9.server.session;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;
import org.seleniumhq.jetty9.http.HttpCookie;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Response;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.server.SessionIdManager;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;
import org.seleniumhq.jetty9.server.handler.ScopedHandler;
import org.seleniumhq.jetty9.util.ConcurrentHashSet;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.statistic.CounterStatistic;
import org.seleniumhq.jetty9.util.statistic.SampleStatistic;
import org.seleniumhq.jetty9.util.thread.Locker.Lock;
import org.seleniumhq.jetty9.util.thread.ScheduledExecutorScheduler;
import org.seleniumhq.jetty9.util.thread.Scheduler;






















public class SessionHandler
  extends ScopedHandler
{
  static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  
  public static final EnumSet<SessionTrackingMode> DEFAULT_TRACKING = EnumSet.of(SessionTrackingMode.COOKIE, SessionTrackingMode.URL);
  



  public static final String __SessionCookieProperty = "org.seleniumhq.jetty9.servlet.SessionCookie";
  



  public static final String __DefaultSessionCookie = "JSESSIONID";
  



  public static final String __SessionIdPathParameterNameProperty = "org.seleniumhq.jetty9.servlet.SessionIdPathParameterName";
  


  public static final String __DefaultSessionIdPathParameterName = "jsessionid";
  


  public static final String __CheckRemoteSessionEncoding = "org.seleniumhq.jetty9.servlet.CheckingRemoteSessionIdEncoding";
  


  public static final String __SessionDomainProperty = "org.seleniumhq.jetty9.servlet.SessionDomain";
  


  public static final String __DefaultSessionDomain = null;
  






  public static final String __SessionPathProperty = "org.seleniumhq.jetty9.servlet.SessionPath";
  





  public static final String __MaxAgeProperty = "org.seleniumhq.jetty9.servlet.MaxAge";
  





  public Set<SessionTrackingMode> __defaultSessionTrackingModes = Collections.unmodifiableSet(new HashSet(
  
    Arrays.asList(new SessionTrackingMode[] { SessionTrackingMode.COOKIE, SessionTrackingMode.URL })));
  



  public static final Class<? extends EventListener>[] SESSION_LISTENER_TYPES = { HttpSessionAttributeListener.class, HttpSessionIdListener.class, HttpSessionListener.class };
  









  public static final BigDecimal MAX_INACTIVE_MINUTES = new BigDecimal(35791394);
  



  public class SessionAsyncListener
    implements AsyncListener
  {
    private Session _session;
    



    public SessionAsyncListener(Session session)
    {
      _session = session;
    }
    

    public void onComplete(AsyncEvent event)
      throws IOException
    {
      complete(((HttpServletRequest)event.getAsyncContext().getRequest()).getSession(false));
    }
    

    public void onTimeout(AsyncEvent event)
      throws IOException
    {}
    

    public void onError(AsyncEvent event)
      throws IOException
    {
      complete(((HttpServletRequest)event.getAsyncContext().getRequest()).getSession(false));
    }
    
    public void onStartAsync(AsyncEvent event)
      throws IOException
    {
      event.getAsyncContext().addListener(this);
    }
  }
  

  static final HttpSessionContext __nullSessionContext = new HttpSessionContext()
  {

    public HttpSession getSession(String sessionId)
    {
      return null;
    }
    


    public Enumeration getIds()
    {
      return Collections.enumeration(Collections.EMPTY_LIST);
    }
  };
  







  protected int _dftMaxIdleSecs = -1;
  protected boolean _httpOnly = false;
  protected SessionIdManager _sessionIdManager;
  protected boolean _secureCookies = false;
  protected boolean _secureRequestOnly = true;
  
  protected final List<HttpSessionAttributeListener> _sessionAttributeListeners = new CopyOnWriteArrayList();
  protected final List<HttpSessionListener> _sessionListeners = new CopyOnWriteArrayList();
  protected final List<HttpSessionIdListener> _sessionIdListeners = new CopyOnWriteArrayList();
  
  protected ClassLoader _loader;
  protected ContextHandler.Context _context;
  protected SessionContext _sessionContext;
  protected String _sessionCookie = "JSESSIONID";
  protected String _sessionIdPathParameterName = "jsessionid";
  protected String _sessionIdPathParameterNamePrefix = ";" + _sessionIdPathParameterName + "=";
  protected String _sessionDomain;
  protected String _sessionPath;
  protected int _maxCookieAge = -1;
  protected int _refreshCookieAge;
  protected boolean _nodeIdInSessionId;
  protected boolean _checkingRemoteSessionIdEncoding;
  protected String _sessionComment;
  protected SessionCache _sessionCache;
  protected final SampleStatistic _sessionTimeStats = new SampleStatistic();
  protected final CounterStatistic _sessionsCreatedStats = new CounterStatistic();
  
  public Set<SessionTrackingMode> _sessionTrackingModes;
  protected boolean _usingURLs;
  protected boolean _usingCookies = true;
  
  protected ConcurrentHashSet<String> _candidateSessionIdsForExpiry = new ConcurrentHashSet();
  
  protected Scheduler _scheduler;
  protected boolean _ownScheduler = false;
  








  public SessionHandler()
  {
    setSessionTrackingModes(__defaultSessionTrackingModes);
  }
  


  @ManagedAttribute("path of the session cookie, or null for default")
  public String getSessionPath()
  {
    return _sessionPath;
  }
  


  @ManagedAttribute("if greater the zero, the time in seconds a session cookie will last for")
  public int getMaxCookieAge()
  {
    return _maxCookieAge;
  }
  










  public HttpCookie access(HttpSession session, boolean secure)
  {
    long now = System.currentTimeMillis();
    
    Session s = ((SessionIf)session).getSession();
    
    if (s.access(now))
    {

      if ((isUsingCookies()) && (
        (s.isIdChanged()) || (
        (getSessionCookieConfig().getMaxAge() > 0) && (getRefreshCookieAge() > 0) && ((now - s.getCookieSetTime()) / 1000L > getRefreshCookieAge()))))
      {


        HttpCookie cookie = getSessionCookie(session, _context == null ? "/" : _context.getContextPath(), secure);
        s.cookieSet();
        s.setIdChanged(false);
        return cookie;
      }
    }
    return null;
  }
  










  public void addEventListener(EventListener listener)
  {
    if ((listener instanceof HttpSessionAttributeListener))
      _sessionAttributeListeners.add((HttpSessionAttributeListener)listener);
    if ((listener instanceof HttpSessionListener))
      _sessionListeners.add((HttpSessionListener)listener);
    if ((listener instanceof HttpSessionIdListener))
      _sessionIdListeners.add((HttpSessionIdListener)listener);
    addBean(listener, false);
  }
  






  public void clearEventListeners()
  {
    for (EventListener e : getBeans(EventListener.class))
      removeBean(e);
    _sessionAttributeListeners.clear();
    _sessionListeners.clear();
    _sessionIdListeners.clear();
  }
  







  public void complete(HttpSession session)
  {
    if (session == null) {
      return;
    }
    Session s = ((SessionIf)session).getSession();
    
    try
    {
      s.complete();
      _sessionCache.put(s.getId(), s);
    }
    catch (Exception e)
    {
      LOG.warn(e);
    }
  }
  

  public void complete(Session session, Request request)
  {
    if ((request.isAsyncStarted()) && (request.getDispatcherType() == DispatcherType.REQUEST))
    {
      request.getAsyncContext().addListener(new SessionAsyncListener(session));
    }
    else
    {
      complete(session);
    }
  }
  











  protected void doStart()
    throws Exception
  {
    Server server = getServer();
    
    _context = ContextHandler.getCurrentContext();
    _loader = Thread.currentThread().getContextClassLoader();
    

    synchronized (server)
    {

      if (_sessionCache == null)
      {
        SessionCacheFactory ssFactory = (SessionCacheFactory)server.getBean(SessionCacheFactory.class);
        setSessionCache(ssFactory != null ? ssFactory.getSessionCache(this) : new DefaultSessionCache(this));
        SessionDataStore sds = null;
        SessionDataStoreFactory sdsFactory = (SessionDataStoreFactory)server.getBean(SessionDataStoreFactory.class);
        if (sdsFactory != null) {
          sds = sdsFactory.getSessionDataStore(this);
        } else {
          sds = new NullSessionDataStore();
        }
        _sessionCache.setSessionDataStore(sds);
      }
      

      if (_sessionIdManager == null)
      {
        _sessionIdManager = server.getSessionIdManager();
        if (_sessionIdManager == null)
        {




          ClassLoader serverLoader = server.getClass().getClassLoader();
          try
          {
            Thread.currentThread().setContextClassLoader(serverLoader);
            _sessionIdManager = new DefaultSessionIdManager(server);
            server.setSessionIdManager(_sessionIdManager);
            server.manage(_sessionIdManager);
            _sessionIdManager.start();
          }
          finally
          {
            Thread.currentThread().setContextClassLoader(_loader);
          }
        }
        

        addBean(_sessionIdManager, false);
      }
      
      _scheduler = ((Scheduler)server.getBean(Scheduler.class));
      if (_scheduler == null)
      {
        _scheduler = new ScheduledExecutorScheduler();
        _ownScheduler = true;
        _scheduler.start();
      }
    }
    



    if (_context != null)
    {
      String tmp = _context.getInitParameter("org.seleniumhq.jetty9.servlet.SessionCookie");
      if (tmp != null) {
        _sessionCookie = tmp;
      }
      tmp = _context.getInitParameter("org.seleniumhq.jetty9.servlet.SessionIdPathParameterName");
      if (tmp != null) {
        setSessionIdPathParameterName(tmp);
      }
      
      if (_maxCookieAge == -1)
      {
        tmp = _context.getInitParameter("org.seleniumhq.jetty9.servlet.MaxAge");
        if (tmp != null) {
          _maxCookieAge = Integer.parseInt(tmp.trim());
        }
      }
      
      if (_sessionDomain == null) {
        _sessionDomain = _context.getInitParameter("org.seleniumhq.jetty9.servlet.SessionDomain");
      }
      
      if (_sessionPath == null) {
        _sessionPath = _context.getInitParameter("org.seleniumhq.jetty9.servlet.SessionPath");
      }
      tmp = _context.getInitParameter("org.seleniumhq.jetty9.servlet.CheckingRemoteSessionIdEncoding");
      if (tmp != null) {
        _checkingRemoteSessionIdEncoding = Boolean.parseBoolean(tmp);
      }
    }
    _sessionContext = new SessionContext(_sessionIdManager.getWorkerName(), _context);
    _sessionCache.initialize(_sessionContext);
    super.doStart();
  }
  





  protected void doStop()
    throws Exception
  {
    shutdownSessions();
    _sessionCache.stop();
    if ((_ownScheduler) && (_scheduler != null))
      _scheduler.stop();
    _scheduler = null;
    super.doStop();
    _loader = null;
  }
  





  @ManagedAttribute("true if cookies use the http only flag")
  public boolean getHttpOnly()
  {
    return _httpOnly;
  }
  







  public HttpSession getHttpSession(String extendedId)
  {
    String id = getSessionIdManager().getId(extendedId);
    
    Session session = getSession(id);
    if ((session != null) && (!session.getExtendedId().equals(extendedId)))
      session.setIdChanged(true);
    return session;
  }
  






  @ManagedAttribute("Session ID Manager")
  public SessionIdManager getSessionIdManager()
  {
    return _sessionIdManager;
  }
  







  @ManagedAttribute("default maximum time a session may be idle for (in s)")
  public int getMaxInactiveInterval()
  {
    return _dftMaxIdleSecs;
  }
  



  @ManagedAttribute("time before a session cookie is re-set (in s)")
  public int getRefreshCookieAge()
  {
    return _refreshCookieAge;
  }
  






  @ManagedAttribute("if true, secure cookie flag is set on session cookies")
  public boolean getSecureCookies()
  {
    return _secureCookies;
  }
  




  public boolean isSecureRequestOnly()
  {
    return _secureRequestOnly;
  }
  







  public void setSecureRequestOnly(boolean secureRequestOnly)
  {
    _secureRequestOnly = secureRequestOnly;
  }
  

  @ManagedAttribute("the set session cookie")
  public String getSessionCookie()
  {
    return _sessionCookie;
  }
  


































  public HttpCookie getSessionCookie(HttpSession session, String contextPath, boolean requestIsSecure)
  {
    if (isUsingCookies())
    {
      String sessionPath = _cookieConfig.getPath() == null ? contextPath : _cookieConfig.getPath();
      sessionPath = (sessionPath == null) || (sessionPath.length() == 0) ? "/" : sessionPath;
      String id = getExtendedId(session);
      HttpCookie cookie = null;
      if (_sessionComment == null)
      {







        cookie = new HttpCookie(_cookieConfig.getName(), id, _cookieConfig.getDomain(), sessionPath, _cookieConfig.getMaxAge(), _cookieConfig.isHttpOnly(), (_cookieConfig.isSecure()) || ((isSecureRequestOnly()) && (requestIsSecure)));




      }
      else
      {



        cookie = new HttpCookie(_cookieConfig.getName(), id, _cookieConfig.getDomain(), sessionPath, _cookieConfig.getMaxAge(), _cookieConfig.isHttpOnly(), (_cookieConfig.isSecure()) || ((isSecureRequestOnly()) && (requestIsSecure)), _sessionComment, 1);
      }
      


      return cookie;
    }
    return null;
  }
  





  @ManagedAttribute("domain of the session cookie, or null for the default")
  public String getSessionDomain()
  {
    return _sessionDomain;
  }
  



  @ManagedAttribute("number of sessions created by this node")
  public int getSessionsCreated()
  {
    return (int)_sessionsCreatedStats.getCurrent();
  }
  





  @ManagedAttribute("name of use for URL session tracking")
  public String getSessionIdPathParameterName()
  {
    return _sessionIdPathParameterName;
  }
  






  public String getSessionIdPathParameterNamePrefix()
  {
    return _sessionIdPathParameterNamePrefix;
  }
  




  public boolean isUsingCookies()
  {
    return _usingCookies;
  }
  





  public boolean isValid(HttpSession session)
  {
    Session s = ((SessionIf)session).getSession();
    return s.isValid();
  }
  






  public String getId(HttpSession session)
  {
    Session s = ((SessionIf)session).getSession();
    return s.getId();
  }
  






  public String getExtendedId(HttpSession session)
  {
    Session s = ((SessionIf)session).getSession();
    return s.getExtendedId();
  }
  







  public HttpSession newHttpSession(HttpServletRequest request)
  {
    long created = System.currentTimeMillis();
    String id = _sessionIdManager.newSessionId(request, created);
    Session session = _sessionCache.newSession(request, id, created, _dftMaxIdleSecs > 0 ? _dftMaxIdleSecs * 1000L : -1L);
    session.setExtendedId(_sessionIdManager.getExtendedId(id, request));
    session.getSessionData().setLastNode(_sessionIdManager.getWorkerName());
    
    try
    {
      _sessionCache.put(id, session);
      _sessionsCreatedStats.increment();
      
      if (request.isSecure())
        session.setAttribute("org.seleniumhq.jetty9.security.sessionCreatedSecure", Boolean.TRUE);
      HttpSessionEvent event;
      if (_sessionListeners != null)
      {
        event = new HttpSessionEvent(session);
        for (HttpSessionListener listener : _sessionListeners) {
          listener.sessionCreated(event);
        }
      }
      return session;
    }
    catch (Exception e)
    {
      LOG.warn(e); }
    return null;
  }
  








  public void removeEventListener(EventListener listener)
  {
    if ((listener instanceof HttpSessionAttributeListener))
      _sessionAttributeListeners.remove(listener);
    if ((listener instanceof HttpSessionListener))
      _sessionListeners.remove(listener);
    if ((listener instanceof HttpSessionIdListener))
      _sessionIdListeners.remove(listener);
    removeBean(listener);
  }
  




  @ManagedOperation(value="reset statistics", impact="ACTION")
  public void statsReset()
  {
    _sessionsCreatedStats.reset();
    _sessionTimeStats.reset();
  }
  





  public void setHttpOnly(boolean httpOnly)
  {
    _httpOnly = httpOnly;
  }
  




  public void setSessionIdManager(SessionIdManager metaManager)
  {
    updateBean(_sessionIdManager, metaManager);
    _sessionIdManager = metaManager;
  }
  







  public void setMaxInactiveInterval(int seconds)
  {
    _dftMaxIdleSecs = seconds;
    if (LOG.isDebugEnabled())
    {
      if (_dftMaxIdleSecs <= 0) {
        LOG.debug("Sessions created by this manager are immortal (default maxInactiveInterval={})", _dftMaxIdleSecs);
      } else {
        LOG.debug("SessionManager default maxInactiveInterval={}", _dftMaxIdleSecs);
      }
    }
  }
  
  public void setRefreshCookieAge(int ageInSeconds)
  {
    _refreshCookieAge = ageInSeconds;
  }
  



  public void setSessionCookie(String cookieName)
  {
    _sessionCookie = cookieName;
  }
  









  public void setSessionIdPathParameterName(String param)
  {
    _sessionIdPathParameterName = ((param == null) || ("none".equals(param)) ? null : param);
    _sessionIdPathParameterNamePrefix = (";" + _sessionIdPathParameterName + "=");
  }
  




  public void setUsingCookies(boolean usingCookies)
  {
    _usingCookies = usingCookies;
  }
  








  public Session getSession(String id)
  {
    try
    {
      Session session = _sessionCache.get(id);
      if (session != null)
      {

        if (session.isExpiredAt(System.currentTimeMillis()))
        {

          try
          {
            session.invalidate();
          }
          catch (Exception e)
          {
            LOG.warn("Invalidating session {} found to be expired when requested", new Object[] { id, e });
          }
          
          return null;
        }
        
        session.setExtendedId(_sessionIdManager.getExtendedId(id, null));
      }
      
      return session;
    }
    catch (UnreadableSessionDataException e)
    {
      LOG.warn(e);
      
      try
      {
        getSessionIdManager().invalidateAll(id);
      }
      catch (Exception x)
      {
        LOG.warn("Error cross-context invalidating unreadable session {}", new Object[] { id, x });
      }
      return null;
    }
    catch (Exception other)
    {
      LOG.warn(other); }
    return null;
  }
  







  protected void shutdownSessions()
    throws Exception
  {
    _sessionCache.shutdown();
  }
  





  public SessionCache getSessionCache()
  {
    return _sessionCache;
  }
  




  public void setSessionCache(SessionCache cache)
  {
    updateBean(_sessionCache, cache);
    _sessionCache = cache;
  }
  




  public boolean isNodeIdInSessionId()
  {
    return _nodeIdInSessionId;
  }
  




  public void setNodeIdInSessionId(boolean nodeIdInSessionId)
  {
    _nodeIdInSessionId = nodeIdInSessionId;
  }
  










  public Session removeSession(String id, boolean invalidate)
  {
    try
    {
      Session session = _sessionCache.delete(id);
      if (session != null)
      {
        if (invalidate)
        {
          if (_sessionListeners != null)
          {
            HttpSessionEvent event = new HttpSessionEvent(session);
            for (int i = _sessionListeners.size() - 1; i >= 0; i--)
            {
              ((HttpSessionListener)_sessionListeners.get(i)).sessionDestroyed(event);
            }
          }
        }
      }
      


      return session;
    }
    catch (Exception e)
    {
      LOG.warn(e); }
    return null;
  }
  








  @ManagedAttribute("maximum amount of time sessions have remained active (in s)")
  public long getSessionTimeMax()
  {
    return _sessionTimeStats.getMax();
  }
  

  public Set<SessionTrackingMode> getDefaultSessionTrackingModes()
  {
    return __defaultSessionTrackingModes;
  }
  

  public Set<SessionTrackingMode> getEffectiveSessionTrackingModes()
  {
    return Collections.unmodifiableSet(_sessionTrackingModes);
  }
  

  public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes)
  {
    _sessionTrackingModes = new HashSet(sessionTrackingModes);
    _usingCookies = _sessionTrackingModes.contains(SessionTrackingMode.COOKIE);
    _usingURLs = _sessionTrackingModes.contains(SessionTrackingMode.URL);
  }
  




  public boolean isUsingURLs()
  {
    return _usingURLs;
  }
  

  public SessionCookieConfig getSessionCookieConfig()
  {
    return _cookieConfig;
  }
  

  private SessionCookieConfig _cookieConfig = new CookieConfig();
  






  @ManagedAttribute("total time sessions have remained valid")
  public long getSessionTimeTotal()
  {
    return _sessionTimeStats.getTotal();
  }
  




  @ManagedAttribute("mean time sessions remain valid (in s)")
  public double getSessionTimeMean()
  {
    return _sessionTimeStats.getMean();
  }
  




  @ManagedAttribute("standard deviation a session remained valid (in s)")
  public double getSessionTimeStdDev()
  {
    return _sessionTimeStats.getStdDev();
  }
  




  @ManagedAttribute("check remote session id encoding")
  public boolean isCheckingRemoteSessionIdEncoding()
  {
    return _checkingRemoteSessionIdEncoding;
  }
  




  public void setCheckingRemoteSessionIdEncoding(boolean remote)
  {
    _checkingRemoteSessionIdEncoding = remote;
  }
  









  public void renewSessionId(String oldId, String oldExtendedId, String newId, String newExtendedId)
  {
    try
    {
      Session session = _sessionCache.renewSessionId(oldId, newId);
      if (session == null)
      {

        return;
      }
      
      session.setExtendedId(newExtendedId);
      

      if (!_sessionIdListeners.isEmpty())
      {
        event = new HttpSessionEvent(session);
        for (HttpSessionIdListener l : _sessionIdListeners)
        {
          l.sessionIdChanged(event, oldId);
        }
      }
    }
    catch (Exception e) {
      HttpSessionEvent event;
      LOG.warn(e);
    }
  }
  









  public void invalidate(String id)
  {
    if (StringUtil.isBlank(id)) {
      return;
    }
    
    try
    {
      Session session = removeSession(id, true);
      
      if (session != null)
      {
        _sessionTimeStats.set(Math.round((System.currentTimeMillis() - session.getSessionData().getCreated()) / 1000.0D));
        session.doInvalidate();
      }
    }
    catch (Exception e)
    {
      LOG.warn(e);
    }
  }
  



  public void scavenge()
  {
    if ((isStopping()) || (isStopped())) {
      return;
    }
    if (LOG.isDebugEnabled()) { LOG.debug("Scavenging sessions", new Object[0]);
    }
    

    String[] ss = (String[])_candidateSessionIdsForExpiry.toArray(new String[0]);
    Set<String> candidates = new HashSet(Arrays.asList(ss));
    _candidateSessionIdsForExpiry.removeAll(candidates);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Scavenging session ids {}", new Object[] { candidates });
    }
    try {
      candidates = _sessionCache.checkExpiration(candidates);
      for (String id : candidates)
      {
        try
        {
          getSessionIdManager().expireAll(id);
        }
        catch (Exception e)
        {
          LOG.warn(e);
        }
      }
    }
    catch (Exception e)
    {
      LOG.warn(e);
    }
  }
  









  public void sessionInactivityTimerExpired(Session session)
  {
    if (session == null) {
      return;
    }
    




    boolean expired = false;
    Locker.Lock lock = session.lock();Throwable localThrowable4 = null;
    try {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Inspecting session {}, valid={}", new Object[] { session.getId(), Boolean.valueOf(session.isValid()) });
      }
      if (!session.isValid()) {
        return;
      }
      if ((session.isExpiredAt(System.currentTimeMillis())) && (session.getRequests() <= 0L)) {
        expired = true;
      }
    }
    catch (Throwable localThrowable2)
    {
      localThrowable4 = localThrowable2;throw localThrowable2;



    }
    finally
    {



      if (lock != null) if (localThrowable4 != null) try { lock.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else lock.close();
    }
    if (expired)
    {






      if ((_sessionIdManager.getSessionHouseKeeper() != null) && (_sessionIdManager.getSessionHouseKeeper().getIntervalSec() > 0L))
      {
        _candidateSessionIdsForExpiry.add(session.getId());
        if (LOG.isDebugEnabled()) LOG.debug("Session {} is candidate for expiry", new Object[] { session.getId() });
      }
    }
    else {
      _sessionCache.checkInactiveSession(session);
    }
  }
  











  public boolean isIdInUse(String id)
    throws Exception
  {
    return _sessionCache.exists(id);
  }
  




  public Scheduler getScheduler()
  {
    return _scheduler;
  }
  








  public static abstract interface SessionIf
    extends HttpSession
  {
    public abstract Session getSession();
  }
  







  public final class CookieConfig
    implements SessionCookieConfig
  {
    public CookieConfig() {}
    







    public String getComment()
    {
      return _sessionComment;
    }
    

    public String getDomain()
    {
      return _sessionDomain;
    }
    

    public int getMaxAge()
    {
      return _maxCookieAge;
    }
    

    public String getName()
    {
      return _sessionCookie;
    }
    

    public String getPath()
    {
      return _sessionPath;
    }
    

    public boolean isHttpOnly()
    {
      return _httpOnly;
    }
    

    public boolean isSecure()
    {
      return _secureCookies;
    }
    

    public void setComment(String comment)
    {
      if ((_context != null) && (_context.getContextHandler().isAvailable()))
        throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
      _sessionComment = comment;
    }
    

    public void setDomain(String domain)
    {
      if ((_context != null) && (_context.getContextHandler().isAvailable()))
        throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
      _sessionDomain = domain;
    }
    

    public void setHttpOnly(boolean httpOnly)
    {
      if ((_context != null) && (_context.getContextHandler().isAvailable()))
        throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
      _httpOnly = httpOnly;
    }
    

    public void setMaxAge(int maxAge)
    {
      if ((_context != null) && (_context.getContextHandler().isAvailable()))
        throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
      _maxCookieAge = maxAge;
    }
    

    public void setName(String name)
    {
      if ((_context != null) && (_context.getContextHandler().isAvailable()))
        throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
      _sessionCookie = name;
    }
    

    public void setPath(String path)
    {
      if ((_context != null) && (_context.getContextHandler().isAvailable()))
        throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
      _sessionPath = path;
    }
    

    public void setSecure(boolean secure)
    {
      if ((_context != null) && (_context.getContextHandler().isAvailable()))
        throw new IllegalStateException("CookieConfig cannot be set after ServletContext is started");
      _secureCookies = secure;
    }
  }
  
  public void doSessionAttributeListeners(Session session, String name, Object old, Object value)
  {
    HttpSessionBindingEvent event;
    if (!_sessionAttributeListeners.isEmpty())
    {
      event = new HttpSessionBindingEvent(session, name, old == null ? value : old);
      
      for (HttpSessionAttributeListener l : _sessionAttributeListeners)
      {
        if (old == null) {
          l.attributeAdded(event);
        } else if (value == null) {
          l.attributeRemoved(event);
        } else {
          l.attributeReplaced(event);
        }
      }
    }
  }
  





  public void doScope(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    SessionHandler old_session_manager = null;
    HttpSession old_session = null;
    HttpSession existingSession = null;
    
    try
    {
      old_session_manager = baseRequest.getSessionHandler();
      old_session = baseRequest.getSession(false);
      
      if (old_session_manager != this)
      {

        baseRequest.setSessionHandler(this);
        baseRequest.setSession(null);
        checkRequestedSessionId(baseRequest, request);
      }
      

      existingSession = baseRequest.getSession(false);
      
      if ((existingSession != null) && (old_session_manager != this))
      {
        HttpCookie cookie = access(existingSession, request.isSecure());
        
        if ((cookie != null) && ((request.getDispatcherType() == DispatcherType.ASYNC) || (request.getDispatcherType() == DispatcherType.REQUEST))) {
          baseRequest.getResponse().addCookie(cookie);
        }
      }
      if (LOG.isDebugEnabled())
      {
        LOG.debug("sessionHandler=" + this, new Object[0]);
        LOG.debug("session=" + existingSession, new Object[0]);
      }
      
      if (_nextScope != null) {
        _nextScope.doScope(target, baseRequest, request, response);
      } else if (_outerScope != null) {
        _outerScope.doHandle(target, baseRequest, request, response);
      } else {
        doHandle(target, baseRequest, request, response);
      }
    }
    finally {
      HttpSession finalSession;
      HttpSession finalSession = baseRequest.getSession(false);
      if (LOG.isDebugEnabled()) LOG.debug("FinalSession=" + finalSession + " old_session_manager=" + old_session_manager + " this=" + this, new Object[0]);
      if ((finalSession != null) && (old_session_manager != this))
      {
        complete((Session)finalSession, baseRequest);
      }
      
      if ((old_session_manager != null) && (old_session_manager != this))
      {
        baseRequest.setSessionHandler(old_session_manager);
        baseRequest.setSession(old_session);
      }
    }
  }
  




  public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    nextHandle(target, baseRequest, request, response);
  }
  







  protected void checkRequestedSessionId(Request baseRequest, HttpServletRequest request)
  {
    String requested_session_id = request.getRequestedSessionId();
    
    if (requested_session_id != null)
    {
      HttpSession session = getHttpSession(requested_session_id);
      
      if ((session != null) && (isValid(session)))
        baseRequest.setSession(session);
      return;
    }
    if (!DispatcherType.REQUEST.equals(baseRequest.getDispatcherType())) {
      return;
    }
    boolean requested_session_id_from_cookie = false;
    HttpSession session = null;
    

    if (isUsingCookies())
    {
      Cookie[] cookies = request.getCookies();
      if ((cookies != null) && (cookies.length > 0))
      {
        String sessionCookie = getSessionCookieConfig().getName();
        for (int i = 0; i < cookies.length; i++)
        {
          if (sessionCookie.equalsIgnoreCase(cookies[i].getName()))
          {
            requested_session_id = cookies[i].getValue();
            requested_session_id_from_cookie = true;
            
            if (LOG.isDebugEnabled()) {
              LOG.debug("Got Session ID {} from cookie", new Object[] { requested_session_id });
            }
            if (requested_session_id != null)
            {
              session = getHttpSession(requested_session_id);
              if ((session != null) && (isValid(session))) {
                break;
              }
              
            }
            else
            {
              LOG.warn("null session id from cookie", new Object[0]);
            }
          }
        }
      }
    }
    
    if ((requested_session_id == null) || (session == null))
    {
      String uri = request.getRequestURI();
      
      String prefix = getSessionIdPathParameterNamePrefix();
      if (prefix != null)
      {
        int s = uri.indexOf(prefix);
        if (s >= 0)
        {
          s += prefix.length();
          int i = s;
          while (i < uri.length())
          {
            char c = uri.charAt(i);
            if ((c == ';') || (c == '#') || (c == '?') || (c == '/'))
              break;
            i++;
          }
          
          requested_session_id = uri.substring(s, i);
          requested_session_id_from_cookie = false;
          session = getHttpSession(requested_session_id);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Got Session ID {} from URL", new Object[] { requested_session_id });
          }
        }
      }
    }
    baseRequest.setRequestedSessionId(requested_session_id);
    baseRequest.setRequestedSessionIdFromCookie((requested_session_id != null) && (requested_session_id_from_cookie));
    if ((session != null) && (isValid(session))) {
      baseRequest.setSession(session);
    }
  }
  




  public String toString()
  {
    return String.format("%s%d==dftMaxIdleSec=%d", new Object[] { getClass().getName(), Integer.valueOf(hashCode()), Integer.valueOf(_dftMaxIdleSecs) });
  }
}
