package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.EcmaError;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.IdFunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




































public class DebugFrameImpl
  extends DebugFrameAdapter
{
  private static final Log LOG = LogFactory.getLog(DebugFrameImpl.class);
  

  private static final String KEY_LAST_LINE = "DebugFrameImpl#line";
  

  private static final String KEY_LAST_SOURCE = "DebugFrameImpl#source";
  
  private final DebuggableScript functionOrScript_;
  

  public DebugFrameImpl(DebuggableScript functionOrScript)
  {
    functionOrScript_ = functionOrScript;
  }
  



  public void onEnter(Context cx, Scriptable activation, Scriptable thisObj, Object[] args)
  {
    if (LOG.isTraceEnabled()) {
      StringBuilder sb = new StringBuilder();
      
      String line = getFirstLine(cx);
      String source = getSourceName(cx);
      sb.append(source).append(":").append(line).append(" ");
      
      Scriptable parent = activation.getParentScope();
      while (parent != null) {
        sb.append("   ");
        parent = parent.getParentScope();
      }
      String functionName = getFunctionName(thisObj);
      sb.append(functionName).append("(");
      int nbParams = functionOrScript_.getParamCount();
      for (int i = 0; i < nbParams; i++) { String argAsString;
        String argAsString;
        if (i < args.length) {
          argAsString = stringValue(args[i]);
        }
        else {
          argAsString = "undefined";
        }
        sb.append(getParamName(i)).append(": ").append(argAsString);
        if (i < nbParams - 1) {
          sb.append(", ");
        }
      }
      sb.append(")");
      
      LOG.trace(sb);
    }
  }
  
  private static String stringValue(Object arg) {
    if ((arg instanceof NativeFunction))
    {

      String name = (String)StringUtils.defaultIfEmpty(((NativeFunction)arg).getFunctionName(), "anonymous");
      return "[function " + name + "]";
    }
    if ((arg instanceof IdFunctionObject)) {
      return "[function " + ((IdFunctionObject)arg).getFunctionName() + "]";
    }
    if ((arg instanceof Function)) {
      return "[function anonymous]";
    }
    String asString = null;
    try
    {
      asString = Context.toString(arg);
      if ((arg instanceof Event)) {
        asString = asString + "<" + ((Event)arg).getType() + ">";
      }
    }
    catch (Throwable e)
    {
      asString = String.valueOf(arg);
    }
    return asString;
  }
  



  public void onExceptionThrown(Context cx, Throwable t)
  {
    if (LOG.isTraceEnabled()) {
      if ((t instanceof JavaScriptException)) {
        JavaScriptException e = (JavaScriptException)t;
        LOG.trace(getSourceName(cx) + ":" + getFirstLine(cx) + 
          " Exception thrown: " + Context.toString(e.details()));
      }
      else if ((t instanceof EcmaError)) {
        EcmaError e = (EcmaError)t;
        LOG.trace(getSourceName(cx) + ":" + getFirstLine(cx) + 
          " Exception thrown: " + Context.toString(e.details()));
      }
      else {
        LOG.trace(getSourceName(cx) + ":" + getFirstLine(cx) + " Exception thrown: " + t.getCause());
      }
    }
  }
  



  public void onLineChange(Context cx, int lineNumber)
  {
    cx.putThreadLocal("DebugFrameImpl#line", Integer.valueOf(lineNumber));
    cx.putThreadLocal("DebugFrameImpl#source", functionOrScript_.getSourceName());
  }
  










  private String getFunctionName(Scriptable thisObj)
  {
    if (functionOrScript_.isFunction()) {
      String name = functionOrScript_.getFunctionName();
      if ((name != null) && (!name.isEmpty()))
      {
        return name;
      }
      




      if ((thisObj instanceof SimpleScriptable)) {
        return "[anonymous]";
      }
      
      Scriptable obj = thisObj;
      while (obj != null) {
        for (Object id : obj.getIds()) {
          if ((id instanceof String)) {
            String s = (String)id;
            if ((obj instanceof ScriptableObject)) {
              Object o = ((ScriptableObject)obj).getGetterOrSetter(s, 0, false);
              if (o == null) {
                o = ((ScriptableObject)obj).getGetterOrSetter(s, 0, true);
                if ((o != null) && ((o instanceof Callable))) {
                  return "__defineSetter__ " + s;
                }
              }
              else if ((o instanceof Callable)) {
                return "__defineGetter__ " + s;
              }
            }
            
            try
            {
              o = obj.get(s, obj);
            } catch (Exception e) {
              Object o;
              return "[anonymous]"; }
            Object o;
            if ((o instanceof NativeFunction)) {
              NativeFunction f = (NativeFunction)o;
              if (f.getDebuggableView() == functionOrScript_) {
                return s;
              }
            }
          }
        }
        obj = obj.getPrototype();
      }
      
      return "[anonymous]";
    }
    
    return "[script]";
  }
  






  private String getParamName(int index)
  {
    if ((index >= 0) && (functionOrScript_.getParamCount() > index)) {
      return functionOrScript_.getParamOrVarName(index);
    }
    return "???";
  }
  




  private static String getSourceName(Context cx)
  {
    String source = (String)cx.getThreadLocal("DebugFrameImpl#source");
    if (source == null) {
      return "unknown";
    }
    
    source = StringUtils.substringAfterLast(source, "/");
    
    source = StringUtils.substringBefore(source, " ");
    return source;
  }
  






  private static String getFirstLine(Context cx)
  {
    Object line = cx.getThreadLocal("DebugFrameImpl#line");
    String result;
    String result; if (line == null) {
      result = "??";
    }
    else {
      result = String.valueOf(line);
    }
    return StringUtils.leftPad(result, 5);
  }
}
