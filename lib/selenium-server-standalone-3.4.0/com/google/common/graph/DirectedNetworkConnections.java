package com.google.common.graph;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

























final class DirectedNetworkConnections<N, E>
  extends AbstractDirectedNetworkConnections<N, E>
{
  protected DirectedNetworkConnections(Map<E, N> inEdgeMap, Map<E, N> outEdgeMap, int selfLoopCount)
  {
    super(inEdgeMap, outEdgeMap, selfLoopCount);
  }
  
  static <N, E> DirectedNetworkConnections<N, E> of() {
    return new DirectedNetworkConnections(
      HashBiMap.create(2), HashBiMap.create(2), 0);
  }
  
  static <N, E> DirectedNetworkConnections<N, E> ofImmutable(Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount)
  {
    return new DirectedNetworkConnections(
      ImmutableBiMap.copyOf(inEdges), ImmutableBiMap.copyOf(outEdges), selfLoopCount);
  }
  
  public Set<N> predecessors()
  {
    return Collections.unmodifiableSet(((BiMap)inEdgeMap).values());
  }
  
  public Set<N> successors()
  {
    return Collections.unmodifiableSet(((BiMap)outEdgeMap).values());
  }
  
  public Set<E> edgesConnecting(Object node)
  {
    return new EdgesConnecting(((BiMap)outEdgeMap).inverse(), node);
  }
}
