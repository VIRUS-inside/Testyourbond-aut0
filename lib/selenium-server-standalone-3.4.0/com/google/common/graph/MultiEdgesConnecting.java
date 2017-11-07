package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;
























abstract class MultiEdgesConnecting<E>
  extends AbstractSet<E>
{
  private final Map<E, ?> outEdgeToNode;
  private final Object targetNode;
  
  MultiEdgesConnecting(Map<E, ?> outEdgeToNode, Object targetNode)
  {
    this.outEdgeToNode = ((Map)Preconditions.checkNotNull(outEdgeToNode));
    this.targetNode = Preconditions.checkNotNull(targetNode);
  }
  
  public UnmodifiableIterator<E> iterator()
  {
    final Iterator<? extends Map.Entry<E, ?>> entries = outEdgeToNode.entrySet().iterator();
    new AbstractIterator()
    {
      protected E computeNext() {
        while (entries.hasNext()) {
          Map.Entry<E, ?> entry = (Map.Entry)entries.next();
          if (targetNode.equals(entry.getValue())) {
            return entry.getKey();
          }
        }
        return endOfData();
      }
    };
  }
  
  public boolean contains(@Nullable Object edge)
  {
    return targetNode.equals(outEdgeToNode.get(edge));
  }
}
