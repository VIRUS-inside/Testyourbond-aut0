package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlParameter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;































@JsxClass(domClass=HtmlParameter.class)
public class HTMLParamElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLParamElement() {}
  
  @JsxGetter
  public String getName()
  {
    return getDomNodeOrDie().getAttribute("name");
  }
  



  @JsxGetter
  public String getValue()
  {
    return getDomNodeOrDie().getAttribute("value");
  }
  



  @JsxGetter
  public String getType()
  {
    return getDomNodeOrDie().getAttribute("type");
  }
  



  @JsxGetter
  public String getValueType()
  {
    return getDomNodeOrDie().getAttribute("valuetype");
  }
  



  protected boolean isEndTagForbidden()
  {
    return true;
  }
}
