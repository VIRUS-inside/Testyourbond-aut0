package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlIsIndex;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;























@JsxClass(domClass=HtmlIsIndex.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class HTMLIsIndexElement
  extends HTMLElement
{
  public HTMLIsIndexElement() {}
  
  protected boolean isEndTagForbidden()
  {
    return true;
  }
}
