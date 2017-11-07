package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventListenersContainer;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;









































@JsxClasses({@com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)}), @com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass(className="OfflineResourceList", browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})})
public class ApplicationCache
  extends EventTarget
{
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public static final short UNCACHED = 0;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public static final short IDLE = 1;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public static final short CHECKING = 2;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public static final short DOWNLOADING = 3;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public static final short UPDATEREADY = 4;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public static final short OBSOLETE = 5;
  private short status_ = 0;
  




  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public ApplicationCache() {}
  



  @JsxGetter
  public Object getOnchecking()
  {
    return getHandlerForJavaScript("checking");
  }
  



  @JsxSetter
  public void setOnchecking(Object o)
  {
    setHandlerForJavaScript("checking", o);
  }
  



  @JsxGetter
  public Object getOnerror()
  {
    return getHandlerForJavaScript("error");
  }
  



  @JsxSetter
  public void setOnerror(Object o)
  {
    setHandlerForJavaScript("error", o);
  }
  



  @JsxGetter
  public Object getOnnoupdate()
  {
    return getHandlerForJavaScript("update");
  }
  



  @JsxSetter
  public void setOnnoupdate(Object o)
  {
    setHandlerForJavaScript("update", o);
  }
  



  @JsxGetter
  public Object getOndownloading()
  {
    return getHandlerForJavaScript("downloading");
  }
  



  @JsxSetter
  public void setOndownloading(Object o)
  {
    setHandlerForJavaScript("downloading", o);
  }
  



  @JsxGetter
  public Object getOnprogress()
  {
    return getHandlerForJavaScript("progress");
  }
  



  @JsxSetter
  public void setOnprogress(Object o)
  {
    setHandlerForJavaScript("progress", o);
  }
  



  @JsxGetter
  public Object getOnupdateready()
  {
    return getHandlerForJavaScript("updateready");
  }
  



  @JsxSetter
  public void setOnupdateready(Object o)
  {
    setHandlerForJavaScript("updateready", o);
  }
  



  @JsxGetter
  public Object getOncached()
  {
    return getHandlerForJavaScript("cached");
  }
  



  @JsxSetter
  public void setOncached(Object o)
  {
    setHandlerForJavaScript("cached", o);
  }
  









  @JsxFunction
  public boolean dispatchEvent(Event event)
  {
    event.setTarget(this);
    ScriptResult result = fireEvent(event);
    return !event.isAborted(result);
  }
  
  private Object getHandlerForJavaScript(String eventName) {
    return getEventListenersContainer().getEventHandlerProp(eventName);
  }
  
  private void setHandlerForJavaScript(String eventName, Object handler) {
    if ((handler == null) || ((handler instanceof Scriptable))) {
      getEventListenersContainer().setEventHandlerProp(eventName, handler);
    }
  }
  




  @JsxGetter
  public short getStatus()
  {
    return status_;
  }
  



  @JsxGetter
  public int getLength()
  {
    return 0;
  }
  





  @JsxFunction
  public void add(String uri) {}
  





  @JsxFunction
  public boolean hasItem(String uri)
  {
    return false;
  }
  




  @JsxFunction
  public String item(int index)
  {
    return null;
  }
  
  @JsxFunction
  public void remove(String uri) {}
  
  @JsxFunction
  public void swapCache() {}
  
  @JsxFunction
  public void update() {}
}
