package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;



























































@GwtCompatible
public abstract class ForwardingObject
{
  protected ForwardingObject() {}
  
  protected abstract Object delegate();
  
  public String toString()
  {
    return delegate().toString();
  }
}
