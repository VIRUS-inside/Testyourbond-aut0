package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;


























@Beta
@GwtIncompatible
public class ImmutableRangeMap<K extends Comparable<?>, V>
  implements RangeMap<K, V>, Serializable
{
  private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(
  
    ImmutableList.of(), ImmutableList.of());
  private final transient ImmutableList<Range<K>> ranges;
  private final transient ImmutableList<V> values;
  private static final long serialVersionUID = 0L;
  
  public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of()
  {
    return EMPTY;
  }
  


  public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value)
  {
    return new ImmutableRangeMap(ImmutableList.of(range), ImmutableList.of(value));
  }
  

  public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap)
  {
    if ((rangeMap instanceof ImmutableRangeMap)) {
      return (ImmutableRangeMap)rangeMap;
    }
    Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
    ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder(map.size());
    ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder(map.size());
    for (Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
      rangesBuilder.add(entry.getKey());
      valuesBuilder.add(entry.getValue());
    }
    return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
  }
  


  public static <K extends Comparable<?>, V> Builder<K, V> builder()
  {
    return new Builder();
  }
  

  public static final class Builder<K extends Comparable<?>, V>
  {
    private final List<Map.Entry<Range<K>, V>> entries;
    
    public Builder()
    {
      entries = Lists.newArrayList();
    }
    




    @CanIgnoreReturnValue
    public Builder<K, V> put(Range<K> range, V value)
    {
      Preconditions.checkNotNull(range);
      Preconditions.checkNotNull(value);
      Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
      entries.add(Maps.immutableEntry(range, value));
      return this;
    }
    


    @CanIgnoreReturnValue
    public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap)
    {
      for (Map.Entry<Range<K>, ? extends V> entry : rangeMap.asMapOfRanges().entrySet()) {
        put((Range)entry.getKey(), entry.getValue());
      }
      return this;
    }
    





    public ImmutableRangeMap<K, V> build()
    {
      Collections.sort(entries, Range.RANGE_LEX_ORDERING.onKeys());
      
      ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder(entries.size());
      ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder(entries.size());
      for (int i = 0; i < entries.size(); i++) {
        Range<K> range = (Range)((Map.Entry)entries.get(i)).getKey();
        if (i > 0) {
          Range<K> prevRange = (Range)((Map.Entry)entries.get(i - 1)).getKey();
          if ((range.isConnected(prevRange)) && (!range.intersection(prevRange).isEmpty())) {
            throw new IllegalArgumentException("Overlapping ranges: range " + prevRange + " overlaps with entry " + range);
          }
        }
        
        rangesBuilder.add(range);
        valuesBuilder.add(((Map.Entry)entries.get(i)).getValue());
      }
      return new ImmutableRangeMap(rangesBuilder.build(), valuesBuilder.build());
    }
  }
  


  ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values)
  {
    this.ranges = ranges;
    this.values = values;
  }
  

  @Nullable
  public V get(K key)
  {
    int index = SortedLists.binarySearch(ranges, 
    
      Range.lowerBoundFn(), 
      Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
    

    if (index == -1) {
      return null;
    }
    Range<K> range = (Range)ranges.get(index);
    return range.contains(key) ? values.get(index) : null;
  }
  


  @Nullable
  public Map.Entry<Range<K>, V> getEntry(K key)
  {
    int index = SortedLists.binarySearch(ranges, 
    
      Range.lowerBoundFn(), 
      Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
    

    if (index == -1) {
      return null;
    }
    Range<K> range = (Range)ranges.get(index);
    return range.contains(key) ? Maps.immutableEntry(range, values.get(index)) : null;
  }
  

  public Range<K> span()
  {
    if (ranges.isEmpty()) {
      throw new NoSuchElementException();
    }
    Range<K> firstRange = (Range)ranges.get(0);
    Range<K> lastRange = (Range)ranges.get(ranges.size() - 1);
    return Range.create(lowerBound, upperBound);
  }
  






  @Deprecated
  public void put(Range<K> range, V value)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public void putAll(RangeMap<K, V> rangeMap)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public void clear()
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public void remove(Range<K> range)
  {
    throw new UnsupportedOperationException();
  }
  
  public ImmutableMap<Range<K>, V> asMapOfRanges()
  {
    if (ranges.isEmpty()) {
      return ImmutableMap.of();
    }
    RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet(ranges, Range.RANGE_LEX_ORDERING);
    
    return new ImmutableSortedMap(rangeSet, values);
  }
  
  public ImmutableMap<Range<K>, V> asDescendingMapOfRanges()
  {
    if (ranges.isEmpty()) {
      return ImmutableMap.of();
    }
    

    RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet(ranges.reverse(), Range.RANGE_LEX_ORDERING.reverse());
    return new ImmutableSortedMap(rangeSet, values.reverse());
  }
  
  public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range)
  {
    if (((Range)Preconditions.checkNotNull(range)).isEmpty())
      return of();
    if ((ranges.isEmpty()) || (range.encloses(span()))) {
      return this;
    }
    
    int lowerIndex = SortedLists.binarySearch(ranges, 
    
      Range.upperBoundFn(), lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    



    int upperIndex = SortedLists.binarySearch(ranges, 
    
      Range.lowerBoundFn(), upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
    


    if (lowerIndex >= upperIndex) {
      return of();
    }
    final int off = lowerIndex;
    final int len = upperIndex - lowerIndex;
    ImmutableList<Range<K>> subRanges = new ImmutableList()
    {
      public int size()
      {
        return len;
      }
      
      public Range<K> get(int index)
      {
        Preconditions.checkElementIndex(index, len);
        if ((index == 0) || (index == len - 1)) {
          return ((Range)ranges.get(index + off)).intersection(range);
        }
        return (Range)ranges.get(index + off);
      }
      

      boolean isPartialView()
      {
        return true;
      }
    };
    final ImmutableRangeMap<K, V> outer = this;
    new ImmutableRangeMap(subRanges, values.subList(lowerIndex, upperIndex))
    {
      public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
        if (range.isConnected(subRange)) {
          return outer.subRangeMap(subRange.intersection(range));
        }
        return ImmutableRangeMap.of();
      }
    };
  }
  

  public int hashCode()
  {
    return asMapOfRanges().hashCode();
  }
  
  public boolean equals(@Nullable Object o)
  {
    if ((o instanceof RangeMap)) {
      RangeMap<?, ?> rangeMap = (RangeMap)o;
      return asMapOfRanges().equals(rangeMap.asMapOfRanges());
    }
    return false;
  }
  
  public String toString()
  {
    return asMapOfRanges().toString();
  }
  

  private static class SerializedForm<K extends Comparable<?>, V>
    implements Serializable
  {
    private final ImmutableMap<Range<K>, V> mapOfRanges;
    private static final long serialVersionUID = 0L;
    
    SerializedForm(ImmutableMap<Range<K>, V> mapOfRanges)
    {
      this.mapOfRanges = mapOfRanges;
    }
    
    Object readResolve() {
      if (mapOfRanges.isEmpty()) {
        return ImmutableRangeMap.of();
      }
      return createRangeMap();
    }
    
    Object createRangeMap()
    {
      ImmutableRangeMap.Builder<K, V> builder = new ImmutableRangeMap.Builder();
      for (UnmodifiableIterator localUnmodifiableIterator = mapOfRanges.entrySet().iterator(); localUnmodifiableIterator.hasNext();) { Map.Entry<Range<K>, V> entry = (Map.Entry)localUnmodifiableIterator.next();
        builder.put((Range)entry.getKey(), entry.getValue());
      }
      return builder.build();
    }
  }
  

  Object writeReplace()
  {
    return new SerializedForm(asMapOfRanges());
  }
}
