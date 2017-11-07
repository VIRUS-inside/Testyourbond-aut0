package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlBase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Location;
import com.gargoylesoftware.htmlunit.javascript.host.Window;




























@JsxClass(domClass=HtmlBase.class)
public class HTMLBaseElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLBaseElement() {}
  
  @JsxGetter
  public String getHref()
  {
    String href = getDomNodeOrDie().getAttribute("href");
    if (DomElement.ATTRIBUTE_NOT_DEFINED == href) {
      if (getBrowserVersion().hasFeature(BrowserVersionFeatures.HTMLBASE_HREF_DEFAULT_EMPTY)) {
        return href;
      }
      return getWindow().getLocation().getHref();
    }
    return href;
  }
  



  @JsxSetter
  public void setHref(String href)
  {
    getDomNodeOrDie().setAttribute("href", href);
  }
  



  @JsxGetter
  public String getTarget()
  {
    return getDomNodeOrDie().getAttribute("target");
  }
  



  @JsxSetter
  public void setTarget(String target)
  {
    getDomNodeOrDie().setAttribute("target", target);
  }
  



  protected boolean isEndTagForbidden()
  {
    return true;
  }
}
