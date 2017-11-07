package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import net.sourceforge.htmlunit.corejs.javascript.Context;






























@JsxClass(domClass=HtmlHtml.class)
public class HTMLHtmlElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLHtmlElement() {}
  
  public Object getParentNode()
  {
    return getWindow().getDocument_js();
  }
  

  public int getClientWidth()
  {
    return getWindow().getInnerWidth();
  }
  

  public int getClientHeight()
  {
    return getWindow().getInnerHeight();
  }
  




  public void setOuterHTML(Object value)
  {
    throw Context.reportRuntimeError("outerHTML is read-only for tag 'html'");
  }
  




  protected void setInnerTextImpl(String value)
  {
    throw Context.reportRuntimeError("innerText is read-only for tag 'html'");
  }
  



  @JsxGetter
  public String getVersion()
  {
    return getDomNodeOrDie().getAttribute("version");
  }
  



  @JsxSetter
  public void setVersion(String version)
  {
    getDomNodeOrDie().setAttribute("version", version);
  }
}
