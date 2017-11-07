package org.apache.commons.lang3.builder;

public abstract interface Diffable<T>
{
  public abstract DiffResult diff(T paramT);
}
