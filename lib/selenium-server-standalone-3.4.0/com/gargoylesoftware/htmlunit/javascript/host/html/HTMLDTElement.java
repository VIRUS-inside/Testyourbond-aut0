package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
























@JsxClass(domClass=HtmlDefinitionTerm.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
public class HTMLDTElement
  extends HTMLElement
{
  public HTMLDTElement() {}
  
  @JsxGetter
  public boolean getNoWrap()
  {
    return getDomNodeOrDie().hasAttribute("noWrap");
  }
  




  @JsxSetter
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
