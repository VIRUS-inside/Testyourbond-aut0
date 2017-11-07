package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;


































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlDeletedText.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlInsertedText.class)})
public class HTMLModElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLModElement() {}
  
  @JsxGetter
  public String getCite()
  {
    String cite = getDomNodeOrDie().getAttribute("cite");
    return cite;
  }
  



  @JsxSetter
  public void setCite(String cite)
  {
    getDomNodeOrDie().setAttribute("cite", cite);
  }
  



  @JsxGetter
  public String getDateTime()
  {
    String cite = getDomNodeOrDie().getAttribute("datetime");
    return cite;
  }
  



  @JsxSetter
  public void setDateTime(String dateTime)
  {
    getDomNodeOrDie().setAttribute("datetime", dateTime);
  }
}
