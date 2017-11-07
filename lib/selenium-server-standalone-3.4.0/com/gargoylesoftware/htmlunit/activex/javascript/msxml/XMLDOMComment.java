package com.gargoylesoftware.htmlunit.activex.javascript.msxml;

import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;




























@JsxClass(domClass=DomComment.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public final class XMLDOMComment
  extends XMLDOMCharacterData
{
  public XMLDOMComment() {}
  
  public String getText()
  {
    return (String)getData();
  }
}
