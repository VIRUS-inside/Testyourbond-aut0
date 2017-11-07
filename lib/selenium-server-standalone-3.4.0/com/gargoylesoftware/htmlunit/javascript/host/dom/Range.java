package com.gargoylesoftware.htmlunit.javascript.host.dom;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import java.util.HashSet;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;












































@JsxClass
public class Range
  extends SimpleScriptable
{
  @JsxConstant
  public static final short START_TO_START = 0;
  @JsxConstant
  public static final short START_TO_END = 1;
  @JsxConstant
  public static final short END_TO_END = 2;
  @JsxConstant
  public static final short END_TO_START = 3;
  private Node startContainer_;
  private Node endContainer_;
  private int startOffset_;
  private int endOffset_;
  
  @JsxConstructor({@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
  public Range() {}
  
  public Range(HTMLDocument document)
  {
    startContainer_ = document;
    endContainer_ = document;
  }
  
  Range(org.w3c.dom.ranges.Range w3cRange) {
    DomNode domNodeStartContainer = (DomNode)w3cRange.getStartContainer();
    startContainer_ = ((Node)domNodeStartContainer.getScriptableObject());
    startOffset_ = w3cRange.getStartOffset();
    
    DomNode domNodeEndContainer = (DomNode)w3cRange.getEndContainer();
    endContainer_ = ((Node)domNodeEndContainer.getScriptableObject());
    endOffset_ = w3cRange.getEndOffset();
  }
  



  public Object getDefaultValue(Class<?> hint)
  {
    if (getPrototype() == null) {
      return super.getDefaultValue(hint);
    }
    return toW3C().toString();
  }
  



  @JsxGetter
  public Object getStartContainer()
  {
    if (startContainer_ == null) {
      return Undefined.instance;
    }
    return startContainer_;
  }
  



  @JsxGetter
  public Object getEndContainer()
  {
    if (endContainer_ == null) {
      return Undefined.instance;
    }
    return endContainer_;
  }
  



  @JsxGetter
  public int getStartOffset()
  {
    return startOffset_;
  }
  



  @JsxGetter
  public int getEndOffset()
  {
    return endOffset_;
  }
  




  @JsxFunction
  public void setStart(Node refNode, int offset)
  {
    if (refNode == null) {
      throw Context.reportRuntimeError("It is illegal to call Range.setStart() with a null node.");
    }
    startContainer_ = refNode;
    startOffset_ = offset;
  }
  



  @JsxFunction
  public void setStartAfter(Node refNode)
  {
    if (refNode == null) {
      throw Context.reportRuntimeError("It is illegal to call Range.setStartAfter() with a null node.");
    }
    startContainer_ = refNode.getParent();
    startOffset_ = (getPositionInContainer(refNode) + 1);
  }
  



  @JsxFunction
  public void setStartBefore(Node refNode)
  {
    if (refNode == null) {
      throw Context.reportRuntimeError("It is illegal to call Range.setStartBefore() with a null node.");
    }
    startContainer_ = refNode.getParent();
    startOffset_ = getPositionInContainer(refNode);
  }
  
  private static int getPositionInContainer(Node refNode) {
    int i = 0;
    Node node = refNode;
    while (node.getPreviousSibling() != null) {
      node = node.getPreviousSibling();
      i++;
    }
    return i;
  }
  



  @JsxGetter
  public boolean getCollapsed()
  {
    return (startContainer_ == endContainer_) && (startOffset_ == endOffset_);
  }
  




  @JsxFunction
  public void setEnd(Node refNode, int offset)
  {
    if (refNode == null) {
      throw Context.reportRuntimeError("It is illegal to call Range.setEnd() with a null node.");
    }
    endContainer_ = refNode;
    endOffset_ = offset;
  }
  



  @JsxFunction
  public void setEndAfter(Node refNode)
  {
    if (refNode == null) {
      throw Context.reportRuntimeError("It is illegal to call Range.setEndAfter() with a null node.");
    }
    endContainer_ = refNode.getParent();
    endOffset_ = (getPositionInContainer(refNode) + 1);
  }
  



  @JsxFunction
  public void setEndBefore(Node refNode)
  {
    if (refNode == null) {
      throw Context.reportRuntimeError("It is illegal to call Range.setEndBefore() with a null node.");
    }
    startContainer_ = refNode.getParent();
    startOffset_ = getPositionInContainer(refNode);
  }
  



  @JsxFunction
  public void selectNodeContents(Node refNode)
  {
    startContainer_ = refNode;
    startOffset_ = 0;
    endContainer_ = refNode;
    endOffset_ = refNode.getChildNodes().getLength();
  }
  



  @JsxFunction
  public void selectNode(Node refNode)
  {
    setStartBefore(refNode);
    setEndAfter(refNode);
  }
  



  @JsxFunction
  public void collapse(boolean toStart)
  {
    if (toStart) {
      endContainer_ = startContainer_;
      endOffset_ = startOffset_;
    }
    else {
      startContainer_ = endContainer_;
      startOffset_ = endOffset_;
    }
  }
  



  @JsxGetter
  public Object getCommonAncestorContainer()
  {
    HashSet<Node> startAncestors = new HashSet();
    Node ancestor = startContainer_;
    while (ancestor != null) {
      startAncestors.add(ancestor);
      ancestor = ancestor.getParent();
    }
    
    ancestor = endContainer_;
    while (ancestor != null) {
      if (startAncestors.contains(ancestor)) {
        return ancestor;
      }
      ancestor = ancestor.getParent();
    }
    
    return Undefined.instance;
  }
  






  @JsxFunction
  public Object createContextualFragment(String valueAsString)
  {
    SgmlPage page = startContainer_.getDomNodeOrDie().getPage();
    DomDocumentFragment fragment = new DomDocumentFragment(page);
    try {
      HTMLParser.parseFragment(fragment, startContainer_.getDomNodeOrDie(), valueAsString);
    }
    catch (Exception e) {
      LogFactory.getLog(Range.class).error("Unexpected exception occurred in createContextualFragment", e);
      throw Context.reportRuntimeError("Unexpected exception occurred in createContextualFragment: " + 
        e.getMessage());
    }
    
    return fragment.getScriptableObject();
  }
  



  @JsxFunction
  public Object extractContents()
  {
    return toW3C().extractContents().getScriptableObject();
  }
  



  public SimpleRange toW3C()
  {
    return new SimpleRange(startContainer_.getDomNodeOrNull(), startOffset_, 
      endContainer_.getDomNodeOrDie(), endOffset_);
  }
  

  @JsxFunction
  public Object compareBoundaryPoints(int how, Range sourceRange)
  {
    int containingMoficator;
    
    Node nodeForThis;
    
    int offsetForThis;
    
    int containingMoficator;
    
    if ((how == 0) || (3 == how)) {
      Node nodeForThis = startContainer_;
      int offsetForThis = startOffset_;
      containingMoficator = 1;
    }
    else {
      nodeForThis = endContainer_;
      offsetForThis = endOffset_;
      containingMoficator = -1;
    }
    int offsetForOther;
    Node nodeForOther;
    int offsetForOther;
    if ((1 == how) || (how == 0)) {
      Node nodeForOther = startContainer_;
      offsetForOther = startOffset_;
    }
    else {
      nodeForOther = endContainer_;
      offsetForOther = endOffset_;
    }
    
    if (nodeForThis == nodeForOther) {
      if (offsetForThis < offsetForOther) {
        return Integer.valueOf(-1);
      }
      if (offsetForThis < offsetForOther) {
        return Integer.valueOf(1);
      }
      return Integer.valueOf(0);
    }
    
    byte nodeComparision = (byte)nodeForThis.compareDocumentPosition(nodeForOther);
    if ((nodeComparision & 0x10) != 0) {
      return Integer.valueOf(-1 * containingMoficator);
    }
    if ((nodeComparision & 0x2) != 0) {
      return Integer.valueOf(-1);
    }
    
    return Integer.valueOf(1);
  }
  



  @JsxFunction
  public Object cloneContents()
  {
    return toW3C().cloneContents().getScriptableObject();
  }
  


  @JsxFunction
  public void deleteContents()
  {
    toW3C().deleteContents();
  }
  




  @JsxFunction
  public void insertNode(Node newNode)
  {
    toW3C().insertNode(newNode.getDomNodeOrDie());
  }
  



  @JsxFunction
  public void surroundContents(Node newNode)
  {
    toW3C().surroundContents(newNode.getDomNodeOrDie());
  }
  



  @JsxFunction
  public Object cloneRange()
  {
    return new Range(toW3C().cloneRange());
  }
  





  @JsxFunction
  public void detach() {}
  




  @JsxFunction
  public String toString()
  {
    return toW3C().toString();
  }
  
  protected Object equivalentValues(Object value)
  {
    if (!(value instanceof Range)) {
      return Boolean.valueOf(false);
    }
    Range other = (Range)value;
    if ((startContainer_ == startContainer_) && 
      (endContainer_ == endContainer_) && 
      (startOffset_ == startOffset_) && 
      (endOffset_ == endOffset_))
      return Boolean.valueOf(true); return Boolean.valueOf(false);
  }
}
