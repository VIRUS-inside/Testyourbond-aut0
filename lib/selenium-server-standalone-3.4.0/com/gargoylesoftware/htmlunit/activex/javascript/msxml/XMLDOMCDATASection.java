package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;

@JsxClass(domClass=DomCDataSection.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public final class XMLDOMCDATASection
  extends XMLDOMText
{
  public XMLDOMCDATASection() {}
}
