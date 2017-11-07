package org.apache.commons.lang3.reflect;

import java.lang.reflect.Type;

public abstract interface Typed<T>
{
  public abstract Type getType();
}
