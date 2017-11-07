package org.apache.xerces.impl.dtd.models;

public class CMAny
  extends CMNode
{
  private final int fType;
  private final String fURI;
  private int fPosition = -1;
  
  public CMAny(int paramInt1, String paramString, int paramInt2)
  {
    super(paramInt1);
    fType = paramInt1;
    fURI = paramString;
    fPosition = paramInt2;
  }
  
  final int getType()
  {
    return fType;
  }
  
  final String getURI()
  {
    return fURI;
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
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append('(');
    localStringBuffer.append("##any:uri=");
    localStringBuffer.append(fURI);
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
