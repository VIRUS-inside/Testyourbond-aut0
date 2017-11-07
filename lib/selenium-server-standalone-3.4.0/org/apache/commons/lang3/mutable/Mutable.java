package org.apache.commons.lang3.mutable;

public abstract interface Mutable<T>
{
  public abstract T getValue();
  
  public abstract void setValue(T paramT);
}
