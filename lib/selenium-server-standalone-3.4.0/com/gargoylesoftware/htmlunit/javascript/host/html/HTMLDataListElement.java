package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlDataList;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
































@JsxClass(domClass=HtmlDataList.class)
public class HTMLDataListElement
  extends HTMLElement
{
  private HTMLCollection options_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLDataListElement() {}
  
  @JsxGetter
  public Object getOptions()
  {
    if (options_ == null) {
      options_ = new HTMLCollection(getDomNodeOrDie(), false)
      {
        protected boolean isMatching(DomNode node) {
          return node instanceof HtmlOption;
        }
      };
    }
    return options_;
  }
}
