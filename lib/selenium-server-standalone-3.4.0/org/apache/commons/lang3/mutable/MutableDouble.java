package org.apache.commons.lang3.mutable;











public class MutableDouble
  extends Number
  implements Comparable<MutableDouble>, Mutable<Number>
{
  private static final long serialVersionUID = 1587163916L;
  









  private double value;
  










  public MutableDouble() {}
  









  public MutableDouble(double value)
  {
    this.value = value;
  }
  






  public MutableDouble(Number value)
  {
    this.value = value.doubleValue();
  }
  






  public MutableDouble(String value)
    throws NumberFormatException
  {
    this.value = Double.parseDouble(value);
  }
  






  public Double getValue()
  {
    return Double.valueOf(value);
  }
  




  public void setValue(double value)
  {
    this.value = value;
  }
  






  public void setValue(Number value)
  {
    this.value = value.doubleValue();
  }
  





  public boolean isNaN()
  {
    return Double.isNaN(value);
  }
  




  public boolean isInfinite()
  {
    return Double.isInfinite(value);
  }
  





  public void increment()
  {
    value += 1.0D;
  }
  






  public double getAndIncrement()
  {
    double last = value;
    value += 1.0D;
    return last;
  }
  






  public double incrementAndGet()
  {
    value += 1.0D;
    return value;
  }
  




  public void decrement()
  {
    value -= 1.0D;
  }
  






  public double getAndDecrement()
  {
    double last = value;
    value -= 1.0D;
    return last;
  }
  






  public double decrementAndGet()
  {
    value -= 1.0D;
    return value;
  }
  






  public void add(double operand)
  {
    value += operand;
  }
  






  public void add(Number operand)
  {
    value += operand.doubleValue();
  }
  





  public void subtract(double operand)
  {
    value -= operand;
  }
  






  public void subtract(Number operand)
  {
    value -= operand.doubleValue();
  }
  







  public double addAndGet(double operand)
  {
    value += operand;
    return value;
  }
  








  public double addAndGet(Number operand)
  {
    value += operand.doubleValue();
    return value;
  }
  







  public double getAndAdd(double operand)
  {
    double last = value;
    value += operand;
    return last;
  }
  








  public double getAndAdd(Number operand)
  {
    double last = value;
    value += operand.doubleValue();
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
  





  public Double toDouble()
  {
    return Double.valueOf(doubleValue());
  }
  






























  public boolean equals(Object obj)
  {
    return ((obj instanceof MutableDouble)) && 
      (Double.doubleToLongBits(value) == Double.doubleToLongBits(value));
  }
  





  public int hashCode()
  {
    long bits = Double.doubleToLongBits(value);
    return (int)(bits ^ bits >>> 32);
  }
  







  public int compareTo(MutableDouble other)
  {
    return Double.compare(value, value);
  }
  






  public String toString()
  {
    return String.valueOf(value);
  }
}
