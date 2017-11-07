package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;







































@JsxClass(domClass=DomText.class)
public class Text
  extends CharacterData
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Text() {}
  
  @JsxFunction
  public Object splitText(int offset)
  {
    DomText domText = (DomText)getDomNodeOrDie();
    return getScriptableFor(domText.splitText(offset));
  }
  



  @JsxGetter
  public String getWholeText()
  {
    return ((DomText)getDomNodeOrDie()).getWholeText();
  }
  



  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object getText()
  {
    DomNode node = getDomNodeOrDie();
    if ((node.getPage() instanceof XmlPage)) {
      return ((DomText)node).getWholeText();
    }
    return Undefined.instance;
  }
}
