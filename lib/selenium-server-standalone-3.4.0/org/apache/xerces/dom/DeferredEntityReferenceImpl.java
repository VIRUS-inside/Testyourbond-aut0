package org.apache.xerces.dom;

public class DeferredEntityReferenceImpl
  extends EntityReferenceImpl
  implements DeferredNode
{
  static final long serialVersionUID = 390319091370032223L;
  protected transient int fNodeIndex;
  
  DeferredEntityReferenceImpl(DeferredDocumentImpl paramDeferredDocumentImpl, int paramInt)
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
    DeferredDocumentImpl localDeferredDocumentImpl = (DeferredDocumentImpl)ownerDocument;
    name = localDeferredDocumentImpl.getNodeName(fNodeIndex);
    baseURI = localDeferredDocumentImpl.getNodeValue(fNodeIndex);
  }
  
  protected void synchronizeChildren()
  {
    needsSyncChildren(false);
    isReadOnly(false);
    DeferredDocumentImpl localDeferredDocumentImpl = (DeferredDocumentImpl)ownerDocument();
    localDeferredDocumentImpl.synchronizeChildren(this, fNodeIndex);
    setReadOnly(true, true);
  }
}
