package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;






































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(className="DOMRect", browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})})
public class ClientRect
  extends SimpleScriptable
{
  private int bottom_;
  private int left_;
  private int right_;
  private int top_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public ClientRect() {}
  
  public ClientRect(int bottom, int left, int right, int top)
  {
    bottom_ = bottom;
    left_ = left;
    right_ = right;
    top_ = top;
  }
  



  @JsxSetter
  public void setBottom(int bottom)
  {
    bottom_ = bottom;
  }
  



  @JsxGetter
  public int getBottom()
  {
    return bottom_;
  }
  



  @JsxSetter
  public void setLeft(int left)
  {
    left_ = left;
  }
  



  @JsxGetter
  public int getLeft()
  {
    return left_;
  }
  



  @JsxSetter
  public void setRight(int right)
  {
    right_ = right;
  }
  



  @JsxGetter
  public int getRight()
  {
    return right_;
  }
  



  @JsxSetter
  public void setTop(int top)
  {
    top_ = top;
  }
  



  @JsxGetter
  public int getTop()
  {
    return top_;
  }
  



  @JsxGetter
  public int getWidth()
  {
    return getRight() - getLeft();
  }
  



  @JsxGetter
  public int getHeight()
  {
    return getBottom() - getTop();
  }
}
