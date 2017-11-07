package com.gargoylesoftware.htmlunit.javascript.host.worker;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class ServiceWorkerRegistration
  extends EventTarget
{
  @JsxConstructor
  public ServiceWorkerRegistration() {}
}
