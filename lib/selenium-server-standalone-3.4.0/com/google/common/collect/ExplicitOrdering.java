package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;













@GwtCompatible(serializable=true)
final class ExplicitOrdering<T>
  extends Ordering<T>
  implements Serializable
{
  final ImmutableMap<T, Integer> rankMap;
  private static final long serialVersionUID = 0L;
  
  ExplicitOrdering(List<T> valuesInOrder)
  {
    this(Maps.indexMap(valuesInOrder));
  }
  
  ExplicitOrdering(ImmutableMap<T, Integer> rankMap) {
    this.rankMap = rankMap;
  }
  
  public int compare(T left, T right)
  {
    return rank(left) - rank(right);
  }
  
  private int rank(T value) {
    Integer rank = (Integer)rankMap.get(value);
    if (rank == null) {
      throw new Ordering.IncomparableValueException(value);
    }
    return rank.intValue();
  }
  
  public boolean equals(@Nullable Object object)
  {
    if ((object instanceof ExplicitOrdering)) {
      ExplicitOrdering<?> that = (ExplicitOrdering)object;
      return rankMap.equals(rankMap);
    }
    return false;
  }
  
  public int hashCode()
  {
    return rankMap.hashCode();
  }
  
  public String toString()
  {
    return "Ordering.explicit(" + rankMap.keySet() + ")";
  }
}
