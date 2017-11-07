package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgFeTurbulence;

@JsxClass(domClass=SvgFeTurbulence.class)
public class SVGFETurbulenceElement
  extends SVGElement
{
  @JsxConstant
  public static final int SVG_STITCHTYPE_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_TURBULENCE_TYPE_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_STITCHTYPE_STITCH = 1;
  @JsxConstant
  public static final int SVG_TURBULENCE_TYPE_FRACTALNOISE = 1;
  @JsxConstant
  public static final int SVG_STITCHTYPE_NOSTITCH = 2;
  @JsxConstant
  public static final int SVG_TURBULENCE_TYPE_TURBULENCE = 2;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGFETurbulenceElement() {}
}
