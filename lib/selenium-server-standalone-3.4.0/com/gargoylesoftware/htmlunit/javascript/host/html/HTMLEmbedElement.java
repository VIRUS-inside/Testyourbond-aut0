package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlEmbed;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;































@JsxClass(domClass=HtmlEmbed.class)
public class HTMLEmbedElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLEmbedElement() {}
  
  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getAlign()
  {
    return getAlign(true);
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setAlign(String align)
  {
    setAlign(align, false);
  }
  



  @JsxGetter(propertyName="height")
  public String getHeightString()
  {
    return getDomNodeOrDie().getAttribute("height");
  }
  



  @JsxSetter(propertyName="height")
  public void setHeightString(String height)
  {
    getDomNodeOrDie().setAttribute("height", height);
  }
  



  @JsxGetter(propertyName="width")
  public String getWidthString()
  {
    return getDomNodeOrDie().getAttribute("width");
  }
  



  @JsxSetter(propertyName="width")
  public void setWidthString(String width)
  {
    getDomNodeOrDie().setAttribute("width", width);
  }
  



  protected boolean isEndTagForbidden()
  {
    return true;
  }
  



  @JsxGetter
  public String getName()
  {
    return getDomNodeOrDie().getAttribute("name");
  }
  



  @JsxSetter
  public void setName(String name)
  {
    getDomNodeOrDie().setAttribute("name", name);
  }
}
