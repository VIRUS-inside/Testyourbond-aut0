package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;































@JsxClass(domClass=HtmlMeta.class)
public class HTMLMetaElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLMetaElement() {}
  
  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getCharset()
  {
    return "";
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setCharset(String charset) {}
  




  @JsxGetter
  public String getContent()
  {
    return getDomNodeOrDie().getAttribute("content");
  }
  



  @JsxSetter
  public void setContent(String content)
  {
    getDomNodeOrDie().setAttribute("content", content);
  }
  



  @JsxGetter
  public String getHttpEquiv()
  {
    return getDomNodeOrDie().getAttribute("http-equiv");
  }
  



  @JsxSetter
  public void setHttpEquiv(String httpEquiv)
  {
    getDomNodeOrDie().setAttribute("http-equiv", httpEquiv);
  }
  



  @JsxGetter
  public String getName()
  {
    return getDomNodeOrDie().getAttribute("name");
  }
  



  @JsxSetter
  public void setName(String name)
  {
    getDomNodeOrDie().setAttribute("name", name);
  }
  



  @JsxGetter
  public String getScheme()
  {
    return getDomNodeOrDie().getAttribute("scheme");
  }
  



  @JsxSetter
  public void setScheme(String scheme)
  {
    getDomNodeOrDie().setAttribute("scheme", scheme);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public String getUrl()
  {
    return "";
  }
  





  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setUrl(String url) {}
  




  protected boolean isEndTagForbidden()
  {
    return true;
  }
}
