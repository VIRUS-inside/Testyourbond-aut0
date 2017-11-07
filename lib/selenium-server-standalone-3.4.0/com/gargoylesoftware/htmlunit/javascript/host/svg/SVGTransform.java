package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass
public class SVGTransform
  extends SimpleScriptable
{
  @JsxConstant
  public static final int SVG_TRANSFORM_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_TRANSFORM_MATRIX = 1;
  @JsxConstant
  public static final int SVG_TRANSFORM_TRANSLATE = 2;
  @JsxConstant
  public static final int SVG_TRANSFORM_SCALE = 3;
  @JsxConstant
  public static final int SVG_TRANSFORM_ROTATE = 4;
  @JsxConstant
  public static final int SVG_TRANSFORM_SKEWX = 5;
  @JsxConstant
  public static final int SVG_TRANSFORM_SKEWY = 6;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGTransform() {}
}
