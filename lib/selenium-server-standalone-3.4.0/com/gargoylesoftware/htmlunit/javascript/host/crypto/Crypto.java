package com.gargoylesoftware.htmlunit.javascript.host.crypto;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferViewBase;
import java.util.Random;
import net.sourceforge.htmlunit.corejs.javascript.Context;


































@JsxClass
public class Crypto
  extends SimpleScriptable
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Crypto() {}
  
  public Crypto(Window window)
  {
    setParentScope(window);
    setPrototype(window.getPrototype(Crypto.class));
  }
  




  @JsxFunction
  public void getRandomValues(ArrayBufferViewBase array)
  {
    if (array == null) {
      throw Context.reportRuntimeError("TypeError: Argument 1 of Crypto.getRandomValues is not an object.");
    }
    
    Random random = new Random();
    for (int i = 0; i < array.getLength(); i++) {
      array.put(i, array, Integer.valueOf(random.nextInt()));
    }
  }
}
