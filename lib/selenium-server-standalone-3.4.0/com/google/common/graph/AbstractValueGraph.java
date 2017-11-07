package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;


























@Beta
public abstract class AbstractValueGraph<N, V>
  extends AbstractGraph<N>
  implements ValueGraph<N, V>
{
  public AbstractValueGraph() {}
  
  public V edgeValue(Object nodeU, Object nodeV)
  {
    V value = edgeValueOrDefault(nodeU, nodeV, null);
    if (value == null) {
      Preconditions.checkArgument(nodes().contains(nodeU), "Node %s is not an element of this graph.", nodeU);
      Preconditions.checkArgument(nodes().contains(nodeV), "Node %s is not an element of this graph.", nodeV);
      throw new IllegalArgumentException(String.format("Edge connecting %s to %s is not present in this graph.", new Object[] { nodeU, nodeV }));
    }
    return value;
  }
  


  public String toString()
  {
    String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", new Object[] { Boolean.valueOf(isDirected()), Boolean.valueOf(allowsSelfLoops()) });
    return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, nodes(), edgeValueMap() });
  }
  
  private Map<EndpointPair<N>, V> edgeValueMap() {
    Function<EndpointPair<N>, V> edgeToValueFn = new Function()
    {
      public V apply(EndpointPair<N> edge)
      {
        return edgeValue(edge.nodeU(), edge.nodeV());
      }
    };
    return Maps.asMap(edges(), edgeToValueFn);
  }
}
