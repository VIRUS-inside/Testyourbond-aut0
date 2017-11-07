package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import javax.annotation.Nullable;





































































































@GwtCompatible
public final class Range<C extends Comparable>
  implements Predicate<C>, Serializable
{
  private static final Function<Range, Cut> LOWER_BOUND_FN = new Function()
  {
    public Cut apply(Range range)
    {
      return lowerBound;
    }
  };
  
  static <C extends Comparable<?>> Function<Range<C>, Cut<C>> lowerBoundFn()
  {
    return LOWER_BOUND_FN;
  }
  
  private static final Function<Range, Cut> UPPER_BOUND_FN = new Function()
  {
    public Cut apply(Range range)
    {
      return upperBound;
    }
  };
  
  static <C extends Comparable<?>> Function<Range<C>, Cut<C>> upperBoundFn()
  {
    return UPPER_BOUND_FN;
  }
  
  static final Ordering<Range<?>> RANGE_LEX_ORDERING = new RangeLexOrdering(null);
  
  static <C extends Comparable<?>> Range<C> create(Cut<C> lowerBound, Cut<C> upperBound) {
    return new Range(lowerBound, upperBound);
  }
  







  public static <C extends Comparable<?>> Range<C> open(C lower, C upper)
  {
    return create(Cut.aboveValue(lower), Cut.belowValue(upper));
  }
  







  public static <C extends Comparable<?>> Range<C> closed(C lower, C upper)
  {
    return create(Cut.belowValue(lower), Cut.aboveValue(upper));
  }
  







  public static <C extends Comparable<?>> Range<C> closedOpen(C lower, C upper)
  {
    return create(Cut.belowValue(lower), Cut.belowValue(upper));
  }
  







  public static <C extends Comparable<?>> Range<C> openClosed(C lower, C upper)
  {
    return create(Cut.aboveValue(lower), Cut.aboveValue(upper));
  }
  









  public static <C extends Comparable<?>> Range<C> range(C lower, BoundType lowerType, C upper, BoundType upperType)
  {
    Preconditions.checkNotNull(lowerType);
    Preconditions.checkNotNull(upperType);
    

    Cut<C> lowerBound = lowerType == BoundType.OPEN ? Cut.aboveValue(lower) : Cut.belowValue(lower);
    
    Cut<C> upperBound = upperType == BoundType.OPEN ? Cut.belowValue(upper) : Cut.aboveValue(upper);
    return create(lowerBound, upperBound);
  }
  





  public static <C extends Comparable<?>> Range<C> lessThan(C endpoint)
  {
    return create(Cut.belowAll(), Cut.belowValue(endpoint));
  }
  





  public static <C extends Comparable<?>> Range<C> atMost(C endpoint)
  {
    return create(Cut.belowAll(), Cut.aboveValue(endpoint));
  }
  





  public static <C extends Comparable<?>> Range<C> upTo(C endpoint, BoundType boundType)
  {
    switch (3.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()]) {
    case 1: 
      return lessThan(endpoint);
    case 2: 
      return atMost(endpoint);
    }
    throw new AssertionError();
  }
  






  public static <C extends Comparable<?>> Range<C> greaterThan(C endpoint)
  {
    return create(Cut.aboveValue(endpoint), Cut.aboveAll());
  }
  





  public static <C extends Comparable<?>> Range<C> atLeast(C endpoint)
  {
    return create(Cut.belowValue(endpoint), Cut.aboveAll());
  }
  





  public static <C extends Comparable<?>> Range<C> downTo(C endpoint, BoundType boundType)
  {
    switch (3.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()]) {
    case 1: 
      return greaterThan(endpoint);
    case 2: 
      return atLeast(endpoint);
    }
    throw new AssertionError();
  }
  

  private static final Range<Comparable> ALL = new Range(
    Cut.belowAll(), Cut.aboveAll());
  
  final Cut<C> lowerBound;
  
  final Cut<C> upperBound;
  private static final long serialVersionUID = 0L;
  
  public static <C extends Comparable<?>> Range<C> all()
  {
    return ALL;
  }
  






  public static <C extends Comparable<?>> Range<C> singleton(C value)
  {
    return closed(value, value);
  }
  










  public static <C extends Comparable<?>> Range<C> encloseAll(Iterable<C> values)
  {
    Preconditions.checkNotNull(values);
    if ((values instanceof ContiguousSet)) {
      return ((ContiguousSet)values).range();
    }
    Iterator<C> valueIterator = values.iterator();
    C min = (Comparable)Preconditions.checkNotNull(valueIterator.next());
    C max = min;
    while (valueIterator.hasNext()) {
      C value = (Comparable)Preconditions.checkNotNull(valueIterator.next());
      min = (Comparable)Ordering.natural().min(min, value);
      max = (Comparable)Ordering.natural().max(max, value);
    }
    return closed(min, max);
  }
  


  private Range(Cut<C> lowerBound, Cut<C> upperBound)
  {
    this.lowerBound = ((Cut)Preconditions.checkNotNull(lowerBound));
    this.upperBound = ((Cut)Preconditions.checkNotNull(upperBound));
    if ((lowerBound.compareTo(upperBound) > 0) || 
      (lowerBound == Cut.aboveAll()) || 
      (upperBound == Cut.belowAll())) {
      throw new IllegalArgumentException("Invalid range: " + toString(lowerBound, upperBound));
    }
  }
  


  public boolean hasLowerBound()
  {
    return lowerBound != Cut.belowAll();
  }
  





  public C lowerEndpoint()
  {
    return lowerBound.endpoint();
  }
  






  public BoundType lowerBoundType()
  {
    return lowerBound.typeAsLowerBound();
  }
  


  public boolean hasUpperBound()
  {
    return upperBound != Cut.aboveAll();
  }
  





  public C upperEndpoint()
  {
    return upperBound.endpoint();
  }
  






  public BoundType upperBoundType()
  {
    return upperBound.typeAsUpperBound();
  }
  








  public boolean isEmpty()
  {
    return lowerBound.equals(upperBound);
  }
  




  public boolean contains(C value)
  {
    Preconditions.checkNotNull(value);
    
    return (lowerBound.isLessThan(value)) && (!upperBound.isLessThan(value));
  }
  




  @Deprecated
  public boolean apply(C input)
  {
    return contains(input);
  }
  



  public boolean containsAll(Iterable<? extends C> values)
  {
    if (Iterables.isEmpty(values)) {
      return true;
    }
    
    SortedSet<? extends C> set;
    if ((values instanceof SortedSet)) {
      set = cast(values);
      Comparator<?> comparator = set.comparator();
      if ((Ordering.natural().equals(comparator)) || (comparator == null)) {
        return (contains((Comparable)set.first())) && (contains((Comparable)set.last()));
      }
    }
    
    for (C value : values) {
      if (!contains(value)) {
        return false;
      }
    }
    return true;
  }
  























  public boolean encloses(Range<C> other)
  {
    return (lowerBound.compareTo(lowerBound) <= 0) && 
      (upperBound.compareTo(upperBound) >= 0);
  }
  























  public boolean isConnected(Range<C> other)
  {
    return (lowerBound.compareTo(upperBound) <= 0) && 
      (lowerBound.compareTo(upperBound) <= 0);
  }
  















  public Range<C> intersection(Range<C> connectedRange)
  {
    int lowerCmp = lowerBound.compareTo(lowerBound);
    int upperCmp = upperBound.compareTo(upperBound);
    if ((lowerCmp >= 0) && (upperCmp <= 0))
      return this;
    if ((lowerCmp <= 0) && (upperCmp >= 0)) {
      return connectedRange;
    }
    Cut<C> newLower = lowerCmp >= 0 ? lowerBound : lowerBound;
    Cut<C> newUpper = upperCmp <= 0 ? upperBound : upperBound;
    return create(newLower, newUpper);
  }
  











  public Range<C> span(Range<C> other)
  {
    int lowerCmp = lowerBound.compareTo(lowerBound);
    int upperCmp = upperBound.compareTo(upperBound);
    if ((lowerCmp <= 0) && (upperCmp >= 0))
      return this;
    if ((lowerCmp >= 0) && (upperCmp <= 0)) {
      return other;
    }
    Cut<C> newLower = lowerCmp <= 0 ? lowerBound : lowerBound;
    Cut<C> newUpper = upperCmp >= 0 ? upperBound : upperBound;
    return create(newLower, newUpper);
  }
  
























  public Range<C> canonical(DiscreteDomain<C> domain)
  {
    Preconditions.checkNotNull(domain);
    Cut<C> lower = lowerBound.canonical(domain);
    Cut<C> upper = upperBound.canonical(domain);
    return (lower == lowerBound) && (upper == upperBound) ? this : create(lower, upper);
  }
  







  public boolean equals(@Nullable Object object)
  {
    if ((object instanceof Range)) {
      Range<?> other = (Range)object;
      return (lowerBound.equals(lowerBound)) && (upperBound.equals(upperBound));
    }
    return false;
  }
  

  public int hashCode()
  {
    return lowerBound.hashCode() * 31 + upperBound.hashCode();
  }
  




  public String toString()
  {
    return toString(lowerBound, upperBound);
  }
  
  private static String toString(Cut<?> lowerBound, Cut<?> upperBound) {
    StringBuilder sb = new StringBuilder(16);
    lowerBound.describeAsLowerBound(sb);
    sb.append("..");
    upperBound.describeAsUpperBound(sb);
    return sb.toString();
  }
  


  private static <T> SortedSet<T> cast(Iterable<T> iterable)
  {
    return (SortedSet)iterable;
  }
  
  Object readResolve() {
    if (equals(ALL)) {
      return all();
    }
    return this;
  }
  

  static int compareOrThrow(Comparable left, Comparable right)
  {
    return left.compareTo(right);
  }
  
  private static class RangeLexOrdering extends Ordering<Range<?>> implements Serializable
  {
    private static final long serialVersionUID = 0L;
    
    private RangeLexOrdering() {}
    
    public int compare(Range<?> left, Range<?> right) {
      return 
      

        ComparisonChain.start().compare(lowerBound, lowerBound).compare(upperBound, upperBound).result();
    }
  }
}
