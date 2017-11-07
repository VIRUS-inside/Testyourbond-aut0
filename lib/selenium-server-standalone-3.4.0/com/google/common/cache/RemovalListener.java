package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract interface RemovalListener<K, V>
{
  public abstract void onRemoval(RemovalNotification<K, V> paramRemovalNotification);
}
