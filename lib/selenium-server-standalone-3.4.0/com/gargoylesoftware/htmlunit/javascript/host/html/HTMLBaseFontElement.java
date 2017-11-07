package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.HtmlBaseFont;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;





























@JsxClass(domClass=HtmlBaseFont.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class HTMLBaseFontElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLBaseFontElement() {}
  
  @JsxGetter
  public String getColor()
  {
    HtmlBaseFont base = (HtmlBaseFont)getDomNodeOrDie();
    return base.getColorAttribute();
  }
  



  @JsxSetter
  public void setColor(String color)
  {
    getDomNodeOrDie().setAttribute("color", color);
  }
  



  @JsxGetter
  public String getFace()
  {
    HtmlBaseFont base = (HtmlBaseFont)getDomNodeOrDie();
    return base.getFaceAttribute();
  }
  



  @JsxSetter
  public void setFace(String face)
  {
    getDomNodeOrDie().setAttribute("face", face);
  }
  



  @JsxGetter
  public int getSize()
  {
    HtmlBaseFont base = (HtmlBaseFont)getDomNodeOrDie();
    return (int)Context.toNumber(base.getSizeAttribute());
  }
  



  @JsxSetter
  public void setSize(int size)
  {
    getDomNodeOrDie().setAttribute("size", Context.toString(Integer.valueOf(size)));
  }
  



  protected boolean isEndTagForbidden()
  {
    return getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLBASEFONT_END_TAG_FORBIDDEN);
  }
}
