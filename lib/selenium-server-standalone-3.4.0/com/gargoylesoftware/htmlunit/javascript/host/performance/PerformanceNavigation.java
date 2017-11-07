package com.gargoylesoftware.htmlunit.javascript.host.performance;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;







































@JsxClass
public class PerformanceNavigation
  extends SimpleScriptable
{
  @JsxConstant
  public static final int TYPE_NAVIGATE = 0;
  @JsxConstant
  public static final int TYPE_RELOAD = 1;
  @JsxConstant
  public static final int TYPE_BACK_FORWARD = 2;
  @JsxConstant
  public static final int TYPE_RESERVED = 255;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public PerformanceNavigation() {}
  
  @JsxGetter
  public int getType()
  {
    return 0;
  }
  



  @JsxGetter
  public int getRedirectCount()
  {
    return 0;
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object toJSON()
  {
    throw new UnsupportedOperationException();
  }
}
