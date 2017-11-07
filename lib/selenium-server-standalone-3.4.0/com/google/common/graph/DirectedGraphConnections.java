package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;






























final class DirectedGraphConnections<N, V>
  implements GraphConnections<N, V>
{
  private static final class PredAndSucc
  {
    private final Object successorValue;
    
    PredAndSucc(Object successorValue)
    {
      this.successorValue = successorValue;
    }
  }
  
  private static final Object PRED = new Object();
  
  private final Map<N, Object> adjacentNodeValues;
  
  private int predecessorCount;
  
  private int successorCount;
  

  private DirectedGraphConnections(Map<N, Object> adjacentNodeValues, int predecessorCount, int successorCount)
  {
    this.adjacentNodeValues = ((Map)Preconditions.checkNotNull(adjacentNodeValues));
    this.predecessorCount = Graphs.checkNonNegative(predecessorCount);
    this.successorCount = Graphs.checkNonNegative(successorCount);
    Preconditions.checkState(
      (predecessorCount <= adjacentNodeValues.size()) && 
      (successorCount <= adjacentNodeValues.size()));
  }
  
  static <N, V> DirectedGraphConnections<N, V> of()
  {
    int initialCapacity = 4;
    return new DirectedGraphConnections(new HashMap(initialCapacity, 1.0F), 0, 0);
  }
  

  static <N, V> DirectedGraphConnections<N, V> ofImmutable(Set<N> predecessors, Map<N, V> successorValues)
  {
    Map<N, Object> adjacentNodeValues = new HashMap();
    adjacentNodeValues.putAll(successorValues);
    for (N predecessor : predecessors) {
      Object value = adjacentNodeValues.put(predecessor, PRED);
      if (value != null) {
        adjacentNodeValues.put(predecessor, new PredAndSucc(value));
      }
    }
    return new DirectedGraphConnections(
      ImmutableMap.copyOf(adjacentNodeValues), predecessors.size(), successorValues.size());
  }
  
  public Set<N> adjacentNodes()
  {
    return Collections.unmodifiableSet(adjacentNodeValues.keySet());
  }
  
  public Set<N> predecessors()
  {
    new AbstractSet()
    {
      public UnmodifiableIterator<N> iterator() {
        final Iterator<Map.Entry<N, Object>> entries = adjacentNodeValues.entrySet().iterator();
        new AbstractIterator()
        {
          protected N computeNext() {
            while (entries.hasNext()) {
              Map.Entry<N, Object> entry = (Map.Entry)entries.next();
              if (DirectedGraphConnections.isPredecessor(entry.getValue())) {
                return entry.getKey();
              }
            }
            return endOfData();
          }
        };
      }
      
      public int size()
      {
        return predecessorCount;
      }
      
      public boolean contains(@Nullable Object obj)
      {
        return DirectedGraphConnections.isPredecessor(adjacentNodeValues.get(obj));
      }
    };
  }
  
  public Set<N> successors()
  {
    new AbstractSet()
    {
      public UnmodifiableIterator<N> iterator() {
        final Iterator<Map.Entry<N, Object>> entries = adjacentNodeValues.entrySet().iterator();
        new AbstractIterator()
        {
          protected N computeNext() {
            while (entries.hasNext()) {
              Map.Entry<N, Object> entry = (Map.Entry)entries.next();
              if (DirectedGraphConnections.isSuccessor(entry.getValue())) {
                return entry.getKey();
              }
            }
            return endOfData();
          }
        };
      }
      
      public int size()
      {
        return successorCount;
      }
      
      public boolean contains(@Nullable Object obj)
      {
        return DirectedGraphConnections.isSuccessor(adjacentNodeValues.get(obj));
      }
    };
  }
  

  public V value(Object node)
  {
    Object value = adjacentNodeValues.get(node);
    if (value == PRED) {
      return null;
    }
    if ((value instanceof PredAndSucc)) {
      return successorValue;
    }
    return value;
  }
  

  public void removePredecessor(Object node)
  {
    Object previousValue = adjacentNodeValues.get(node);
    if (previousValue == PRED) {
      adjacentNodeValues.remove(node);
      Graphs.checkNonNegative(--predecessorCount);
    } else if ((previousValue instanceof PredAndSucc)) {
      adjacentNodeValues.put(node, successorValue);
      Graphs.checkNonNegative(--predecessorCount);
    }
  }
  

  public V removeSuccessor(Object node)
  {
    Object previousValue = adjacentNodeValues.get(node);
    if ((previousValue == null) || (previousValue == PRED))
      return null;
    if ((previousValue instanceof PredAndSucc)) {
      adjacentNodeValues.put(node, PRED);
      Graphs.checkNonNegative(--successorCount);
      return successorValue;
    }
    adjacentNodeValues.remove(node);
    Graphs.checkNonNegative(--successorCount);
    return previousValue;
  }
  

  public void addPredecessor(N node, V unused)
  {
    Object previousValue = adjacentNodeValues.put(node, PRED);
    if (previousValue == null) {
      Graphs.checkPositive(++predecessorCount);
    } else if ((previousValue instanceof PredAndSucc))
    {
      adjacentNodeValues.put(node, previousValue);
    } else if (previousValue != PRED)
    {
      adjacentNodeValues.put(node, new PredAndSucc(previousValue));
      Graphs.checkPositive(++predecessorCount);
    }
  }
  

  public V addSuccessor(N node, V value)
  {
    Object previousValue = adjacentNodeValues.put(node, value);
    if (previousValue == null) {
      Graphs.checkPositive(++successorCount);
      return null; }
    if ((previousValue instanceof PredAndSucc)) {
      adjacentNodeValues.put(node, new PredAndSucc(value));
      return successorValue; }
    if (previousValue == PRED) {
      adjacentNodeValues.put(node, new PredAndSucc(value));
      Graphs.checkPositive(++successorCount);
      return null;
    }
    return previousValue;
  }
  
  private static boolean isPredecessor(@Nullable Object value)
  {
    return (value == PRED) || ((value instanceof PredAndSucc));
  }
  
  private static boolean isSuccessor(@Nullable Object value) {
    return (value != PRED) && (value != null);
  }
}
