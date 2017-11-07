package org.apache.xerces.dom;

public class DeferredTextImpl
  extends TextImpl
  implements DeferredNode
{
  static final long serialVersionUID = 2310613872100393425L;
  protected transient int fNodeIndex;
  
  DeferredTextImpl(DeferredDocumentImpl paramDeferredDocumentImpl, int paramInt)
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
    isIgnorableWhitespace(localDeferredDocumentImpl.getNodeExtra(fNodeIndex) == 1);
  }
}
