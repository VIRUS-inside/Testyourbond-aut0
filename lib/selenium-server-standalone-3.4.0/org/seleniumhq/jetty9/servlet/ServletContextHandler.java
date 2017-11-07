package org.seleniumhq.jetty9.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletSecurityElement;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.descriptor.JspPropertyGroupDescriptor;
import javax.servlet.descriptor.TaglibDescriptor;
import org.seleniumhq.jetty9.security.ConstraintAware;
import org.seleniumhq.jetty9.security.ConstraintMapping;
import org.seleniumhq.jetty9.security.ConstraintSecurityHandler;
import org.seleniumhq.jetty9.security.SecurityHandler;
import org.seleniumhq.jetty9.server.Dispatcher;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.HandlerContainer;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;
import org.seleniumhq.jetty9.server.handler.ErrorHandler;
import org.seleniumhq.jetty9.server.handler.HandlerCollection;
import org.seleniumhq.jetty9.server.handler.HandlerWrapper;
import org.seleniumhq.jetty9.server.handler.gzip.GzipHandler;
import org.seleniumhq.jetty9.server.session.SessionHandler;
import org.seleniumhq.jetty9.util.DecoratedObjectFactory;
import org.seleniumhq.jetty9.util.Decorator;
import org.seleniumhq.jetty9.util.DeprecationWarning;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.LifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;






























