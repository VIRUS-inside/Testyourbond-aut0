package net.sf.cglib.core.internal;

public abstract interface Function<K, V>
{
  public abstract V apply(K paramK);
}
