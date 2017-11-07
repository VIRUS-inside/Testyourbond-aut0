package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract interface Graph<N>
{
  public abstract Set<N> nodes();
  
  public abstract Set<EndpointPair<N>> edges();
  
  public abstract boolean isDirected();
  
  public abstract boolean allowsSelfLoops();
  
  public abstract ElementOrder<N> nodeOrder();
  
  public abstract Set<N> adjacentNodes(@CompatibleWith("N") Object paramObject);
  
  public abstract Set<N> predecessors(@CompatibleWith("N") Object paramObject);
  
  public abstract Set<N> successors(@CompatibleWith("N") Object paramObject);
  
  public abstract int degree(@CompatibleWith("N") Object paramObject);
  
  public abstract int inDegree(@CompatibleWith("N") Object paramObject);
  
  public abstract int outDegree(@CompatibleWith("N") Object paramObject);
  
  public abstract boolean equals(@Nullable Object paramObject);
  
  public abstract int hashCode();
}
