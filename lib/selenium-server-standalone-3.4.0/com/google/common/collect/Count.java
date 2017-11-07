package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

















@GwtCompatible
final class Count
  implements Serializable
{
  private int value;
  
  Count(int value)
  {
    this.value = value;
  }
  
  public int get() {
    return value;
  }
  
  public void add(int delta) {
    value += delta;
  }
  
  public int addAndGet(int delta) {
    return this.value += delta;
  }
  
  public void set(int newValue) {
    value = newValue;
  }
  
  public int getAndSet(int newValue) {
    int result = value;
    value = newValue;
    return result;
  }
  
  public int hashCode()
  {
    return value;
  }
  
  public boolean equals(@Nullable Object obj)
  {
    return ((obj instanceof Count)) && (value == value);
  }
  
  public String toString()
  {
    return Integer.toString(value);
  }
}
