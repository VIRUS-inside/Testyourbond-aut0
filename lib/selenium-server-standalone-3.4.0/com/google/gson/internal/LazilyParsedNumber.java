package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.math.BigDecimal;



















public final class LazilyParsedNumber
  extends Number
{
  private final String value;
  
  public LazilyParsedNumber(String value)
  {
    this.value = value;
  }
  
  public int intValue()
  {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      try {
        return (int)Long.parseLong(value);
      } catch (NumberFormatException nfe) {} }
    return new BigDecimal(value).intValue();
  }
  

  public long longValue()
  {
    try
    {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {}
    return new BigDecimal(value).longValue();
  }
  

  public float floatValue()
  {
    return Float.parseFloat(value);
  }
  
  public double doubleValue()
  {
    return Double.parseDouble(value);
  }
  
  public String toString()
  {
    return value;
  }
  



  private Object writeReplace()
    throws ObjectStreamException
  {
    return new BigDecimal(value);
  }
  
  public int hashCode()
  {
    return value.hashCode();
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if ((obj instanceof LazilyParsedNumber)) {
      LazilyParsedNumber other = (LazilyParsedNumber)obj;
      return (value == value) || (value.equals(value));
    }
    return false;
  }
}
