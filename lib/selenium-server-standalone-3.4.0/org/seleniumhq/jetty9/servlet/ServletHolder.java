package org.seleniumhq.jetty9.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletSecurityElement;
import javax.servlet.SingleThreadModel;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.ServletSecurity;
import org.seleniumhq.jetty9.security.IdentityService;
import org.seleniumhq.jetty9.security.RunAsToken;
import org.seleniumhq.jetty9.server.MultiPartCleanerListener;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.UserIdentity.Scope;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.util.Loader;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



























@ManagedObject("Servlet Holder")
public class ServletHolder
  extends Holder<Servlet>
  implements UserIdentity.Scope, Comparable<ServletHolder>
{
  private static final Logger LOG = Log.getLogger(ServletHolder.class);
  private int _initOrder = -1;
  private boolean _initOnStartup = false;
  
  private Map<String, String> _roleMap;
  private String _forcedPath;
  private String _runAsRole;
  private RunAsToken _runAsToken;
  private IdentityService _identityService;
  private ServletRegistration.Dynamic _registration;
  private JspContainer _jspContainer;
  private transient Servlet _servlet;
  private transient Config _config;
  private transient long _unavailable;
  private transient boolean _enabled = true;
  
  private transient UnavailableException _unavailableEx;
  
  public static final String APACHE_SENTINEL_CLASS = "org.apache.tomcat.InstanceManager";
  public static final String JSP_GENERATED_PACKAGE_NAME = "org.seleniumhq.jetty9.servlet.jspPackagePrefix";
  public static final Map<String, String> NO_MAPPED_ROLES = Collections.emptyMap();
  public static enum JspContainer { APACHE,  OTHER;
    
    private JspContainer() {}
  }
  
  public ServletHolder()
  {
    this(Source.EMBEDDED);
  }
  




  public ServletHolder(Source creator)
  {
    super(creator);
  }
  




  public ServletHolder(Servlet servlet)
  {
    this(Source.EMBEDDED);
    setServlet(servlet);
  }
  





  public ServletHolder(String name, Class<? extends Servlet> servlet)
  {
    this(Source.EMBEDDED);
    setName(name);
    setHeldClass(servlet);
  }
  





  public ServletHolder(String name, Servlet servlet)
  {
    this(Source.EMBEDDED);
    setName(name);
    setServlet(servlet);
  }
  




  public ServletHolder(Class<? extends Servlet> servlet)
  {
    this(Source.EMBEDDED);
    setHeldClass(servlet);
  }
  




  public UnavailableException getUnavailableException()
  {
    return _unavailableEx;
  }
  

  public synchronized void setServlet(Servlet servlet)
  {
    if ((servlet == null) || ((servlet instanceof SingleThreadModel))) {
      throw new IllegalArgumentException();
    }
    _extInstance = true;
    _servlet = servlet;
    setHeldClass(servlet.getClass());
    if (getName() == null) {
      setName(servlet.getClass().getName() + "-" + super.hashCode());
    }
  }
  
  @ManagedAttribute(value="initialization order", readonly=true)
  public int getInitOrder()
  {
    return _initOrder;
  }
  









  public void setInitOrder(int order)
  {
    _initOnStartup = (order >= 0);
    _initOrder = order;
  }
  





  public int compareTo(ServletHolder sh)
  {
    if (sh == this) {
      return 0;
    }
    if (_initOrder < _initOrder) {
      return 1;
    }
    if (_initOrder > _initOrder) {
      return -1;
    }
    int c;
    int c;
    if ((_className == null) && (_className == null)) {
      c = 0; } else { int c;
      if (_className == null) {
        c = -1; } else { int c;
        if (_className == null) {
          c = 1;
        } else
          c = _className.compareTo(_className);
      }
    }
    if (c == 0) {
      c = _name.compareTo(_name);
    }
    return c;
  }
  

  public boolean equals(Object o)
  {
    return ((o instanceof ServletHolder)) && (compareTo((ServletHolder)o) == 0);
  }
  

  public int hashCode()
  {
    return _name == null ? System.identityHashCode(this) : _name.hashCode();
  }
  







  public synchronized void setUserRoleLink(String name, String link)
  {
    if (_roleMap == null)
      _roleMap = new HashMap();
    _roleMap.put(name, link);
  }
  






  public String getUserRoleLink(String name)
  {
    if (_roleMap == null)
      return name;
    String link = (String)_roleMap.get(name);
    return link == null ? name : link;
  }
  




  @ManagedAttribute(value="forced servlet path", readonly=true)
  public String getForcedPath()
  {
    return _forcedPath;
  }
  




  public void setForcedPath(String forcedPath)
  {
    _forcedPath = forcedPath;
  }
  
  public boolean isEnabled()
  {
    return _enabled;
  }
  

  public void setEnabled(boolean enabled)
  {
    _enabled = enabled;
  }
  


  public void doStart()
    throws Exception
  {
    _unavailable = 0L;
    if (!_enabled) {
      return;
    }
    
    if (_forcedPath != null)
    {

      String precompiled = getClassNameForJsp(_forcedPath);
      if (!StringUtil.isBlank(precompiled))
      {
        if (LOG.isDebugEnabled())
          LOG.debug("Checking for precompiled servlet {} for jsp {}", new Object[] { precompiled, _forcedPath });
        ServletHolder jsp = getServletHandler().getServlet(precompiled);
        if ((jsp != null) && (jsp.getClassName() != null))
        {
          if (LOG.isDebugEnabled()) {
            LOG.debug("JSP file {} for {} mapped to Servlet {}", new Object[] { _forcedPath, getName(), jsp.getClassName() });
          }
          setClassName(jsp.getClassName());

        }
        else
        {
          jsp = getServletHandler().getServlet("jsp");
          if (jsp != null)
          {
            if (LOG.isDebugEnabled())
              LOG.debug("JSP file {} for {} mapped to JspServlet class {}", new Object[] { _forcedPath, getName(), jsp.getClassName() });
            setClassName(jsp.getClassName());
            
            for (Map.Entry<String, String> entry : jsp.getInitParameters().entrySet())
            {
              if (!_initParams.containsKey(entry.getKey())) {
                setInitParameter((String)entry.getKey(), (String)entry.getValue());
              }
            }
            


            setInitParameter("jspFile", _forcedPath);
          }
        }
      }
      else {
        LOG.warn("Bad jsp-file {} conversion to classname in holder {}", new Object[] { _forcedPath, getName() });
      }
    }
    

    try
    {
      super.doStart();
    }
    catch (UnavailableException ue)
    {
      makeUnavailable(ue);
      if (_servletHandler.isStartWithUnavailable())
      {
        LOG.ignore(ue);
        return;
      }
      
      throw ue;
    }
    


    try
    {
      checkServletType();
    }
    catch (UnavailableException ue)
    {
      makeUnavailable(ue);
      if (_servletHandler.isStartWithUnavailable())
      {
        LOG.ignore(ue);
        return;
      }
      
      throw ue;
    }
    

    checkInitOnStartup();
    
    _identityService = _servletHandler.getIdentityService();
    if ((_identityService != null) && (_runAsRole != null)) {
      _runAsToken = _identityService.newRunAsToken(_runAsRole);
    }
    _config = new Config();
    
    if ((_class != null) && (SingleThreadModel.class.isAssignableFrom(_class))) {
      _servlet = new SingleThreadedWrapper(null);
    }
  }
  



  public void initialize()
    throws Exception
  {
    if (!_initialized)
    {
      super.initialize();
      if ((_extInstance) || (_initOnStartup))
      {
        try
        {
          initServlet();
        }
        catch (Exception e)
        {
          if (_servletHandler.isStartWithUnavailable()) {
            LOG.ignore(e);
          } else
            throw e;
        }
      }
    }
    _initialized = true;
  }
  


  public void doStop()
    throws Exception
  {
    Object old_run_as = null;
    if (_servlet != null)
    {
      try
      {
        if (_identityService != null) {
          old_run_as = _identityService.setRunAs(_identityService.getSystemUserIdentity(), _runAsToken);
        }
        destroyInstance(_servlet);
      }
      catch (Exception e)
      {
        LOG.warn(e);
      }
      finally
      {
        if (_identityService != null) {
          _identityService.unsetRunAs(old_run_as);
        }
      }
    }
    if (!_extInstance) {
      _servlet = null;
    }
    _config = null;
    _initialized = false;
  }
  


  public void destroyInstance(Object o)
    throws Exception
  {
    if (o == null)
      return;
    Servlet servlet = (Servlet)o;
    getServletHandler().destroyServlet(servlet);
    servlet.destroy();
  }
  






  public synchronized Servlet getServlet()
    throws ServletException
  {
    if (_unavailable != 0L)
    {
      if ((_unavailable < 0L) || ((_unavailable > 0L) && (System.currentTimeMillis() < _unavailable)))
        throw _unavailableEx;
      _unavailable = 0L;
      _unavailableEx = null;
    }
    
    if (_servlet == null)
      initServlet();
    return _servlet;
  }
  




  public Servlet getServletInstance()
  {
    return _servlet;
  }
  





  public void checkServletType()
    throws UnavailableException
  {
    if ((_class == null) || (!Servlet.class.isAssignableFrom(_class)))
    {
      throw new UnavailableException("Servlet " + _class + " is not a javax.servlet.Servlet");
    }
  }
  




  public boolean isAvailable()
  {
    if ((isStarted()) && (_unavailable == 0L)) {
      return true;
    }
    try {
      getServlet();
    }
    catch (Exception e)
    {
      LOG.ignore(e);
    }
    
    return (isStarted()) && (_unavailable == 0L);
  }
  








  private void checkInitOnStartup()
  {
    if (_class == null) {
      return;
    }
    if ((_class.getAnnotation(ServletSecurity.class) != null) && (!_initOnStartup)) {
      setInitOrder(Integer.MAX_VALUE);
    }
  }
  
  private void makeUnavailable(UnavailableException e)
  {
    if ((_unavailableEx == e) && (_unavailable != 0L)) {
      return;
    }
    _servletHandler.getServletContext().log("unavailable", e);
    
    _unavailableEx = e;
    _unavailable = -1L;
    if (e.isPermanent()) {
      _unavailable = -1L;

    }
    else if (_unavailableEx.getUnavailableSeconds() > 0) {
      _unavailable = (System.currentTimeMillis() + 1000 * _unavailableEx.getUnavailableSeconds());
    } else {
      _unavailable = (System.currentTimeMillis() + 5000L);
    }
  }
  


  private void makeUnavailable(final Throwable e)
  {
    if ((e instanceof UnavailableException)) {
      makeUnavailable((UnavailableException)e);
    }
    else {
      ServletContext ctx = _servletHandler.getServletContext();
      if (ctx == null) {
        LOG.info("unavailable", e);
      } else
        ctx.log("unavailable", e);
      _unavailableEx = new UnavailableException(String.valueOf(e), -1) {};
      _unavailable = -1L;
    }
  }
  

  private void initServlet()
    throws ServletException
  {
    Object old_run_as = null;
    try
    {
      if (_servlet == null)
        _servlet = newInstance();
      if (_config == null) {
        _config = new Config();
      }
      
      if (_identityService != null)
      {
        old_run_as = _identityService.setRunAs(_identityService.getSystemUserIdentity(), _runAsToken);
      }
      

      if (isJspServlet())
      {
        initJspServlet();
        detectJspContainer();
      }
      else if (_forcedPath != null) {
        detectJspContainer();
      }
      initMultiPart();
      
      if (LOG.isDebugEnabled())
        LOG.debug("Servlet.init {} for {}", new Object[] { _servlet, getName() });
      _servlet.init(_config);
    }
    catch (UnavailableException e)
    {
      makeUnavailable(e);
      _servlet = null;
      _config = null;
      throw e;
    }
    catch (ServletException e)
    {
      makeUnavailable(e.getCause() == null ? e : e.getCause());
      _servlet = null;
      _config = null;
      throw e;
    }
    catch (Exception e)
    {
      makeUnavailable(e);
      _servlet = null;
      _config = null;
      throw new ServletException(toString(), e);

    }
    finally
    {
      if (_identityService != null) {
        _identityService.unsetRunAs(old_run_as);
      }
    }
  }
  



  protected void initJspServlet()
    throws Exception
  {
    ContextHandler ch = ContextHandler.getContextHandler(getServletHandler().getServletContext());
    

    ch.setAttribute("org.apache.catalina.jsp_classpath", ch.getClassPath());
    

    if ("?".equals(getInitParameter("classpath")))
    {
      String classpath = ch.getClassPath();
      if (LOG.isDebugEnabled())
        LOG.debug("classpath=" + classpath, new Object[0]);
      if (classpath != null) {
        setInitParameter("classpath", classpath);
      }
    }
    
    File scratch = null;
    if (getInitParameter("scratchdir") == null)
    {
      File tmp = (File)getServletHandler().getServletContext().getAttribute("javax.servlet.context.tempdir");
      scratch = new File(tmp, "jsp");
      setInitParameter("scratchdir", scratch.getAbsolutePath());
    }
    
    scratch = new File(getInitParameter("scratchdir"));
    if (!scratch.exists()) { scratch.mkdir();
    }
  }
  







  protected void initMultiPart()
    throws Exception
  {
    if (((Registration)getRegistration()).getMultipartConfig() != null)
    {



      ContextHandler ch = ContextHandler.getContextHandler(getServletHandler().getServletContext());
      ch.addEventListener(MultiPartCleanerListener.INSTANCE);
    }
  }
  





  public String getContextPath()
  {
    return _config.getServletContext().getContextPath();
  }
  





  public Map<String, String> getRoleRefMap()
  {
    return _roleMap;
  }
  

  @ManagedAttribute(value="role to run servlet as", readonly=true)
  public String getRunAsRole()
  {
    return _runAsRole;
  }
  

  public void setRunAsRole(String role)
  {
    _runAsRole = role;
  }
  










  protected void prepare(Request baseRequest, ServletRequest request, ServletResponse response)
    throws ServletException, UnavailableException
  {
    ensureInstance();
    MultipartConfigElement mpce = ((Registration)getRegistration()).getMultipartConfig();
    if (mpce != null) {
      baseRequest.setAttribute("org.seleniumhq.jetty9.multipartConfig", mpce);
    }
  }
  
  public synchronized Servlet ensureInstance() throws ServletException, UnavailableException
  {
    if (_class == null)
      throw new UnavailableException("Servlet Not Initialized");
    Servlet servlet = _servlet;
    if (!isStarted())
      throw new UnavailableException("Servlet not initialized", -1);
    if ((_unavailable != 0L) || ((!_initOnStartup) && (servlet == null)))
      servlet = getServlet();
    if (servlet == null) {
      throw new UnavailableException("Could not instantiate " + _class);
    }
    return servlet;
  }
  















  public void handle(Request baseRequest, ServletRequest request, ServletResponse response)
    throws ServletException, UnavailableException, IOException
  {
    if (_class == null) {
      throw new UnavailableException("Servlet Not Initialized");
    }
    Servlet servlet = ensureInstance();
    

    Object old_run_as = null;
    boolean suspendable = baseRequest.isAsyncSupported();
    
    try
    {
      if (_forcedPath != null) {
        adaptForcedPathToJspContainer(request);
      }
      
      if (_identityService != null) {
        old_run_as = _identityService.setRunAs(baseRequest.getResolvedUserIdentity(), _runAsToken);
      }
      if ((baseRequest.isAsyncSupported()) && (!isAsyncSupported()))
      {
        try
        {
          baseRequest.setAsyncSupported(false, toString());
          servlet.service(request, response);
        }
        finally
        {
          baseRequest.setAsyncSupported(true, null);
        }
        
      } else {
        servlet.service(request, response);
      }
    }
    catch (UnavailableException e) {
      makeUnavailable(e);
      throw _unavailableEx;

    }
    finally
    {
      if (_identityService != null) {
        _identityService.unsetRunAs(old_run_as);
      }
    }
  }
  

  private boolean isJspServlet()
  {
    if (_servlet == null) {
      return false;
    }
    Class<?> c = _servlet.getClass();
    
    boolean result = false;
    while ((c != null) && (!result))
    {
      result = isJspServlet(c.getName());
      c = c.getSuperclass();
    }
    
    return result;
  }
  


  private boolean isJspServlet(String classname)
  {
    if (classname == null)
      return false;
    return "org.apache.jasper.servlet.JspServlet".equals(classname);
  }
  



  private void adaptForcedPathToJspContainer(ServletRequest request) {}
  


  private void detectJspContainer()
  {
    if (_jspContainer == null)
    {
      try
      {

        Loader.loadClass("org.apache.tomcat.InstanceManager");
        if (LOG.isDebugEnabled()) LOG.debug("Apache jasper detected", new Object[0]);
        _jspContainer = JspContainer.APACHE;
      }
      catch (ClassNotFoundException x)
      {
        if (LOG.isDebugEnabled()) LOG.debug("Other jasper detected", new Object[0]);
        _jspContainer = JspContainer.OTHER;
      }
    }
  }
  





  public String getNameOfJspClass(String jsp)
  {
    if (StringUtil.isBlank(jsp)) {
      return "";
    }
    jsp = jsp.trim();
    if ("/".equals(jsp)) {
      return "";
    }
    int i = jsp.lastIndexOf('/');
    if (i == jsp.length() - 1) {
      return "";
    }
    jsp = jsp.substring(i + 1);
    try
    {
      Class<?> jspUtil = Loader.loadClass("org.apache.jasper.compiler.JspUtil");
      Method makeJavaIdentifier = jspUtil.getMethod("makeJavaIdentifier", new Class[] { String.class });
      return (String)makeJavaIdentifier.invoke(null, new Object[] { jsp });
    }
    catch (Exception e)
    {
      String tmp = jsp.replace('.', '_');
      if (LOG.isDebugEnabled())
      {
        LOG.warn("JspUtil.makeJavaIdentifier failed for jsp " + jsp + " using " + tmp + " instead", new Object[0]);
        LOG.warn(e);
      }
      return tmp;
    }
  }
  


  public String getPackageOfJspClass(String jsp)
  {
    if (jsp == null) {
      return "";
    }
    int i = jsp.lastIndexOf('/');
    if (i <= 0) {
      return "";
    }
    try {
      Class<?> jspUtil = Loader.loadClass("org.apache.jasper.compiler.JspUtil");
      Method makeJavaPackage = jspUtil.getMethod("makeJavaPackage", new Class[] { String.class });
      return (String)makeJavaPackage.invoke(null, new Object[] { jsp.substring(0, i) });

    }
    catch (Exception e)
    {
      String tmp = jsp;
      

      int s = 0;
      if ('/' == tmp.charAt(0)) {
        s = 1;
      }
      
      tmp = tmp.substring(s, i);
      
      tmp = tmp.replace('/', '.').trim();
      tmp = ".".equals(tmp) ? "" : tmp;
      if (LOG.isDebugEnabled())
      {
        LOG.warn("JspUtil.makeJavaPackage failed for " + jsp + " using " + tmp + " instead", new Object[0]);
        LOG.warn(e);
      }
      return tmp;
    }
  }
  





  public String getJspPackagePrefix()
  {
    String jspPackageName = null;
    
    if ((getServletHandler() != null) && (getServletHandler().getServletContext() != null)) {
      jspPackageName = getServletHandler().getServletContext().getInitParameter("org.seleniumhq.jetty9.servlet.jspPackagePrefix");
    }
    if (jspPackageName == null) {
      jspPackageName = "org.apache.jsp";
    }
    return jspPackageName;
  }
  






  public String getClassNameForJsp(String jsp)
  {
    if (jsp == null) {
      return null;
    }
    String name = getNameOfJspClass(jsp);
    if (StringUtil.isBlank(name)) {
      return null;
    }
    StringBuffer fullName = new StringBuffer();
    appendPath(fullName, getJspPackagePrefix());
    appendPath(fullName, getPackageOfJspClass(jsp));
    appendPath(fullName, name);
    return fullName.toString();
  }
  







  protected void appendPath(StringBuffer path, String element)
  {
    if (StringUtil.isBlank(element))
      return;
    if (path.length() > 0)
      path.append(".");
    path.append(element);
  }
  
  protected class Config extends Holder<Servlet>.HolderConfig implements ServletConfig
  {
    protected Config()
    {
      super();
    }
    

    public String getServletName()
    {
      return getName();
    }
  }
  
  public class Registration extends Holder<Servlet>.HolderRegistration implements ServletRegistration.Dynamic {
    protected MultipartConfigElement _multipartConfig;
    
    public Registration() {
      super();
    }
    


    public Set<String> addMapping(String... urlPatterns)
    {
      illegalStateIfContextStarted();
      Set<String> clash = null;
      for (String pattern : urlPatterns)
      {
        ServletMapping mapping = _servletHandler.getServletMapping(pattern);
        if (mapping != null)
        {

          if (!mapping.isDefault())
          {
            if (clash == null)
              clash = new HashSet();
            clash.add(pattern);
          }
        }
      }
      

      if (clash != null) {
        return clash;
      }
      
      ServletMapping mapping = new ServletMapping(Source.JAVAX_API);
      mapping.setServletName(ServletHolder.this.getName());
      mapping.setPathSpecs(urlPatterns);
      _servletHandler.addServletMapping(mapping);
      
      return Collections.emptySet();
    }
    

    public Collection<String> getMappings()
    {
      ServletMapping[] mappings = _servletHandler.getServletMappings();
      List<String> patterns = new ArrayList();
      if (mappings != null)
      {
        for (ServletMapping mapping : mappings)
        {
          if (mapping.getServletName().equals(getName()))
          {
            String[] specs = mapping.getPathSpecs();
            if ((specs != null) && (specs.length > 0))
              patterns.addAll(Arrays.asList(specs));
          } }
      }
      return patterns;
    }
    

    public String getRunAsRole()
    {
      return _runAsRole;
    }
    

    public void setLoadOnStartup(int loadOnStartup)
    {
      illegalStateIfContextStarted();
      setInitOrder(loadOnStartup);
    }
    
    public int getInitOrder()
    {
      return ServletHolder.this.getInitOrder();
    }
    

    public void setMultipartConfig(MultipartConfigElement element)
    {
      _multipartConfig = element;
    }
    
    public MultipartConfigElement getMultipartConfig()
    {
      return _multipartConfig;
    }
    

    public void setRunAsRole(String role)
    {
      _runAsRole = role;
    }
    

    public Set<String> setServletSecurity(ServletSecurityElement securityElement)
    {
      return _servletHandler.setServletSecurity(this, securityElement);
    }
  }
  
  public ServletRegistration.Dynamic getRegistration()
  {
    if (_registration == null)
      _registration = new Registration();
    return _registration;
  }
  


  private class SingleThreadedWrapper
    implements Servlet
  {
    Stack<Servlet> _stack = new Stack();
    
    private SingleThreadedWrapper() {}
    
    public void destroy() {
      synchronized (this)
      {
        while (_stack.size() > 0) {
          try { ((Servlet)_stack.pop()).destroy(); } catch (Exception e) {} ServletHolder.LOG.warn(e);
        }
      }
    }
    
    public ServletConfig getServletConfig()
    {
      return _config;
    }
    

    public String getServletInfo()
    {
      return null;
    }
    
    public void init(ServletConfig config)
      throws ServletException
    {
      synchronized (this)
      {
        if (_stack.size() == 0)
        {
          try
          {
            Servlet s = newInstance();
            s.init(config);
            _stack.push(s);
          }
          catch (ServletException e)
          {
            throw e;
          }
          catch (Exception e)
          {
            throw new ServletException(e);
          }
        }
      }
    }
    
    public void service(ServletRequest req, ServletResponse res)
      throws ServletException, IOException
    {
      Servlet s;
      synchronized (this) {
        Servlet s;
        if (_stack.size() > 0) {
          s = (Servlet)_stack.pop();
        }
        else {
          try
          {
            Servlet s = newInstance();
            s.init(_config);
          }
          catch (ServletException e)
          {
            throw e;
          }
          catch (Exception e)
          {
            throw new ServletException(e);
          }
        }
      }
      
      try
      {
        s.service(req, res);
      }
      finally
      {
        synchronized (this) {
          Servlet s;
          _stack.push(s);
        }
      }
    }
  }
  






  protected Servlet newInstance()
    throws ServletException, IllegalAccessException, InstantiationException
  {
    try
    {
      ServletContext ctx = getServletHandler().getServletContext();
      if ((ctx instanceof ServletContextHandler.Context))
        return ((ServletContextHandler.Context)ctx).createServlet(getHeldClass());
      return (Servlet)getHeldClass().newInstance();
    }
    catch (ServletException se)
    {
      Throwable cause = se.getRootCause();
      if ((cause instanceof InstantiationException))
        throw ((InstantiationException)cause);
      if ((cause instanceof IllegalAccessException))
        throw ((IllegalAccessException)cause);
      throw se;
    }
  }
  



  public String toString()
  {
    return String.format("%s@%x==%s,jsp=%s,order=%d,inst=%b", new Object[] { _name, Integer.valueOf(hashCode()), _className, _forcedPath, Integer.valueOf(_initOrder), Boolean.valueOf(_servlet != null ? 1 : false) });
  }
}
