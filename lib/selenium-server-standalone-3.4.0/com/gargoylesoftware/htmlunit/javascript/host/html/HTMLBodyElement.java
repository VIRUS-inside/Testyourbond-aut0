package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import java.util.Locale;





































@JsxClass(domClass=HtmlBody.class)
public class HTMLBodyElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLBodyElement() {}
  
  public void createEventHandlerFromAttribute(String attributeName, String value)
  {
    if (attributeName.toLowerCase(Locale.ROOT).startsWith("on")) {
      createEventHandler(attributeName, value);
    }
  }
  



  protected boolean isEventHandlerOnWindow()
  {
    return true;
  }
  



  public void setDefaults(ComputedCSSStyleDeclaration style)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_BODY_MARGINS_8)) {
      style.setDefaultLocalStyleAttribute("margin", "8px");
      style.setDefaultLocalStyleAttribute("padding", "0px");
    }
    else {
      style.setDefaultLocalStyleAttribute("margin-left", "8px");
      style.setDefaultLocalStyleAttribute("margin-right", "8px");
      style.setDefaultLocalStyleAttribute("margin-top", "8px");
      style.setDefaultLocalStyleAttribute("margin-bottom", "8px");
    }
  }
  



  public HTMLElement getOffsetParent_js()
  {
    return null;
  }
  




  @JsxGetter
  public String getALink()
  {
    return getDomNodeOrDie().getAttribute("aLink");
  }
  




  @JsxSetter
  public void setALink(String aLink)
  {
    setColorAttribute("aLink", aLink);
  }
  




  @JsxGetter
  public String getBackground()
  {
    HtmlElement node = getDomNodeOrDie();
    return node.getAttribute("background");
  }
  




  @JsxSetter
  public void setBackground(String background)
  {
    getDomNodeOrDie().setAttribute("background", background);
  }
  




  @JsxGetter
  public String getBgColor()
  {
    return getDomNodeOrDie().getAttribute("bgColor");
  }
  




  @JsxSetter
  public void setBgColor(String bgColor)
  {
    setColorAttribute("bgColor", bgColor);
  }
  




  @JsxGetter
  public String getLink()
  {
    return getDomNodeOrDie().getAttribute("link");
  }
  




  @JsxSetter
  public void setLink(String link)
  {
    setColorAttribute("link", link);
  }
  




  @JsxGetter
  public String getText()
  {
    return getDomNodeOrDie().getAttribute("text");
  }
  




  @JsxSetter
  public void setText(String text)
  {
    setColorAttribute("text", text);
  }
  




  @JsxGetter
  public String getVLink()
  {
    return getDomNodeOrDie().getAttribute("vLink");
  }
  




  @JsxSetter
  public void setVLink(String vLink)
  {
    setColorAttribute("vLink", vLink);
  }
  



  public int getClientWidth()
  {
    return super.getClientWidth() + 16;
  }
}
