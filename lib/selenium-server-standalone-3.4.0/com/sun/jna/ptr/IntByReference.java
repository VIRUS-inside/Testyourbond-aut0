package com.sun.jna.ptr;

import com.sun.jna.Pointer;









public class IntByReference
  extends ByReference
{
  public IntByReference()
  {
    this(0);
  }
  
  public IntByReference(int value) {
    super(4);
    setValue(value);
  }
  
  public void setValue(int value) {
    getPointer().setInt(0L, value);
  }
  
  public int getValue() {
    return getPointer().getInt(0L);
  }
}
