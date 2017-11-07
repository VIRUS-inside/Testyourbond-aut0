package com.gargoylesoftware.htmlunit.javascript.host.svg;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass
public class SVGComponentTransferFunctionElement
  extends SVGElement
{
  @JsxConstant
  public static final int SVG_FECOMPONENTTRANSFER_TYPE_UNKNOWN = 0;
  @JsxConstant
  public static final int SVG_FECOMPONENTTRANSFER_TYPE_IDENTITY = 1;
  @JsxConstant
  public static final int SVG_FECOMPONENTTRANSFER_TYPE_TABLE = 2;
  @JsxConstant
  public static final int SVG_FECOMPONENTTRANSFER_TYPE_DISCRETE = 3;
  @JsxConstant
  public static final int SVG_FECOMPONENTTRANSFER_TYPE_LINEAR = 4;
  @JsxConstant
  public static final int SVG_FECOMPONENTTRANSFER_TYPE_GAMMA = 5;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public SVGComponentTransferFunctionElement() {}
}
