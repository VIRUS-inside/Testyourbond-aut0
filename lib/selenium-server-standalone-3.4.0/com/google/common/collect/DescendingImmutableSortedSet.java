package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;



















@GwtIncompatible
class DescendingImmutableSortedSet<E>
  extends ImmutableSortedSet<E>
{
  private final ImmutableSortedSet<E> forward;
  
  DescendingImmutableSortedSet(ImmutableSortedSet<E> forward)
  {
    super(Ordering.from(forward.comparator()).reverse());
    this.forward = forward;
  }
  
  public boolean contains(@Nullable Object object)
  {
    return forward.contains(object);
  }
  
  public int size()
  {
    return forward.size();
  }
  
  public UnmodifiableIterator<E> iterator()
  {
    return forward.descendingIterator();
  }
  
  ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive)
  {
    return forward.tailSet(toElement, inclusive).descendingSet();
  }
  

  ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
  {
    return forward.subSet(toElement, toInclusive, fromElement, fromInclusive).descendingSet();
  }
  
  ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive)
  {
    return forward.headSet(fromElement, inclusive).descendingSet();
  }
  
  @GwtIncompatible("NavigableSet")
  public ImmutableSortedSet<E> descendingSet()
  {
    return forward;
  }
  
  @GwtIncompatible("NavigableSet")
  public UnmodifiableIterator<E> descendingIterator()
  {
    return forward.iterator();
  }
  
  @GwtIncompatible("NavigableSet")
  ImmutableSortedSet<E> createDescendingSet()
  {
    throw new AssertionError("should never be called");
  }
  
  public E lower(E element)
  {
    return forward.higher(element);
  }
  
  public E floor(E element)
  {
    return forward.ceiling(element);
  }
  
  public E ceiling(E element)
  {
    return forward.floor(element);
  }
  
  public E higher(E element)
  {
    return forward.lower(element);
  }
  
  int indexOf(@Nullable Object target)
  {
    int index = forward.indexOf(target);
    if (index == -1) {
      return index;
    }
    return size() - 1 - index;
  }
  

  boolean isPartialView()
  {
    return forward.isPartialView();
  }
}
