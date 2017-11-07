package com.gargoylesoftware.htmlunit.javascript.host.security;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class PasswordCredential
  extends SiteBoundCredential
{
  @JsxConstructor
  public PasswordCredential() {}
}
