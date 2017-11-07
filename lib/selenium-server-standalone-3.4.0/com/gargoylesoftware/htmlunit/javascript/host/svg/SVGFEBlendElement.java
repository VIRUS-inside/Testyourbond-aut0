package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgFeBlend;

@JsxClass(domClass=SvgFeBlend.class)
public class SVGFEBlendElement
  extends SVGElement
{
  @JsxConstant
  public static final int SVG_FEBLEND_MODE_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_FEBLEND_MODE_NORMAL = 1;
  @JsxConstant
  public static final int SVG_FEBLEND_MODE_MULTIPLY = 2;
  @JsxConstant
  public static final int SVG_FEBLEND_MODE_SCREEN = 3;
  @JsxConstant
  public static final int SVG_FEBLEND_MODE_DARKEN = 4;
  @JsxConstant
  public static final int SVG_FEBLEND_MODE_LIGHTEN = 5;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_OVERLAY = 6;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_COLOR_DODGE = 7;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_COLOR_BURN = 8;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_HARD_LIGHT = 9;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_SOFT_LIGHT = 10;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_DIFFERENCE = 11;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_EXCLUSION = 12;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_HUE = 13;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_SATURATION = 14;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_COLOR = 15;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SVG_FEBLEND_MODE_LUMINOSITY = 16;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGFEBlendElement() {}
}
