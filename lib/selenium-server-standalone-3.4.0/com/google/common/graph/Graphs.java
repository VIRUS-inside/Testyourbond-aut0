package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javax.annotation.Nullable;

































@Beta
public final class Graphs
{
  private Graphs() {}
  
  public static boolean hasCycle(Graph<?> graph)
  {
    int numEdges = graph.edges().size();
    if (numEdges == 0) {
      return false;
    }
    if ((!graph.isDirected()) && (numEdges >= graph.nodes().size())) {
      return true;
    }
    

    Map<Object, NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
    for (Object node : graph.nodes()) {
      if (subgraphHasCycle(graph, visitedNodes, node, null)) {
        return true;
      }
    }
    return false;
  }
  








  public static boolean hasCycle(Network<?, ?> network)
  {
    if ((!network.isDirected()) && 
      (network.allowsParallelEdges()) && 
      (network.edges().size() > network.asGraph().edges().size())) {
      return true;
    }
    return hasCycle(network.asGraph());
  }
  








  private static boolean subgraphHasCycle(Graph<?> graph, Map<Object, NodeVisitState> visitedNodes, Object node, @Nullable Object previousNode)
  {
    NodeVisitState state = (NodeVisitState)visitedNodes.get(node);
    if (state == NodeVisitState.COMPLETE) {
      return false;
    }
    if (state == NodeVisitState.PENDING) {
      return true;
    }
    
    visitedNodes.put(node, NodeVisitState.PENDING);
    for (Object nextNode : graph.successors(node)) {
      if ((canTraverseWithoutReusingEdge(graph, nextNode, previousNode)) && 
        (subgraphHasCycle(graph, visitedNodes, nextNode, node))) {
        return true;
      }
    }
    visitedNodes.put(node, NodeVisitState.COMPLETE);
    return false;
  }
  






