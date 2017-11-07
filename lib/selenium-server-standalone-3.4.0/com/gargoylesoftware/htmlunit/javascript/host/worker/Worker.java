package com.gargoylesoftware.htmlunit.javascript.host.worker;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventListenersContainer;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;






















@JsxClass
public class Worker
  extends EventTarget
{
  private final DedicatedWorkerGlobalScope workerScope_;
  
  public Worker()
  {
    workerScope_ = null;
  }
  
  private Worker(Context cx, Window owningWindow, String url) throws Exception {
    setParentScope(owningWindow);
    setPrototype(getPrototype(getClass()));
    
    WebClient webClient = getWindow().getWebWindow().getWebClient();
    workerScope_ = new DedicatedWorkerGlobalScope(owningWindow, cx, webClient.getBrowserVersion(), this);
    
    workerScope_.loadAndExecute(url, null);
  }
  









  @JsxConstructor
  public static Scriptable jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr)
    throws Exception
  {
    if ((args.length < 1) || (args.length > 2)) {
      throw Context.reportRuntimeError(
        "Worker Error: constructor must have one or two String parameters.");
    }
    
    String url = Context.toString(args[0]);
    
    return new Worker(cx, getWindow(ctorObj), url);
  }
  



  @JsxFunction
  public void postMessage(Object message)
  {
    workerScope_.messagePosted(message);
  }
  



  @JsxSetter
  public void setOnmessage(Object onmessage)
  {
    getEventListenersContainer().setEventHandlerProp("message", onmessage);
  }
  



  @JsxGetter
  public Object getOnmessage()
  {
    return getEventListenersContainer().getEventHandlerProp("message");
  }
}
