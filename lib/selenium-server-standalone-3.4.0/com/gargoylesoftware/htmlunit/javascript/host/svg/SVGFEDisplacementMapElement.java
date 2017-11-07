package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgFeDisplacementMap;

@JsxClass(domClass=SvgFeDisplacementMap.class)
public class SVGFEDisplacementMapElement
  extends SVGElement
{
  @JsxConstant
  public static final int SVG_CHANNEL_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_CHANNEL_R = 1;
  @JsxConstant
  public static final int SVG_CHANNEL_G = 2;
  @JsxConstant
  public static final int SVG_CHANNEL_B = 3;
  @JsxConstant
  public static final int SVG_CHANNEL_A = 4;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGFEDisplacementMapElement() {}
}
