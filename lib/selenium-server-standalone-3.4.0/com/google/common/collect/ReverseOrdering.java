package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;














@GwtCompatible(serializable=true)
final class ReverseOrdering<T>
  extends Ordering<T>
  implements Serializable
{
  final Ordering<? super T> forwardOrder;
  private static final long serialVersionUID = 0L;
  
  ReverseOrdering(Ordering<? super T> forwardOrder)
  {
    this.forwardOrder = ((Ordering)Preconditions.checkNotNull(forwardOrder));
  }
  
  public int compare(T a, T b)
  {
    return forwardOrder.compare(b, a);
  }
  

  public <S extends T> Ordering<S> reverse()
  {
    return forwardOrder;
  }
  


  public <E extends T> E min(E a, E b)
  {
    return forwardOrder.max(a, b);
  }
  
  public <E extends T> E min(E a, E b, E c, E... rest)
  {
    return forwardOrder.max(a, b, c, rest);
  }
  
  public <E extends T> E min(Iterator<E> iterator)
  {
    return forwardOrder.max(iterator);
  }
  
  public <E extends T> E min(Iterable<E> iterable)
  {
    return forwardOrder.max(iterable);
  }
  
  public <E extends T> E max(E a, E b)
  {
    return forwardOrder.min(a, b);
  }
  
  public <E extends T> E max(E a, E b, E c, E... rest)
  {
    return forwardOrder.min(a, b, c, rest);
  }
  
  public <E extends T> E max(Iterator<E> iterator)
  {
    return forwardOrder.min(iterator);
  }
  
  public <E extends T> E max(Iterable<E> iterable)
  {
    return forwardOrder.min(iterable);
  }
  
  public int hashCode()
  {
    return -forwardOrder.hashCode();
  }
  
  public boolean equals(@Nullable Object object)
  {
    if (object == this) {
      return true;
    }
    if ((object instanceof ReverseOrdering)) {
      ReverseOrdering<?> that = (ReverseOrdering)object;
      return forwardOrder.equals(forwardOrder);
    }
    return false;
  }
  
  public String toString()
  {
    return forwardOrder + ".reverse()";
  }
}
