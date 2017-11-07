package com.google.common.graph;

import java.util.Set;




















abstract class ForwardingGraph<N>
  extends AbstractGraph<N>
{
  ForwardingGraph() {}
  
  protected abstract Graph<N> delegate();
  
  public Set<N> nodes()
  {
    return delegate().nodes();
  }
  
  public Set<EndpointPair<N>> edges()
  {
    return delegate().edges();
  }
  
  public boolean isDirected()
  {
    return delegate().isDirected();
  }
  
  public boolean allowsSelfLoops()
  {
    return delegate().allowsSelfLoops();
  }
  
  public ElementOrder<N> nodeOrder()
  {
    return delegate().nodeOrder();
  }
  
  public Set<N> adjacentNodes(Object node)
  {
    return delegate().adjacentNodes(node);
  }
  
  public Set<N> predecessors(Object node)
  {
    return delegate().predecessors(node);
  }
  
  public Set<N> successors(Object node)
  {
    return delegate().successors(node);
  }
  
  public int degree(Object node)
  {
    return delegate().degree(node);
  }
  
  public int inDegree(Object node)
  {
    return delegate().inDegree(node);
  }
  
  public int outDegree(Object node)
  {
    return delegate().outDegree(node);
  }
}
