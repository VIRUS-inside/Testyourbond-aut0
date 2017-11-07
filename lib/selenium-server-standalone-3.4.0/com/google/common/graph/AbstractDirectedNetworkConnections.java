package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;






























abstract class AbstractDirectedNetworkConnections<N, E>
  implements NetworkConnections<N, E>
{
  protected final Map<E, N> inEdgeMap;
  protected final Map<E, N> outEdgeMap;
  private int selfLoopCount;
  
  protected AbstractDirectedNetworkConnections(Map<E, N> inEdgeMap, Map<E, N> outEdgeMap, int selfLoopCount)
  {
    this.inEdgeMap = ((Map)Preconditions.checkNotNull(inEdgeMap));
    this.outEdgeMap = ((Map)Preconditions.checkNotNull(outEdgeMap));
    this.selfLoopCount = Graphs.checkNonNegative(selfLoopCount);
    Preconditions.checkState((selfLoopCount <= inEdgeMap.size()) && (selfLoopCount <= outEdgeMap.size()));
  }
  
  public Set<N> adjacentNodes()
  {
    return Sets.union(predecessors(), successors());
  }
  
  public Set<E> incidentEdges()
  {
    new AbstractSet()
    {

      public UnmodifiableIterator<E> iterator()
      {

        Iterable<E> incidentEdges = selfLoopCount == 0 ? Iterables.concat(inEdgeMap.keySet(), outEdgeMap.keySet()) : Sets.union(inEdgeMap.keySet(), outEdgeMap.keySet());
        return Iterators.unmodifiableIterator(incidentEdges.iterator());
      }
      
      public int size()
      {
        return IntMath.saturatedAdd(inEdgeMap.size(), outEdgeMap.size() - selfLoopCount);
      }
      
      public boolean contains(@Nullable Object obj)
      {
        return (inEdgeMap.containsKey(obj)) || (outEdgeMap.containsKey(obj));
      }
    };
  }
  
  public Set<E> inEdges()
  {
    return Collections.unmodifiableSet(inEdgeMap.keySet());
  }
  
  public Set<E> outEdges()
  {
    return Collections.unmodifiableSet(outEdgeMap.keySet());
  }
  


  public N oppositeNode(Object edge)
  {
    return Preconditions.checkNotNull(outEdgeMap.get(edge));
  }
  
  public N removeInEdge(Object edge, boolean isSelfLoop)
  {
    if (isSelfLoop) {
      Graphs.checkNonNegative(--selfLoopCount);
    }
    N previousNode = inEdgeMap.remove(edge);
    return Preconditions.checkNotNull(previousNode);
  }
  
  public N removeOutEdge(Object edge)
  {
    N previousNode = outEdgeMap.remove(edge);
    return Preconditions.checkNotNull(previousNode);
  }
  
  public void addInEdge(E edge, N node, boolean isSelfLoop)
  {
    if (isSelfLoop) {
      Graphs.checkPositive(++selfLoopCount);
    }
    N previousNode = inEdgeMap.put(edge, node);
    Preconditions.checkState(previousNode == null);
  }
  
  public void addOutEdge(E edge, N node)
  {
    N previousNode = outEdgeMap.put(edge, node);
    Preconditions.checkState(previousNode == null);
  }
}
