package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;






























































































































































public class WebConsole
  implements Serializable
{
  private Formatter formatter_ = new DefaultFormatter(null);
  private Logger logger_ = new DefaultLogger(null);
  

  public WebConsole() {}
  
  public void setFormatter(Formatter formatter)
  {
    formatter_ = formatter;
  }
  



  public Formatter getFormatter()
  {
    return formatter_;
  }
  



  public void setLogger(Logger logger)
  {
    logger_ = logger;
  }
  



  public Logger getLogger()
  {
    return logger_;
  }
  



  public void trace(Object... args)
  {
    if (logger_.isTraceEnabled()) {
      logger_.trace(process(args));
    }
  }
  



  public void debug(Object... args)
  {
    if (logger_.isDebugEnabled()) {
      logger_.debug(process(args));
    }
  }
  



  public void info(Object... args)
  {
    if (logger_.isInfoEnabled()) {
      logger_.info(process(args));
    }
  }
  



  public void warn(Object... args)
  {
    if (logger_.isWarnEnabled()) {
      logger_.warn(process(args));
    }
  }
  



  public void error(Object... args)
  {
    if (logger_.isErrorEnabled()) {
      logger_.error(process(args));
    }
  }
  








  private String process(Object[] objs)
  {
    if (objs == null) {
      return "null";
    }
    
    StringBuilder sb = new StringBuilder();
    LinkedList<Object> args = new LinkedList(Arrays.asList(objs));
    
    Formatter formatter = getFormatter();
    int startPos;
    if ((args.size() > 1) && ((args.get(0) instanceof String))) {
      StringBuilder msg = new StringBuilder((String)args.remove(0));
      startPos = msg.indexOf("%");
      
      while ((startPos > -1) && (startPos < msg.length() - 1) && (args.size() > 0)) {
        if ((startPos != 0) && (msg.charAt(startPos - 1) == '%'))
        {
          msg.replace(startPos, startPos + 1, "");
        }
        else {
          char type = msg.charAt(startPos + 1);
          String replacement = null;
          switch (type) {
          case 'o': 
          case 's': 
            replacement = formatter.parameterAsString(pop(args));
            break;
          case 'd': 
          case 'i': 
            replacement = formatter.parameterAsInteger(pop(args));
            break;
          case 'f': 
            replacement = formatter.parameterAsFloat(pop(args));
            break;
          }
          
          
          if (replacement != null) {
            msg.replace(startPos, startPos + 2, replacement);
            startPos += replacement.length();
          }
          else {
            startPos++;
          }
        }
        startPos = msg.indexOf("%", startPos);
      }
      sb.append(msg);
    }
    
    for (Object o : args) {
      if (sb.length() != 0) {
        sb.append(' ');
      }
      sb.append(formatter.printObject(o));
    }
    return sb.toString();
  }
  





  private static Object pop(List<Object> list)
  {
    return list.isEmpty() ? null : list.remove(0);
  }
  
  private static class DefaultFormatter
    implements WebConsole.Formatter, Serializable
  {
    private DefaultFormatter() {}
    
    public String printObject(Object o)
    {
      return parameterAsString(o);
    }
    
    public String parameterAsString(Object o)
    {
      if (o != null) {
        return o.toString();
      }
      return "null";
    }
    
    public String parameterAsInteger(Object o)
    {
      if ((o instanceof Number)) {
        return Integer.toString(((Number)o).intValue());
      }
      if ((o instanceof String)) {
        try {
          return Integer.toString(Integer.parseInt((String)o));
        }
        catch (NumberFormatException localNumberFormatException) {}
      }
      

      return "NaN";
    }
    
    public String parameterAsFloat(Object o)
    {
      if ((o instanceof Number)) {
        return Float.toString(((Number)o).floatValue());
      }
      if ((o instanceof String)) {
        try {
          return Float.toString(Float.parseFloat((String)o));
        }
        catch (NumberFormatException localNumberFormatException) {}
      }
      

      return "NaN";
    }
  }
  


  private static class DefaultLogger
    implements WebConsole.Logger, Serializable
  {
    private static final Log LOG = LogFactory.getLog(WebConsole.class);
    
    private DefaultLogger() {}
    
    public boolean isTraceEnabled() { return LOG.isTraceEnabled(); }
    

    public void trace(Object message)
    {
      if (LOG.isTraceEnabled()) {
        LOG.trace(message);
      }
    }
    
    public boolean isDebugEnabled()
    {
      return LOG.isDebugEnabled();
    }
    
    public void debug(Object message)
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug(message);
      }
    }
    
    public boolean isInfoEnabled()
    {
      return LOG.isInfoEnabled();
    }
    
    public void info(Object message)
    {
      LOG.info(message);
    }
    
    public boolean isWarnEnabled()
    {
      return LOG.isWarnEnabled();
    }
    
    public void warn(Object message)
    {
      LOG.warn(message);
    }
    
    public boolean isErrorEnabled()
    {
      return LOG.isErrorEnabled();
    }
    
    public void error(Object message)
    {
      LOG.error(message);
    }
  }
  
  public static abstract interface Formatter
  {
    public abstract String printObject(Object paramObject);
    
    public abstract String parameterAsString(Object paramObject);
    
    public abstract String parameterAsInteger(Object paramObject);
    
    public abstract String parameterAsFloat(Object paramObject);
  }
  
  public static abstract interface Logger
  {
    public abstract boolean isTraceEnabled();
    
    public abstract void trace(Object paramObject);
    
    public abstract boolean isDebugEnabled();
    
    public abstract void debug(Object paramObject);
    
    public abstract boolean isInfoEnabled();
    
    public abstract void info(Object paramObject);
    
    public abstract boolean isWarnEnabled();
    
    public abstract void warn(Object paramObject);
    
    public abstract boolean isErrorEnabled();
    
    public abstract void error(Object paramObject);
  }
}
