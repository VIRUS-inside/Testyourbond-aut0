package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;





























@GwtCompatible
abstract class AbstractMultiset<E>
  extends AbstractCollection<E>
  implements Multiset<E>
{
  private transient Set<E> elementSet;
  private transient Set<Multiset.Entry<E>> entrySet;
  
  AbstractMultiset() {}
  
  public int size()
  {
    return Multisets.sizeImpl(this);
  }
  
  public boolean isEmpty()
  {
    return entrySet().isEmpty();
  }
  
  public boolean contains(@Nullable Object element)
  {
    return count(element) > 0;
  }
  
  public Iterator<E> iterator()
  {
    return Multisets.iteratorImpl(this);
  }
  
  public int count(@Nullable Object element)
  {
    for (Multiset.Entry<E> entry : entrySet()) {
      if (Objects.equal(entry.getElement(), element)) {
        return entry.getCount();
      }
    }
    return 0;
  }
  

  @CanIgnoreReturnValue
  public boolean add(@Nullable E element)
  {
    add(element, 1);
    return true;
  }
  
  @CanIgnoreReturnValue
  public int add(@Nullable E element, int occurrences)
  {
    throw new UnsupportedOperationException();
  }
  
  @CanIgnoreReturnValue
  public boolean remove(@Nullable Object element)
  {
    return remove(element, 1) > 0;
  }
  
  @CanIgnoreReturnValue
  public int remove(@Nullable Object element, int occurrences)
  {
    throw new UnsupportedOperationException();
  }
  
  @CanIgnoreReturnValue
  public int setCount(@Nullable E element, int count)
  {
    return Multisets.setCountImpl(this, element, count);
  }
  
  @CanIgnoreReturnValue
  public boolean setCount(@Nullable E element, int oldCount, int newCount)
  {
    return Multisets.setCountImpl(this, element, oldCount, newCount);
  }
  








  @CanIgnoreReturnValue
  public boolean addAll(Collection<? extends E> elementsToAdd)
  {
    return Multisets.addAllImpl(this, elementsToAdd);
  }
  
  @CanIgnoreReturnValue
  public boolean removeAll(Collection<?> elementsToRemove)
  {
    return Multisets.removeAllImpl(this, elementsToRemove);
  }
  
  @CanIgnoreReturnValue
  public boolean retainAll(Collection<?> elementsToRetain)
  {
    return Multisets.retainAllImpl(this, elementsToRetain);
  }
  
  public void clear()
  {
    Iterators.clear(entryIterator());
  }
  




  public Set<E> elementSet()
  {
    Set<E> result = elementSet;
    if (result == null) {
      elementSet = (result = createElementSet());
    }
    return result;
  }
  





  Set<E> createElementSet() { return new ElementSet(); }
  
  abstract Iterator<Multiset.Entry<E>> entryIterator();
  
  class ElementSet extends Multisets.ElementSet<E> {
    ElementSet() {}
    
    Multiset<E> multiset() { return AbstractMultiset.this; }
  }
  



  abstract int distinctElements();
  


  public Set<Multiset.Entry<E>> entrySet()
  {
    Set<Multiset.Entry<E>> result = entrySet;
    if (result == null) {
      entrySet = (result = createEntrySet());
    }
    return result;
  }
  
  class EntrySet extends Multisets.EntrySet<E> {
    EntrySet() {}
    
    Multiset<E> multiset() {
      return AbstractMultiset.this;
    }
    
    public Iterator<Multiset.Entry<E>> iterator()
    {
      return entryIterator();
    }
    
    public int size()
    {
      return distinctElements();
    }
  }
  
  Set<Multiset.Entry<E>> createEntrySet() {
    return new EntrySet();
  }
  









  public boolean equals(@Nullable Object object)
  {
    return Multisets.equalsImpl(this, object);
  }
  






  public int hashCode()
  {
    return entrySet().hashCode();
  }
  






  public String toString()
  {
    return entrySet().toString();
  }
}
