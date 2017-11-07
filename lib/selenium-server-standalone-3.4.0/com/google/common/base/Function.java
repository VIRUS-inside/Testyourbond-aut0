package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;

@FunctionalInterface
@GwtCompatible
public abstract interface Function<F, T>
  extends java.util.function.Function<F, T>
{
  @Nullable
  @CanIgnoreReturnValue
  public abstract T apply(@Nullable F paramF);
  
  public abstract boolean equals(@Nullable Object paramObject);
}
