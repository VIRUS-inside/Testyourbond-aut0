package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.Nullable;














@GwtCompatible(serializable=true)
final class ComparatorOrdering<T>
  extends Ordering<T>
  implements Serializable
{
  final Comparator<T> comparator;
  private static final long serialVersionUID = 0L;
  
  ComparatorOrdering(Comparator<T> comparator)
  {
    this.comparator = ((Comparator)Preconditions.checkNotNull(comparator));
  }
  
  public int compare(T a, T b)
  {
    return comparator.compare(a, b);
  }
  
  public boolean equals(@Nullable Object object)
  {
    if (object == this) {
      return true;
    }
    if ((object instanceof ComparatorOrdering)) {
      ComparatorOrdering<?> that = (ComparatorOrdering)object;
      return comparator.equals(comparator);
    }
    return false;
  }
  
  public int hashCode()
  {
    return comparator.hashCode();
  }
  
  public String toString()
  {
    return comparator.toString();
  }
}
