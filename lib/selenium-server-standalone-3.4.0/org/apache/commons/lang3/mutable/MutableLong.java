package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;








































public class MutableLong
  extends Number
  implements Comparable<MutableLong>, Mutable<Number>
{
  private static final long serialVersionUID = 62986528375L;
  private long value;
  
  public MutableLong() {}
  
  public MutableLong(long value)
  {
    this.value = value;
  }
  






  public MutableLong(Number value)
  {
    this.value = value.longValue();
  }
  






  public MutableLong(String value)
    throws NumberFormatException
  {
    this.value = Long.parseLong(value);
  }
  






  public Long getValue()
  {
    return Long.valueOf(value);
  }
  




  public void setValue(long value)
  {
    this.value = value;
  }
  






  public void setValue(Number value)
  {
    this.value = value.longValue();
  }
  





  public void increment()
  {
    value += 1L;
  }
  






  public long getAndIncrement()
  {
    long last = value;
    value += 1L;
    return last;
  }
  






  public long incrementAndGet()
  {
    value += 1L;
    return value;
  }
  




  public void decrement()
  {
    value -= 1L;
  }
  






  public long getAndDecrement()
  {
    long last = value;
    value -= 1L;
    return last;
  }
  






  public long decrementAndGet()
  {
    value -= 1L;
    return value;
  }
  






  public void add(long operand)
  {
    value += operand;
  }
  






  public void add(Number operand)
  {
    value += operand.longValue();
  }
  





  public void subtract(long operand)
  {
    value -= operand;
  }
  






  public void subtract(Number operand)
  {
    value -= operand.longValue();
  }
  







  public long addAndGet(long operand)
  {
    value += operand;
    return value;
  }
  








  public long addAndGet(Number operand)
  {
    value += operand.longValue();
    return value;
  }
  







  public long getAndAdd(long operand)
  {
    long last = value;
    value += operand;
    return last;
  }
  








  public long getAndAdd(Number operand)
  {
    long last = value;
    value += operand.longValue();
    return last;
  }
  







  public int intValue()
  {
    return (int)value;
  }
  





  public long longValue()
  {
    return value;
  }
  





  public float floatValue()
  {
    return (float)value;
  }
  





  public double doubleValue()
  {
    return value;
  }
  





  public Long toLong()
  {
    return Long.valueOf(longValue());
  }
  









  public boolean equals(Object obj)
  {
    if ((obj instanceof MutableLong)) {
      return value == ((MutableLong)obj).longValue();
    }
    return false;
  }
  





  public int hashCode()
  {
    return (int)(value ^ value >>> 32);
  }
  







  public int compareTo(MutableLong other)
  {
    return NumberUtils.compare(value, value);
  }
  






  public String toString()
  {
    return String.valueOf(value);
  }
}