@ManagedObject("Servlet Context Handler")
public class ServletContextHandler
  extends ContextHandler
{
  private static final Logger LOG = Log.getLogger(ServletContextHandler.class);
  
  public static final int SESSIONS = 1;
  
  public static final int SECURITY = 2;
  
  public static final int GZIP = 4;
  
  public static final int NO_SESSIONS = 0;
  public static final int NO_SECURITY = 0;
  protected final DecoratedObjectFactory _objFactory;
  protected Class<? extends SecurityHandler> _defaultSecurityHandlerClass = ConstraintSecurityHandler.class;
  
  protected SessionHandler _sessionHandler;
  protected SecurityHandler _securityHandler;
  protected ServletHandler _servletHandler;
  protected GzipHandler _gzipHandler;
  protected int _options;
  protected JspConfigDescriptor _jspConfig;
  
  public ServletContextHandler()
  {
    this(null, null, null, null, null);
  }
  

  public ServletContextHandler(int options)
  {
    this(null, null, options);
  }
  

  public ServletContextHandler(HandlerContainer parent, String contextPath)
  {
    this(parent, contextPath, null, null, null, null);
  }
  

  public ServletContextHandler(HandlerContainer parent, String contextPath, int options)
  {
    this(parent, contextPath, null, null, null, null, options);
  }
  

  public ServletContextHandler(HandlerContainer parent, String contextPath, boolean sessions, boolean security)
  {
    this(parent, contextPath, (sessions ? 1 : 0) | (security ? 2 : 0));
  }
  

  public ServletContextHandler(HandlerContainer parent, SessionHandler sessionHandler, SecurityHandler securityHandler, ServletHandler servletHandler, ErrorHandler errorHandler)
  {
    this(parent, null, sessionHandler, securityHandler, servletHandler, errorHandler);
  }
  

  public ServletContextHandler(HandlerContainer parent, String contextPath, SessionHandler sessionHandler, SecurityHandler securityHandler, ServletHandler servletHandler, ErrorHandler errorHandler)
  {
    this(parent, contextPath, sessionHandler, securityHandler, servletHandler, errorHandler, 0);
  }
  

  public ServletContextHandler(HandlerContainer parent, String contextPath, SessionHandler sessionHandler, SecurityHandler securityHandler, ServletHandler servletHandler, ErrorHandler errorHandler, int options)
  {
    super((ContextHandler.Context)null);
    _options = options;
    _scontext = new Context();
    _sessionHandler = sessionHandler;
    _securityHandler = securityHandler;
    _servletHandler = servletHandler;
    
    _objFactory = new DecoratedObjectFactory();
    _objFactory.addDecorator(new DeprecationWarning());
    
    if (contextPath != null) {
      setContextPath(contextPath);
    }
    if ((parent instanceof HandlerWrapper)) {
      ((HandlerWrapper)parent).setHandler(this);
    } else if ((parent instanceof HandlerCollection)) {
      ((HandlerCollection)parent).addHandler(this);
    }
    

    relinkHandlers();
    
    if (errorHandler != null) {
      setErrorHandler(errorHandler);
    }
  }
  
  public void setHandler(Handler handler)
  {
    if (handler != null)
      LOG.warn("ServletContextHandler.setHandler should not be called directly. Use insertHandler or setSessionHandler etc.", new Object[0]);
    super.setHandler(handler);
  }
  
  private void doSetHandler(HandlerWrapper wrapper, Handler handler)
  {
    if (wrapper == this) {
      super.setHandler(handler);
    } else {
      wrapper.setHandler(handler);
    }
  }
  
  private void relinkHandlers()
  {
    HandlerWrapper handler = this;
    

    if (getSessionHandler() != null)
    {

      while ((!(handler.getHandler() instanceof SessionHandler)) && 
        (!(handler.getHandler() instanceof SecurityHandler)) && 
        (!(handler.getHandler() instanceof GzipHandler)) && 
        (!(handler.getHandler() instanceof ServletHandler)) && 
        ((handler.getHandler() instanceof HandlerWrapper))) {
        handler = (HandlerWrapper)handler.getHandler();
      }
      if (handler.getHandler() != _sessionHandler)
        doSetHandler(handler, _sessionHandler);
      handler = _sessionHandler;
    }
    

    if (getSecurityHandler() != null)
    {
      while ((!(handler.getHandler() instanceof SecurityHandler)) && 
        (!(handler.getHandler() instanceof GzipHandler)) && 
        (!(handler.getHandler() instanceof ServletHandler)) && 
        ((handler.getHandler() instanceof HandlerWrapper))) {
        handler = (HandlerWrapper)handler.getHandler();
      }
      if (handler.getHandler() != _securityHandler)
        doSetHandler(handler, _securityHandler);
      handler = _securityHandler;
    }
    

    if (getGzipHandler() != null)
    {
      while ((!(handler.getHandler() instanceof GzipHandler)) && 
        (!(handler.getHandler() instanceof ServletHandler)) && 
        ((handler.getHandler() instanceof HandlerWrapper))) {
        handler = (HandlerWrapper)handler.getHandler();
      }
      if (handler.getHandler() != _gzipHandler)
        doSetHandler(handler, _gzipHandler);
      handler = _gzipHandler;
    }
    


    if (getServletHandler() != null)
    {
      while ((!(handler.getHandler() instanceof ServletHandler)) && 
        ((handler.getHandler() instanceof HandlerWrapper))) {
        handler = (HandlerWrapper)handler.getHandler();
      }
      if (handler.getHandler() != _servletHandler)
        doSetHandler(handler, _servletHandler);
      handler = _servletHandler;
    }
  }
  


  protected void doStart()
    throws Exception
  {
    getServletContext().setAttribute(DecoratedObjectFactory.ATTR, _objFactory);
    super.doStart();
  }
  




  protected void doStop()
    throws Exception
  {
    super.doStop();
    _objFactory.clear();
  }
  




  public Class<? extends SecurityHandler> getDefaultSecurityHandlerClass()
  {
    return _defaultSecurityHandlerClass;
  }
  




  public void setDefaultSecurityHandlerClass(Class<? extends SecurityHandler> defaultSecurityHandlerClass)
  {
    _defaultSecurityHandlerClass = defaultSecurityHandlerClass;
  }
  

  protected SessionHandler newSessionHandler()
  {
    return new SessionHandler();
  }
  

  protected SecurityHandler newSecurityHandler()
  {
    try
    {
      return (SecurityHandler)_defaultSecurityHandlerClass.newInstance();
    }
    catch (Exception e)
    {
      throw new IllegalStateException(e);
    }
  }
  

  protected ServletHandler newServletHandler()
  {
    return new ServletHandler();
  }
  






  protected void startContext()
    throws Exception
  {
    ServletContainerInitializerCaller sciBean = (ServletContainerInitializerCaller)getBean(ServletContainerInitializerCaller.class);
    if (sciBean != null) {
      sciBean.start();
    }
    if (_servletHandler != null)
    {


      if (_servletHandler.getListeners() != null)
      {
        for (ListenerHolder holder : _servletHandler.getListeners())
        {
          _objFactory.decorate(holder.getListener());
        }
      }
    }
    
    super.startContext();
    

    if (_servletHandler != null) {
      _servletHandler.initialize();
    }
  }
  
  protected void stopContext()
    throws Exception
  {
    super.stopContext();
  }
  




  @ManagedAttribute(value="context security handler", readonly=true)
  public SecurityHandler getSecurityHandler()
  {
    if ((_securityHandler == null) && ((_options & 0x2) != 0) && (!isStarted())) {
      _securityHandler = newSecurityHandler();
    }
    return _securityHandler;
  }
  




  @ManagedAttribute(value="context servlet handler", readonly=true)
  public ServletHandler getServletHandler()
  {
    if ((_servletHandler == null) && (!isStarted()))
      _servletHandler = newServletHandler();
    return _servletHandler;
  }
  




  @ManagedAttribute(value="context session handler", readonly=true)
  public SessionHandler getSessionHandler()
  {
    if ((_sessionHandler == null) && ((_options & 0x1) != 0) && (!isStarted()))
      _sessionHandler = newSessionHandler();
    return _sessionHandler;
  }
  




  @ManagedAttribute(value="context gzip handler", readonly=true)
  public GzipHandler getGzipHandler()
  {
    if ((_gzipHandler == null) && ((_options & 0x4) != 0) && (!isStarted()))
      _gzipHandler = new GzipHandler();
    return _gzipHandler;
  }
  






  public ServletHolder addServlet(String className, String pathSpec)
  {
    return getServletHandler().addServletWithMapping(className, pathSpec);
  }
  






  public ServletHolder addServlet(Class<? extends Servlet> servlet, String pathSpec)
  {
    return getServletHandler().addServletWithMapping(servlet, pathSpec);
  }
  





  public void addServlet(ServletHolder servlet, String pathSpec)
  {
    getServletHandler().addServletWithMapping(servlet, pathSpec);
  }
  






  public void addFilter(FilterHolder holder, String pathSpec, EnumSet<DispatcherType> dispatches)
  {
    getServletHandler().addFilterWithMapping(holder, pathSpec, dispatches);
  }
  







  public FilterHolder addFilter(Class<? extends Filter> filterClass, String pathSpec, EnumSet<DispatcherType> dispatches)
  {
    return getServletHandler().addFilterWithMapping(filterClass, pathSpec, dispatches);
  }
  







  public FilterHolder addFilter(String filterClass, String pathSpec, EnumSet<DispatcherType> dispatches)
  {
    return getServletHandler().addFilterWithMapping(filterClass, pathSpec, dispatches);
  }
  




  protected ServletRegistration.Dynamic dynamicHolderAdded(ServletHolder holder)
  {
    return holder.getRegistration();
  }
  




  protected void addRoles(String... roleNames)
  {
    if ((_securityHandler != null) && ((_securityHandler instanceof ConstraintAware)))
    {
      HashSet<String> union = new HashSet();
      Set<String> existing = ((ConstraintAware)_securityHandler).getRoles();
      if (existing != null)
        union.addAll(existing);
      union.addAll(Arrays.asList(roleNames));
      ((ConstraintSecurityHandler)_securityHandler).setRoles(union);
    }
  }
  








  public Set<String> setServletSecurity(ServletRegistration.Dynamic registration, ServletSecurityElement servletSecurityElement)
  {
    Collection<String> pathSpecs = registration.getMappings();
    if (pathSpecs != null)
    {
      for (String pathSpec : pathSpecs)
      {
        List<ConstraintMapping> mappings = ConstraintSecurityHandler.createConstraintsWithMappingsForPath(registration.getName(), pathSpec, servletSecurityElement);
        for (ConstraintMapping m : mappings)
          ((ConstraintAware)getSecurityHandler()).addConstraintMapping(m);
      }
    }
    return Collections.emptySet();
  }
  


  public void callContextInitialized(ServletContextListener l, ServletContextEvent e)
  {
    try
    {
      if (isProgrammaticListener(l)) {
        getServletContext().setEnabled(false);
      }
      super.callContextInitialized(l, e);
      



      getServletContext().setEnabled(true); } finally { getServletContext().setEnabled(true);
    }
  }
  


  public void callContextDestroyed(ServletContextListener l, ServletContextEvent e)
  {
    super.callContextDestroyed(l, e);
  }
  
  private boolean replaceHandler(Handler handler, Handler replace)
  {
    HandlerWrapper wrapper = this;
    for (;;)
    {
      if (wrapper.getHandler() == handler)
      {
        doSetHandler(wrapper, replace);
        return true;
      }
      
      if (!(wrapper.getHandler() instanceof HandlerWrapper))
        return false;
      wrapper = (HandlerWrapper)wrapper.getHandler();
    }
  }
  




  public void setSessionHandler(SessionHandler sessionHandler)
  {
    if (isStarted()) {
      throw new IllegalStateException("STARTED");
    }
    Handler next = null;
    if (_sessionHandler != null)
    {
      next = _sessionHandler.getHandler();
      _sessionHandler.setHandler(null);
      replaceHandler(_sessionHandler, sessionHandler);
    }
    
    _sessionHandler = sessionHandler;
    if ((next != null) && (_sessionHandler.getHandler() == null))
      _sessionHandler.setHandler(next);
    relinkHandlers();
  }
  




  public void setSecurityHandler(SecurityHandler securityHandler)
  {
    if (isStarted()) {
      throw new IllegalStateException("STARTED");
    }
    Handler next = null;
    if (_securityHandler != null)
    {
      next = _securityHandler.getHandler();
      _securityHandler.setHandler(null);
      replaceHandler(_securityHandler, securityHandler);
    }
    
    _securityHandler = securityHandler;
    if ((next != null) && (_securityHandler.getHandler() == null))
      _securityHandler.setHandler(next);
    relinkHandlers();
  }
  





  public void setGzipHandler(GzipHandler gzipHandler)
  {
    if (isStarted()) {
      throw new IllegalStateException("STARTED");
    }
    Handler next = null;
    if (_gzipHandler != null)
    {
      next = _gzipHandler.getHandler();
      _gzipHandler.setHandler(null);
      replaceHandler(_gzipHandler, gzipHandler);
    }
    
    _gzipHandler = gzipHandler;
    if ((next != null) && (_gzipHandler.getHandler() == null))
      _gzipHandler.setHandler(next);
    relinkHandlers();
  }
  




  public void setServletHandler(ServletHandler servletHandler)
  {
    if (isStarted()) {
      throw new IllegalStateException("STARTED");
    }
    Handler next = null;
    if (_servletHandler != null)
    {
      next = _servletHandler.getHandler();
      _servletHandler.setHandler(null);
      replaceHandler(_servletHandler, servletHandler);
    }
    _servletHandler = servletHandler;
    if ((next != null) && (_servletHandler.getHandler() == null))
      _servletHandler.setHandler(next);
    relinkHandlers();
  }
  






  public void insertHandler(HandlerWrapper handler)
  {
    if ((handler instanceof SessionHandler)) {
      setSessionHandler((SessionHandler)handler);
    } else if ((handler instanceof SecurityHandler)) {
      setSecurityHandler((SecurityHandler)handler);
    } else if ((handler instanceof GzipHandler)) {
      setGzipHandler((GzipHandler)handler);
    } else if ((handler instanceof ServletHandler)) {
      setServletHandler((ServletHandler)handler);
    }
    else {
      HandlerWrapper tail = handler;
      while ((tail.getHandler() instanceof HandlerWrapper))
        tail = (HandlerWrapper)tail.getHandler();
      if (tail.getHandler() != null) {
        throw new IllegalArgumentException("bad tail of inserted wrapper chain");
      }
      
      HandlerWrapper h = this;
      while ((h.getHandler() instanceof HandlerWrapper))
      {
        HandlerWrapper wrapper = (HandlerWrapper)h.getHandler();
        if (((wrapper instanceof SessionHandler)) || ((wrapper instanceof SecurityHandler)) || ((wrapper instanceof ServletHandler))) {
          break;
        }
        
        h = wrapper;
      }
      
      Handler next = h.getHandler();
      doSetHandler(h, handler);
      doSetHandler(tail, next);
    }
    relinkHandlers();
  }
  






  public DecoratedObjectFactory getObjectFactory()
  {
    return _objFactory;
  }
  





  @Deprecated
  public List<Decorator> getDecorators()
  {
    List<Decorator> ret = new ArrayList();
    for (Decorator decorator : _objFactory)
    {
      ret.add(new LegacyDecorator(decorator));
    }
    return Collections.unmodifiableList(ret);
  }
  





  @Deprecated
  public void setDecorators(List<Decorator> decorators)
  {
    _objFactory.setDecorators(decorators);
  }
  





  @Deprecated
  public void addDecorator(Decorator decorator)
  {
    _objFactory.addDecorator(decorator);
  }
  

  void destroyServlet(Servlet servlet)
  {
    _objFactory.destroy(servlet);
  }
  



  void destroyFilter(Filter filter) { _objFactory.destroy(filter); }
  
  public static abstract interface ServletContainerInitializerCaller extends LifeCycle
  {}
  
  public static class JspPropertyGroup implements JspPropertyGroupDescriptor {
    private List<String> _urlPatterns = new ArrayList();
    private String _elIgnored;
    private String _pageEncoding;
    private String _scriptingInvalid;
    private String _isXml;
    private List<String> _includePreludes = new ArrayList();
    private List<String> _includeCodas = new ArrayList();
    
    private String _deferredSyntaxAllowedAsLiteral;
    
    private String _trimDirectiveWhitespaces;
    
    private String _defaultContentType;
    private String _buffer;
    private String _errorOnUndeclaredNamespace;
    
    public JspPropertyGroup() {}
    
    public Collection<String> getUrlPatterns()
    {
      return new ArrayList(_urlPatterns);
    }
    
    public void addUrlPattern(String s)
    {
      if (!_urlPatterns.contains(s)) {
        _urlPatterns.add(s);
      }
    }
    


    public String getElIgnored()
    {
      return _elIgnored;
    }
    
    public void setElIgnored(String s)
    {
      _elIgnored = s;
    }
    



    public String getPageEncoding()
    {
      return _pageEncoding;
    }
    
    public void setPageEncoding(String pageEncoding)
    {
      _pageEncoding = pageEncoding;
    }
    
    public void setScriptingInvalid(String scriptingInvalid)
    {
      _scriptingInvalid = scriptingInvalid;
    }
    
    public void setIsXml(String isXml)
    {
      _isXml = isXml;
    }
    
    public void setDeferredSyntaxAllowedAsLiteral(String deferredSyntaxAllowedAsLiteral)
    {
      _deferredSyntaxAllowedAsLiteral = deferredSyntaxAllowedAsLiteral;
    }
    
    public void setTrimDirectiveWhitespaces(String trimDirectiveWhitespaces)
    {
      _trimDirectiveWhitespaces = trimDirectiveWhitespaces;
    }
    
    public void setDefaultContentType(String defaultContentType)
    {
      _defaultContentType = defaultContentType;
    }
    
    public void setBuffer(String buffer)
    {
      _buffer = buffer;
    }
    
    public void setErrorOnUndeclaredNamespace(String errorOnUndeclaredNamespace)
    {
      _errorOnUndeclaredNamespace = errorOnUndeclaredNamespace;
    }
    



    public String getScriptingInvalid()
    {
      return _scriptingInvalid;
    }
    



    public String getIsXml()
    {
      return _isXml;
    }
    



    public Collection<String> getIncludePreludes()
    {
      return new ArrayList(_includePreludes);
    }
    
    public void addIncludePrelude(String prelude)
    {
      if (!_includePreludes.contains(prelude)) {
        _includePreludes.add(prelude);
      }
    }
    


    public Collection<String> getIncludeCodas()
    {
      return new ArrayList(_includeCodas);
    }
    
    public void addIncludeCoda(String coda)
    {
      if (!_includeCodas.contains(coda)) {
        _includeCodas.add(coda);
      }
    }
    


    public String getDeferredSyntaxAllowedAsLiteral()
    {
      return _deferredSyntaxAllowedAsLiteral;
    }
    



    public String getTrimDirectiveWhitespaces()
    {
      return _trimDirectiveWhitespaces;
    }
    



    public String getDefaultContentType()
    {
      return _defaultContentType;
    }
    



    public String getBuffer()
    {
      return _buffer;
    }
    



    public String getErrorOnUndeclaredNamespace()
    {
      return _errorOnUndeclaredNamespace;
    }
    
    public String toString()
    {
      StringBuffer sb = new StringBuffer();
      sb.append("JspPropertyGroupDescriptor:");
      sb.append(" el-ignored=" + _elIgnored);
      sb.append(" is-xml=" + _isXml);
      sb.append(" page-encoding=" + _pageEncoding);
      sb.append(" scripting-invalid=" + _scriptingInvalid);
      sb.append(" deferred-syntax-allowed-as-literal=" + _deferredSyntaxAllowedAsLiteral);
      sb.append(" trim-directive-whitespaces" + _trimDirectiveWhitespaces);
      sb.append(" default-content-type=" + _defaultContentType);
      sb.append(" buffer=" + _buffer);
      sb.append(" error-on-undeclared-namespace=" + _errorOnUndeclaredNamespace);
      for (String prelude : _includePreludes)
        sb.append(" include-prelude=" + prelude);
      for (String coda : _includeCodas)
        sb.append(" include-coda=" + coda);
      return sb.toString();
    }
  }
  

  public static class TagLib
    implements TaglibDescriptor
  {
    private String _uri;
    private String _location;
    
    public TagLib() {}
    
    public String getTaglibURI()
    {
      return _uri;
    }
    
    public void setTaglibURI(String uri)
    {
      _uri = uri;
    }
    



    public String getTaglibLocation()
    {
      return _location;
    }
    
    public void setTaglibLocation(String location)
    {
      _location = location;
    }
    
    public String toString()
    {
      return "TagLibDescriptor: taglib-uri=" + _uri + " location=" + _location;
    }
  }
  

  public static class JspConfig
    implements JspConfigDescriptor
  {
    private List<TaglibDescriptor> _taglibs = new ArrayList();
    private List<JspPropertyGroupDescriptor> _jspPropertyGroups = new ArrayList();
    


    public JspConfig() {}
    

    public Collection<TaglibDescriptor> getTaglibs()
    {
      return new ArrayList(_taglibs);
    }
    
    public void addTaglibDescriptor(TaglibDescriptor d)
    {
      _taglibs.add(d);
    }
    



    public Collection<JspPropertyGroupDescriptor> getJspPropertyGroups()
    {
      return new ArrayList(_jspPropertyGroups);
    }
    
    public void addJspPropertyGroup(JspPropertyGroupDescriptor g)
    {
      _jspPropertyGroups.add(g);
    }
    
    public String toString()
    {
      StringBuffer sb = new StringBuffer();
      sb.append("JspConfigDescriptor: \n");
      for (TaglibDescriptor taglib : _taglibs)
        sb.append(taglib + "\n");
      for (JspPropertyGroupDescriptor jpg : _jspPropertyGroups)
        sb.append(jpg + "\n");
      return sb.toString();
    }
  }
  
  public class Context extends ContextHandler.Context {
    public Context() {
      super();
    }
    




    public RequestDispatcher getNamedDispatcher(String name)
    {
      ContextHandler context = ServletContextHandler.this;
      if (_servletHandler == null)
        return null;
      ServletHolder holder = _servletHandler.getServlet(name);
      if ((holder == null) || (!holder.isEnabled()))
        return null;
      return new Dispatcher(context, name);
    }
    





    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass)
    {
      if (isStarted()) {
        throw new IllegalStateException();
      }
      if ((filterName == null) || ("".equals(filterName.trim()))) {
        throw new IllegalStateException("Missing filter name");
      }
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      ServletHandler handler = getServletHandler();
      FilterHolder holder = handler.getFilter(filterName);
      if (holder == null)
      {

        holder = handler.newFilterHolder(Source.JAVAX_API);
        holder.setName(filterName);
        holder.setHeldClass(filterClass);
        handler.addFilter(holder);
        return holder.getRegistration();
      }
      if ((holder.getClassName() == null) && (holder.getHeldClass() == null))
      {

        holder.setHeldClass(filterClass);
        return holder.getRegistration();
      }
      
      return null;
    }
    





    public FilterRegistration.Dynamic addFilter(String filterName, String className)
    {
      if (isStarted()) {
        throw new IllegalStateException();
      }
      if ((filterName == null) || ("".equals(filterName.trim()))) {
        throw new IllegalStateException("Missing filter name");
      }
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      ServletHandler handler = getServletHandler();
      FilterHolder holder = handler.getFilter(filterName);
      if (holder == null)
      {

        holder = handler.newFilterHolder(Source.JAVAX_API);
        holder.setName(filterName);
        holder.setClassName(className);
        handler.addFilter(holder);
        return holder.getRegistration();
      }
      if ((holder.getClassName() == null) && (holder.getHeldClass() == null))
      {

        holder.setClassName(className);
        return holder.getRegistration();
      }
      
      return null;
    }
    






    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter)
    {
      if (isStarted()) {
        throw new IllegalStateException();
      }
      if ((filterName == null) || ("".equals(filterName.trim()))) {
        throw new IllegalStateException("Missing filter name");
      }
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      ServletHandler handler = getServletHandler();
      FilterHolder holder = handler.getFilter(filterName);
      if (holder == null)
      {

        holder = handler.newFilterHolder(Source.JAVAX_API);
        holder.setName(filterName);
        holder.setFilter(filter);
        handler.addFilter(holder);
        return holder.getRegistration();
      }
      
      if ((holder.getClassName() == null) && (holder.getHeldClass() == null))
      {

        holder.setFilter(filter);
        return holder.getRegistration();
      }
      
      return null;
    }
    





    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass)
    {
      if (!isStarting()) {
        throw new IllegalStateException();
      }
      if ((servletName == null) || ("".equals(servletName.trim()))) {
        throw new IllegalStateException("Missing servlet name");
      }
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      ServletHandler handler = getServletHandler();
      ServletHolder holder = handler.getServlet(servletName);
      if (holder == null)
      {

        holder = handler.newServletHolder(Source.JAVAX_API);
        holder.setName(servletName);
        holder.setHeldClass(servletClass);
        handler.addServlet(holder);
        return dynamicHolderAdded(holder);
      }
      

      if ((holder.getClassName() == null) && (holder.getHeldClass() == null))
      {
        holder.setHeldClass(servletClass);
        return holder.getRegistration();
      }
      
      return null;
    }
    





    public ServletRegistration.Dynamic addServlet(String servletName, String className)
    {
      if (!isStarting()) {
        throw new IllegalStateException();
      }
      if ((servletName == null) || ("".equals(servletName.trim()))) {
        throw new IllegalStateException("Missing servlet name");
      }
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      
      ServletHandler handler = getServletHandler();
      ServletHolder holder = handler.getServlet(servletName);
      if (holder == null)
      {

        holder = handler.newServletHolder(Source.JAVAX_API);
        holder.setName(servletName);
        holder.setClassName(className);
        handler.addServlet(holder);
        return dynamicHolderAdded(holder);
      }
      

      if ((holder.getClassName() == null) && (holder.getHeldClass() == null))
      {
        holder.setClassName(className);
        return holder.getRegistration();
      }
      
      return null;
    }
    





    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet)
    {
      if (!isStarting()) {
        throw new IllegalStateException();
      }
      if ((servletName == null) || ("".equals(servletName.trim()))) {
        throw new IllegalStateException("Missing servlet name");
      }
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      ServletHandler handler = getServletHandler();
      ServletHolder holder = handler.getServlet(servletName);
      if (holder == null)
      {
        holder = handler.newServletHolder(Source.JAVAX_API);
        holder.setName(servletName);
        holder.setServlet(servlet);
        handler.addServlet(holder);
        return dynamicHolderAdded(holder);
      }
      

      if ((holder.getClassName() == null) && (holder.getHeldClass() == null))
      {
        holder.setServlet(servlet);
        return holder.getRegistration();
      }
      
      return null;
    }
    


    public boolean setInitParameter(String name, String value)
    {
      if (!isStarting()) {
        throw new IllegalStateException();
      }
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      return super.setInitParameter(name, value);
    }
    

    public <T extends Filter> T createFilter(Class<T> c)
      throws ServletException
    {
      try
      {
        T f = (Filter)createInstance(c);
        return (Filter)_objFactory.decorate(f);

      }
      catch (Exception e)
      {
        throw new ServletException(e);
      }
    }
    

    public <T extends Servlet> T createServlet(Class<T> c)
      throws ServletException
    {
      try
      {
        T s = (Servlet)createInstance(c);
        return (Servlet)_objFactory.decorate(s);

      }
      catch (Exception e)
      {
        throw new ServletException(e);
      }
    }
    


    public Set<SessionTrackingMode> getDefaultSessionTrackingModes()
    {
      if (_sessionHandler != null)
        return _sessionHandler.getDefaultSessionTrackingModes();
      return null;
    }
    

    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes()
    {
      if (_sessionHandler != null)
        return _sessionHandler.getEffectiveSessionTrackingModes();
      return null;
    }
    

    public FilterRegistration getFilterRegistration(String filterName)
    {
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      FilterHolder holder = getServletHandler().getFilter(filterName);
      return holder == null ? null : holder.getRegistration();
    }
    

    public Map<String, ? extends FilterRegistration> getFilterRegistrations()
    {
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      HashMap<String, FilterRegistration> registrations = new HashMap();
      ServletHandler handler = getServletHandler();
      FilterHolder[] holders = handler.getFilters();
      if (holders != null)
      {
        for (FilterHolder holder : holders)
          registrations.put(holder.getName(), holder.getRegistration());
      }
      return registrations;
    }
    

    public ServletRegistration getServletRegistration(String servletName)
    {
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      ServletHolder holder = getServletHandler().getServlet(servletName);
      return holder == null ? null : holder.getRegistration();
    }
    

    public Map<String, ? extends ServletRegistration> getServletRegistrations()
    {
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      HashMap<String, ServletRegistration> registrations = new HashMap();
      ServletHandler handler = getServletHandler();
      ServletHolder[] holders = handler.getServlets();
      if (holders != null)
      {
        for (ServletHolder holder : holders)
          registrations.put(holder.getName(), holder.getRegistration());
      }
      return registrations;
    }
    

    public SessionCookieConfig getSessionCookieConfig()
    {
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      if (_sessionHandler != null)
        return _sessionHandler.getSessionCookieConfig();
      return null;
    }
    

    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes)
    {
      if (!isStarting())
        throw new IllegalStateException();
      if (!_enabled) {
        throw new UnsupportedOperationException();
      }
      
      if (_sessionHandler != null) {
        _sessionHandler.setSessionTrackingModes(sessionTrackingModes);
      }
    }
    
    public void addListener(String className)
    {
      if (!isStarting())
        throw new IllegalStateException();
      if (!_enabled)
        throw new UnsupportedOperationException();
      super.addListener(className);
    }
    

    public <T extends EventListener> void addListener(T t)
    {
      if (!isStarting())
        throw new IllegalStateException();
      if (!_enabled)
        throw new UnsupportedOperationException();
      super.addListener(t);
      ListenerHolder holder = getServletHandler().newListenerHolder(Source.JAVAX_API);
      holder.setListener(t);
      getServletHandler().addListener(holder);
    }
    

    public void addListener(Class<? extends EventListener> listenerClass)
    {
      if (!isStarting())
        throw new IllegalStateException();
      if (!_enabled)
        throw new UnsupportedOperationException();
      super.addListener(listenerClass);
    }
    
    public <T extends EventListener> T createListener(Class<T> clazz)
      throws ServletException
    {
      try
      {
        T l = (EventListener)createInstance(clazz);
        return (EventListener)_objFactory.decorate(l);

      }
      catch (Exception e)
      {
        throw new ServletException(e);
      }
    }
    


    public JspConfigDescriptor getJspConfigDescriptor()
    {
      return _jspConfig;
    }
    

    public void setJspConfigDescriptor(JspConfigDescriptor d)
    {
      _jspConfig = d;
    }
    


    public void declareRoles(String... roleNames)
    {
      if (!isStarting())
        throw new IllegalStateException();
      if (!_enabled)
        throw new UnsupportedOperationException();
      addRoles(roleNames);
    }
  }
  





  @Deprecated
  public static abstract interface Decorator
    extends Decorator
  {}
  





  private static class LegacyDecorator
    implements ServletContextHandler.Decorator
  {
    private Decorator decorator;
    




    public LegacyDecorator(Decorator decorator)
    {
      this.decorator = decorator;
    }
    

    public <T> T decorate(T o)
    {
      return decorator.decorate(o);
    }
    

    public void destroy(Object o)
    {
      decorator.destroy(o);
    }
  }
}
