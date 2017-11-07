package org.apache.xerces.dom;

import org.w3c.dom.Node;

public abstract class ChildNode
  extends NodeImpl
{
  static final long serialVersionUID = -6112455738802414002L;
  protected ChildNode previousSibling;
  protected ChildNode nextSibling;
  
  protected ChildNode(CoreDocumentImpl paramCoreDocumentImpl)
  {
    super(paramCoreDocumentImpl);
  }
  
  public ChildNode() {}
  
  public Node cloneNode(boolean paramBoolean)
  {
    ChildNode localChildNode = (ChildNode)super.cloneNode(paramBoolean);
    previousSibling = null;
    nextSibling = null;
    localChildNode.isFirstChild(false);
    return localChildNode;
  }
  
  public Node getParentNode()
  {
    return isOwned() ? ownerNode : null;
  }
  
  final NodeImpl parentNode()
  {
    return isOwned() ? ownerNode : null;
  }
  
  public Node getNextSibling()
  {
    return nextSibling;
  }
  
  public Node getPreviousSibling()
  {
    return isFirstChild() ? null : previousSibling;
  }
  
  final ChildNode previousSibling()
  {
    return isFirstChild() ? null : previousSibling;
  }
}
