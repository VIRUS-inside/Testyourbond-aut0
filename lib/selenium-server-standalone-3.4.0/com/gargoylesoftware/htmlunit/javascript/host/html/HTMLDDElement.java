package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
























@JsxClass(domClass=HtmlDefinitionDescription.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class HTMLDDElement
  extends HTMLElement
{
  public HTMLDDElement() {}
  
  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getNoWrap()
  {
    return getDomNodeOrDie().hasAttribute("noWrap");
  }
  




  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setNoWrap(boolean noWrap)
  {
    if (noWrap) {
      getDomNodeOrDie().setAttribute("noWrap", "");
    }
    else {
      getDomNodeOrDie().removeAttribute("noWrap");
    }
  }
}
