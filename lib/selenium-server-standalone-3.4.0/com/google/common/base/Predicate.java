package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;






























































@FunctionalInterface
@GwtCompatible
public abstract interface Predicate<T>
  extends java.util.function.Predicate<T>
{
  @CanIgnoreReturnValue
  public abstract boolean apply(@Nullable T paramT);
  
  public abstract boolean equals(@Nullable Object paramObject);
  
  public boolean test(@Nullable T input)
  {
    return apply(input);
  }
}
