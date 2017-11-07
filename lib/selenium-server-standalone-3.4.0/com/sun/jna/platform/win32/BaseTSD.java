package com.sun.jna.platform.win32;

import com.sun.jna.IntegerType;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.win32.StdCallLibrary;

















public abstract interface BaseTSD
  extends StdCallLibrary
{
  public static class LONG_PTR
    extends IntegerType
  {
    public LONG_PTR()
    {
      this(0L);
    }
    
    public LONG_PTR(long value) {
      super(value);
    }
    
    public Pointer toPointer() {
      return Pointer.createConstant(longValue());
    }
  }
  
  public static class SSIZE_T
    extends BaseTSD.LONG_PTR
  {
    public SSIZE_T()
    {
      this(0L);
    }
    
    public SSIZE_T(long value) {
      super();
    }
  }
  
  public static class ULONG_PTR
    extends IntegerType
  {
    public ULONG_PTR()
    {
      this(0L);
    }
    
    public ULONG_PTR(long value) {
      super(value, true);
    }
    
    public Pointer toPointer() {
      return Pointer.createConstant(longValue());
    }
  }
  


  public static class ULONG_PTRByReference
    extends ByReference
  {
    public ULONG_PTRByReference() { this(new BaseTSD.ULONG_PTR(0L)); }
    
    public ULONG_PTRByReference(BaseTSD.ULONG_PTR value) {
      super();
      setValue(value);
    }
    
    public void setValue(BaseTSD.ULONG_PTR value) { if (Pointer.SIZE == 4) {
        getPointer().setInt(0L, value.intValue());
      }
      else
        getPointer().setLong(0L, value.longValue());
    }
    
    public BaseTSD.ULONG_PTR getValue() {
      return new BaseTSD.ULONG_PTR(Pointer.SIZE == 4 ? getPointer().getInt(0L) : getPointer().getLong(0L));
    }
  }
  



  public static class DWORD_PTR
    extends IntegerType
  {
    public DWORD_PTR()
    {
      this(0L);
    }
    
    public DWORD_PTR(long value) {
      super(value);
    }
  }
  

  public static class SIZE_T
    extends BaseTSD.ULONG_PTR
  {
    public SIZE_T()
    {
      this(0L);
    }
    
    public SIZE_T(long value) {
      super();
    }
  }
}
