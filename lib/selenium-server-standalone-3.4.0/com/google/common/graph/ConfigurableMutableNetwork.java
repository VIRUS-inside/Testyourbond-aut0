package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.util.Set;
































final class ConfigurableMutableNetwork<N, E>
  extends ConfigurableNetwork<N, E>
  implements MutableNetwork<N, E>
{
  ConfigurableMutableNetwork(NetworkBuilder<? super N, ? super E> builder)
  {
    super(builder);
  }
  
  @CanIgnoreReturnValue
  public boolean addNode(N node)
  {
    Preconditions.checkNotNull(node, "node");
    
    if (containsNode(node)) {
      return false;
    }
    
    addNodeInternal(node);
    return true;
  }
  




  @CanIgnoreReturnValue
  private NetworkConnections<N, E> addNodeInternal(N node)
  {
    NetworkConnections<N, E> connections = newConnections();
    Preconditions.checkState(nodeConnections.put(node, connections) == null);
    return connections;
  }
  
  @CanIgnoreReturnValue
  public boolean addEdge(N nodeU, N nodeV, E edge)
  {
    Preconditions.checkNotNull(nodeU, "nodeU");
    Preconditions.checkNotNull(nodeV, "nodeV");
    Preconditions.checkNotNull(edge, "edge");
    
    if (containsEdge(edge)) {
      EndpointPair<N> existingIncidentNodes = incidentNodes(edge);
      EndpointPair<N> newIncidentNodes = EndpointPair.of(this, nodeU, nodeV);
      Preconditions.checkArgument(existingIncidentNodes
        .equals(newIncidentNodes), "Edge %s already exists between the following nodes: %s, so it cannot be reused to connect the following nodes: %s.", edge, existingIncidentNodes, newIncidentNodes);
      



      return false;
    }
    NetworkConnections<N, E> connectionsU = (NetworkConnections)nodeConnections.get(nodeU);
    if (!allowsParallelEdges()) {
      Preconditions.checkArgument((connectionsU == null) || 
        (!connectionsU.successors().contains(nodeV)), "Nodes %s and %s are already connected by a different edge. To construct a graph that allows parallel edges, call allowsParallelEdges(true) on the Builder.", nodeU, nodeV);
    }
    


    boolean isSelfLoop = nodeU.equals(nodeV);
    if (!allowsSelfLoops()) {
      Preconditions.checkArgument(!isSelfLoop, "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
    }
    
    if (connectionsU == null) {
      connectionsU = addNodeInternal(nodeU);
    }
    connectionsU.addOutEdge(edge, nodeV);
    NetworkConnections<N, E> connectionsV = (NetworkConnections)nodeConnections.get(nodeV);
    if (connectionsV == null) {
      connectionsV = addNodeInternal(nodeV);
    }
    connectionsV.addInEdge(edge, nodeU, isSelfLoop);
    edgeToReferenceNode.put(edge, nodeU);
    return true;
  }
  
  @CanIgnoreReturnValue
  public boolean removeNode(Object node)
  {
    Preconditions.checkNotNull(node, "node");
    
    NetworkConnections<N, E> connections = (NetworkConnections)nodeConnections.get(node);
    if (connections == null) {
      return false;
    }
    


    for (UnmodifiableIterator localUnmodifiableIterator = ImmutableList.copyOf(connections.incidentEdges()).iterator(); localUnmodifiableIterator.hasNext();) { E edge = localUnmodifiableIterator.next();
      removeEdge(edge);
    }
    nodeConnections.remove(node);
    return true;
  }
  
  @CanIgnoreReturnValue
  public boolean removeEdge(Object edge)
  {
    Preconditions.checkNotNull(edge, "edge");
    
    N nodeU = edgeToReferenceNode.get(edge);
    if (nodeU == null) {
      return false;
    }
    
    NetworkConnections<N, E> connectionsU = (NetworkConnections)nodeConnections.get(nodeU);
    N nodeV = connectionsU.oppositeNode(edge);
    NetworkConnections<N, E> connectionsV = (NetworkConnections)nodeConnections.get(nodeV);
    connectionsU.removeOutEdge(edge);
    connectionsV.removeInEdge(edge, (allowsSelfLoops()) && (nodeU.equals(nodeV)));
    edgeToReferenceNode.remove(edge);
    return true;
  }
  
  private NetworkConnections<N, E> newConnections() {
    return 
    


      allowsParallelEdges() ? 
      UndirectedMultiNetworkConnections.of() : isDirected() ? DirectedNetworkConnections.of() : allowsParallelEdges() ? DirectedMultiNetworkConnections.of() : 
      UndirectedNetworkConnections.of();
  }
}
