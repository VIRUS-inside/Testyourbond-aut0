package com.gargoylesoftware.htmlunit.html;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;







































public class DomTreeWalker
  implements TreeWalker
{
  private final DomNode root_;
  private DomNode currentNode_;
  private final int whatToShow_;
  private final NodeFilter filter_;
  private final boolean expandEntityReferences_;
  
  public DomTreeWalker(DomNode root, int whatToShow, NodeFilter filter, boolean expandEntityReferences)
    throws DOMException
  {
    if (root == null) {
      Context.throwAsScriptRuntimeEx(new DOMException((short)9, 
        "root must not be null"));
    }
    root_ = root;
    whatToShow_ = whatToShow;
    filter_ = filter;
    expandEntityReferences_ = expandEntityReferences;
    currentNode_ = root_;
  }
  



  public DomNode getRoot()
  {
    return root_;
  }
  



  public int getWhatToShow()
  {
    return whatToShow_;
  }
  



  public NodeFilter getFilter()
  {
    return filter_;
  }
  



  public boolean getExpandEntityReferences()
  {
    return expandEntityReferences_;
  }
  



  public DomNode getCurrentNode()
  {
    return currentNode_;
  }
  


  public void setCurrentNode(Node currentNode)
    throws DOMException
  {
    if (currentNode == null) {
      throw new DOMException((short)9, 
        "currentNode cannot be set to null");
    }
    currentNode_ = ((DomNode)currentNode);
  }
  



  public DomNode nextNode()
  {
    DomNode leftChild = getEquivalentLogical(currentNode_.getFirstChild(), false);
    if (leftChild != null) {
      currentNode_ = leftChild;
      return leftChild;
    }
    DomNode rightSibling = getEquivalentLogical(currentNode_.getNextSibling(), false);
    if (rightSibling != null) {
      currentNode_ = rightSibling;
      return rightSibling;
    }
    
    DomNode uncle = getFirstUncleNode(currentNode_);
    if (uncle != null) {
      currentNode_ = uncle;
      return uncle;
    }
    
    return null;
  }
  



  private DomNode getFirstUncleNode(DomNode n)
  {
    if ((n == root_) || (n == null)) {
      return null;
    }
    
    DomNode parent = n.getParentNode();
    if (parent == null) {
      return null;
    }
    
    DomNode uncle = getEquivalentLogical(parent.getNextSibling(), false);
    if (uncle != null) {
      return uncle;
    }
    
    return getFirstUncleNode(parent);
  }
  










  private DomNode getEquivalentLogical(DomNode n, boolean lookLeft)
  {
    if (n == null) {
      return null;
    }
    if (isNodeVisible(n)) {
      return n;
    }
    

    if (isNodeSkipped(n)) { DomNode child;
      DomNode child;
      if (lookLeft) {
        child = getEquivalentLogical(n.getLastChild(), lookLeft);
      }
      else {
        child = getEquivalentLogical(n.getFirstChild(), lookLeft);
      }
      
      if (child != null) {
        return child;
      }
    }
    


    return getSibling(n, lookLeft);
  }
  


  private boolean isNodeVisible(Node n)
  {
    if ((acceptNode(n) == 1) && (
      (filter_ == null) || (filter_.acceptNode(n) == 1))) {
      return (expandEntityReferences_) || (n.getParentNode() == null) || 
        (n.getParentNode().getNodeType() != 5);
    }
    
    return false;
  }
  







  private short acceptNode(Node n)
  {
    int flag = getFlagForNode(n);
    
    if ((whatToShow_ & flag) != 0) {
      return 1;
    }
    
    return 3;
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
  

  private boolean isNodeSkipped(Node n)
  {
    return (!isNodeVisible(n)) && (!isNodeRejected(n));
  }
  
  private boolean isNodeRejected(Node n)
  {
    if (acceptNode(n) == 2) {
      return true;
    }
    if ((filter_ != null) && (filter_.acceptNode(n) == 2)) {
      return true;
    }
    return (!expandEntityReferences_) && (n.getParentNode() != null) && 
      (n.getParentNode().getNodeType() == 5);
  }
  
  private DomNode getSibling(DomNode n, boolean lookLeft)
  {
    if (n == null) {
      return null;
    }
    
    if (isNodeVisible(n)) {
      return null;
    }
    DomNode sibling;
    DomNode sibling;
    if (lookLeft) {
      sibling = n.getPreviousSibling();
    }
    else {
      sibling = n.getNextSibling();
    }
    
    if (sibling == null)
    {
      if (n == root_) {
        return null;
      }
      return getSibling(n.getParentNode(), lookLeft);
    }
    
    return getEquivalentLogical(sibling, lookLeft);
  }
  



  public DomNode nextSibling()
  {
    if (currentNode_ == root_) {
      return null;
    }
    
    DomNode newNode = getEquivalentLogical(currentNode_.getNextSibling(), false);
    
    if (newNode != null) {
      currentNode_ = newNode;
    }
    
    return newNode;
  }
  



  public DomNode parentNode()
  {
    if (currentNode_ == root_) {
      return null;
    }
    
    DomNode newNode = currentNode_;
    do
    {
      newNode = newNode.getParentNode();
    }
    while ((newNode != null) && (!isNodeVisible(newNode)) && (newNode != root_));
    
    if ((newNode == null) || (!isNodeVisible(newNode))) {
      return null;
    }
    currentNode_ = newNode;
    return newNode;
  }
  



  public DomNode previousSibling()
  {
    if (currentNode_ == root_) {
      return null;
    }
    
    DomNode newNode = getEquivalentLogical(currentNode_.getPreviousSibling(), true);
    
    if (newNode != null) {
      currentNode_ = newNode;
    }
    
    return newNode;
  }
  



  public DomNode lastChild()
  {
    DomNode newNode = getEquivalentLogical(currentNode_.getLastChild(), true);
    
    if (newNode != null) {
      currentNode_ = newNode;
    }
    
    return newNode;
  }
  



  public DomNode previousNode()
  {
    DomNode newNode = getPreviousNode(currentNode_);
    
    if (newNode != null) {
      currentNode_ = newNode;
    }
    
    return newNode;
  }
  



  private DomNode getPreviousNode(DomNode n)
  {
    if (n == root_) {
      return null;
    }
    DomNode left = getEquivalentLogical(n.getPreviousSibling(), true);
    if (left == null) {
      DomNode parent = n.getParentNode();
      if (parent == null) {
        return null;
      }
      if (isNodeVisible(parent)) {
        return parent;
      }
    }
    
    DomNode follow = left;
    if (follow != null) {
      while (follow.hasChildNodes()) {
        DomNode toFollow = getEquivalentLogical(follow.getLastChild(), true);
        if (toFollow == null) {
          break;
        }
        follow = toFollow;
      }
    }
    return follow;
  }
  



  public DomNode firstChild()
  {
    DomNode newNode = getEquivalentLogical(currentNode_.getFirstChild(), false);
    
    if (newNode != null) {
      currentNode_ = newNode;
    }
    
    return newNode;
  }
}
