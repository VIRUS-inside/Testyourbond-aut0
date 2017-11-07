package com.gargoylesoftware.htmlunit.javascript.host.worker;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventListenersContainer;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;
import java.io.IOException;
import java.net.URL;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

























@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(className="WorkerGlobalScope", browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})})
public class DedicatedWorkerGlobalScope
  extends HtmlUnitScriptable
{
  private static final Log LOG = LogFactory.getLog(DedicatedWorkerGlobalScope.class);
  
  private final Window owningWindow_;
  
  private final String origin_;
  
  private final Worker worker_;
  
  public DedicatedWorkerGlobalScope()
  {
    owningWindow_ = null;
    origin_ = null;
    worker_ = null;
  }
  





  DedicatedWorkerGlobalScope(Window owningWindow, Context context, BrowserVersion browserVersion, Worker worker)
    throws Exception
  {
    context.initStandardObjects(this);
    
    ClassConfiguration config = AbstractJavaScriptConfiguration.getClassConfiguration(
      DedicatedWorkerGlobalScope.class, browserVersion);
    HtmlUnitScriptable prototype = JavaScriptEngine.configureClass(config, null, browserVersion);
    setPrototype(prototype);
    
    owningWindow_ = owningWindow;
    URL currentURL = owningWindow.getWebWindow().getEnclosedPage().getUrl();
    origin_ = (currentURL.getProtocol() + "://" + currentURL.getHost() + ':' + currentURL.getPort());
    
    worker_ = worker;
  }
  



  @JsxGetter
  public Object getSelf()
  {
    return this;
  }
  



  @JsxFunction
  public void postMessage(Object message)
  {
    final MessageEvent event = new MessageEvent();
    event.initMessageEvent("message", false, false, message, origin_, "", owningWindow_, null);
    event.setParentScope(owningWindow_);
    event.setPrototype(owningWindow_.getPrototype(event.getClass()));
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("[DedicatedWorker] postMessage: {}" + message);
    }
    JavaScriptEngine jsEngine = owningWindow_.getWebWindow().getWebClient().getJavaScriptEngine();
    ContextAction action = new ContextAction()
    {
      public Object run(Context cx) {
        worker_.getEventListenersContainer().executeCapturingListeners(event, null);
        Object[] args = { event };
        return worker_.getEventListenersContainer().executeBubblingListeners(event, args, args);
      }
      
    };
    ContextFactory cf = jsEngine.getContextFactory();
    
    JavaScriptJob job = new WorkerJob(cf, action, "postMessage: " + Context.toString(message));
    
    HtmlPage page = (HtmlPage)owningWindow_.getDocument().getPage();
    owningWindow_.getWebWindow().getJobManager().addJob(job, page);
  }
  
  void messagePosted(Object message) {
    final MessageEvent event = new MessageEvent();
    event.initMessageEvent("message", false, false, message, origin_, "", owningWindow_, null);
    event.setParentScope(owningWindow_);
    event.setPrototype(owningWindow_.getPrototype(event.getClass()));
    
    JavaScriptEngine jsEngine = owningWindow_.getWebWindow().getWebClient().getJavaScriptEngine();
    ContextAction action = new ContextAction()
    {
      public Object run(Context cx) {
        return DedicatedWorkerGlobalScope.this.executeEvent(cx, event);
      }
      
    };
    ContextFactory cf = jsEngine.getContextFactory();
    
    JavaScriptJob job = new WorkerJob(cf, action, "messagePosted: " + Context.toString(message));
    
    HtmlPage page = (HtmlPage)owningWindow_.getDocument().getPage();
    owningWindow_.getWebWindow().getJobManager().addJob(job, page);
  }
  
  private Object executeEvent(Context cx, MessageEvent event) {
    Object handler = get("onmessage", this);
    
    if ((handler != null) && ((handler instanceof Function))) {
      Function handlerFunction = (Function)handler;
      Object[] args = { event };
      handlerFunction.call(cx, this, this, args);
    }
    return null;
  }
  







  @JsxFunction
  public static void importScripts(Context cx, Scriptable thisObj, Object[] args, Function funObj)
    throws IOException
  {
    DedicatedWorkerGlobalScope scope = (DedicatedWorkerGlobalScope)thisObj;
    
    for (Object arg : args) {
      String url = Context.toString(arg);
      scope.loadAndExecute(url, cx);
    }
  }
  
  void loadAndExecute(String url, Context context) throws IOException {
    final HtmlPage page = (HtmlPage)owningWindow_.getDocument().getPage();
    final URL fullUrl = page.getFullyQualifiedUrl(url);
    
    final WebClient webClient = owningWindow_.getWebWindow().getWebClient();
    
    WebRequest webRequest = new WebRequest(fullUrl);
    WebResponse response = webClient.loadWebResponse(webRequest);
    final String scriptCode = response.getContentAsString();
    final JavaScriptEngine javaScriptEngine = webClient.getJavaScriptEngine();
    
    final DedicatedWorkerGlobalScope thisScope = this;
    ContextAction action = new ContextAction()
    {
      public Object run(Context cx) {
        Script script = javaScriptEngine.compile(page, thisScope, scriptCode, 
          fullUrl.toExternalForm(), 1);
        return webClient.getJavaScriptEngine().execute(page, thisScope, script);
      }
      
    };
    ContextFactory cf = javaScriptEngine.getContextFactory();
    
    if (context != null) {
      action.run(context);
    }
    else {
      JavaScriptJob job = new WorkerJob(cf, action, "loadAndExecute " + url);
      
      owningWindow_.getWebWindow().getJobManager().addJob(job, page);
    }
  }
}
