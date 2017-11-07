package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import javax.annotation.Nullable;














@GwtCompatible
abstract class AbstractMultimap<K, V>
  implements Multimap<K, V>
{
  private transient Collection<Map.Entry<K, V>> entries;
  private transient Set<K> keySet;
  private transient Multiset<K> keys;
  private transient Collection<V> values;
  private transient Map<K, Collection<V>> asMap;
  
  AbstractMultimap() {}
  
  public boolean isEmpty()
  {
    return size() == 0;
  }
  
  public boolean containsValue(@Nullable Object value)
  {
    for (Collection<V> collection : asMap().values()) {
      if (collection.contains(value)) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean containsEntry(@Nullable Object key, @Nullable Object value)
  {
    Collection<V> collection = (Collection)asMap().get(key);
    return (collection != null) && (collection.contains(value));
  }
  
  @CanIgnoreReturnValue
  public boolean remove(@Nullable Object key, @Nullable Object value)
  {
    Collection<V> collection = (Collection)asMap().get(key);
    return (collection != null) && (collection.remove(value));
  }
  
  @CanIgnoreReturnValue
  public boolean put(@Nullable K key, @Nullable V value)
  {
    return get(key).add(value);
  }
  
  @CanIgnoreReturnValue
  public boolean putAll(@Nullable K key, Iterable<? extends V> values)
  {
    Preconditions.checkNotNull(values);
    

    if ((values instanceof Collection)) {
      Collection<? extends V> valueCollection = (Collection)values;
      return (!valueCollection.isEmpty()) && (get(key).addAll(valueCollection));
    }
    Iterator<? extends V> valueItr = values.iterator();
    return (valueItr.hasNext()) && (Iterators.addAll(get(key), valueItr));
  }
  

  @CanIgnoreReturnValue
  public boolean putAll(Multimap<? extends K, ? extends V> multimap)
  {
    boolean changed = false;
    for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
      changed |= put(entry.getKey(), entry.getValue());
    }
    return changed;
  }
  
  @CanIgnoreReturnValue
  public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values)
  {
    Preconditions.checkNotNull(values);
    Collection<V> result = removeAll(key);
    putAll(key, values);
    return result;
  }
  


  public Collection<Map.Entry<K, V>> entries()
  {
    Collection<Map.Entry<K, V>> result = entries;
    return result == null ? (this.entries = createEntries()) : result;
  }
  
  Collection<Map.Entry<K, V>> createEntries() {
    if ((this instanceof SetMultimap)) {
      return new EntrySet(null);
    }
    return new Entries(null);
  }
  
  abstract Iterator<Map.Entry<K, V>> entryIterator();
  
  private class Entries extends Multimaps.Entries<K, V> {
    private Entries() {}
    
    Multimap<K, V> multimap() { return AbstractMultimap.this; }
    

    public Iterator<Map.Entry<K, V>> iterator()
    {
      return entryIterator();
    }
    
    public Spliterator<Map.Entry<K, V>> spliterator()
    {
      return entrySpliterator();
    }
  }
  
  private class EntrySet extends AbstractMultimap<K, V>.Entries implements Set<Map.Entry<K, V>> {
    private EntrySet() { super(null); }
    
    public int hashCode() {
      return Sets.hashCodeImpl(this);
    }
    
    public boolean equals(@Nullable Object obj)
    {
      return Sets.equalsImpl(this, obj);
    }
  }
  

  Spliterator<Map.Entry<K, V>> entrySpliterator()
  {
    return Spliterators.spliterator(
      entryIterator(), size(), (this instanceof SetMultimap) ? 1 : 0);
  }
  


  public Set<K> keySet()
  {
    Set<K> result = keySet;
    return result == null ? (this.keySet = createKeySet()) : result;
  }
  
  Set<K> createKeySet() {
    return new Maps.KeySet(asMap());
  }
  


  public Multiset<K> keys()
  {
    Multiset<K> result = keys;
    return result == null ? (this.keys = createKeys()) : result;
  }
  
  Multiset<K> createKeys() {
    return new Multimaps.Keys(this);
  }
  


  public Collection<V> values()
  {
    Collection<V> result = values;
    return result == null ? (this.values = createValues()) : result;
  }
  
  Collection<V> createValues() {
    return new Values();
  }
  
  class Values extends AbstractCollection<V> {
    Values() {}
    
    public Iterator<V> iterator() {
      return valueIterator();
    }
    
    public Spliterator<V> spliterator()
    {
      return valueSpliterator();
    }
    
    public int size()
    {
      return AbstractMultimap.this.size();
    }
    
    public boolean contains(@Nullable Object o)
    {
      return containsValue(o);
    }
    
    public void clear()
    {
      AbstractMultimap.this.clear();
    }
  }
  
  Iterator<V> valueIterator() {
    return Maps.valueIterator(entries().iterator());
  }
  
  Spliterator<V> valueSpliterator() {
    return Spliterators.spliterator(valueIterator(), size(), 0);
  }
  


  public Map<K, Collection<V>> asMap()
  {
    Map<K, Collection<V>> result = asMap;
    return result == null ? (this.asMap = createAsMap()) : result;
  }
  

  abstract Map<K, Collection<V>> createAsMap();
  

  public boolean equals(@Nullable Object object)
  {
    return Multimaps.equalsImpl(this, object);
  }
  








  public int hashCode()
  {
    return asMap().hashCode();
  }
  






  public String toString()
  {
    return asMap().toString();
  }
}
