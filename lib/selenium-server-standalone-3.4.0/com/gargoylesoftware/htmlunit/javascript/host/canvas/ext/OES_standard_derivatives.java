package com.gargoylesoftware.htmlunit.javascript.host.canvas.ext;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class OES_standard_derivatives
  extends SimpleScriptable
{
  @JsxConstant
  public static final int FRAGMENT_SHADER_DERIVATIVE_HINT_OES = 35723;
  
  public OES_standard_derivatives() {}
}
