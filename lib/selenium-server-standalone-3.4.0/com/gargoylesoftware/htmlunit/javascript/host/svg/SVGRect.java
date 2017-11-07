package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;































@JsxClass
public class SVGRect
  extends SimpleScriptable
{
  private double xValue_;
  private double yValue_;
  private double width_;
  private double height_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGRect() {}
  
  @JsxGetter
  public double getX()
  {
    return xValue_;
  }
  



  @JsxSetter
  public void setX(double x)
  {
    xValue_ = x;
  }
  



  @JsxGetter
  public double getY()
  {
    return yValue_;
  }
  



  @JsxSetter
  public void setY(double y)
  {
    yValue_ = y;
  }
  



  @JsxGetter
  public double getWidth()
  {
    return width_;
  }
  



  @JsxSetter
  public void setWidth(double width)
  {
    width_ = width;
  }
  



  @JsxGetter
  public double getHeight()
  {
    return height_;
  }
  



  @JsxSetter
  public void setHeigth(double height)
  {
    height_ = height;
  }
}
