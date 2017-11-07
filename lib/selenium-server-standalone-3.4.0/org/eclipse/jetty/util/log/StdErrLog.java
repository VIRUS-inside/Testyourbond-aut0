package org.eclipse.jetty.util.log;

import java.io.PrintStream;
import java.security.AccessControlException;
import java.util.Map;
import java.util.Properties;
import org.eclipse.jetty.util.DateCache;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.eclipse.jetty.util.annotation.ManagedObject;


















































































@ManagedObject("Jetty StdErr Logging Implementation")
public class StdErrLog
  extends AbstractLogger
{
  private static final String EOL = System.getProperty("line.separator");
  
  private static int __tagpad = Integer.parseInt(Log.__props.getProperty("org.eclipse.jetty.util.log.StdErrLog.TAG_PAD", "0"));
  
  private static DateCache _dateCache;
  private static final boolean __source = Boolean.parseBoolean(Log.__props.getProperty("org.eclipse.jetty.util.log.SOURCE", Log.__props
    .getProperty("org.eclipse.jetty.util.log.stderr.SOURCE", "false")));
  private static final boolean __long = Boolean.parseBoolean(Log.__props.getProperty("org.eclipse.jetty.util.log.stderr.LONG", "false"));
  private static final boolean __escape = Boolean.parseBoolean(Log.__props.getProperty("org.eclipse.jetty.util.log.stderr.ESCAPE", "true"));
  
  static
  {
    String[] deprecatedProperties = { "DEBUG", "org.eclipse.jetty.util.log.DEBUG", "org.eclipse.jetty.util.log.stderr.DEBUG" };
    


    for (String deprecatedProp : deprecatedProperties)
    {
      if (System.getProperty(deprecatedProp) != null)
      {
        System.err.printf("System Property [%s] has been deprecated! (Use org.eclipse.jetty.LEVEL=DEBUG instead)%n", new Object[] { deprecatedProp });
      }
    }
    
    try
    {
      _dateCache = new DateCache("yyyy-MM-dd HH:mm:ss");
    }
    catch (Exception x)
    {
      x.printStackTrace(System.err);
    }
  }
  
  public static void setTagPad(int pad)
  {
    __tagpad = pad;
  }
  

  private int _level = 2;
  
  private int _configuredLevel;
  private PrintStream _stderr = null;
  private boolean _source = __source;
  
  private boolean _printLongNames = __long;
  
  private final String _name;
  
  private final String _abbrevname;
  private boolean _hideStacks = false;
  

  public static int getLoggingLevel(Properties props, String name)
  {
    int level = lookupLoggingLevel(props, name);
    if (level == -1)
    {
      level = lookupLoggingLevel(props, "log");
      if (level == -1)
        level = 2;
    }
    return level;
  }
  












  public static StdErrLog getLogger(Class<?> clazz)
  {
    Logger log = Log.getLogger(clazz);
    if ((log instanceof StdErrLog))
    {
      return (StdErrLog)log;
    }
    throw new RuntimeException("Logger for " + clazz + " is not of type StdErrLog");
  }
  





  public StdErrLog()
  {
    this(null);
  }
  






  public StdErrLog(String name)
  {
    this(name, null);
  }
  








  public StdErrLog(String name, Properties props)
  {
    if ((props != null) && (props != Log.__props))
      Log.__props.putAll(props);
    _name = (name == null ? "" : name);
    _abbrevname = condensePackageString(_name);
    _level = getLoggingLevel(Log.__props, _name);
    _configuredLevel = _level;
    
    try
    {
      String source = getLoggingProperty(Log.__props, _name, "SOURCE");
      _source = (source == null ? __source : Boolean.parseBoolean(source));
    }
    catch (AccessControlException ace)
    {
      _source = __source;
    }
    

    try
    {
      String stacks = getLoggingProperty(Log.__props, _name, "STACKS");
      _hideStacks = (stacks != null);
    }
    catch (AccessControlException localAccessControlException1) {}
  }
  



  public String getName()
  {
    return _name;
  }
  
  public void setPrintLongNames(boolean printLongNames)
  {
    _printLongNames = printLongNames;
  }
  
  public boolean isPrintLongNames()
  {
    return _printLongNames;
  }
  
  public boolean isHideStacks()
  {
    return _hideStacks;
  }
  
  public void setHideStacks(boolean hideStacks)
  {
    _hideStacks = hideStacks;
  }
  






  public boolean isSource()
  {
    return _source;
  }
  







  public void setSource(boolean source)
  {
    _source = source;
  }
  
  public void warn(String msg, Object... args)
  {
    if (_level <= 3)
    {
      StringBuilder buffer = new StringBuilder(64);
      format(buffer, ":WARN:", msg, args);
      (_stderr == null ? System.err : _stderr).println(buffer);
    }
  }
  
  public void warn(Throwable thrown)
  {
    warn("", thrown);
  }
  
  public void warn(String msg, Throwable thrown)
  {
    if (_level <= 3)
    {
      StringBuilder buffer = new StringBuilder(64);
      format(buffer, ":WARN:", msg, thrown);
      (_stderr == null ? System.err : _stderr).println(buffer);
    }
  }
  
  public void info(String msg, Object... args)
  {
    if (_level <= 2)
    {
      StringBuilder buffer = new StringBuilder(64);
      format(buffer, ":INFO:", msg, args);
      (_stderr == null ? System.err : _stderr).println(buffer);
    }
  }
  
  public void info(Throwable thrown)
  {
    info("", thrown);
  }
  
  public void info(String msg, Throwable thrown)
  {
    if (_level <= 2)
    {
      StringBuilder buffer = new StringBuilder(64);
      format(buffer, ":INFO:", msg, thrown);
      (_stderr == null ? System.err : _stderr).println(buffer);
    }
  }
  
  @ManagedAttribute("is debug enabled for root logger Log.LOG")
  public boolean isDebugEnabled()
  {
    return _level <= 1;
  }
  





  public void setDebugEnabled(boolean enabled)
  {
    if (enabled)
    {
      _level = 1;
      
      for (Logger log : Log.getLoggers().values())
      {
        if ((log.getName().startsWith(getName())) && ((log instanceof StdErrLog))) {
          ((StdErrLog)log).setLevel(1);
        }
      }
    }
    else {
      _level = _configuredLevel;
      
      for (Logger log : Log.getLoggers().values())
      {
        if ((log.getName().startsWith(getName())) && ((log instanceof StdErrLog))) {
          ((StdErrLog)log).setLevel(_configuredLevel);
        }
      }
    }
  }
  
  public int getLevel() {
    return _level;
  }
  









  public void setLevel(int level)
  {
    _level = level;
  }
  
  public void setStdErrStream(PrintStream stream)
  {
    _stderr = (stream == System.err ? null : stream);
  }
  
  public void debug(String msg, Object... args)
  {
    if (_level <= 1)
    {
      StringBuilder buffer = new StringBuilder(64);
      format(buffer, ":DBUG:", msg, args);
      (_stderr == null ? System.err : _stderr).println(buffer);
    }
  }
  
  public void debug(String msg, long arg)
  {
    if (isDebugEnabled())
    {
      StringBuilder buffer = new StringBuilder(64);
      format(buffer, ":DBUG:", msg, new Object[] { Long.valueOf(arg) });
      (_stderr == null ? System.err : _stderr).println(buffer);
    }
  }
  
  public void debug(Throwable thrown)
  {
    debug("", thrown);
  }
  
  public void debug(String msg, Throwable thrown)
  {
    if (_level <= 1)
    {
      StringBuilder buffer = new StringBuilder(64);
      format(buffer, ":DBUG:", msg, thrown);
      (_stderr == null ? System.err : _stderr).println(buffer);
    }
  }
  
  private void format(StringBuilder buffer, String level, String msg, Object... args)
  {
    long now = System.currentTimeMillis();
    int ms = (int)(now % 1000L);
    String d = _dateCache.formatNow(now);
    tag(buffer, d, ms, level);
    format(buffer, msg, args);
  }
  
  private void format(StringBuilder buffer, String level, String msg, Throwable thrown)
  {
    format(buffer, level, msg, new Object[0]);
    if (isHideStacks())
    {
      format(buffer, ": " + String.valueOf(thrown), new Object[0]);
    }
    else
    {
      format(buffer, thrown);
    }
  }
  
  private void tag(StringBuilder buffer, String d, int ms, String tag)
  {
    buffer.setLength(0);
    buffer.append(d);
    if (ms > 99)
    {
      buffer.append('.');
    }
    else if (ms > 9)
    {
      buffer.append(".0");
    }
    else
    {
      buffer.append(".00");
    }
    buffer.append(ms).append(tag);
    
    String name = _printLongNames ? _name : _abbrevname;
    String tname = Thread.currentThread().getName();
    
    int p = __tagpad > 0 ? name.length() + tname.length() - __tagpad : 0;
    
    if (p < 0)
    {




      buffer.append(name).append(':').append("                                                  ", 0, -p).append(tname);
    }
    else if (p == 0)
    {
      buffer.append(name).append(':').append(tname);
    }
    buffer.append(':');
    
    if (_source)
    {
      Throwable source = new Throwable();
      StackTraceElement[] frames = source.getStackTrace();
      for (int i = 0; i < frames.length; i++)
      {
        StackTraceElement frame = frames[i];
        String clazz = frame.getClassName();
        if ((!clazz.equals(StdErrLog.class.getName())) && (!clazz.equals(Log.class.getName())))
        {


          if ((!_printLongNames) && (clazz.startsWith("org.eclipse.jetty.")))
          {
            buffer.append(condensePackageString(clazz));
          }
          else
          {
            buffer.append(clazz);
          }
          buffer.append('#').append(frame.getMethodName());
          if (frame.getFileName() != null)
          {
            buffer.append('(').append(frame.getFileName()).append(':').append(frame.getLineNumber()).append(')');
          }
          buffer.append(':');
          break;
        }
      }
    }
    buffer.append(' ');
  }
  
  private void format(StringBuilder builder, String msg, Object... args)
  {
    if (msg == null)
    {
      msg = "";
      for (int i = 0; i < args.length; i++)
      {
        msg = msg + "{} ";
      }
    }
    String braces = "{}";
    int start = 0;
    for (Object arg : args)
    {
      int bracesIndex = msg.indexOf(braces, start);
      if (bracesIndex < 0)
      {
        escape(builder, msg.substring(start));
        builder.append(" ");
        builder.append(arg);
        start = msg.length();
      }
      else
      {
        escape(builder, msg.substring(start, bracesIndex));
        builder.append(String.valueOf(arg));
        start = bracesIndex + braces.length();
      }
    }
    escape(builder, msg.substring(start));
  }
  
  private void escape(StringBuilder builder, String string)
  {
    if (__escape)
    {
      for (int i = 0; i < string.length(); i++)
      {
        char c = string.charAt(i);
        if (Character.isISOControl(c))
        {
          if (c == '\n')
          {
            builder.append('|');
          }
          else if (c == '\r')
          {
            builder.append('<');
          }
          else
          {
            builder.append('?');
          }
          
        }
        else {
          builder.append(c);
        }
        
      }
    } else {
      builder.append(string);
    }
  }
  
  protected void format(StringBuilder buffer, Throwable thrown) {
    format(buffer, thrown, "");
  }
  
  protected void format(StringBuilder buffer, Throwable thrown, String indent)
  {
    if (thrown == null)
    {
      buffer.append("null");
    }
    else
    {
      buffer.append(EOL).append(indent);
      format(buffer, thrown.toString(), new Object[0]);
      StackTraceElement[] elements = thrown.getStackTrace();
      for (int i = 0; (elements != null) && (i < elements.length); i++)
      {
        buffer.append(EOL).append(indent).append("\tat ");
        format(buffer, elements[i].toString(), new Object[0]);
      }
      
      for (Throwable suppressed : thrown.getSuppressed())
      {
        buffer.append(EOL).append(indent).append("Suppressed: ");
        format(buffer, suppressed, "\t|" + indent);
      }
      
      Throwable cause = thrown.getCause();
      if ((cause != null) && (cause != thrown))
      {
        buffer.append(EOL).append(indent).append("Caused by: ");
        format(buffer, cause, indent);
      }
    }
  }
  





  protected Logger newLogger(String fullname)
  {
    StdErrLog logger = new StdErrLog(fullname);
    
    logger.setPrintLongNames(_printLongNames);
    _stderr = _stderr;
    

    if (_level != _configuredLevel) {
      _level = _level;
    }
    return logger;
  }
  

  public String toString()
  {
    StringBuilder s = new StringBuilder();
    s.append("StdErrLog:");
    s.append(_name);
    s.append(":LEVEL=");
    switch (_level)
    {
    case 0: 
      s.append("ALL");
      break;
    case 1: 
      s.append("DEBUG");
      break;
    case 2: 
      s.append("INFO");
      break;
    case 3: 
      s.append("WARN");
      break;
    default: 
      s.append("?");
    }
    
    return s.toString();
  }
  
  public void ignore(Throwable ignored)
  {
    if (_level <= 0)
    {
      StringBuilder buffer = new StringBuilder(64);
      format(buffer, ":IGNORED:", "", ignored);
      (_stderr == null ? System.err : _stderr).println(buffer);
    }
  }
}
