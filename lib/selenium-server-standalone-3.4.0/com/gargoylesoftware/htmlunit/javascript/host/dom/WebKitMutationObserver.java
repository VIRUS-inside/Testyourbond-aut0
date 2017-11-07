package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import net.sourceforge.htmlunit.corejs.javascript.Function;






























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class WebKitMutationObserver
  extends MutationObserver
{
  public WebKitMutationObserver() {}
  
  @JsxConstructor
  public WebKitMutationObserver(Function function)
  {
    super(function);
  }
}
