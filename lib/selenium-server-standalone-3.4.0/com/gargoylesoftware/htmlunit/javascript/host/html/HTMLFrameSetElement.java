package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFrameSet;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;


































@JsxClass(domClass=HtmlFrameSet.class)
public class HTMLFrameSetElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLFrameSetElement() {}
  
  protected boolean isEventHandlerOnWindow()
  {
    return true;
  }
  




  @JsxSetter
  public void setRows(String rows)
  {
    HtmlFrameSet htmlFrameSet = (HtmlFrameSet)getDomNodeOrNull();
    if (htmlFrameSet != null) {
      htmlFrameSet.setAttribute("rows", rows);
    }
  }
  





  @JsxGetter
  public String getRows()
  {
    HtmlFrameSet htmlFrameSet = (HtmlFrameSet)getDomNodeOrNull();
    return htmlFrameSet.getRowsAttribute();
  }
  




  @JsxSetter
  public void setCols(String cols)
  {
    HtmlFrameSet htmlFrameSet = (HtmlFrameSet)getDomNodeOrNull();
    if (htmlFrameSet != null) {
      htmlFrameSet.setAttribute("cols", cols);
    }
  }
  




  @JsxGetter
  public String getCols()
  {
    HtmlFrameSet htmlFrameSet = (HtmlFrameSet)getDomNodeOrNull();
    return htmlFrameSet.getColsAttribute();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getBorder()
  {
    String border = getDomNodeOrDie().getAttribute("border");
    return border;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setBorder(String border)
  {
    getDomNodeOrDie().setAttribute("border", border);
  }
  




  @JsxSetter
  public void setOuterHTML(Object value)
  {
    throw Context.reportRuntimeError("outerHTML is read-only for tag 'frameset'");
  }
}
