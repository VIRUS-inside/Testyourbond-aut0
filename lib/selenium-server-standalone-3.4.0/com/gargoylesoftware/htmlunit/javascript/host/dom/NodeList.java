package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;












































@JsxClass
public class NodeList
  extends AbstractList
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public NodeList() {}
  
  public NodeList(DomNode domNode, boolean attributeChangeSensitive)
  {
    super(domNode, attributeChangeSensitive);
  }
  




  public NodeList(DomNode domNode, List<?> initialElements)
  {
    super(domNode, initialElements);
  }
  



  private NodeList(ScriptableObject parentScope)
  {
    setParentScope(parentScope);
    setPrototype(getPrototype(getClass()));
  }
  






  public static NodeList staticNodeList(HtmlUnitScriptable parentScope, final List<Object> elements)
  {
    new NodeList(parentScope, elements)
    {
      public List<Object> getElements() {
        return elements;
      }
    };
  }
}
