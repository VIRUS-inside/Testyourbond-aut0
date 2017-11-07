package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTableColumn;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;



































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=HtmlTableColumn.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlTableColumnGroup.class)})
public class HTMLTableColElement
  extends HTMLTableComponent
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLTableColElement() {}
  
  @JsxGetter
  public int getSpan()
  {
    String span = getDomNodeOrDie().getAttribute("span");
    int i;
    try {
      int i = Integer.parseInt(span);
      if (i < 1) {
        i = 1;
      }
    }
    catch (NumberFormatException e) {
      i = 1;
    }
    return i;
  }
  



  @JsxSetter
  public void setSpan(Object span)
  {
    double d = Context.toNumber(span);
    int i = (int)d;
    if (i < 1) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_SPAN_THROWS_EXCEPTION_IF_INVALID)) {
        Exception e = new Exception("Cannot set the span property to invalid value: " + span);
        Context.throwAsScriptRuntimeEx(e);
      }
      else {
        i = 1;
      }
    }
    getDomNodeOrDie().setAttribute("span", Integer.toString(i));
  }
  



  @JsxGetter(propertyName="width")
  public String getWidth_js()
  {
    boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_COLUMN_WIDTH_NO_NEGATIVE_VALUES);
    Boolean returnNegativeValues = ie ? Boolean.FALSE : null;
    return getWidthOrHeight("width", returnNegativeValues);
  }
  

  @JsxSetter
  public void setWidth(Object width)
  {
    String value;
    
    String value;
    if ((width == null) && (!getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_COLUMN_WIDTH_NULL_STRING))) {
      value = "";
    }
    else {
      value = Context.toString(width);
    }
    setWidthOrHeight("width", value, false);
  }
  



  protected boolean isEndTagForbidden()
  {
    return getDomNodeOrDie() instanceof HtmlTableColumn;
  }
  




  public void setOuterHTML(Object value)
  {
    throw Context.reportRuntimeError("outerHTML is read-only for tag '" + 
      getDomNodeOrDie().getNodeName() + "'");
  }
}
