package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConsole;
import com.gargoylesoftware.htmlunit.WebConsole.Formatter;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Delegator;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;


























@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})})
public class Console
  extends SimpleScriptable
{
  private static final Map<String, Long> TIMERS = new HashMap();
  private static WebConsole.Formatter FORMATTER_ = new ConsoleFormatter(null);
  


  private WebWindow webWindow_;
  



  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Console() {}
  


  public void setWebWindow(WebWindow webWindow)
  {
    webWindow_ = webWindow;
  }
  







  @JsxFunction
  public static void log(Context cx, Scriptable thisObj, Object[] args, Function funObj)
  {
    WebConsole webConsole = toWebConsole(thisObj);
    WebConsole.Formatter oldFormatter = webConsole.getFormatter();
    webConsole.setFormatter(FORMATTER_);
    webConsole.info(args);
    webConsole.setFormatter(oldFormatter);
  }
  
  private static WebConsole toWebConsole(Scriptable thisObj) {
    if (((thisObj instanceof Window)) && 
      (((SimpleScriptable)thisObj).getDomNodeOrDie().hasFeature(BrowserVersionFeatures.JS_CONSOLE_HANDLE_WINDOW))) {
      thisObj = ((Window)thisObj).getConsole();
    }
    if ((thisObj instanceof Console)) {
      return ((Console)thisObj).getWebConsole();
    }
    throw Context.reportRuntimeError("TypeError: object does not implemennt interface Console");
  }
  







  @JsxFunction
  public static void info(Context cx, Scriptable thisObj, Object[] args, Function funObj)
  {
    WebConsole webConsole = toWebConsole(thisObj);
    WebConsole.Formatter oldFormatter = webConsole.getFormatter();
    webConsole.setFormatter(FORMATTER_);
    webConsole.info(args);
    webConsole.setFormatter(oldFormatter);
  }
  







  @JsxFunction
  public static void warn(Context cx, Scriptable thisObj, Object[] args, Function funObj)
  {
    WebConsole webConsole = toWebConsole(thisObj);
    WebConsole.Formatter oldFormatter = webConsole.getFormatter();
    webConsole.setFormatter(FORMATTER_);
    webConsole.warn(args);
    webConsole.setFormatter(oldFormatter);
  }
  







  @JsxFunction
  public static void error(Context cx, Scriptable thisObj, Object[] args, Function funObj)
  {
    WebConsole webConsole = toWebConsole(thisObj);
    WebConsole.Formatter oldFormatter = webConsole.getFormatter();
    webConsole.setFormatter(FORMATTER_);
    webConsole.error(args);
    webConsole.setFormatter(oldFormatter);
  }
  







  @JsxFunction
  public static void debug(Context cx, Scriptable thisObj, Object[] args, Function funObj)
  {
    WebConsole webConsole = toWebConsole(thisObj);
    WebConsole.Formatter oldFormatter = webConsole.getFormatter();
    webConsole.setFormatter(FORMATTER_);
    webConsole.debug(args);
    webConsole.setFormatter(oldFormatter);
  }
  







  @JsxFunction
  public static void trace(Context cx, Scriptable thisObj, Object[] args, Function funObj)
  {
    WebConsole webConsole = toWebConsole(thisObj);
    WebConsole.Formatter oldFormatter = webConsole.getFormatter();
    webConsole.setFormatter(FORMATTER_);
    webConsole.trace(args);
    webConsole.setFormatter(oldFormatter);
  }
  
  private WebConsole getWebConsole() {
    return webWindow_.getWebClient().getWebConsole();
  }
  




  @JsxFunction
  public void dir(Object o)
  {
    if ((o instanceof ScriptableObject)) {
      ScriptableObject obj = (ScriptableObject)o;
      Object[] ids = obj.getIds();
      if ((ids != null) && (ids.length > 0)) {
        StringBuilder sb = new StringBuilder();
        for (Object id : ids) {
          Object value = obj.get(id);
          if ((value instanceof Delegator)) {
            sb.append(id + ": " + ((Delegator)value).getClassName() + "\n");
          }
          else if ((value instanceof SimpleScriptable)) {
            sb.append(id + ": " + ((SimpleScriptable)value).getClassName() + "\n");
          }
          else if ((value instanceof BaseFunction)) {
            sb.append(id + ": function " + ((BaseFunction)value).getFunctionName() + "()\n");
          }
          else {
            sb.append(id + ": " + value + "\n");
          }
        }
        getWebConsole().info(new Object[] { sb.toString() });
      }
    }
  }
  





  @JsxFunction
  public void group() {}
  





  @JsxFunction
  public void groupEnd() {}
  





  @JsxFunction
  public void groupCollapsed() {}
  





  @JsxFunction
  public void time(String timerName)
  {
    if (!TIMERS.containsKey(timerName)) {
      TIMERS.put(timerName, Long.valueOf(System.currentTimeMillis()));
    }
    getWebConsole().info(new Object[] { timerName + ": timer started" });
  }
  




  @JsxFunction
  public void timeEnd(String timerName)
  {
    Long startTime = (Long)TIMERS.remove(timerName);
    if (startTime != null) {
      getWebConsole().info(new Object[] { timerName + ": " + (System.currentTimeMillis() - startTime.longValue()) + "ms" });
    }
  }
  


  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void timeStamp(String label) {}
  

  private static class ConsoleFormatter
    implements WebConsole.Formatter
  {
    private ConsoleFormatter() {}
    

    private static void appendNativeArray(NativeArray a, StringBuilder sb, int level)
    {
      sb.append("[");
      if (level < 3) {
        for (int i = 0; i < a.size(); i++) {
          if (i > 0) {
            sb.append(", ");
          }
          Object val = a.get(i);
          if (val != null) {
            appendValue(val, sb, level + 1);
          }
        }
      }
      sb.append("]");
    }
    
    private static void appendNativeObject(NativeObject obj, StringBuilder sb, int level) {
      if (level == 0)
      {



        sb.append("(");
      }
      sb.append("{");
      if (level < 3) {
        Object[] ids = obj.getIds();
        if ((ids != null) && (ids.length > 0)) {
          boolean needsSeparator = false;
          for (Object key : ids) {
            if (needsSeparator) {
              sb.append(", ");
            }
            sb.append(key);
            sb.append(":");
            appendValue(obj.get(key), sb, level + 1);
            needsSeparator = true;
          }
        }
      }
      sb.append("}");
      if (level == 0) {
        sb.append(")");
      }
    }
    














    private static void appendValue(Object val, StringBuilder sb, int level)
    {
      if ((val instanceof NativeFunction)) {
        sb.append("(");
        
        Pattern p = Pattern.compile("[ \\t]*\\r?\\n[ \\t]*", 
          8);
        Matcher m = p.matcher(((NativeFunction)val).toString());
        sb.append(m.replaceAll(" ").trim());
        sb.append(")");
      }
      else if ((val instanceof BaseFunction)) {
        sb.append("function ");
        sb.append(((BaseFunction)val).getFunctionName());
        sb.append("() {[native code]}");
      }
      else if ((val instanceof NativeObject)) {
        appendNativeObject((NativeObject)val, sb, level);
      }
      else if ((val instanceof NativeArray)) {
        appendNativeArray((NativeArray)val, sb, level);
      }
      else if ((val instanceof Delegator)) {
        if (level == 0) {
          sb.append("[object ");
          sb.append(((Delegator)val).getDelegee().getClassName());
          sb.append("]");
        }
        else {
          sb.append("({})");
        }
      }
      else if ((val instanceof SimpleScriptable)) {
        if (level == 0) {
          sb.append("[object ");
          sb.append(((SimpleScriptable)val).getClassName());
          sb.append("]");
        }
        else {
          sb.append("({})");
        }
      }
      else if ((val instanceof String)) {
        if (level == 0) {
          sb.append((String)val);

        }
        else
        {

          sb.append(quote((String)val));
        }
      }
      else if ((val instanceof Number)) {
        sb.append(((Number)val).toString());
      }
      else
      {
        sb.append(val);
      }
    }
    




    private static String quote(CharSequence s)
    {
      StringBuilder sb = new StringBuilder();
      sb.append("\"");
      for (int i = 0; i < s.length(); i++) {
        char ch = s.charAt(i);
        switch (ch) {
        case '\\': 
          sb.append("\\\\");
          break;
        case '"': 
          sb.append("\\\"");
          break;
        case '\b': 
          sb.append("\\b");
          break;
        case '\t': 
          sb.append("\\t");
          break;
        case '\n': 
          sb.append("\\n");
          break;
        case '\f': 
          sb.append("\\f");
          break;
        case '\r': 
          sb.append("\\r");
          break;
        default: 
          if ((ch < ' ') || (ch > '~')) {
            sb.append("\\u" + Integer.toHexString(ch).toUpperCase(Locale.ROOT));
          }
          else
            sb.append(ch);
          break;
        }
      }
      sb.append("\"");
      return sb.toString();
    }
    
    private static String formatToString(Object o) {
      if (o == null) {
        return "null";
      }
      if ((o instanceof NativeFunction)) {
        return ((NativeFunction)o).toString();
      }
      if ((o instanceof BaseFunction)) {
        return 
          "function " + ((BaseFunction)o).getFunctionName() + "\n" + "    [native code]\n" + "}";
      }
      if ((o instanceof NativeArray))
      {

        return "[object Object]";
      }
      if ((o instanceof Delegator)) {
        return 
          "[object " + ((Delegator)o).getDelegee().getClassName() + "]";
      }
      if ((o instanceof NativeObject)) {
        return "[object " + ((NativeObject)o).getClassName() + "]";
      }
      if ((o instanceof SimpleScriptable)) {
        return "[object " + ((SimpleScriptable)o).getClassName() + "]";
      }
      
      return o.toString();
    }
    

    public String printObject(Object o)
    {
      StringBuilder sb = new StringBuilder();
      appendValue(o, sb, 0);
      return sb.toString();
    }
    
    public String parameterAsString(Object o)
    {
      if ((o instanceof NativeArray)) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ((NativeArray)o).size(); i++) {
          if (i > 0) {
            sb.append(",");
          }
          sb.append(formatToString(((NativeArray)o).get(i)));
        }
        return sb.toString();
      }
      return formatToString(o);
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
}
