package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOutput;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;































@JsxClass(domClass=HtmlOutput.class, browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
public class HTMLOutputElement
  extends HTMLElement
{
  private AbstractList labels_;
  
  @JsxConstructor
  public HTMLOutputElement() {}
  
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
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME)})
  public AbstractList getLabels()
  {
    if (labels_ == null) {
      labels_ = new LabelsHelper(getDomNodeOrDie());
    }
    return labels_;
  }
}
