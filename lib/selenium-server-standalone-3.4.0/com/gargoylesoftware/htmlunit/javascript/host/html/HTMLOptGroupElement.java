package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOptionGroup;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;































@JsxClass(domClass=HtmlOptionGroup.class)
public class HTMLOptGroupElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLOptGroupElement() {}
  
  @JsxGetter
  public boolean getDisabled()
  {
    return super.getDisabled();
  }
  



  @JsxSetter
  public void setDisabled(boolean disabled)
  {
    super.setDisabled(disabled);
  }
  



  @JsxGetter
  public String getLabel()
  {
    String label = getDomNodeOrDie().getAttribute("label");
    if (DomElement.ATTRIBUTE_NOT_DEFINED == label) {
      return "";
    }
    return label;
  }
  



  @JsxSetter
  public void setLabel(String newLabel)
  {
    getDomNodeOrDie().setAttribute("label", newLabel);
  }
}
