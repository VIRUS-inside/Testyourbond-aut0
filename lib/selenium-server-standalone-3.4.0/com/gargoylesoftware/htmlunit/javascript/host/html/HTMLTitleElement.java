package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTitle;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
































@JsxClass(domClass=HtmlTitle.class)
public class HTMLTitleElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLTitleElement() {}
  
  @JsxGetter
  public Object getText()
  {
    DomNode firstChild = getDomNodeOrDie().getFirstChild();
    if (firstChild != null) {
      return firstChild.getNodeValue();
    }
    return "";
  }
  



  @JsxSetter
  public void setText(String text)
  {
    DomNode htmlElement = getDomNodeOrDie();
    DomNode firstChild = htmlElement.getFirstChild();
    if (firstChild == null) {
      firstChild = new DomText(htmlElement.getPage(), text);
      htmlElement.appendChild(firstChild);
    }
    else {
      firstChild.setNodeValue(text);
    }
  }
}
