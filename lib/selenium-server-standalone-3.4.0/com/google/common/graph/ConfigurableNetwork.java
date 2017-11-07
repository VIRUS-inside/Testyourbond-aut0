package com.google.common.graph;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.Nullable;











































class ConfigurableNetwork<N, E>
  extends AbstractNetwork<N, E>
{
  private final boolean isDirected;
  private final boolean allowsParallelEdges;
  private final boolean allowsSelfLoops;
  private final ElementOrder<N> nodeOrder;
  private final ElementOrder<E> edgeOrder;
  protected final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
  protected final MapIteratorCache<E, N> edgeToReferenceNode;
  
  ConfigurableNetwork(NetworkBuilder<? super N, ? super E> builder)
  {
    this(builder, nodeOrder
    
      .createMap(
      ((Integer)expectedNodeCount.or(Integer.valueOf(10))).intValue()), edgeOrder
      .createMap(((Integer)expectedEdgeCount.or(Integer.valueOf(20))).intValue()));
  }
  






  ConfigurableNetwork(NetworkBuilder<? super N, ? super E> builder, Map<N, NetworkConnections<N, E>> nodeConnections, Map<E, N> edgeToReferenceNode)
  {
    isDirected = directed;
    allowsParallelEdges = allowsParallelEdges;
    allowsSelfLoops = allowsSelfLoops;
    nodeOrder = nodeOrder.cast();
    edgeOrder = edgeOrder.cast();
    

    this.nodeConnections = ((nodeConnections instanceof TreeMap) ? new MapRetrievalCache(nodeConnections) : new MapIteratorCache(nodeConnections));
    


    this.edgeToReferenceNode = new MapIteratorCache(edgeToReferenceNode);
  }
  
  public Set<N> nodes()
  {
    return nodeConnections.unmodifiableKeySet();
  }
  
  public Set<E> edges()
  {
    return edgeToReferenceNode.unmodifiableKeySet();
  }
  
  public boolean isDirected()
  {
    return isDirected;
  }
  
  public boolean allowsParallelEdges()
  {
    return allowsParallelEdges;
  }
  
  public boolean allowsSelfLoops()
  {
    return allowsSelfLoops;
  }
  
  public ElementOrder<N> nodeOrder()
  {
    return nodeOrder;
  }
  
  public ElementOrder<E> edgeOrder()
  {
    return edgeOrder;
  }
  
  public Set<E> incidentEdges(Object node)
  {
    return checkedConnections(node).incidentEdges();
  }
  
  public EndpointPair<N> incidentNodes(Object edge)
  {
    N nodeU = checkedReferenceNode(edge);
    N nodeV = ((NetworkConnections)nodeConnections.get(nodeU)).oppositeNode(edge);
    return EndpointPair.of(this, nodeU, nodeV);
  }
  
  public Set<N> adjacentNodes(Object node)
  {
    return checkedConnections(node).adjacentNodes();
  }
  
  public Set<E> edgesConnecting(Object nodeU, Object nodeV)
  {
    NetworkConnections<N, E> connectionsU = checkedConnections(nodeU);
    if ((!allowsSelfLoops) && (nodeU == nodeV)) {
      return ImmutableSet.of();
    }
    Preconditions.checkArgument(containsNode(nodeV), "Node %s is not an element of this graph.", nodeV);
    return connectionsU.edgesConnecting(nodeV);
  }
  
  public Set<E> inEdges(Object node)
  {
    return checkedConnections(node).inEdges();
  }
  
  public Set<E> outEdges(Object node)
  {
    return checkedConnections(node).outEdges();
  }
  
  public Set<N> predecessors(Object node)
  {
    return checkedConnections(node).predecessors();
  }
  
  public Set<N> successors(Object node)
  {
    return checkedConnections(node).successors();
  }
  
  protected final NetworkConnections<N, E> checkedConnections(Object node) {
    NetworkConnections<N, E> connections = (NetworkConnections)nodeConnections.get(node);
    if (connections == null) {
      Preconditions.checkNotNull(node);
      throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[] { node }));
    }
    return connections;
  }
  
  protected final N checkedReferenceNode(Object edge) {
    N referenceNode = edgeToReferenceNode.get(edge);
    if (referenceNode == null) {
      Preconditions.checkNotNull(edge);
      throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", new Object[] { edge }));
    }
    return referenceNode;
  }
  
  protected final boolean containsNode(@Nullable Object node) {
    return nodeConnections.containsKey(node);
  }
  
  protected final boolean containsEdge(@Nullable Object edge) {
    return edgeToReferenceNode.containsKey(edge);
  }
}
