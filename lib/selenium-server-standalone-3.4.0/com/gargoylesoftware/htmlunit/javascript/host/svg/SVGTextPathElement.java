package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.svg.SvgTextPath;

@JsxClass(domClass=SvgTextPath.class)
public class SVGTextPathElement
  extends SVGTextContentElement
{
  @JsxConstant
  public static final int TEXTPATH_METHODTYPE_UNKNOWN = 0;
  @JsxConstant
  public static final int TEXTPATH_SPACINGTYPE_UNKNOWN = 0;
  @JsxConstant
  public static final int TEXTPATH_METHODTYPE_ALIGN = 1;
  @JsxConstant
  public static final int TEXTPATH_SPACINGTYPE_AUTO = 1;
  @JsxConstant
  public static final int TEXTPATH_METHODTYPE_STRETCH = 2;
  @JsxConstant
  public static final int TEXTPATH_SPACINGTYPE_EXACT = 2;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGTextPathElement() {}
}
