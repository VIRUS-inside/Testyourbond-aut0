package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

































@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class MessageChannel
  extends SimpleScriptable
{
  private MessagePort port1_;
  private MessagePort port2_;
  
  @JsxConstructor
  public MessageChannel() {}
  
  @JsxGetter
  public MessagePort getPort1()
  {
    if (port1_ == null) {
      port1_ = new MessagePort();
      port1_.setParentScope(getParentScope());
      port1_.setPrototype(getPrototype(port1_.getClass()));
    }
    return port1_;
  }
  



  @JsxGetter
  public MessagePort getPort2()
  {
    if (port2_ == null) {
      port2_ = new MessagePort(getPort1());
      port2_.setParentScope(getParentScope());
      port2_.setPrototype(getPrototype(port2_.getClass()));
    }
    return port2_;
  }
}
