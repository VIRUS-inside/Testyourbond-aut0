package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;








































public class MutableByte
  extends Number
  implements Comparable<MutableByte>, Mutable<Number>
{
  private static final long serialVersionUID = -1585823265L;
  private byte value;
  
  public MutableByte() {}
  
  public MutableByte(byte value)
  {
    this.value = value;
  }
  






  public MutableByte(Number value)
  {
    this.value = value.byteValue();
  }
  






  public MutableByte(String value)
    throws NumberFormatException
  {
    this.value = Byte.parseByte(value);
  }
  






  public Byte getValue()
  {
    return Byte.valueOf(value);
  }
  




  public void setValue(byte value)
  {
    this.value = value;
  }
  






  public void setValue(Number value)
  {
    this.value = value.byteValue();
  }
  





  public void increment()
  {
    value = ((byte)(value + 1));
  }
  






  public byte getAndIncrement()
  {
    byte last = value;
    value = ((byte)(value + 1));
    return last;
  }
  






  public byte incrementAndGet()
  {
    value = ((byte)(value + 1));
    return value;
  }
  




  public void decrement()
  {
    value = ((byte)(value - 1));
  }
  






  public byte getAndDecrement()
  {
    byte last = value;
    value = ((byte)(value - 1));
    return last;
  }
  






  public byte decrementAndGet()
  {
    value = ((byte)(value - 1));
    return value;
  }
  






  public void add(byte operand)
  {
    value = ((byte)(value + operand));
  }
  






  public void add(Number operand)
  {
    value = ((byte)(value + operand.byteValue()));
  }
  





  public void subtract(byte operand)
  {
    value = ((byte)(value - operand));
  }
  






  public void subtract(Number operand)
  {
    value = ((byte)(value - operand.byteValue()));
  }
  







  public byte addAndGet(byte operand)
  {
    value = ((byte)(value + operand));
    return value;
  }
  








  public byte addAndGet(Number operand)
  {
    value = ((byte)(value + operand.byteValue()));
    return value;
  }
  







  public byte getAndAdd(byte operand)
  {
    byte last = value;
    value = ((byte)(value + operand));
    return last;
  }
  








  public byte getAndAdd(Number operand)
  {
    byte last = value;
    value = ((byte)(value + operand.byteValue()));
    return last;
  }
  







  public byte byteValue()
  {
    return value;
  }
  





  public int intValue()
  {
    return value;
  }
  





  public long longValue()
  {
    return value;
  }
  





  public float floatValue()
  {
    return value;
  }
  





  public double doubleValue()
  {
    return value;
  }
  





  public Byte toByte()
  {
    return Byte.valueOf(byteValue());
  }
  









  public boolean equals(Object obj)
  {
    if ((obj instanceof MutableByte)) {
      return value == ((MutableByte)obj).byteValue();
    }
    return false;
  }
  





  public int hashCode()
  {
    return value;
  }
  







  public int compareTo(MutableByte other)
  {
    return NumberUtils.compare(value, value);
  }
  






  public String toString()
  {
    return String.valueOf(value);
  }
}
