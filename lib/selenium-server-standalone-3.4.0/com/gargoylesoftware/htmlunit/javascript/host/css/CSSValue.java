package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Element;























































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class CSSValue
  extends SimpleScriptable
{
  @JsxConstant
  public static final short CSS_INHERIT = 0;
  @JsxConstant
  public static final short CSS_PRIMITIVE_VALUE = 1;
  @JsxConstant
  public static final short CSS_VALUE_LIST = 2;
  @JsxConstant
  public static final short CSS_CUSTOM = 3;
  private org.w3c.dom.css.CSSValue wrappedCssValue_;
  
  @JsxConstructor
  public CSSValue() {}
  
  CSSValue(Element element, org.w3c.dom.css.CSSValue cssValue)
  {
    setParentScope(element.getParentScope());
    setPrototype(getPrototype(getClass()));
    setDomNode(element.getDomNodeOrNull(), false);
    wrappedCssValue_ = cssValue;
  }
  



  @JsxGetter
  public String getCssText()
  {
    return wrappedCssValue_.getCssText();
  }
}
