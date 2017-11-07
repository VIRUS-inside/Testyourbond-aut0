package org.seleniumhq.jetty9.server.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.HttpURI;
import org.seleniumhq.jetty9.http.MimeTypes;
import org.seleniumhq.jetty9.server.ClassLoaderDump;
import org.seleniumhq.jetty9.server.Connector;
import org.seleniumhq.jetty9.server.Dispatcher;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HandlerContainer;
import org.seleniumhq.jetty9.server.HttpChannel;
import org.seleniumhq.jetty9.server.HttpChannelState;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.util.Attributes;
import org.seleniumhq.jetty9.util.AttributesMap;
import org.seleniumhq.jetty9.util.FutureCallback;
import org.seleniumhq.jetty9.util.Loader;
import org.seleniumhq.jetty9.util.MultiException;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.DumpableCollection;
import org.seleniumhq.jetty9.util.component.Graceful;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;

































@ManagedObject("URI Context")
public class ContextHandler
  extends ScopedHandler
  implements Attributes, Graceful
{
  public static final int SERVLET_MAJOR_VERSION = 3;
  public static final int SERVLET_MINOR_VERSION = 1;
  public static final Class<?>[] SERVLET_LISTENER_TYPES = { ServletContextListener.class, ServletContextAttributeListener.class, ServletRequestListener.class, ServletRequestAttributeListener.class };
  

  public static final int DEFAULT_LISTENER_TYPE_INDEX = 1;
  

  public static final int EXTENDED_LISTENER_TYPE_INDEX = 0;
  

  private static final String __unimplmented = "Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler";
  
  private static final Logger LOG = Log.getLogger(ContextHandler.class);
  
  private static final ThreadLocal<Context> __context = new ThreadLocal();
  
  private static String __serverInfo = "jetty/" + Server.getVersion();
  

  public static final String MANAGED_ATTRIBUTES = "org.seleniumhq.jetty9.server.context.ManagedAttributes";
  

  protected Context _scontext;
  
  private final AttributesMap _attributes;
  
  private final Map<String, String> _initParams;
  
  private ClassLoader _classLoader;
  

  public static Context getCurrentContext()
  {
    return (Context)__context.get();
  }
  

  public static ContextHandler getContextHandler(ServletContext context)
  {
    if ((context instanceof Context))
      return ((Context)context).getContextHandler();
    Context c = getCurrentContext();
    if (c != null)
      return c.getContextHandler();
    return null;
  }
  

  public static String getServerInfo()
  {
    return __serverInfo;
  }
  

  public static void setServerInfo(String serverInfo)
  {
    __serverInfo = serverInfo;
  }
  




  private String _contextPath = "/";
  private String _contextPathEncoded = "/";
  
  private String _displayName;
  
  private Resource _baseResource;
  
  private MimeTypes _mimeTypes;
  private Map<String, String> _localeEncodingMap;
  private String[] _welcomeFiles;
  private ErrorHandler _errorHandler;
  private String[] _vhosts;
  private Logger _logger;
  private boolean _allowNullPathInfo;
  private int _maxFormKeys = Integer.getInteger("org.seleniumhq.jetty9.server.Request.maxFormKeys", -1).intValue();
  private int _maxFormContentSize = Integer.getInteger("org.seleniumhq.jetty9.server.Request.maxFormContentSize", -1).intValue();
  private boolean _compactPath = false;
  private boolean _usingSecurityManager = System.getSecurityManager() != null;
  
  private final List<EventListener> _eventListeners = new CopyOnWriteArrayList();
  private final List<EventListener> _programmaticListeners = new CopyOnWriteArrayList();
  private final List<ServletContextListener> _servletContextListeners = new CopyOnWriteArrayList();
  private final List<ServletContextListener> _destroySerletContextListeners = new ArrayList();
  private final List<ServletContextAttributeListener> _servletContextAttributeListeners = new CopyOnWriteArrayList();
  private final List<ServletRequestListener> _servletRequestListeners = new CopyOnWriteArrayList();
  private final List<ServletRequestAttributeListener> _servletRequestAttributeListeners = new CopyOnWriteArrayList();
  private final List<ContextScopeListener> _contextListeners = new CopyOnWriteArrayList();
  private final List<EventListener> _durableListeners = new CopyOnWriteArrayList();
  private Map<String, Object> _managedAttributes;
  private String[] _protectedTargets;
  private final CopyOnWriteArrayList<AliasCheck> _aliasChecks = new CopyOnWriteArrayList();
  
  public static enum Availability { UNAVAILABLE,  STARTING,  AVAILABLE,  SHUTDOWN;
    
    private Availability() {}
  }
  
  public ContextHandler() {
    this(null, null, null);
  }
  

  protected ContextHandler(Context context)
  {
    this(context, null, null);
  }
  

  public ContextHandler(String contextPath)
  {
    this(null, null, contextPath);
  }
  

  public ContextHandler(HandlerContainer parent, String contextPath)
  {
    this(null, parent, contextPath);
  }
  

  private ContextHandler(Context context, HandlerContainer parent, String contextPath)
  {
    _scontext = (context == null ? new Context() : context);
    _attributes = new AttributesMap();
    _initParams = new HashMap();
    addAliasCheck(new ApproveNonExistentDirectoryAliases());
    if (File.separatorChar == '/') {
      addAliasCheck(new AllowSymLinkAliasChecker());
    }
    if (contextPath != null)
      setContextPath(contextPath);
    if ((parent instanceof HandlerWrapper)) {
      ((HandlerWrapper)parent).setHandler(this);
    } else if ((parent instanceof HandlerCollection)) {
      ((HandlerCollection)parent).addHandler(this);
    }
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    dumpBeans(out, indent, new Collection[] {
      Collections.singletonList(new ClassLoaderDump(getClassLoader())), 
      Collections.singletonList(new DumpableCollection("Handler attributes " + this, ((AttributesMap)getAttributes()).getAttributeEntrySet())), 
      Collections.singletonList(new DumpableCollection("Context attributes " + this, getServletContext().getAttributeEntrySet())), 
      Collections.singletonList(new DumpableCollection("Initparams " + this, getInitParams().entrySet())) });
  }
  


  public Context getServletContext()
  {
    return _scontext;
  }
  




  @ManagedAttribute("Checks if the /context is not redirected to /context/")
  public boolean getAllowNullPathInfo()
  {
    return _allowNullPathInfo;
  }
  





  public void setAllowNullPathInfo(boolean allowNullPathInfo)
  {
    _allowNullPathInfo = allowNullPathInfo;
  }
  


  public void setServer(Server server)
  {
    super.setServer(server);
    if (_errorHandler != null) {
      _errorHandler.setServer(server);
    }
  }
  
  public boolean isUsingSecurityManager()
  {
    return _usingSecurityManager;
  }
  

  public void setUsingSecurityManager(boolean usingSecurityManager)
  {
    _usingSecurityManager = usingSecurityManager;
  }
  





  private volatile Availability _availability;
  




  public void setVirtualHosts(String[] vhosts)
  {
    if (vhosts == null)
    {
      _vhosts = vhosts;
    }
    else
    {
      _vhosts = new String[vhosts.length];
      for (int i = 0; i < vhosts.length; i++) {
        _vhosts[i] = normalizeHostname(vhosts[i]);
      }
    }
  }
  







  public void addVirtualHosts(String[] virtualHosts)
  {
    if (virtualHosts == null)
    {
      return;
    }
    

    List<String> currentVirtualHosts = null;
    if (_vhosts != null)
    {
      currentVirtualHosts = new ArrayList(Arrays.asList(_vhosts));
    }
    else
    {
      currentVirtualHosts = new ArrayList();
    }
    
    for (int i = 0; i < virtualHosts.length; i++)
    {
      String normVhost = normalizeHostname(virtualHosts[i]);
      if (!currentVirtualHosts.contains(normVhost))
      {
        currentVirtualHosts.add(normVhost);
      }
    }
    _vhosts = ((String[])currentVirtualHosts.toArray(new String[0]));
  }
  









  public void removeVirtualHosts(String[] virtualHosts)
  {
    if (virtualHosts == null)
    {
      return;
    }
    if ((_vhosts == null) || (_vhosts.length == 0))
    {
      return;
    }
    

    List<String> existingVirtualHosts = new ArrayList(Arrays.asList(_vhosts));
    
    for (int i = 0; i < virtualHosts.length; i++)
    {
      String toRemoveVirtualHost = normalizeHostname(virtualHosts[i]);
      if (existingVirtualHosts.contains(toRemoveVirtualHost))
      {
        existingVirtualHosts.remove(toRemoveVirtualHost);
      }
    }
    
    if (existingVirtualHosts.isEmpty())
    {
      _vhosts = null;
    }
    else
    {
      _vhosts = ((String[])existingVirtualHosts.toArray(new String[0]));
    }
  }
  










  @ManagedAttribute(value="Virtual hosts accepted by the context", readonly=true)
  public String[] getVirtualHosts()
  {
    return _vhosts;
  }
  





  public Object getAttribute(String name)
  {
    return _attributes.getAttribute(name);
  }
  





  public Enumeration<String> getAttributeNames()
  {
    return AttributesMap.getAttributeNamesCopy(_attributes);
  }
  




  public Attributes getAttributes()
  {
    return _attributes;
  }
  




  public ClassLoader getClassLoader()
  {
    return _classLoader;
  }
  






  @ManagedAttribute("The file classpath")
  public String getClassPath()
  {
    if ((_classLoader == null) || (!(_classLoader instanceof URLClassLoader)))
      return null;
    URLClassLoader loader = (URLClassLoader)_classLoader;
    URL[] urls = loader.getURLs();
    StringBuilder classpath = new StringBuilder();
    for (int i = 0; i < urls.length; i++)
    {
      try
      {
        Resource resource = newResource(urls[i]);
        File file = resource.getFile();
        if ((file != null) && (file.exists()))
        {
          if (classpath.length() > 0)
            classpath.append(File.pathSeparatorChar);
          classpath.append(file.getAbsolutePath());
        }
      }
      catch (IOException e)
      {
        LOG.debug(e);
      }
    }
    if (classpath.length() == 0)
      return null;
    return classpath.toString();
  }
  




  @ManagedAttribute("True if URLs are compacted to replace the multiple '/'s with a single '/'")
  public String getContextPath()
  {
    return _contextPath;
  }
  




  public String getContextPathEncoded()
  {
    return _contextPathEncoded;
  }
  




  public String getInitParameter(String name)
  {
    return (String)_initParams.get(name);
  }
  



  public String setInitParameter(String name, String value)
  {
    return (String)_initParams.put(name, value);
  }
  




  public Enumeration<String> getInitParameterNames()
  {
    return Collections.enumeration(_initParams.keySet());
  }
  




  @ManagedAttribute("Initial Parameter map for the context")
  public Map<String, String> getInitParams()
  {
    return _initParams;
  }
  




  @ManagedAttribute(value="Display name of the Context", readonly=true)
  public String getDisplayName()
  {
    return _displayName;
  }
  

  public EventListener[] getEventListeners()
  {
    return (EventListener[])_eventListeners.toArray(new EventListener[_eventListeners.size()]);
  }
  











  public void setEventListeners(EventListener[] eventListeners)
  {
    _contextListeners.clear();
    _servletContextListeners.clear();
    _servletContextAttributeListeners.clear();
    _servletRequestListeners.clear();
    _servletRequestAttributeListeners.clear();
    _eventListeners.clear();
    
    if (eventListeners != null) {
      for (EventListener listener : eventListeners) {
        addEventListener(listener);
      }
    }
  }
  








  public void addEventListener(EventListener listener)
  {
    _eventListeners.add(listener);
    
    if ((!isStarted()) && (!isStarting())) {
      _durableListeners.add(listener);
    }
    if ((listener instanceof ContextScopeListener)) {
      _contextListeners.add((ContextScopeListener)listener);
    }
    if ((listener instanceof ServletContextListener)) {
      _servletContextListeners.add((ServletContextListener)listener);
    }
    if ((listener instanceof ServletContextAttributeListener)) {
      _servletContextAttributeListeners.add((ServletContextAttributeListener)listener);
    }
    if ((listener instanceof ServletRequestListener)) {
      _servletRequestListeners.add((ServletRequestListener)listener);
    }
    if ((listener instanceof ServletRequestAttributeListener)) {
      _servletRequestAttributeListeners.add((ServletRequestAttributeListener)listener);
    }
  }
  









  public void removeEventListener(EventListener listener)
  {
    _eventListeners.remove(listener);
    
    if ((listener instanceof ContextScopeListener)) {
      _contextListeners.remove(listener);
    }
    if ((listener instanceof ServletContextListener)) {
      _servletContextListeners.remove(listener);
    }
    if ((listener instanceof ServletContextAttributeListener)) {
      _servletContextAttributeListeners.remove(listener);
    }
    if ((listener instanceof ServletRequestListener)) {
      _servletRequestListeners.remove(listener);
    }
    if ((listener instanceof ServletRequestAttributeListener)) {
      _servletRequestAttributeListeners.remove(listener);
    }
  }
  





  protected void addProgrammaticListener(EventListener listener)
  {
    _programmaticListeners.add(listener);
  }
  

  protected boolean isProgrammaticListener(EventListener listener)
  {
    return _programmaticListeners.contains(listener);
  }
  






  @ManagedAttribute("true for graceful shutdown, which allows existing requests to complete")
  public boolean isShutdown()
  {
    switch (_availability)
    {
    case SHUTDOWN: 
      return true;
    }
    return false;
  }
  








  public Future<Void> shutdown()
  {
    _availability = (isRunning() ? Availability.SHUTDOWN : Availability.UNAVAILABLE);
    return new FutureCallback(true);
  }
  




  public boolean isAvailable()
  {
    return _availability == Availability.AVAILABLE;
  }
  





  public void setAvailable(boolean available)
  {
    synchronized (this)
    {
      if ((available) && (isRunning())) {
        _availability = Availability.AVAILABLE;
      } else if ((!available) || (!isRunning())) {
        _availability = Availability.UNAVAILABLE;
      }
    }
  }
  
  public Logger getLogger()
  {
    return _logger;
  }
  

  public void setLogger(Logger logger)
  {
    _logger = logger;
  }
  




  protected void doStart()
    throws Exception
  {
    _availability = Availability.STARTING;
    
    if (_contextPath == null) {
      throw new IllegalStateException("Null contextPath");
    }
    if (_logger == null)
      _logger = Log.getLogger(getDisplayName() == null ? getContextPath() : getDisplayName());
    ClassLoader old_classloader = null;
    Thread current_thread = null;
    Context old_context = null;
    
    _attributes.setAttribute("org.seleniumhq.jetty9.server.Executor", getServer().getThreadPool());
    
    if (_mimeTypes == null) {
      _mimeTypes = new MimeTypes();
    }
    
    try
    {
      if (_classLoader != null)
      {
        current_thread = Thread.currentThread();
        old_classloader = current_thread.getContextClassLoader();
        current_thread.setContextClassLoader(_classLoader);
      }
      old_context = (Context)__context.get();
      __context.set(_scontext);
      enterScope(null, getState());
      

      startContext();
      
      _availability = Availability.AVAILABLE;
      LOG.info("Started {}", new Object[] { this });
    }
    finally
    {
      if (_availability == Availability.STARTING)
        _availability = Availability.UNAVAILABLE;
      exitScope(null);
      __context.set(old_context);
      
      if ((_classLoader != null) && (current_thread != null)) {
        current_thread.setContextClassLoader(old_classloader);
      }
    }
  }
  






  protected void startContext()
    throws Exception
  {
    String managedAttributes = (String)_initParams.get("org.seleniumhq.jetty9.server.context.ManagedAttributes");
    if (managedAttributes != null) {
      addEventListener(new ManagedAttributeListener(this, StringUtil.csvSplit(managedAttributes)));
    }
    super.doStart();
    

    _destroySerletContextListeners.clear();
    ServletContextEvent event; if (!_servletContextListeners.isEmpty())
    {
      event = new ServletContextEvent(_scontext);
      for (ServletContextListener listener : _servletContextListeners)
      {
        callContextInitialized(listener, event);
        _destroySerletContextListeners.add(listener);
      }
    }
  }
  

  protected void stopContext()
    throws Exception
  {
    super.doStop();
    

    ServletContextEvent event = new ServletContextEvent(_scontext);
    Collections.reverse(_destroySerletContextListeners);
    MultiException ex = new MultiException();
    for (ServletContextListener listener : _destroySerletContextListeners)
    {
      try
      {
        callContextDestroyed(listener, event);
      }
      catch (Exception x)
      {
        ex.add(x);
      }
    }
    ex.ifExceptionThrow();
  }
  



  protected void callContextInitialized(ServletContextListener l, ServletContextEvent e)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("contextInitialized: {}->{}", new Object[] { e, l });
    l.contextInitialized(e);
  }
  

  protected void callContextDestroyed(ServletContextListener l, ServletContextEvent e)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("contextDestroyed: {}->{}", new Object[] { e, l });
    l.contextDestroyed(e);
  }
  
  /* Error */
  protected void doStop()
    throws Exception
  {
    // Byte code:
    //   0: aload_0
    //   1: getstatic 560	org/seleniumhq/jetty9/server/handler/ContextHandler$Availability:UNAVAILABLE	Lorg/seleniumhq/jetty9/server/handler/ContextHandler$Availability;
    //   4: putfield 546	org/seleniumhq/jetty9/server/handler/ContextHandler:_availability	Lorg/seleniumhq/jetty9/server/handler/ContextHandler$Availability;
    //   7: aconst_null
    //   8: astore_1
    //   9: aconst_null
    //   10: astore_2
    //   11: aconst_null
    //   12: astore_3
    //   13: getstatic 116	org/seleniumhq/jetty9/server/handler/ContextHandler:__context	Ljava/lang/ThreadLocal;
    //   16: invokevirtual 122	java/lang/ThreadLocal:get	()Ljava/lang/Object;
    //   19: checkcast 31	org/seleniumhq/jetty9/server/handler/ContextHandler$Context
    //   22: astore 4
    //   24: aload_0
    //   25: aconst_null
    //   26: ldc_w 744
    //   29: invokevirtual 648	org/seleniumhq/jetty9/server/handler/ContextHandler:enterScope	(Lorg/seleniumhq/jetty9/server/Request;Ljava/lang/Object;)V
    //   32: getstatic 116	org/seleniumhq/jetty9/server/handler/ContextHandler:__context	Ljava/lang/ThreadLocal;
    //   35: aload_0
    //   36: getfield 222	org/seleniumhq/jetty9/server/handler/ContextHandler:_scontext	Lorg/seleniumhq/jetty9/server/handler/ContextHandler$Context;
    //   39: invokevirtual 641	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
    //   42: aload_0
    //   43: getfield 420	org/seleniumhq/jetty9/server/handler/ContextHandler:_classLoader	Ljava/lang/ClassLoader;
    //   46: ifnull +25 -> 71
    //   49: aload_0
    //   50: getfield 420	org/seleniumhq/jetty9/server/handler/ContextHandler:_classLoader	Ljava/lang/ClassLoader;
    //   53: astore_2
    //   54: invokestatic 631	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   57: astore_3
    //   58: aload_3
    //   59: invokevirtual 634	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
    //   62: astore_1
    //   63: aload_3
    //   64: aload_0
    //   65: getfield 420	org/seleniumhq/jetty9/server/handler/ContextHandler:_classLoader	Ljava/lang/ClassLoader;
    //   68: invokevirtual 637	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
    //   71: aload_0
    //   72: invokevirtual 746	org/seleniumhq/jetty9/server/handler/ContextHandler:stopContext	()V
    //   75: aload_0
    //   76: aload_0
    //   77: getfield 215	org/seleniumhq/jetty9/server/handler/ContextHandler:_durableListeners	Ljava/util/List;
    //   80: aload_0
    //   81: getfield 215	org/seleniumhq/jetty9/server/handler/ContextHandler:_durableListeners	Ljava/util/List;
    //   84: invokeinterface 504 1 0
    //   89: anewarray 506	java/util/EventListener
    //   92: invokeinterface 389 2 0
    //   97: checkcast 508	[Ljava/util/EventListener;
    //   100: invokevirtual 748	org/seleniumhq/jetty9/server/handler/ContextHandler:setEventListeners	([Ljava/util/EventListener;)V
    //   103: aload_0
    //   104: getfield 215	org/seleniumhq/jetty9/server/handler/ContextHandler:_durableListeners	Ljava/util/List;
    //   107: invokeinterface 513 1 0
    //   112: aload_0
    //   113: getfield 348	org/seleniumhq/jetty9/server/handler/ContextHandler:_errorHandler	Lorg/seleniumhq/jetty9/server/handler/ErrorHandler;
    //   116: ifnull +10 -> 126
    //   119: aload_0
    //   120: getfield 348	org/seleniumhq/jetty9/server/handler/ContextHandler:_errorHandler	Lorg/seleniumhq/jetty9/server/handler/ErrorHandler;
    //   123: invokevirtual 751	org/seleniumhq/jetty9/server/handler/ErrorHandler:stop	()V
    //   126: aload_0
    //   127: getfield 198	org/seleniumhq/jetty9/server/handler/ContextHandler:_programmaticListeners	Ljava/util/List;
    //   130: invokeinterface 687 1 0
    //   135: astore 5
    //   137: aload 5
    //   139: invokeinterface 692 1 0
    //   144: ifeq +62 -> 206
    //   147: aload 5
    //   149: invokeinterface 695 1 0
    //   154: checkcast 506	java/util/EventListener
    //   157: astore 6
    //   159: aload_0
    //   160: aload 6
    //   162: invokevirtual 753	org/seleniumhq/jetty9/server/handler/ContextHandler:removeEventListener	(Ljava/util/EventListener;)V
    //   165: aload 6
    //   167: instanceof 16
    //   170: ifeq +33 -> 203
    //   173: aload 6
    //   175: checkcast 16	org/seleniumhq/jetty9/server/handler/ContextHandler$ContextScopeListener
    //   178: aload_0
    //   179: getfield 222	org/seleniumhq/jetty9/server/handler/ContextHandler:_scontext	Lorg/seleniumhq/jetty9/server/handler/ContextHandler$Context;
    //   182: aconst_null
    //   183: invokeinterface 756 3 0
    //   188: goto +15 -> 203
    //   191: astore 7
    //   193: getstatic 457	org/seleniumhq/jetty9/server/handler/ContextHandler:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   196: aload 7
    //   198: invokeinterface 759 2 0
    //   203: goto -66 -> 137
    //   206: aload_0
    //   207: getfield 198	org/seleniumhq/jetty9/server/handler/ContextHandler:_programmaticListeners	Ljava/util/List;
    //   210: invokeinterface 513 1 0
    //   215: getstatic 116	org/seleniumhq/jetty9/server/handler/ContextHandler:__context	Ljava/lang/ThreadLocal;
    //   218: aload 4
    //   220: invokevirtual 641	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
    //   223: aload_0
    //   224: aconst_null
    //   225: invokevirtual 661	org/seleniumhq/jetty9/server/handler/ContextHandler:exitScope	(Lorg/seleniumhq/jetty9/server/Request;)V
    //   228: getstatic 457	org/seleniumhq/jetty9/server/handler/ContextHandler:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   231: ldc_w 761
    //   234: iconst_1
    //   235: anewarray 571	java/lang/Object
    //   238: dup
    //   239: iconst_0
    //   240: aload_0
    //   241: aastore
    //   242: invokeinterface 657 3 0
    //   247: aload_1
    //   248: ifnull +8 -> 256
    //   251: aload_1
    //   252: aload_2
    //   253: if_acmpeq +70 -> 323
    //   256: aload_3
    //   257: ifnull +66 -> 323
    //   260: aload_3
    //   261: aload_1
    //   262: invokevirtual 637	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
    //   265: goto +58 -> 323
    //   268: astore 8
    //   270: getstatic 116	org/seleniumhq/jetty9/server/handler/ContextHandler:__context	Ljava/lang/ThreadLocal;
    //   273: aload 4
    //   275: invokevirtual 641	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
    //   278: aload_0
    //   279: aconst_null
    //   280: invokevirtual 661	org/seleniumhq/jetty9/server/handler/ContextHandler:exitScope	(Lorg/seleniumhq/jetty9/server/Request;)V
    //   283: getstatic 457	org/seleniumhq/jetty9/server/handler/ContextHandler:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   286: ldc_w 761
    //   289: iconst_1
    //   290: anewarray 571	java/lang/Object
    //   293: dup
    //   294: iconst_0
    //   295: aload_0
    //   296: aastore
    //   297: invokeinterface 657 3 0
    //   302: aload_1
    //   303: ifnull +8 -> 311
    //   306: aload_1
    //   307: aload_2
    //   308: if_acmpeq +12 -> 320
    //   311: aload_3
    //   312: ifnull +8 -> 320
    //   315: aload_3
    //   316: aload_1
    //   317: invokevirtual 637	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
    //   320: aload 8
    //   322: athrow
    //   323: aload_0
    //   324: getfield 222	org/seleniumhq/jetty9/server/handler/ContextHandler:_scontext	Lorg/seleniumhq/jetty9/server/handler/ContextHandler$Context;
    //   327: invokevirtual 764	org/seleniumhq/jetty9/server/handler/ContextHandler$Context:clearAttributes	()V
    //   330: return
    // Line number table:
    //   Java source line #883	-> byte code offset #0
    //   Java source line #885	-> byte code offset #7
    //   Java source line #886	-> byte code offset #9
    //   Java source line #887	-> byte code offset #11
    //   Java source line #888	-> byte code offset #13
    //   Java source line #889	-> byte code offset #24
    //   Java source line #890	-> byte code offset #32
    //   Java source line #894	-> byte code offset #42
    //   Java source line #896	-> byte code offset #49
    //   Java source line #897	-> byte code offset #54
    //   Java source line #898	-> byte code offset #58
    //   Java source line #899	-> byte code offset #63
    //   Java source line #902	-> byte code offset #71
    //   Java source line #905	-> byte code offset #75
    //   Java source line #906	-> byte code offset #103
    //   Java source line #908	-> byte code offset #112
    //   Java source line #909	-> byte code offset #119
    //   Java source line #911	-> byte code offset #126
    //   Java source line #913	-> byte code offset #159
    //   Java source line #914	-> byte code offset #165
    //   Java source line #918	-> byte code offset #173
    //   Java source line #923	-> byte code offset #188
    //   Java source line #920	-> byte code offset #191
    //   Java source line #922	-> byte code offset #193
    //   Java source line #925	-> byte code offset #203
    //   Java source line #926	-> byte code offset #206
    //   Java source line #930	-> byte code offset #215
    //   Java source line #931	-> byte code offset #223
    //   Java source line #932	-> byte code offset #228
    //   Java source line #934	-> byte code offset #247
    //   Java source line #935	-> byte code offset #260
    //   Java source line #930	-> byte code offset #268
    //   Java source line #931	-> byte code offset #278
    //   Java source line #932	-> byte code offset #283
    //   Java source line #934	-> byte code offset #302
    //   Java source line #935	-> byte code offset #315
    //   Java source line #938	-> byte code offset #323
    //   Java source line #939	-> byte code offset #330
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	331	0	this	ContextHandler
    //   8	309	1	old_classloader	ClassLoader
    //   10	298	2	old_webapploader	ClassLoader
    //   12	304	3	current_thread	Thread
    //   22	252	4	old_context	Context
    //   135	13	5	localIterator	Iterator
    //   157	17	6	l	EventListener
    //   191	6	7	e	Throwable
    //   268	53	8	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   173	188	191	java/lang/Throwable
    //   42	215	268	finally
    //   268	270	268	finally
  }
  
  public boolean checkVirtualHost(Request baseRequest)
  {
    if ((_vhosts != null) && (_vhosts.length > 0))
    {
      String vhost = normalizeHostname(baseRequest.getServerName());
      
      boolean match = false;
      boolean connectorName = false;
      boolean connectorMatch = false;
      
      for (String contextVhost : _vhosts)
      {
        if ((contextVhost != null) && (contextVhost.length() != 0))
        {
          char c = contextVhost.charAt(0);
          switch (c)
          {
          case '*': 
            if (contextVhost.startsWith("*."))
            {
              match = (match) || (contextVhost.regionMatches(true, 2, vhost, vhost.indexOf(".") + 1, contextVhost.length() - 2)); }
            break;
          case '@': 
            connectorName = true;
            String name = baseRequest.getHttpChannel().getConnector().getName();
            boolean m = (name != null) && (contextVhost.length() == name.length() + 1) && (contextVhost.endsWith(name));
            match = (match) || (m);
            connectorMatch = (connectorMatch) || (m);
            break;
          default: 
            match = (match) || (contextVhost.equalsIgnoreCase(vhost));
          }
        }
      }
      if ((!match) || ((connectorName) && (!connectorMatch)))
        return false;
    }
    return true;
  }
  



  public boolean checkContextPath(String uri)
  {
    if (_contextPath.length() > 1)
    {

      if (!uri.startsWith(_contextPath))
        return false;
      if ((uri.length() > _contextPath.length()) && (uri.charAt(_contextPath.length()) != '/'))
        return false;
    }
    return true;
  }
  



  public boolean checkContext(String target, Request baseRequest, HttpServletResponse response)
    throws IOException
  {
    DispatcherType dispatch = baseRequest.getDispatcherType();
    

    if (!checkVirtualHost(baseRequest)) {
      return false;
    }
    if (!checkContextPath(target)) {
      return false;
    }
    

    if ((!_allowNullPathInfo) && (_contextPath.length() == target.length()) && (_contextPath.length() > 1))
    {

      baseRequest.setHandled(true);
      if (baseRequest.getQueryString() != null) {
        response.sendRedirect(URIUtil.addPaths(baseRequest.getRequestURI(), "/") + "?" + baseRequest.getQueryString());
      } else
        response.sendRedirect(URIUtil.addPaths(baseRequest.getRequestURI(), "/"));
      return false;
    }
    
    switch (_availability)
    {
    case SHUTDOWN: 
    case UNAVAILABLE: 
      baseRequest.setHandled(true);
      response.sendError(503);
      return false;
    }
    if ((DispatcherType.REQUEST.equals(dispatch)) && (baseRequest.isHandled())) {
      return false;
    }
    
    return true;
  }
  





  public void doScope(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("scope {}|{}|{} @ {}", new Object[] { baseRequest.getContextPath(), baseRequest.getServletPath(), baseRequest.getPathInfo(), this });
    }
    Context old_context = null;
    String old_context_path = null;
    String old_servlet_path = null;
    String old_path_info = null;
    ClassLoader old_classloader = null;
    Thread current_thread = null;
    String pathInfo = target;
    
    DispatcherType dispatch = baseRequest.getDispatcherType();
    
    old_context = baseRequest.getContext();
    

    if (old_context != _scontext)
    {

      if ((DispatcherType.REQUEST.equals(dispatch)) || 
        (DispatcherType.ASYNC.equals(dispatch)) || (
        (DispatcherType.ERROR.equals(dispatch)) && (baseRequest.getHttpChannelState().isAsync())))
      {
        if (_compactPath)
          target = URIUtil.compactPath(target);
        if (!checkContext(target, baseRequest, response)) {
          return;
        }
        if (target.length() > _contextPath.length())
        {
          if (_contextPath.length() > 1)
            target = target.substring(_contextPath.length());
          pathInfo = target;
        }
        else if (_contextPath.length() == 1)
        {
          target = "/";
          pathInfo = "/";
        }
        else
        {
          target = "/";
          pathInfo = null;
        }
      }
      

      if (_classLoader != null)
      {
        current_thread = Thread.currentThread();
        old_classloader = current_thread.getContextClassLoader();
        current_thread.setContextClassLoader(_classLoader);
      }
    }
    
    try
    {
      old_context_path = baseRequest.getContextPath();
      old_servlet_path = baseRequest.getServletPath();
      old_path_info = baseRequest.getPathInfo();
      

      baseRequest.setContext(_scontext);
      __context.set(_scontext);
      if ((!DispatcherType.INCLUDE.equals(dispatch)) && (target.startsWith("/")))
      {
        if (_contextPath.length() == 1) {
          baseRequest.setContextPath("");
        } else
          baseRequest.setContextPath(_contextPathEncoded);
        baseRequest.setServletPath(null);
        baseRequest.setPathInfo(pathInfo);
      }
      
      if (old_context != _scontext) {
        enterScope(baseRequest, dispatch);
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("context={}|{}|{} @ {}", new Object[] { baseRequest.getContextPath(), baseRequest.getServletPath(), baseRequest.getPathInfo(), this });
      }
      nextScope(target, baseRequest, request, response);
    }
    finally
    {
      if (old_context != _scontext)
      {
        exitScope(baseRequest);
        

        if ((_classLoader != null) && (current_thread != null))
        {
          current_thread.setContextClassLoader(old_classloader);
        }
        

        baseRequest.setContext(old_context);
        __context.set(old_context);
        baseRequest.setContextPath(old_context_path);
        baseRequest.setServletPath(old_servlet_path);
        baseRequest.setPathInfo(old_path_info);
      }
    }
  }
  

  protected void requestInitialized(Request baseRequest, HttpServletRequest request)
  {
    Iterator localIterator;
    if (!_servletRequestAttributeListeners.isEmpty())
      for (localIterator = _servletRequestAttributeListeners.iterator(); localIterator.hasNext();) { l = (ServletRequestAttributeListener)localIterator.next();
        baseRequest.addEventListener(l); }
    ServletRequestAttributeListener l;
    ServletRequestEvent sre; if (!_servletRequestListeners.isEmpty())
    {
      sre = new ServletRequestEvent(_scontext, request);
      for (ServletRequestListener l : _servletRequestListeners) {
        l.requestInitialized(sre);
      }
    }
  }
  
  protected void requestDestroyed(Request baseRequest, HttpServletRequest request) {
    ServletRequestEvent sre;
    int i;
    if (!_servletRequestListeners.isEmpty())
    {
      sre = new ServletRequestEvent(_scontext, request);
      for (i = _servletRequestListeners.size(); i-- > 0;)
        ((ServletRequestListener)_servletRequestListeners.get(i)).requestDestroyed(sre);
    }
    int i;
    if (!_servletRequestAttributeListeners.isEmpty())
    {
      for (i = _servletRequestAttributeListeners.size(); i-- > 0;) {
        baseRequest.removeEventListener((EventListener)_servletRequestAttributeListeners.get(i));
      }
    }
  }
  




  public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    DispatcherType dispatch = baseRequest.getDispatcherType();
    boolean new_context = baseRequest.takeNewContext();
    try
    {
      if (new_context) {
        requestInitialized(baseRequest, request);
      }
      switch (dispatch)
      {
      case REQUEST: 
        if (isProtectedTarget(target))
        {
          response.sendError(404);
          baseRequest.setHandled(true); return;
        }
        


        break;
      case ERROR: 
        if (!Boolean.TRUE.equals(baseRequest.getAttribute("org.seleniumhq.jetty9.server.Dispatcher.ERROR")))
        {




          doError(target, baseRequest, request, response); return;
        }
        
        break;
      }
      
      nextHandle(target, baseRequest, request, response);
    }
    finally
    {
      if (new_context) {
        requestDestroyed(baseRequest, request);
      }
    }
  }
  



  protected void enterScope(Request request, Object reason)
  {
    if (!_contextListeners.isEmpty())
    {
      for (ContextScopeListener listener : _contextListeners)
      {
        try
        {
          listener.enterScope(_scontext, request, reason);
        }
        catch (Throwable e)
        {
          LOG.warn(e);
        }
      }
    }
  }
  


  protected void exitScope(Request request)
  {
    int i;
    
    if (!_contextListeners.isEmpty())
    {
      for (i = _contextListeners.size(); i-- > 0;)
      {
        try
        {
          ((ContextScopeListener)_contextListeners.get(i)).exitScope(_scontext, request);
        }
        catch (Throwable e)
        {
          LOG.warn(e);
        }
      }
    }
  }
  






  public void handle(Request request, Runnable runnable)
  {
    ClassLoader old_classloader = null;
    Thread current_thread = null;
    Context old_context = (Context)__context.get();
    

    if (old_context == _scontext)
    {
      runnable.run();
      return;
    }
    

    try
    {
      __context.set(_scontext);
      

      if (_classLoader != null)
      {
        current_thread = Thread.currentThread();
        old_classloader = current_thread.getContextClassLoader();
        current_thread.setContextClassLoader(_classLoader);
      }
      
      enterScope(request, runnable);
      runnable.run();
    }
    finally
    {
      exitScope(request);
      
      __context.set(old_context);
      if (old_classloader != null)
      {
        current_thread.setContextClassLoader(old_classloader);
      }
    }
  }
  




  public void handle(Runnable runnable)
  {
    handle(null, runnable);
  }
  







  public boolean isProtectedTarget(String target)
  {
    if ((target == null) || (_protectedTargets == null)) {
      return false;
    }
    while (target.startsWith("//")) {
      target = URIUtil.compactPath(target);
    }
    for (int i = 0; i < _protectedTargets.length; i++)
    {
      String t = _protectedTargets[i];
      if (StringUtil.startsWithIgnoreCase(target, t))
      {
        if (target.length() == t.length()) {
          return true;
        }
        

        char c = target.charAt(t.length());
        if ((c == '/') || (c == '?') || (c == '#') || (c == ';'))
          return true;
      }
    }
    return false;
  }
  






  public void setProtectedTargets(String[] targets)
  {
    if (targets == null)
    {
      _protectedTargets = null;
      return;
    }
    
    _protectedTargets = ((String[])Arrays.copyOf(targets, targets.length));
  }
  

  public String[] getProtectedTargets()
  {
    if (_protectedTargets == null) {
      return null;
    }
    return (String[])Arrays.copyOf(_protectedTargets, _protectedTargets.length);
  }
  






  public void removeAttribute(String name)
  {
    _attributes.removeAttribute(name);
  }
  








  public void setAttribute(String name, Object value)
  {
    _attributes.setAttribute(name, value);
  }
  





  public void setAttributes(Attributes attributes)
  {
    _attributes.clearAttributes();
    _attributes.addAll(attributes);
  }
  


  public void clearAttributes()
  {
    _attributes.clearAttributes();
  }
  

  public void setManagedAttribute(String name, Object value)
  {
    Object old = _managedAttributes.put(name, value);
    updateBean(old, value);
  }
  





  public void setClassLoader(ClassLoader classLoader)
  {
    _classLoader = classLoader;
  }
  





  public void setContextPath(String contextPath)
  {
    if (contextPath == null) {
      throw new IllegalArgumentException("null contextPath");
    }
    if (contextPath.endsWith("/*"))
    {
      LOG.warn(this + " contextPath ends with /*", new Object[0]);
      contextPath = contextPath.substring(0, contextPath.length() - 2);
    }
    else if ((contextPath.length() > 1) && (contextPath.endsWith("/")))
    {
      LOG.warn(this + " contextPath ends with /", new Object[0]);
      contextPath = contextPath.substring(0, contextPath.length() - 1);
    }
    
    if (contextPath.length() == 0)
    {
      LOG.warn("Empty contextPath", new Object[0]);
      contextPath = "/";
    }
    
    _contextPath = contextPath;
    _contextPathEncoded = URIUtil.encodePath(contextPath);
    
    if ((getServer() != null) && ((getServer().isStarting()) || (getServer().isStarted())))
    {
      Handler[] contextCollections = getServer().getChildHandlersByClass(ContextHandlerCollection.class);
      for (int h = 0; (contextCollections != null) && (h < contextCollections.length); h++) {
        ((ContextHandlerCollection)contextCollections[h]).mapContexts();
      }
    }
  }
  




  public void setDisplayName(String servletContextName)
  {
    _displayName = servletContextName;
  }
  




  public Resource getBaseResource()
  {
    if (_baseResource == null)
      return null;
    return _baseResource;
  }
  




  @ManagedAttribute("document root for context")
  public String getResourceBase()
  {
    if (_baseResource == null)
      return null;
    return _baseResource.toString();
  }
  






  public void setBaseResource(Resource base)
  {
    _baseResource = base;
  }
  







  public void setResourceBase(String resourceBase)
  {
    try
    {
      setBaseResource(newResource(resourceBase));
    }
    catch (Exception e)
    {
      LOG.warn(e.toString(), new Object[0]);
      LOG.debug(e);
      throw new IllegalArgumentException(resourceBase);
    }
  }
  




  public MimeTypes getMimeTypes()
  {
    if (_mimeTypes == null)
      _mimeTypes = new MimeTypes();
    return _mimeTypes;
  }
  





  public void setMimeTypes(MimeTypes mimeTypes)
  {
    _mimeTypes = mimeTypes;
  }
  

  public void setWelcomeFiles(String[] files)
  {
    _welcomeFiles = files;
  }
  






  @ManagedAttribute(value="Partial URIs of directory welcome files", readonly=true)
  public String[] getWelcomeFiles()
  {
    return _welcomeFiles;
  }
  




  @ManagedAttribute("The error handler to use for the context")
  public ErrorHandler getErrorHandler()
  {
    return _errorHandler;
  }
  





  public void setErrorHandler(ErrorHandler errorHandler)
  {
    if (errorHandler != null)
      errorHandler.setServer(getServer());
    updateBean(_errorHandler, errorHandler, true);
    _errorHandler = errorHandler;
  }
  

  @ManagedAttribute("The maximum content size")
  public int getMaxFormContentSize()
  {
    return _maxFormContentSize;
  }
  





  public void setMaxFormContentSize(int maxSize)
  {
    _maxFormContentSize = maxSize;
  }
  

  public int getMaxFormKeys()
  {
    return _maxFormKeys;
  }
  





  public void setMaxFormKeys(int max)
  {
    _maxFormKeys = max;
  }
  




  public boolean isCompactPath()
  {
    return _compactPath;
  }
  





  public void setCompactPath(boolean compactPath)
  {
    _compactPath = compactPath;
  }
  


  public String toString()
  {
    String[] vhosts = getVirtualHosts();
    
    StringBuilder b = new StringBuilder();
    
    Package pkg = getClass().getPackage();
    if (pkg != null)
    {
      String p = pkg.getName();
      if ((p != null) && (p.length() > 0))
      {
        String[] ss = p.split("\\.");
        for (String s : ss)
          b.append(s.charAt(0)).append('.');
      }
    }
    b.append(getClass().getSimpleName()).append('@').append(Integer.toString(hashCode(), 16));
    b.append('{').append(getContextPath()).append(',').append(getBaseResource()).append(',').append(_availability);
    
    if ((vhosts != null) && (vhosts.length > 0))
      b.append(',').append(vhosts[0]);
    b.append('}');
    
    return b.toString();
  }
  
  public synchronized Class<?> loadClass(String className)
    throws ClassNotFoundException
  {
    if (className == null) {
      return null;
    }
    if (_classLoader == null) {
      return Loader.loadClass(className);
    }
    return _classLoader.loadClass(className);
  }
  

  public void addLocaleEncoding(String locale, String encoding)
  {
    if (_localeEncodingMap == null)
      _localeEncodingMap = new HashMap();
    _localeEncodingMap.put(locale, encoding);
  }
  

  public String getLocaleEncoding(String locale)
  {
    if (_localeEncodingMap == null)
      return null;
    String encoding = (String)_localeEncodingMap.get(locale);
    return encoding;
  }
  









  public String getLocaleEncoding(Locale locale)
  {
    if (_localeEncodingMap == null)
      return null;
    String encoding = (String)_localeEncodingMap.get(locale.toString());
    if (encoding == null)
      encoding = (String)_localeEncodingMap.get(locale.getLanguage());
    return encoding;
  }
  






  public Map<String, String> getLocaleEncodings()
  {
    if (_localeEncodingMap == null)
      return null;
    return Collections.unmodifiableMap(_localeEncodingMap);
  }
  


  public Resource getResource(String path)
    throws MalformedURLException
  {
    if ((path == null) || (!path.startsWith("/"))) {
      throw new MalformedURLException(path);
    }
    if (_baseResource == null) {
      return null;
    }
    try
    {
      path = URIUtil.canonicalPath(path);
      Resource resource = _baseResource.addPath(path);
      
      if (checkAlias(path, resource))
        return resource;
      return null;
    }
    catch (Exception e)
    {
      LOG.ignore(e);
    }
    
    return null;
  }
  







  public boolean checkAlias(String path, Resource resource)
  {
    if (resource.isAlias())
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Aliased resource: " + resource + "~=" + resource.getAlias(), new Object[0]);
      }
      
      for (Iterator<AliasCheck> i = _aliasChecks.iterator(); i.hasNext();)
      {
        AliasCheck check = (AliasCheck)i.next();
        if (check.check(path, resource))
        {
          if (LOG.isDebugEnabled())
            LOG.debug("Aliased resource: " + resource + " approved by " + check, new Object[0]);
          return true;
        }
      }
      return false;
    }
    return true;
  }
  






  public Resource newResource(URL url)
    throws IOException
  {
    return Resource.newResource(url);
  }
  






  public Resource newResource(URI uri)
    throws IOException
  {
    return Resource.newResource(uri);
  }
  









  public Resource newResource(String urlOrPath)
    throws IOException
  {
    return Resource.newResource(urlOrPath);
  }
  



  public Set<String> getResourcePaths(String path)
  {
    try
    {
      path = URIUtil.canonicalPath(path);
      Resource resource = getResource(path);
      
      if ((resource != null) && (resource.exists()))
      {
        if (!path.endsWith("/")) {
          path = path + "/";
        }
        String[] l = resource.list();
        if (l != null)
        {
          HashSet<String> set = new HashSet();
          for (int i = 0; i < l.length; i++)
            set.add(path + l[i]);
          return set;
        }
      }
    }
    catch (Exception e)
    {
      LOG.ignore(e);
    }
    return Collections.emptySet();
  }
  

  private String normalizeHostname(String host)
  {
    if (host == null) {
      return null;
    }
    if (host.endsWith(".")) {
      return host.substring(0, host.length() - 1);
    }
    return host;
  }
  





  public void addAliasCheck(AliasCheck check)
  {
    _aliasChecks.add(check);
  }
  




  public List<AliasCheck> getAliasChecks()
  {
    return _aliasChecks;
  }
  




  public void setAliasChecks(List<AliasCheck> checks)
  {
    _aliasChecks.clear();
    _aliasChecks.addAll(checks);
  }
  




  public void clearAliasChecks()
  {
    _aliasChecks.clear();
  }
  









  public class Context
    extends ContextHandler.StaticContext
  {
    protected boolean _enabled = true;
    protected boolean _extendedListenerTypes = false;
    



    protected Context() {}
    


    public ContextHandler getContextHandler()
    {
      return ContextHandler.this;
    }
    





    public ServletContext getContext(String uripath)
    {
      List<ContextHandler> contexts = new ArrayList();
      Handler[] handlers = getServer().getChildHandlersByClass(ContextHandler.class);
      String matched_path = null;
      
      for (Handler handler : handlers)
      {
        if (handler != null)
        {
          ContextHandler ch = (ContextHandler)handler;
          String context_path = ch.getContextPath();
          
          if ((uripath.equals(context_path)) || ((uripath.startsWith(context_path)) && (uripath.charAt(context_path.length()) == '/')) || 
            ("/".equals(context_path)))
          {

            if ((getVirtualHosts() != null) && (getVirtualHosts().length > 0))
            {
              if ((ch.getVirtualHosts() != null) && (ch.getVirtualHosts().length > 0))
              {
                for (String h1 : getVirtualHosts()) {
                  for (String h2 : ch.getVirtualHosts()) {
                    if (h1.equals(h2))
                    {
                      if ((matched_path == null) || (context_path.length() > matched_path.length()))
                      {
                        contexts.clear();
                        matched_path = context_path;
                      }
                      
                      if (matched_path.equals(context_path))
                        contexts.add(ch);
                    }
                  }
                }
              }
            } else {
              if ((matched_path == null) || (context_path.length() > matched_path.length()))
              {
                contexts.clear();
                matched_path = context_path;
              }
              
              if (matched_path.equals(context_path))
                contexts.add(ch);
            }
          }
        }
      }
      if (contexts.size() > 0) {
        return get0_scontext;
      }
      
      matched_path = null;
      for (Handler handler : handlers)
      {
        if (handler != null)
        {
          ContextHandler ch = (ContextHandler)handler;
          String context_path = ch.getContextPath();
          
          if ((uripath.equals(context_path)) || ((uripath.startsWith(context_path)) && (uripath.charAt(context_path.length()) == '/')) || 
            ("/".equals(context_path)))
          {
            if ((matched_path == null) || (context_path.length() > matched_path.length()))
            {
              contexts.clear();
              matched_path = context_path;
            }
            
            if ((matched_path != null) && (matched_path.equals(context_path)))
              contexts.add(ch);
          }
        }
      }
      if (contexts.size() > 0)
        return get0_scontext;
      return null;
    }
    





    public String getMimeType(String file)
    {
      if (_mimeTypes == null)
        return null;
      return _mimeTypes.getMimeByExtension(file);
    }
    







    public RequestDispatcher getRequestDispatcher(String uriInContext)
    {
      if (uriInContext == null) {
        return null;
      }
      if (!uriInContext.startsWith("/")) {
        return null;
      }
      try
      {
        HttpURI uri = new HttpURI(null, null, 0, uriInContext);
        
        String pathInfo = URIUtil.canonicalPath(uri.getDecodedPath());
        if (pathInfo == null) {
          return null;
        }
        String contextPath = getContextPath();
        if ((contextPath != null) && (contextPath.length() > 0)) {
          uri.setPath(URIUtil.addPaths(contextPath, uri.getPath()));
        }
        return new Dispatcher(ContextHandler.this, uri, pathInfo);
      }
      catch (Exception e)
      {
        ContextHandler.LOG.ignore(e);
      }
      return null;
    }
    





    public String getRealPath(String path)
    {
      if (path == null)
        return null;
      if (path.length() == 0) {
        path = "/";
      } else if (path.charAt(0) != '/') {
        path = "/" + path;
      }
      try
      {
        Resource resource = getResource(path);
        if (resource != null)
        {
          File file = resource.getFile();
          if (file != null) {
            return file.getCanonicalPath();
          }
        }
      }
      catch (Exception e) {
        ContextHandler.LOG.ignore(e);
      }
      
      return null;
    }
    

    public URL getResource(String path)
      throws MalformedURLException
    {
      Resource resource = getResource(path);
      if ((resource != null) && (resource.exists()))
        return resource.getURI().toURL();
      return null;
    }
    





    public InputStream getResourceAsStream(String path)
    {
      try
      {
        URL url = getResource(path);
        if (url == null)
          return null;
        Resource r = Resource.newResource(url);
        
        if (r.isDirectory())
          return null;
        return r.getInputStream();
      }
      catch (Exception e)
      {
        ContextHandler.LOG.ignore(e); }
      return null;
    }
    






    public Set<String> getResourcePaths(String path)
    {
      return ContextHandler.this.getResourcePaths(path);
    }
    





    public void log(Exception exception, String msg)
    {
      _logger.warn(msg, exception);
    }
    





    public void log(String msg)
    {
      _logger.info(msg, new Object[0]);
    }
    





    public void log(String message, Throwable throwable)
    {
      _logger.warn(message, throwable);
    }
    





    public String getInitParameter(String name)
    {
      return ContextHandler.this.getInitParameter(name);
    }
    





    public Enumeration<String> getInitParameterNames()
    {
      return ContextHandler.this.getInitParameterNames();
    }
    





    public synchronized Object getAttribute(String name)
    {
      Object o = ContextHandler.this.getAttribute(name);
      if (o == null)
        o = super.getAttribute(name);
      return o;
    }
    





    public synchronized Enumeration<String> getAttributeNames()
    {
      HashSet<String> set = new HashSet();
      Enumeration<String> e = super.getAttributeNames();
      while (e.hasMoreElements())
        set.add(e.nextElement());
      e = _attributes.getAttributeNames();
      while (e.hasMoreElements()) {
        set.add(e.nextElement());
      }
      return Collections.enumeration(set);
    }
    





    public synchronized void setAttribute(String name, Object value)
    {
      Object old_value = super.getAttribute(name);
      
      if (value == null) {
        super.removeAttribute(name);
      } else
        super.setAttribute(name, value);
      ServletContextAttributeEvent event;
      if (!_servletContextAttributeListeners.isEmpty())
      {
        event = new ServletContextAttributeEvent(_scontext, name, old_value == null ? value : old_value);
        
        for (ServletContextAttributeListener l : _servletContextAttributeListeners)
        {
          if (old_value == null) {
            l.attributeAdded(event);
          } else if (value == null) {
            l.attributeRemoved(event);
          } else {
            l.attributeReplaced(event);
          }
        }
      }
    }
    




    public synchronized void removeAttribute(String name)
    {
      Object old_value = super.getAttribute(name);
      super.removeAttribute(name);
      ServletContextAttributeEvent event; if ((old_value != null) && (!_servletContextAttributeListeners.isEmpty()))
      {
        event = new ServletContextAttributeEvent(_scontext, name, old_value);
        
        for (ServletContextAttributeListener l : _servletContextAttributeListeners) {
          l.attributeRemoved(event);
        }
      }
    }
    




    public String getServletContextName()
    {
      String name = getDisplayName();
      if (name == null)
        name = ContextHandler.this.getContextPath();
      return name;
    }
    


    public String getContextPath()
    {
      if ((_contextPath != null) && (_contextPath.equals("/"))) {
        return "";
      }
      return _contextPath;
    }
    


    public String toString()
    {
      return "ServletContext@" + ContextHandler.this.toString();
    }
    


    public boolean setInitParameter(String name, String value)
    {
      if (ContextHandler.this.getInitParameter(name) != null)
        return false;
      getInitParams().put(name, value);
      return true;
    }
    

    public void addListener(String className)
    {
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      
      try
      {
        Class<? extends EventListener> clazz = _classLoader == null ? Loader.loadClass(className) : _classLoader.loadClass(className);
        addListener(clazz);
      }
      catch (ClassNotFoundException e)
      {
        throw new IllegalArgumentException(e);
      }
    }
    

    public <T extends EventListener> void addListener(T t)
    {
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      checkListener(t.getClass());
      
      addEventListener(t);
      addProgrammaticListener(t);
    }
    

    public void addListener(Class<? extends EventListener> listenerClass)
    {
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      try
      {
        EventListener e = createListener(listenerClass);
        addListener(e);
      }
      catch (ServletException e)
      {
        throw new IllegalArgumentException(e);
      }
    }
    
    public <T extends EventListener> T createListener(Class<T> clazz)
      throws ServletException
    {
      try
      {
        return (EventListener)createInstance(clazz);
      }
      catch (Exception e)
      {
        throw new ServletException(e);
      }
    }
    
    public void checkListener(Class<? extends EventListener> listener)
      throws IllegalStateException
    {
      boolean ok = false;
      int startIndex = isExtendedListenerTypes() ? 0 : 1;
      for (int i = startIndex; i < ContextHandler.SERVLET_LISTENER_TYPES.length; i++)
      {
        if (ContextHandler.SERVLET_LISTENER_TYPES[i].isAssignableFrom(listener))
        {
          ok = true;
          break;
        }
      }
      if (!ok) {
        throw new IllegalArgumentException("Inappropriate listener class " + listener.getName());
      }
    }
    
    public void setExtendedListenerTypes(boolean extended) {
      _extendedListenerTypes = extended;
    }
    
    public boolean isExtendedListenerTypes()
    {
      return _extendedListenerTypes;
    }
    

    public ClassLoader getClassLoader()
    {
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      
      if (!_usingSecurityManager) {
        return _classLoader;
      }
      


      try
      {
        Class<?> reflect = Loader.loadClass("sun.reflect.Reflection");
        Method getCallerClass = reflect.getMethod("getCallerClass", new Class[] { Integer.TYPE });
        Class<?> caller = (Class)getCallerClass.invoke(null, new Object[] { Integer.valueOf(2) });
        
        boolean ok = false;
        ClassLoader callerLoader = caller.getClassLoader();
        while ((!ok) && (callerLoader != null))
        {
          if (callerLoader == _classLoader) {
            ok = true;
          } else {
            callerLoader = callerLoader.getParent();
          }
        }
        if (ok) {
          return _classLoader;
        }
      }
      catch (Exception e) {
        ContextHandler.LOG.warn("Unable to check classloader of caller", e);
      }
      
      AccessController.checkPermission(new RuntimePermission("getClassLoader"));
      return _classLoader;
    }
    


    public JspConfigDescriptor getJspConfigDescriptor()
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    


    public void setJspConfigDescriptor(JspConfigDescriptor d) {}
    


    public void declareRoles(String... roleNames)
    {
      if (!isStarting())
        throw new IllegalStateException();
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
    }
    
    public void setEnabled(boolean enabled) {
      _enabled = enabled;
    }
    
    public boolean isEnabled()
    {
      return _enabled;
    }
    
    public <T> T createInstance(Class<T> clazz) throws Exception
    {
      T o = clazz.newInstance();
      return o;
    }
    

    public String getVirtualServerName()
    {
      String[] hosts = getVirtualHosts();
      if ((hosts != null) && (hosts.length > 0))
        return hosts[0];
      return null;
    }
  }
  
  public static class StaticContext extends AttributesMap implements ServletContext
  {
    private int _effectiveMajorVersion = 3;
    private int _effectiveMinorVersion = 1;
    


    public StaticContext() {}
    


    public ServletContext getContext(String uripath)
    {
      return null;
    }
    

    public int getMajorVersion()
    {
      return 3;
    }
    

    public String getMimeType(String file)
    {
      return null;
    }
    

    public int getMinorVersion()
    {
      return 1;
    }
    

    public RequestDispatcher getNamedDispatcher(String name)
    {
      return null;
    }
    

    public RequestDispatcher getRequestDispatcher(String uriInContext)
    {
      return null;
    }
    

    public String getRealPath(String path)
    {
      return null;
    }
    
    public URL getResource(String path)
      throws MalformedURLException
    {
      return null;
    }
    

    public InputStream getResourceAsStream(String path)
    {
      return null;
    }
    

    public Set<String> getResourcePaths(String path)
    {
      return null;
    }
    

    public String getServerInfo()
    {
      return ContextHandler.__serverInfo;
    }
    
    @Deprecated
    public Servlet getServlet(String name)
      throws ServletException
    {
      return null;
    }
    


    @Deprecated
    public Enumeration<String> getServletNames()
    {
      return Collections.enumeration(Collections.EMPTY_LIST);
    }
    


    @Deprecated
    public Enumeration<Servlet> getServlets()
    {
      return Collections.enumeration(Collections.EMPTY_LIST);
    }
    

    public void log(Exception exception, String msg)
    {
      ContextHandler.LOG.warn(msg, exception);
    }
    

    public void log(String msg)
    {
      ContextHandler.LOG.info(msg, new Object[0]);
    }
    

    public void log(String message, Throwable throwable)
    {
      ContextHandler.LOG.warn(message, throwable);
    }
    

    public String getInitParameter(String name)
    {
      return null;
    }
    


    public Enumeration<String> getInitParameterNames()
    {
      return Collections.enumeration(Collections.EMPTY_LIST);
    }
    


    public String getServletContextName()
    {
      return "No Context";
    }
    

    public String getContextPath()
    {
      return null;
    }
    

    public boolean setInitParameter(String name, String value)
    {
      return false;
    }
    

    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public FilterRegistration.Dynamic addFilter(String filterName, String className)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public ServletRegistration.Dynamic addServlet(String servletName, String className)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    
    public <T extends Filter> T createFilter(Class<T> c)
      throws ServletException
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    
    public <T extends Servlet> T createServlet(Class<T> c)
      throws ServletException
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public Set<SessionTrackingMode> getDefaultSessionTrackingModes()
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes()
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public FilterRegistration getFilterRegistration(String filterName)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public Map<String, ? extends FilterRegistration> getFilterRegistrations()
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public ServletRegistration getServletRegistration(String servletName)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public Map<String, ? extends ServletRegistration> getServletRegistrations()
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public SessionCookieConfig getSessionCookieConfig()
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
    }
    

    public void addListener(String className)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
    }
    

    public <T extends EventListener> void addListener(T t)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
    }
    

    public void addListener(Class<? extends EventListener> listenerClass)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
    }
    
    public <T extends EventListener> T createListener(Class<T> clazz)
      throws ServletException
    {
      try
      {
        return (EventListener)clazz.newInstance();
      }
      catch (InstantiationException e)
      {
        throw new ServletException(e);
      }
      catch (IllegalAccessException e)
      {
        throw new ServletException(e);
      }
    }
    

    public ClassLoader getClassLoader()
    {
      return ContextHandler.class.getClassLoader();
    }
    

    public int getEffectiveMajorVersion()
    {
      return _effectiveMajorVersion;
    }
    

    public int getEffectiveMinorVersion()
    {
      return _effectiveMinorVersion;
    }
    
    public void setEffectiveMajorVersion(int v)
    {
      _effectiveMajorVersion = v;
    }
    
    public void setEffectiveMinorVersion(int v)
    {
      _effectiveMinorVersion = v;
    }
    

    public JspConfigDescriptor getJspConfigDescriptor()
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
      return null;
    }
    

    public void declareRoles(String... roleNames)
    {
      ContextHandler.LOG.warn("Unimplemented - use org.eclipse.jetty.servlet.ServletContextHandler", new Object[0]);
    }
    

    public String getVirtualServerName()
    {
      return null;
    }
  }
  




  public static abstract interface AliasCheck
  {
    public abstract boolean check(String paramString, Resource paramResource);
  }
  




  public static class ApproveAliases
    implements ContextHandler.AliasCheck
  {
    public ApproveAliases() {}
    




    public boolean check(String path, Resource resource)
    {
      return true;
    }
  }
  


  public static class ApproveNonExistentDirectoryAliases
    implements ContextHandler.AliasCheck
  {
    public ApproveNonExistentDirectoryAliases() {}
    

    public boolean check(String path, Resource resource)
    {
      if (resource.exists()) {
        return false;
      }
      String a = resource.getAlias().toString();
      String r = resource.getURI().toString();
      
      if (a.length() > r.length())
        return (a.startsWith(r)) && (a.length() == r.length() + 1) && (a.endsWith("/"));
      if (a.length() < r.length()) {
        return (r.startsWith(a)) && (r.length() == a.length() + 1) && (r.endsWith("/"));
      }
      return a.equals(r);
    }
  }
  
  public static abstract interface ContextScopeListener
    extends EventListener
  {
    public abstract void enterScope(ContextHandler.Context paramContext, Request paramRequest, Object paramObject);
    
    public abstract void exitScope(ContextHandler.Context paramContext, Request paramRequest);
  }
}
