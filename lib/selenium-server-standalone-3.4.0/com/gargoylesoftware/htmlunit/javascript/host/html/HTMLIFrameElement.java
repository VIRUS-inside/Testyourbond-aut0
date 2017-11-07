package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInlineFrame;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.WindowProxy;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;

































@JsxClass(domClass=HtmlInlineFrame.class)
public class HTMLIFrameElement
  extends HTMLElement
{
  private boolean isAttachedToPageDuringOnload_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLIFrameElement() {}
  
  @JsxGetter
  public String getSrc()
  {
    return getFrame().getSrcAttribute();
  }
  



  @JsxSetter
  public void setSrc(String src)
  {
    getFrame().setSrcAttribute(src);
    isAttachedToPageDuringOnload_ = false;
  }
  




  @JsxGetter
  public DocumentProxy getContentDocument()
  {
    return ((Window)getFrame().getEnclosedWindow().getScriptableObject()).getDocument_js();
  }
  





  @JsxGetter
  public WindowProxy getContentWindow()
  {
    return Window.getProxy(getFrame().getEnclosedWindow());
  }
  



  @JsxGetter
  public String getName()
  {
    return getFrame().getNameAttribute();
  }
  



  @JsxSetter
  public void setName(String name)
  {
    getFrame().setNameAttribute(name);
  }
  
  private BaseFrameElement getFrame() {
    return (BaseFrameElement)getDomNodeOrDie();
  }
  



  @JsxSetter
  public void setOnload(Object eventHandler)
  {
    setEventHandlerProp("onload", eventHandler);
    isAttachedToPageDuringOnload_ = getDomNodeOrDie().isAttachedToPage();
  }
  



  @JsxGetter
  public Object getOnload()
  {
    return getEventHandlerProp("onload");
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getBorder()
  {
    String border = getDomNodeOrDie().getAttribute("border");
    return border;
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setBorder(String border)
  {
    getDomNodeOrDie().setAttribute("border", border);
  }
  



  @JsxGetter
  public String getAlign()
  {
    return getAlign(true);
  }
  



  @JsxSetter
  public void setAlign(String align)
  {
    setAlign(align, false);
  }
  



  @JsxGetter(propertyName="width")
  public String getWidth_js()
  {
    return getWidthOrHeight("width", Boolean.TRUE);
  }
  



  @JsxSetter
  public void setWidth(String width)
  {
    setWidthOrHeight("width", width, true);
  }
  



  @JsxGetter(propertyName="height")
  public String getHeight_js()
  {
    return getWidthOrHeight("height", Boolean.TRUE);
  }
  



  @JsxSetter
  public void setHeight(String height)
  {
    setWidthOrHeight("height", height, true);
  }
  



  public ScriptResult executeEventLocally(Event event)
  {
    if ((!isAttachedToPageDuringOnload_) || (getBrowserVersion().hasFeature(BrowserVersionFeatures.JS_IFRAME_ALWAYS_EXECUTE_ONLOAD))) {
      return super.executeEventLocally(event);
    }
    return null;
  }
  


  public void onRefresh()
  {
    isAttachedToPageDuringOnload_ = false;
  }
}
