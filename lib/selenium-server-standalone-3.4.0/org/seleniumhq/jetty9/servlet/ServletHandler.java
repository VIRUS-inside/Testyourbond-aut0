package org.seleniumhq.jetty9.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletSecurityElement;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.seleniumhq.jetty9.http.pathmap.MappedResource;
import org.seleniumhq.jetty9.http.pathmap.PathMappings;
import org.seleniumhq.jetty9.http.pathmap.PathSpec;
import org.seleniumhq.jetty9.http.pathmap.ServletPathSpec;
import org.seleniumhq.jetty9.security.IdentityService;
import org.seleniumhq.jetty9.security.SecurityHandler;
import org.seleniumhq.jetty9.server.Request;
import org.seleniumhq.jetty9.server.ServletRequestHttpWrapper;
import org.seleniumhq.jetty9.server.ServletResponseHttpWrapper;
import org.seleniumhq.jetty9.server.UserIdentity.Scope;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;
import org.seleniumhq.jetty9.server.handler.ContextHandler.StaticContext;
import org.seleniumhq.jetty9.server.handler.ScopedHandler;
import org.seleniumhq.jetty9.util.ArrayUtil;
import org.seleniumhq.jetty9.util.LazyList;
import org.seleniumhq.jetty9.util.MultiException;
import org.seleniumhq.jetty9.util.MultiMap;
import org.seleniumhq.jetty9.util.URIUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.LifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;

































