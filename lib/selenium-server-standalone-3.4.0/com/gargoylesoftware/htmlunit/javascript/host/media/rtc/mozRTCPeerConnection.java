package com.gargoylesoftware.htmlunit.javascript.host.media.rtc;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class mozRTCPeerConnection
  extends EventTarget
{
  @JsxConstructor
  public mozRTCPeerConnection() {}
}
