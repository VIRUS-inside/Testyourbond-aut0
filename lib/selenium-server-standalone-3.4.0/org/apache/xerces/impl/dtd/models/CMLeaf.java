package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.xni.QName;

public class CMLeaf
  extends CMNode
{
  private final QName fElement = new QName();
  private int fPosition = -1;
  
  public CMLeaf(QName paramQName, int paramInt)
  {
    super(0);
    fElement.setValues(paramQName);
    fPosition = paramInt;
  }
  
  public CMLeaf(QName paramQName)
  {
    super(0);
    fElement.setValues(paramQName);
  }
  
  final QName getElement()
  {
    return fElement;
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
    StringBuffer localStringBuffer = new StringBuffer(fElement.toString());
    localStringBuffer.append(" (");
    localStringBuffer.append(fElement.uri);
    localStringBuffer.append(',');
    localStringBuffer.append(fElement.localpart);
    localStringBuffer.append(')');
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
