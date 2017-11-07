package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlContent;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass(domClass=HtmlContent.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(value=com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF, maxVersion=45)})
public class HTMLContentElement
  extends HTMLElement
{
  @JsxConstructor
  public HTMLContentElement() {}
}
