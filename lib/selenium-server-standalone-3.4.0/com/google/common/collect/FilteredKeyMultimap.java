package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;



















@GwtCompatible
class FilteredKeyMultimap<K, V>
  extends AbstractMultimap<K, V>
  implements FilteredMultimap<K, V>
{
  final Multimap<K, V> unfiltered;
  final Predicate<? super K> keyPredicate;
  
  FilteredKeyMultimap(Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate)
  {
    this.unfiltered = ((Multimap)Preconditions.checkNotNull(unfiltered));
    this.keyPredicate = ((Predicate)Preconditions.checkNotNull(keyPredicate));
  }
  
  public Multimap<K, V> unfiltered()
  {
    return unfiltered;
  }
  
  public Predicate<? super Map.Entry<K, V>> entryPredicate()
  {
    return Maps.keyPredicateOnEntries(keyPredicate);
  }
  
  public int size()
  {
    int size = 0;
    for (Collection<V> collection : asMap().values()) {
      size += collection.size();
    }
    return size;
  }
  
  public boolean containsKey(@Nullable Object key)
  {
    if (unfiltered.containsKey(key))
    {
      K k = key;
      return keyPredicate.apply(k);
    }
    return false;
  }
  
  public Collection<V> removeAll(Object key)
  {
    return containsKey(key) ? unfiltered.removeAll(key) : unmodifiableEmptyCollection();
  }
  
  Collection<V> unmodifiableEmptyCollection() {
    if ((unfiltered instanceof SetMultimap)) {
      return ImmutableSet.of();
    }
    return ImmutableList.of();
  }
  

  public void clear()
  {
    keySet().clear();
  }
  
  Set<K> createKeySet()
  {
    return Sets.filter(unfiltered.keySet(), keyPredicate);
  }
  
  public Collection<V> get(K key)
  {
    if (keyPredicate.apply(key))
      return unfiltered.get(key);
    if ((unfiltered instanceof SetMultimap)) {
      return new AddRejectingSet(key);
    }
    return new AddRejectingList(key);
  }
  
  static class AddRejectingSet<K, V> extends ForwardingSet<V>
  {
    final K key;
    
    AddRejectingSet(K key) {
      this.key = key;
    }
    
    public boolean add(V element)
    {
      throw new IllegalArgumentException("Key does not satisfy predicate: " + key);
    }
    
    public boolean addAll(Collection<? extends V> collection)
    {
      Preconditions.checkNotNull(collection);
      throw new IllegalArgumentException("Key does not satisfy predicate: " + key);
    }
    
    protected Set<V> delegate()
    {
      return Collections.emptySet();
    }
  }
  
  static class AddRejectingList<K, V> extends ForwardingList<V> {
    final K key;
    
    AddRejectingList(K key) {
      this.key = key;
    }
    
    public boolean add(V v)
    {
      add(0, v);
      return true;
    }
    
    public boolean addAll(Collection<? extends V> collection)
    {
      addAll(0, collection);
      return true;
    }
    
    public void add(int index, V element)
    {
      Preconditions.checkPositionIndex(index, 0);
      throw new IllegalArgumentException("Key does not satisfy predicate: " + key);
    }
    
    @CanIgnoreReturnValue
    public boolean addAll(int index, Collection<? extends V> elements)
    {
      Preconditions.checkNotNull(elements);
      Preconditions.checkPositionIndex(index, 0);
      throw new IllegalArgumentException("Key does not satisfy predicate: " + key);
    }
    
    protected List<V> delegate()
    {
      return Collections.emptyList();
    }
  }
  
  Iterator<Map.Entry<K, V>> entryIterator()
  {
    throw new AssertionError("should never be called");
  }
  
  Collection<Map.Entry<K, V>> createEntries()
  {
    return new Entries();
  }
  
  class Entries extends ForwardingCollection<Map.Entry<K, V>> {
    Entries() {}
    
    protected Collection<Map.Entry<K, V>> delegate() {
      return Collections2.filter(unfiltered.entries(), entryPredicate());
    }
    

    public boolean remove(@Nullable Object o)
    {
      if ((o instanceof Map.Entry)) {
        Map.Entry<?, ?> entry = (Map.Entry)o;
        if (unfiltered.containsKey(entry.getKey()))
        {
          if (keyPredicate.apply(entry.getKey()))
            return unfiltered.remove(entry.getKey(), entry.getValue());
        }
      }
      return false;
    }
  }
  
  Collection<V> createValues()
  {
    return new FilteredMultimapValues(this);
  }
  
  Map<K, Collection<V>> createAsMap()
  {
    return Maps.filterKeys(unfiltered.asMap(), keyPredicate);
  }
  
  Multiset<K> createKeys()
  {
    return Multisets.filter(unfiltered.keys(), keyPredicate);
  }
}
