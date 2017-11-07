package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface SortedSetMultimap<K, V>
  extends SetMultimap<K, V>
{
  public abstract SortedSet<V> get(@Nullable K paramK);
  
  @CanIgnoreReturnValue
  public abstract SortedSet<V> removeAll(@Nullable Object paramObject);
  
  @CanIgnoreReturnValue
  public abstract SortedSet<V> replaceValues(K paramK, Iterable<? extends V> paramIterable);
  
  public abstract Map<K, Collection<V>> asMap();
  
  public abstract Comparator<? super V> valueComparator();
}
