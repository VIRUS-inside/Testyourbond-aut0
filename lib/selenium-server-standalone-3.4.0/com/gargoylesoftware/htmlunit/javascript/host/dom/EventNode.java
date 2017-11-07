package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Function;




























@JsxClass(isJSObject=false)
public class EventNode
  extends Node
{
  public EventNode() {}
  
  @JsxSetter
  public void setOnclick(Object handler)
  {
    setEventHandlerProp("onclick", handler);
  }
  



  @JsxGetter
  public Object getOnclick()
  {
    return getEventHandlerProp("onclick");
  }
  



  @JsxSetter
  public void setOndblclick(Object handler)
  {
    setEventHandlerProp("ondblclick", handler);
  }
  



  @JsxGetter
  public Object getOndblclick()
  {
    return getEventHandlerProp("ondblclick");
  }
  



  @JsxSetter
  public void setOnblur(Object handler)
  {
    setEventHandlerProp("onblur", handler);
  }
  



  @JsxGetter
  public Object getOnblur()
  {
    return getEventHandlerProp("onblur");
  }
  



  @JsxSetter
  public void setOnfocus(Object handler)
  {
    setEventHandlerProp("onfocus", handler);
  }
  



  @JsxGetter
  public Object getOnfocus()
  {
    return getEventHandlerProp("onfocus");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setOnfocusin(Object handler)
  {
    setEventHandlerProp("onfocusin", handler);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object getOnfocusin()
  {
    return getEventHandlerProp("onfocusin");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setOnfocusout(Object handler)
  {
    setEventHandlerProp("onfocusout", handler);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object getOnfocusout()
  {
    return getEventHandlerProp("onfocusout");
  }
  



  @JsxSetter
  public void setOnkeydown(Object handler)
  {
    setEventHandlerProp("onkeydown", handler);
  }
  



  @JsxGetter
  public Object getOnkeydown()
  {
    return getEventHandlerProp("onkeydown");
  }
  



  @JsxSetter
  public void setOnkeypress(Object handler)
  {
    setEventHandlerProp("onkeypress", handler);
  }
  



  @JsxGetter
  public Object getOnkeypress()
  {
    return getEventHandlerProp("onkeypress");
  }
  



  @JsxSetter
  public void setOnkeyup(Object handler)
  {
    setEventHandlerProp("onkeyup", handler);
  }
  



  @JsxGetter
  public Object getOnkeyup()
  {
    return getEventHandlerProp("onkeyup");
  }
  



  @JsxSetter
  public void setOnmousedown(Object handler)
  {
    setEventHandlerProp("onmousedown", handler);
  }
  



  @JsxGetter
  public Object getOnmousedown()
  {
    return getEventHandlerProp("onmousedown");
  }
  



  @JsxSetter
  public void setOnmousemove(Object handler)
  {
    setEventHandlerProp("onmousemove", handler);
  }
  



  @JsxGetter
  public Object getOnmousemove()
  {
    return getEventHandlerProp("onmousemove");
  }
  



  @JsxSetter
  public void setOnmouseout(Object handler)
  {
    setEventHandlerProp("onmouseout", handler);
  }
  



  @JsxGetter
  public Object getOnmouseout()
  {
    return getEventHandlerProp("onmouseout");
  }
  



  @JsxSetter
  public void setOnmouseover(Object handler)
  {
    setEventHandlerProp("onmouseover", handler);
  }
  



  @JsxGetter
  public Object getOnmouseover()
  {
    return getEventHandlerProp("onmouseover");
  }
  



  @JsxSetter
  public void setOnmouseup(Object handler)
  {
    setEventHandlerProp("onmouseup", handler);
  }
  



  @JsxGetter
  public Object getOnmouseup()
  {
    return getEventHandlerProp("onmouseup");
  }
  



  @JsxSetter
  public void setOncontextmenu(Object handler)
  {
    setEventHandlerProp("oncontextmenu", handler);
  }
  



  @JsxGetter
  public Object getOncontextmenu()
  {
    return getEventHandlerProp("oncontextmenu");
  }
  



  @JsxSetter
  public void setOnresize(Object handler)
  {
    setEventHandlerProp("onresize", handler);
  }
  



  @JsxGetter
  public Object getOnresize()
  {
    return getEventHandlerProp("onresize");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setOnpropertychange(Object handler)
  {
    setEventHandlerProp("onpropertychange", handler);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object getOnpropertychange()
  {
    return getEventHandlerProp("onpropertychange");
  }
  



  @JsxSetter
  public void setOnerror(Object handler)
  {
    setEventHandlerProp("onerror", handler);
  }
  



  @JsxGetter
  public Object getOnerror()
  {
    return getEventHandlerProp("onerror");
  }
  



  @JsxGetter
  public Function getOninput()
  {
    return getEventHandler("oninput");
  }
  



  @JsxSetter
  public void setOninput(Object onchange)
  {
    setEventHandlerProp("oninput", onchange);
  }
}
