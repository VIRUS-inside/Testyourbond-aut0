package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgFeConvolveMatrix;

@JsxClass(domClass=SvgFeConvolveMatrix.class)
public class SVGFEConvolveMatrixElement
  extends SVGElement
{
  @JsxConstant
  public static final int SVG_EDGEMODE_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_EDGEMODE_DUPLICATE = 1;
  @JsxConstant
  public static final int SVG_EDGEMODE_WRAP = 2;
  @JsxConstant
  public static final int SVG_EDGEMODE_NONE = 3;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGFEConvolveMatrixElement() {}
}
