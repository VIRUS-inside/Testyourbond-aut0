package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import javax.annotation.Nullable;




















@GwtIncompatible
abstract class AbstractNavigableMap<K, V>
  extends Maps.IteratorBasedAbstractMap<K, V>
  implements NavigableMap<K, V>
{
  AbstractNavigableMap() {}
  
  @Nullable
  public abstract V get(@Nullable Object paramObject);
  
  @Nullable
  public Map.Entry<K, V> firstEntry()
  {
    return (Map.Entry)Iterators.getNext(entryIterator(), null);
  }
  
  @Nullable
  public Map.Entry<K, V> lastEntry()
  {
    return (Map.Entry)Iterators.getNext(descendingEntryIterator(), null);
  }
  
  @Nullable
  public Map.Entry<K, V> pollFirstEntry()
  {
    return (Map.Entry)Iterators.pollNext(entryIterator());
  }
  
  @Nullable
  public Map.Entry<K, V> pollLastEntry()
  {
    return (Map.Entry)Iterators.pollNext(descendingEntryIterator());
  }
  
  public K firstKey()
  {
    Map.Entry<K, V> entry = firstEntry();
    if (entry == null) {
      throw new NoSuchElementException();
    }
    return entry.getKey();
  }
  

  public K lastKey()
  {
    Map.Entry<K, V> entry = lastEntry();
    if (entry == null) {
      throw new NoSuchElementException();
    }
    return entry.getKey();
  }
  

  @Nullable
  public Map.Entry<K, V> lowerEntry(K key)
  {
    return headMap(key, false).lastEntry();
  }
  
  @Nullable
  public Map.Entry<K, V> floorEntry(K key)
  {
    return headMap(key, true).lastEntry();
  }
  
  @Nullable
  public Map.Entry<K, V> ceilingEntry(K key)
  {
    return tailMap(key, true).firstEntry();
  }
  
  @Nullable
  public Map.Entry<K, V> higherEntry(K key)
  {
    return tailMap(key, false).firstEntry();
  }
  
  public K lowerKey(K key)
  {
    return Maps.keyOrNull(lowerEntry(key));
  }
  
  public K floorKey(K key)
  {
    return Maps.keyOrNull(floorEntry(key));
  }
  
  public K ceilingKey(K key)
  {
    return Maps.keyOrNull(ceilingEntry(key));
  }
  
  public K higherKey(K key)
  {
    return Maps.keyOrNull(higherEntry(key));
  }
  
  abstract Iterator<Map.Entry<K, V>> descendingEntryIterator();
  
  public SortedMap<K, V> subMap(K fromKey, K toKey)
  {
    return subMap(fromKey, true, toKey, false);
  }
  
  public SortedMap<K, V> headMap(K toKey)
  {
    return headMap(toKey, false);
  }
  
  public SortedMap<K, V> tailMap(K fromKey)
  {
    return tailMap(fromKey, true);
  }
  
  public NavigableSet<K> navigableKeySet()
  {
    return new Maps.NavigableKeySet(this);
  }
  
  public Set<K> keySet()
  {
    return navigableKeySet();
  }
  
  public NavigableSet<K> descendingKeySet()
  {
    return descendingMap().navigableKeySet();
  }
  
  public NavigableMap<K, V> descendingMap()
  {
    return new DescendingMap(null);
  }
  
  private final class DescendingMap extends Maps.DescendingMap<K, V> {
    private DescendingMap() {}
    
    NavigableMap<K, V> forward() { return AbstractNavigableMap.this; }
    

    Iterator<Map.Entry<K, V>> entryIterator()
    {
      return descendingEntryIterator();
    }
  }
}
