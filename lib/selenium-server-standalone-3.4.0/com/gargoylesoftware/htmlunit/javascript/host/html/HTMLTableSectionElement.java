package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;


































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlTableBody.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlTableHeader.class), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(domClass=com.gargoylesoftware.htmlunit.html.HtmlTableFooter.class)})
public class HTMLTableSectionElement
  extends RowContainer
{
  private static final String[] VALIGN_VALID_VALUES_IE = { "top", "bottom", "middle", "baseline" };
  


  private static final String VALIGN_DEFAULT_VALUE = "top";
  



  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLTableSectionElement() {}
  



  @JsxGetter
  public String getVAlign()
  {
    return getVAlign(getValidVAlignValues(), "top");
  }
  



  @JsxSetter
  public void setVAlign(Object vAlign)
  {
    setVAlign(vAlign, getValidVAlignValues());
  }
  



  private String[] getValidVAlignValues()
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_TABLE_VALIGN_SUPPORTS_IE_VALUES)) {
      return VALIGN_VALID_VALUES_IE;
    }
    return null;
  }
  




  @JsxGetter
  public String getCh()
  {
    return super.getCh();
  }
  




  @JsxSetter
  public void setCh(String ch)
  {
    super.setCh(ch);
  }
  




  @JsxGetter
  public String getChOff()
  {
    return super.getChOff();
  }
  




  @JsxSetter
  public void setChOff(String chOff)
  {
    super.setChOff(chOff);
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getBgColor()
  {
    return getDomNodeOrDie().getAttribute("bgColor");
  }
  




  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setBgColor(String bgColor)
  {
    setColorAttribute("bgColor", bgColor);
  }
  




  @JsxSetter
  public void setOuterHTML(Object value)
  {
    throw Context.reportRuntimeError("outerHTML is read-only for tag '" + 
      getDomNodeOrDie().getNodeName() + "'");
  }
  




  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public void setInnerText(Object value)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_INNER_TEXT_READONLY_FOR_TABLE)) {
      throw Context.reportRuntimeError("innerText is read-only for tag '" + 
        getDomNodeOrDie().getNodeName() + "'");
    }
    super.setInnerText(value);
  }
}
