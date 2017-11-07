package org.seleniumhq.jetty9.servlet;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.UnavailableException;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.server.handler.ContextHandler.Context;
import org.seleniumhq.jetty9.util.Loader;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.component.Dumpable;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;


























public abstract class BaseHolder<T>
  extends AbstractLifeCycle
  implements Dumpable
{
  private static final Logger LOG = Log.getLogger(BaseHolder.class);
  
  protected final Source _source;
  
  protected transient Class<? extends T> _class;
  
  protected String _className;
  protected boolean _extInstance;
  protected ServletHandler _servletHandler;
  
  protected BaseHolder(Source source)
  {
    _source = source;
  }
  

  public Source getSource()
  {
    return _source;
  }
  





  public void initialize()
    throws Exception
  {
    if (!isStarted()) {
      throw new IllegalStateException("Not started: " + this);
    }
  }
  



  public void doStart()
    throws Exception
  {
    if ((_class == null) && ((_className == null) || (_className.equals("")))) {
      throw new UnavailableException("No class in holder " + toString());
    }
    
    if (_class == null)
    {
      try
      {
        _class = Loader.loadClass(_className);
        if (LOG.isDebugEnabled()) {
          LOG.debug("Holding {} from {}", new Object[] { _class, _class.getClassLoader() });
        }
      }
      catch (Exception e) {
        LOG.warn(e);
        throw new UnavailableException("Class loading error for holder " + toString());
      }
    }
  }
  



  public void doStop()
    throws Exception
  {
    if (!_extInstance) {
      _class = null;
    }
  }
  

  @ManagedAttribute(value="Class Name", readonly=true)
  public String getClassName()
  {
    return _className;
  }
  

  public Class<? extends T> getHeldClass()
  {
    return _class;
  }
  




  public ServletHandler getServletHandler()
  {
    return _servletHandler;
  }
  





  public void setServletHandler(ServletHandler servletHandler)
  {
    _servletHandler = servletHandler;
  }
  




  public void setClassName(String className)
  {
    _className = className;
    _class = null;
  }
  




  public void setHeldClass(Class<? extends T> held)
  {
    _class = held;
    if (held != null)
    {
      _className = held.getName();
    }
  }
  


  protected void illegalStateIfContextStarted()
  {
    if (_servletHandler != null)
    {
      ServletContext context = _servletHandler.getServletContext();
      if (((context instanceof ContextHandler.Context)) && (((ContextHandler.Context)context).getContextHandler().isStarted())) {
        throw new IllegalStateException("Started");
      }
    }
  }
  



  public boolean isInstance()
  {
    return _extInstance;
  }
  



  public void dump(Appendable out, String indent)
    throws IOException
  {
    out.append(toString()).append(" - ").append(AbstractLifeCycle.getState(this)).append("\n");
  }
  


  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
}
