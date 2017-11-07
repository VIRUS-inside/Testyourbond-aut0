package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgFeColorMatrix;

@JsxClass(domClass=SvgFeColorMatrix.class)
public class SVGFEColorMatrixElement
  extends SVGElement
{
  @JsxConstant
  public static final int SVG_FECOLORMATRIX_TYPE_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_FECOLORMATRIX_TYPE_MATRIX = 1;
  @JsxConstant
  public static final int SVG_FECOLORMATRIX_TYPE_SATURATE = 2;
  @JsxConstant
  public static final int SVG_FECOLORMATRIX_TYPE_HUEROTATE = 3;
  @JsxConstant
  public static final int SVG_FECOLORMATRIX_TYPE_LUMINANCETOALPHA = 4;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGFEColorMatrixElement() {}
}
