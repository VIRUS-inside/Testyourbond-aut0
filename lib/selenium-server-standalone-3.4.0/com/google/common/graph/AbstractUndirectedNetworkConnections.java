package com.google.common.graph;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
























abstract class AbstractUndirectedNetworkConnections<N, E>
  implements NetworkConnections<N, E>
{
  protected final Map<E, N> incidentEdgeMap;
  
  protected AbstractUndirectedNetworkConnections(Map<E, N> incidentEdgeMap)
  {
    this.incidentEdgeMap = ((Map)Preconditions.checkNotNull(incidentEdgeMap));
  }
  
  public Set<N> predecessors()
  {
    return adjacentNodes();
  }
  
  public Set<N> successors()
  {
    return adjacentNodes();
  }
  
  public Set<E> incidentEdges()
  {
    return Collections.unmodifiableSet(incidentEdgeMap.keySet());
  }
  
  public Set<E> inEdges()
  {
    return incidentEdges();
  }
  
  public Set<E> outEdges()
  {
    return incidentEdges();
  }
  
  public N oppositeNode(Object edge)
  {
    return Preconditions.checkNotNull(incidentEdgeMap.get(edge));
  }
  
  public N removeInEdge(Object edge, boolean isSelfLoop)
  {
    if (!isSelfLoop) {
      return removeOutEdge(edge);
    }
    return null;
  }
  
  public N removeOutEdge(Object edge)
  {
    N previousNode = incidentEdgeMap.remove(edge);
    return Preconditions.checkNotNull(previousNode);
  }
  
  public void addInEdge(E edge, N node, boolean isSelfLoop)
  {
    if (!isSelfLoop) {
      addOutEdge(edge, node);
    }
  }
  
  public void addOutEdge(E edge, N node)
  {
    N previousNode = incidentEdgeMap.put(edge, node);
    Preconditions.checkState(previousNode == null);
  }
}
