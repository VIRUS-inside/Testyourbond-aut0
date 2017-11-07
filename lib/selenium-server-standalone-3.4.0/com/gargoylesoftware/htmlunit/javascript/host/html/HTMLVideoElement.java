package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlVideo;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

































@JsxClass(domClass=HtmlVideo.class)
public class HTMLVideoElement
  extends HTMLMediaElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLVideoElement() {}
  
  @JsxGetter
  public int getWidth()
  {
    String value = getDomNodeOrDie().getAttribute("width");
    Integer intValue = HTMLCanvasElement.getValue(value);
    if (intValue != null) {
      return intValue.intValue();
    }
    return 0;
  }
  



  @JsxSetter
  public void setWidth(int width)
  {
    getDomNodeOrDie().setAttribute("width", Integer.toString(width));
  }
  




  @JsxGetter
  public int getHeight()
  {
    String value = getDomNodeOrDie().getAttribute("height");
    Integer intValue = HTMLCanvasElement.getValue(value);
    if (intValue != null) {
      return intValue.intValue();
    }
    return 0;
  }
  



  @JsxSetter
  public void setHeight(int height)
  {
    getDomNodeOrDie().setAttribute("height", Integer.toString(height));
  }
}
