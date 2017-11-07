package com.gargoylesoftware.htmlunit.javascript.host.canvas.ext;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class WEBGL_compressed_texture_s3tc
  extends SimpleScriptable
{
  @JsxConstant
  public static final int COMPRESSED_RGBA_S3TC_DXT1_EXT = 33777;
  @JsxConstant
  public static final int COMPRESSED_RGBA_S3TC_DXT3_EXT = 33778;
  @JsxConstant
  public static final int COMPRESSED_RGBA_S3TC_DXT5_EXT = 33779;
  @JsxConstant
  public static final int COMPRESSED_RGB_S3TC_DXT1_EXT = 33776;
  
  public WEBGL_compressed_texture_s3tc() {}
}
