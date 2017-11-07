package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLActiveXObjectFactory;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class ActiveXObject
  extends SimpleScriptable
{
  private static final Log LOG = LogFactory.getLog(ActiveXObject.class);
  











  public ActiveXObject() {}
  










  @JsxConstructor
  public static Scriptable jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr)
  {
    if ((args.length < 1) || (args.length > 2)) {
      throw Context.reportRuntimeError(
        "ActiveXObject Error: constructor must have one or two String parameters.");
    }
    if (args[0] == Undefined.instance) {
      throw Context.reportRuntimeError("ActiveXObject Error: constructor parameter is undefined.");
    }
    if (!(args[0] instanceof String)) {
      throw Context.reportRuntimeError("ActiveXObject Error: constructor parameter must be a String.");
    }
    String activeXName = (String)args[0];
    
    WebWindow window = getWindow(ctorObj).getWebWindow();
    MSXMLActiveXObjectFactory factory = window.getWebClient().getMSXMLActiveXObjectFactory();
    if (factory.supports(activeXName)) {
      Scriptable scriptable = factory.create(activeXName, window);
      if (scriptable != null) {
        return scriptable;
      }
    }
    
    WebClient webClient = getWindow(ctorObj).getWebWindow().getWebClient();
    Map<String, String> map = webClient.getActiveXObjectMap();
    if (map != null) {
      String xClassString = (String)map.get(activeXName);
      if (xClassString != null) {
        try {
          Class<?> xClass = Class.forName(xClassString);
          Object object = xClass.newInstance();
          return Context.toObject(object, ctorObj);
        }
        catch (Exception e) {
          throw Context.reportRuntimeError("ActiveXObject Error: failed instantiating class " + xClassString + 
            " because " + e.getMessage() + ".");
        }
      }
    }
    if ((webClient.getOptions().isActiveXNative()) && (System.getProperty("os.name").contains("Windows"))) {
      try {
        return new ActiveXObjectImpl(activeXName);
      }
      catch (Exception e) {
        LOG.warn("Error initiating Jacob", e);
      }
    }
    
    LOG.warn("Automation server can't create object for '" + activeXName + "'.");
    throw Context.reportRuntimeError("Automation server can't create object for '" + activeXName + "'.");
  }
  







  public static void addProperty(SimpleScriptable scriptable, String propertyName, boolean isGetter, boolean isSetter)
  {
    String initialUpper = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
    String getterName = null;
    if (isGetter) {
      getterName = "get" + initialUpper;
    }
    String setterName = null;
    if (isSetter) {
      setterName = "set" + initialUpper;
    }
    addProperty(scriptable, propertyName, getterName, setterName);
  }
  
  static void addProperty(SimpleScriptable scriptable, String propertyName, String getterMethodName, String setterMethodName)
  {
    scriptable.defineProperty(propertyName, null, 
      getMethod(scriptable.getClass(), getterMethodName, JsxGetter.class), 
      getMethod(scriptable.getClass(), setterMethodName, JsxSetter.class), 4);
  }
  








  static Method getMethod(Class<? extends SimpleScriptable> clazz, String name, Class<? extends Annotation> annotationClass)
  {
    if (name == null) {
      return null;
    }
    
    Method foundMethod = null;
    int foundByNameOnlyCount = 0;
    for (Method method : clazz.getMethods()) {
      if (method.getName().equals(name)) {
        if (method.getAnnotation(annotationClass) != null) {
          return method;
        }
        foundByNameOnlyCount++;
        foundMethod = method;
      }
    }
    if (foundByNameOnlyCount > 1) {
      throw new IllegalArgumentException("Found " + foundByNameOnlyCount + " methods for name '" + 
        name + "' in class '" + clazz + "'.");
    }
    return foundMethod;
  }
}
