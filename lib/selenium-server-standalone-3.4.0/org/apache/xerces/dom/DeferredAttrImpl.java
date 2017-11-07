package org.apache.xerces.dom;

public final class DeferredAttrImpl
  extends AttrImpl
  implements DeferredNode
{
  static final long serialVersionUID = 6903232312469148636L;
  protected transient int fNodeIndex;
  
  DeferredAttrImpl(DeferredDocumentImpl paramDeferredDocumentImpl, int paramInt)
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
    DeferredDocumentImpl localDeferredDocumentImpl = (DeferredDocumentImpl)ownerDocument();
    name = localDeferredDocumentImpl.getNodeName(fNodeIndex);
    int i = localDeferredDocumentImpl.getNodeExtra(fNodeIndex);
    isSpecified((i & 0x20) != 0);
    isIdAttribute((i & 0x200) != 0);
    int j = localDeferredDocumentImpl.getLastChild(fNodeIndex);
    type = localDeferredDocumentImpl.getTypeInfo(j);
  }
  
  protected void synchronizeChildren()
  {
    DeferredDocumentImpl localDeferredDocumentImpl = (DeferredDocumentImpl)ownerDocument();
    localDeferredDocumentImpl.synchronizeChildren(this, fNodeIndex);
  }
}
