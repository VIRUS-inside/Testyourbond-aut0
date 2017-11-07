package com.gargoylesoftware.htmlunit.javascript.host.media.rtc;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class webkitRTCPeerConnection
  extends EventTarget
{
  @JsxConstructor
  public webkitRTCPeerConnection() {}
}
