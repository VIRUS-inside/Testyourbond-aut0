package org.apache.xerces.impl.xs.util;

public final class XInt
{
  private final int fValue;
  
  XInt(int paramInt)
  {
    fValue = paramInt;
  }
  
  public final int intValue()
  {
    return fValue;
  }
  
  public final short shortValue()
  {
    return (short)fValue;
  }
  
  public final boolean equals(XInt paramXInt)
  {
    return fValue == fValue;
  }
  
  public String toString()
  {
    return Integer.toString(fValue);
  }
}
