package com.gargoylesoftware.htmlunit.javascript.host.canvas.ext;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class WEBGL_debug_renderer_info
  extends SimpleScriptable
{
  @JsxConstant
  public static final int UNMASKED_RENDERER_WEBGL = 37446;
  @JsxConstant
  public static final int UNMASKED_VENDOR_WEBGL = 37445;
  
  public WEBGL_debug_renderer_info() {}
}
