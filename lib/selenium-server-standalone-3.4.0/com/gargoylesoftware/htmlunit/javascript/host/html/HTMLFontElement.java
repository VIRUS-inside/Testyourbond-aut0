package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFont;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
































@JsxClass(domClass=HtmlFont.class)
public class HTMLFontElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLFontElement() {}
  
  @JsxGetter
  public String getColor()
  {
    return getDomNodeOrDie().getAttribute("color");
  }
  



  @JsxSetter
  public void setColor(String color)
  {
    getDomNodeOrDie().setAttribute("color", color);
  }
  



  @JsxGetter
  public String getFace()
  {
    return getDomNodeOrDie().getAttribute("face");
  }
  



  @JsxSetter
  public void setFace(String face)
  {
    getDomNodeOrDie().setAttribute("face", face);
  }
  



  @JsxGetter
  public int getSize()
  {
    return (int)Context.toNumber(getDomNodeOrDie().getAttribute("size"));
  }
  



  @JsxSetter
  public void setSize(int size)
  {
    getDomNodeOrDie().setAttribute("size", Context.toString(Integer.valueOf(size)));
  }
}
