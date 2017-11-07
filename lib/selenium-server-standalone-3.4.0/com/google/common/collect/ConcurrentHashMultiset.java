package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;













































@GwtIncompatible
public final class ConcurrentHashMultiset<E>
  extends AbstractMultiset<E>
  implements Serializable
{
  private final transient ConcurrentMap<E, AtomicInteger> countMap;
  private static final long serialVersionUID = 1L;
  
  private static class FieldSettersHolder
  {
    static final Serialization.FieldSetter<ConcurrentHashMultiset> COUNT_MAP_FIELD_SETTER = Serialization.getFieldSetter(ConcurrentHashMultiset.class, "countMap");
    


    private FieldSettersHolder() {}
  }
  


  public static <E> ConcurrentHashMultiset<E> create()
  {
    return new ConcurrentHashMultiset(new ConcurrentHashMap());
  }
  







  public static <E> ConcurrentHashMultiset<E> create(Iterable<? extends E> elements)
  {
    ConcurrentHashMultiset<E> multiset = create();
    Iterables.addAll(multiset, elements);
    return multiset;
  }
  













  @Beta
  public static <E> ConcurrentHashMultiset<E> create(ConcurrentMap<E, AtomicInteger> countMap)
  {
    return new ConcurrentHashMultiset(countMap);
  }
  
  @VisibleForTesting
  ConcurrentHashMultiset(ConcurrentMap<E, AtomicInteger> countMap) {
    Preconditions.checkArgument(countMap.isEmpty(), "the backing map (%s) must be empty", countMap);
    this.countMap = countMap;
  }
  








  public int count(@Nullable Object element)
  {
    AtomicInteger existingCounter = (AtomicInteger)Maps.safeGet(countMap, element);
    return existingCounter == null ? 0 : existingCounter.get();
  }
  






  public int size()
  {
    long sum = 0L;
    for (AtomicInteger value : countMap.values()) {
      sum += value.get();
    }
    return Ints.saturatedCast(sum);
  }
  





  public Object[] toArray()
  {
    return snapshot().toArray();
  }
  
  public <T> T[] toArray(T[] array)
  {
    return snapshot().toArray(array);
  }
  



  private List<E> snapshot()
  {
    List<E> list = Lists.newArrayListWithExpectedSize(size());
    for (Multiset.Entry<E> entry : entrySet()) {
      E element = entry.getElement();
      for (int i = entry.getCount(); i > 0; i--) {
        list.add(element);
      }
    }
    return list;
  }
  











  @CanIgnoreReturnValue
  public int add(E element, int occurrences)
  {
    Preconditions.checkNotNull(element);
    if (occurrences == 0) {
      return count(element);
    }
    CollectPreconditions.checkPositive(occurrences, "occurences");
    for (;;)
    {
      AtomicInteger existingCounter = (AtomicInteger)Maps.safeGet(countMap, element);
      if (existingCounter == null) {
        existingCounter = (AtomicInteger)countMap.putIfAbsent(element, new AtomicInteger(occurrences));
        if (existingCounter == null) {
          return 0;
        }
      }
      
      for (;;)
      {
        int oldValue = existingCounter.get();
        if (oldValue != 0) {
          try {
            int newValue = IntMath.checkedAdd(oldValue, occurrences);
            if (existingCounter.compareAndSet(oldValue, newValue))
            {
              return oldValue;
            }
          } catch (ArithmeticException overflow) {
            throw new IllegalArgumentException("Overflow adding " + occurrences + " occurrences to a count of " + oldValue);
          }
        }
        



        AtomicInteger newCounter = new AtomicInteger(occurrences);
        if ((countMap.putIfAbsent(element, newCounter) != null) && 
          (!countMap.replace(element, existingCounter, newCounter))) break;
        return 0;
      }
    }
  }
  























  @CanIgnoreReturnValue
  public int remove(@Nullable Object element, int occurrences)
  {
    if (occurrences == 0) {
      return count(element);
    }
    CollectPreconditions.checkPositive(occurrences, "occurences");
    
    AtomicInteger existingCounter = (AtomicInteger)Maps.safeGet(countMap, element);
    if (existingCounter == null) {
      return 0;
    }
    for (;;) {
      int oldValue = existingCounter.get();
      if (oldValue != 0) {
        int newValue = Math.max(0, oldValue - occurrences);
        if (existingCounter.compareAndSet(oldValue, newValue)) {
          if (newValue == 0)
          {

            countMap.remove(element, existingCounter);
          }
          return oldValue;
        }
      } else {
        return 0;
      }
    }
  }
  











  @CanIgnoreReturnValue
  public boolean removeExactly(@Nullable Object element, int occurrences)
  {
    if (occurrences == 0) {
      return true;
    }
    CollectPreconditions.checkPositive(occurrences, "occurences");
    
    AtomicInteger existingCounter = (AtomicInteger)Maps.safeGet(countMap, element);
    if (existingCounter == null) {
      return false;
    }
    for (;;) {
      int oldValue = existingCounter.get();
      if (oldValue < occurrences) {
        return false;
      }
      int newValue = oldValue - occurrences;
      if (existingCounter.compareAndSet(oldValue, newValue)) {
        if (newValue == 0)
        {

          countMap.remove(element, existingCounter);
        }
        return true;
      }
    }
  }
  







  @CanIgnoreReturnValue
  public int setCount(E element, int count)
  {
    Preconditions.checkNotNull(element);
    CollectPreconditions.checkNonnegative(count, "count");
    for (;;) {
      AtomicInteger existingCounter = (AtomicInteger)Maps.safeGet(countMap, element);
      if (existingCounter == null) {
        if (count == 0) {
          return 0;
        }
        existingCounter = (AtomicInteger)countMap.putIfAbsent(element, new AtomicInteger(count));
        if (existingCounter == null) {
          return 0;
        }
      }
      

      for (;;)
      {
        int oldValue = existingCounter.get();
        if (oldValue == 0) {
          if (count == 0) {
            return 0;
          }
          AtomicInteger newCounter = new AtomicInteger(count);
          if ((countMap.putIfAbsent(element, newCounter) == null) || 
            (countMap.replace(element, existingCounter, newCounter))) {
            return 0;
          }
          
          break;
        }
        if (existingCounter.compareAndSet(oldValue, count)) {
          if (count == 0)
          {

            countMap.remove(element, existingCounter);
          }
          return oldValue;
        }
      }
    }
  }
  












  @CanIgnoreReturnValue
  public boolean setCount(E element, int expectedOldCount, int newCount)
  {
    Preconditions.checkNotNull(element);
    CollectPreconditions.checkNonnegative(expectedOldCount, "oldCount");
    CollectPreconditions.checkNonnegative(newCount, "newCount");
    
    AtomicInteger existingCounter = (AtomicInteger)Maps.safeGet(countMap, element);
    if (existingCounter == null) {
      if (expectedOldCount != 0)
        return false;
      if (newCount == 0) {
        return true;
      }
      
      return countMap.putIfAbsent(element, new AtomicInteger(newCount)) == null;
    }
    
    int oldValue = existingCounter.get();
    if (oldValue == expectedOldCount) {
      if (oldValue == 0) {
        if (newCount == 0)
        {
          countMap.remove(element, existingCounter);
          return true;
        }
        AtomicInteger newCounter = new AtomicInteger(newCount);
        return (countMap.putIfAbsent(element, newCounter) == null) || 
          (countMap.replace(element, existingCounter, newCounter));
      }
      
      if (existingCounter.compareAndSet(oldValue, newCount)) {
        if (newCount == 0)
        {

          countMap.remove(element, existingCounter);
        }
        return true;
      }
    }
    
    return false;
  }
  


  Set<E> createElementSet()
  {
    final Set<E> delegate = countMap.keySet();
    new ForwardingSet()
    {
      protected Set<E> delegate() {
        return delegate;
      }
      
      public boolean contains(@Nullable Object object)
      {
        return (object != null) && (Collections2.safeContains(delegate, object));
      }
      
      public boolean containsAll(Collection<?> collection)
      {
        return standardContainsAll(collection);
      }
      
      public boolean remove(Object object)
      {
        return (object != null) && (Collections2.safeRemove(delegate, object));
      }
      
      public boolean removeAll(Collection<?> c)
      {
        return standardRemoveAll(c);
      }
    };
  }
  
  public Set<Multiset.Entry<E>> createEntrySet()
  {
    return new EntrySet(null);
  }
  
  int distinctElements()
  {
    return countMap.size();
  }
  
  public boolean isEmpty()
  {
    return countMap.isEmpty();
  }
  


  Iterator<Multiset.Entry<E>> entryIterator()
  {
    final Iterator<Multiset.Entry<E>> readOnlyIterator = new AbstractIterator()
    {

      private final Iterator<Map.Entry<E, AtomicInteger>> mapEntries = countMap.entrySet().iterator();
      
      protected Multiset.Entry<E> computeNext()
      {
        for (;;) {
          if (!mapEntries.hasNext()) {
            return (Multiset.Entry)endOfData();
          }
          Map.Entry<E, AtomicInteger> mapEntry = (Map.Entry)mapEntries.next();
          int count = ((AtomicInteger)mapEntry.getValue()).get();
          if (count != 0) {
            return Multisets.immutableEntry(mapEntry.getKey(), count);
          }
          
        }
      }
    };
    new ForwardingIterator()
    {
      private Multiset.Entry<E> last;
      
      protected Iterator<Multiset.Entry<E>> delegate() {
        return readOnlyIterator;
      }
      
      public Multiset.Entry<E> next()
      {
        last = ((Multiset.Entry)super.next());
        return last;
      }
      
      public void remove()
      {
        CollectPreconditions.checkRemove(last != null);
        setCount(last.getElement(), 0);
        last = null;
      }
    };
  }
  
  public void clear()
  {
    countMap.clear();
  }
  
  private class EntrySet extends AbstractMultiset<E>.EntrySet {
    private EntrySet() { super(); }
    
    ConcurrentHashMultiset<E> multiset() {
      return ConcurrentHashMultiset.this;
    }
    





    public Object[] toArray()
    {
      return snapshot().toArray();
    }
    
    public <T> T[] toArray(T[] array)
    {
      return snapshot().toArray(array);
    }
    
    private List<Multiset.Entry<E>> snapshot() {
      List<Multiset.Entry<E>> list = Lists.newArrayListWithExpectedSize(size());
      
      Iterators.addAll(list, iterator());
      return list;
    }
  }
  

  private void writeObject(ObjectOutputStream stream)
    throws IOException
  {
    stream.defaultWriteObject();
    stream.writeObject(countMap);
  }
  
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    

    ConcurrentMap<E, Integer> deserializedCountMap = (ConcurrentMap)stream.readObject();
    FieldSettersHolder.COUNT_MAP_FIELD_SETTER.set(this, deserializedCountMap);
  }
}
