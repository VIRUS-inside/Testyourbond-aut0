package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.j2objc.annotations.RetainedWith;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import javax.annotation.Nullable;

























@GwtCompatible(emulated=true)
final class Synchronized
{
  private Synchronized() {}
  
  static class SynchronizedObject
    implements Serializable
  {
    final Object delegate;
    final Object mutex;
    @GwtIncompatible
    private static final long serialVersionUID = 0L;
    
    SynchronizedObject(Object delegate, @Nullable Object mutex)
    {
      this.delegate = Preconditions.checkNotNull(delegate);
      this.mutex = (mutex == null ? this : mutex);
    }
    
    Object delegate() {
      return delegate;
    }
    


    public String toString()
    {
      synchronized (mutex) {
        return delegate.toString();
      }
    }
    



    @GwtIncompatible
    private void writeObject(ObjectOutputStream stream)
      throws IOException
    {
      synchronized (mutex) {
        stream.defaultWriteObject();
      }
    }
  }
  




  private static <E> Collection<E> collection(Collection<E> collection, @Nullable Object mutex) { return new SynchronizedCollection(collection, mutex, null); }
  
  @VisibleForTesting
  static class SynchronizedCollection<E> extends Synchronized.SynchronizedObject implements Collection<E> {
    private static final long serialVersionUID = 0L;
    
    private SynchronizedCollection(Collection<E> delegate, @Nullable Object mutex) { super(mutex); }
    


    Collection<E> delegate()
    {
      return (Collection)super.delegate();
    }
    
    public boolean add(E e)
    {
      synchronized (mutex) {
        return delegate().add(e);
      }
    }
    
    public boolean addAll(Collection<? extends E> c)
    {
      synchronized (mutex) {
        return delegate().addAll(c);
      }
    }
    
    public void clear()
    {
      synchronized (mutex) {
        delegate().clear();
      }
    }
    
    public boolean contains(Object o)
    {
      synchronized (mutex) {
        return delegate().contains(o);
      }
    }
    
    public boolean containsAll(Collection<?> c)
    {
      synchronized (mutex) {
        return delegate().containsAll(c);
      }
    }
    
    public boolean isEmpty()
    {
      synchronized (mutex) {
        return delegate().isEmpty();
      }
    }
    
    public Iterator<E> iterator()
    {
      return delegate().iterator();
    }
    
    public Spliterator<E> spliterator()
    {
      synchronized (mutex) {
        return delegate().spliterator();
      }
    }
    
    public Stream<E> stream()
    {
      synchronized (mutex) {
        return delegate().stream();
      }
    }
    
    public Stream<E> parallelStream()
    {
      synchronized (mutex) {
        return delegate().parallelStream();
      }
    }
    
    public void forEach(Consumer<? super E> action)
    {
      synchronized (mutex) {
        delegate().forEach(action);
      }
    }
    
    public boolean remove(Object o)
    {
      synchronized (mutex) {
        return delegate().remove(o);
      }
    }
    
    public boolean removeAll(Collection<?> c)
    {
      synchronized (mutex) {
        return delegate().removeAll(c);
      }
    }
    
    public boolean retainAll(Collection<?> c)
    {
      synchronized (mutex) {
        return delegate().retainAll(c);
      }
    }
    
    public boolean removeIf(Predicate<? super E> filter)
    {
      synchronized (mutex) {
        return delegate().removeIf(filter);
      }
    }
    
    public int size()
    {
      synchronized (mutex) {
        return delegate().size();
      }
    }
    
    public Object[] toArray()
    {
      synchronized (mutex) {
        return delegate().toArray();
      }
    }
    
    public <T> T[] toArray(T[] a)
    {
      synchronized (mutex) {
        return delegate().toArray(a);
      }
    }
  }
  

  @VisibleForTesting
  static <E> Set<E> set(Set<E> set, @Nullable Object mutex)
  {
    return new SynchronizedSet(set, mutex);
  }
  
  static class SynchronizedSet<E> extends Synchronized.SynchronizedCollection<E> implements Set<E> {
    private static final long serialVersionUID = 0L;
    
    SynchronizedSet(Set<E> delegate, @Nullable Object mutex) { super(mutex, null); }
    

    Set<E> delegate()
    {
      return (Set)super.delegate();
    }
    
    public boolean equals(Object o)
    {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(o);
      }
    }
    
