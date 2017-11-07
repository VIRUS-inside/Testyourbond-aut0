package org.apache.xerces.impl.xs.models;

public final class XSCMRepeatingLeaf
  extends XSCMLeaf
{
  private final int fMinOccurs;
  private final int fMaxOccurs;
  
  public XSCMRepeatingLeaf(int paramInt1, Object paramObject, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    super(paramInt1, paramObject, paramInt4, paramInt5);
    fMinOccurs = paramInt2;
    fMaxOccurs = paramInt3;
  }
  
  final int getMinOccurs()
  {
    return fMinOccurs;
  }
  
  final int getMaxOccurs()
  {
    return fMaxOccurs;
  }
}
