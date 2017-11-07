package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class SVGPathSeg
  extends SimpleScriptable
{
  @JsxConstant
  public static final int PATHSEG_UNKNOWN = 0;
  @JsxConstant
  public static final int PATHSEG_CLOSEPATH = 1;
  @JsxConstant
  public static final int PATHSEG_MOVETO_ABS = 2;
  @JsxConstant
  public static final int PATHSEG_MOVETO_REL = 3;
  @JsxConstant
  public static final int PATHSEG_LINETO_ABS = 4;
  @JsxConstant
  public static final int PATHSEG_LINETO_REL = 5;
  @JsxConstant
  public static final int PATHSEG_CURVETO_CUBIC_ABS = 6;
  @JsxConstant
  public static final int PATHSEG_CURVETO_CUBIC_REL = 7;
  @JsxConstant
  public static final int PATHSEG_CURVETO_QUADRATIC_ABS = 8;
  @JsxConstant
  public static final int PATHSEG_CURVETO_QUADRATIC_REL = 9;
  @JsxConstant
  public static final int PATHSEG_ARC_ABS = 10;
  @JsxConstant
  public static final int PATHSEG_ARC_REL = 11;
  @JsxConstant
  public static final int PATHSEG_LINETO_HORIZONTAL_ABS = 12;
  @JsxConstant
  public static final int PATHSEG_LINETO_HORIZONTAL_REL = 13;
  @JsxConstant
  public static final int PATHSEG_LINETO_VERTICAL_ABS = 14;
  @JsxConstant
  public static final int PATHSEG_LINETO_VERTICAL_REL = 15;
  @JsxConstant
  public static final int PATHSEG_CURVETO_CUBIC_SMOOTH_ABS = 16;
  @JsxConstant
  public static final int PATHSEG_CURVETO_CUBIC_SMOOTH_REL = 17;
  @JsxConstant
  public static final int PATHSEG_CURVETO_QUADRATIC_SMOOTH_ABS = 18;
  @JsxConstant
  public static final int PATHSEG_CURVETO_QUADRATIC_SMOOTH_REL = 19;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGPathSeg() {}
}
