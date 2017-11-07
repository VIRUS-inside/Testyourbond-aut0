package com.gargoylesoftware.htmlunit.javascript.host.css;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;






























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class CSS2Properties
  extends ComputedCSSStyleDeclaration
{
  @JsxConstructor
  public CSS2Properties() {}
  
  public CSS2Properties(CSSStyleDeclaration style)
  {
    super(style);
  }
}
