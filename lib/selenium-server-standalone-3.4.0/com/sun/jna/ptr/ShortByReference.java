package com.sun.jna.ptr;

import com.sun.jna.Pointer;









public class ShortByReference
  extends ByReference
{
  public ShortByReference()
  {
    this((short)0);
  }
  
  public ShortByReference(short value) {
    super(2);
    setValue(value);
  }
  
  public void setValue(short value) {
    getPointer().setShort(0L, value);
  }
  
  public short getValue() {
    return getPointer().getShort(0L);
  }
}
