package com.gargoylesoftware.htmlunit.javascript.host.speech;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class webkitSpeechGrammar
  extends SimpleScriptable
{
  @JsxConstructor
  public webkitSpeechGrammar() {}
}
