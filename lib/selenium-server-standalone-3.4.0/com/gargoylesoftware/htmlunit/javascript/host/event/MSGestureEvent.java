package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class MSGestureEvent
  extends Event
{
  @JsxConstant
  public static final int MSGESTURE_FLAG_BEGIN = 1;
  @JsxConstant
  public static final int MSGESTURE_FLAG_CANCEL = 4;
  @JsxConstant
  public static final int MSGESTURE_FLAG_END = 2;
  @JsxConstant
  public static final int MSGESTURE_FLAG_INERTIA = 8;
  @JsxConstant
  public static final int MSGESTURE_FLAG_NONE = 0;
  
  public MSGestureEvent() {}
}
