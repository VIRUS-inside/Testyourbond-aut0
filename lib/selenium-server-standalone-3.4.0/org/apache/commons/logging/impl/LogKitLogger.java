package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

































public class LogKitLogger
  implements Log, Serializable
{
  private static final long serialVersionUID = 3768538055836059519L;
  protected volatile transient Logger logger = null;
  

  protected String name = null;
  







  public LogKitLogger(String name)
  {
    this.name = name;
    logger = getLogger();
  }
  




  public Logger getLogger()
  {
    Logger result = logger;
    if (result == null) {
      synchronized (this) {
        result = logger;
        if (result == null) {
          logger = (result = Hierarchy.getDefaultHierarchy().getLoggerFor(name));
        }
      }
    }
    return result;
  }
  







  public void trace(Object message)
  {
    debug(message);
  }
  






  public void trace(Object message, Throwable t)
  {
    debug(message, t);
  }
  





  public void debug(Object message)
  {
    if (message != null) {
      getLogger().debug(String.valueOf(message));
    }
  }
  






  public void debug(Object message, Throwable t)
  {
    if (message != null) {
      getLogger().debug(String.valueOf(message), t);
    }
  }
  





  public void info(Object message)
  {
    if (message != null) {
      getLogger().info(String.valueOf(message));
    }
  }
  






  public void info(Object message, Throwable t)
  {
    if (message != null) {
      getLogger().info(String.valueOf(message), t);
    }
  }
  





  public void warn(Object message)
  {
    if (message != null) {
      getLogger().warn(String.valueOf(message));
    }
  }
  






  public void warn(Object message, Throwable t)
  {
    if (message != null) {
      getLogger().warn(String.valueOf(message), t);
    }
  }
  





  public void error(Object message)
  {
    if (message != null) {
      getLogger().error(String.valueOf(message));
    }
  }
  






  public void error(Object message, Throwable t)
  {
    if (message != null) {
      getLogger().error(String.valueOf(message), t);
    }
  }
  





  public void fatal(Object message)
  {
    if (message != null) {
      getLogger().fatalError(String.valueOf(message));
    }
  }
  






  public void fatal(Object message, Throwable t)
  {
    if (message != null) {
      getLogger().fatalError(String.valueOf(message), t);
    }
  }
  


  public boolean isDebugEnabled()
  {
    return getLogger().isDebugEnabled();
  }
  


  public boolean isErrorEnabled()
  {
    return getLogger().isErrorEnabled();
  }
  


  public boolean isFatalEnabled()
  {
    return getLogger().isFatalErrorEnabled();
  }
  


  public boolean isInfoEnabled()
  {
    return getLogger().isInfoEnabled();
  }
  


  public boolean isTraceEnabled()
  {
    return getLogger().isDebugEnabled();
  }
  


  public boolean isWarnEnabled()
  {
    return getLogger().isWarnEnabled();
  }
}
