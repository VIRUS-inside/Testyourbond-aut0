package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public abstract interface AsyncCallable<V>
{
  public abstract ListenableFuture<V> call()
    throws Exception;
}
