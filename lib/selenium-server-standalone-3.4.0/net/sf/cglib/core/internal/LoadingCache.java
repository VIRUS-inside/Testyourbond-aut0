package net.sf.cglib.core.internal;

import java.util.concurrent.FutureTask;

public class LoadingCache<K, KK, V>
{
  protected final java.util.concurrent.ConcurrentMap<KK, Object> map;
  protected final Function<K, V> loader;
  protected final Function<K, KK> keyMapper;
  public static final Function IDENTITY = new Function() {
    public Object apply(Object key) {
      return key;
    }
  };
  
  public LoadingCache(Function<K, KK> keyMapper, Function<K, V> loader) {
    this.keyMapper = keyMapper;
    this.loader = loader;
    map = new java.util.concurrent.ConcurrentHashMap();
  }
  
  public static <K> Function<K, K> identity()
  {
    return IDENTITY;
  }
  
  public V get(K key) {
    KK cacheKey = keyMapper.apply(key);
    Object v = map.get(cacheKey);
    if ((v != null) && (!(v instanceof FutureTask))) {
      return v;
    }
    
    return createEntry(key, cacheKey, v);
  }
  








  protected V createEntry(final K key, KK cacheKey, Object v)
  {
    boolean creator = false;
    FutureTask<V> task; FutureTask<V> task; if (v != null)
    {
      task = (FutureTask)v;
    } else {
      task = new FutureTask(new java.util.concurrent.Callable() {
        public V call() throws Exception {
          return loader.apply(key);
        }
      });
      Object prevTask = map.putIfAbsent(cacheKey, task);
      if (prevTask == null)
      {
        creator = true;
        task.run();
      } else if ((prevTask instanceof FutureTask)) {
        task = (FutureTask)prevTask;
      } else {
        return prevTask;
      }
    }
    
    try
    {
      result = task.get();
    } catch (InterruptedException e) { V result;
      throw new IllegalStateException("Interrupted while loading cache item", e);
    } catch (java.util.concurrent.ExecutionException e) {
      Throwable cause = e.getCause();
      if ((cause instanceof RuntimeException)) {
        throw ((RuntimeException)cause);
      }
      throw new IllegalStateException("Unable to load cache item", cause); }
    V result;
    if (creator) {
      map.put(cacheKey, result);
    }
    return result;
  }
}