    public int hashCode()
    {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }
  }
  



  private static <E> SortedSet<E> sortedSet(SortedSet<E> set, @Nullable Object mutex) { return new SynchronizedSortedSet(set, mutex); }
  
  static class SynchronizedSortedSet<E> extends Synchronized.SynchronizedSet<E> implements SortedSet<E> {
    private static final long serialVersionUID = 0L;
    
    SynchronizedSortedSet(SortedSet<E> delegate, @Nullable Object mutex) { super(mutex); }
    

    SortedSet<E> delegate()
    {
      return (SortedSet)super.delegate();
    }
    
    public Comparator<? super E> comparator()
    {
      synchronized (mutex) {
        return delegate().comparator();
      }
    }
    
    public SortedSet<E> subSet(E fromElement, E toElement)
    {
      synchronized (mutex) {
        return Synchronized.sortedSet(delegate().subSet(fromElement, toElement), mutex);
      }
    }
    
    public SortedSet<E> headSet(E toElement)
    {
      synchronized (mutex) {
        return Synchronized.sortedSet(delegate().headSet(toElement), mutex);
      }
    }
    
    public SortedSet<E> tailSet(E fromElement)
    {
      synchronized (mutex) {
        return Synchronized.sortedSet(delegate().tailSet(fromElement), mutex);
      }
    }
    
    public E first()
    {
      synchronized (mutex) {
        return delegate().first();
      }
    }
    
    public E last()
    {
      synchronized (mutex) {
        return delegate().last();
      }
    }
  }
  

  private static <E> List<E> list(List<E> list, @Nullable Object mutex)
  {
    return (list instanceof RandomAccess) ? new SynchronizedRandomAccessList(list, mutex) : new SynchronizedList(list, mutex);
  }
  
  private static class SynchronizedList<E> extends Synchronized.SynchronizedCollection<E> implements List<E> {
    private static final long serialVersionUID = 0L;
    
    SynchronizedList(List<E> delegate, @Nullable Object mutex) {
      super(mutex, null);
    }
    
    List<E> delegate()
    {
      return (List)super.delegate();
    }
    
    public void add(int index, E element)
    {
      synchronized (mutex) {
        delegate().add(index, element);
      }
    }
    
    public boolean addAll(int index, Collection<? extends E> c)
    {
      synchronized (mutex) {
        return delegate().addAll(index, c);
      }
    }
    
    public E get(int index)
    {
      synchronized (mutex) {
        return delegate().get(index);
      }
    }
    
    public int indexOf(Object o)
    {
      synchronized (mutex) {
        return delegate().indexOf(o);
      }
    }
    
    public int lastIndexOf(Object o)
    {
      synchronized (mutex) {
        return delegate().lastIndexOf(o);
      }
    }
    
    public ListIterator<E> listIterator()
    {
      return delegate().listIterator();
    }
    
    public ListIterator<E> listIterator(int index)
    {
      return delegate().listIterator(index);
    }
    
    public E remove(int index)
    {
      synchronized (mutex) {
        return delegate().remove(index);
      }
    }
    
    public E set(int index, E element)
    {
      synchronized (mutex) {
        return delegate().set(index, element);
      }
    }
    
    public void replaceAll(UnaryOperator<E> operator)
    {
      synchronized (mutex) {
        delegate().replaceAll(operator);
      }
    }
    
    public void sort(Comparator<? super E> c)
    {
      synchronized (mutex) {
        delegate().sort(c);
      }
    }
    
    public List<E> subList(int fromIndex, int toIndex)
    {
      synchronized (mutex) {
        return Synchronized.list(delegate().subList(fromIndex, toIndex), mutex);
      }
    }
    
    public boolean equals(Object o)
    {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(o);
      }
    }
    
    public int hashCode()
    {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }
  }
  
  private static class SynchronizedRandomAccessList<E> extends Synchronized.SynchronizedList<E> implements RandomAccess
  {
    private static final long serialVersionUID = 0L;
    
    SynchronizedRandomAccessList(List<E> list, @Nullable Object mutex) {
      super(mutex);
    }
  }
  

  static <E> Multiset<E> multiset(Multiset<E> multiset, @Nullable Object mutex)
  {
    if (((multiset instanceof SynchronizedMultiset)) || ((multiset instanceof ImmutableMultiset))) {
      return multiset;
    }
    return new SynchronizedMultiset(multiset, mutex);
  }
  
  private static class SynchronizedMultiset<E> extends Synchronized.SynchronizedCollection<E> implements Multiset<E> {
    transient Set<E> elementSet;
    transient Set<Multiset.Entry<E>> entrySet;
    private static final long serialVersionUID = 0L;
    
    SynchronizedMultiset(Multiset<E> delegate, @Nullable Object mutex) {
      super(mutex, null);
    }
    
    Multiset<E> delegate()
    {
      return (Multiset)super.delegate();
    }
    
    public int count(Object o)
    {
      synchronized (mutex) {
        return delegate().count(o);
      }
    }
    
    public int add(E e, int n)
    {
      synchronized (mutex) {
        return delegate().add(e, n);
      }
    }
    
    public int remove(Object o, int n)
    {
      synchronized (mutex) {
        return delegate().remove(o, n);
      }
    }
    
    public int setCount(E element, int count)
    {
      synchronized (mutex) {
        return delegate().setCount(element, count);
      }
    }
    
    public boolean setCount(E element, int oldCount, int newCount)
    {
      synchronized (mutex) {
        return delegate().setCount(element, oldCount, newCount);
      }
    }
    
    public Set<E> elementSet()
    {
      synchronized (mutex) {
        if (elementSet == null) {
          elementSet = Synchronized.typePreservingSet(delegate().elementSet(), mutex);
        }
        return elementSet;
      }
    }
    
    public Set<Multiset.Entry<E>> entrySet()
    {
      synchronized (mutex) {
        if (entrySet == null) {
          entrySet = Synchronized.typePreservingSet(delegate().entrySet(), mutex);
        }
        return entrySet;
      }
    }
    
    public boolean equals(Object o)
    {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(o);
      }
    }
    
    public int hashCode()
    {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }
  }
  

  static <K, V> Multimap<K, V> multimap(Multimap<K, V> multimap, @Nullable Object mutex)
  {
    if (((multimap instanceof SynchronizedMultimap)) || ((multimap instanceof ImmutableMultimap))) {
      return multimap;
    }
    return new SynchronizedMultimap(multimap, mutex);
  }
  
  private static class SynchronizedMultimap<K, V> extends Synchronized.SynchronizedObject implements Multimap<K, V>
  {
    transient Set<K> keySet;
    transient Collection<V> valuesCollection;
    transient Collection<Map.Entry<K, V>> entries;
    transient Map<K, Collection<V>> asMap;
    transient Multiset<K> keys;
    private static final long serialVersionUID = 0L;
    
    Multimap<K, V> delegate()
    {
      return (Multimap)super.delegate();
    }
    
    SynchronizedMultimap(Multimap<K, V> delegate, @Nullable Object mutex) {
      super(mutex);
    }
    
    public int size()
    {
      synchronized (mutex) {
        return delegate().size();
      }
    }
    
    public boolean isEmpty()
    {
      synchronized (mutex) {
        return delegate().isEmpty();
      }
    }
    
    public boolean containsKey(Object key)
    {
      synchronized (mutex) {
        return delegate().containsKey(key);
      }
    }
    
    public boolean containsValue(Object value)
    {
      synchronized (mutex) {
        return delegate().containsValue(value);
      }
    }
    
    public boolean containsEntry(Object key, Object value)
    {
      synchronized (mutex) {
        return delegate().containsEntry(key, value);
      }
    }
    
    public Collection<V> get(K key)
    {
      synchronized (mutex) {
        return Synchronized.typePreservingCollection(delegate().get(key), mutex);
      }
    }
    
    public boolean put(K key, V value)
    {
      synchronized (mutex) {
        return delegate().put(key, value);
      }
    }
    
    public boolean putAll(K key, Iterable<? extends V> values)
    {
      synchronized (mutex) {
        return delegate().putAll(key, values);
      }
    }
    
    public boolean putAll(Multimap<? extends K, ? extends V> multimap)
    {
      synchronized (mutex) {
        return delegate().putAll(multimap);
      }
    }
    
    public Collection<V> replaceValues(K key, Iterable<? extends V> values)
    {
      synchronized (mutex) {
        return delegate().replaceValues(key, values);
      }
    }
    
    public boolean remove(Object key, Object value)
    {
      synchronized (mutex) {
        return delegate().remove(key, value);
      }
    }
    
    public Collection<V> removeAll(Object key)
    {
      synchronized (mutex) {
        return delegate().removeAll(key);
      }
    }
    
    public void clear()
    {
      synchronized (mutex) {
        delegate().clear();
      }
    }
    
    public Set<K> keySet()
    {
      synchronized (mutex) {
        if (keySet == null) {
          keySet = Synchronized.typePreservingSet(delegate().keySet(), mutex);
        }
        return keySet;
      }
    }
    
    public Collection<V> values()
    {
      synchronized (mutex) {
        if (valuesCollection == null) {
          valuesCollection = Synchronized.collection(delegate().values(), mutex);
        }
        return valuesCollection;
      }
    }
    
    public Collection<Map.Entry<K, V>> entries()
    {
      synchronized (mutex) {
        if (entries == null) {
          entries = Synchronized.typePreservingCollection(delegate().entries(), mutex);
        }
        return entries;
      }
    }
    
    public void forEach(BiConsumer<? super K, ? super V> action)
    {
      synchronized (mutex) {
        delegate().forEach(action);
      }
    }
    
    public Map<K, Collection<V>> asMap()
    {
      synchronized (mutex) {
        if (asMap == null) {
          asMap = new Synchronized.SynchronizedAsMap(delegate().asMap(), mutex);
        }
        return asMap;
      }
    }
    
    public Multiset<K> keys()
    {
      synchronized (mutex) {
        if (keys == null) {
          keys = Synchronized.multiset(delegate().keys(), mutex);
        }
        return keys;
      }
    }
    
    public boolean equals(Object o)
    {
      if (o == this) {
        return true;
      }
      synchronized (mutex) {
        return delegate().equals(o);
      }
    }
    
    public int hashCode()
    {
      synchronized (mutex) {
        return delegate().hashCode();
      }
    }
  }
  


  static <K, V> ListMultimap<K, V> listMultimap(ListMultimap<K, V> multimap, @Nullable Object mutex)
  {
    if (((multimap instanceof SynchronizedListMultimap)) || ((multimap instanceof ImmutableListMultimap))) {
      return multimap;
    }
    return new SynchronizedListMultimap(multimap, mutex);
  }
  
  private static class SynchronizedListMultimap<K, V> extends Synchronized.SynchronizedMultimap<K, V> implements ListMultimap<K, V> {
    private static final long serialVersionUID = 0L;
    
    SynchronizedListMultimap(ListMultimap<K, V> delegate, @Nullable Object mutex) { super(mutex); }
    

    ListMultimap<K, V> delegate()
    {
      return (ListMultimap)super.delegate();
    }
    
    public List<V> get(K key)
    {
      synchronized (mutex) {
        return Synchronized.list(delegate().get(key), mutex);
      }
    }
    
    public List<V> removeAll(Object key)
    {
      synchronized (mutex) {
        return delegate().removeAll(key);
      }
    }
    
    public List<V> replaceValues(K key, Iterable<? extends V> values)
    {
      synchronized (mutex) {
        return delegate().replaceValues(key, values);
      }
    }
  }
  

  static <K, V> SetMultimap<K, V> setMultimap(SetMultimap<K, V> multimap, @Nullable Object mutex)
  {
    if (((multimap instanceof SynchronizedSetMultimap)) || ((multimap instanceof ImmutableSetMultimap))) {
      return multimap;
    }
    return new SynchronizedSetMultimap(multimap, mutex);
  }
  
  private static class SynchronizedSetMultimap<K, V> extends Synchronized.SynchronizedMultimap<K, V> implements SetMultimap<K, V> {
    transient Set<Map.Entry<K, V>> entrySet;
    private static final long serialVersionUID = 0L;
    
    SynchronizedSetMultimap(SetMultimap<K, V> delegate, @Nullable Object mutex) {
      super(mutex);
    }
    
    SetMultimap<K, V> delegate()
    {
      return (SetMultimap)super.delegate();
    }
    
    public Set<V> get(K key)
    {
      synchronized (mutex) {
        return Synchronized.set(delegate().get(key), mutex);
      }
    }
    
    public Set<V> removeAll(Object key)
    {
      synchronized (mutex) {
        return delegate().removeAll(key);
      }
    }
    
    public Set<V> replaceValues(K key, Iterable<? extends V> values)
    {
      synchronized (mutex) {
        return delegate().replaceValues(key, values);
      }
    }
    
    public Set<Map.Entry<K, V>> entries()
    {
      synchronized (mutex) {
        if (entrySet == null) {
          entrySet = Synchronized.set(delegate().entries(), mutex);
        }
        return entrySet;
      }
    }
  }
  


  static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(SortedSetMultimap<K, V> multimap, @Nullable Object mutex)
  {
    if ((multimap instanceof SynchronizedSortedSetMultimap)) {
      return multimap;
    }
    return new SynchronizedSortedSetMultimap(multimap, mutex);
  }
  
  private static class SynchronizedSortedSetMultimap<K, V> extends Synchronized.SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
    private static final long serialVersionUID = 0L;
    
    SynchronizedSortedSetMultimap(SortedSetMultimap<K, V> delegate, @Nullable Object mutex) { super(mutex); }
    

    SortedSetMultimap<K, V> delegate()
    {
      return (SortedSetMultimap)super.delegate();
    }
    
    public SortedSet<V> get(K key)
    {
      synchronized (mutex) {
        return Synchronized.sortedSet(delegate().get(key), mutex);
      }
    }
    
    public SortedSet<V> removeAll(Object key)
    {
      synchronized (mutex) {
        return delegate().removeAll(key);
      }
    }
    
    public SortedSet<V> replaceValues(K key, Iterable<? extends V> values)
    {
      synchronized (mutex) {
        return delegate().replaceValues(key, values);
      }
    }
    
    public Comparator<? super V> valueComparator()
    {
      synchronized (mutex) {
        return delegate().valueComparator();
      }
    }
  }
  


  private static <E> Collection<E> typePreservingCollection(Collection<E> collection, @Nullable Object mutex)
  {
    if ((collection instanceof SortedSet)) {
      return sortedSet((SortedSet)collection, mutex);
    }
    if ((collection instanceof Set)) {
      return set((Set)collection, mutex);
    }
    if ((collection instanceof List)) {
      return list((List)collection, mutex);
    }
    return collection(collection, mutex);
  }
  
  private static <E> Set<E> typePreservingSet(Set<E> set, @Nullable Object mutex) {
    if ((set instanceof SortedSet)) {
      return sortedSet((SortedSet)set, mutex);
    }
    return set(set, mutex);
  }
  
  private static class SynchronizedAsMapEntries<K, V> extends Synchronized.SynchronizedSet<Map.Entry<K, Collection<V>>> {
    private static final long serialVersionUID = 0L;
    
    SynchronizedAsMapEntries(Set<Map.Entry<K, Collection<V>>> delegate, @Nullable Object mutex) {
      super(mutex);
    }
    

    public Iterator<Map.Entry<K, Collection<V>>> iterator()
    {
      new TransformedIterator(
        super.iterator())
        {
          Map.Entry<K, Collection<V>> transform(final Map.Entry<K, Collection<V>> entry)
          {
            new ForwardingMapEntry()
            {
              protected Map.Entry<K, Collection<V>> delegate() {
                return entry;
              }
              
              public Collection<V> getValue()
              {
                return Synchronized.typePreservingCollection((Collection)entry.getValue(), mutex);
              }
            };
          }
        };
      }
      


      public Object[] toArray()
      {
        synchronized (mutex) {
          return ObjectArrays.toArrayImpl(delegate());
        }
      }
      
      public <T> T[] toArray(T[] array)
      {
        synchronized (mutex) {
          return ObjectArrays.toArrayImpl(delegate(), array);
        }
      }
      
      public boolean contains(Object o)
      {
        synchronized (mutex) {
          return Maps.containsEntryImpl(delegate(), o);
        }
      }
      
      public boolean containsAll(Collection<?> c)
      {
        synchronized (mutex) {
          return Collections2.containsAllImpl(delegate(), c);
        }
      }
      
      public boolean equals(Object o)
      {
        if (o == this) {
          return true;
        }
        synchronized (mutex) {
          return Sets.equalsImpl(delegate(), o);
        }
      }
      
      public boolean remove(Object o)
      {
        synchronized (mutex) {
          return Maps.removeEntryImpl(delegate(), o);
        }
      }
      
      public boolean removeAll(Collection<?> c)
      {
        synchronized (mutex) {
          return Iterators.removeAll(delegate().iterator(), c);
        }
      }
      
      public boolean retainAll(Collection<?> c)
      {
        synchronized (mutex) {
          return Iterators.retainAll(delegate().iterator(), c);
        }
      }
    }
    

    @VisibleForTesting
    static <K, V> Map<K, V> map(Map<K, V> map, @Nullable Object mutex)
    {
      return new SynchronizedMap(map, mutex);
    }
    
    private static class SynchronizedMap<K, V> extends Synchronized.SynchronizedObject implements Map<K, V> {
      transient Set<K> keySet;
      transient Collection<V> values;
      transient Set<Map.Entry<K, V>> entrySet;
      private static final long serialVersionUID = 0L;
      
      SynchronizedMap(Map<K, V> delegate, @Nullable Object mutex) { super(mutex); }
      


      Map<K, V> delegate()
      {
        return (Map)super.delegate();
      }
      
      public void clear()
      {
        synchronized (mutex) {
          delegate().clear();
        }
      }
      
      public boolean containsKey(Object key)
      {
        synchronized (mutex) {
          return delegate().containsKey(key);
        }
      }
      
      public boolean containsValue(Object value)
      {
        synchronized (mutex) {
          return delegate().containsValue(value);
        }
      }
      
      public Set<Map.Entry<K, V>> entrySet()
      {
        synchronized (mutex) {
          if (entrySet == null) {
            entrySet = Synchronized.set(delegate().entrySet(), mutex);
          }
          return entrySet;
        }
      }
      
      public void forEach(BiConsumer<? super K, ? super V> action)
      {
        synchronized (mutex) {
          delegate().forEach(action);
        }
      }
      
      public V get(Object key)
      {
        synchronized (mutex) {
          return delegate().get(key);
        }
      }
      
      public V getOrDefault(Object key, V defaultValue)
      {
        synchronized (mutex) {
          return delegate().getOrDefault(key, defaultValue);
        }
      }
      
      public boolean isEmpty()
      {
        synchronized (mutex) {
          return delegate().isEmpty();
        }
      }
      
      public Set<K> keySet()
      {
        synchronized (mutex) {
          if (keySet == null) {
            keySet = Synchronized.set(delegate().keySet(), mutex);
          }
          return keySet;
        }
      }
      
      public V put(K key, V value)
      {
        synchronized (mutex) {
          return delegate().put(key, value);
        }
      }
      
      public V putIfAbsent(K key, V value)
      {
        synchronized (mutex) {
          return delegate().putIfAbsent(key, value);
        }
      }
      
      public boolean replace(K key, V oldValue, V newValue)
      {
        synchronized (mutex) {
          return delegate().replace(key, oldValue, newValue);
        }
      }
      
      public V replace(K key, V value)
      {
        synchronized (mutex) {
          return delegate().replace(key, value);
        }
      }
      
      public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)
      {
        synchronized (mutex) {
          return delegate().computeIfAbsent(key, mappingFunction);
        }
      }
      

      public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
      {
        synchronized (mutex) {
          return delegate().computeIfPresent(key, remappingFunction);
        }
      }
      
      public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)
      {
        synchronized (mutex) {
          return delegate().compute(key, remappingFunction);
        }
      }
      

      public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
      {
        synchronized (mutex) {
          return delegate().merge(key, value, remappingFunction);
        }
      }
      
      public void putAll(Map<? extends K, ? extends V> map)
      {
        synchronized (mutex) {
          delegate().putAll(map);
        }
      }
      
      public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function)
      {
        synchronized (mutex) {
          delegate().replaceAll(function);
        }
      }
      
      public V remove(Object key)
      {
        synchronized (mutex) {
          return delegate().remove(key);
        }
      }
      
      public boolean remove(Object key, Object value)
      {
        synchronized (mutex) {
          return delegate().remove(key, value);
        }
      }
      
      public int size()
      {
        synchronized (mutex) {
          return delegate().size();
        }
      }
      
      public Collection<V> values()
      {
        synchronized (mutex) {
          if (values == null) {
            values = Synchronized.collection(delegate().values(), mutex);
          }
          return values;
        }
      }
      
      public boolean equals(Object o)
      {
        if (o == this) {
          return true;
        }
        synchronized (mutex) {
          return delegate().equals(o);
        }
      }
      
      public int hashCode()
      {
        synchronized (mutex) {
          return delegate().hashCode();
        }
      }
    }
    

    static <K, V> SortedMap<K, V> sortedMap(SortedMap<K, V> sortedMap, @Nullable Object mutex)
    {
      return new SynchronizedSortedMap(sortedMap, mutex);
    }
    
    static class SynchronizedSortedMap<K, V> extends Synchronized.SynchronizedMap<K, V> implements SortedMap<K, V> {
      private static final long serialVersionUID = 0L;
      
      SynchronizedSortedMap(SortedMap<K, V> delegate, @Nullable Object mutex) {
        super(mutex);
      }
      
      SortedMap<K, V> delegate()
      {
        return (SortedMap)super.delegate();
      }
      
      public Comparator<? super K> comparator()
      {
        synchronized (mutex) {
          return delegate().comparator();
        }
      }
      
      public K firstKey()
      {
        synchronized (mutex) {
          return delegate().firstKey();
        }
      }
      
      public SortedMap<K, V> headMap(K toKey)
      {
        synchronized (mutex) {
          return Synchronized.sortedMap(delegate().headMap(toKey), mutex);
        }
      }
      
      public K lastKey()
      {
        synchronized (mutex) {
          return delegate().lastKey();
        }
      }
      
      public SortedMap<K, V> subMap(K fromKey, K toKey)
      {
        synchronized (mutex) {
          return Synchronized.sortedMap(delegate().subMap(fromKey, toKey), mutex);
        }
      }
      
      public SortedMap<K, V> tailMap(K fromKey)
      {
        synchronized (mutex) {
          return Synchronized.sortedMap(delegate().tailMap(fromKey), mutex);
        }
      }
    }
    

    static <K, V> BiMap<K, V> biMap(BiMap<K, V> bimap, @Nullable Object mutex)
    {
      if (((bimap instanceof SynchronizedBiMap)) || ((bimap instanceof ImmutableBiMap))) {
        return bimap;
      }
      return new SynchronizedBiMap(bimap, mutex, null, null);
    }
    
    @VisibleForTesting
    static class SynchronizedBiMap<K, V> extends Synchronized.SynchronizedMap<K, V> implements BiMap<K, V>, Serializable
    {
      private transient Set<V> valueSet;
      @RetainedWith
      private transient BiMap<V, K> inverse;
      private static final long serialVersionUID = 0L;
      
      private SynchronizedBiMap(BiMap<K, V> delegate, @Nullable Object mutex, @Nullable BiMap<V, K> inverse) {
        super(mutex);
        this.inverse = inverse;
      }
      
      BiMap<K, V> delegate()
      {
        return (BiMap)super.delegate();
      }
      
      public Set<V> values()
      {
        synchronized (mutex) {
          if (valueSet == null) {
            valueSet = Synchronized.set(delegate().values(), mutex);
          }
          return valueSet;
        }
      }
      
      public V forcePut(K key, V value)
      {
        synchronized (mutex) {
          return delegate().forcePut(key, value);
        }
      }
      
      public BiMap<V, K> inverse()
      {
        synchronized (mutex) {
          if (inverse == null) {
            inverse = new SynchronizedBiMap(delegate().inverse(), mutex, this);
          }
          return inverse;
        }
      }
    }
    
    private static class SynchronizedAsMap<K, V> extends Synchronized.SynchronizedMap<K, Collection<V>>
    {
      transient Set<Map.Entry<K, Collection<V>>> asMapEntrySet;
      transient Collection<Collection<V>> asMapValues;
      private static final long serialVersionUID = 0L;
      
      SynchronizedAsMap(Map<K, Collection<V>> delegate, @Nullable Object mutex) {
        super(mutex);
      }
      
      public Collection<V> get(Object key)
      {
        synchronized (mutex) {
          Collection<V> collection = (Collection)super.get(key);
          return collection == null ? null : Synchronized.typePreservingCollection(collection, mutex);
        }
      }
      
      public Set<Map.Entry<K, Collection<V>>> entrySet()
      {
        synchronized (mutex) {
          if (asMapEntrySet == null) {
            asMapEntrySet = new Synchronized.SynchronizedAsMapEntries(delegate().entrySet(), mutex);
          }
          return asMapEntrySet;
        }
      }
      
      public Collection<Collection<V>> values()
      {
        synchronized (mutex) {
          if (asMapValues == null) {
            asMapValues = new Synchronized.SynchronizedAsMapValues(delegate().values(), mutex);
          }
          return asMapValues;
        }
      }
      

      public boolean containsValue(Object o)
      {
        return values().contains(o);
      }
    }
    
    private static class SynchronizedAsMapValues<V> extends Synchronized.SynchronizedCollection<Collection<V>> {
      private static final long serialVersionUID = 0L;
      
      SynchronizedAsMapValues(Collection<Collection<V>> delegate, @Nullable Object mutex) {
        super(mutex, null);
      }
      

      public Iterator<Collection<V>> iterator()
      {
        new TransformedIterator(super.iterator())
        {
          Collection<V> transform(Collection<V> from) {
            return Synchronized.typePreservingCollection(from, mutex);
          }
        };
      }
    }
    
    @GwtIncompatible
    @VisibleForTesting
    static class SynchronizedNavigableSet<E> extends Synchronized.SynchronizedSortedSet<E> implements NavigableSet<E> {
      transient NavigableSet<E> descendingSet;
      private static final long serialVersionUID = 0L;
      
      SynchronizedNavigableSet(NavigableSet<E> delegate, @Nullable Object mutex) {
        super(mutex);
      }
      
      NavigableSet<E> delegate()
      {
        return (NavigableSet)super.delegate();
      }
      
      public E ceiling(E e)
      {
        synchronized (mutex) {
          return delegate().ceiling(e);
        }
      }
      
      public Iterator<E> descendingIterator()
      {
        return delegate().descendingIterator();
      }
      


      public NavigableSet<E> descendingSet()
      {
        synchronized (mutex) {
          if (descendingSet == null) {
            NavigableSet<E> dS = Synchronized.navigableSet(delegate().descendingSet(), mutex);
            descendingSet = dS;
            return dS;
          }
          return descendingSet;
        }
      }
      
      public E floor(E e)
      {
        synchronized (mutex) {
          return delegate().floor(e);
        }
      }
      
      public NavigableSet<E> headSet(E toElement, boolean inclusive)
      {
        synchronized (mutex) {
          return Synchronized.navigableSet(delegate().headSet(toElement, inclusive), mutex);
        }
      }
      
      public E higher(E e)
      {
        synchronized (mutex) {
          return delegate().higher(e);
        }
      }
      
      public E lower(E e)
      {
        synchronized (mutex) {
          return delegate().lower(e);
        }
      }
      
      public E pollFirst()
      {
        synchronized (mutex) {
          return delegate().pollFirst();
        }
      }
      
      public E pollLast()
      {
        synchronized (mutex) {
          return delegate().pollLast();
        }
      }
      

      public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
      {
        synchronized (mutex) {
          return Synchronized.navigableSet(
            delegate().subSet(fromElement, fromInclusive, toElement, toInclusive), mutex);
        }
      }
      
      public NavigableSet<E> tailSet(E fromElement, boolean inclusive)
      {
        synchronized (mutex) {
          return Synchronized.navigableSet(delegate().tailSet(fromElement, inclusive), mutex);
        }
      }
      
      public SortedSet<E> headSet(E toElement)
      {
        return headSet(toElement, false);
      }
      
      public SortedSet<E> subSet(E fromElement, E toElement)
      {
        return subSet(fromElement, true, toElement, false);
      }
      
      public SortedSet<E> tailSet(E fromElement)
      {
        return tailSet(fromElement, true);
      }
    }
    

    @GwtIncompatible
    static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet, @Nullable Object mutex)
    {
      return new SynchronizedNavigableSet(navigableSet, mutex);
    }
    
    @GwtIncompatible
    static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet) {
      return navigableSet(navigableSet, null);
    }
    
    @GwtIncompatible
    static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap) {
      return navigableMap(navigableMap, null);
    }
    


    @GwtIncompatible
    static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap, @Nullable Object mutex) { return new SynchronizedNavigableMap(navigableMap, mutex); }
    
    @GwtIncompatible
    @VisibleForTesting
    static class SynchronizedNavigableMap<K, V> extends Synchronized.SynchronizedSortedMap<K, V> implements NavigableMap<K, V> { transient NavigableSet<K> descendingKeySet;
      transient NavigableMap<K, V> descendingMap;
      transient NavigableSet<K> navigableKeySet;
      private static final long serialVersionUID = 0L;
      
      SynchronizedNavigableMap(NavigableMap<K, V> delegate, @Nullable Object mutex) { super(mutex); }
      

      NavigableMap<K, V> delegate()
      {
        return (NavigableMap)super.delegate();
      }
      
      public Map.Entry<K, V> ceilingEntry(K key)
      {
        synchronized (mutex) {
          return Synchronized.nullableSynchronizedEntry(delegate().ceilingEntry(key), mutex);
        }
      }
      
      public K ceilingKey(K key)
      {
        synchronized (mutex) {
          return delegate().ceilingKey(key);
        }
      }
      


      public NavigableSet<K> descendingKeySet()
      {
        synchronized (mutex) {
          if (descendingKeySet == null) {
            return this.descendingKeySet = Synchronized.navigableSet(delegate().descendingKeySet(), mutex);
          }
          return descendingKeySet;
        }
      }
      


      public NavigableMap<K, V> descendingMap()
      {
        synchronized (mutex) {
          if (descendingMap == null) {
            return this.descendingMap = Synchronized.navigableMap(delegate().descendingMap(), mutex);
          }
          return descendingMap;
        }
      }
      
      public Map.Entry<K, V> firstEntry()
      {
        synchronized (mutex) {
          return Synchronized.nullableSynchronizedEntry(delegate().firstEntry(), mutex);
        }
      }
      
      public Map.Entry<K, V> floorEntry(K key)
      {
        synchronized (mutex) {
          return Synchronized.nullableSynchronizedEntry(delegate().floorEntry(key), mutex);
        }
      }
      
      public K floorKey(K key)
      {
        synchronized (mutex) {
          return delegate().floorKey(key);
        }
      }
      
      public NavigableMap<K, V> headMap(K toKey, boolean inclusive)
      {
        synchronized (mutex) {
          return Synchronized.navigableMap(delegate().headMap(toKey, inclusive), mutex);
        }
      }
      
      public Map.Entry<K, V> higherEntry(K key)
      {
        synchronized (mutex) {
          return Synchronized.nullableSynchronizedEntry(delegate().higherEntry(key), mutex);
        }
      }
      
      public K higherKey(K key)
      {
        synchronized (mutex) {
          return delegate().higherKey(key);
        }
      }
      
      public Map.Entry<K, V> lastEntry()
      {
        synchronized (mutex) {
          return Synchronized.nullableSynchronizedEntry(delegate().lastEntry(), mutex);
        }
      }
      
      public Map.Entry<K, V> lowerEntry(K key)
      {
        synchronized (mutex) {
          return Synchronized.nullableSynchronizedEntry(delegate().lowerEntry(key), mutex);
        }
      }
      
      public K lowerKey(K key)
      {
        synchronized (mutex) {
          return delegate().lowerKey(key);
        }
      }
      
      public Set<K> keySet()
      {
        return navigableKeySet();
      }
      


      public NavigableSet<K> navigableKeySet()
      {
        synchronized (mutex) {
          if (navigableKeySet == null) {
            return this.navigableKeySet = Synchronized.navigableSet(delegate().navigableKeySet(), mutex);
          }
          return navigableKeySet;
        }
      }
      
      public Map.Entry<K, V> pollFirstEntry()
      {
        synchronized (mutex) {
          return Synchronized.nullableSynchronizedEntry(delegate().pollFirstEntry(), mutex);
        }
      }
      
      public Map.Entry<K, V> pollLastEntry()
      {
        synchronized (mutex) {
          return Synchronized.nullableSynchronizedEntry(delegate().pollLastEntry(), mutex);
        }
      }
      

      public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
      {
        synchronized (mutex) {
          return Synchronized.navigableMap(delegate().subMap(fromKey, fromInclusive, toKey, toInclusive), mutex);
        }
      }
      
      public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive)
      {
        synchronized (mutex) {
          return Synchronized.navigableMap(delegate().tailMap(fromKey, inclusive), mutex);
        }
      }
      
      public SortedMap<K, V> headMap(K toKey)
      {
        return headMap(toKey, false);
      }
      
      public SortedMap<K, V> subMap(K fromKey, K toKey)
      {
        return subMap(fromKey, true, toKey, false);
      }
      
      public SortedMap<K, V> tailMap(K fromKey)
      {
        return tailMap(fromKey, true);
      }
    }
    


    @GwtIncompatible
    private static <K, V> Map.Entry<K, V> nullableSynchronizedEntry(@Nullable Map.Entry<K, V> entry, @Nullable Object mutex)
    {
      if (entry == null) {
        return null;
      }
      return new SynchronizedEntry(entry, mutex);
    }
    
    @GwtIncompatible
    private static class SynchronizedEntry<K, V> extends Synchronized.SynchronizedObject implements Map.Entry<K, V> {
      private static final long serialVersionUID = 0L;
      
      SynchronizedEntry(Map.Entry<K, V> delegate, @Nullable Object mutex) { super(mutex); }
      


      Map.Entry<K, V> delegate()
      {
        return (Map.Entry)super.delegate();
      }
      
      public boolean equals(Object obj)
      {
        synchronized (mutex) {
          return delegate().equals(obj);
        }
      }
      
      public int hashCode()
      {
        synchronized (mutex) {
          return delegate().hashCode();
        }
      }
      
      public K getKey()
      {
        synchronized (mutex) {
          return delegate().getKey();
        }
      }
      
      public V getValue()
      {
        synchronized (mutex) {
          return delegate().getValue();
        }
      }
      
      public V setValue(V value)
      {
        synchronized (mutex) {
          return delegate().setValue(value);
        }
      }
    }
    

    static <E> Queue<E> queue(Queue<E> queue, @Nullable Object mutex)
    {
      return (queue instanceof SynchronizedQueue) ? queue : new SynchronizedQueue(queue, mutex);
    }
    
    private static class SynchronizedQueue<E> extends Synchronized.SynchronizedCollection<E> implements Queue<E> {
      private static final long serialVersionUID = 0L;
      
      SynchronizedQueue(Queue<E> delegate, @Nullable Object mutex) { super(mutex, null); }
      

      Queue<E> delegate()
      {
        return (Queue)super.delegate();
      }
      
      public E element()
      {
        synchronized (mutex) {
          return delegate().element();
        }
      }
      
      public boolean offer(E e)
      {
        synchronized (mutex) {
          return delegate().offer(e);
        }
      }
      
      public E peek()
      {
        synchronized (mutex) {
          return delegate().peek();
        }
      }
      
      public E poll()
      {
        synchronized (mutex) {
          return delegate().poll();
        }
      }
      
      public E remove()
      {
        synchronized (mutex) {
          return delegate().remove();
        }
      }
    }
    

    static <E> Deque<E> deque(Deque<E> deque, @Nullable Object mutex)
    {
      return new SynchronizedDeque(deque, mutex);
    }
    
    private static final class SynchronizedDeque<E> extends Synchronized.SynchronizedQueue<E> implements Deque<E> {
      private static final long serialVersionUID = 0L;
      
      SynchronizedDeque(Deque<E> delegate, @Nullable Object mutex) { super(mutex); }
      

      Deque<E> delegate()
      {
        return (Deque)super.delegate();
      }
      
      public void addFirst(E e)
      {
        synchronized (mutex) {
          delegate().addFirst(e);
        }
      }
      
      public void addLast(E e)
      {
        synchronized (mutex) {
          delegate().addLast(e);
        }
      }
      
      public boolean offerFirst(E e)
      {
        synchronized (mutex) {
          return delegate().offerFirst(e);
        }
      }
      
      public boolean offerLast(E e)
      {
        synchronized (mutex) {
          return delegate().offerLast(e);
        }
      }
      
      public E removeFirst()
      {
        synchronized (mutex) {
          return delegate().removeFirst();
        }
      }
      
      public E removeLast()
      {
        synchronized (mutex) {
          return delegate().removeLast();
        }
      }
      
      public E pollFirst()
      {
        synchronized (mutex) {
          return delegate().pollFirst();
        }
      }
      
      public E pollLast()
      {
        synchronized (mutex) {
          return delegate().pollLast();
        }
      }
      
      public E getFirst()
      {
        synchronized (mutex) {
          return delegate().getFirst();
        }
      }
      
      public E getLast()
      {
        synchronized (mutex) {
          return delegate().getLast();
        }
      }
      
      public E peekFirst()
      {
        synchronized (mutex) {
          return delegate().peekFirst();
        }
      }
      
      public E peekLast()
      {
        synchronized (mutex) {
          return delegate().peekLast();
        }
      }
      
      public boolean removeFirstOccurrence(Object o)
      {
        synchronized (mutex) {
          return delegate().removeFirstOccurrence(o);
        }
      }
      
      public boolean removeLastOccurrence(Object o)
      {
        synchronized (mutex) {
          return delegate().removeLastOccurrence(o);
        }
      }
      
      public void push(E e)
      {
        synchronized (mutex) {
          delegate().push(e);
        }
      }
      
      public E pop()
      {
        synchronized (mutex) {
          return delegate().pop();
        }
      }
      
      public Iterator<E> descendingIterator()
      {
        synchronized (mutex) {
          return delegate().descendingIterator();
        }
      }
    }
  }
