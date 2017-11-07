package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeIterator;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import org.w3c.dom.traversal.NodeFilter;




































@JsxClass
public class NodeIterator
  extends SimpleScriptable
{
  private DomNodeIterator iterator_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public NodeIterator() {}
  
  public NodeIterator(SgmlPage page, Node root, int whatToShow, NodeFilter filter)
  {
    iterator_ = page.createNodeIterator(root.getDomNodeOrDie(), whatToShow, filter, true);
  }
  



  @JsxGetter
  public Node getRoot()
  {
    return getNodeOrNull(iterator_.getRoot());
  }
  
  private static Node getNodeOrNull(DomNode domNode) {
    if (domNode == null) {
      return null;
    }
    return (Node)domNode.getScriptableObject();
  }
  



  public long getWhatToShow()
  {
    if (iterator_.getWhatToShow() == 4294967295L) {
      return 4294967295L;
    }
    return iterator_.getWhatToShow();
  }
  




  @JsxGetter
  public Object getFilter()
  {
    return iterator_.getFilter();
  }
  


  @JsxFunction
  public void detach()
  {
    iterator_.detach();
  }
  



  @JsxFunction
  public Node nextNode()
  {
    return getNodeOrNull(iterator_.nextNode());
  }
  



  @JsxFunction
  public Node previousNode()
  {
    return getNodeOrNull(iterator_.previousNode());
  }
}
