package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;








































public class MutableShort
  extends Number
  implements Comparable<MutableShort>, Mutable<Number>
{
  private static final long serialVersionUID = -2135791679L;
  private short value;
  
  public MutableShort() {}
  
  public MutableShort(short value)
  {
    this.value = value;
  }
  






  public MutableShort(Number value)
  {
    this.value = value.shortValue();
  }
  






  public MutableShort(String value)
    throws NumberFormatException
  {
    this.value = Short.parseShort(value);
  }
  






  public Short getValue()
  {
    return Short.valueOf(value);
  }
  




  public void setValue(short value)
  {
    this.value = value;
  }
  






  public void setValue(Number value)
  {
    this.value = value.shortValue();
  }
  





  public void increment()
  {
    value = ((short)(value + 1));
  }
  






  public short getAndIncrement()
  {
    short last = value;
    value = ((short)(value + 1));
    return last;
  }
  






  public short incrementAndGet()
  {
    value = ((short)(value + 1));
    return value;
  }
  




  public void decrement()
  {
    value = ((short)(value - 1));
  }
  






  public short getAndDecrement()
  {
    short last = value;
    value = ((short)(value - 1));
    return last;
  }
  






  public short decrementAndGet()
  {
    value = ((short)(value - 1));
    return value;
  }
  






  public void add(short operand)
  {
    value = ((short)(value + operand));
  }
  






  public void add(Number operand)
  {
    value = ((short)(value + operand.shortValue()));
  }
  





  public void subtract(short operand)
  {
    value = ((short)(value - operand));
  }
  






  public void subtract(Number operand)
  {
    value = ((short)(value - operand.shortValue()));
  }
  







  public short addAndGet(short operand)
  {
    value = ((short)(value + operand));
    return value;
  }
  








  public short addAndGet(Number operand)
  {
    value = ((short)(value + operand.shortValue()));
    return value;
  }
  







  public short getAndAdd(short operand)
  {
    short last = value;
    value = ((short)(value + operand));
    return last;
  }
  








  public short getAndAdd(Number operand)
  {
    short last = value;
    value = ((short)(value + operand.shortValue()));
    return last;
  }
  







  public short shortValue()
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
  





  public Short toShort()
  {
    return Short.valueOf(shortValue());
  }
  









  public boolean equals(Object obj)
  {
    if ((obj instanceof MutableShort)) {
      return value == ((MutableShort)obj).shortValue();
    }
    return false;
  }
  





  public int hashCode()
  {
    return value;
  }
  







  public int compareTo(MutableShort other)
  {
    return NumberUtils.compare(value, value);
  }
  






  public String toString()
  {
    return String.valueOf(value);
  }
}
