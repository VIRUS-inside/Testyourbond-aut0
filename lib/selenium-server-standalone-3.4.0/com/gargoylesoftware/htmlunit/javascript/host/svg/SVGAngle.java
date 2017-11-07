package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass
public class SVGAngle
  extends SimpleScriptable
{
  @JsxConstant
  public static final short SVG_ANGLETYPE_UNKNOWN = 0;
  @JsxConstant
  public static final short SVG_ANGLETYPE_UNSPECIFIED = 1;
  @JsxConstant
  public static final short SVG_ANGLETYPE_DEG = 2;
  @JsxConstant
  public static final short SVG_ANGLETYPE_RAD = 3;
  @JsxConstant
  public static final short SVG_ANGLETYPE_GRAD = 4;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGAngle() {}
}
