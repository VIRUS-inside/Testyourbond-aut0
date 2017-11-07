package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.WindowProxy;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;


































@JsxClass
public class MessageEvent
  extends Event
{
  private Object data_;
  private String origin_;
  private String lastEventId_;
  private Window source_;
  private Object ports_;
  
  public MessageEvent()
  {
    setType("message");
    origin_ = "";
    lastEventId_ = "";
    data_ = Undefined.instance;
  }
  



  public MessageEvent(Object data)
  {
    this();
    data_ = data;
  }
  






  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public void jsConstructor(String type, ScriptableObject details)
  {
    super.jsConstructor(type, details);
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONMESSAGE_DEFAULT_DATA_NULL)) {
      data_ = null;
    }
    
    String origin = "";
    String lastEventId = "";
    if ((details != null) && (!Undefined.instance.equals(details))) {
      data_ = details.get("data");
      
      String detailOrigin = (String)details.get("origin");
      if (detailOrigin != null) {
        origin = detailOrigin;
      }
      
      Object detailLastEventId = details.get("lastEventId");
      if (detailLastEventId != null) {
        lastEventId = Context.toString(detailLastEventId);
      }
      
      source_ = null;
      Object detailSource = details.get("source");
      if ((detailSource instanceof Window)) {
        source_ = ((Window)detailSource);
      }
      else if ((detailSource instanceof WindowProxy)) {
        source_ = ((WindowProxy)detailSource).getDelegee();
      }
      ports_ = details.get("ports");
    }
    origin_ = origin;
    lastEventId_ = lastEventId;
  }
  


















  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void initMessageEvent(String type, boolean canBubble, boolean cancelable, Object data, String origin, String lastEventId, Window source, Object ports)
  {
    initEvent(type, canBubble, cancelable);
    data_ = data;
    origin_ = origin;
    lastEventId_ = lastEventId;
    source_ = source;
    ports_ = ports;
  }
  



  @JsxGetter
  public Object getData()
  {
    return data_;
  }
  



  @JsxGetter
  public String getOrigin()
  {
    return origin_;
  }
  



  public void setOrigin(String origin)
  {
    origin_ = origin;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public String getLastEventId()
  {
    return lastEventId_;
  }
  



  @JsxGetter
  public Window getSource()
  {
    return source_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public Object getPorts()
  {
    return ports_;
  }
}
