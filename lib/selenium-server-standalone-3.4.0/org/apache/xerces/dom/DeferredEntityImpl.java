package org.apache.xerces.dom;

public class DeferredEntityImpl
  extends EntityImpl
  implements DeferredNode
{
  static final long serialVersionUID = 4760180431078941638L;
  protected transient int fNodeIndex;
  
  DeferredEntityImpl(DeferredDocumentImpl paramDeferredDocumentImpl, int paramInt)
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
    publicId = localDeferredDocumentImpl.getNodeValue(fNodeIndex);
    systemId = localDeferredDocumentImpl.getNodeURI(fNodeIndex);
    int i = localDeferredDocumentImpl.getNodeExtra(fNodeIndex);
    localDeferredDocumentImpl.getNodeType(i);
    notationName = localDeferredDocumentImpl.getNodeName(i);
    version = localDeferredDocumentImpl.getNodeValue(i);
    encoding = localDeferredDocumentImpl.getNodeURI(i);
    int j = localDeferredDocumentImpl.getNodeExtra(i);
    baseURI = localDeferredDocumentImpl.getNodeName(j);
    inputEncoding = localDeferredDocumentImpl.getNodeValue(j);
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
