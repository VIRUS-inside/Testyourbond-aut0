package org.hamcrest.internal;

import org.hamcrest.Description;

public class SelfDescribingValue<T> implements org.hamcrest.SelfDescribing
{
  private T value;
  
  public SelfDescribingValue(T value) {
    this.value = value;
  }
  
  public void describeTo(Description description)
  {
    description.appendValue(value);
  }
}
