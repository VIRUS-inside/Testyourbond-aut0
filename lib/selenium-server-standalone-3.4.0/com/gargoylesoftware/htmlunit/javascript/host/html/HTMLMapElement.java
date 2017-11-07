package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlMap;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import java.util.ArrayList;
import java.util.List;
































@JsxClass(domClass=HtmlMap.class)
public class HTMLMapElement
  extends HTMLElement
{
  private HTMLCollection areas_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public HTMLMapElement() {}
  
  @JsxGetter
  public HTMLCollection getAreas()
  {
    if (areas_ == null) {
      final HtmlMap map = (HtmlMap)getDomNodeOrDie();
      areas_ = new HTMLCollection(map, false)
      {
        protected List<Object> computeElements() {
          List<Object> list = new ArrayList();
          for (DomNode node : map.getChildElements()) {
            if ((node instanceof HtmlArea)) {
              list.add(node);
            }
          }
          return list;
        }
      };
    }
    return areas_;
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
}
