package org.apache.xerces.dom;

public class DeferredProcessingInstructionImpl
  extends ProcessingInstructionImpl
  implements DeferredNode
{
  static final long serialVersionUID = -4643577954293565388L;
  protected transient int fNodeIndex;
  
  DeferredProcessingInstructionImpl(DeferredDocumentImpl paramDeferredDocumentImpl, int paramInt)
  {
    super(paramDeferredDocumentImpl, null, null);
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
    target = localDeferredDocumentImpl.getNodeName(fNodeIndex);
    data = localDeferredDocumentImpl.getNodeValueString(fNodeIndex);
  }
}
