package org.seleniumhq.jetty9.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.component.Dumpable;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;




















public class FilterHolder
  extends Holder<Filter>
{
  private static final Logger LOG = Log.getLogger(FilterHolder.class);
  

  private transient Filter _filter;
  
  private transient Config _config;
  
  private transient FilterRegistration.Dynamic _registration;
  

  public FilterHolder()
  {
    this(Source.EMBEDDED);
  }
  





  public FilterHolder(Source source)
  {
    super(source);
  }
  




  public FilterHolder(Class<? extends Filter> filter)
  {
    this(Source.EMBEDDED);
    setHeldClass(filter);
  }
  




  public FilterHolder(Filter filter)
  {
    this(Source.EMBEDDED);
    setFilter(filter);
  }
  


  public void doStart()
    throws Exception
  {
    super.doStart();
    

    if (!Filter.class.isAssignableFrom(_class))
    {
      String msg = _class + " is not a javax.servlet.Filter";
      super.stop();
      throw new IllegalStateException(msg);
    }
  }
  






  public void initialize()
    throws Exception
  {
    if (!_initialized)
    {
      super.initialize();
      
      if (_filter == null)
      {
        try
        {
          ServletContext context = _servletHandler.getServletContext();
          

          _filter = ((context instanceof ServletContextHandler.Context) ? ((ServletContextHandler.Context)context).createFilter(getHeldClass()) : (Filter)getHeldClass().newInstance());
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
      
      _config = new Config();
      if (LOG.isDebugEnabled())
        LOG.debug("Filter.init {}", new Object[] { _filter });
      _filter.init(_config);
    }
    
    _initialized = true;
  }
  



  public void doStop()
    throws Exception
  {
    if (_filter != null)
    {
      try
      {
        destroyInstance(_filter);
      }
      catch (Exception e)
      {
        LOG.warn(e);
      }
    }
    if (!_extInstance) {
      _filter = null;
    }
    _config = null;
    _initialized = false;
    super.doStop();
  }
  


  public void destroyInstance(Object o)
    throws Exception
  {
    if (o == null)
      return;
    Filter f = (Filter)o;
    f.destroy();
    getServletHandler().destroyFilter(f);
  }
  

  public synchronized void setFilter(Filter filter)
  {
    _filter = filter;
    _extInstance = true;
    setHeldClass(filter.getClass());
    if (getName() == null) {
      setName(filter.getClass().getName());
    }
  }
  
  public Filter getFilter()
  {
    return _filter;
  }
  


  public String toString()
  {
    return getName();
  }
  

  public void dump(Appendable out, String indent)
    throws IOException
  {
    super.dump(out, indent);
    if ((_filter instanceof Dumpable)) {
      ((Dumpable)_filter).dump(out, indent);
    }
  }
  

  public FilterRegistration.Dynamic getRegistration()
  {
    if (_registration == null)
      _registration = new Registration();
    return _registration;
  }
  
  protected class Registration extends Holder<Filter>.HolderRegistration implements FilterRegistration.Dynamic
  {
    protected Registration() {
      super();
    }
    
    public void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... servletNames) {
      illegalStateIfContextStarted();
      FilterMapping mapping = new FilterMapping();
      mapping.setFilterHolder(FilterHolder.this);
      mapping.setServletNames(servletNames);
      mapping.setDispatcherTypes(dispatcherTypes);
      if (isMatchAfter) {
        _servletHandler.addFilterMapping(mapping);
      } else {
        _servletHandler.prependFilterMapping(mapping);
      }
    }
    
    public void addMappingForUrlPatterns(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... urlPatterns) {
      illegalStateIfContextStarted();
      FilterMapping mapping = new FilterMapping();
      mapping.setFilterHolder(FilterHolder.this);
      mapping.setPathSpecs(urlPatterns);
      mapping.setDispatcherTypes(dispatcherTypes);
      if (isMatchAfter) {
        _servletHandler.addFilterMapping(mapping);
      } else {
        _servletHandler.prependFilterMapping(mapping);
      }
    }
    
    public Collection<String> getServletNameMappings() {
      FilterMapping[] mappings = _servletHandler.getFilterMappings();
      List<String> names = new ArrayList();
      for (FilterMapping mapping : mappings)
      {
        if (mapping.getFilterHolder() == FilterHolder.this)
        {
          String[] servlets = mapping.getServletNames();
          if ((servlets != null) && (servlets.length > 0))
            names.addAll(Arrays.asList(servlets));
        } }
      return names;
    }
    
    public Collection<String> getUrlPatternMappings()
    {
      FilterMapping[] mappings = _servletHandler.getFilterMappings();
      List<String> patterns = new ArrayList();
      for (FilterMapping mapping : mappings)
      {
        if (mapping.getFilterHolder() == FilterHolder.this)
        {
          String[] specs = mapping.getPathSpecs();
          patterns.addAll(TypeUtil.asList(specs));
        } }
      return patterns;
    }
  }
  
  class Config extends Holder<Filter>.HolderConfig implements FilterConfig
  {
    Config() {
      super();
    }
    
    public String getFilterName()
    {
      return _name;
    }
  }
}
