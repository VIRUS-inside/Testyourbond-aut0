package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;






























@GwtCompatible
public abstract class AbstractCache<K, V>
  implements Cache<K, V>
{
  protected AbstractCache() {}
  
  public V get(K key, Callable<? extends V> valueLoader)
    throws ExecutionException
  {
    throw new UnsupportedOperationException();
  }
  









  public ImmutableMap<K, V> getAllPresent(Iterable<?> keys)
  {
    Map<K, V> result = Maps.newLinkedHashMap();
    for (Object key : keys) {
      if (!result.containsKey(key))
      {
        K castKey = key;
        V value = getIfPresent(key);
        if (value != null) {
          result.put(castKey, value);
        }
      }
    }
    return ImmutableMap.copyOf(result);
  }
  



  public void put(K key, V value)
  {
    throw new UnsupportedOperationException();
  }
  



  public void putAll(Map<? extends K, ? extends V> m)
  {
    for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }
  

  public void cleanUp() {}
  
  public long size()
  {
    throw new UnsupportedOperationException();
  }
  
  public void invalidate(Object key)
  {
    throw new UnsupportedOperationException();
  }
  



  public void invalidateAll(Iterable<?> keys)
  {
    for (Object key : keys) {
      invalidate(key);
    }
  }
  
  public void invalidateAll()
  {
    throw new UnsupportedOperationException();
  }
  
  public CacheStats stats()
  {
    throw new UnsupportedOperationException();
  }
  
  public ConcurrentMap<K, V> asMap()
  {
    throw new UnsupportedOperationException();
  }
  

































































  public static final class SimpleStatsCounter
    implements AbstractCache.StatsCounter
  {
    private final LongAddable hitCount = LongAddables.create();
    private final LongAddable missCount = LongAddables.create();
    private final LongAddable loadSuccessCount = LongAddables.create();
    private final LongAddable loadExceptionCount = LongAddables.create();
    private final LongAddable totalLoadTime = LongAddables.create();
    private final LongAddable evictionCount = LongAddables.create();
    



    public SimpleStatsCounter() {}
    



    public void recordHits(int count)
    {
      hitCount.add(count);
    }
    



    public void recordMisses(int count)
    {
      missCount.add(count);
    }
    
    public void recordLoadSuccess(long loadTime)
    {
      loadSuccessCount.increment();
      totalLoadTime.add(loadTime);
    }
    
    public void recordLoadException(long loadTime)
    {
      loadExceptionCount.increment();
      totalLoadTime.add(loadTime);
    }
    
    public void recordEviction()
    {
      evictionCount.increment();
    }
    
    public CacheStats snapshot()
    {
      return new CacheStats(hitCount
        .sum(), missCount
        .sum(), loadSuccessCount
        .sum(), loadExceptionCount
        .sum(), totalLoadTime
        .sum(), evictionCount
        .sum());
    }
    


    public void incrementBy(AbstractCache.StatsCounter other)
    {
      CacheStats otherStats = other.snapshot();
      hitCount.add(otherStats.hitCount());
      missCount.add(otherStats.missCount());
      loadSuccessCount.add(otherStats.loadSuccessCount());
      loadExceptionCount.add(otherStats.loadExceptionCount());
      totalLoadTime.add(otherStats.totalLoadTime());
      evictionCount.add(otherStats.evictionCount());
    }
  }
  
  public static abstract interface StatsCounter
  {
    public abstract void recordHits(int paramInt);
    
    public abstract void recordMisses(int paramInt);
    
    public abstract void recordLoadSuccess(long paramLong);
    
    public abstract void recordLoadException(long paramLong);
    
    public abstract void recordEviction();
    
    public abstract CacheStats snapshot();
  }
}
