package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMStateSet;

public class XSCMLeaf
  extends CMNode
{
  private final Object fLeaf;
  private int fParticleId = -1;
  private int fPosition = -1;
  
  public XSCMLeaf(int paramInt1, Object paramObject, int paramInt2, int paramInt3)
  {
    super(paramInt1);
    fLeaf = paramObject;
    fParticleId = paramInt2;
    fPosition = paramInt3;
  }
  
  final Object getLeaf()
  {
    return fLeaf;
  }
  
  final int getParticleId()
  {
    return fParticleId;
  }
  
  final int getPosition()
  {
    return fPosition;
  }
  
  final void setPosition(int paramInt)
  {
    fPosition = paramInt;
  }
  
  public boolean isNullable()
  {
    return fPosition == -1;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer(fLeaf.toString());
    if (fPosition >= 0) {
      localStringBuffer.append(" (Pos:").append(Integer.toString(fPosition)).append(')');
    }
    return localStringBuffer.toString();
  }
  
  protected void calcFirstPos(CMStateSet paramCMStateSet)
  {
    if (fPosition == -1) {
      paramCMStateSet.zeroBits();
    } else {
      paramCMStateSet.setBit(fPosition);
    }
  }
  
  protected void calcLastPos(CMStateSet paramCMStateSet)
  {
    if (fPosition == -1) {
      paramCMStateSet.zeroBits();
    } else {
      paramCMStateSet.setBit(fPosition);
    }
  }
}
