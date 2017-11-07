package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class MouseScrollEvent
  extends MouseEvent
{
  @JsxConstant
  public static final int HORIZONTAL_AXIS = 1;
  @JsxConstant
  public static final int VERTICAL_AXIS = 2;
  
  @JsxConstructor
  public MouseScrollEvent() {}
}
