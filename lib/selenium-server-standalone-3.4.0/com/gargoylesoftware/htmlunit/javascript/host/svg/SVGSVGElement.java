package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.svg.SvgSvg;


































@JsxClass(domClass=SvgSvg.class)
public class SVGSVGElement
  extends SVGGraphicsElement
{
  @JsxConstant
  public static final int SVG_ZOOMANDPAN_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_ZOOMANDPAN_DISABLE = 1;
  @JsxConstant
  public static final int SVG_ZOOMANDPAN_MAGNIFY = 2;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGSVGElement() {}
  
  @JsxFunction
  public SVGMatrix createSVGMatrix()
  {
    return new SVGMatrix(getWindow());
  }
  



  @JsxFunction
  public SVGMatrix getScreenCTM()
  {
    return new SVGMatrix(getWindow());
  }
  



  @JsxFunction
  public SVGRect createSVGRect()
  {
    SVGRect rect = new SVGRect();
    rect.setPrototype(getPrototype(rect.getClass()));
    rect.setParentScope(getParentScope());
    return rect;
  }
}
