package org.apache.xerces.dom;

public final class DeferredAttrNSImpl
  extends AttrNSImpl
  implements DeferredNode
{
  static final long serialVersionUID = 6074924934945957154L;
  protected transient int fNodeIndex;
  
  DeferredAttrNSImpl(DeferredDocumentImpl paramDeferredDocumentImpl, int paramInt)
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
    int i = name.indexOf(':');
    if (i < 0) {
      localName = name;
    } else {
      localName = name.substring(i + 1);
    }
    int j = localDeferredDocumentImpl.getNodeExtra(fNodeIndex);
    isSpecified((j & 0x20) != 0);
    isIdAttribute((j & 0x200) != 0);
    namespaceURI = localDeferredDocumentImpl.getNodeURI(fNodeIndex);
    int k = localDeferredDocumentImpl.getLastChild(fNodeIndex);
    type = localDeferredDocumentImpl.getTypeInfo(k);
  }
  
  protected void synchronizeChildren()
  {
    DeferredDocumentImpl localDeferredDocumentImpl = (DeferredDocumentImpl)ownerDocument();
    localDeferredDocumentImpl.synchronizeChildren(this, fNodeIndex);
  }
}