  private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object nextNode, @Nullable Object previousNode)
  {
    if ((graph.isDirected()) || (!Objects.equal(previousNode, nextNode))) {
      return true;
    }
    

    return false;
  }
  









  public static <N> Graph<N> transitiveClosure(Graph<N> graph)
  {
    MutableGraph<N> transitiveClosure = GraphBuilder.from(graph).allowsSelfLoops(true).build();
    Iterator localIterator1;
    N node;
    Object visitedNodes;
    if (graph.isDirected())
    {
      for (localIterator1 = graph.nodes().iterator(); localIterator1.hasNext();) { node = localIterator1.next();
        for (N reachableNode : reachableNodes(graph, node)) {
          transitiveClosure.putEdge(node, reachableNode);
        }
      }
    }
    else
    {
      visitedNodes = new HashSet();
      for (Object node : graph.nodes())
        if (!((Set)visitedNodes).contains(node)) {
          reachableNodes = reachableNodes(graph, node);
          ((Set)visitedNodes).addAll(reachableNodes);
          pairwiseMatch = 1;
          for (localIterator3 = reachableNodes.iterator(); localIterator3.hasNext();) { nodeU = localIterator3.next();
            for (N nodeV : Iterables.limit(reachableNodes, pairwiseMatch++))
              transitiveClosure.putEdge(nodeU, nodeV);
          }
        } }
    Set<N> reachableNodes;
    int pairwiseMatch;
    Iterator localIterator3;
    N nodeU;
    return transitiveClosure;
  }
  











  public static <N> Set<N> reachableNodes(Graph<N> graph, Object node)
  {
    Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
    Set<N> visitedNodes = new LinkedHashSet();
    Queue<N> queuedNodes = new ArrayDeque();
    visitedNodes.add(node);
    queuedNodes.add(node);
    
    while (!queuedNodes.isEmpty()) {
      N currentNode = queuedNodes.remove();
      for (N successor : graph.successors(currentNode)) {
        if (visitedNodes.add(successor)) {
          queuedNodes.add(successor);
        }
      }
    }
    return Collections.unmodifiableSet(visitedNodes);
  }
  

















  public static boolean equivalent(@Nullable Graph<?> graphA, @Nullable Graph<?> graphB)
  {
    if (graphA == graphB) {
      return true;
    }
    if ((graphA == null) || (graphB == null)) {
      return false;
    }
    
    return (graphA.isDirected() == graphB.isDirected()) && 
      (graphA.nodes().equals(graphB.nodes())) && 
      (graphA.edges().equals(graphB.edges()));
  }
  





















  public static boolean equivalent(@Nullable ValueGraph<?, ?> graphA, @Nullable ValueGraph<?, ?> graphB)
  {
    if (graphA == graphB) {
      return true;
    }
    if ((graphA == null) || (graphB == null)) {
      return false;
    }
    
    if ((graphA.isDirected() != graphB.isDirected()) || 
      (!graphA.nodes().equals(graphB.nodes())) || 
      (!graphA.edges().equals(graphB.edges()))) {
      return false;
    }
    
    for (EndpointPair<?> edge : graphA.edges())
    {

      if (!graphA.edgeValue(edge.nodeU(), edge.nodeV()).equals(graphB.edgeValue(edge.nodeU(), edge.nodeV()))) {
        return false;
      }
    }
    
    return true;
  }
  




















  public static boolean equivalent(@Nullable Network<?, ?> networkA, @Nullable Network<?, ?> networkB)
  {
    if (networkA == networkB) {
      return true;
    }
    if ((networkA == null) || (networkB == null)) {
      return false;
    }
    
    if ((networkA.isDirected() != networkB.isDirected()) || 
      (!networkA.nodes().equals(networkB.nodes())) || 
      (!networkA.edges().equals(networkB.edges()))) {
      return false;
    }
    
    for (Object edge : networkA.edges()) {
      if (!networkA.incidentNodes(edge).equals(networkB.incidentNodes(edge))) {
        return false;
      }
    }
    
    return true;
  }
  







  public static <N> Graph<N> transpose(Graph<N> graph)
  {
    if (!graph.isDirected()) {
      return graph;
    }
    
    if ((graph instanceof TransposedGraph)) {
      return graph;
    }
    
    return new TransposedGraph(graph);
  }
  
  private static class TransposedGraph<N> extends AbstractGraph<N> {
    private final Graph<N> graph;
    
    TransposedGraph(Graph<N> graph) {
      this.graph = graph;
    }
    
    public Set<N> nodes()
    {
      return graph.nodes();
    }
    




    protected long edgeCount()
    {
      return graph.edges().size();
    }
    
    public boolean isDirected()
    {
      return graph.isDirected();
    }
    
    public boolean allowsSelfLoops()
    {
      return graph.allowsSelfLoops();
    }
    
    public ElementOrder<N> nodeOrder()
    {
      return graph.nodeOrder();
    }
    
    public Set<N> adjacentNodes(Object node)
    {
      return graph.adjacentNodes(node);
    }
    
    public Set<N> predecessors(Object node)
    {
      return graph.successors(node);
    }
    
    public Set<N> successors(Object node)
    {
      return graph.predecessors(node);
    }
  }
  



  public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> graph)
  {
    if (!graph.isDirected()) {
      return graph;
    }
    
    if ((graph instanceof TransposedValueGraph)) {
      return graph;
    }
    
    return new TransposedValueGraph(graph);
  }
  
  private static class TransposedValueGraph<N, V> extends AbstractValueGraph<N, V> {
    private final ValueGraph<N, V> graph;
    
    TransposedValueGraph(ValueGraph<N, V> graph) {
      this.graph = graph;
    }
    
    public Set<N> nodes()
    {
      return graph.nodes();
    }
    




    protected long edgeCount()
    {
      return graph.edges().size();
    }
    
    public boolean isDirected()
    {
      return graph.isDirected();
    }
    
    public boolean allowsSelfLoops()
    {
      return graph.allowsSelfLoops();
    }
    
    public ElementOrder<N> nodeOrder()
    {
      return graph.nodeOrder();
    }
    
    public Set<N> adjacentNodes(Object node)
    {
      return graph.adjacentNodes(node);
    }
    
    public Set<N> predecessors(Object node)
    {
      return graph.successors(node);
    }
    
    public Set<N> successors(Object node)
    {
      return graph.predecessors(node);
    }
    
    public V edgeValue(Object nodeU, Object nodeV)
    {
      return graph.edgeValue(nodeV, nodeU);
    }
    
    public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue)
    {
      return graph.edgeValueOrDefault(nodeV, nodeU, defaultValue);
    }
  }
  



  public static <N, E> Network<N, E> transpose(Network<N, E> network)
  {
    if (!network.isDirected()) {
      return network;
    }
    
    if ((network instanceof TransposedNetwork)) {
      return network;
    }
    
    return new TransposedNetwork(network);
  }
  
  private static class TransposedNetwork<N, E> extends AbstractNetwork<N, E> {
    private final Network<N, E> network;
    
    TransposedNetwork(Network<N, E> network) {
      this.network = network;
    }
    
    public Set<N> nodes()
    {
      return network.nodes();
    }
    
    public Set<E> edges()
    {
      return network.edges();
    }
    
    public boolean isDirected()
    {
      return network.isDirected();
    }
    
    public boolean allowsParallelEdges()
    {
      return network.allowsParallelEdges();
    }
    
    public boolean allowsSelfLoops()
    {
      return network.allowsSelfLoops();
    }
    
    public ElementOrder<N> nodeOrder()
    {
      return network.nodeOrder();
    }
    
    public ElementOrder<E> edgeOrder()
    {
      return network.edgeOrder();
    }
    
    public Set<N> adjacentNodes(Object node)
    {
      return network.adjacentNodes(node);
    }
    
    public Set<N> predecessors(Object node)
    {
      return network.successors(node);
    }
    
    public Set<N> successors(Object node)
    {
      return network.predecessors(node);
    }
    
    public Set<E> incidentEdges(Object node)
    {
      return network.incidentEdges(node);
    }
    
    public Set<E> inEdges(Object node)
    {
      return network.outEdges(node);
    }
    
    public Set<E> outEdges(Object node)
    {
      return network.inEdges(node);
    }
    
    public EndpointPair<N> incidentNodes(Object edge)
    {
      EndpointPair<N> endpointPair = network.incidentNodes(edge);
      return EndpointPair.of(network, endpointPair.nodeV(), endpointPair.nodeU());
    }
    
    public Set<E> adjacentEdges(Object edge)
    {
      return network.adjacentEdges(edge);
    }
    
    public Set<E> edgesConnecting(Object nodeU, Object nodeV)
    {
      return network.edgesConnecting(nodeV, nodeU);
    }
  }
  








  public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> nodes)
  {
    MutableGraph<N> subgraph = GraphBuilder.from(graph).build();
    for (N node : nodes) {
      subgraph.addNode(node);
    }
    for (??? = subgraph.nodes().iterator(); ???.hasNext();) { node = ???.next();
      for (N successorNode : graph.successors(node)) {
        if (subgraph.nodes().contains(successorNode))
          subgraph.putEdge(node, successorNode);
      }
    }
    N node;
    return subgraph;
  }
  








  public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> graph, Iterable<? extends N> nodes)
  {
    MutableValueGraph<N, V> subgraph = ValueGraphBuilder.from(graph).build();
    for (N node : nodes) {
      subgraph.addNode(node);
    }
    for (??? = subgraph.nodes().iterator(); ???.hasNext();) { node = ???.next();
      for (N successorNode : graph.successors(node)) {
        if (subgraph.nodes().contains(successorNode))
          subgraph.putEdgeValue(node, successorNode, graph.edgeValue(node, successorNode));
      }
    }
    N node;
    return subgraph;
  }
  








  public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> nodes)
  {
    MutableNetwork<N, E> subgraph = NetworkBuilder.from(network).build();
    for (N node : nodes) {
      subgraph.addNode(node);
    }
    for (??? = subgraph.nodes().iterator(); ???.hasNext();) { node = ???.next();
      for (E edge : network.outEdges(node)) {
        N successorNode = network.incidentNodes(edge).adjacentNode(node);
        if (subgraph.nodes().contains(successorNode))
          subgraph.addEdge(node, successorNode, edge);
      }
    }
    N node;
    return subgraph;
  }
  
  public static <N> MutableGraph<N> copyOf(Graph<N> graph)
  {
    MutableGraph<N> copy = GraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
    for (N node : graph.nodes()) {
      copy.addNode(node);
    }
    for (EndpointPair<N> edge : graph.edges()) {
      copy.putEdge(edge.nodeU(), edge.nodeV());
    }
    return copy;
  }
  

  public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph)
  {
    MutableValueGraph<N, V> copy = ValueGraphBuilder.from(graph).expectedNodeCount(graph.nodes().size()).build();
    for (N node : graph.nodes()) {
      copy.addNode(node);
    }
    for (EndpointPair<N> edge : graph.edges()) {
      copy.putEdgeValue(edge.nodeU(), edge.nodeV(), graph.edgeValue(edge.nodeU(), edge.nodeV()));
    }
    return copy;
  }
  




  public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network)
  {
    MutableNetwork<N, E> copy = NetworkBuilder.from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
    for (N node : network.nodes()) {
      copy.addNode(node);
    }
    for (E edge : network.edges()) {
      EndpointPair<N> endpointPair = network.incidentNodes(edge);
      copy.addEdge(endpointPair.nodeU(), endpointPair.nodeV(), edge);
    }
    return copy;
  }
  
  @CanIgnoreReturnValue
  static int checkNonNegative(int value) {
    Preconditions.checkArgument(value >= 0, "Not true that %s is non-negative.", value);
    return value;
  }
  
  @CanIgnoreReturnValue
  static int checkPositive(int value) {
    Preconditions.checkArgument(value > 0, "Not true that %s is positive.", value);
    return value;
  }
  
  @CanIgnoreReturnValue
  static long checkNonNegative(long value) {
    Preconditions.checkArgument(value >= 0L, "Not true that %s is non-negative.", value);
    return value;
  }
  
  @CanIgnoreReturnValue
  static long checkPositive(long value) {
    Preconditions.checkArgument(value > 0L, "Not true that %s is positive.", value);
    return value;
  }
  




  private static enum NodeVisitState
  {
    PENDING, 
    COMPLETE;
    
    private NodeVisitState() {}
  }
}
