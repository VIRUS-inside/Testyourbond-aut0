package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

























@GwtCompatible
final class WellBehavedMap<K, V>
  extends ForwardingMap<K, V>
{
  private final Map<K, V> delegate;
  private Set<Map.Entry<K, V>> entrySet;
  
  private WellBehavedMap(Map<K, V> delegate)
  {
    this.delegate = delegate;
  }
  





  static <K, V> WellBehavedMap<K, V> wrap(Map<K, V> delegate)
  {
    return new WellBehavedMap(delegate);
  }
  
  protected Map<K, V> delegate()
  {
    return delegate;
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    Set<Map.Entry<K, V>> es = entrySet;
    if (es != null) {
      return es;
    }
    return this.entrySet = new EntrySet(null);
  }
  
  private final class EntrySet extends Maps.EntrySet<K, V> {
    private EntrySet() {}
    
    Map<K, V> map() {
      return WellBehavedMap.this;
    }
    
    public Iterator<Map.Entry<K, V>> iterator()
    {
      new TransformedIterator(keySet().iterator())
      {
        Map.Entry<K, V> transform(final K key) {
          new AbstractMapEntry()
          {
            public K getKey() {
              return key;
            }
            
            public V getValue()
            {
              return get(key);
            }
            
            public V setValue(V value)
            {
              return put(key, value);
            }
          };
        }
      };
    }
  }
}
