package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;





































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlAddress.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlBlockQuote.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlCenter.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlExample.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlKeygen.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlListing.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlPlainText.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})})
public class HTMLBlockElement
  extends HTMLElement
{
  public HTMLBlockElement() {}
  
  public void setDomNode(DomNode domNode)
  {
    super.setDomNode(domNode);
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_XML_SUPPORT_VIA_ACTIVEXOBJECT)) {
      ActiveXObject.addProperty(this, "cite", true, true);
    }
  }
  



  public String getCite()
  {
    return getDomNodeOrDie().getAttribute("cite");
  }
  



  public void setCite(String cite)
  {
    getDomNodeOrDie().setAttribute("cite", cite);
  }
  



  public String getDateTime()
  {
    String dateTime = getDomNodeOrDie().getAttribute("datetime");
    return dateTime;
  }
  



  public void setDateTime(String dateTime)
  {
    getDomNodeOrDie().setAttribute("datetime", dateTime);
  }
  





  protected boolean isEndTagForbidden()
  {
    if (("KEYGEN".equals(getNodeName())) && (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLKEYGEN_END_TAG_FORBIDDEN))) {
      return true;
    }
    return false;
  }
  



  @JsxGetter
  public String getClear()
  {
    return getDomNodeOrDie().getAttribute("clear");
  }
  



  @JsxSetter
  public void setClear(String clear)
  {
    getDomNodeOrDie().setAttribute("clear", clear);
  }
  



  @JsxGetter(propertyName="width")
  public String getWidth_js()
  {
    return getWidthOrHeight("width", Boolean.TRUE);
  }
  



  @JsxSetter
  public void setWidth(String width)
  {
    setWidthOrHeight("width", width, true);
  }
}
