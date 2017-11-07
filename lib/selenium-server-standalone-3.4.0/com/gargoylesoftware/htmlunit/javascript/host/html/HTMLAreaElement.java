package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import net.sourceforge.htmlunit.corejs.javascript.Context;




































@JsxClass(domClass=HtmlArea.class)
public class HTMLAreaElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLAreaElement() {}
  
  public Object getDefaultValue(Class<?> hint)
  {
    HtmlElement element = getDomNodeOrNull();
    if (element == null) {
      return super.getDefaultValue(null);
    }
    return HTMLAnchorElement.getDefaultValue(element);
  }
  



  @JsxGetter
  public String getAlt()
  {
    return getDomNodeOrDie().getAttribute("alt");
  }
  



  @JsxSetter
  public void setAlt(String alt)
  {
    getDomNodeOrDie().setAttribute("alt", alt);
  }
  



  protected boolean isEndTagForbidden()
  {
    return true;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public DOMTokenList getRelList()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
  




  public void focus()
  {
    HtmlArea area = (HtmlArea)getDomNodeOrDie();
    String hrefAttr = area.getHrefAttribute();
    
    if (hrefAttr != DomElement.ATTRIBUTE_NOT_DEFINED) {
      area.focus();
    }
  }
}
