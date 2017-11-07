package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract interface Network<N, E>
{
  public abstract Set<N> nodes();
  
  public abstract Set<E> edges();
  
  public abstract Graph<N> asGraph();
  
  public abstract boolean isDirected();
  
  public abstract boolean allowsParallelEdges();
  
  public abstract boolean allowsSelfLoops();
  
  public abstract ElementOrder<N> nodeOrder();
  
  public abstract ElementOrder<E> edgeOrder();
  
  public abstract Set<N> adjacentNodes(@CompatibleWith("N") Object paramObject);
  
  public abstract Set<N> predecessors(@CompatibleWith("N") Object paramObject);
  
  public abstract Set<N> successors(@CompatibleWith("N") Object paramObject);
  
  public abstract Set<E> incidentEdges(@CompatibleWith("N") Object paramObject);
  
  public abstract Set<E> inEdges(@CompatibleWith("N") Object paramObject);
  
  public abstract Set<E> outEdges(@CompatibleWith("N") Object paramObject);
  
  public abstract int degree(@CompatibleWith("N") Object paramObject);
  
  public abstract int inDegree(@CompatibleWith("N") Object paramObject);
  
  public abstract int outDegree(@CompatibleWith("N") Object paramObject);
  
  public abstract EndpointPair<N> incidentNodes(@CompatibleWith("E") Object paramObject);
  
  public abstract Set<E> adjacentEdges(@CompatibleWith("E") Object paramObject);
  
  public abstract Set<E> edgesConnecting(@CompatibleWith("N") Object paramObject1, @CompatibleWith("N") Object paramObject2);
  
  public abstract boolean equals(@Nullable Object paramObject);
  
  public abstract int hashCode();
}
