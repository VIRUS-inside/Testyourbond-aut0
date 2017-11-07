package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass
public class SVGLength
  extends SimpleScriptable
{
  @JsxConstant
  public static final int SVG_LENGTHTYPE_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_LENGTHTYPE_NUMBER = 1;
  @JsxConstant
  public static final int SVG_LENGTHTYPE_PERCENTAGE = 2;
  @JsxConstant
  public static final int SVG_LENGTHTYPE_EMS = 3;
  @JsxConstant
  public static final int SVG_LENGTHTYPE_EXS = 4;
  @JsxConstant
  public static final int SVG_LENGTHTYPE_PX = 5;
  @JsxConstant
  public static final int SVG_LENGTHTYPE_CM = 6;
  @JsxConstant
  public static final int SVG_LENGTHTYPE_MM = 7;
  @JsxConstant
  public static final int SVG_LENGTHTYPE_IN = 8;
  @JsxConstant
  public static final int SVG_LENGTHTYPE_PT = 9;
  @JsxConstant
  public static final int SVG_LENGTHTYPE_PC = 10;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGLength() {}
}
