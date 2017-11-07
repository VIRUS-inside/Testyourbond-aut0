package com.google.common.graph;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;





































class ConfigurableValueGraph<N, V>
  extends AbstractValueGraph<N, V>
{
  private final boolean isDirected;
  private final boolean allowsSelfLoops;
  private final ElementOrder<N> nodeOrder;
  protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
  protected long edgeCount;
  
  ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder)
  {
    this(builder, nodeOrder
    
      .createMap(
      ((Integer)expectedNodeCount.or(Integer.valueOf(10))).intValue()), 0L);
  }
  







  ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount)
  {
    isDirected = directed;
    allowsSelfLoops = allowsSelfLoops;
    nodeOrder = nodeOrder.cast();
    
    this.nodeConnections = ((nodeConnections instanceof TreeMap) ? new MapRetrievalCache(nodeConnections) : new MapIteratorCache(nodeConnections));
    


    this.edgeCount = Graphs.checkNonNegative(edgeCount);
  }
  
  public Set<N> nodes()
  {
    return nodeConnections.unmodifiableKeySet();
  }
  
  public boolean isDirected()
  {
    return isDirected;
  }
  
  public boolean allowsSelfLoops()
  {
    return allowsSelfLoops;
  }
  
  public ElementOrder<N> nodeOrder()
  {
    return nodeOrder;
  }
  
  public Set<N> adjacentNodes(Object node)
  {
    return checkedConnections(node).adjacentNodes();
  }
  
  public Set<N> predecessors(Object node)
  {
    return checkedConnections(node).predecessors();
  }
  
  public Set<N> successors(Object node)
  {
    return checkedConnections(node).successors();
  }
  
  public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue)
  {
    GraphConnections<N, V> connectionsU = (GraphConnections)nodeConnections.get(nodeU);
    if (connectionsU == null) {
      return defaultValue;
    }
    V value = connectionsU.value(nodeV);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
  
  protected long edgeCount()
  {
    return edgeCount;
  }
  
  protected final GraphConnections<N, V> checkedConnections(Object node) {
    GraphConnections<N, V> connections = (GraphConnections)nodeConnections.get(node);
    if (connections == null) {
      Preconditions.checkNotNull(node);
      throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[] { node }));
    }
    return connections;
  }
  
  protected final boolean containsNode(@Nullable Object node) {
    return nodeConnections.containsKey(node);
  }
}
