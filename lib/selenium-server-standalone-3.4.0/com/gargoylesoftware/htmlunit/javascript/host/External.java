package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;











































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(isJSObject=false)})
public class External
  extends SimpleScriptable
{
  @JsxConstructor
  public External() {}
  
  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void AutoCompleteSaveForm() {}
  
  @JsxFunction
  public void AddSearchProvider() {}
  
  @JsxFunction
  public int IsSearchProviderInstalled()
  {
    return 0;
  }
}
