package com.gargoylesoftware.htmlunit.html.impl;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.DomText;
import java.io.Serializable;
import java.util.Iterator;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.ranges.RangeException;














































public class SimpleRange
  implements Range, Serializable
{
  private Node startContainer_;
  private Node endContainer_;
  private int startOffset_;
  private int endOffset_;
  
  public SimpleRange() {}
  
  public SimpleRange(Node node)
  {
    startContainer_ = node;
    endContainer_ = node;
    startOffset_ = 0;
    endOffset_ = getMaxOffset(node);
  }
  




  public SimpleRange(Node node, int offset)
  {
    startContainer_ = node;
    endContainer_ = node;
    startOffset_ = offset;
    endOffset_ = offset;
  }
  






  public SimpleRange(Node startNode, int startOffset, Node endNode, int endOffset)
  {
    startContainer_ = startNode;
    endContainer_ = endNode;
    startOffset_ = startOffset;
    endOffset_ = endOffset;
    if ((startNode == endNode) && (startOffset > endOffset)) {
      endOffset_ = startOffset;
    }
  }
  



  public DomDocumentFragment cloneContents()
    throws DOMException
  {
    DomNode ancestor = (DomNode)getCommonAncestorContainer();
    
    if (ancestor == null) {
      return new DomDocumentFragment(null);
    }
    DomNode ancestorClone = ancestor.cloneNode(true);
    

    DomNode startClone = null;
    DomNode endClone = null;
    DomNode start = (DomNode)startContainer_;
    DomNode end = (DomNode)endContainer_;
    if (start == ancestor) {
      startClone = ancestorClone;
    }
    if (end == ancestor) {
      endClone = ancestorClone;
    }
    Iterable<DomNode> descendants = ancestor.getDescendants();
    DomNode ce; if ((startClone == null) || (endClone == null)) {
      Iterator<DomNode> i = descendants.iterator();
      Iterator<DomNode> ci = ancestorClone.getDescendants().iterator();
      while (i.hasNext()) {
        DomNode e = (DomNode)i.next();
        ce = (DomNode)ci.next();
        if (start == e) {
          startClone = ce;
        }
        else if (end == e) {
          endClone = ce;
          break;
        }
      }
    }
    



    if (endClone == null) {
      throw Context.reportRuntimeError("Unable to find end node clone.");
    }
    deleteAfter(endClone, endOffset_);
    for (DomNode n = endClone; n != null; n = n.getParentNode()) {
      while (n.getNextSibling() != null) {
        n.getNextSibling().remove();
      }
    }
    

    if (startClone == null) {
      throw Context.reportRuntimeError("Unable to find start node clone.");
    }
    deleteBefore(startClone, startOffset_);
    for (DomNode n = startClone; n != null; n = n.getParentNode()) {
      while (n.getPreviousSibling() != null) {
        n.getPreviousSibling().remove();
      }
    }
    
    SgmlPage page = ancestor.getPage();
    DomDocumentFragment fragment = new DomDocumentFragment(page);
    if (start == end) {
      fragment.appendChild(ancestorClone);
    }
    else {
      for (DomNode n : ancestorClone.getChildNodes()) {
        fragment.appendChild(n);
      }
    }
    return fragment;
  }
  


  public Range cloneRange()
    throws DOMException
  {
    return new SimpleRange(startContainer_, startOffset_, endContainer_, endOffset_);
  }
  


  public void collapse(boolean toStart)
    throws DOMException
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
  


  public short compareBoundaryPoints(short how, Range sourceRange)
    throws DOMException
  {
    throw new RuntimeException("Not implemented!");
  }
  


  public void deleteContents()
    throws DOMException
  {
    DomNode ancestor = (DomNode)getCommonAncestorContainer();
    if (ancestor != null) {
      deleteContents(ancestor);
    }
  }
  
  private void deleteContents(DomNode ancestor)
  {
    DomNode start;
    if (isOffsetChars(startContainer_)) {
      DomNode start = (DomNode)startContainer_;
      String text = getText(start);
      text = text.substring(0, startOffset_);
      setText(start, text);
    } else { DomNode start;
      if (startContainer_.getChildNodes().getLength() > startOffset_) {
        start = (DomNode)startContainer_.getChildNodes().item(startOffset_);
      }
      else
        start = (DomNode)startContainer_.getNextSibling(); }
    DomNode end;
    if (isOffsetChars(endContainer_)) {
      DomNode end = (DomNode)endContainer_;
      String text = getText(end);
      text = text.substring(endOffset_);
      setText(end, text);
    } else { DomNode end;
      if (endContainer_.getChildNodes().getLength() > endOffset_) {
        end = (DomNode)endContainer_.getChildNodes().item(endOffset_);
      }
      else
        end = (DomNode)endContainer_.getNextSibling();
    }
    boolean foundStart = false;
    boolean started = false;
    Iterator<DomNode> i = ancestor.getDescendants().iterator();
    while (i.hasNext()) {
      DomNode n = (DomNode)i.next();
      if (n == end) {
        break;
      }
      if (n == start) {
        foundStart = true;
      }
      if ((foundStart) && ((n != start) || (!isOffsetChars(startContainer_)))) {
        started = true;
      }
      if ((started) && (!n.isAncestorOf(end))) {
        i.remove();
      }
    }
  }
  


  public void detach()
    throws DOMException
  {
    throw new RuntimeException("Not implemented!");
  }
  


  public DomDocumentFragment extractContents()
    throws DOMException
  {
    DomDocumentFragment fragment = cloneContents();
    

    deleteContents();
    

    return fragment;
  }
  


  public boolean getCollapsed()
    throws DOMException
  {
    return (startContainer_ == endContainer_) && (startOffset_ == endOffset_);
  }
  


  public Node getCommonAncestorContainer()
    throws DOMException
  {
    if ((startContainer_ != null) && (endContainer_ != null)) {
      for (Node p1 = startContainer_; p1 != null; p1 = p1.getParentNode()) {
        for (Node p2 = endContainer_; p2 != null; p2 = p2.getParentNode()) {
          if (p1 == p2) {
            return p1;
          }
        }
      }
    }
    return null;
  }
  


  public Node getEndContainer()
    throws DOMException
  {
    return endContainer_;
  }
  


  public int getEndOffset()
    throws DOMException
  {
    return endOffset_;
  }
  


  public Node getStartContainer()
    throws DOMException
  {
    return startContainer_;
  }
  


  public int getStartOffset()
    throws DOMException
  {
    return startOffset_;
  }
  


  public void insertNode(Node newNode)
    throws DOMException, RangeException
  {
    if (isOffsetChars(startContainer_)) {
      Node split = startContainer_.cloneNode(false);
      String text = getText(startContainer_);
      text = text.substring(0, startOffset_);
      setText(startContainer_, text);
      text = getText(split);
      text = text.substring(startOffset_);
      setText(split, text);
      insertNodeOrDocFragment(startContainer_.getParentNode(), split, startContainer_.getNextSibling());
      insertNodeOrDocFragment(startContainer_.getParentNode(), newNode, split);
    }
    else {
      insertNodeOrDocFragment(startContainer_, newNode, startContainer_.getChildNodes().item(startOffset_));
    }
    
    setStart(newNode, 0);
  }
  
  private static void insertNodeOrDocFragment(Node parent, Node newNode, Node refNode) {
    if ((newNode instanceof DocumentFragment)) {
      DocumentFragment fragment = (DocumentFragment)newNode;
      
      NodeList childNodes = fragment.getChildNodes();
      while (childNodes.getLength() > 0) {
        Node item = childNodes.item(0);
        parent.insertBefore(item, refNode);
      }
    }
    else {
      parent.insertBefore(newNode, refNode);
    }
  }
  


  public void selectNode(Node node)
    throws RangeException, DOMException
  {
    startContainer_ = node;
    startOffset_ = 0;
    endContainer_ = node;
    endOffset_ = getMaxOffset(node);
  }
  


  public void selectNodeContents(Node node)
    throws RangeException, DOMException
  {
    startContainer_ = node.getFirstChild();
    startOffset_ = 0;
    endContainer_ = node.getLastChild();
    endOffset_ = getMaxOffset(node.getLastChild());
  }
  


  public void setEnd(Node refNode, int offset)
    throws RangeException, DOMException
  {
    endContainer_ = refNode;
    endOffset_ = offset;
  }
  


  public void setEndAfter(Node refNode)
    throws RangeException, DOMException
  {
    throw new RuntimeException("Not implemented!");
  }
  


  public void setEndBefore(Node refNode)
    throws RangeException, DOMException
  {
    throw new RuntimeException("Not implemented!");
  }
  


  public void setStart(Node refNode, int offset)
    throws RangeException, DOMException
  {
    startContainer_ = refNode;
    startOffset_ = offset;
  }
  


  public void setStartAfter(Node refNode)
    throws RangeException, DOMException
  {
    throw new RuntimeException("Not implemented!");
  }
  


  public void setStartBefore(Node refNode)
    throws RangeException, DOMException
  {
    throw new RuntimeException("Not implemented!");
  }
  


  public void surroundContents(Node newParent)
    throws DOMException, RangeException
  {
    newParent.appendChild(extractContents());
    insertNode(newParent);
    setStart(newParent, 0);
    setEnd(newParent, getMaxOffset(newParent));
  }
  



  public boolean equals(Object obj)
  {
    if (!(obj instanceof SimpleRange)) {
      return false;
    }
    SimpleRange other = (SimpleRange)obj;
    return new EqualsBuilder()
      .append(startContainer_, startContainer_)
      .append(endContainer_, endContainer_)
      .append(startOffset_, startOffset_)
      .append(endOffset_, endOffset_).isEquals();
  }
  



  public int hashCode()
  {
    return 
    


      new HashCodeBuilder().append(startContainer_).append(endContainer_).append(startOffset_).append(endOffset_).toHashCode();
  }
  



  public String toString()
  {
    DomDocumentFragment fragment = cloneContents();
    if (fragment.getPage() != null) {
      return fragment.asText();
    }
    return "";
  }
  
  private static boolean isOffsetChars(Node node) {
    return ((node instanceof DomText)) || ((node instanceof SelectableTextInput));
  }
  
  private static String getText(Node node) {
    if ((node instanceof SelectableTextInput)) {
      return ((SelectableTextInput)node).getText();
    }
    return node.getTextContent();
  }
  
  private static void setText(Node node, String text) {
    if ((node instanceof SelectableTextInput)) {
      ((SelectableTextInput)node).setText(text);
    }
    else {
      node.setTextContent(text);
    }
  }
  
  private static void deleteBefore(DomNode node, int offset) {
    if (isOffsetChars(node)) {
      String text = getText(node);
      if (offset < text.length()) {
        text = text.substring(offset);
      }
      else {
        text = "";
      }
      setText(node, text);
    }
    else {
      DomNodeList<DomNode> children = node.getChildNodes();
      for (int i = 0; (i < offset) && (i < children.getLength()); i++) {
        DomNode child = (DomNode)children.get(i);
        child.remove();
        i--;
        offset--;
      }
    }
  }
  
  private static void deleteAfter(DomNode node, int offset) {
    if (isOffsetChars(node)) {
      String text = getText(node);
      if (offset < text.length()) {
        text = text.substring(0, offset);
        setText(node, text);
      }
    }
    else {
      DomNodeList<DomNode> children = node.getChildNodes();
      for (int i = offset; i < children.getLength(); i++) {
        DomNode child = (DomNode)children.get(i);
        child.remove();
        i--;
      }
    }
  }
  
  private static int getMaxOffset(Node node) {
    return isOffsetChars(node) ? getText(node).length() : node.getChildNodes().getLength();
  }
}
