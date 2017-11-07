package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract interface Weigher<K, V>
{
  public abstract int weigh(K paramK, V paramV);
}
