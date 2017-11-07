package org.apache.xerces.impl.dtd.models;

public class CMStateSet
{
  int fBitCount;
  int fByteCount;
  int fBits1;
  int fBits2;
  byte[] fByteArray;
  
  public CMStateSet(int paramInt)
  {
    fBitCount = paramInt;
    if (fBitCount < 0) {
      throw new RuntimeException("ImplementationMessages.VAL_CMSI");
    }
    if (fBitCount > 64)
    {
      fByteCount = (fBitCount / 8);
      if (fBitCount % 8 != 0) {
        fByteCount += 1;
      }
      fByteArray = new byte[fByteCount];
    }
    zeroBits();
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      localStringBuffer.append('{');
      for (int i = 0; i < fBitCount; i++) {
        if (getBit(i)) {
          localStringBuffer.append(' ').append(i);
        }
      }
      localStringBuffer.append(" }");
    }
    catch (RuntimeException localRuntimeException) {}
    return localStringBuffer.toString();
  }
  
  public final void intersection(CMStateSet paramCMStateSet)
  {
    if (fBitCount < 65)
    {
      fBits1 &= fBits1;
      fBits2 &= fBits2;
    }
    else
    {
      for (int i = fByteCount - 1; i >= 0; i--)
      {
        int tmp53_52 = i;
        byte[] tmp53_49 = fByteArray;
        tmp53_49[tmp53_52] = ((byte)(tmp53_49[tmp53_52] & fByteArray[i]));
      }
    }
  }
  
  public final boolean getBit(int paramInt)
  {
    if (paramInt >= fBitCount) {
      throw new RuntimeException("ImplementationMessages.VAL_CMSI");
    }
    if (fBitCount < 65)
    {
      i = 1 << paramInt % 32;
      if (paramInt < 32) {
        return (fBits1 & i) != 0;
      }
      return (fBits2 & i) != 0;
    }
    int i = (byte)(1 << paramInt % 8);
    int j = paramInt >> 3;
    return (fByteArray[j] & i) != 0;
  }
  
  public final boolean isEmpty()
  {
    if (fBitCount < 65) {
      return (fBits1 == 0) && (fBits2 == 0);
    }
    for (int i = fByteCount - 1; i >= 0; i--) {
      if (fByteArray[i] != 0) {
        return false;
      }
    }
    return true;
  }
  
  final boolean isSameSet(CMStateSet paramCMStateSet)
  {
    if (fBitCount != fBitCount) {
      return false;
    }
    if (fBitCount < 65) {
      return (fBits1 == fBits1) && (fBits2 == fBits2);
    }
    for (int i = fByteCount - 1; i >= 0; i--) {
      if (fByteArray[i] != fByteArray[i]) {
        return false;
      }
    }
    return true;
  }
  
  public final void union(CMStateSet paramCMStateSet)
  {
    if (fBitCount < 65)
    {
      fBits1 |= fBits1;
      fBits2 |= fBits2;
    }
    else
    {
      for (int i = fByteCount - 1; i >= 0; i--)
      {
        int tmp53_52 = i;
        byte[] tmp53_49 = fByteArray;
        tmp53_49[tmp53_52] = ((byte)(tmp53_49[tmp53_52] | fByteArray[i]));
      }
    }
  }
  
  public final void setBit(int paramInt)
  {
    if (paramInt >= fBitCount) {
      throw new RuntimeException("ImplementationMessages.VAL_CMSI");
    }
    int i;
    if (fBitCount < 65)
    {
      i = 1 << paramInt % 32;
      if (paramInt < 32)
      {
        fBits1 &= (i ^ 0xFFFFFFFF);
        fBits1 |= i;
      }
      else
      {
        fBits2 &= (i ^ 0xFFFFFFFF);
        fBits2 |= i;
      }
    }
    else
    {
      i = (byte)(1 << paramInt % 8);
      int j = paramInt >> 3;
      int tmp107_106 = j;
      byte[] tmp107_103 = fByteArray;
      tmp107_103[tmp107_106] = ((byte)(tmp107_103[tmp107_106] & (i ^ 0xFFFFFFFF)));
      int tmp120_119 = j;
      byte[] tmp120_116 = fByteArray;
      tmp120_116[tmp120_119] = ((byte)(tmp120_116[tmp120_119] | i));
    }
  }
  
  public final void setTo(CMStateSet paramCMStateSet)
  {
    if (fBitCount != fBitCount) {
      throw new RuntimeException("ImplementationMessages.VAL_CMSI");
    }
    if (fBitCount < 65)
    {
      fBits1 = fBits1;
      fBits2 = fBits2;
    }
    else
    {
      for (int i = fByteCount - 1; i >= 0; i--) {
        fByteArray[i] = fByteArray[i];
      }
    }
  }
  
  public final void zeroBits()
  {
    if (fBitCount < 65)
    {
      fBits1 = 0;
      fBits2 = 0;
    }
    else
    {
      for (int i = fByteCount - 1; i >= 0; i--) {
        fByteArray[i] = 0;
      }
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof CMStateSet)) {
      return false;
    }
    return isSameSet((CMStateSet)paramObject);
  }
  
  public int hashCode()
  {
    if (fBitCount < 65) {
      return fBits1 + fBits2 * 31;
    }
    int i = 0;
    for (int j = fByteCount - 1; j >= 0; j--) {
      i = fByteArray[j] + i * 31;
    }
    return i;
  }
}
