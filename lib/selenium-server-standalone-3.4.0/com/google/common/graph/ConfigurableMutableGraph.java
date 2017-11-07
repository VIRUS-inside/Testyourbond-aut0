package com.google.common.graph;














final class ConfigurableMutableGraph<N>
  extends ForwardingGraph<N>
  implements MutableGraph<N>
{
  private final MutableValueGraph<N, GraphConstants.Presence> backingValueGraph;
  












  ConfigurableMutableGraph(AbstractGraphBuilder<? super N> builder)
  {
    backingValueGraph = new ConfigurableMutableValueGraph(builder);
  }
  
  protected Graph<N> delegate()
  {
    return backingValueGraph;
  }
  
  public boolean addNode(N node)
  {
    return backingValueGraph.addNode(node);
  }
  
  public boolean putEdge(N nodeU, N nodeV)
  {
    return backingValueGraph.putEdgeValue(nodeU, nodeV, GraphConstants.Presence.EDGE_EXISTS) == null;
  }
  
  public boolean removeNode(Object node)
  {
    return backingValueGraph.removeNode(node);
  }
  
  public boolean removeEdge(Object nodeU, Object nodeV)
  {
    return backingValueGraph.removeEdge(nodeU, nodeV) != null;
  }
}
