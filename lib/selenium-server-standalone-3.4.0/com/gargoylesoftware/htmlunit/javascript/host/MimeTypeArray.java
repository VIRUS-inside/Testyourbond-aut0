package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;




































@JsxClass
public class MimeTypeArray
  extends SimpleArray
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public MimeTypeArray() {}
  
  protected String getItemName(Object element)
  {
    return ((MimeType)element).getType();
  }
}
