package com.gargoylesoftware.htmlunit.javascript.host.media;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class LocalMediaStream
  extends MediaStream
{
  @JsxConstructor
  public LocalMediaStream() {}
}
