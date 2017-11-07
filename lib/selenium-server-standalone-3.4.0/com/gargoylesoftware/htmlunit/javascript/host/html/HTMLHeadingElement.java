package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.ArrayUtils;







































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlHeading1.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlHeading2.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlHeading3.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlHeading4.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlHeading5.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlHeading6.class)})
public class HTMLHeadingElement
  extends HTMLElement
{
  private static final String[] VALID_CLEAR_VALUES = { "left", "right", "all", "none" };
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLHeadingElement() {}
  



  @JsxGetter
  public String getAlign()
  {
    return getAlign(false);
  }
  



  @JsxSetter
  public void setAlign(String align)
  {
    setAlign(align, false);
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
