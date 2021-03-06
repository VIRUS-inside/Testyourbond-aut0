package com.gargoylesoftware.htmlunit.javascript.host.media.midi;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class MIDIOutputMap
  extends SimpleScriptable
{
  @JsxConstructor
  public MIDIOutputMap() {}
}
