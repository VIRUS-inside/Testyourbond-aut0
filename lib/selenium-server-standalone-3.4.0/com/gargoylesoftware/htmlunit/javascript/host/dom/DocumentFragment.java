package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.w3c.css.sac.CSSException;










































@JsxClass(domClass=DomDocumentFragment.class)
public class DocumentFragment
  extends Node
{
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public DocumentFragment() {}
  
  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object createAttribute(String attributeName)
  {
    return getDocument().createAttribute(attributeName);
  }
  



  protected HTMLDocument getDocument()
  {
    return (HTMLDocument)getDomNodeOrDie().getPage().getScriptableObject();
  }
  




  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object createComment(String comment)
  {
    return getDocument().createComment(comment);
  }
  



  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object createDocumentFragment()
  {
    return getDocument().createDocumentFragment();
  }
  





  @JsxFunction({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public Object createTextNode(String newData)
  {
    return getDocument().createTextNode(newData);
  }
  





  @JsxFunction
  public NodeList querySelectorAll(String selectors)
  {
    try
    {
      List<Object> nodes = new ArrayList();
      for (DomNode domNode : getDomNodeOrDie().querySelectorAll(selectors)) {
        nodes.add(domNode.getScriptableObject());
      }
      return NodeList.staticNodeList(this, nodes);
    }
    catch (CSSException e) {
      throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '" + 
        selectors + "' error: " + e.getMessage() + ").");
    }
  }
  



  @JsxFunction
  public Node querySelector(String selectors)
  {
    try
    {
      DomNode node = getDomNodeOrDie().querySelector(selectors);
      if (node != null) {
        return (Node)node.getScriptableObject();
      }
      return null;
    }
    catch (CSSException e) {
      throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '" + 
        selectors + "' error: " + e.getMessage() + ").");
    }
  }
  



  public Object getDefaultValue(Class<?> hint)
  {
    if ((String.class.equals(hint)) || (hint == null)) {
      return "[object " + getClassName() + "]";
    }
    return super.getDefaultValue(hint);
  }
}
