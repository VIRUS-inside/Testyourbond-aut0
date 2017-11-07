package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collection;
import javax.annotation.Nullable;




















@GwtCompatible(emulated=true)
final class RegularContiguousSet<C extends Comparable>
  extends ContiguousSet<C>
{
  private final Range<C> range;
  private static final long serialVersionUID = 0L;
  
  RegularContiguousSet(Range<C> range, DiscreteDomain<C> domain)
  {
    super(domain);
    this.range = range;
  }
  
  private ContiguousSet<C> intersectionInCurrentDomain(Range<C> other) {
    return range.isConnected(other) ? 
      ContiguousSet.create(range.intersection(other), domain) : new EmptyContiguousSet(domain);
  }
  

  ContiguousSet<C> headSetImpl(C toElement, boolean inclusive)
  {
    return intersectionInCurrentDomain(Range.upTo(toElement, BoundType.forBoolean(inclusive)));
  }
  

  ContiguousSet<C> subSetImpl(C fromElement, boolean fromInclusive, C toElement, boolean toInclusive)
  {
    if ((fromElement.compareTo(toElement) == 0) && (!fromInclusive) && (!toInclusive))
    {
      return new EmptyContiguousSet(domain);
    }
    return intersectionInCurrentDomain(
      Range.range(fromElement, 
      BoundType.forBoolean(fromInclusive), toElement, 
      BoundType.forBoolean(toInclusive)));
  }
  
  ContiguousSet<C> tailSetImpl(C fromElement, boolean inclusive)
  {
    return intersectionInCurrentDomain(Range.downTo(fromElement, BoundType.forBoolean(inclusive)));
  }
  
  @GwtIncompatible
  int indexOf(Object target)
  {
    return contains(target) ? (int)domain.distance(first(), (Comparable)target) : -1;
  }
  
  public UnmodifiableIterator<C> iterator()
  {
    new AbstractSequentialIterator(first()) {
      final C last = last();
      
      protected C computeNext(C previous)
      {
        return RegularContiguousSet.equalsOrThrow(previous, last) ? null : domain.next(previous);
      }
    };
  }
  
  @GwtIncompatible
  public UnmodifiableIterator<C> descendingIterator()
  {
    new AbstractSequentialIterator(last()) {
      final C first = first();
      
      protected C computeNext(C previous)
      {
        return RegularContiguousSet.equalsOrThrow(previous, first) ? null : domain.previous(previous);
      }
    };
  }
  
  private static boolean equalsOrThrow(Comparable<?> left, @Nullable Comparable<?> right) {
    return (right != null) && (Range.compareOrThrow(left, right) == 0);
  }
  
  boolean isPartialView()
  {
    return false;
  }
  
  public C first()
  {
    return range.lowerBound.leastValueAbove(domain);
  }
  
  public C last()
  {
    return range.upperBound.greatestValueBelow(domain);
  }
  
  public int size()
  {
    long distance = domain.distance(first(), last());
    return distance >= 2147483647L ? Integer.MAX_VALUE : (int)distance + 1;
  }
  
  public boolean contains(@Nullable Object object)
  {
    if (object == null) {
      return false;
    }
    try {
      return range.contains((Comparable)object);
    } catch (ClassCastException e) {}
    return false;
  }
  

  public boolean containsAll(Collection<?> targets)
  {
    return Collections2.containsAllImpl(this, targets);
  }
  
  public boolean isEmpty()
  {
    return false;
  }
  
  public ContiguousSet<C> intersection(ContiguousSet<C> other)
  {
    Preconditions.checkNotNull(other);
    Preconditions.checkArgument(domain.equals(domain));
    if (other.isEmpty()) {
      return other;
    }
    C lowerEndpoint = (Comparable)Ordering.natural().max(first(), other.first());
    C upperEndpoint = (Comparable)Ordering.natural().min(last(), other.last());
    return lowerEndpoint.compareTo(upperEndpoint) <= 0 ? 
      ContiguousSet.create(Range.closed(lowerEndpoint, upperEndpoint), domain) : new EmptyContiguousSet(domain);
  }
  


  public Range<C> range()
  {
    return range(BoundType.CLOSED, BoundType.CLOSED);
  }
  
  public Range<C> range(BoundType lowerBoundType, BoundType upperBoundType)
  {
    return Range.create(range.lowerBound
      .withLowerBoundType(lowerBoundType, domain), range.upperBound
      .withUpperBoundType(upperBoundType, domain));
  }
  
  public boolean equals(@Nullable Object object)
  {
    if (object == this)
      return true;
    if ((object instanceof RegularContiguousSet)) {
      RegularContiguousSet<?> that = (RegularContiguousSet)object;
      if (domain.equals(domain)) {
        return (first().equals(that.first())) && (last().equals(that.last()));
      }
    }
    return super.equals(object);
  }
  

  public int hashCode()
  {
    return Sets.hashCodeImpl(this);
  }
  
  @GwtIncompatible
  private static final class SerializedForm<C extends Comparable> implements Serializable {
    final Range<C> range;
    final DiscreteDomain<C> domain;
    
    private SerializedForm(Range<C> range, DiscreteDomain<C> domain) {
      this.range = range;
      this.domain = domain;
    }
    
    private Object readResolve() {
      return new RegularContiguousSet(range, domain);
    }
  }
  
  @GwtIncompatible
  Object writeReplace()
  {
    return new SerializedForm(range, domain, null);
  }
}
