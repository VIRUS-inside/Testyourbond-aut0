package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.BaseFrameElement;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFrame;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.WindowProxy;

































@JsxClass(domClass=HtmlFrame.class)
public class HTMLFrameElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLFrameElement() {}
  
  @JsxGetter
  public String getSrc()
  {
    return getFrame().getSrcAttribute();
  }
  



  @JsxSetter
  public void setSrc(String src)
  {
    getFrame().setSrcAttribute(src);
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
  



  protected boolean isEndTagForbidden()
  {
    return true;
  }
}
