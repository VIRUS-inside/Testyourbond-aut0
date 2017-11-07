package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgMarker;

@JsxClass(domClass=SvgMarker.class)
public class SVGMarkerElement
  extends SVGElement
{
  @JsxConstant
  public static final int SVG_MARKER_ORIENT_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_MARKERUNITS_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_MARKER_ORIENT_AUTO = 1;
  @JsxConstant
  public static final int SVG_MARKERUNITS_USERSPACEONUSE = 1;
  @JsxConstant
  public static final int SVG_MARKER_ORIENT_ANGLE = 2;
  @JsxConstant
  public static final int SVG_MARKERUNITS_STROKEWIDTH = 2;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGMarkerElement() {}
}
