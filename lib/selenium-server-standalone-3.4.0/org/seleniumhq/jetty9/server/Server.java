package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.DateGenerator;
import org.seleniumhq.jetty9.http.HttpField;
import org.seleniumhq.jetty9.http.HttpGenerator;
import org.seleniumhq.jetty9.http.HttpHeader;
import org.seleniumhq.jetty9.http.HttpMethod;
import org.seleniumhq.jetty9.http.HttpURI;
import org.seleniumhq.jetty9.http.PreEncodedHttpField;
import org.seleniumhq.jetty9.io.SelectorManager;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ErrorHandler;
import org.seleniumhq.jetty9.server.handler.ErrorHandler.ErrorPageMapper;
import org.seleniumhq.jetty9.server.handler.HandlerWrapper;
import org.seleniumhq.jetty9.util.Attributes;
import org.seleniumhq.jetty9.util.AttributesMap;
import org.seleniumhq.jetty9.util.Jetty;
import org.seleniumhq.jetty9.util.MultiException;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.Uptime;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.Name;
import org.seleniumhq.jetty9.util.component.Graceful;
import org.seleniumhq.jetty9.util.component.LifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Locker;
import org.seleniumhq.jetty9.util.thread.Locker.Lock;
import org.seleniumhq.jetty9.util.thread.QueuedThreadPool;
import org.seleniumhq.jetty9.util.thread.ShutdownThread;
import org.seleniumhq.jetty9.util.thread.ThreadPool;
import org.seleniumhq.jetty9.util.thread.ThreadPool.SizedThreadPool;






















