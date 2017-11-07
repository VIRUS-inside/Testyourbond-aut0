package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;





































@GwtCompatible(serializable=true, emulated=true)
public abstract class ImmutableBiMap<K, V>
  extends ImmutableBiMapFauxverideShim<K, V>
  implements BiMap<K, V>
{
  @Beta
  public static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction)
  {
    return CollectCollectors.toImmutableBiMap(keyFunction, valueFunction);
  }
  




  public static <K, V> ImmutableBiMap<K, V> of()
  {
    return RegularImmutableBiMap.EMPTY;
  }
  


  public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1)
  {
    return new SingletonImmutableBiMap(k1, v1);
  }
  




  public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2)
  {
    return RegularImmutableBiMap.fromEntries(new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
  }
  




  public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3)
  {
    return RegularImmutableBiMap.fromEntries(new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
  }
  




  public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4)
  {
    return RegularImmutableBiMap.fromEntries(new Map.Entry[] {
      entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
  }
  





  public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5)
  {
    return RegularImmutableBiMap.fromEntries(new Map.Entry[] {
      entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5) });
  }
  





  public static <K, V> Builder<K, V> builder()
  {
    return new Builder();
  }
  











  public static final class Builder<K, V>
    extends ImmutableMap.Builder<K, V>
  {
    public Builder() {}
    











    Builder(int size)
    {
      super();
    }
    




    @CanIgnoreReturnValue
    public Builder<K, V> put(K key, V value)
    {
      super.put(key, value);
      return this;
    }
    






    @CanIgnoreReturnValue
    public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry)
    {
      super.put(entry);
      return this;
    }
    







    @CanIgnoreReturnValue
    public Builder<K, V> putAll(Map<? extends K, ? extends V> map)
    {
      super.putAll(map);
      return this;
    }
    







    @CanIgnoreReturnValue
    @Beta
    public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries)
    {
      super.putAll(entries);
      return this;
    }
    











    @CanIgnoreReturnValue
    @Beta
    public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator)
    {
      super.orderEntriesByValue(valueComparator);
      return this;
    }
    
    @CanIgnoreReturnValue
    Builder<K, V> combine(ImmutableMap.Builder<K, V> builder) {
      super.combine(builder);
      return this;
    }
    





    public ImmutableBiMap<K, V> build()
    {
      switch (size) {
      case 0: 
        return ImmutableBiMap.of();
      case 1: 
        return ImmutableBiMap.of(entries[0].getKey(), entries[0].getValue());
      }
      
      





      if (valueComparator != null) {
        if (entriesUsed) {
          entries = ((ImmutableMapEntry[])Arrays.copyOf(entries, size));
        }
        Arrays.sort(entries, 0, size, 
        


          Ordering.from(valueComparator).onResultOf(Maps.valueFunction()));
      }
      entriesUsed = (size == entries.length);
      return RegularImmutableBiMap.fromEntryArray(size, entries);
    }
  }
  













  public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map)
  {
    if ((map instanceof ImmutableBiMap))
    {
      ImmutableBiMap<K, V> bimap = (ImmutableBiMap)map;
      

      if (!bimap.isPartialView()) {
        return bimap;
      }
    }
    return copyOf(map.entrySet());
  }
  









  @Beta
  public static <K, V> ImmutableBiMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries)
  {
    Map.Entry<K, V>[] entryArray = (Map.Entry[])Iterables.toArray(entries, EMPTY_ENTRY_ARRAY);
    switch (entryArray.length) {
    case 0: 
      return of();
    case 1: 
      Map.Entry<K, V> entry = entryArray[0];
      return of(entry.getKey(), entry.getValue());
    }
    
    


    return RegularImmutableBiMap.fromEntries(entryArray);
  }
  




  ImmutableBiMap() {}
  




  public abstract ImmutableBiMap<V, K> inverse();
  




  public ImmutableSet<V> values()
  {
    return inverse().keySet();
  }
  






  @Deprecated
  @CanIgnoreReturnValue
  public V forcePut(K key, V value)
  {
    throw new UnsupportedOperationException();
  }
  


  private static class SerializedForm
    extends ImmutableMap.SerializedForm
  {
    private static final long serialVersionUID = 0L;
    


    SerializedForm(ImmutableBiMap<?, ?> bimap)
    {
      super();
    }
    
    Object readResolve()
    {
      ImmutableBiMap.Builder<Object, Object> builder = new ImmutableBiMap.Builder();
      return createMap(builder);
    }
  }
  


  Object writeReplace()
  {
    return new SerializedForm(this);
  }
}
