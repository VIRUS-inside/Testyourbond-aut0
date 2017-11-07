package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;
























@GwtCompatible
public abstract class ForwardingSetMultimap<K, V>
  extends ForwardingMultimap<K, V>
  implements SetMultimap<K, V>
{
  public ForwardingSetMultimap() {}
  
  protected abstract SetMultimap<K, V> delegate();
  
  public Set<Map.Entry<K, V>> entries()
  {
    return delegate().entries();
  }
  
  public Set<V> get(@Nullable K key)
  {
    return delegate().get(key);
  }
  
  @CanIgnoreReturnValue
  public Set<V> removeAll(@Nullable Object key)
  {
    return delegate().removeAll(key);
  }
  
  @CanIgnoreReturnValue
  public Set<V> replaceValues(K key, Iterable<? extends V> values)
  {
    return delegate().replaceValues(key, values);
  }
}
