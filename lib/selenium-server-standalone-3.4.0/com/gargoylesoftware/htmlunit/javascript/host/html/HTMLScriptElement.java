package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import java.net.MalformedURLException;
import java.net.URL;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;






































@JsxClass(domClass=HtmlScript.class)
public class HTMLScriptElement
  extends HTMLElement
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLScriptElement() {}
  
  @JsxGetter
  public String getSrc()
  {
    HtmlScript tmpScript = (HtmlScript)getDomNodeOrDie();
    String src = tmpScript.getSrcAttribute();
    if (DomElement.ATTRIBUTE_NOT_DEFINED == src) {
      return src;
    }
    try {
      URL expandedSrc = ((HtmlPage)tmpScript.getPage()).getFullyQualifiedUrl(src);
      src = expandedSrc.toString();
    }
    catch (MalformedURLException localMalformedURLException) {}
    

    return src;
  }
  



  @JsxSetter
  public void setSrc(String src)
  {
    getDomNodeOrDie().setAttribute("src", src);
  }
  



  @JsxGetter
  public String getText()
  {
    StringBuilder scriptCode = new StringBuilder();
    for (DomNode node : getDomNodeOrDie().getChildren()) {
      if ((node instanceof DomText)) {
        DomText domText = (DomText)node;
        scriptCode.append(domText.getData());
      }
    }
    return scriptCode.toString();
  }
  



  @JsxSetter
  public void setText(String text)
  {
    HtmlElement htmlElement = getDomNodeOrDie();
    htmlElement.removeAllChildren();
    DomNode textChild = new DomText(htmlElement.getPage(), text);
    htmlElement.appendChild(textChild);
    
    HtmlScript tmpScript = (HtmlScript)htmlElement;
    tmpScript.executeScriptIfNeeded();
  }
  



  @JsxGetter
  public String getType()
  {
    return getDomNodeOrDie().getAttribute("type");
  }
  



  @JsxSetter
  public void setType(String type)
  {
    getDomNodeOrDie().setAttribute("type", type);
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object getOnreadystatechange()
  {
    return getEventHandlerProp("onreadystatechange");
  }
  



  @JsxSetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public void setOnreadystatechange(Object handler)
  {
    setEventHandlerProp("onreadystatechange", handler);
  }
  



  @JsxGetter
  public Object getOnload()
  {
    return getEventHandlerProp("onload");
  }
  



  @JsxSetter
  public void setOnload(Object handler)
  {
    setEventHandlerProp("onload", handler);
  }
  








  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object getReadyState()
  {
    HtmlScript tmpScript = (HtmlScript)getDomNodeOrDie();
    if (tmpScript.wasCreatedByJavascript()) {
      return Undefined.instance;
    }
    return tmpScript.getReadyState();
  }
  






  public Object appendChild(Object childObject)
  {
    HtmlScript tmpScript = (HtmlScript)getDomNodeOrDie();
    boolean wasEmpty = tmpScript.getFirstChild() == null;
    Object result = super.appendChild(childObject);
    
    if (wasEmpty) {
      tmpScript.executeScriptIfNeeded();
    }
    return result;
  }
  



  @JsxGetter
  public boolean getAsync()
  {
    return getDomNodeOrDie().hasAttribute("async");
  }
  



  @JsxSetter
  public void setAsync(boolean async)
  {
    if (async) {
      getDomNodeOrDie().setAttribute("async", "");
    }
    else {
      getDomNodeOrDie().removeAttribute("async");
    }
  }
}
