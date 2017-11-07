package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.SortedSet;























@GwtCompatible
final class SortedIterables
{
  private SortedIterables() {}
  
  public static boolean hasSameComparator(Comparator<?> comparator, Iterable<?> elements)
  {
    Preconditions.checkNotNull(comparator);
    Preconditions.checkNotNull(elements);
    Comparator<?> comparator2;
    if ((elements instanceof SortedSet)) {
      comparator2 = comparator((SortedSet)elements); } else { Comparator<?> comparator2;
      if ((elements instanceof SortedIterable)) {
        comparator2 = ((SortedIterable)elements).comparator();
      } else
        return false; }
    Comparator<?> comparator2;
    return comparator.equals(comparator2);
  }
  

  public static <E> Comparator<? super E> comparator(SortedSet<E> sortedSet)
  {
    Comparator<? super E> result = sortedSet.comparator();
    if (result == null) {
      result = Ordering.natural();
    }
    return result;
  }
}
