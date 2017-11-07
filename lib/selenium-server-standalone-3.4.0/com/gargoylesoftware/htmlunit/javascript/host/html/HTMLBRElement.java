package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlBreak;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.ArrayUtils;


























@JsxClass(domClass=HtmlBreak.class)
public class HTMLBRElement
  extends HTMLElement
{
  private static final String[] VALID_CLEAR_VALUES = { "left", "right", "all", "none" };
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLBRElement() {}
  



  @JsxGetter
  public String getClear()
  {
    String clear = getDomNodeOrDie().getAttribute("clear");
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CLEAR_RESTRICT)) && (!ArrayUtils.contains(VALID_CLEAR_VALUES, clear))) {
      return "";
    }
    return clear;
  }
  



  @JsxSetter
  public void setClear(String clear)
  {
    if ((getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_CLEAR_RESTRICT)) && (!ArrayUtils.contains(VALID_CLEAR_VALUES, clear))) {
      throw Context.reportRuntimeError("Invalid clear property value: '" + clear + "'.");
    }
    getDomNodeOrDie().setAttribute("clear", clear);
  }
  



  protected boolean isEndTagForbidden()
  {
    return true;
  }
}
