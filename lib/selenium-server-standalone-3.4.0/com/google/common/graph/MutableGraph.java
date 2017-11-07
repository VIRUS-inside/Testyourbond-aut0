package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;

@Beta
public abstract interface MutableGraph<N>
  extends Graph<N>
{
  @CanIgnoreReturnValue
  public abstract boolean addNode(N paramN);
  
  @CanIgnoreReturnValue
  public abstract boolean putEdge(N paramN1, N paramN2);
  
  @CanIgnoreReturnValue
  public abstract boolean removeNode(@CompatibleWith("N") Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract boolean removeEdge(@CompatibleWith("N") Object paramObject1, @CompatibleWith("N") Object paramObject2);
}
