package org.apache.xerces.dom;

public class DeferredCDATASectionImpl
  extends CDATASectionImpl
  implements DeferredNode
{
  static final long serialVersionUID = 1983580632355645726L;
  protected transient int fNodeIndex;
  
  DeferredCDATASectionImpl(DeferredDocumentImpl paramDeferredDocumentImpl, int paramInt)
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
    data = localDeferredDocumentImpl.getNodeValueString(fNodeIndex);
  }
}
