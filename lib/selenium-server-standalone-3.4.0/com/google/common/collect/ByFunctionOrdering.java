package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import javax.annotation.Nullable;

















@GwtCompatible(serializable=true)
final class ByFunctionOrdering<F, T>
  extends Ordering<F>
  implements Serializable
{
  final Function<F, ? extends T> function;
  final Ordering<T> ordering;
  private static final long serialVersionUID = 0L;
  
  ByFunctionOrdering(Function<F, ? extends T> function, Ordering<T> ordering)
  {
    this.function = ((Function)Preconditions.checkNotNull(function));
    this.ordering = ((Ordering)Preconditions.checkNotNull(ordering));
  }
  
  public int compare(F left, F right)
  {
    return ordering.compare(function.apply(left), function.apply(right));
  }
  
  public boolean equals(@Nullable Object object)
  {
    if (object == this) {
      return true;
    }
    if ((object instanceof ByFunctionOrdering)) {
      ByFunctionOrdering<?, ?> that = (ByFunctionOrdering)object;
      return (function.equals(function)) && (ordering.equals(ordering));
    }
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hashCode(new Object[] { function, ordering });
  }
  
  public String toString()
  {
    return ordering + ".onResultOf(" + function + ")";
  }
}
