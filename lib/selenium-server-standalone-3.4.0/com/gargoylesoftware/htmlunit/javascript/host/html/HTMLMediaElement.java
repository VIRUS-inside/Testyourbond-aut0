package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlMedia;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;






























































@JsxClass
public class HTMLMediaElement
  extends HTMLElement
{
  @JsxConstant
  public static final short HAVE_NOTHING = 0;
  @JsxConstant
  public static final short HAVE_METADATA = 1;
  @JsxConstant
  public static final short HAVE_CURRENT_DATA = 2;
  @JsxConstant
  public static final short HAVE_FUTURE_DATA = 3;
  @JsxConstant
  public static final short HAVE_ENOUGH_DATA = 4;
  @JsxConstant
  public static final short NETWORK_EMPTY = 0;
  @JsxConstant
  public static final short NETWORK_IDLE = 1;
  @JsxConstant
  public static final short NETWORK_LOADING = 2;
  @JsxConstant
  public static final short NETWORK_NO_SOURCE = 3;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLMediaElement() {}
  
  @JsxFunction
  public String canPlayType(String type)
  {
    return ((HtmlMedia)getDomNodeOrDie()).canPlayType(type);
  }
}
