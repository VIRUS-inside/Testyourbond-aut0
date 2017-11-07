package org.apache.xerces.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

public class TreeWalkerImpl
  implements TreeWalker
{
  private boolean fEntityReferenceExpansion = false;
  int fWhatToShow = -1;
  NodeFilter fNodeFilter;
  Node fCurrentNode;
  Node fRoot;
  private boolean fUseIsSameNode;
  
  public TreeWalkerImpl(Node paramNode, int paramInt, NodeFilter paramNodeFilter, boolean paramBoolean)
  {
    fCurrentNode = paramNode;
    fRoot = paramNode;
    fUseIsSameNode = useIsSameNode(paramNode);
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
  
  public void setWhatShow(int paramInt)
  {
    fWhatToShow = paramInt;
  }
  
  public NodeFilter getFilter()
  {
    return fNodeFilter;
  }
  
  public boolean getExpandEntityReferences()
  {
    return fEntityReferenceExpansion;
  }
  
  public Node getCurrentNode()
  {
    return fCurrentNode;
  }
  
  public void setCurrentNode(Node paramNode)
  {
    if (paramNode == null)
    {
      String str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", null);
      throw new DOMException((short)9, str);
    }
    fCurrentNode = paramNode;
  }
  
  public Node parentNode()
  {
    if (fCurrentNode == null) {
      return null;
    }
    Node localNode = getParentNode(fCurrentNode);
    if (localNode != null) {
      fCurrentNode = localNode;
    }
    return localNode;
  }
  
  public Node firstChild()
  {
    if (fCurrentNode == null) {
      return null;
    }
    Node localNode = getFirstChild(fCurrentNode);
    if (localNode != null) {
      fCurrentNode = localNode;
    }
    return localNode;
  }
  
  public Node lastChild()
  {
    if (fCurrentNode == null) {
      return null;
    }
    Node localNode = getLastChild(fCurrentNode);
    if (localNode != null) {
      fCurrentNode = localNode;
    }
    return localNode;
  }
  
  public Node previousSibling()
  {
    if (fCurrentNode == null) {
      return null;
    }
    Node localNode = getPreviousSibling(fCurrentNode);
    if (localNode != null) {
      fCurrentNode = localNode;
    }
    return localNode;
  }
  
  public Node nextSibling()
  {
    if (fCurrentNode == null) {
      return null;
    }
    Node localNode = getNextSibling(fCurrentNode);
    if (localNode != null) {
      fCurrentNode = localNode;
    }
    return localNode;
  }
  
  public Node previousNode()
  {
    if (fCurrentNode == null) {
      return null;
    }
    Node localNode = getPreviousSibling(fCurrentNode);
    if (localNode == null)
    {
      localNode = getParentNode(fCurrentNode);
      if (localNode != null)
      {
        fCurrentNode = localNode;
        return fCurrentNode;
      }
      return null;
    }
    Object localObject1 = getLastChild(localNode);
    Object localObject2 = localObject1;
    while (localObject1 != null)
    {
      localObject2 = localObject1;
      localObject1 = getLastChild(localObject2);
    }
    localObject1 = localObject2;
    if (localObject1 != null)
    {
      fCurrentNode = ((Node)localObject1);
      return fCurrentNode;
    }
    if (localNode != null)
    {
      fCurrentNode = localNode;
      return fCurrentNode;
    }
    return null;
  }
  
  public Node nextNode()
  {
    if (fCurrentNode == null) {
      return null;
    }
    Node localNode1 = getFirstChild(fCurrentNode);
    if (localNode1 != null)
    {
      fCurrentNode = localNode1;
      return localNode1;
    }
    localNode1 = getNextSibling(fCurrentNode);
    if (localNode1 != null)
    {
      fCurrentNode = localNode1;
      return localNode1;
    }
    for (Node localNode2 = getParentNode(fCurrentNode); localNode2 != null; localNode2 = getParentNode(localNode2))
    {
      localNode1 = getNextSibling(localNode2);
      if (localNode1 != null)
      {
        fCurrentNode = localNode1;
        return localNode1;
      }
    }
    return null;
  }
  
  Node getParentNode(Node paramNode)
  {
    if ((paramNode == null) || (isSameNode(paramNode, fRoot))) {
      return null;
    }
    Node localNode = paramNode.getParentNode();
    if (localNode == null) {
      return null;
    }
    int i = acceptNode(localNode);
    if (i == 1) {
      return localNode;
    }
    return getParentNode(localNode);
  }
  
  Node getNextSibling(Node paramNode)
  {
    return getNextSibling(paramNode, fRoot);
  }
  
  Node getNextSibling(Node paramNode1, Node paramNode2)
  {
    if ((paramNode1 == null) || (isSameNode(paramNode1, paramNode2))) {
      return null;
    }
    Node localNode1 = paramNode1.getNextSibling();
    if (localNode1 == null)
    {
      localNode1 = paramNode1.getParentNode();
      if ((localNode1 == null) || (isSameNode(localNode1, paramNode2))) {
        return null;
      }
      i = acceptNode(localNode1);
      if (i == 3) {
        return getNextSibling(localNode1, paramNode2);
      }
      return null;
    }
    int i = acceptNode(localNode1);
    if (i == 1) {
      return localNode1;
    }
    if (i == 3)
    {
      Node localNode2 = getFirstChild(localNode1);
      if (localNode2 == null) {
        return getNextSibling(localNode1, paramNode2);
      }
      return localNode2;
    }
    return getNextSibling(localNode1, paramNode2);
  }
  
  Node getPreviousSibling(Node paramNode)
  {
    return getPreviousSibling(paramNode, fRoot);
  }
  
  Node getPreviousSibling(Node paramNode1, Node paramNode2)
  {
    if ((paramNode1 == null) || (isSameNode(paramNode1, paramNode2))) {
      return null;
    }
    Node localNode1 = paramNode1.getPreviousSibling();
    if (localNode1 == null)
    {
      localNode1 = paramNode1.getParentNode();
      if ((localNode1 == null) || (isSameNode(localNode1, paramNode2))) {
        return null;
      }
      i = acceptNode(localNode1);
      if (i == 3) {
        return getPreviousSibling(localNode1, paramNode2);
      }
      return null;
    }
    int i = acceptNode(localNode1);
    if (i == 1) {
      return localNode1;
    }
    if (i == 3)
    {
      Node localNode2 = getLastChild(localNode1);
      if (localNode2 == null) {
        return getPreviousSibling(localNode1, paramNode2);
      }
      return localNode2;
    }
    return getPreviousSibling(localNode1, paramNode2);
  }
  
  Node getFirstChild(Node paramNode)
  {
    if (paramNode == null) {
      return null;
    }
    if ((!fEntityReferenceExpansion) && (paramNode.getNodeType() == 5)) {
      return null;
    }
    Node localNode1 = paramNode.getFirstChild();
    if (localNode1 == null) {
      return null;
    }
    int i = acceptNode(localNode1);
    if (i == 1) {
      return localNode1;
    }
    if ((i == 3) && (localNode1.hasChildNodes()))
    {
      Node localNode2 = getFirstChild(localNode1);
      if (localNode2 == null) {
        return getNextSibling(localNode1, paramNode);
      }
      return localNode2;
    }
    return getNextSibling(localNode1, paramNode);
  }
  
  Node getLastChild(Node paramNode)
  {
    if (paramNode == null) {
      return null;
    }
    if ((!fEntityReferenceExpansion) && (paramNode.getNodeType() == 5)) {
      return null;
    }
    Node localNode1 = paramNode.getLastChild();
    if (localNode1 == null) {
      return null;
    }
    int i = acceptNode(localNode1);
    if (i == 1) {
      return localNode1;
    }
    if ((i == 3) && (localNode1.hasChildNodes()))
    {
      Node localNode2 = getLastChild(localNode1);
      if (localNode2 == null) {
        return getPreviousSibling(localNode1, paramNode);
      }
      return localNode2;
    }
    return getPreviousSibling(localNode1, paramNode);
  }
  
  short acceptNode(Node paramNode)
  {
    if (fNodeFilter == null)
    {
      if ((fWhatToShow & 1 << paramNode.getNodeType() - 1) != 0) {
        return 1;
      }
      return 3;
    }
    if ((fWhatToShow & 1 << paramNode.getNodeType() - 1) != 0) {
      return fNodeFilter.acceptNode(paramNode);
    }
    return 3;
  }
  
  private boolean useIsSameNode(Node paramNode)
  {
    if ((paramNode instanceof NodeImpl)) {
      return false;
    }
    Document localDocument = paramNode.getNodeType() == 9 ? (Document)paramNode : paramNode.getOwnerDocument();
    return (localDocument != null) && (localDocument.getImplementation().hasFeature("Core", "3.0"));
  }
  
  private boolean isSameNode(Node paramNode1, Node paramNode2)
  {
    return paramNode1 == paramNode2 ? true : fUseIsSameNode ? paramNode1.isSameNode(paramNode2) : false;
  }
}
