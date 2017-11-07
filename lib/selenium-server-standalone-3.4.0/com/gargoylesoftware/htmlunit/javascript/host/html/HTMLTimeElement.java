package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTime;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;





























@JsxClass(domClass=HtmlTime.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class HTMLTimeElement
  extends HTMLElement
{
  @JsxConstructor
  public HTMLTimeElement() {}
  
  @JsxGetter
  public String getDateTime()
  {
    return getDomNodeOrDie().getAttribute("dateTime");
  }
  



  @JsxSetter
  public void setDateTime(String dateTime)
  {
    getDomNodeOrDie().setAttribute("dateTime", dateTime);
  }
}
