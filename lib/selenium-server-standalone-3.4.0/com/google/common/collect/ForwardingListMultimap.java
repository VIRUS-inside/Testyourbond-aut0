package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.List;
import javax.annotation.Nullable;



























@GwtCompatible
public abstract class ForwardingListMultimap<K, V>
  extends ForwardingMultimap<K, V>
  implements ListMultimap<K, V>
{
  protected ForwardingListMultimap() {}
  
  protected abstract ListMultimap<K, V> delegate();
  
  public List<V> get(@Nullable K key)
  {
    return delegate().get(key);
  }
  
  @CanIgnoreReturnValue
  public List<V> removeAll(@Nullable Object key)
  {
    return delegate().removeAll(key);
  }
  
  @CanIgnoreReturnValue
  public List<V> replaceValues(K key, Iterable<? extends V> values)
  {
    return delegate().replaceValues(key, values);
  }
}
