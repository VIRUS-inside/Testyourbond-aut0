package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;






















public class EventHandler
  extends BaseFunction
{
  private final DomNode node_;
  private final String eventName_;
  private final String jsSnippet_;
  private Function realFunction_;
  
  public EventHandler(DomNode node, String eventName, String jsSnippet)
  {
    node_ = node;
    eventName_ = eventName;
    
    jsSnippet_ = ("function " + eventName + "(event) {" + jsSnippet + "\n}");
    
    ScriptableObject w = node.getPage().getEnclosingWindow().getScriptableObject();
    Scriptable function = (Scriptable)w.get("Function", w);
    setPrototype(function.getPrototype());
  }
  






  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
    throws JavaScriptException
  {
    SimpleScriptable jsObj = (SimpleScriptable)node_.getScriptableObject();
    
    if (realFunction_ == null) {
      realFunction_ = cx.compileFunction(jsObj, jsSnippet_, eventName_ + " event for " + node_ + 
        " in " + node_.getPage().getUrl(), 0, null);
      realFunction_.setParentScope(jsObj);
    }
    
    Object result = realFunction_.call(cx, scope, thisObj, args);
    return result;
  }
  





  public Object getDefaultValue(Class<?> typeHint)
  {
    return jsSnippet_;
  }
  




  public Object get(String name, Scriptable start)
  {
    if ("toString".equals(name)) {
      new BaseFunction()
      {
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
        {
          return jsSnippet_;
        }
      };
    }
    
    return super.get(name, start);
  }
}
