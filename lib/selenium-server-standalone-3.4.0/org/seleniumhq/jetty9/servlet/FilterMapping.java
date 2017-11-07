package org.seleniumhq.jetty9.servlet;

import java.io.IOException;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.seleniumhq.jetty9.http.PathMap;
import org.seleniumhq.jetty9.util.TypeUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.component.Dumpable;



























@ManagedObject("Filter Mappings")
public class FilterMapping
  implements Dumpable
{
  public static final int DEFAULT = 0;
  public static final int REQUEST = 1;
  public static final int FORWARD = 2;
  public static final int INCLUDE = 4;
  public static final int ERROR = 8;
  public static final int ASYNC = 16;
  public static final int ALL = 31;
  
  public static DispatcherType dispatch(String type)
  {
    if ("request".equalsIgnoreCase(type))
      return DispatcherType.REQUEST;
    if ("forward".equalsIgnoreCase(type))
      return DispatcherType.FORWARD;
    if ("include".equalsIgnoreCase(type))
      return DispatcherType.INCLUDE;
    if ("error".equalsIgnoreCase(type))
      return DispatcherType.ERROR;
    if ("async".equalsIgnoreCase(type))
      return DispatcherType.ASYNC;
    throw new IllegalArgumentException(type);
  }
  





  public static int dispatch(DispatcherType type)
  {
    switch (1.$SwitchMap$javax$servlet$DispatcherType[type.ordinal()])
    {
    case 1: 
      return 1;
    case 2: 
      return 16;
    case 3: 
      return 2;
    case 4: 
      return 4;
    case 5: 
      return 8;
    }
    throw new IllegalArgumentException(type.toString());
  }
  





  private int _dispatches = 0;
  

  private String _filterName;
  

  private transient FilterHolder _holder;
  
  private String[] _pathSpecs;
  
  private String[] _servletNames;
  

  public FilterMapping() {}
  

  boolean appliesTo(String path, int type)
  {
    if (appliesTo(type))
    {
      for (int i = 0; i < _pathSpecs.length; i++) {
        if ((_pathSpecs[i] != null) && (PathMap.match(_pathSpecs[i], path, true)))
          return true;
      }
    }
    return false;
  }
  






  boolean appliesTo(int type)
  {
    if (_dispatches == 0)
      return (type == 1) || ((type == 16) && (_holder.isAsyncSupported()));
    return (_dispatches & type) != 0;
  }
  

  public boolean appliesTo(DispatcherType t)
  {
    return appliesTo(dispatch(t));
  }
  

  public boolean isDefaultDispatches()
  {
    return _dispatches == 0;
  }
  




  @ManagedAttribute(value="filter name", readonly=true)
  public String getFilterName()
  {
    return _filterName;
  }
  




  FilterHolder getFilterHolder()
  {
    return _holder;
  }
  




  @ManagedAttribute(value="url patterns", readonly=true)
  public String[] getPathSpecs()
  {
    return _pathSpecs;
  }
  

  public void setDispatcherTypes(EnumSet<DispatcherType> dispatcherTypes)
  {
    _dispatches = 0;
    if (dispatcherTypes != null)
    {
      if (dispatcherTypes.contains(DispatcherType.ERROR))
        _dispatches |= 0x8;
      if (dispatcherTypes.contains(DispatcherType.FORWARD))
        _dispatches |= 0x2;
      if (dispatcherTypes.contains(DispatcherType.INCLUDE))
        _dispatches |= 0x4;
      if (dispatcherTypes.contains(DispatcherType.REQUEST))
        _dispatches |= 0x1;
      if (dispatcherTypes.contains(DispatcherType.ASYNC)) {
        _dispatches |= 0x10;
      }
    }
  }
  
  public EnumSet<DispatcherType> getDispatcherTypes()
  {
    EnumSet<DispatcherType> dispatcherTypes = EnumSet.noneOf(DispatcherType.class);
    if ((_dispatches & 0x8) == 8)
      dispatcherTypes.add(DispatcherType.ERROR);
    if ((_dispatches & 0x2) == 2)
      dispatcherTypes.add(DispatcherType.FORWARD);
    if ((_dispatches & 0x4) == 4)
      dispatcherTypes.add(DispatcherType.INCLUDE);
    if ((_dispatches & 0x1) == 1)
      dispatcherTypes.add(DispatcherType.REQUEST);
    if ((_dispatches & 0x10) == 16)
      dispatcherTypes.add(DispatcherType.ASYNC);
    return dispatcherTypes;
  }
  









  public void setDispatches(int dispatches)
  {
    _dispatches = dispatches;
  }
  




  public void setFilterName(String filterName)
  {
    _filterName = filterName;
  }
  




  void setFilterHolder(FilterHolder holder)
  {
    _holder = holder;
    setFilterName(holder.getName());
  }
  




  public void setPathSpecs(String[] pathSpecs)
  {
    _pathSpecs = pathSpecs;
  }
  




  public void setPathSpec(String pathSpec)
  {
    _pathSpecs = new String[] { pathSpec };
  }
  




  @ManagedAttribute(value="servlet names", readonly=true)
  public String[] getServletNames()
  {
    return _servletNames;
  }
  





  public void setServletNames(String[] servletNames)
  {
    _servletNames = servletNames;
  }
  





  public void setServletName(String servletName)
  {
    _servletNames = new String[] { servletName };
  }
  

  public String toString()
  {
    return 
    
      TypeUtil.asList(_pathSpecs) + "/" + TypeUtil.asList(_servletNames) + "==" + _dispatches + "=>" + _filterName;
  }
  


  public void dump(Appendable out, String indent)
    throws IOException
  {
    out.append(String.valueOf(this)).append("\n");
  }
  

  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
}
