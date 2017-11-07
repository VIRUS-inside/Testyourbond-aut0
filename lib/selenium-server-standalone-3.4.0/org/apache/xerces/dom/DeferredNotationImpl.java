package org.apache.xerces.dom;

public class DeferredNotationImpl
  extends NotationImpl
  implements DeferredNode
{
  static final long serialVersionUID = 5705337172887990848L;
  protected transient int fNodeIndex;
  
  DeferredNotationImpl(DeferredDocumentImpl paramDeferredDocumentImpl, int paramInt)
  {
    super(paramDeferredDocumentImpl, null);
    fNodeIndex = paramInt;
    needsSyncData(true);
  }
  
  public int getNodeIndex()
  {
    return fNodeIndex;
  }
  
  protected void synchronizeData()
  {
    needsSyncData(false);
    DeferredDocumentImpl localDeferredDocumentImpl = (DeferredDocumentImpl)ownerDocument();
    name = localDeferredDocumentImpl.getNodeName(fNodeIndex);
    localDeferredDocumentImpl.getNodeType(fNodeIndex);
    publicId = localDeferredDocumentImpl.getNodeValue(fNodeIndex);
    systemId = localDeferredDocumentImpl.getNodeURI(fNodeIndex);
    int i = localDeferredDocumentImpl.getNodeExtra(fNodeIndex);
    localDeferredDocumentImpl.getNodeType(i);
    baseURI = localDeferredDocumentImpl.getNodeName(i);
  }
}
