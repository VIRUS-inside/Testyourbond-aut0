package com.gargoylesoftware.htmlunit.javascript.host.event;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;













































@JsxClass
public class UIEvent
  extends Event
{
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final int SCROLL_PAGE_DOWN = 32768;
  @JsxConstant({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF)})
  public static final short SCROLL_PAGE_UP = -32768;
  private long detail_;
  private boolean metaKey_;
  private boolean cancelBubble_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public UIEvent() {}
  
  public UIEvent(DomNode domNode, String type)
  {
    super(domNode, type);
  }
  




  public UIEvent(EventTarget target, String type)
  {
    super(target, type);
  }
  






  @JsxGetter
  public long getDetail()
  {
    return detail_;
  }
  




  protected void setDetail(long detail)
  {
    detail_ = detail;
  }
  



  @JsxGetter
  public boolean getCancelBubble()
  {
    if (getBrowserVersion().hasFeature(BrowserVersionFeatures.EVENT_CANCEL_BUBBLE)) {
      return cancelBubble_;
    }
    return super.getCancelBubble();
  }
  



  @JsxSetter
  public void setCancelBubble(boolean newValue)
  {
    super.setCancelBubble(newValue);
    cancelBubble_ = newValue;
  }
  




  @JsxGetter
  public Object getView()
  {
    return getWindow();
  }
  













  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void initUIEvent(String type, boolean bubbles, boolean cancelable, Object view, int detail)
  {
    initEvent(type, bubbles, cancelable);
    
    setDetail(detail);
  }
  



  @JsxGetter
  public boolean getMetaKey()
  {
    return metaKey_;
  }
  


  protected void setMetaKey(boolean metaKey)
  {
    metaKey_ = metaKey;
  }
}
