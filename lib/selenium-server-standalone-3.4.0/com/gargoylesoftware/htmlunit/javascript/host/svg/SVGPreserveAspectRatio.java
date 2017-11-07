package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass
public class SVGPreserveAspectRatio
  extends SimpleScriptable
{
  @JsxConstant
  public static final int SVG_MEETORSLICE_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_MEETORSLICE_MEET = 1;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_NONE = 1;
  @JsxConstant
  public static final int SVG_MEETORSLICE_SLICE = 2;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_XMINYMIN = 2;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_XMIDYMIN = 3;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_XMAXYMIN = 4;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_XMINYMID = 5;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_XMIDYMID = 6;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_XMAXYMID = 7;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_XMINYMAX = 8;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_XMIDYMAX = 9;
  @JsxConstant
  public static final int SVG_PRESERVEASPECTRATIO_XMAXYMAX = 10;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGPreserveAspectRatio() {}
}
