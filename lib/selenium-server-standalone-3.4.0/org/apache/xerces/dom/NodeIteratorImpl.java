package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

public class NodeIteratorImpl
  implements NodeIterator
{
  private DocumentImpl fDocument;
  private Node fRoot;
  private int fWhatToShow = -1;
  private NodeFilter fNodeFilter;
  private boolean fDetach = false;
  private Node fCurrentNode;
  private boolean fForward = true;
  private boolean fEntityReferenceExpansion;
  
  public NodeIteratorImpl(DocumentImpl paramDocumentImpl, Node paramNode, int paramInt, NodeFilter paramNodeFilter, boolean paramBoolean)
  {
    fDocument = paramDocumentImpl;
    fRoot = paramNode;
    fCurrentNode = null;
    fWhatToShow = paramInt;
    fNodeFilter = paramNodeFilter;
    fEntityReferenceExpansion = paramBoolean;
  }
  
  public Node getRoot()
  {
    return fRoot;
  }
  
  public int getWhatToShow()
  {
    return fWhatToShow;
  }
  
  public NodeFilter getFilter()
  {
    return fNodeFilter;
  }
  
  public boolean getExpandEntityReferences()
  {
    return fEntityReferenceExpansion;
  }
  
  public Node nextNode()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    if (fRoot == null) {
      return null;
    }
    Node localNode = fCurrentNode;
    boolean bool = false;
    while (!bool)
    {
      if ((!fForward) && (localNode != null)) {
        localNode = fCurrentNode;
      } else if ((!fEntityReferenceExpansion) && (localNode != null) && (localNode.getNodeType() == 5)) {
        localNode = nextNode(localNode, false);
      } else {
        localNode = nextNode(localNode, true);
      }
      fForward = true;
      if (localNode == null) {
        return null;
      }
      bool = acceptNode(localNode);
      if (bool)
      {
        fCurrentNode = localNode;
        return fCurrentNode;
      }
    }
    return null;
  }
  
  public Node previousNode()
  {
    if (fDetach) {
      throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", null));
    }
    if ((fRoot == null) || (fCurrentNode == null)) {
      return null;
    }
    Node localNode = fCurrentNode;
    boolean bool = false;
    while (!bool)
    {
      if ((fForward) && (localNode != null)) {
        localNode = fCurrentNode;
      } else {
        localNode = previousNode(localNode);
      }
      fForward = false;
      if (localNode == null) {
        return null;
      }
      bool = acceptNode(localNode);
      if (bool)
      {
        fCurrentNode = localNode;
        return fCurrentNode;
      }
    }
    return null;
  }
  
  boolean acceptNode(Node paramNode)
  {
    if (fNodeFilter == null) {
      return (fWhatToShow & 1 << paramNode.getNodeType() - 1) != 0;
    }
    return ((fWhatToShow & 1 << paramNode.getNodeType() - 1) != 0) && (fNodeFilter.acceptNode(paramNode) == 1);
  }
  
  Node matchNodeOrParent(Node paramNode)
  {
    if (fCurrentNode == null) {
      return null;
    }
    for (Node localNode = fCurrentNode; localNode != fRoot; localNode = localNode.getParentNode()) {
      if (paramNode == localNode) {
        return localNode;
      }
    }
    return null;
  }
  
  Node nextNode(Node paramNode, boolean paramBoolean)
  {
    if (paramNode == null) {
      return fRoot;
    }
    if ((paramBoolean) && (paramNode.hasChildNodes()))
    {
      localNode1 = paramNode.getFirstChild();
      return localNode1;
    }
    if (paramNode == fRoot) {
      return null;
    }
    Node localNode1 = paramNode.getNextSibling();
    if (localNode1 != null) {
      return localNode1;
    }
    for (Node localNode2 = paramNode.getParentNode(); (localNode2 != null) && (localNode2 != fRoot); localNode2 = localNode2.getParentNode())
    {
      localNode1 = localNode2.getNextSibling();
      if (localNode1 != null) {
        return localNode1;
      }
    }
    return null;
  }
  
  Node previousNode(Node paramNode)
  {
    if (paramNode == fRoot) {
      return null;
    }
    Node localNode = paramNode.getPreviousSibling();
    if (localNode == null)
    {
      localNode = paramNode.getParentNode();
      return localNode;
    }
    if (localNode.hasChildNodes()) {
      if ((!fEntityReferenceExpansion) && (localNode != null))
      {
        if (localNode.getNodeType() == 5) {}
      }
      else {
        while (localNode.hasChildNodes()) {
          localNode = localNode.getLastChild();
        }
      }
    }
    return localNode;
  }
  
  public void removeNode(Node paramNode)
  {
    if (paramNode == null) {
      return;
    }
    Node localNode1 = matchNodeOrParent(paramNode);
    if (localNode1 == null) {
      return;
    }
    if (fForward)
    {
      fCurrentNode = previousNode(localNode1);
    }
    else
    {
      Node localNode2 = nextNode(localNode1, false);
      if (localNode2 != null)
      {
        fCurrentNode = localNode2;
      }
      else
      {
        fCurrentNode = previousNode(localNode1);
        fForward = true;
      }
    }
  }
  
  public void detach()
  {
    fDetach = true;
    fDocument.removeNodeIterator(this);
  }
}
