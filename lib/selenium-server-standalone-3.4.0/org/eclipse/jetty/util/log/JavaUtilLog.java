package org.eclipse.jetty.util.log;

import java.io.PrintStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import org.eclipse.jetty.util.Loader;



















































public class JavaUtilLog
  extends AbstractLogger
{
  private static final String THIS_CLASS = JavaUtilLog.class.getName();
  
  private static final boolean __source = Boolean.parseBoolean(Log.__props.getProperty("org.eclipse.jetty.util.log.SOURCE", Log.__props
    .getProperty("org.eclipse.jetty.util.log.javautil.SOURCE", "true")));
  
  private static boolean _initialized = false;
  
  private Level configuredLevel;
  private java.util.logging.Logger _logger;
  
  public JavaUtilLog()
  {
    this("org.eclipse.jetty.util.log.javautil");
  }
  
  public JavaUtilLog(String name)
  {
    synchronized (JavaUtilLog.class)
    {
      if (!_initialized)
      {
        _initialized = true;
        
        final String properties = Log.__props.getProperty("org.eclipse.jetty.util.log.javautil.PROPERTIES", null);
        if (properties != null)
        {
          AccessController.doPrivileged(new PrivilegedAction()
          {
            public Object run()
            {
              try
              {
                URL props = Loader.getResource(properties);
                if (props != null) {
                  LogManager.getLogManager().readConfiguration(props.openStream());
                }
              }
              catch (Throwable e) {
                System.err.println("[WARN] Error loading logging config: " + properties);
                e.printStackTrace(System.err);
              }
              
              return null;
            }
          });
        }
      }
    }
    
    _logger = java.util.logging.Logger.getLogger(name);
    
    switch (lookupLoggingLevel(Log.__props, name))
    {
    case 0: 
      _logger.setLevel(Level.ALL);
      break;
    case 1: 
      _logger.setLevel(Level.FINE);
      break;
    case 2: 
      _logger.setLevel(Level.INFO);
      break;
    case 3: 
      _logger.setLevel(Level.WARNING);
      break;
    case 10: 
      _logger.setLevel(Level.OFF);
      break;
    }
    
    


    configuredLevel = _logger.getLevel();
  }
  
  public String getName()
  {
    return _logger.getName();
  }
  
  protected void log(Level level, String msg, Throwable thrown)
  {
    LogRecord record = new LogRecord(level, msg);
    if (thrown != null)
      record.setThrown(thrown);
    record.setLoggerName(_logger.getName());
    if (__source)
    {
      StackTraceElement[] stack = new Throwable().getStackTrace();
      for (int i = 0; i < stack.length; i++)
      {
        StackTraceElement e = stack[i];
        if (!e.getClassName().equals(THIS_CLASS))
        {
          record.setSourceClassName(e.getClassName());
          record.setSourceMethodName(e.getMethodName());
          break;
        }
      }
    }
    _logger.log(record);
  }
  
  public void warn(String msg, Object... args)
  {
    if (_logger.isLoggable(Level.WARNING)) {
      log(Level.WARNING, format(msg, args), null);
    }
  }
  
  public void warn(Throwable thrown) {
    if (_logger.isLoggable(Level.WARNING)) {
      log(Level.WARNING, "", thrown);
    }
  }
  
  public void warn(String msg, Throwable thrown) {
    if (_logger.isLoggable(Level.WARNING)) {
      log(Level.WARNING, msg, thrown);
    }
  }
  
  public void info(String msg, Object... args) {
    if (_logger.isLoggable(Level.INFO)) {
      log(Level.INFO, format(msg, args), null);
    }
  }
  
  public void info(Throwable thrown) {
    if (_logger.isLoggable(Level.INFO)) {
      log(Level.INFO, "", thrown);
    }
  }
  
  public void info(String msg, Throwable thrown) {
    if (_logger.isLoggable(Level.INFO)) {
      log(Level.INFO, msg, thrown);
    }
  }
  
  public boolean isDebugEnabled() {
    return _logger.isLoggable(Level.FINE);
  }
  
  public void setDebugEnabled(boolean enabled)
  {
    if (enabled)
    {
      configuredLevel = _logger.getLevel();
      _logger.setLevel(Level.FINE);
    }
    else
    {
      _logger.setLevel(configuredLevel);
    }
  }
  
  public void debug(String msg, Object... args)
  {
    if (_logger.isLoggable(Level.FINE)) {
      log(Level.FINE, format(msg, args), null);
    }
  }
  
  public void debug(String msg, long arg) {
    if (_logger.isLoggable(Level.FINE)) {
      log(Level.FINE, format(msg, new Object[] { Long.valueOf(arg) }), null);
    }
  }
  
  public void debug(Throwable thrown) {
    if (_logger.isLoggable(Level.FINE)) {
      log(Level.FINE, "", thrown);
    }
  }
  
  public void debug(String msg, Throwable thrown) {
    if (_logger.isLoggable(Level.FINE)) {
      log(Level.FINE, msg, thrown);
    }
  }
  


  protected Logger newLogger(String fullname)
  {
    return new JavaUtilLog(fullname);
  }
  
  public void ignore(Throwable ignored)
  {
    if (_logger.isLoggable(Level.FINEST)) {
      log(Level.FINEST, "IGNORED EXCEPTION ", ignored);
    }
  }
  
  private String format(String msg, Object... args) {
    msg = String.valueOf(msg);
    String braces = "{}";
    StringBuilder builder = new StringBuilder();
    int start = 0;
    for (Object arg : args)
    {
      int bracesIndex = msg.indexOf(braces, start);
      if (bracesIndex < 0)
      {
        builder.append(msg.substring(start));
        builder.append(" ");
        builder.append(arg);
        start = msg.length();
      }
      else
      {
        builder.append(msg.substring(start, bracesIndex));
        builder.append(String.valueOf(arg));
        start = bracesIndex + braces.length();
      }
    }
    builder.append(msg.substring(start));
    return builder.toString();
  }
}
