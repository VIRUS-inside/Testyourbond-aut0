package org.apache.commons.lang3.mutable;

import java.io.Serializable;








































public class MutableObject<T>
  implements Mutable<T>, Serializable
{
  private static final long serialVersionUID = 86241875189L;
  private T value;
  
  public MutableObject() {}
  
  public MutableObject(T value)
  {
    this.value = value;
  }
  






  public T getValue()
  {
    return value;
  }
  





  public void setValue(T value)
  {
    this.value = value;
  }
  













  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (getClass() == obj.getClass()) {
      MutableObject<?> that = (MutableObject)obj;
      return value.equals(value);
    }
    return false;
  }
  





  public int hashCode()
  {
    return value == null ? 0 : value.hashCode();
  }
  






  public String toString()
  {
    return value == null ? "null" : value.toString();
  }
}
