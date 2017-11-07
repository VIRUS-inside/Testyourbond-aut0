package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
































































































































































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class CSSPrimitiveValue
  extends CSSValue
{
  @JsxConstant
  public static final short CSS_UNKNOWN = 0;
  @JsxConstant
  public static final short CSS_NUMBER = 1;
  @JsxConstant
  public static final short CSS_PERCENTAGE = 2;
  @JsxConstant
  public static final short CSS_EMS = 3;
  @JsxConstant
  public static final short CSS_EXS = 4;
  @JsxConstant
  public static final short CSS_PX = 5;
  @JsxConstant
  public static final short CSS_CM = 6;
  @JsxConstant
  public static final short CSS_MM = 7;
  @JsxConstant
  public static final short CSS_IN = 8;
  @JsxConstant
  public static final short CSS_PT = 9;
  @JsxConstant
  public static final short CSS_PC = 10;
  @JsxConstant
  public static final short CSS_DEG = 11;
  @JsxConstant
  public static final short CSS_RAD = 12;
  @JsxConstant
  public static final short CSS_GRAD = 13;
  @JsxConstant
  public static final short CSS_MS = 14;
  @JsxConstant
  public static final short CSS_S = 15;
  @JsxConstant
  public static final short CSS_HZ = 16;
  @JsxConstant
  public static final short CSS_KHZ = 17;
  @JsxConstant
  public static final short CSS_DIMENSION = 18;
  @JsxConstant
  public static final short CSS_STRING = 19;
  @JsxConstant
  public static final short CSS_URI = 20;
  @JsxConstant
  public static final short CSS_IDENT = 21;
  @JsxConstant
  public static final short CSS_ATTR = 22;
  @JsxConstant
  public static final short CSS_COUNTER = 23;
  @JsxConstant
  public static final short CSS_RECT = 24;
  @JsxConstant
  public static final short CSS_RGBCOLOR = 25;
  private org.w3c.dom.css.CSSPrimitiveValue wrappedCssPrimitiveValue_;
  
  @JsxConstructor
  public CSSPrimitiveValue() {}
  
  CSSPrimitiveValue(Element element, org.w3c.dom.css.CSSPrimitiveValue cssValue)
  {
    super(element, cssValue);
    setParentScope(element.getParentScope());
    setPrototype(getPrototype(getClass()));
    setDomNode(element.getDomNodeOrNull(), false);
    wrappedCssPrimitiveValue_ = cssValue;
  }
  




  @JsxFunction
  public double getFloatValue(int unitType)
  {
    return wrappedCssPrimitiveValue_.getFloatValue((short)unitType);
  }
}
