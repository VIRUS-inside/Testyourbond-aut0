package org.apache.commons.lang3.builder;











final class IDKey
{
  private final Object value;
  









  private final int id;
  









  public IDKey(Object _value)
  {
    id = System.identityHashCode(_value);
    


    value = _value;
  }
  




  public int hashCode()
  {
    return id;
  }
  





  public boolean equals(Object other)
  {
    if (!(other instanceof IDKey)) {
      return false;
    }
    IDKey idKey = (IDKey)other;
    if (id != id) {
      return false;
    }
    
    return value == value;
  }
}
