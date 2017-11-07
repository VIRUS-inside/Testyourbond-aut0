package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;








































public class MutableInt
  extends Number
  implements Comparable<MutableInt>, Mutable<Number>
{
  private static final long serialVersionUID = 512176391864L;
  private int value;
  
  public MutableInt() {}
  
  public MutableInt(int value)
  {
    this.value = value;
  }
  






  public MutableInt(Number value)
  {
    this.value = value.intValue();
  }
  






  public MutableInt(String value)
    throws NumberFormatException
  {
    this.value = Integer.parseInt(value);
  }
  






  public Integer getValue()
  {
    return Integer.valueOf(value);
  }
  




  public void setValue(int value)
  {
    this.value = value;
  }
  






  public void setValue(Number value)
  {
    this.value = value.intValue();
  }
  





  public void increment()
  {
    value += 1;
  }
  






  public int getAndIncrement()
  {
    int last = value;
    value += 1;
    return last;
  }
  






  public int incrementAndGet()
  {
    value += 1;
    return value;
  }
  




  public void decrement()
  {
    value -= 1;
  }
  






  public int getAndDecrement()
  {
    int last = value;
    value -= 1;
    return last;
  }
  






  public int decrementAndGet()
  {
    value -= 1;
    return value;
  }
  






  public void add(int operand)
  {
    value += operand;
  }
  






  public void add(Number operand)
  {
    value += operand.intValue();
  }
  





  public void subtract(int operand)
  {
    value -= operand;
  }
  






  public void subtract(Number operand)
  {
    value -= operand.intValue();
  }
  







  public int addAndGet(int operand)
  {
    value += operand;
    return value;
  }
  








  public int addAndGet(Number operand)
  {
    value += operand.intValue();
    return value;
  }
  







  public int getAndAdd(int operand)
  {
    int last = value;
    value += operand;
    return last;
  }
  








  public int getAndAdd(Number operand)
  {
    int last = value;
    value += operand.intValue();
    return last;
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
  





  public Integer toInteger()
  {
    return Integer.valueOf(intValue());
  }
  









  public boolean equals(Object obj)
  {
    if ((obj instanceof MutableInt)) {
      return value == ((MutableInt)obj).intValue();
    }
    return false;
  }
  





  public int hashCode()
  {
    return value;
  }
  







  public int compareTo(MutableInt other)
  {
    return NumberUtils.compare(value, value);
  }
  






  public String toString()
  {
    return String.valueOf(value);
  }
}
