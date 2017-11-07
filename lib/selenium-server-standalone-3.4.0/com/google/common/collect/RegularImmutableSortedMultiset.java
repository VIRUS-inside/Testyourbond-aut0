package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.Comparator;
import java.util.function.ObjIntConsumer;
import javax.annotation.Nullable;






















@GwtIncompatible
final class RegularImmutableSortedMultiset<E>
  extends ImmutableSortedMultiset<E>
{
  private static final long[] ZERO_CUMULATIVE_COUNTS = { 0L };
  
  static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET = new RegularImmutableSortedMultiset(
    Ordering.natural());
  private final transient RegularImmutableSortedSet<E> elementSet;
  private final transient long[] cumulativeCounts;
  private final transient int offset;
  private final transient int length;
  
  RegularImmutableSortedMultiset(Comparator<? super E> comparator)
  {
    elementSet = ImmutableSortedSet.emptySet(comparator);
    cumulativeCounts = ZERO_CUMULATIVE_COUNTS;
    offset = 0;
    length = 0;
  }
  
  RegularImmutableSortedMultiset(RegularImmutableSortedSet<E> elementSet, long[] cumulativeCounts, int offset, int length)
  {
    this.elementSet = elementSet;
    this.cumulativeCounts = cumulativeCounts;
    this.offset = offset;
    this.length = length;
  }
  
  private int getCount(int index) {
    return (int)(cumulativeCounts[(offset + index + 1)] - cumulativeCounts[(offset + index)]);
  }
  
  Multiset.Entry<E> getEntry(int index)
  {
    return Multisets.immutableEntry(elementSet.asList().get(index), getCount(index));
  }
  
  public void forEachEntry(ObjIntConsumer<? super E> action)
  {
    Preconditions.checkNotNull(action);
    for (int i = 0; i < size(); i++) {
      action.accept(elementSet.asList().get(i), getCount(i));
    }
  }
  
  public Multiset.Entry<E> firstEntry()
  {
    return isEmpty() ? null : getEntry(0);
  }
  
  public Multiset.Entry<E> lastEntry()
  {
    return isEmpty() ? null : getEntry(length - 1);
  }
  
  public int count(@Nullable Object element)
  {
    int index = elementSet.indexOf(element);
    return index >= 0 ? getCount(index) : 0;
  }
  
  public int size()
  {
    long size = cumulativeCounts[(offset + length)] - cumulativeCounts[offset];
    return Ints.saturatedCast(size);
  }
  
  public ImmutableSortedSet<E> elementSet()
  {
    return elementSet;
  }
  
  public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType)
  {
    return getSubMultiset(0, elementSet.headIndex(upperBound, Preconditions.checkNotNull(boundType) == BoundType.CLOSED));
  }
  
  public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType)
  {
    return getSubMultiset(elementSet
      .tailIndex(lowerBound, Preconditions.checkNotNull(boundType) == BoundType.CLOSED), length);
  }
  
  ImmutableSortedMultiset<E> getSubMultiset(int from, int to) {
    Preconditions.checkPositionIndexes(from, to, length);
    if (from == to)
      return emptyMultiset(comparator());
    if ((from == 0) && (to == length)) {
      return this;
    }
    RegularImmutableSortedSet<E> subElementSet = elementSet.getSubSet(from, to);
    return new RegularImmutableSortedMultiset(subElementSet, cumulativeCounts, offset + from, to - from);
  }
  


  boolean isPartialView()
  {
    return (offset > 0) || (length < cumulativeCounts.length - 1);
  }
}
