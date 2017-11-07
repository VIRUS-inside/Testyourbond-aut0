package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;





























@JsxClass
public class CloseEvent
  extends Event
{
  private String reason_;
  private int code_;
  private boolean wasClean_;
  
  public CloseEvent()
  {
    setType("close");
    reason_ = "";
  }
  



  public void eventCreated()
  {
    super.eventCreated();
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONCLOSE_DEFAULT_TYPE_EMPTY)) {
      setType("");
    }
  }
  






  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public void jsConstructor(String type, ScriptableObject details)
  {
    super.jsConstructor(type, details);
    
    int code = 0;
    String reason = "";
    boolean wasClean = false;
    
    if ((details != null) && (!Undefined.instance.equals(details))) {
      Double detailCode = (Double)details.get("code");
      if (detailCode != null) {
        code = detailCode.intValue();
      }
      
      String detailReason = (String)details.get("reason");
      if (detailReason != null) {
        reason = detailReason;
      }
      
      Boolean detailWasClean = (Boolean)details.get("wasClean");
      if (detailWasClean != null) {
        wasClean = detailWasClean.booleanValue();
      }
    }
    code_ = code;
    reason_ = reason;
    wasClean_ = wasClean;
  }
  









  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void initCloseEvent(String type, boolean bubbles, boolean cancelable, boolean wasClean, int reasonCode, String reason)
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONCLOSE_INIT_CLOSE_EVENT_THROWS)) {
      Context.throwAsScriptRuntimeEx(new IllegalArgumentException("Illegal call to initCloseEvent()"));
    }
    super.initEvent(type, bubbles, cancelable);
    wasClean_ = wasClean;
    code_ = reasonCode;
    reason_ = reason;
  }
  


  @JsxGetter
  public int getCode()
  {
    return code_;
  }
  


  public void setCode(int code)
  {
    code_ = code;
  }
  


  @JsxGetter
  public String getReason()
  {
    return reason_;
  }
  


  public void setReason(String reason)
  {
    reason_ = reason;
  }
  


  @JsxGetter
  public boolean getWasClean()
  {
    return wasClean_;
  }
  


  public void setWasClean(boolean wasClean)
  {
    wasClean_ = wasClean;
  }
}
