package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import java.net.MalformedURLException;
import java.net.URL;
import net.sourceforge.htmlunit.corejs.javascript.Context;



































@JsxClass(domClass=HtmlLink.class)
public class HTMLLinkElement
  extends HTMLElement
{
  private CSSStyleSheet sheet_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLLinkElement() {}
  
  @JsxSetter
  public void setHref(String href)
  {
    getDomNodeOrDie().setAttribute("href", href);
  }
  



  @JsxGetter
  public String getHref()
  {
    HtmlLink link = (HtmlLink)getDomNodeOrDie();
    String href = link.getHrefAttribute();
    if (href.isEmpty()) {
      return href;
    }
    try {
      return ((HtmlPage)link.getPage()).getFullyQualifiedUrl(href).toString();
    }
    catch (MalformedURLException e) {}
    return href;
  }
  




  @JsxSetter
  public void setRel(String rel)
  {
    getDomNodeOrDie().setAttribute("rel", rel);
  }
  



  @JsxGetter
  public String getRel()
  {
    return ((HtmlLink)getDomNodeOrDie()).getRelAttribute();
  }
  



  @JsxSetter
  public void setRev(String rel)
  {
    getDomNodeOrDie().setAttribute("rev", rel);
  }
  



  @JsxGetter
  public String getRev()
  {
    return ((HtmlLink)getDomNodeOrDie()).getRevAttribute();
  }
  



  @JsxSetter
  public void setType(String type)
  {
    getDomNodeOrDie().setAttribute("type", type);
  }
  



  @JsxGetter
  public String getType()
  {
    return ((HtmlLink)getDomNodeOrDie()).getTypeAttribute();
  }
  




  public CSSStyleSheet getSheet()
  {
    if (sheet_ == null) {
      sheet_ = CSSStyleSheet.loadStylesheet(getWindow(), this, (HtmlLink)getDomNodeOrDie(), null);
    }
    return sheet_;
  }
  



  protected boolean isEndTagForbidden()
  {
    return true;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public DOMTokenList getRelList()
  {
    throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
  }
}
