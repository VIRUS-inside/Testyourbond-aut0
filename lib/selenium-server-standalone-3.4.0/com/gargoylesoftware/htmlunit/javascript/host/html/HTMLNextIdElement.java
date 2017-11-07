package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlNextId;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;























@JsxClass(domClass=HtmlNextId.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class HTMLNextIdElement
  extends HTMLElement
{
  public HTMLNextIdElement() {}
  
  protected boolean isEndTagForbidden()
  {
    return true;
  }
}
