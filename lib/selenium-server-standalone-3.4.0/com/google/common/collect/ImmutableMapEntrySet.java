package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.j2objc.annotations.Weak;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import javax.annotation.Nullable;














@GwtCompatible(emulated=true)
abstract class ImmutableMapEntrySet<K, V>
  extends ImmutableSet<Map.Entry<K, V>>
{
  ImmutableMapEntrySet() {}
  
  abstract ImmutableMap<K, V> map();
  
  static final class RegularEntrySet<K, V>
    extends ImmutableMapEntrySet<K, V>
  {
    @Weak
    private final transient ImmutableMap<K, V> map;
    private final transient Map.Entry<K, V>[] entries;
    
    RegularEntrySet(ImmutableMap<K, V> map, Map.Entry<K, V>[] entries)
    {
      this.map = map;
      this.entries = entries;
    }
    
    ImmutableMap<K, V> map()
    {
      return map;
    }
    
    public UnmodifiableIterator<Map.Entry<K, V>> iterator()
    {
      return Iterators.forArray(entries);
    }
    
    public Spliterator<Map.Entry<K, V>> spliterator()
    {
      return Spliterators.spliterator(entries, 1297);
    }
    
    public void forEach(Consumer<? super Map.Entry<K, V>> action)
    {
      Preconditions.checkNotNull(action);
      for (Map.Entry<K, V> entry : entries) {
        action.accept(entry);
      }
    }
    
    ImmutableList<Map.Entry<K, V>> createAsList()
    {
      return new RegularImmutableAsList(this, entries);
    }
  }
  




  public int size()
  {
    return map().size();
  }
  
  public boolean contains(@Nullable Object object)
  {
    if ((object instanceof Map.Entry)) {
      Map.Entry<?, ?> entry = (Map.Entry)object;
      V value = map().get(entry.getKey());
      return (value != null) && (value.equals(entry.getValue()));
    }
    return false;
  }
  
  boolean isPartialView()
  {
    return map().isPartialView();
  }
  
  @GwtIncompatible
  boolean isHashCodeFast()
  {
    return map().isHashCodeFast();
  }
  
  public int hashCode()
  {
    return map().hashCode();
  }
  
  @GwtIncompatible
  Object writeReplace()
  {
    return new EntrySetSerializedForm(map());
  }
  
  @GwtIncompatible
  private static class EntrySetSerializedForm<K, V> implements Serializable {
    final ImmutableMap<K, V> map;
    private static final long serialVersionUID = 0L;
    
    EntrySetSerializedForm(ImmutableMap<K, V> map) { this.map = map; }
    
    Object readResolve()
    {
      return map.entrySet();
    }
  }
}
