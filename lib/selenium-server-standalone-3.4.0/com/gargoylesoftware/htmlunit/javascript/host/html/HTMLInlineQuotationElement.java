package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineQuotation;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
























@JsxClass(domClass=HtmlInlineQuotation.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class HTMLInlineQuotationElement
  extends HTMLElement
{
  public HTMLInlineQuotationElement() {}
  
  public String getClassName()
  {
    return "HTMLQuoteElement";
  }
  



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
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getDateTime()
  {
    String cite = getDomNodeOrDie().getAttribute("datetime");
    return cite;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setDateTime(String dateTime)
  {
    getDomNodeOrDie().setAttribute("datetime", dateTime);
  }
}
