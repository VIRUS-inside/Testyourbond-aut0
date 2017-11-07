package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@Beta
@GwtIncompatible
public abstract interface Interner<E>
{
  @CanIgnoreReturnValue
  public abstract E intern(E paramE);
}
