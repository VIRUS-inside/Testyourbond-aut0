package com.gargoylesoftware.htmlunit.javascript.host.media;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass
public class MediaError
  extends SimpleScriptable
{
  @JsxConstant
  public static final int MEDIA_ERR_ABORTED = 1;
  @JsxConstant
  public static final int MEDIA_ERR_NETWORK = 2;
  @JsxConstant
  public static final int MEDIA_ERR_DECODE = 3;
  @JsxConstant
  public static final int MEDIA_ERR_SRC_NOT_SUPPORTED = 4;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public MediaError() {}
}
