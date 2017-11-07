package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;





























public class DomNodeIterator
  implements NodeIterator
{
  private DomNode root_;
  private int whatToShow_;
  private NodeFilter filter_;
  private DomNode referenceNode_;
  private final boolean expandEntityReferences_;
  private boolean pointerBeforeReferenceNode_;
  
  public DomNodeIterator(DomNode root, int whatToShow, NodeFilter filter, boolean expandEntityReferences)
  {
    root_ = root;
    referenceNode_ = root;
    whatToShow_ = whatToShow;
    filter_ = filter;
    expandEntityReferences_ = expandEntityReferences;
    pointerBeforeReferenceNode_ = true;
  }
  



  public DomNode getRoot()
  {
    return root_;
  }
  



  public int getWhatToShow()
  {
    return whatToShow_;
  }
  



  public boolean getExpandEntityReferences()
  {
    return expandEntityReferences_;
  }
  



  public NodeFilter getFilter()
  {
    return filter_;
  }
  



  public boolean isPointerBeforeReferenceNode()
  {
    return pointerBeforeReferenceNode_;
  }
  




  public void detach() {}
  




  public DomNode nextNode()
  {
    return traverse(true);
  }
  



  public DomNode previousNode()
  {
    return traverse(false);
  }
  
  private DomNode traverse(boolean next) {
    DomNode node = referenceNode_;
    boolean beforeNode = pointerBeforeReferenceNode_;
    do {
      if (next) {
        if (beforeNode) {
          beforeNode = false;
        }
        else {
          DomNode leftChild = getChild(node, true);
          if (leftChild != null) {
            node = leftChild;
          }
          else {
            DomNode rightSibling = getSibling(node, false);
            if (rightSibling != null) {
              node = rightSibling;
            }
            else {
              node = getFirstUncleNode(node);
            }
            
          }
        }
      }
      else if (!beforeNode) {
        beforeNode = true;
      }
      else {
        DomNode left = getSibling(node, true);
        if (left == null) {
          Node parent = node.getParentNode();
          if (parent == null) {
            node = null;
          }
        }
        
        DomNode follow = left;
        if (follow != null) {
          while (follow.hasChildNodes()) {
            DomNode toFollow = getChild(follow, false);
            if (toFollow == null) {
              break;
            }
            follow = toFollow;
          }
        }
        node = follow;
      }
      
    }
    while ((node != null) && ((!isNodeVisible(node)) || (!isAccepted(node))));
    


    referenceNode_ = node;
    pointerBeforeReferenceNode_ = beforeNode;
    return node;
  }
  
  private boolean isNodeVisible(Node node) {
    return (whatToShow_ & DomTreeWalker.getFlagForNode(node)) != 0;
  }
  
  private boolean isAccepted(Node node) {
    if (filter_ == null) {
      return true;
    }
    return filter_.acceptNode(node) == 1;
  }
  



  private DomNode getFirstUncleNode(DomNode node)
  {
    if ((node == root_) || (node == null)) {
      return null;
    }
    
    DomNode parent = node.getParentNode();
    if (parent == null) {
      return null;
    }
    
    DomNode uncle = getSibling(parent, false);
    if (uncle != null) {
      return uncle;
    }
    
    return getFirstUncleNode(parent);
  }
  
  private static DomNode getChild(DomNode node, boolean lookLeft) {
    if (node == null) {
      return null;
    }
    DomNode child;
    DomNode child;
    if (lookLeft) {
      child = node.getFirstChild();
    }
    else {
      child = node.getLastChild();
    }
    
    return child;
  }
  
  private static DomNode getSibling(DomNode node, boolean lookLeft) {
    if (node == null) {
      return null;
    }
    DomNode sibling;
    DomNode sibling;
    if (lookLeft) {
      sibling = node.getPreviousSibling();
    }
    else {
      sibling = node.getNextSibling();
    }
    
    return sibling;
  }
}
