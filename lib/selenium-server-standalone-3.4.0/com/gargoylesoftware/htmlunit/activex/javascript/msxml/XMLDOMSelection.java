package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;



































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class XMLDOMSelection
  extends XMLDOMNodeList
{
  public XMLDOMSelection() {}
  
  public XMLDOMSelection(DomNode parentScope, boolean attributeChangeSensitive, String description)
  {
    super(parentScope, attributeChangeSensitive, description);
  }
}
