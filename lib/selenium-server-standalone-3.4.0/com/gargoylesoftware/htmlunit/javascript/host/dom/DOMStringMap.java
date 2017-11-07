package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
































@JsxClass
public final class DOMStringMap
  extends SimpleScriptable
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public DOMStringMap() {}
  
  public DOMStringMap(Node node)
  {
    setDomNode(node.getDomNodeOrDie(), false);
    setParentScope(node.getParentScope());
    setPrototype(getPrototype(getClass()));
  }
  



  public Object get(String name, Scriptable start)
  {
    HtmlElement e = (HtmlElement)getDomNodeOrNull();
    if (e != null) {
      String value = e.getAttribute("data-" + decamelize(name));
      if (value != DomElement.ATTRIBUTE_NOT_DEFINED) {
        return value;
      }
    }
    return NOT_FOUND;
  }
  



  public void put(String name, Scriptable start, Object value)
  {
    if ((!(ScriptableObject.getTopLevelScope(this) instanceof Window)) || (getWindow().getWebWindow() == null)) {
      super.put(name, start, value);
    }
    else {
      HtmlElement e = (HtmlElement)getDomNodeOrNull();
      if (e != null) {
        e.setAttribute("data-" + decamelize(name), Context.toString(value));
      }
    }
  }
  







  public static String decamelize(String string)
  {
    if ((string == null) || (string.isEmpty())) {
      return string;
    }
    
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < string.length(); i++) {
      char ch = string.charAt(i);
      if (Character.isUpperCase(ch)) {
        builder.append('-').append(Character.toLowerCase(ch));
      }
      else {
        builder.append(ch);
      }
    }
    return builder.toString();
  }
}
