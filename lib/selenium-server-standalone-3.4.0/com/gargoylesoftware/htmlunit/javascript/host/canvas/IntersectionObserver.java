package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Element;















































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class IntersectionObserver
  extends SimpleScriptable
{
  @JsxConstructor
  public IntersectionObserver() {}
  
  @JsxFunction
  public void observe(Element target) {}
  
  @JsxFunction
  public void unobserve(Element target) {}
  
  @JsxFunction
  public void disconnect() {}
  
  @JsxFunction
  public Object takeRecords()
  {
    return null;
  }
}
