package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import java.util.AbstractSet;
import java.util.Set;
import javax.annotation.Nullable;




























@Beta
public abstract class AbstractGraph<N>
  implements Graph<N>
{
  public AbstractGraph() {}
  
  protected long edgeCount()
  {
    long degreeSum = 0L;
    for (N node : nodes()) {
      degreeSum += degree(node);
    }
    
    Preconditions.checkState((degreeSum & 1L) == 0L);
    return degreeSum >>> 1;
  }
  




  public Set<EndpointPair<N>> edges()
  {
    new AbstractSet()
    {
      public UnmodifiableIterator<EndpointPair<N>> iterator() {
        return EndpointPairIterator.of(AbstractGraph.this);
      }
      
      public int size()
      {
        return Ints.saturatedCast(edgeCount());
      }
      
      public boolean contains(@Nullable Object obj)
      {
        if (!(obj instanceof EndpointPair)) {
          return false;
        }
        EndpointPair<?> endpointPair = (EndpointPair)obj;
        return (isDirected() == endpointPair.isOrdered()) && 
          (nodes().contains(endpointPair.nodeU())) && 
          (successors(endpointPair.nodeU()).contains(endpointPair.nodeV()));
      }
    };
  }
  
  public int degree(Object node)
  {
    if (isDirected()) {
      return IntMath.saturatedAdd(predecessors(node).size(), successors(node).size());
    }
    Set<N> neighbors = adjacentNodes(node);
    int selfLoopCount = (allowsSelfLoops()) && (neighbors.contains(node)) ? 1 : 0;
    return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
  }
  

  public int inDegree(Object node)
  {
    return isDirected() ? predecessors(node).size() : degree(node);
  }
  
  public int outDegree(Object node)
  {
    return isDirected() ? successors(node).size() : degree(node);
  }
  


  public String toString()
  {
    String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", new Object[] { Boolean.valueOf(isDirected()), Boolean.valueOf(allowsSelfLoops()) });
    return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, nodes(), edges() });
  }
}
