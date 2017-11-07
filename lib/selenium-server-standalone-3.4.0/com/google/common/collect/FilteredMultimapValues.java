package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.j2objc.annotations.Weak;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;


















@GwtCompatible
final class FilteredMultimapValues<K, V>
  extends AbstractCollection<V>
{
  @Weak
  private final FilteredMultimap<K, V> multimap;
  
  FilteredMultimapValues(FilteredMultimap<K, V> multimap)
  {
    this.multimap = ((FilteredMultimap)Preconditions.checkNotNull(multimap));
  }
  
  public Iterator<V> iterator()
  {
    return Maps.valueIterator(multimap.entries().iterator());
  }
  
  public boolean contains(@Nullable Object o)
  {
    return multimap.containsValue(o);
  }
  
  public int size()
  {
    return multimap.size();
  }
  
  public boolean remove(@Nullable Object o)
  {
    Predicate<? super Map.Entry<K, V>> entryPredicate = multimap.entryPredicate();
    Iterator<Map.Entry<K, V>> unfilteredItr = multimap.unfiltered().entries().iterator();
    while (unfilteredItr.hasNext())
    {
      Map.Entry<K, V> entry = (Map.Entry)unfilteredItr.next();
      if ((entryPredicate.apply(entry)) && (Objects.equal(entry.getValue(), o))) {
        unfilteredItr.remove();
        return true;
      }
    }
    return false;
  }
  
  public boolean removeAll(Collection<?> c)
  {
    return Iterables.removeIf(multimap
      .unfiltered().entries(), 
      
      Predicates.and(multimap
      .entryPredicate(), Maps.valuePredicateOnEntries(Predicates.in(c))));
  }
  
  public boolean retainAll(Collection<?> c)
  {
    return Iterables.removeIf(multimap
      .unfiltered().entries(), 
      
      Predicates.and(multimap
      .entryPredicate(), 
      Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c)))));
  }
  
  public void clear()
  {
    multimap.clear();
  }
}
