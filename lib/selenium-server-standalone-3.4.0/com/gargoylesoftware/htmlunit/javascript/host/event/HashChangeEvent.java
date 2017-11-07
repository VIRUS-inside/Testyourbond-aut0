package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;




























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class HashChangeEvent
  extends Event
{
  private String oldURL_ = "";
  private String newURL_ = "";
  


  public HashChangeEvent()
  {
    setEventType("");
  }
  








  public HashChangeEvent(EventTarget target, String type, String oldURL, String newURL)
  {
    super(target, type);
    oldURL_ = oldURL;
    newURL_ = newURL;
    
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_ONHASHCHANGE_BUBBLES_FALSE)) {
      setBubbles(false);
    }
    setCancelable(false);
  }
  



  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public void jsConstructor(String type, ScriptableObject details)
  {
    super.jsConstructor(type, details);
    
    String oldURL = "";
    String newURL = "";
    if ((details != null) && (!Undefined.instance.equals(details))) {
      oldURL = (String)details.get("oldURL");
      newURL = (String)details.get("newURL");
    }
    oldURL_ = oldURL;
    newURL_ = newURL;
  }
  









  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public void initHashChangeEvent(String type, boolean bubbles, boolean cancelable, String oldURL, String newURL)
  {
    initEvent(type, bubbles, cancelable);
    oldURL_ = oldURL;
    newURL_ = newURL;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public Object getOldURL()
  {
    return oldURL_;
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public Object getNewURL()
  {
    return newURL_;
  }
}
