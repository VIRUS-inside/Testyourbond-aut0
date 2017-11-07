package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.j2objc.annotations.RetainedWith;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;




















@GwtCompatible(serializable=true, emulated=true)
final class SingletonImmutableBiMap<K, V>
  extends ImmutableBiMap<K, V>
{
  final transient K singleKey;
  final transient V singleValue;
  @LazyInit
  @RetainedWith
  transient ImmutableBiMap<V, K> inverse;
  
  SingletonImmutableBiMap(K singleKey, V singleValue)
  {
    CollectPreconditions.checkEntryNotNull(singleKey, singleValue);
    this.singleKey = singleKey;
    this.singleValue = singleValue;
  }
  
  private SingletonImmutableBiMap(K singleKey, V singleValue, ImmutableBiMap<V, K> inverse) {
    this.singleKey = singleKey;
    this.singleValue = singleValue;
    this.inverse = inverse;
  }
  
  public V get(@Nullable Object key)
  {
    return singleKey.equals(key) ? singleValue : null;
  }
  
  public int size()
  {
    return 1;
  }
  
  public void forEach(BiConsumer<? super K, ? super V> action)
  {
    ((BiConsumer)Preconditions.checkNotNull(action)).accept(singleKey, singleValue);
  }
  
  public boolean containsKey(@Nullable Object key)
  {
    return singleKey.equals(key);
  }
  
  public boolean containsValue(@Nullable Object value)
  {
    return singleValue.equals(value);
  }
  
  boolean isPartialView()
  {
    return false;
  }
  
  ImmutableSet<Map.Entry<K, V>> createEntrySet()
  {
    return ImmutableSet.of(Maps.immutableEntry(singleKey, singleValue));
  }
  
  ImmutableSet<K> createKeySet()
  {
    return ImmutableSet.of(singleKey);
  }
  





  public ImmutableBiMap<V, K> inverse()
  {
    ImmutableBiMap<V, K> result = inverse;
    if (result == null) {
      return this.inverse = new SingletonImmutableBiMap(singleValue, singleKey, this);
    }
    return result;
  }
}
