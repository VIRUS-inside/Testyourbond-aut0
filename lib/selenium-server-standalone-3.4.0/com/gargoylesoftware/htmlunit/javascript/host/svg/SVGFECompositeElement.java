package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgFeComposite;

@JsxClass(domClass=SvgFeComposite.class)
public class SVGFECompositeElement
  extends SVGElement
{
  @JsxConstant
  public static final int SVG_FECOMPOSITE_OPERATOR_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_FECOMPOSITE_OPERATOR_OVER = 1;
  @JsxConstant
  public static final int SVG_FECOMPOSITE_OPERATOR_IN = 2;
  @JsxConstant
  public static final int SVG_FECOMPOSITE_OPERATOR_OUT = 3;
  @JsxConstant
  public static final int SVG_FECOMPOSITE_OPERATOR_ATOP = 4;
  @JsxConstant
  public static final int SVG_FECOMPOSITE_OPERATOR_XOR = 5;
  @JsxConstant
  public static final int SVG_FECOMPOSITE_OPERATOR_ARITHMETIC = 6;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGFECompositeElement() {}
}
