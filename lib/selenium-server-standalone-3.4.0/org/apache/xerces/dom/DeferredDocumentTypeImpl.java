package org.apache.xerces.dom;

import java.io.PrintStream;
import org.w3c.dom.Node;

public class DeferredDocumentTypeImpl
  extends DocumentTypeImpl
  implements DeferredNode
{
  static final long serialVersionUID = -2172579663227313509L;
  protected transient int fNodeIndex;
  
  DeferredDocumentTypeImpl(DeferredDocumentImpl paramDeferredDocumentImpl, int paramInt)
  {
    super(paramDeferredDocumentImpl, null);
    fNodeIndex = paramInt;
    needsSyncData(true);
    needsSyncChildren(true);
  }
  
  public int getNodeIndex()
  {
    return fNodeIndex;
  }
  
  protected void synchronizeData()
  {
    needsSyncData(false);
    DeferredDocumentImpl localDeferredDocumentImpl = (DeferredDocumentImpl)ownerDocument;
    name = localDeferredDocumentImpl.getNodeName(fNodeIndex);
    publicID = localDeferredDocumentImpl.getNodeValue(fNodeIndex);
    systemID = localDeferredDocumentImpl.getNodeURI(fNodeIndex);
    int i = localDeferredDocumentImpl.getNodeExtra(fNodeIndex);
    internalSubset = localDeferredDocumentImpl.getNodeValue(i);
  }
  
  protected void synchronizeChildren()
  {
    boolean bool = ownerDocument().getMutationEvents();
    ownerDocument().setMutationEvents(false);
    needsSyncChildren(false);
    DeferredDocumentImpl localDeferredDocumentImpl = (DeferredDocumentImpl)ownerDocument;
    entities = new NamedNodeMapImpl(this);
    notations = new NamedNodeMapImpl(this);
    elements = new NamedNodeMapImpl(this);
    Object localObject = null;
    for (int i = localDeferredDocumentImpl.getLastChild(fNodeIndex); i != -1; i = localDeferredDocumentImpl.getPrevSibling(i))
    {
      DeferredNode localDeferredNode = localDeferredDocumentImpl.getNodeObject(i);
      int j = localDeferredNode.getNodeType();
      switch (j)
      {
      case 6: 
        entities.setNamedItem(localDeferredNode);
        break;
      case 12: 
        notations.setNamedItem(localDeferredNode);
        break;
      case 21: 
        elements.setNamedItem(localDeferredNode);
        break;
      case 1: 
        if (getOwnerDocumentallowGrammarAccess)
        {
          insertBefore(localDeferredNode, (Node)localObject);
          localObject = localDeferredNode;
        }
        break;
      }
      System.out.println("DeferredDocumentTypeImpl#synchronizeInfo: node.getNodeType() = " + localDeferredNode.getNodeType() + ", class = " + localDeferredNode.getClass().getName());
    }
    ownerDocument().setMutationEvents(bool);
    setReadOnly(true, false);
  }
}
