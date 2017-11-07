package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.xml.XmlPage;



































@JsxClass(domClass=HtmlUnknownElement.class)
public class HTMLUnknownElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLUnknownElement() {}
  
  public String getNodeName()
  {
    HtmlElement elem = getDomNodeOrDie();
    Page page = elem.getPage();
    if ((page instanceof XmlPage)) {
      return elem.getLocalName();
    }
    return super.getNodeName();
  }
  



  public String getClassName()
  {
    if (getWindow().getWebWindow() != null) {
      HtmlElement element = getDomNodeOrNull();
      if (element != null) {
        String name = element.getNodeName();
        if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_HTML_RUBY_ELEMENT_CLASS_NAME)) && (
          ("rp".equals(name)) || 
          ("rt".equals(name)) || 
          ("ruby".equals(name)) || 
          ("rb".equals(name)) || 
          ("rtc".equals(name)))) {
          return "HTMLElement";
        }
        
        if ((name.indexOf('-') != -1) && 
          (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_HTML_HYPHEN_ELEMENT_CLASS_NAME))) {
          return "HTMLElement";
        }
      }
    }
    return super.getClassName();
  }
  



  protected boolean isLowerCaseInOuterHtml()
  {
    return true;
  }
  



  protected boolean isEndTagForbidden()
  {
    if ("IMAGE".equals(getNodeName())) {
      return true;
    }
    return super.isEndTagForbidden();
  }
}
