package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import javax.annotation.Nullable;

@FunctionalInterface
@GwtCompatible
public abstract interface AsyncFunction<I, O>
{
  public abstract ListenableFuture<O> apply(@Nullable I paramI)
    throws Exception;
}
