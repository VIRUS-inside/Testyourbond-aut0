package org.apache.commons.logging.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import org.apache.commons.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;








































public class Log4JLogger
  implements Log, Serializable
{
  private static final long serialVersionUID = 5160705895411730424L;
  private static final String FQCN = Log4JLogger.class.getName();
  

  private volatile transient Logger logger = null;
  





  private final String name;
  




  private static final Priority traceLevel;
  





  static
  {
    if (!Priority.class.isAssignableFrom(Level.class))
    {
      throw new InstantiationError("Log4J 1.2 not available");
    }
    

    Priority _traceLevel;
    

    try
    {
      _traceLevel = (Priority)Level.class.getDeclaredField("TRACE").get(null);
    }
    catch (Exception ex) {
      _traceLevel = Level.DEBUG;
    }
    traceLevel = _traceLevel;
  }
  

  public Log4JLogger()
  {
    name = null;
  }
  


  public Log4JLogger(String name)
  {
    this.name = name;
    logger = getLogger();
  }
  


  public Log4JLogger(Logger logger)
  {
    if (logger == null) {
      throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration.");
    }
    
    name = logger.getName();
    this.logger = logger;
  }
  







  public void trace(Object message)
  {
    getLogger().log(FQCN, traceLevel, message, null);
  }
  








  public void trace(Object message, Throwable t)
  {
    getLogger().log(FQCN, traceLevel, message, t);
  }
  





  public void debug(Object message)
  {
    getLogger().log(FQCN, Level.DEBUG, message, null);
  }
  






  public void debug(Object message, Throwable t)
  {
    getLogger().log(FQCN, Level.DEBUG, message, t);
  }
  





  public void info(Object message)
  {
    getLogger().log(FQCN, Level.INFO, message, null);
  }
  






  public void info(Object message, Throwable t)
  {
    getLogger().log(FQCN, Level.INFO, message, t);
  }
  





  public void warn(Object message)
  {
    getLogger().log(FQCN, Level.WARN, message, null);
  }
  






  public void warn(Object message, Throwable t)
  {
    getLogger().log(FQCN, Level.WARN, message, t);
  }
  





  public void error(Object message)
  {
    getLogger().log(FQCN, Level.ERROR, message, null);
  }
  






  public void error(Object message, Throwable t)
  {
    getLogger().log(FQCN, Level.ERROR, message, t);
  }
  





  public void fatal(Object message)
  {
    getLogger().log(FQCN, Level.FATAL, message, null);
  }
  






  public void fatal(Object message, Throwable t)
  {
    getLogger().log(FQCN, Level.FATAL, message, t);
  }
  


  public Logger getLogger()
  {
    Logger result = logger;
    if (result == null) {
      synchronized (this) {
        result = logger;
        if (result == null) {
          logger = (result = Logger.getLogger(name));
        }
      }
    }
    return result;
  }
  


  public boolean isDebugEnabled()
  {
    return getLogger().isDebugEnabled();
  }
  


  public boolean isErrorEnabled()
  {
    return getLogger().isEnabledFor(Level.ERROR);
  }
  


  public boolean isFatalEnabled()
  {
    return getLogger().isEnabledFor(Level.FATAL);
  }
  


  public boolean isInfoEnabled()
  {
    return getLogger().isInfoEnabled();
  }
  




  public boolean isTraceEnabled()
  {
    return getLogger().isEnabledFor(traceLevel);
  }
  


  public boolean isWarnEnabled()
  {
    return getLogger().isEnabledFor(Level.WARN);
  }
}
