package org.seleniumhq.jetty9.util.log;

import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentMap;


















public abstract class AbstractLogger
  implements Logger
{
  public static final int LEVEL_DEFAULT = -1;
  public static final int LEVEL_ALL = 0;
  public static final int LEVEL_DEBUG = 1;
  public static final int LEVEL_INFO = 2;
  public static final int LEVEL_WARN = 3;
  public static final int LEVEL_OFF = 10;
  
  public AbstractLogger() {}
  
  public final Logger getLogger(String name)
  {
    if (isBlank(name)) {
      return this;
    }
    String basename = getName();
    String fullname = basename + "." + name;
    
    Logger logger = (Logger)Log.getLoggers().get(fullname);
    if (logger == null)
    {
      Logger newlog = newLogger(fullname);
      
      logger = (Logger)Log.getMutableLoggers().putIfAbsent(fullname, newlog);
      if (logger == null) {
        logger = newlog;
      }
    }
    return logger;
  }
  




  protected abstract Logger newLogger(String paramString);
  




  private static boolean isBlank(String name)
  {
    if (name == null)
    {
      return true;
    }
    int size = name.length();
    
    for (int i = 0; i < size; i++)
    {
      char c = name.charAt(i);
      if (!Character.isWhitespace(c))
      {
        return false;
      }
    }
    return true;
  }
  










  public static int lookupLoggingLevel(Properties props, String name)
  {
    if ((props == null) || (props.isEmpty()) || (name == null)) {
      return -1;
    }
    

    String nameSegment = name;
    
    while ((nameSegment != null) && (nameSegment.length() > 0))
    {
      String levelStr = props.getProperty(nameSegment + ".LEVEL");
      
      int level = getLevelId(nameSegment + ".LEVEL", levelStr);
      if (level != -1)
      {
        return level;
      }
      

      int idx = nameSegment.lastIndexOf('.');
      if (idx >= 0)
      {
        nameSegment = nameSegment.substring(0, idx);
      }
      else
      {
        nameSegment = null;
      }
    }
    

    return -1;
  }
  



  public static String getLoggingProperty(Properties props, String name, String property)
  {
    String nameSegment = name;
    
    while ((nameSegment != null) && (nameSegment.length() > 0))
    {
      String s = props.getProperty(nameSegment + "." + property);
      if (s != null) {
        return s;
      }
      
      int idx = nameSegment.lastIndexOf('.');
      nameSegment = idx >= 0 ? nameSegment.substring(0, idx) : null;
    }
    
    return null;
  }
  

  protected static int getLevelId(String levelSegment, String levelName)
  {
    if (levelName == null)
    {
      return -1;
    }
    String levelStr = levelName.trim();
    if ("ALL".equalsIgnoreCase(levelStr))
    {
      return 0;
    }
    if ("DEBUG".equalsIgnoreCase(levelStr))
    {
      return 1;
    }
    if ("INFO".equalsIgnoreCase(levelStr))
    {
      return 2;
    }
    if ("WARN".equalsIgnoreCase(levelStr))
    {
      return 3;
    }
    if ("OFF".equalsIgnoreCase(levelStr))
    {
      return 10;
    }
    
    System.err.println("Unknown StdErrLog level [" + levelSegment + "]=[" + levelStr + "], expecting only [ALL, DEBUG, INFO, WARN, OFF] as values.");
    return -1;
  }
  















  protected static String condensePackageString(String classname)
  {
    String[] parts = classname.split("\\.");
    StringBuilder dense = new StringBuilder();
    for (int i = 0; i < parts.length - 1; i++)
    {
      dense.append(parts[i].charAt(0));
    }
    if (dense.length() > 0)
    {
      dense.append('.');
    }
    dense.append(parts[(parts.length - 1)]);
    return dense.toString();
  }
  

  public void debug(String msg, long arg)
  {
    if (isDebugEnabled())
    {
      debug(msg, new Object[] { new Long(arg) });
    }
  }
}
