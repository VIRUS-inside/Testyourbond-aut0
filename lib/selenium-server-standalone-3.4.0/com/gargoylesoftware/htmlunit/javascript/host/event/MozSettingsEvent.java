package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(value=com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF, maxVersion=45)})
public class MozSettingsEvent
  extends Event
{
  @JsxConstructor
  public MozSettingsEvent() {}
}
