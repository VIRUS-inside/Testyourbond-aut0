package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

































class MapIteratorCache<K, V>
{
  private final Map<K, V> backingMap;
  @Nullable
  private transient Map.Entry<K, V> entrySetCache;
  
  MapIteratorCache(Map<K, V> backingMap)
  {
    this.backingMap = ((Map)Preconditions.checkNotNull(backingMap));
  }
  
  @CanIgnoreReturnValue
  public V put(@Nullable K key, @Nullable V value) {
    clearCache();
    return backingMap.put(key, value);
  }
  
  @CanIgnoreReturnValue
  public V remove(@Nullable Object key) {
    clearCache();
    return backingMap.remove(key);
  }
  
  public void clear() {
    clearCache();
    backingMap.clear();
  }
  
  public V get(@Nullable Object key) {
    V value = getIfCached(key);
    return value != null ? value : getWithoutCaching(key);
  }
  
  public final V getWithoutCaching(@Nullable Object key) {
    return backingMap.get(key);
  }
  
  public final boolean containsKey(@Nullable Object key) {
    return (getIfCached(key) != null) || (backingMap.containsKey(key));
  }
  
  public final Set<K> unmodifiableKeySet() {
    new AbstractSet()
    {
      public UnmodifiableIterator<K> iterator() {
        final Iterator<Map.Entry<K, V>> entryIterator = backingMap.entrySet().iterator();
        
        new UnmodifiableIterator()
        {
          public boolean hasNext() {
            return entryIterator.hasNext();
          }
          
          public K next()
          {
            Map.Entry<K, V> entry = (Map.Entry)entryIterator.next();
            entrySetCache = entry;
            return entry.getKey();
          }
        };
      }
      
      public int size()
      {
        return backingMap.size();
      }
      
      public boolean contains(@Nullable Object key)
      {
        return containsKey(key);
      }
    };
  }
  

  protected V getIfCached(@Nullable Object key)
  {
    Map.Entry<K, V> entry = entrySetCache;
    

    if ((entry != null) && (entry.getKey() == key)) {
      return entry.getValue();
    }
    return null;
  }
  
  protected void clearCache() {
    entrySetCache = null;
  }
}
