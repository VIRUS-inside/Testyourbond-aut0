package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStream;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
public class CanvasCaptureMediaStream
  extends MediaStream
{
  @JsxConstructor
  public CanvasCaptureMediaStream() {}
}
