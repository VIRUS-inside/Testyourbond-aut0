package org.apache.xerces.dom;

public class DeferredCommentImpl
  extends CommentImpl
  implements DeferredNode
{
  static final long serialVersionUID = 6498796371083589338L;
  protected transient int fNodeIndex;
  
  DeferredCommentImpl(DeferredDocumentImpl paramDeferredDocumentImpl, int paramInt)
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
