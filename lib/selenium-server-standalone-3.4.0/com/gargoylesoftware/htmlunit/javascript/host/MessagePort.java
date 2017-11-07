package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventListenersContainer;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;
import java.net.URL;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;






























@JsxClass
public class MessagePort
  extends EventTarget
{
  private MessagePort port1_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public MessagePort() {}
  
  public MessagePort(MessagePort port1)
  {
    port1_ = port1;
  }
  



  @JsxGetter
  public Object getOnmessage()
  {
    return getHandlerForJavaScript("message");
  }
  



  @JsxSetter
  public void setOnmessage(Object onmessage)
  {
    setHandlerForJavaScript("message", onmessage);
  }
  
  private Object getHandlerForJavaScript(String eventName) {
    return getEventListenersContainer().getEventHandlerProp(eventName);
  }
  
  private void setHandlerForJavaScript(String eventName, Object handler) {
    if ((handler == null) || ((handler instanceof Function))) {
      getEventListenersContainer().setEventHandlerProp(eventName, handler);
    }
  }
  






  @JsxFunction
  public void postMessage(String message, Object transfer)
  {
    if (port1_ != null) {
      URL currentURL = getWindow().getWebWindow().getEnclosedPage().getUrl();
      final MessageEvent event = new MessageEvent();
      String origin = currentURL.getProtocol() + "://" + currentURL.getHost() + ':' + currentURL.getPort();
      event.initMessageEvent("message", false, false, message, origin, "", getWindow(), transfer);
      event.setParentScope(port1_);
      event.setPrototype(getPrototype(event.getClass()));
      
      final JavaScriptEngine jsEngine = getWindow().getWebWindow().getWebClient().getJavaScriptEngine();
      PostponedAction action = new PostponedAction(getWindow().getWebWindow().getEnclosedPage())
      {
        public void execute() throws Exception {
          ContextAction contextAction = new ContextAction()
          {
            public Object run(Context cx) {
              return Boolean.valueOf(port1_.dispatchEvent(val$event));
            }
            
          };
          ContextFactory cf = jsEngine.getContextFactory();
          cf.call(contextAction);
        }
      };
      jsEngine.addPostponedAction(action);
    }
  }
}
