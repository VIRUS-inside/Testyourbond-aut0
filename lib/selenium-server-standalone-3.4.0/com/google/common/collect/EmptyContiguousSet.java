package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

















@GwtCompatible(emulated=true)
final class EmptyContiguousSet<C extends Comparable>
  extends ContiguousSet<C>
{
  EmptyContiguousSet(DiscreteDomain<C> domain)
  {
    super(domain);
  }
  
  public C first()
  {
    throw new NoSuchElementException();
  }
  
  public C last()
  {
    throw new NoSuchElementException();
  }
  
  public int size()
  {
    return 0;
  }
  
  public ContiguousSet<C> intersection(ContiguousSet<C> other)
  {
    return this;
  }
  
  public Range<C> range()
  {
    throw new NoSuchElementException();
  }
  
  public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType)
  {
    throw new NoSuchElementException();
  }
  
  ContiguousSet<C> headSetImpl(C toElement, boolean inclusive)
  {
    return this;
  }
  

  ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive)
  {
    return this;
  }
  
  ContiguousSet<C> tailSetImpl(C fromElement, boolean fromInclusive)
  {
    return this;
  }
  
  public boolean contains(Object object)
  {
    return false;
  }
  
  @GwtIncompatible
  int indexOf(Object target)
  {
    return -1;
  }
  
  public UnmodifiableIterator<C> iterator()
  {
    return Iterators.emptyIterator();
  }
  
  @GwtIncompatible
  public UnmodifiableIterator<C> descendingIterator()
  {
    return Iterators.emptyIterator();
  }
  
  boolean isPartialView()
  {
    return false;
  }
  
  public boolean isEmpty()
  {
    return true;
  }
  
  public ImmutableList<C> asList()
  {
    return ImmutableList.of();
  }
  
  public String toString()
  {
    return "[]";
  }
  
  public boolean equals(@Nullable Object object)
  {
    if ((object instanceof Set)) {
      Set<?> that = (Set)object;
      return that.isEmpty();
    }
    return false;
  }
  
  @GwtIncompatible
  boolean isHashCodeFast()
  {
    return true;
  }
  
  public int hashCode()
  {
    return 0;
  }
  
  @GwtIncompatible
  private static final class SerializedForm<C extends Comparable> implements Serializable {
    private final DiscreteDomain<C> domain;
    private static final long serialVersionUID = 0L;
    
    private SerializedForm(DiscreteDomain<C> domain) { this.domain = domain; }
    
    private Object readResolve()
    {
      return new EmptyContiguousSet(domain);
    }
  }
  


  @GwtIncompatible
  Object writeReplace()
  {
    return new SerializedForm(domain, null);
  }
  
  @GwtIncompatible
  ImmutableSortedSet<C> createDescendingSet() {
    return ImmutableSortedSet.emptySet(Ordering.natural().reverse());
  }
}
