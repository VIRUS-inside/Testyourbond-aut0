package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;

























public class XHtmlPage
  extends HtmlPage
{
  public XHtmlPage(WebResponse webResponse, WebWindow webWindow)
  {
    super(webResponse, webWindow);
  }
  



  public boolean hasCaseSensitiveTagNames()
  {
    return true;
  }
}
