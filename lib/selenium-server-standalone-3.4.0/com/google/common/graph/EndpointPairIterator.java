package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;






















abstract class EndpointPairIterator<N>
  extends AbstractIterator<EndpointPair<N>>
{
  private final Graph<N> graph;
  private final Iterator<N> nodeIterator;
  protected N node = null;
  protected Iterator<N> successorIterator = ImmutableSet.of().iterator();
  
  static <N> EndpointPairIterator<N> of(Graph<N> graph) {
    return graph.isDirected() ? new Directed(graph, null) : new Undirected(graph, null);
  }
  
  private EndpointPairIterator(Graph<N> graph) {
    this.graph = graph;
    nodeIterator = graph.nodes().iterator();
  }
  



  protected final boolean advance()
  {
    Preconditions.checkState(!successorIterator.hasNext());
    if (!nodeIterator.hasNext()) {
      return false;
    }
    node = nodeIterator.next();
    successorIterator = graph.successors(node).iterator();
    return true;
  }
  

  private static final class Directed<N>
    extends EndpointPairIterator<N>
  {
    private Directed(Graph<N> graph)
    {
      super(null);
    }
    
    protected EndpointPair<N> computeNext()
    {
      do {
        if (successorIterator.hasNext()) {
          return EndpointPair.ordered(node, successorIterator.next());
        }
      } while (advance());
      return (EndpointPair)endOfData();
    }
  }
  













  private static final class Undirected<N>
    extends EndpointPairIterator<N>
  {
    private Set<N> visitedNodes;
    












    private Undirected(Graph<N> graph)
    {
      super(null);
      visitedNodes = Sets.newHashSetWithExpectedSize(graph.nodes().size());
    }
    
    protected EndpointPair<N> computeNext()
    {
      do {
        while (successorIterator.hasNext()) {
          N otherNode = successorIterator.next();
          if (!visitedNodes.contains(otherNode)) {
            return EndpointPair.unordered(node, otherNode);
          }
        }
        
        visitedNodes.add(node);
      } while (advance());
      visitedNodes = null;
      return (EndpointPair)endOfData();
    }
  }
}
