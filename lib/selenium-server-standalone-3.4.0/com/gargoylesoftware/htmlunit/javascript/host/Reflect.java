package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;


























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class Reflect
  extends SimpleScriptable
{
  public Reflect() {}
  
  public void defineProperties()
  {
    setClassName("Object");
  }
  



  public void setParentScope(Scriptable scope)
  {
    super.setParentScope(scope);
    try {
      FunctionObject functionObject = new FunctionObject("has", 
        getClass().getDeclaredMethod("has", new Class[] { Scriptable.class, String.class }), scope);
      defineProperty("has", functionObject, 2);
    }
    catch (Exception e) {
      Context.throwAsScriptRuntimeEx(e);
    }
  }
  






  @JsxStaticFunction
  public boolean has(Scriptable target, String propertyKey)
  {
    return ScriptableObject.hasProperty(target, propertyKey);
  }
}
