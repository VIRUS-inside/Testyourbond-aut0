package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgView;

@JsxClass(domClass=SvgView.class)
public class SVGViewElement
  extends SVGElement
{
  @JsxConstant
  public static final int SVG_ZOOMANDPAN_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_ZOOMANDPAN_DISABLE = 1;
  @JsxConstant
  public static final int SVG_ZOOMANDPAN_MAGNIFY = 2;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGViewElement() {}
}
