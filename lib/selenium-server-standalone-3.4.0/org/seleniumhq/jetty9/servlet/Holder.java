package org.seleniumhq.jetty9.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.Registration.Dynamic;
import javax.servlet.ServletContext;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



























@ManagedObject("Holder - a container for servlets and the like")
public class Holder<T>
  extends BaseHolder<T>
{
  private static final Logger LOG = Log.getLogger(Holder.class);
  
  protected final Map<String, String> _initParams = new HashMap(3);
  protected String _displayName;
  protected boolean _asyncSupported;
  protected String _name;
  protected boolean _initialized = false;
  


  protected Holder(Source source)
  {
    super(source);
    switch (1.$SwitchMap$org$eclipse$jetty$servlet$Source$Origin[_source.getOrigin().ordinal()])
    {
    case 1: 
    case 2: 
    case 3: 
      _asyncSupported = false;
      break;
    default: 
      _asyncSupported = true;
    }
    
  }
  


  @ManagedAttribute(value="Display Name", readonly=true)
  public String getDisplayName()
  {
    return _displayName;
  }
  

  public String getInitParameter(String param)
  {
    if (_initParams == null)
      return null;
    return (String)_initParams.get(param);
  }
  

  public Enumeration<String> getInitParameterNames()
  {
    if (_initParams == null)
      return Collections.enumeration(Collections.EMPTY_LIST);
    return Collections.enumeration(_initParams.keySet());
  }
  

  @ManagedAttribute(value="Initial Parameters", readonly=true)
  public Map<String, String> getInitParameters()
  {
    return _initParams;
  }
  

  @ManagedAttribute(value="Name", readonly=true)
  public String getName()
  {
    return _name;
  }
  



  public void destroyInstance(Object instance)
    throws Exception
  {}
  



  public void setClassName(String className)
  {
    super.setClassName(className);
    if (_name == null) {
      _name = (className + "-" + Integer.toHexString(hashCode()));
    }
  }
  



  public void setHeldClass(Class<? extends T> held)
  {
    super.setHeldClass(held);
    if (held != null)
    {
      if (_name == null) {
        _name = (held.getName() + "-" + Integer.toHexString(hashCode()));
      }
    }
  }
  
  public void setDisplayName(String name)
  {
    _displayName = name;
  }
  

  public void setInitParameter(String param, String value)
  {
    _initParams.put(param, value);
  }
  

  public void setInitParameters(Map<String, String> map)
  {
    _initParams.clear();
    _initParams.putAll(map);
  }
  







  public void setName(String name)
  {
    _name = name;
  }
  


  public void setAsyncSupported(boolean suspendable)
  {
    _asyncSupported = suspendable;
  }
  

  public boolean isAsyncSupported()
  {
    return _asyncSupported;
  }
  


  public void dump(Appendable out, String indent)
    throws IOException
  {
    super.dump(out, indent);
    ContainerLifeCycle.dump(out, indent, new Collection[] { _initParams.entrySet() });
  }
  


  public String dump()
  {
    return super.dump();
  }
  


  public String toString()
  {
    return String.format("%s@%x==%s", new Object[] { _name, Integer.valueOf(hashCode()), _className });
  }
  


  protected class HolderConfig
  {
    protected HolderConfig() {}
    

    public ServletContext getServletContext()
    {
      return _servletHandler.getServletContext();
    }
    

    public String getInitParameter(String param)
    {
      return Holder.this.getInitParameter(param);
    }
    

    public Enumeration<String> getInitParameterNames()
    {
      return Holder.this.getInitParameterNames();
    }
  }
  
  protected class HolderRegistration
    implements Registration.Dynamic
  {
    protected HolderRegistration() {}
    
    public void setAsyncSupported(boolean isAsyncSupported)
    {
      illegalStateIfContextStarted();
      Holder.this.setAsyncSupported(isAsyncSupported);
    }
    
    public void setDescription(String description)
    {
      if (Holder.LOG.isDebugEnabled()) {
        Holder.LOG.debug(this + " is " + description, new Object[0]);
      }
    }
    
    public String getClassName() {
      return Holder.this.getClassName();
    }
    
    public String getInitParameter(String name)
    {
      return Holder.this.getInitParameter(name);
    }
    
    public Map<String, String> getInitParameters()
    {
      return Holder.this.getInitParameters();
    }
    
    public String getName()
    {
      return Holder.this.getName();
    }
    
    public boolean setInitParameter(String name, String value)
    {
      illegalStateIfContextStarted();
      if (name == null) {
        throw new IllegalArgumentException("init parameter name required");
      }
      if (value == null) {
        throw new IllegalArgumentException("non-null value required for init parameter " + name);
      }
      if (Holder.this.getInitParameter(name) != null)
        return false;
      setInitParameter(name, value);
      return true;
    }
    
    public Set<String> setInitParameters(Map<String, String> initParameters)
    {
      illegalStateIfContextStarted();
      Set<String> clash = null;
      for (Map.Entry<String, String> entry : initParameters.entrySet())
      {
        if (entry.getKey() == null) {
          throw new IllegalArgumentException("init parameter name required");
        }
        if (entry.getValue() == null) {
          throw new IllegalArgumentException("non-null value required for init parameter " + (String)entry.getKey());
        }
        if (Holder.this.getInitParameter((String)entry.getKey()) != null)
        {
          if (clash == null)
            clash = new HashSet();
          clash.add(entry.getKey());
        }
      }
      if (clash != null)
        return clash;
      Holder.this.getInitParameters().putAll(initParameters);
      return Collections.emptySet();
    }
  }
}
