package org.apache.commons.lang3.mutable;

import java.io.Serializable;
import org.apache.commons.lang3.BooleanUtils;











































public class MutableBoolean
  implements Mutable<Boolean>, Serializable, Comparable<MutableBoolean>
{
  private static final long serialVersionUID = -4830728138360036487L;
  private boolean value;
  
  public MutableBoolean() {}
  
  public MutableBoolean(boolean value)
  {
    this.value = value;
  }
  






  public MutableBoolean(Boolean value)
  {
    this.value = value.booleanValue();
  }
  






  public Boolean getValue()
  {
    return Boolean.valueOf(value);
  }
  




  public void setValue(boolean value)
  {
    this.value = value;
  }
  




  public void setFalse()
  {
    value = false;
  }
  




  public void setTrue()
  {
    value = true;
  }
  






  public void setValue(Boolean value)
  {
    this.value = value.booleanValue();
  }
  






  public boolean isTrue()
  {
    return value == true;
  }
  





  public boolean isFalse()
  {
    return !value;
  }
  





  public boolean booleanValue()
  {
    return value;
  }
  






  public Boolean toBoolean()
  {
    return Boolean.valueOf(booleanValue());
  }
  









  public boolean equals(Object obj)
  {
    if ((obj instanceof MutableBoolean)) {
      return value == ((MutableBoolean)obj).booleanValue();
    }
    return false;
  }
  





  public int hashCode()
  {
    return value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
  }
  








  public int compareTo(MutableBoolean other)
  {
    return BooleanUtils.compare(value, value);
  }
  






  public String toString()
  {
    return String.valueOf(value);
  }
}
