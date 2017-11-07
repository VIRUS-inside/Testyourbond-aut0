package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

























@Beta
public abstract class AbstractNetwork<N, E>
  implements Network<N, E>
{
  public AbstractNetwork() {}
  
  public Graph<N> asGraph()
  {
    new AbstractGraph()
    {
      public Set<N> nodes() {
        return AbstractNetwork.this.nodes();
      }
      
      public Set<EndpointPair<N>> edges()
      {
        if (allowsParallelEdges()) {
          return super.edges();
        }
        

        new AbstractSet()
        {
          public Iterator<EndpointPair<N>> iterator() {
            Iterators.transform(AbstractNetwork.this
              .edges().iterator(), new Function()
              {
                public EndpointPair<N> apply(E edge)
                {
                  return incidentNodes(edge);
                }
              });
          }
          
          public int size()
          {
            return AbstractNetwork.this.edges().size();
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
      
      public ElementOrder<N> nodeOrder()
      {
        return AbstractNetwork.this.nodeOrder();
      }
      
      public boolean isDirected()
      {
        return AbstractNetwork.this.isDirected();
      }
      
      public boolean allowsSelfLoops()
      {
        return AbstractNetwork.this.allowsSelfLoops();
      }
      
      public Set<N> adjacentNodes(Object node)
      {
        return AbstractNetwork.this.adjacentNodes(node);
      }
      
      public Set<N> predecessors(Object node)
      {
        return AbstractNetwork.this.predecessors(node);
      }
      
      public Set<N> successors(Object node)
      {
        return AbstractNetwork.this.successors(node);
      }
    };
  }
  


  public int degree(Object node)
  {
    if (isDirected()) {
      return IntMath.saturatedAdd(inEdges(node).size(), outEdges(node).size());
    }
    return IntMath.saturatedAdd(incidentEdges(node).size(), edgesConnecting(node, node).size());
  }
  

  public int inDegree(Object node)
  {
    return isDirected() ? inEdges(node).size() : degree(node);
  }
  
  public int outDegree(Object node)
  {
    return isDirected() ? outEdges(node).size() : degree(node);
  }
  
  public Set<E> adjacentEdges(Object edge)
  {
    EndpointPair<?> endpointPair = incidentNodes(edge);
    
    Set<E> endpointPairIncidentEdges = Sets.union(incidentEdges(endpointPair.nodeU()), incidentEdges(endpointPair.nodeV()));
    return Sets.difference(endpointPairIncidentEdges, ImmutableSet.of(edge));
  }
  


  public String toString()
  {
    String propertiesString = String.format("isDirected: %s, allowsParallelEdges: %s, allowsSelfLoops: %s", new Object[] {
    
      Boolean.valueOf(isDirected()), Boolean.valueOf(allowsParallelEdges()), Boolean.valueOf(allowsSelfLoops()) });
    return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, nodes(), edgeIncidentNodesMap() });
  }
  
  private Map<E, EndpointPair<N>> edgeIncidentNodesMap() {
    Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function()
    {
      public EndpointPair<N> apply(E edge)
      {
        return incidentNodes(edge);
      }
    };
    return Maps.asMap(edges(), edgeToIncidentNodesFn);
  }
}
