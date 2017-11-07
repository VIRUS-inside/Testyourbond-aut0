package com.gargoylesoftware.htmlunit.javascript.host.canvas.ext;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class EXT_texture_filter_anisotropic
  extends SimpleScriptable
{
  @JsxConstant
  public static final int MAX_TEXTURE_MAX_ANISOTROPY_EXT = 34047;
  @JsxConstant
  public static final int TEXTURE_MAX_ANISOTROPY_EXT = 34046;
  
  public EXT_texture_filter_anisotropic() {}
}
