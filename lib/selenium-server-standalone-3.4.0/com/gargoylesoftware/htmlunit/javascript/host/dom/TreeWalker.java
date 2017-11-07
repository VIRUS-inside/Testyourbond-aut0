package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomTreeWalker;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.w3c.dom.DOMException;
import org.w3c.dom.traversal.NodeFilter;



















































@JsxClass
public class TreeWalker
  extends SimpleScriptable
{
  private DomTreeWalker walker_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public TreeWalker() {}
  
  public TreeWalker(SgmlPage page, Node root, int whatToShow, NodeFilter filter, boolean expandEntityReferences)
    throws DOMException
  {
    if (root == null) {
      Context.throwAsScriptRuntimeEx(new DOMException((short)9, 
        "root must not be null"));
    }
    walker_ = page.createTreeWalker(root.getDomNodeOrDie(), whatToShow, filter, expandEntityReferences);
  }
  




  @JsxGetter
  public Node getRoot()
  {
    return getNodeOrNull(walker_.getRoot());
  }
  






  @JsxGetter
  public long getWhatToShow()
  {
    long whatToShow = walker_.getWhatToShow();
    if (whatToShow == -1L) {
      whatToShow = 4294967295L;
    }
    return whatToShow;
  }
  





  @JsxGetter
  public Object getFilter()
  {
    return walker_.getFilter();
  }
  




  @JsxGetter({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE)})
  public boolean getExpandEntityReferences()
  {
    return walker_.getExpandEntityReferences();
  }
  




  @JsxGetter
  public Node getCurrentNode()
  {
    return getNodeOrNull(walker_.getCurrentNode());
  }
  






  @JsxSetter
  public void setCurrentNode(Node currentNode)
    throws DOMException
  {
    if (currentNode == null) {
      throw new DOMException((short)9, 
        "currentNode cannot be set to null");
    }
    walker_.setCurrentNode(currentNode.getDomNodeOrDie());
  }
  





  static int getFlagForNode(Node node)
  {
    switch (node.getNodeType()) {
    case 1: 
      return 1;
    case 2: 
      return 2;
    case 3: 
      return 4;
    case 4: 
      return 8;
    case 5: 
      return 16;
    case 6: 
      return 32;
    case 7: 
      return 64;
    case 8: 
      return 128;
    case 9: 
      return 256;
    case 10: 
      return 512;
    case 11: 
      return 1024;
    case 12: 
      return 2048;
    }
    return 0;
  }
  









  @JsxFunction
  public Node parentNode()
  {
    return getNodeOrNull(walker_.parentNode());
  }
  
  private static Node getNodeOrNull(DomNode domNode) {
    if (domNode == null) {
      return null;
    }
    return (Node)domNode.getScriptableObject();
  }
  







  @JsxFunction
  public Node firstChild()
  {
    return getNodeOrNull(walker_.firstChild());
  }
  







  @JsxFunction
  public Node lastChild()
  {
    return getNodeOrNull(walker_.lastChild());
  }
  







  @JsxFunction
  public Node previousSibling()
  {
    return getNodeOrNull(walker_.previousSibling());
  }
  







  @JsxFunction
  public Node nextSibling()
  {
    return getNodeOrNull(walker_.nextSibling());
  }
  









  @JsxFunction
  public Node previousNode()
  {
    return getNodeOrNull(walker_.previousNode());
  }
  









  @JsxFunction
  public Node nextNode()
  {
    return getNodeOrNull(walker_.nextNode());
  }
}
