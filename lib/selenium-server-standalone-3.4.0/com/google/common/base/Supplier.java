package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@FunctionalInterface
@GwtCompatible
public abstract interface Supplier<T>
  extends java.util.function.Supplier<T>
{
  @CanIgnoreReturnValue
  public abstract T get();
}