@ManagedObject("Servlet Handler")
public class ServletHandler
  extends ScopedHandler
{
  private static final Logger LOG = Log.getLogger(ServletHandler.class);
  
  public static final String __DEFAULT_SERVLET = "default";
  
  private ServletContextHandler _contextHandler;
  
  private ServletContext _servletContext;
  
  private FilterHolder[] _filters = new FilterHolder[0];
  private FilterMapping[] _filterMappings;
  private int _matchBeforeIndex = -1;
  private int _matchAfterIndex = -1;
  private boolean _filterChainsCached = true;
  private int _maxFilterChainsCacheSize = 512;
  private boolean _startWithUnavailable = false;
  private boolean _ensureDefaultServlet = true;
  private IdentityService _identityService;
  private boolean _allowDuplicateMappings = false;
  
  private ServletHolder[] _servlets = new ServletHolder[0];
  private ServletMapping[] _servletMappings;
  private final Map<String, FilterHolder> _filterNameMap = new HashMap();
  
  private List<FilterMapping> _filterPathMappings;
  private MultiMap<FilterMapping> _filterNameMappings;
  private final Map<String, ServletHolder> _servletNameMap = new HashMap();
  
  private PathMappings<ServletHolder> _servletPathMap;
  
  private ListenerHolder[] _listeners = new ListenerHolder[0];
  
  protected final ConcurrentMap<String, FilterChain>[] _chainCache = new ConcurrentMap[31];
  

  protected final Queue<String>[] _chainLRU = new Queue[31];
  





  public ServletHandler() {}
  





  protected synchronized void doStart()
    throws Exception
  {
    ContextHandler.Context context = ContextHandler.getCurrentContext();
    _servletContext = (context == null ? new ContextHandler.StaticContext() : context);
    _contextHandler = ((ServletContextHandler)(context == null ? null : context.getContextHandler()));
    
    if (_contextHandler != null)
    {
      SecurityHandler security_handler = (SecurityHandler)_contextHandler.getChildHandlerByClass(SecurityHandler.class);
      if (security_handler != null) {
        _identityService = security_handler.getIdentityService();
      }
    }
    updateNameMappings();
    updateMappings();
    
    if ((getServletMapping("/") == null) && (isEnsureDefaultServlet()))
    {
      if (LOG.isDebugEnabled())
        LOG.debug("Adding Default404Servlet to {}", new Object[] { this });
      addServletWithMapping(Default404Servlet.class, "/");
      updateMappings();
      getServletMapping("/").setDefault(true);
    }
    
    if (isFilterChainsCached())
    {
      _chainCache[1] = new ConcurrentHashMap();
      _chainCache[2] = new ConcurrentHashMap();
      _chainCache[4] = new ConcurrentHashMap();
      _chainCache[8] = new ConcurrentHashMap();
      _chainCache[16] = new ConcurrentHashMap();
      
      _chainLRU[1] = new ConcurrentLinkedQueue();
      _chainLRU[2] = new ConcurrentLinkedQueue();
      _chainLRU[4] = new ConcurrentLinkedQueue();
      _chainLRU[8] = new ConcurrentLinkedQueue();
      _chainLRU[16] = new ConcurrentLinkedQueue();
    }
    
    if (_contextHandler == null) {
      initialize();
    }
    super.doStart();
  }
  






  public boolean isEnsureDefaultServlet()
  {
    return _ensureDefaultServlet;
  }
  





  public void setEnsureDefaultServlet(boolean ensureDefaultServlet)
  {
    _ensureDefaultServlet = ensureDefaultServlet;
  }
  





  protected void start(LifeCycle l)
    throws Exception
  {
    if (!(l instanceof Holder)) {
      super.start(l);
    }
  }
  

  protected synchronized void doStop()
    throws Exception
  {
    super.doStop();
    

    List<FilterHolder> filterHolders = new ArrayList();
    List<FilterMapping> filterMappings = ArrayUtil.asMutableList(_filterMappings);
    int i; if (_filters != null)
    {
      for (i = _filters.length; i-- > 0;)
      {
        try
        {
          _filters[i].stop();
        }
        catch (Exception e)
        {
          LOG.warn("EXCEPTION ", e);
        }
        if (_filters[i].getSource() != Source.EMBEDDED)
        {

          _filterNameMap.remove(_filters[i].getName());
          
          ListIterator<FilterMapping> fmitor = filterMappings.listIterator();
          while (fmitor.hasNext())
          {
            FilterMapping fm = (FilterMapping)fmitor.next();
            if (fm.getFilterName().equals(_filters[i].getName())) {
              fmitor.remove();
            }
          }
        } else {
          filterHolders.add(_filters[i]);
        }
      }
    }
    
    FilterHolder[] fhs = (FilterHolder[])LazyList.toArray(filterHolders, FilterHolder.class);
    updateBeans(_filters, fhs);
    _filters = fhs;
    FilterMapping[] fms = (FilterMapping[])LazyList.toArray(filterMappings, FilterMapping.class);
    updateBeans(_filterMappings, fms);
    _filterMappings = fms;
    
    _matchAfterIndex = ((_filterMappings == null) || (_filterMappings.length == 0) ? -1 : _filterMappings.length - 1);
    _matchBeforeIndex = -1;
    

    List<ServletHolder> servletHolders = new ArrayList();
    List<ServletMapping> servletMappings = ArrayUtil.asMutableList(_servletMappings);
    int i; if (_servlets != null)
    {
      for (i = _servlets.length; i-- > 0;)
      {
        try
        {
          _servlets[i].stop();
        }
        catch (Exception e)
        {
          LOG.warn("EXCEPTION ", e);
        }
        
        if (_servlets[i].getSource() != Source.EMBEDDED)
        {

          _servletNameMap.remove(_servlets[i].getName());
          
          ListIterator<ServletMapping> smitor = servletMappings.listIterator();
          while (smitor.hasNext())
          {
            ServletMapping sm = (ServletMapping)smitor.next();
            if (sm.getServletName().equals(_servlets[i].getName())) {
              smitor.remove();
            }
          }
        } else {
          servletHolders.add(_servlets[i]);
        }
      }
    }
    
    ServletHolder[] shs = (ServletHolder[])LazyList.toArray(servletHolders, ServletHolder.class);
    updateBeans(_servlets, shs);
    _servlets = shs;
    ServletMapping[] sms = (ServletMapping[])LazyList.toArray(servletMappings, ServletMapping.class);
    updateBeans(_servletMappings, sms);
    _servletMappings = sms;
    

    List<ListenerHolder> listenerHolders = new ArrayList();
    int i; if (_listeners != null)
    {
      for (i = _listeners.length; i-- > 0;)
      {
        try
        {
          _listeners[i].stop();
        }
        catch (Exception e)
        {
          LOG.warn("EXCEPTION ", e);
        }
        if (_listeners[i].getSource() == Source.EMBEDDED)
          listenerHolders.add(_listeners[i]);
      }
    }
    ListenerHolder[] listeners = (ListenerHolder[])LazyList.toArray(listenerHolders, ListenerHolder.class);
    updateBeans(_listeners, listeners);
    _listeners = listeners;
    

    _filterPathMappings = null;
    _filterNameMappings = null;
    _servletPathMap = null;
  }
  

  protected IdentityService getIdentityService()
  {
    return _identityService;
  }
  
  @ManagedAttribute(value="filters", readonly=true)
  public FilterMapping[] getFilterMappings()
  {
    return _filterMappings;
  }
  
  @ManagedAttribute(value="filters", readonly=true)
  public FilterHolder[] getFilters()
  {
    return _filters;
  }
  







  public MappedResource<ServletHolder> getHolderEntry(String pathInContext)
  {
    if (_servletPathMap == null)
      return null;
    return _servletPathMap.getMatch(pathInContext);
  }
  

  public ServletContext getServletContext()
  {
    return _servletContext;
  }
  

  @ManagedAttribute(value="mappings of servlets", readonly=true)
  public ServletMapping[] getServletMappings()
  {
    return _servletMappings;
  }
  







  public ServletMapping getServletMapping(String pathSpec)
  {
    if ((pathSpec == null) || (_servletMappings == null)) {
      return null;
    }
    ServletMapping mapping = null;
    for (int i = 0; (i < _servletMappings.length) && (mapping == null); i++)
    {
      ServletMapping m = _servletMappings[i];
      if (m.getPathSpecs() != null)
      {
        for (String p : m.getPathSpecs())
        {
          if (pathSpec.equals(p))
          {
            mapping = m;
            break;
          }
        }
      }
    }
    return mapping;
  }
  
  @ManagedAttribute(value="servlets", readonly=true)
  public ServletHolder[] getServlets()
  {
    return _servlets;
  }
  
  public ServletHolder getServlet(String name)
  {
    return (ServletHolder)_servletNameMap.get(name);
  }
  

  public void doScope(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String old_servlet_path = baseRequest.getServletPath();
    String old_path_info = baseRequest.getPathInfo();
    
    DispatcherType type = baseRequest.getDispatcherType();
    
    ServletHolder servlet_holder = null;
    UserIdentity.Scope old_scope = null;
    

    if (target.startsWith("/"))
    {

      MappedResource<ServletHolder> entry = getHolderEntry(target);
      if (entry != null)
      {
        PathSpec pathSpec = entry.getPathSpec();
        servlet_holder = (ServletHolder)entry.getResource();
        
        String servlet_path = pathSpec.getPathMatch(target);
        String path_info = pathSpec.getPathInfo(target);
        
        if (DispatcherType.INCLUDE.equals(type))
        {
          baseRequest.setAttribute("javax.servlet.include.servlet_path", servlet_path);
          baseRequest.setAttribute("javax.servlet.include.path_info", path_info);
        }
        else
        {
          baseRequest.setServletPath(servlet_path);
          baseRequest.setPathInfo(path_info);
        }
        
      }
    }
    else
    {
      servlet_holder = (ServletHolder)_servletNameMap.get(target);
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("servlet {}|{}|{} -> {}", new Object[] { baseRequest.getContextPath(), baseRequest.getServletPath(), baseRequest.getPathInfo(), servlet_holder });
    }
    
    try
    {
      old_scope = baseRequest.getUserIdentityScope();
      baseRequest.setUserIdentityScope(servlet_holder);
      
      nextScope(target, baseRequest, request, response);
    }
    finally
    {
      if (old_scope != null) {
        baseRequest.setUserIdentityScope(old_scope);
      }
      if (!DispatcherType.INCLUDE.equals(type))
      {
        baseRequest.setServletPath(old_servlet_path);
        baseRequest.setPathInfo(old_path_info);
      }
    }
  }
  

  public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    ServletHolder servlet_holder = (ServletHolder)baseRequest.getUserIdentityScope();
    FilterChain chain = null;
    

    if (target.startsWith("/"))
    {
      if ((servlet_holder != null) && (_filterMappings != null) && (_filterMappings.length > 0)) {
        chain = getFilterChain(baseRequest, target, servlet_holder);
      }
      
    }
    else if (servlet_holder != null)
    {
      if ((_filterMappings != null) && (_filterMappings.length > 0))
      {
        chain = getFilterChain(baseRequest, null, servlet_holder);
      }
    }
    

    if (LOG.isDebugEnabled()) {
      LOG.debug("chain={}", new Object[] { chain });
    }
    try
    {
      if (servlet_holder == null) {
        notFound(baseRequest, request, response);
      }
      else
      {
        ServletRequest req = request;
        if ((req instanceof ServletRequestHttpWrapper))
          req = ((ServletRequestHttpWrapper)req).getRequest();
        ServletResponse res = response;
        if ((res instanceof ServletResponseHttpWrapper)) {
          res = ((ServletResponseHttpWrapper)res).getResponse();
        }
        
        servlet_holder.prepare(baseRequest, req, res);
        
        if (chain != null) {
          chain.doFilter(req, res);
        } else {
          servlet_holder.handle(baseRequest, req, res);
        }
      }
    }
    finally {
      if (servlet_holder != null) {
        baseRequest.setHandled(true);
      }
    }
  }
  
  protected FilterChain getFilterChain(Request baseRequest, String pathInContext, ServletHolder servletHolder) {
    String key = pathInContext == null ? servletHolder.getName() : pathInContext;
    int dispatch = FilterMapping.dispatch(baseRequest.getDispatcherType());
    
    if ((_filterChainsCached) && (_chainCache != null))
    {
      FilterChain chain = (FilterChain)_chainCache[dispatch].get(key);
      if (chain != null) {
        return chain;
      }
    }
    
    List<FilterHolder> filters = new ArrayList();
    

    if ((pathInContext != null) && (_filterPathMappings != null))
    {
      for (FilterMapping filterPathMapping : _filterPathMappings)
      {
        if (filterPathMapping.appliesTo(pathInContext, dispatch)) {
          filters.add(filterPathMapping.getFilterHolder());
        }
      }
    }
    
    if ((servletHolder != null) && (_filterNameMappings != null) && (!_filterNameMappings.isEmpty()))
    {
      Object o = _filterNameMappings.get(servletHolder.getName());
      
      for (int i = 0; i < LazyList.size(o); i++)
      {
        FilterMapping mapping = (FilterMapping)LazyList.get(o, i);
        if (mapping.appliesTo(dispatch)) {
          filters.add(mapping.getFilterHolder());
        }
      }
      o = _filterNameMappings.get("*");
      for (int i = 0; i < LazyList.size(o); i++)
      {
        FilterMapping mapping = (FilterMapping)LazyList.get(o, i);
        if (mapping.appliesTo(dispatch)) {
          filters.add(mapping.getFilterHolder());
        }
      }
    }
    if (filters.isEmpty()) {
      return null;
    }
    
    Object chain = null;
    if (_filterChainsCached)
    {
      if (filters.size() > 0) {
        chain = newCachedChain(filters, servletHolder);
      }
      Map<String, FilterChain> cache = _chainCache[dispatch];
      Queue<String> lru = _chainLRU[dispatch];
      

      while ((_maxFilterChainsCacheSize > 0) && (cache.size() >= _maxFilterChainsCacheSize))
      {



        String k = (String)lru.poll();
        if (k == null)
        {
          cache.clear();
          break;
        }
        cache.remove(k);
      }
      
      cache.put(key, chain);
      lru.add(key);
    }
    else if (filters.size() > 0) {
      chain = new Chain(baseRequest, filters, servletHolder, null);
    }
    return chain;
  }
  

  protected void invalidateChainsCache()
  {
    if (_chainLRU[1] != null)
    {
      _chainLRU[1].clear();
      _chainLRU[2].clear();
      _chainLRU[4].clear();
      _chainLRU[8].clear();
      _chainLRU[16].clear();
      
      _chainCache[1].clear();
      _chainCache[2].clear();
      _chainCache[4].clear();
      _chainCache[8].clear();
      _chainCache[16].clear();
    }
  }
  




  public boolean isAvailable()
  {
    if (!isStarted())
      return false;
    ServletHolder[] holders = getServlets();
    for (ServletHolder holder : holders)
    {
      if ((holder != null) && (!holder.isAvailable()))
        return false;
    }
    return true;
  }
  




  public void setStartWithUnavailable(boolean start)
  {
    _startWithUnavailable = start;
  }
  



  public boolean isAllowDuplicateMappings()
  {
    return _allowDuplicateMappings;
  }
  



  public void setAllowDuplicateMappings(boolean allowDuplicateMappings)
  {
    _allowDuplicateMappings = allowDuplicateMappings;
  }
  




  public boolean isStartWithUnavailable()
  {
    return _startWithUnavailable;
  }
  






  public void initialize()
    throws Exception
  {
    MultiException mx = new MultiException();
    FilterHolder localFilterHolder1;
    FilterHolder f;
    if (_filters != null)
    {
      FilterHolder[] arrayOfFilterHolder = _filters;int i = arrayOfFilterHolder.length; for (localFilterHolder1 = 0; localFilterHolder1 < i; localFilterHolder1++) { f = arrayOfFilterHolder[localFilterHolder1];
        
        try
        {
          f.start();
          f.initialize();
        }
        catch (Exception e)
        {
          mx.add(e);
        }
      }
    }
    

    if (_servlets != null)
    {
      servlets = (ServletHolder[])_servlets.clone();
      Arrays.sort((Object[])servlets);
      Object localObject1 = servlets;localFilterHolder1 = localObject1.length; for (f = 0; f < localFilterHolder1; f++) { ServletHolder servlet = localObject1[f];
        
        try
        {
          servlet.start();
          servlet.initialize();
        }
        catch (Throwable e)
        {
          LOG.debug("EXCEPTION ", e);
          mx.add(e);
        }
      }
    }
    

    for (Object servlets = getBeans(Holder.class).iterator(); ((Iterator)servlets).hasNext();) { Object h = (Holder)((Iterator)servlets).next();
      
      try
      {
        if (!((Holder)h).isStarted())
        {
          ((Holder)h).start();
          ((Holder)h).initialize();
        }
      }
      catch (Exception e)
      {
        mx.add(e);
      }
    }
    
    mx.ifExceptionThrow();
  }
  




  public boolean isFilterChainsCached()
  {
    return _filterChainsCached;
  }
  




  public void addListener(ListenerHolder listener)
  {
    if (listener != null) {
      setListeners((ListenerHolder[])ArrayUtil.addToArray(getListeners(), listener, ListenerHolder.class));
    }
  }
  

  public ListenerHolder[] getListeners()
  {
    return _listeners;
  }
  

  public void setListeners(ListenerHolder[] listeners)
  {
    if (listeners != null) {
      for (ListenerHolder holder : listeners)
        holder.setServletHandler(this);
    }
    updateBeans(_listeners, listeners);
    _listeners = listeners;
  }
  

  public ListenerHolder newListenerHolder(Source source)
  {
    return new ListenerHolder(source);
  }
  




  public CachedChain newCachedChain(List<FilterHolder> filters, ServletHolder servletHolder)
  {
    return new CachedChain(filters, servletHolder);
  }
  






  public ServletHolder newServletHolder(Source source)
  {
    return new ServletHolder(source);
  }
  






  public ServletHolder addServletWithMapping(String className, String pathSpec)
  {
    ServletHolder holder = newServletHolder(Source.EMBEDDED);
    holder.setClassName(className);
    addServletWithMapping(holder, pathSpec);
    return holder;
  }
  






  public ServletHolder addServletWithMapping(Class<? extends Servlet> servlet, String pathSpec)
  {
    ServletHolder holder = newServletHolder(Source.EMBEDDED);
    holder.setHeldClass(servlet);
    addServletWithMapping(holder, pathSpec);
    
    return holder;
  }
  





  public void addServletWithMapping(ServletHolder servlet, String pathSpec)
  {
    ServletHolder[] holders = getServlets();
    if (holders != null) {
      holders = (ServletHolder[])holders.clone();
    }
    try
    {
      synchronized (this)
      {
        if ((servlet != null) && (!containsServletHolder(servlet))) {
          setServlets((ServletHolder[])ArrayUtil.addToArray(holders, servlet, ServletHolder.class));
        }
      }
      ServletMapping mapping = new ServletMapping();
      mapping.setServletName(servlet.getName());
      mapping.setPathSpec(pathSpec);
      setServletMappings((ServletMapping[])ArrayUtil.addToArray(getServletMappings(), mapping, ServletMapping.class));
    }
    catch (RuntimeException e)
    {
      setServlets(holders);
      throw e;
    }
  }
  






  public void addServlet(ServletHolder holder)
  {
    if (holder == null) {
      return;
    }
    synchronized (this)
    {
      if (!containsServletHolder(holder)) {
        setServlets((ServletHolder[])ArrayUtil.addToArray(getServlets(), holder, ServletHolder.class));
      }
    }
  }
  




  public void addServletMapping(ServletMapping mapping)
  {
    setServletMappings((ServletMapping[])ArrayUtil.addToArray(getServletMappings(), mapping, ServletMapping.class));
  }
  

  public Set<String> setServletSecurity(ServletRegistration.Dynamic registration, ServletSecurityElement servletSecurityElement)
  {
    if (_contextHandler != null)
    {
      return _contextHandler.setServletSecurity(registration, servletSecurityElement);
    }
    return Collections.emptySet();
  }
  

  public FilterHolder newFilterHolder(Source source)
  {
    return new FilterHolder(source);
  }
  

  public FilterHolder getFilter(String name)
  {
    return (FilterHolder)_filterNameMap.get(name);
  }
  








  public FilterHolder addFilterWithMapping(Class<? extends Filter> filter, String pathSpec, EnumSet<DispatcherType> dispatches)
  {
    FilterHolder holder = newFilterHolder(Source.EMBEDDED);
    holder.setHeldClass(filter);
    addFilterWithMapping(holder, pathSpec, dispatches);
    
    return holder;
  }
  







  public FilterHolder addFilterWithMapping(String className, String pathSpec, EnumSet<DispatcherType> dispatches)
  {
    FilterHolder holder = newFilterHolder(Source.EMBEDDED);
    holder.setClassName(className);
    
    addFilterWithMapping(holder, pathSpec, dispatches);
    return holder;
  }
  






  public void addFilterWithMapping(FilterHolder holder, String pathSpec, EnumSet<DispatcherType> dispatches)
  {
    FilterHolder[] holders = getFilters();
    if (holders != null) {
      holders = (FilterHolder[])holders.clone();
    }
    try
    {
      synchronized (this)
      {
        if ((holder != null) && (!containsFilterHolder(holder))) {
          setFilters((FilterHolder[])ArrayUtil.addToArray(holders, holder, FilterHolder.class));
        }
      }
      FilterMapping mapping = new FilterMapping();
      mapping.setFilterName(holder.getName());
      mapping.setPathSpec(pathSpec);
      mapping.setDispatcherTypes(dispatches);
      addFilterMapping(mapping);

    }
    catch (Throwable e)
    {
      setFilters(holders);
      throw e;
    }
  }
  







  public FilterHolder addFilterWithMapping(Class<? extends Filter> filter, String pathSpec, int dispatches)
  {
    FilterHolder holder = newFilterHolder(Source.EMBEDDED);
    holder.setHeldClass(filter);
    addFilterWithMapping(holder, pathSpec, dispatches);
    
    return holder;
  }
  







  public FilterHolder addFilterWithMapping(String className, String pathSpec, int dispatches)
  {
    FilterHolder holder = newFilterHolder(Source.EMBEDDED);
    holder.setClassName(className);
    
    addFilterWithMapping(holder, pathSpec, dispatches);
    return holder;
  }
  






  public void addFilterWithMapping(FilterHolder holder, String pathSpec, int dispatches)
  {
    FilterHolder[] holders = getFilters();
    if (holders != null) {
      holders = (FilterHolder[])holders.clone();
    }
    try
    {
      synchronized (this)
      {
        if ((holder != null) && (!containsFilterHolder(holder))) {
          setFilters((FilterHolder[])ArrayUtil.addToArray(holders, holder, FilterHolder.class));
        }
      }
      FilterMapping mapping = new FilterMapping();
      mapping.setFilterName(holder.getName());
      mapping.setPathSpec(pathSpec);
      mapping.setDispatches(dispatches);
      addFilterMapping(mapping);
    }
    catch (Throwable e)
    {
      setFilters(holders);
      throw e;
    }
  }
  











  @Deprecated
  public FilterHolder addFilter(String className, String pathSpec, EnumSet<DispatcherType> dispatches)
  {
    return addFilterWithMapping(className, pathSpec, dispatches);
  }
  






  public void addFilter(FilterHolder filter, FilterMapping filterMapping)
  {
    if (filter != null)
    {
      synchronized (this)
      {
        if (!containsFilterHolder(filter))
          setFilters((FilterHolder[])ArrayUtil.addToArray(getFilters(), filter, FilterHolder.class));
      }
    }
    if (filterMapping != null) {
      addFilterMapping(filterMapping);
    }
  }
  





  public void addFilter(FilterHolder filter)
  {
    if (filter == null) {
      return;
    }
    synchronized (this)
    {
      if (!containsFilterHolder(filter)) {
        setFilters((FilterHolder[])ArrayUtil.addToArray(getFilters(), filter, FilterHolder.class));
      }
    }
  }
  





  public void addFilterMapping(FilterMapping mapping)
  {
    if (mapping != null)
    {
      Source source = mapping.getFilterHolder() == null ? null : mapping.getFilterHolder().getSource();
      FilterMapping[] mappings = getFilterMappings();
      if ((mappings == null) || (mappings.length == 0))
      {
        setFilterMappings(insertFilterMapping(mapping, 0, false));
        if ((source != null) && (source == Source.JAVAX_API)) {
          _matchAfterIndex = 0;

        }
        


      }
      else if ((source != null) && (Source.JAVAX_API == source))
      {
        setFilterMappings(insertFilterMapping(mapping, mappings.length - 1, false));
        if (_matchAfterIndex < 0) {
          _matchAfterIndex = (getFilterMappings().length - 1);
        }
        

      }
      else if (_matchAfterIndex < 0) {
        setFilterMappings(insertFilterMapping(mapping, mappings.length - 1, false));
      }
      else {
        FilterMapping[] new_mappings = insertFilterMapping(mapping, _matchAfterIndex, true);
        _matchAfterIndex += 1;
        setFilterMappings(new_mappings);
      }
    }
  }
  








  public void prependFilterMapping(FilterMapping mapping)
  {
    if (mapping != null)
    {
      Source source = mapping.getFilterHolder() == null ? null : mapping.getFilterHolder().getSource();
      FilterMapping[] mappings = getFilterMappings();
      if ((mappings == null) || (mappings.length == 0))
      {
        setFilterMappings(insertFilterMapping(mapping, 0, false));
        if ((source != null) && (Source.JAVAX_API == source)) {
          _matchBeforeIndex = 0;
        }
      }
      else {
        if ((source != null) && (Source.JAVAX_API == source))
        {




          if (_matchBeforeIndex < 0)
          {

            _matchBeforeIndex = 0;
            FilterMapping[] new_mappings = insertFilterMapping(mapping, 0, true);
            setFilterMappings(new_mappings);
          }
          else
          {
            FilterMapping[] new_mappings = insertFilterMapping(mapping, _matchBeforeIndex, false);
            _matchBeforeIndex += 1;
            setFilterMappings(new_mappings);
          }
          
        }
        else
        {
          FilterMapping[] new_mappings = insertFilterMapping(mapping, 0, true);
          setFilterMappings(new_mappings);
        }
        

        if (_matchAfterIndex >= 0) {
          _matchAfterIndex += 1;
        }
      }
    }
  }
  








  protected FilterMapping[] insertFilterMapping(FilterMapping mapping, int pos, boolean before)
  {
    if (pos < 0)
      throw new IllegalArgumentException("FilterMapping insertion pos < 0");
    FilterMapping[] mappings = getFilterMappings();
    
    if ((mappings == null) || (mappings.length == 0))
    {
      return new FilterMapping[] { mapping };
    }
    FilterMapping[] new_mappings = new FilterMapping[mappings.length + 1];
    

    if (before)
    {

      System.arraycopy(mappings, 0, new_mappings, 0, pos);
      

      new_mappings[pos] = mapping;
      

      System.arraycopy(mappings, pos, new_mappings, pos + 1, mappings.length - pos);

    }
    else
    {

      System.arraycopy(mappings, 0, new_mappings, 0, pos + 1);
      
      new_mappings[(pos + 1)] = mapping;
      

      if (mappings.length > pos + 1)
        System.arraycopy(mappings, pos + 1, new_mappings, pos + 2, mappings.length - (pos + 1));
    }
    return new_mappings;
  }
  



  protected synchronized void updateNameMappings()
  {
    _filterNameMap.clear();
    if (_filters != null)
    {
      for (FilterHolder filter : _filters)
      {
        _filterNameMap.put(filter.getName(), filter);
        filter.setServletHandler(this);
      }
    }
    

    _servletNameMap.clear();
    if (_servlets != null)
    {

      for (ServletHolder servlet : _servlets)
      {
        _servletNameMap.put(servlet.getName(), servlet);
        servlet.setServletHandler(this);
      } }
  }
  
  protected synchronized void updateMappings() {
    FilterMapping filtermapping;
    FilterHolder filter_holder;
    String[] names;
    String name;
    if (_filterMappings == null)
    {
      _filterPathMappings = null;
      _filterNameMappings = null;
    }
    else
    {
      _filterPathMappings = new ArrayList();
      _filterNameMappings = new MultiMap();
      for (filtermapping : _filterMappings)
      {
        filter_holder = (FilterHolder)_filterNameMap.get(filtermapping.getFilterName());
        if (filter_holder == null)
          throw new IllegalStateException("No filter named " + filtermapping.getFilterName());
        filtermapping.setFilterHolder(filter_holder);
        if (filtermapping.getPathSpecs() != null) {
          _filterPathMappings.add(filtermapping);
        }
        if (filtermapping.getServletNames() != null)
        {
          names = filtermapping.getServletNames();
          for (name : names)
          {
            if (name != null) {
              _filterNameMappings.add(name, filtermapping);
            }
          }
        }
      }
    }
    
    if ((_servletMappings == null) || (_servletNameMap == null))
    {
      _servletPathMap = null;
    }
    else
    {
      Object pm = new PathMappings();
      Object servletPathMappings = new HashMap();
      

      Object sms = new HashMap();
      filtermapping = _servletMappings;filter_holder = filtermapping.length; Object pathSpecs; for (names = 0; names < filter_holder; names++) { ServletMapping servletMapping = filtermapping[names];
        
        pathSpecs = servletMapping.getPathSpecs();
        if (pathSpecs != null)
        {
          Object localObject1 = pathSpecs;name = localObject1.length; for (String str1 = 0; str1 < name; str1++) { String pathSpec = localObject1[str1];
            
            List<ServletMapping> mappings = (List)((HashMap)sms).get(pathSpec);
            if (mappings == null)
            {
              mappings = new ArrayList();
              ((HashMap)sms).put(pathSpec, mappings);
            }
            mappings.add(servletMapping);
          }
        }
      }
      

      for (String pathSpec : ((HashMap)sms).keySet())
      {


        List<ServletMapping> mappings = (List)((HashMap)sms).get(pathSpec);
        
        ServletMapping finalMapping = null;
        for (pathSpecs = mappings.iterator(); ((Iterator)pathSpecs).hasNext();) { ServletMapping mapping = (ServletMapping)((Iterator)pathSpecs).next();
          

          ServletHolder servlet_holder = (ServletHolder)_servletNameMap.get(mapping.getServletName());
          if (servlet_holder == null) {
            throw new IllegalStateException("No such servlet: " + mapping.getServletName());
          }
          if (servlet_holder.isEnabled())
          {


            if (finalMapping == null) {
              finalMapping = mapping;



            }
            else if (finalMapping.isDefault()) {
              finalMapping = mapping;
            } else if (isAllowDuplicateMappings())
            {
              LOG.warn("Multiple servlets map to path {}: {} and {}, choosing {}", new Object[] { pathSpec, finalMapping.getServletName(), mapping.getServletName(), mapping });
              finalMapping = mapping;



            }
            else if (!mapping.isDefault())
            {
              ServletHolder finalMappedServlet = (ServletHolder)_servletNameMap.get(finalMapping.getServletName());
              


              throw new IllegalStateException("Multiple servlets map to path " + pathSpec + ": " + finalMappedServlet.getName() + "[mapped:" + finalMapping.getSource() + "]," + mapping.getServletName() + "[mapped:" + mapping.getSource() + "]");
            }
          }
        }
        
        if (finalMapping == null) {
          throw new IllegalStateException("No acceptable servlet mappings for " + pathSpec);
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("Path={}[{}] mapped to servlet={}[{}]", new Object[] { pathSpec, finalMapping
          
            .getSource(), finalMapping
            .getServletName(), 
            ((ServletHolder)_servletNameMap.get(finalMapping.getServletName())).getSource() });
        }
        ((Map)servletPathMappings).put(pathSpec, finalMapping);
        ((PathMappings)pm).put(new ServletPathSpec(pathSpec), _servletNameMap.get(finalMapping.getServletName()));
      }
      
      _servletPathMap = ((PathMappings)pm);
    }
    
    int i;
    if (_chainCache != null)
    {
      for (i = _chainCache.length; i-- > 0;)
      {
        if (_chainCache[i] != null) {
          _chainCache[i].clear();
        }
      }
    }
    if (LOG.isDebugEnabled())
    {
      LOG.debug("filterNameMap=" + _filterNameMap, new Object[0]);
      LOG.debug("pathFilters=" + _filterPathMappings, new Object[0]);
      LOG.debug("servletFilterMap=" + _filterNameMappings, new Object[0]);
      LOG.debug("servletPathMap=" + _servletPathMap, new Object[0]);
      LOG.debug("servletNameMap=" + _servletNameMap, new Object[0]);
    }
    
    try
    {
      if (((_contextHandler != null) && (_contextHandler.isStarted())) || ((_contextHandler == null) && (isStarted()))) {
        initialize();
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  protected void notFound(Request baseRequest, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    if (LOG.isDebugEnabled())
      LOG.debug("Not Found {}", new Object[] { request.getRequestURI() });
    if (getHandler() != null) {
      nextHandle(URIUtil.addPaths(request.getServletPath(), request.getPathInfo()), baseRequest, request, response);
    }
  }
  
  protected synchronized boolean containsFilterHolder(FilterHolder holder)
  {
    if (_filters == null)
      return false;
    boolean found = false;
    for (FilterHolder f : _filters)
    {
      if (f == holder)
        found = true;
    }
    return found;
  }
  

  protected synchronized boolean containsServletHolder(ServletHolder holder)
  {
    if (_servlets == null)
      return false;
    boolean found = false;
    for (ServletHolder s : _servlets)
    {
      if (s == holder)
        found = true;
    }
    return found;
  }
  





  public void setFilterChainsCached(boolean filterChainsCached)
  {
    _filterChainsCached = filterChainsCached;
  }
  




  public void setFilterMappings(FilterMapping[] filterMappings)
  {
    updateBeans(_filterMappings, filterMappings);
    _filterMappings = filterMappings;
    if (isStarted()) updateMappings();
    invalidateChainsCache();
  }
  

  public synchronized void setFilters(FilterHolder[] holders)
  {
    if (holders != null) {
      for (FilterHolder holder : holders)
        holder.setServletHandler(this);
    }
    updateBeans(_filters, holders);
    _filters = holders;
    updateNameMappings();
    invalidateChainsCache();
  }
  




  public void setServletMappings(ServletMapping[] servletMappings)
  {
    updateBeans(_servletMappings, servletMappings);
    _servletMappings = servletMappings;
    if (isStarted()) updateMappings();
    invalidateChainsCache();
  }
  




  public synchronized void setServlets(ServletHolder[] holders)
  {
    if (holders != null) {
      for (ServletHolder holder : holders)
        holder.setServletHandler(this);
    }
    updateBeans(_servlets, holders);
    _servlets = holders;
    updateNameMappings();
    invalidateChainsCache();
  }
  


  protected class CachedChain
    implements FilterChain
  {
    FilterHolder _filterHolder;
    

    CachedChain _next;
    
    ServletHolder _servletHolder;
    

    protected CachedChain(ServletHolder filters)
    {
      if (filters.size() > 0)
      {
        _filterHolder = ((FilterHolder)filters.get(0));
        filters.remove(0);
        _next = new CachedChain(ServletHandler.this, filters, servletHolder);
      }
      else {
        _servletHolder = servletHolder;
      }
    }
    

    public void doFilter(ServletRequest request, ServletResponse response)
      throws IOException, ServletException
    {
      Request baseRequest = Request.getBaseRequest(request);
      

      if (_filterHolder != null)
      {
        if (ServletHandler.LOG.isDebugEnabled())
          ServletHandler.LOG.debug("call filter {}", new Object[] { _filterHolder });
        Filter filter = _filterHolder.getFilter();
        



        if ((baseRequest.isAsyncSupported()) && (!_filterHolder.isAsyncSupported()))
        {
          try
          {
            baseRequest.setAsyncSupported(false, _filterHolder.toString());
            filter.doFilter(request, response, _next);
          }
          finally
          {
            baseRequest.setAsyncSupported(true, null);
          }
          
        } else {
          filter.doFilter(request, response, _next);
        }
        return;
      }
      

      HttpServletRequest srequest = (HttpServletRequest)request;
      if (_servletHolder == null) {
        notFound(baseRequest, srequest, (HttpServletResponse)response);
      }
      else {
        if (ServletHandler.LOG.isDebugEnabled())
          ServletHandler.LOG.debug("call servlet " + _servletHolder, new Object[0]);
        _servletHolder.handle(baseRequest, request, response);
      }
    }
    

    public String toString()
    {
      if (_filterHolder != null)
        return _filterHolder + "->" + _next.toString();
      if (_servletHolder != null)
        return _servletHolder.toString();
      return "null";
    }
  }
  

  private class Chain
    implements FilterChain
  {
    final Request _baseRequest;
    final List<FilterHolder> _chain;
    final ServletHolder _servletHolder;
    int _filter = 0;
    

    private Chain(List<FilterHolder> baseRequest, ServletHolder filters)
    {
      _baseRequest = baseRequest;
      _chain = filters;
      _servletHolder = servletHolder;
    }
    


    public void doFilter(ServletRequest request, ServletResponse response)
      throws IOException, ServletException
    {
      if (ServletHandler.LOG.isDebugEnabled()) {
        ServletHandler.LOG.debug("doFilter " + _filter, new Object[0]);
      }
      
      if (_filter < _chain.size())
      {
        FilterHolder holder = (FilterHolder)_chain.get(_filter++);
        if (ServletHandler.LOG.isDebugEnabled())
          ServletHandler.LOG.debug("call filter " + holder, new Object[0]);
        Filter filter = holder.getFilter();
        



        if ((!holder.isAsyncSupported()) && (_baseRequest.isAsyncSupported()))
        {
          try
          {
            _baseRequest.setAsyncSupported(false, holder.toString());
            filter.doFilter(request, response, this);
          }
          finally
          {
            _baseRequest.setAsyncSupported(true, null);
          }
          
        } else {
          filter.doFilter(request, response, this);
        }
        return;
      }
      

      HttpServletRequest srequest = (HttpServletRequest)request;
      if (_servletHolder == null) {
        notFound(Request.getBaseRequest(request), srequest, (HttpServletResponse)response);
      }
      else {
        if (ServletHandler.LOG.isDebugEnabled())
          ServletHandler.LOG.debug("call servlet {}", new Object[] { _servletHolder });
        _servletHolder.handle(_baseRequest, request, response);
      }
    }
    


    public String toString()
    {
      StringBuilder b = new StringBuilder();
      for (FilterHolder f : _chain)
      {
        b.append(f.toString());
        b.append("->");
      }
      b.append(_servletHolder);
      return b.toString();
    }
  }
  




  public int getMaxFilterChainsCacheSize()
  {
    return _maxFilterChainsCacheSize;
  }
  







  public void setMaxFilterChainsCacheSize(int maxFilterChainsCacheSize)
  {
    _maxFilterChainsCacheSize = maxFilterChainsCacheSize;
  }
  

  void destroyServlet(Servlet servlet)
  {
    if (_contextHandler != null) {
      _contextHandler.destroyServlet(servlet);
    }
  }
  
  void destroyFilter(Filter filter)
  {
    if (_contextHandler != null) {
      _contextHandler.destroyFilter(filter);
    }
  }
  
  public static class Default404Servlet
    extends HttpServlet
  {
    public Default404Servlet() {}
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException
    {
      resp.sendError(404);
    }
  }
}
