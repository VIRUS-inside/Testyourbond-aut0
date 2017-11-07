package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgFeMorphology;

@JsxClass(domClass=SvgFeMorphology.class)
public class SVGFEMorphologyElement
  extends SVGElement
{
  @JsxConstant
  public static final int SVG_MORPHOLOGY_OPERATOR_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_MORPHOLOGY_OPERATOR_ERODE = 1;
  @JsxConstant
  public static final int SVG_MORPHOLOGY_OPERATOR_DILATE = 2;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGFEMorphologyElement() {}
}
