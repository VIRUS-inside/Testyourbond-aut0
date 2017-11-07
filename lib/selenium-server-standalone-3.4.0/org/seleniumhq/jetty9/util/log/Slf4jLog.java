package org.seleniumhq.jetty9.util.log;

import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;



















public class Slf4jLog
  extends AbstractLogger
{
  private final org.slf4j.Logger _logger;
  
  public Slf4jLog()
    throws Exception
  {
    this("org.seleniumhq.jetty9.util.log");
  }
  

  public Slf4jLog(String name)
  {
    org.slf4j.Logger logger = LoggerFactory.getLogger(name);
    


    if ((logger instanceof LocationAwareLogger))
    {
      _logger = new JettyAwareLogger((LocationAwareLogger)logger);
    }
    else
    {
      _logger = logger;
    }
  }
  
  public String getName()
  {
    return _logger.getName();
  }
  
  public void warn(String msg, Object... args)
  {
    _logger.warn(msg, args);
  }
  
  public void warn(Throwable thrown)
  {
    warn("", thrown);
  }
  
  public void warn(String msg, Throwable thrown)
  {
    _logger.warn(msg, thrown);
  }
  
  public void info(String msg, Object... args)
  {
    _logger.info(msg, args);
  }
  
  public void info(Throwable thrown)
  {
    info("", thrown);
  }
  
  public void info(String msg, Throwable thrown)
  {
    _logger.info(msg, thrown);
  }
  
  public void debug(String msg, Object... args)
  {
    _logger.debug(msg, args);
  }
  
  public void debug(String msg, long arg)
  {
    if (isDebugEnabled()) {
      _logger.debug(msg, new Object[] { new Long(arg) });
    }
  }
  
  public void debug(Throwable thrown) {
    debug("", thrown);
  }
  
  public void debug(String msg, Throwable thrown)
  {
    _logger.debug(msg, thrown);
  }
  
  public boolean isDebugEnabled()
  {
    return _logger.isDebugEnabled();
  }
  
  public void setDebugEnabled(boolean enabled)
  {
    warn("setDebugEnabled not implemented", new Object[] { null, null });
  }
  



  protected Logger newLogger(String fullname)
  {
    return new Slf4jLog(fullname);
  }
  
  public void ignore(Throwable ignored)
  {
    if (Log.isIgnored())
    {
      debug("IGNORED EXCEPTION ", ignored);
    }
  }
  

  public String toString()
  {
    return _logger.toString();
  }
}
