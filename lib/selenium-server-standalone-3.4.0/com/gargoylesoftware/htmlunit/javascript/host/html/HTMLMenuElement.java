package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlMenu;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.StringUtils;





























@JsxClass(domClass=HtmlMenu.class)
public class HTMLMenuElement
  extends HTMLListElement
{
  private String label_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLMenuElement()
  {
    label_ = "";
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getType()
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_MENU_TYPE_EMPTY)) {
      return "";
    }
    
    String type = getDomNodeOrDie().getAttribute("type");
    if ("context".equalsIgnoreCase(type)) {
      return "context";
    }
    if ("toolbar".equalsIgnoreCase(type)) {
      return "toolbar";
    }
    return "list";
  }
  




  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setType(String type)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_MENU_TYPE_EMPTY)) {
      if (StringUtils.isEmpty(type)) {
        return;
      }
      throw Context.reportRuntimeError("Cannot set the type property to invalid value: '" + type + "'");
    }
    
    if ("context".equalsIgnoreCase(type)) {
      getDomNodeOrDie().setAttribute("type", "context");
      return;
    }
    if ("toolbar".equalsIgnoreCase(type)) {
      getDomNodeOrDie().setAttribute("type", "toolbar");
      return;
    }
    
    getDomNodeOrDie().setAttribute("type", "list");
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public String getLabel()
  {
    return label_;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void setLabel(String label)
  {
    label_ = label;
  }
}
