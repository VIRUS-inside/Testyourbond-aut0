package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;
import java.net.URI;





































@JsxClass
public class URL
  extends SimpleScriptable
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public URL() {}
  
  @JsxStaticFunction
  public static String createObjectURL(Object fileOrBlob)
  {
    if ((fileOrBlob instanceof com.gargoylesoftware.htmlunit.javascript.host.file.File)) {
      com.gargoylesoftware.htmlunit.javascript.host.file.File file = (com.gargoylesoftware.htmlunit.javascript.host.file.File)fileOrBlob;
      return file.getFile().toURI().normalize().toString();
    }
    
    return null;
  }
  
  @JsxStaticFunction
  public static void revokeObjectURL(Object objectURL) {}
}
