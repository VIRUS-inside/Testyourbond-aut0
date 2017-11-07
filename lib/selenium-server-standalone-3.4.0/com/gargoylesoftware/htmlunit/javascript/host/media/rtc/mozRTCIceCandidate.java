package com.gargoylesoftware.htmlunit.javascript.host.media.rtc;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class mozRTCIceCandidate
  extends SimpleScriptable
{
  @JsxConstructor
  public mozRTCIceCandidate() {}
}
