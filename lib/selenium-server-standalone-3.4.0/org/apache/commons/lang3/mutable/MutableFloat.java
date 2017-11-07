package org.apache.commons.lang3.mutable;











public class MutableFloat
  extends Number
  implements Comparable<MutableFloat>, Mutable<Number>
{
  private static final long serialVersionUID = 5787169186L;
  









  private float value;
  










  public MutableFloat() {}
  









  public MutableFloat(float value)
  {
    this.value = value;
  }
  






  public MutableFloat(Number value)
  {
    this.value = value.floatValue();
  }
  






  public MutableFloat(String value)
    throws NumberFormatException
  {
    this.value = Float.parseFloat(value);
  }
  






  public Float getValue()
  {
    return Float.valueOf(value);
  }
  




  public void setValue(float value)
  {
    this.value = value;
  }
  






  public void setValue(Number value)
  {
    this.value = value.floatValue();
  }
  





  public boolean isNaN()
  {
    return Float.isNaN(value);
  }
  




  public boolean isInfinite()
  {
    return Float.isInfinite(value);
  }
  





  public void increment()
  {
    value += 1.0F;
  }
  






  public float getAndIncrement()
  {
    float last = value;
    value += 1.0F;
    return last;
  }
  






  public float incrementAndGet()
  {
    value += 1.0F;
    return value;
  }
  




  public void decrement()
  {
    value -= 1.0F;
  }
  






  public float getAndDecrement()
  {
    float last = value;
    value -= 1.0F;
    return last;
  }
  






  public float decrementAndGet()
  {
    value -= 1.0F;
    return value;
  }
  






  public void add(float operand)
  {
    value += operand;
  }
  






  public void add(Number operand)
  {
    value += operand.floatValue();
  }
  





  public void subtract(float operand)
  {
    value -= operand;
  }
  






  public void subtract(Number operand)
  {
    value -= operand.floatValue();
  }
  







  public float addAndGet(float operand)
  {
    value += operand;
    return value;
  }
  








  public float addAndGet(Number operand)
  {
    value += operand.floatValue();
    return value;
  }
  







  public float getAndAdd(float operand)
  {
    float last = value;
    value += operand;
    return last;
  }
  








  public float getAndAdd(Number operand)
  {
    float last = value;
    value += operand.floatValue();
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
    return value;
  }
  





  public double doubleValue()
  {
    return value;
  }
  





  public Float toFloat()
  {
    return Float.valueOf(floatValue());
  }
  
































  public boolean equals(Object obj)
  {
    return ((obj instanceof MutableFloat)) && 
      (Float.floatToIntBits(value) == Float.floatToIntBits(value));
  }
  





  public int hashCode()
  {
    return Float.floatToIntBits(value);
  }
  







  public int compareTo(MutableFloat other)
  {
    return Float.compare(value, value);
  }
  






  public String toString()
  {
    return String.valueOf(value);
  }
}