@ManagedObject("Jetty HTTP Servlet server")
public class Server
  extends HandlerWrapper
  implements Attributes
{
  private static final Logger LOG = Log.getLogger(Server.class);
  
  private final AttributesMap _attributes = new AttributesMap();
  private final ThreadPool _threadPool;
  private final List<Connector> _connectors = new CopyOnWriteArrayList();
  private SessionIdManager _sessionIdManager;
  private boolean _stopAtShutdown;
  private boolean _dumpAfterStart = false;
  private boolean _dumpBeforeStop = false;
  
  private ErrorHandler _errorHandler;
  private RequestLog _requestLog;
  private final Locker _dateLocker = new Locker();
  
  private volatile DateField _dateField;
  

  public Server()
  {
    this((ThreadPool)null);
  }
  






  public Server(@Name("port") int port)
  {
    this((ThreadPool)null);
    ServerConnector connector = new ServerConnector(this);
    connector.setPort(port);
    setConnectors(new Connector[] { connector });
  }
  







  public Server(@Name("address") InetSocketAddress addr)
  {
    this((ThreadPool)null);
    ServerConnector connector = new ServerConnector(this);
    connector.setHost(addr.getHostName());
    connector.setPort(addr.getPort());
    setConnectors(new Connector[] { connector });
  }
  

  public Server(@Name("threadpool") ThreadPool pool)
  {
    _threadPool = (pool != null ? pool : new QueuedThreadPool());
    addBean(_threadPool);
    setServer(this);
  }
  

  public RequestLog getRequestLog()
  {
    return _requestLog;
  }
  

  public ErrorHandler getErrorHandler()
  {
    return _errorHandler;
  }
  

  public void setRequestLog(RequestLog requestLog)
  {
    updateBean(_requestLog, requestLog);
    _requestLog = requestLog;
  }
  

  public void setErrorHandler(ErrorHandler errorHandler)
  {
    if ((errorHandler instanceof ErrorHandler.ErrorPageMapper))
      throw new IllegalArgumentException("ErrorPageMapper is applicable only to ContextHandler");
    updateBean(_errorHandler, errorHandler);
    _errorHandler = errorHandler;
    if (errorHandler != null) {
      errorHandler.setServer(this);
    }
  }
  
  @ManagedAttribute("version of this server")
  public static String getVersion()
  {
    return Jetty.VERSION;
  }
  

  public boolean getStopAtShutdown()
  {
    return _stopAtShutdown;
  }
  








  public void setStopTimeout(long stopTimeout)
  {
    super.setStopTimeout(stopTimeout);
  }
  








  public void setStopAtShutdown(boolean stop)
  {
    if (stop)
    {

      if (!_stopAtShutdown)
      {

        if (isStarted()) {
          ShutdownThread.register(new LifeCycle[] { this });
        }
      }
    } else {
      ShutdownThread.deregister(this);
    }
    _stopAtShutdown = stop;
  }
  




  @ManagedAttribute(value="connectors for this server", readonly=true)
  public Connector[] getConnectors()
  {
    List<Connector> connectors = new ArrayList(_connectors);
    return (Connector[])connectors.toArray(new Connector[connectors.size()]);
  }
  

  public void addConnector(Connector connector)
  {
    if (connector.getServer() != this)
    {
      throw new IllegalArgumentException("Connector " + connector + " cannot be shared among server " + connector.getServer() + " and server " + this); }
    if (_connectors.add(connector)) {
      addBean(connector);
    }
  }
  





  public void removeConnector(Connector connector)
  {
    if (_connectors.remove(connector)) {
      removeBean(connector);
    }
  }
  




  public void setConnectors(Connector[] connectors)
  {
    if (connectors != null)
    {
      for (Connector connector : connectors)
      {
        if (connector.getServer() != this)
        {
          throw new IllegalArgumentException("Connector " + connector + " cannot be shared among server " + connector.getServer() + " and server " + this);
        }
      }
    }
    Connector[] oldConnectors = getConnectors();
    updateBeans(oldConnectors, connectors);
    _connectors.removeAll(Arrays.asList(oldConnectors));
    if (connectors != null) {
      _connectors.addAll(Arrays.asList(connectors));
    }
  }
  



  @ManagedAttribute("the server thread pool")
  public ThreadPool getThreadPool()
  {
    return _threadPool;
  }
  



  @ManagedAttribute("dump state to stderr after start")
  public boolean isDumpAfterStart()
  {
    return _dumpAfterStart;
  }
  



  public void setDumpAfterStart(boolean dumpAfterStart)
  {
    _dumpAfterStart = dumpAfterStart;
  }
  



  @ManagedAttribute("dump state to stderr before stop")
  public boolean isDumpBeforeStop()
  {
    return _dumpBeforeStop;
  }
  



  public void setDumpBeforeStop(boolean dumpBeforeStop)
  {
    _dumpBeforeStop = dumpBeforeStop;
  }
  

  public HttpField getDateField()
  {
    long now = System.currentTimeMillis();
    long seconds = now / 1000L;
    DateField df = _dateField;
    
    if ((df == null) || (_seconds != seconds))
    {
      Locker.Lock lock = _dateLocker.lock();Throwable localThrowable4 = null;
      try {
        df = _dateField;
        if ((df == null) || (_seconds != seconds))
        {
          HttpField field = new PreEncodedHttpField(HttpHeader.DATE, DateGenerator.formatDate(now));
          _dateField = new DateField(seconds, field);
          return field;
        }
      }
      catch (Throwable localThrowable2)
      {
        localThrowable4 = localThrowable2;throw localThrowable2;



      }
      finally
      {


        if (lock != null) if (localThrowable4 != null) try { lock.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else lock.close();
      } }
    return _dateField;
  }
  


  protected void doStart()
    throws Exception
  {
    if (_errorHandler == null)
      _errorHandler = ((ErrorHandler)getBean(ErrorHandler.class));
    if (_errorHandler == null)
      setErrorHandler(new ErrorHandler());
    if ((_errorHandler instanceof ErrorHandler.ErrorPageMapper)) {
      LOG.warn("ErrorPageMapper not supported for Server level Error Handling", new Object[0]);
    }
    

    if (getStopAtShutdown()) {
      ShutdownThread.register(new LifeCycle[] { this });
    }
    

    ShutdownMonitor.register(new LifeCycle[] { this });
    

    ShutdownMonitor.getInstance().start();
    
    LOG.info("jetty-" + getVersion(), new Object[0]);
    if (!Jetty.STABLE)
    {
      LOG.warn("THIS IS NOT A STABLE RELEASE! DO NOT USE IN PRODUCTION!", new Object[0]);
      LOG.warn("Download a stable release from http://download.eclipse.org/jetty/", new Object[0]);
    }
    
    HttpGenerator.setJettyVersion(HttpConfiguration.SERVER_VERSION);
    

    ThreadPool.SizedThreadPool pool = (ThreadPool.SizedThreadPool)getBean(ThreadPool.SizedThreadPool.class);
    int max = pool == null ? -1 : pool.getMaxThreads();
    int selectors = 0;
    int acceptors = 0;
    
    for (Connector connector : _connectors)
    {
      if ((connector instanceof AbstractConnector))
      {
        AbstractConnector abstractConnector = (AbstractConnector)connector;
        Executor connectorExecutor = connector.getExecutor();
        
        if (connectorExecutor == pool)
        {





          acceptors += abstractConnector.getAcceptors();
          
          if ((connector instanceof ServerConnector))
          {


            selectors += 2 * ((ServerConnector)connector).getSelectorManager().getSelectorCount();
          }
        }
      }
    }
    int needed = 1 + selectors + acceptors;
    if ((max > 0) && (needed > max)) {
      throw new IllegalStateException(String.format("Insufficient threads: max=%d < needed(acceptors=%d + selectors=%d + request=1)", new Object[] { Integer.valueOf(max), Integer.valueOf(acceptors), Integer.valueOf(selectors) }));
    }
    MultiException mex = new MultiException();
    try
    {
      super.doStart();
    }
    catch (Throwable e)
    {
      mex.add(e);
    }
    

    for (Connector connector : _connectors)
    {
      try
      {
        connector.start();
      }
      catch (Throwable e)
      {
        mex.add(e);
      }
    }
    
    if (isDumpAfterStart()) {
      dumpStdErr();
    }
    mex.ifExceptionThrow();
    
    LOG.info(String.format("Started @%dms", new Object[] { Long.valueOf(Uptime.getUptime()) }), new Object[0]);
  }
  

  protected void start(LifeCycle l)
    throws Exception
  {
    if (!(l instanceof Connector)) {
      super.start(l);
    }
  }
  
  protected void doStop()
    throws Exception
  {
    if (isDumpBeforeStop()) {
      dumpStdErr();
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("doStop {}", new Object[] { this });
    }
    MultiException mex = new MultiException();
    

    List<Future<Void>> futures = new ArrayList();
    

    for (Iterator localIterator1 = _connectors.iterator(); localIterator1.hasNext();) { connector = (Connector)localIterator1.next();
      futures.add(connector.shutdown());
    }
    Connector connector;
    Handler[] gracefuls = getChildHandlersByClass(Graceful.class);
    for (Handler graceful : gracefuls) {
      futures.add(((Graceful)graceful).shutdown());
    }
    
    long stopTimeout = getStopTimeout();
    long stop_by; if (stopTimeout > 0L)
    {
      stop_by = System.currentTimeMillis() + stopTimeout;
      if (LOG.isDebugEnabled()) {
        LOG.debug("Graceful shutdown {} by ", new Object[] { this, new Date(stop_by) });
      }
      
      for (Future<Void> future : futures)
      {
        try
        {
          if (!future.isDone()) {
            future.get(Math.max(1L, stop_by - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
          }
        }
        catch (Exception e) {
          mex.add(e);
        }
      }
    }
    

    for (Future<Void> future : futures) {
      if (!future.isDone()) {
        future.cancel(true);
      }
    }
    for (Connector connector : _connectors)
    {
      try
      {
        connector.stop();
      }
      catch (Throwable e)
      {
        mex.add(e);
      }
    }
    

    try
    {
      super.doStop();
    }
    catch (Throwable e)
    {
      mex.add(e);
    }
    
    if (getStopAtShutdown()) {
      ShutdownThread.deregister(this);
    }
    

    ShutdownMonitor.deregister(this);
    
    mex.ifExceptionThrow();
  }
  





  public void handle(HttpChannel channel)
    throws IOException, ServletException
  {
    String target = channel.getRequest().getPathInfo();
    Request request = channel.getRequest();
    Response response = channel.getResponse();
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} {} {} on {}", new Object[] { request.getDispatcherType(), request.getMethod(), target, channel });
    }
    if ((HttpMethod.OPTIONS.is(request.getMethod())) || ("*".equals(target)))
    {
      if (!HttpMethod.OPTIONS.is(request.getMethod()))
        response.sendError(400);
      handleOptions(request, response);
      if (!request.isHandled()) {
        handle(target, request, request, response);
      }
    } else {
      handle(target, request, request, response);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("handled={} async={} committed={} on {}", new Object[] { Boolean.valueOf(request.isHandled()), Boolean.valueOf(request.isAsyncStarted()), Boolean.valueOf(response.isCommitted()), channel });
    }
  }
  




  protected void handleOptions(Request request, Response response)
    throws IOException
  {}
  



  public void handleAsync(HttpChannel channel)
    throws IOException, ServletException
  {
    HttpChannelState state = channel.getRequest().getHttpChannelState();
    AsyncContextEvent event = state.getAsyncContextEvent();
    
    Request baseRequest = channel.getRequest();
    String path = event.getPath();
    
    if (path != null)
    {

      ServletContext context = event.getServletContext();
      String query = baseRequest.getQueryString();
      baseRequest.setURIPathQuery(URIUtil.addPaths(context == null ? null : URIUtil.encodePath(context.getContextPath()), path));
      HttpURI uri = baseRequest.getHttpURI();
      baseRequest.setPathInfo(uri.getDecodedPath());
      if (uri.getQuery() != null) {
        baseRequest.mergeQueryParameters(query, uri.getQuery(), true);
      }
    }
    String target = baseRequest.getPathInfo();
    HttpServletRequest request = (HttpServletRequest)event.getSuppliedRequest();
    HttpServletResponse response = (HttpServletResponse)event.getSuppliedResponse();
    
    if (LOG.isDebugEnabled())
      LOG.debug("{} {} {} on {}", new Object[] { request.getDispatcherType(), request.getMethod(), target, channel });
    handle(target, baseRequest, request, response);
    if (LOG.isDebugEnabled()) {
      LOG.debug("handledAsync={} async={} committed={} on {}", new Object[] { Boolean.valueOf(channel.getRequest().isHandled()), Boolean.valueOf(request.isAsyncStarted()), Boolean.valueOf(response.isCommitted()), channel });
    }
  }
  
  public void join() throws InterruptedException
  {
    getThreadPool().join();
  }
  




  public SessionIdManager getSessionIdManager()
  {
    return _sessionIdManager;
  }
  




  public void setSessionIdManager(SessionIdManager sessionIdManager)
  {
    updateBean(_sessionIdManager, sessionIdManager);
    _sessionIdManager = sessionIdManager;
  }
  





  public void clearAttributes()
  {
    Enumeration<String> names = _attributes.getAttributeNames();
    while (names.hasMoreElements())
      removeBean(_attributes.getAttribute((String)names.nextElement()));
    _attributes.clearAttributes();
  }
  





  public Object getAttribute(String name)
  {
    return _attributes.getAttribute(name);
  }
  





  public Enumeration<String> getAttributeNames()
  {
    return AttributesMap.getAttributeNamesCopy(_attributes);
  }
  





  public void removeAttribute(String name)
  {
    Object bean = _attributes.getAttribute(name);
    if (bean != null)
      removeBean(bean);
    _attributes.removeAttribute(name);
  }
  





  public void setAttribute(String name, Object attribute)
  {
    Object old = _attributes.getAttribute(name);
    updateBean(old, attribute);
    _attributes.setAttribute(name, attribute);
  }
  




  public URI getURI()
  {
    NetworkConnector connector = null;
    for (Connector c : _connectors)
    {
      if ((c instanceof NetworkConnector))
      {
        connector = (NetworkConnector)c;
        break;
      }
    }
    
    if (connector == null) {
      return null;
    }
    ContextHandler context = (ContextHandler)getChildHandlerByClass(ContextHandler.class);
    
    try
    {
      String protocol = connector.getDefaultConnectionFactory().getProtocol();
      String scheme = "http";
      if ((protocol.startsWith("SSL-")) || (protocol.equals("SSL"))) {
        scheme = "https";
      }
      String host = connector.getHost();
      if ((context != null) && (context.getVirtualHosts() != null) && (context.getVirtualHosts().length > 0))
        host = context.getVirtualHosts()[0];
      if (host == null) {
        host = InetAddress.getLocalHost().getHostAddress();
      }
      String path = context == null ? null : context.getContextPath();
      if (path == null)
        path = "/";
      return new URI(scheme, null, host, connector.getLocalPort(), path, null, null);
    }
    catch (Exception e)
    {
      LOG.warn(e); }
    return null;
  }
  



  public String toString()
  {
    return getClass().getName() + "@" + Integer.toHexString(hashCode());
  }
  

  public void dump(Appendable out, String indent)
    throws IOException
  {
    dumpBeans(out, indent, new Collection[] { Collections.singleton(new ClassLoaderDump(getClass().getClassLoader())) });
  }
  
  public static void main(String... args)
    throws Exception
  {
    System.err.println(getVersion());
  }
  

  private static class DateField
  {
    final long _seconds;
    
    final HttpField _dateField;
    
    public DateField(long seconds, HttpField dateField)
    {
      _seconds = seconds;
      _dateField = dateField;
    }
  }
}
