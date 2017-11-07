package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.Nullable;

@Beta
public abstract interface ValueGraph<N, V>
  extends Graph<N>
{
  public abstract V edgeValue(@CompatibleWith("N") Object paramObject1, @CompatibleWith("N") Object paramObject2);
  
  public abstract V edgeValueOrDefault(@CompatibleWith("N") Object paramObject1, @CompatibleWith("N") Object paramObject2, @Nullable V paramV);
  
  public abstract boolean equals(@Nullable Object paramObject);
  
  public abstract int hashCode();
}
