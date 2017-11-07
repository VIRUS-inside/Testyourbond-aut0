package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;

@Beta
public abstract interface MutableNetwork<N, E>
  extends Network<N, E>
{
  @CanIgnoreReturnValue
  public abstract boolean addNode(N paramN);
  
  @CanIgnoreReturnValue
  public abstract boolean addEdge(N paramN1, N paramN2, E paramE);
  
  @CanIgnoreReturnValue
  public abstract boolean removeNode(@CompatibleWith("N") Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract boolean removeEdge(@CompatibleWith("E") Object paramObject);
}
