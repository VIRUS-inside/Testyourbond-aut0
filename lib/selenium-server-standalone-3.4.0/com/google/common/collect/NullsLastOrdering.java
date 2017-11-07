package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import javax.annotation.Nullable;













@GwtCompatible(serializable=true)
final class NullsLastOrdering<T>
  extends Ordering<T>
  implements Serializable
{
  final Ordering<? super T> ordering;
  private static final long serialVersionUID = 0L;
  
  NullsLastOrdering(Ordering<? super T> ordering)
  {
    this.ordering = ordering;
  }
  
  public int compare(@Nullable T left, @Nullable T right)
  {
    if (left == right) {
      return 0;
    }
    if (left == null) {
      return 1;
    }
    if (right == null) {
      return -1;
    }
    return ordering.compare(left, right);
  }
  

  public <S extends T> Ordering<S> reverse()
  {
    return ordering.reverse().nullsFirst();
  }
  
  public <S extends T> Ordering<S> nullsFirst()
  {
    return ordering.nullsFirst();
  }
  

  public <S extends T> Ordering<S> nullsLast()
  {
    return this;
  }
  
  public boolean equals(@Nullable Object object)
  {
    if (object == this) {
      return true;
    }
    if ((object instanceof NullsLastOrdering)) {
      NullsLastOrdering<?> that = (NullsLastOrdering)object;
      return ordering.equals(ordering);
    }
    return false;
  }
  
  public int hashCode()
  {
    return ordering.hashCode() ^ 0xC9177248;
  }
  
  public String toString()
  {
    return ordering + ".nullsLast()";
  }
}
