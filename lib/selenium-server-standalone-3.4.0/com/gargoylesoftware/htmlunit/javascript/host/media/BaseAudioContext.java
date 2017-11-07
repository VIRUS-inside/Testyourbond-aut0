package com.gargoylesoftware.htmlunit.javascript.host.media;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(isJSObject=false, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})})
public class BaseAudioContext
  extends EventTarget
{
  @JsxConstructor
  public BaseAudioContext() {}
}
