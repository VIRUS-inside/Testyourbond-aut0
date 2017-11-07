package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;

abstract interface NetworkConnections<N, E>
{
  public abstract Set<N> adjacentNodes();
  
  public abstract Set<N> predecessors();
  
  public abstract Set<N> successors();
  
  public abstract Set<E> incidentEdges();
  
  public abstract Set<E> inEdges();
  
  public abstract Set<E> outEdges();
  
  public abstract Set<E> edgesConnecting(Object paramObject);
  
  public abstract N oppositeNode(Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract N removeInEdge(Object paramObject, boolean paramBoolean);
  
  @CanIgnoreReturnValue
  public abstract N removeOutEdge(Object paramObject);
  
  public abstract void addInEdge(E paramE, N paramN, boolean paramBoolean);
  
  public abstract void addOutEdge(E paramE, N paramN);
}
