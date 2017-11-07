package com.google.common.graph;

import java.util.Map;
import javax.annotation.Nullable;


















class MapRetrievalCache<K, V>
  extends MapIteratorCache<K, V>
{
  @Nullable
  private transient CacheEntry<K, V> cacheEntry1;
  @Nullable
  private transient CacheEntry<K, V> cacheEntry2;
  
  MapRetrievalCache(Map<K, V> backingMap)
  {
    super(backingMap);
  }
  

  public V get(@Nullable Object key)
  {
    V value = getIfCached(key);
    if (value != null) {
      return value;
    }
    
    value = getWithoutCaching(key);
    if (value != null) {
      addToCache(key, value);
    }
    return value;
  }
  


  protected V getIfCached(@Nullable Object key)
  {
    V value = super.getIfCached(key);
    if (value != null) {
      return value;
    }
    





    CacheEntry<K, V> entry = cacheEntry1;
    if ((entry != null) && (key == key)) {
      return value;
    }
    entry = cacheEntry2;
    if ((entry != null) && (key == key))
    {

      addToCache(entry);
      return value;
    }
    return null;
  }
  
  protected void clearCache()
  {
    super.clearCache();
    cacheEntry1 = null;
    cacheEntry2 = null;
  }
  
  private void addToCache(K key, V value) {
    addToCache(new CacheEntry(key, value));
  }
  
  private void addToCache(CacheEntry<K, V> entry)
  {
    cacheEntry2 = cacheEntry1;
    cacheEntry1 = entry;
  }
  
  private static final class CacheEntry<K, V> {
    final K key;
    final V value;
    
    CacheEntry(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }
}
