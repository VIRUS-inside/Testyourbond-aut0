package com.sun.jna;









public abstract class PointerType
  implements NativeMapped
{
  private Pointer pointer;
  







  protected PointerType()
  {
    pointer = Pointer.NULL;
  }
  


  protected PointerType(Pointer p)
  {
    pointer = p;
  }
  

  public Class nativeType()
  {
    return Pointer.class;
  }
  
  public Object toNative()
  {
    return getPointer();
  }
  
  public Pointer getPointer()
  {
    return pointer;
  }
  
  public void setPointer(Pointer p) {
    pointer = p;
  }
  






  public Object fromNative(Object nativeValue, FromNativeContext context)
  {
    if (nativeValue == null) {
      return null;
    }
    try {
      PointerType pt = (PointerType)getClass().newInstance();
      pointer = ((Pointer)nativeValue);
      return pt;
    }
    catch (InstantiationException e) {
      throw new IllegalArgumentException("Can't instantiate " + getClass());
    }
    catch (IllegalAccessException e) {
      throw new IllegalArgumentException("Not allowed to instantiate " + getClass());
    }
  }
  


  public int hashCode()
  {
    return pointer != null ? pointer.hashCode() : 0;
  }
  


  public boolean equals(Object o)
  {
    if (o == this) return true;
    if ((o instanceof PointerType)) {
      Pointer p = ((PointerType)o).getPointer();
      if (pointer == null)
        return p == null;
      return pointer.equals(p);
    }
    return false;
  }
  
  public String toString() {
    return pointer.toString() + " (" + super.toString() + ")";
  }
}
