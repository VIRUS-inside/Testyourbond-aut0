package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.ArrayUtils;
































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlExample.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlPreformattedText.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlListing.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})})
public class HTMLPreElement
  extends HTMLElement
{
  private static final String[] VALID_CLEAR_VALUES = { "left", "right", "all", "none" };
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLPreElement() {}
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getCite()
  {
    String cite = getDomNodeOrDie().getAttribute("cite");
    return cite;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setCite(String cite)
  {
    getDomNodeOrDie().setAttribute("cite", cite);
  }
  



  @JsxGetter(propertyName="width")
  public Object getWidth_js()
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_PRE_WIDTH_STRING)) {
      return getWidthOrHeight("width", Boolean.TRUE);
    }
    String value = getDomNodeOrDie().getAttribute("width");
    Integer intValue = HTMLCanvasElement.getValue(value);
    if (intValue != null) {
      return intValue;
    }
    return Integer.valueOf(0);
  }
  



  @JsxSetter
  public void setWidth(String width)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_PRE_WIDTH_STRING)) {
      setWidthOrHeight("width", width, true);
    }
    else {
      getDomNodeOrDie().setAttribute("width", width);
    }
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getClear()
  {
    String clear = getDomNodeOrDie().getAttribute("clear");
    if (!ArrayUtils.contains(VALID_CLEAR_VALUES, clear)) {
      return "";
    }
    return clear;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setClear(String clear)
  {
    if (!ArrayUtils.contains(VALID_CLEAR_VALUES, clear)) {
      throw Context.reportRuntimeError("Invalid clear property value: '" + clear + "'.");
    }
    getDomNodeOrDie().setAttribute("clear", clear);
  }
}
