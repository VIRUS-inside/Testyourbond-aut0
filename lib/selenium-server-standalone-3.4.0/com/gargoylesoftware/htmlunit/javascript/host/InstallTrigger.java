package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;






























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class InstallTrigger
  extends SimpleScriptable
{
  @JsxConstant
  public static final int SKIN = 1;
  @JsxConstant
  public static final int LOCALE = 2;
  @JsxConstant
  public static final int CONTENT = 4;
  @JsxConstant
  public static final int PACKAGE = 7;
  
  public InstallTrigger() {}
  
  public Object getDefaultValue(Class<?> hint)
  {
    return "[object InstallTriggerImpl]";
  }
}
