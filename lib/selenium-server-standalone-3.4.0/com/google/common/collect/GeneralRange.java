package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Comparator;
import javax.annotation.Nullable;

















@GwtCompatible(serializable=true)
final class GeneralRange<T>
  implements Serializable
{
  private final Comparator<? super T> comparator;
  private final boolean hasLowerBound;
  @Nullable
  private final T lowerEndpoint;
  private final BoundType lowerBoundType;
  private final boolean hasUpperBound;
  @Nullable
  private final T upperEndpoint;
  private final BoundType upperBoundType;
  private transient GeneralRange<T> reverse;
  
  static <T extends Comparable> GeneralRange<T> from(Range<T> range)
  {
    T lowerEndpoint = range.hasLowerBound() ? range.lowerEndpoint() : null;
    BoundType lowerBoundType = range.hasLowerBound() ? range.lowerBoundType() : BoundType.OPEN;
    
    T upperEndpoint = range.hasUpperBound() ? range.upperEndpoint() : null;
    BoundType upperBoundType = range.hasUpperBound() ? range.upperBoundType() : BoundType.OPEN;
    return new GeneralRange(
      Ordering.natural(), range
      .hasLowerBound(), lowerEndpoint, lowerBoundType, range
      

      .hasUpperBound(), upperEndpoint, upperBoundType);
  }
  




  static <T> GeneralRange<T> all(Comparator<? super T> comparator)
  {
    return new GeneralRange(comparator, false, null, BoundType.OPEN, false, null, BoundType.OPEN);
  }
  




  static <T> GeneralRange<T> downTo(Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType)
  {
    return new GeneralRange(comparator, true, endpoint, boundType, false, null, BoundType.OPEN);
  }
  




  static <T> GeneralRange<T> upTo(Comparator<? super T> comparator, @Nullable T endpoint, BoundType boundType)
  {
    return new GeneralRange(comparator, false, null, BoundType.OPEN, true, endpoint, boundType);
  }
  








  static <T> GeneralRange<T> range(Comparator<? super T> comparator, @Nullable T lower, BoundType lowerType, @Nullable T upper, BoundType upperType)
  {
    return new GeneralRange(comparator, true, lower, lowerType, true, upper, upperType);
  }
  














  private GeneralRange(Comparator<? super T> comparator, boolean hasLowerBound, @Nullable T lowerEndpoint, BoundType lowerBoundType, boolean hasUpperBound, @Nullable T upperEndpoint, BoundType upperBoundType)
  {
    this.comparator = ((Comparator)Preconditions.checkNotNull(comparator));
    this.hasLowerBound = hasLowerBound;
    this.hasUpperBound = hasUpperBound;
    this.lowerEndpoint = lowerEndpoint;
    this.lowerBoundType = ((BoundType)Preconditions.checkNotNull(lowerBoundType));
    this.upperEndpoint = upperEndpoint;
    this.upperBoundType = ((BoundType)Preconditions.checkNotNull(upperBoundType));
    
    if (hasLowerBound) {
      comparator.compare(lowerEndpoint, lowerEndpoint);
    }
    if (hasUpperBound) {
      comparator.compare(upperEndpoint, upperEndpoint);
    }
    if ((hasLowerBound) && (hasUpperBound)) {
      int cmp = comparator.compare(lowerEndpoint, upperEndpoint);
      
      Preconditions.checkArgument(cmp <= 0, "lowerEndpoint (%s) > upperEndpoint (%s)", lowerEndpoint, upperEndpoint);
      
      if (cmp == 0) {
        Preconditions.checkArgument((lowerBoundType != BoundType.OPEN ? 1 : 0) | (upperBoundType != BoundType.OPEN ? 1 : 0));
      }
    }
  }
  
  Comparator<? super T> comparator() {
    return comparator;
  }
  
  boolean hasLowerBound() {
    return hasLowerBound;
  }
  
  boolean hasUpperBound() {
    return hasUpperBound;
  }
  
  boolean isEmpty() {
    return ((hasUpperBound()) && (tooLow(getUpperEndpoint()))) || (
      (hasLowerBound()) && (tooHigh(getLowerEndpoint())));
  }
  
  boolean tooLow(@Nullable T t) {
    if (!hasLowerBound()) {
      return false;
    }
    T lbound = getLowerEndpoint();
    int cmp = comparator.compare(t, lbound);
    return (cmp < 0 ? 1 : 0) | (cmp == 0 ? 1 : 0) & (getLowerBoundType() == BoundType.OPEN ? 1 : 0);
  }
  
  boolean tooHigh(@Nullable T t) {
    if (!hasUpperBound()) {
      return false;
    }
    T ubound = getUpperEndpoint();
    int cmp = comparator.compare(t, ubound);
    return (cmp > 0 ? 1 : 0) | (cmp == 0 ? 1 : 0) & (getUpperBoundType() == BoundType.OPEN ? 1 : 0);
  }
  
  boolean contains(@Nullable T t) {
    return (!tooLow(t)) && (!tooHigh(t));
  }
  


  GeneralRange<T> intersect(GeneralRange<T> other)
  {
    Preconditions.checkNotNull(other);
    Preconditions.checkArgument(comparator.equals(comparator));
    
    boolean hasLowBound = hasLowerBound;
    T lowEnd = getLowerEndpoint();
    BoundType lowType = getLowerBoundType();
    if (!hasLowerBound()) {
      hasLowBound = hasLowerBound;
      lowEnd = other.getLowerEndpoint();
      lowType = other.getLowerBoundType();
    } else if (other.hasLowerBound()) {
      int cmp = comparator.compare(getLowerEndpoint(), other.getLowerEndpoint());
      if ((cmp < 0) || ((cmp == 0) && (other.getLowerBoundType() == BoundType.OPEN))) {
        lowEnd = other.getLowerEndpoint();
        lowType = other.getLowerBoundType();
      }
    }
    
    boolean hasUpBound = hasUpperBound;
    T upEnd = getUpperEndpoint();
    BoundType upType = getUpperBoundType();
    if (!hasUpperBound()) {
      hasUpBound = hasUpperBound;
      upEnd = other.getUpperEndpoint();
      upType = other.getUpperBoundType();
    } else if (other.hasUpperBound()) {
      int cmp = comparator.compare(getUpperEndpoint(), other.getUpperEndpoint());
      if ((cmp > 0) || ((cmp == 0) && (other.getUpperBoundType() == BoundType.OPEN))) {
        upEnd = other.getUpperEndpoint();
        upType = other.getUpperBoundType();
      }
    }
    
    if ((hasLowBound) && (hasUpBound)) {
      int cmp = comparator.compare(lowEnd, upEnd);
      if ((cmp > 0) || ((cmp == 0) && (lowType == BoundType.OPEN) && (upType == BoundType.OPEN)))
      {
        lowEnd = upEnd;
        lowType = BoundType.OPEN;
        upType = BoundType.CLOSED;
      }
    }
    
    return new GeneralRange(comparator, hasLowBound, lowEnd, lowType, hasUpBound, upEnd, upType);
  }
  
  public boolean equals(@Nullable Object obj)
  {
    if ((obj instanceof GeneralRange)) {
      GeneralRange<?> r = (GeneralRange)obj;
      if ((comparator.equals(comparator)) && (hasLowerBound == hasLowerBound) && (hasUpperBound == hasUpperBound)) {} return 
      

        (getLowerBoundType().equals(r.getLowerBoundType())) && 
        (getUpperBoundType().equals(r.getUpperBoundType())) && 
        (Objects.equal(getLowerEndpoint(), r.getLowerEndpoint())) && 
        (Objects.equal(getUpperEndpoint(), r.getUpperEndpoint()));
    }
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hashCode(new Object[] { comparator, 
    
      getLowerEndpoint(), 
      getLowerBoundType(), 
      getUpperEndpoint(), 
      getUpperBoundType() });
  }
  




  GeneralRange<T> reverse()
  {
    GeneralRange<T> result = reverse;
    if (result == null)
    {







      result = new GeneralRange(Ordering.from(comparator).reverse(), hasUpperBound, getUpperEndpoint(), getUpperBoundType(), hasLowerBound, getLowerEndpoint(), getLowerBoundType());
      reverse = this;
      return this.reverse = result;
    }
    return result;
  }
  
  public String toString()
  {
    return comparator + ":" + (lowerBoundType == BoundType.CLOSED ? '[' : '(') + (hasLowerBound ? lowerEndpoint : "-∞") + ',' + (hasUpperBound ? upperEndpoint : "∞") + (upperBoundType == BoundType.CLOSED ? ']' : ')');
  }
  





  T getLowerEndpoint()
  {
    return lowerEndpoint;
  }
  
  BoundType getLowerBoundType() {
    return lowerBoundType;
  }
  
  T getUpperEndpoint() {
    return upperEndpoint;
  }
  
  BoundType getUpperBoundType() {
    return upperBoundType;
  }
}
