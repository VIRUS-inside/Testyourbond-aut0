package com.sun.jna;






public abstract class IntegerType
  extends Number
  implements NativeMapped
{
  private int size;
  




  private Number number;
  




  private boolean unsigned;
  




  private long value;
  




  public IntegerType(int size)
  {
    this(size, 0L, false);
  }
  
  public IntegerType(int size, boolean unsigned)
  {
    this(size, 0L, unsigned);
  }
  
  public IntegerType(int size, long value)
  {
    this(size, value, false);
  }
  
  public IntegerType(int size, long value, boolean unsigned)
  {
    this.size = size;
    this.unsigned = unsigned;
    setValue(value);
  }
  
  public void setValue(long value)
  {
    long truncated = value;
    this.value = value;
    switch (size) {
    case 1: 
      if (unsigned) this.value = (value & 0xFF);
      truncated = (byte)(int)value;
      number = new Byte((byte)(int)value);
      break;
    case 2: 
      if (unsigned) this.value = (value & 0xFFFF);
      truncated = (short)(int)value;
      number = new Short((short)(int)value);
      break;
    case 4: 
      if (unsigned) this.value = (value & 0xFFFFFFFF);
      truncated = (int)value;
      number = new Integer((int)value);
      break;
    case 8: 
      number = new Long(value);
      break;
    case 3: case 5: case 6: case 7: default: 
      throw new IllegalArgumentException("Unsupported size: " + size);
    }
    if (size < 8) {
      long mask = (1L << size * 8) - 1L ^ 0xFFFFFFFFFFFFFFFF;
      if (((value < 0L) && (truncated != value)) || ((value >= 0L) && ((mask & value) != 0L)))
      {
        throw new IllegalArgumentException("Argument value 0x" + Long.toHexString(value) + " exceeds native capacity (" + size + " bytes) mask=0x" + Long.toHexString(mask));
      }
    }
  }
  

  public Object toNative()
  {
    return number;
  }
  
  public Object fromNative(Object nativeValue, FromNativeContext context)
  {
    long value = nativeValue == null ? 0L : ((Number)nativeValue).longValue();
    try
    {
      IntegerType number = (IntegerType)getClass().newInstance();
      number.setValue(value);
      return number;
    }
    catch (InstantiationException e) {
      throw new IllegalArgumentException("Can't instantiate " + getClass());
    }
    catch (IllegalAccessException e)
    {
      throw new IllegalArgumentException("Not allowed to instantiate " + getClass());
    }
  }
  
  public Class nativeType()
  {
    return number.getClass();
  }
  
  public int intValue() {
    return (int)value;
  }
  
  public long longValue() {
    return value;
  }
  
  public float floatValue() {
    return number.floatValue();
  }
  
  public double doubleValue() {
    return number.doubleValue();
  }
  
  public boolean equals(Object rhs) {
    return ((rhs instanceof IntegerType)) && (number.equals(number));
  }
  
  public String toString()
  {
    return number.toString();
  }
  
  public int hashCode() {
    return number.hashCode();
  }
}
