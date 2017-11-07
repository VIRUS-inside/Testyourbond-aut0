package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Comparator;


























































public final class Range<T>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final Comparator<T> comparator;
  private final T minimum;
  private final T maximum;
  private transient int hashCode;
  private transient String toString;
  
  public static <T extends Comparable<T>> Range<T> is(T element)
  {
    return between(element, element, null);
  }
  













  public static <T> Range<T> is(T element, Comparator<T> comparator)
  {
    return between(element, element, comparator);
  }
  















  public static <T extends Comparable<T>> Range<T> between(T fromInclusive, T toInclusive)
  {
    return between(fromInclusive, toInclusive, null);
  }
  
















  public static <T> Range<T> between(T fromInclusive, T toInclusive, Comparator<T> comparator)
  {
    return new Range(fromInclusive, toInclusive, comparator);
  }
  







  private Range(T element1, T element2, Comparator<T> comp)
  {
    if ((element1 == null) || (element2 == null)) {
      throw new IllegalArgumentException("Elements in a range must not be null: element1=" + element1 + ", element2=" + element2);
    }
    
    if (comp == null) {
      comparator = ComparableComparator.INSTANCE;
    } else {
      comparator = comp;
    }
    if (comparator.compare(element1, element2) < 1) {
      minimum = element1;
      maximum = element2;
    } else {
      minimum = element2;
      maximum = element1;
    }
  }
  







  public T getMinimum()
  {
    return minimum;
  }
  




  public T getMaximum()
  {
    return maximum;
  }
  







  public Comparator<T> getComparator()
  {
    return comparator;
  }
  







  public boolean isNaturalOrdering()
  {
    return comparator == ComparableComparator.INSTANCE;
  }
  








  public boolean contains(T element)
  {
    if (element == null) {
      return false;
    }
    return (comparator.compare(element, minimum) > -1) && (comparator.compare(element, maximum) < 1);
  }
  





  public boolean isAfter(T element)
  {
    if (element == null) {
      return false;
    }
    return comparator.compare(element, minimum) < 0;
  }
  





  public boolean isStartedBy(T element)
  {
    if (element == null) {
      return false;
    }
    return comparator.compare(element, minimum) == 0;
  }
  





  public boolean isEndedBy(T element)
  {
    if (element == null) {
      return false;
    }
    return comparator.compare(element, maximum) == 0;
  }
  





  public boolean isBefore(T element)
  {
    if (element == null) {
      return false;
    }
    return comparator.compare(element, maximum) > 0;
  }
  









  public int elementCompareTo(T element)
  {
    if (element == null)
    {
      throw new NullPointerException("Element is null");
    }
    if (isAfter(element))
      return -1;
    if (isBefore(element)) {
      return 1;
    }
    return 0;
  }
  












  public boolean containsRange(Range<T> otherRange)
  {
    if (otherRange == null) {
      return false;
    }
    return (contains(minimum)) && 
      (contains(maximum));
  }
  








  public boolean isAfterRange(Range<T> otherRange)
  {
    if (otherRange == null) {
      return false;
    }
    return isAfter(maximum);
  }
  











  public boolean isOverlappedBy(Range<T> otherRange)
  {
    if (otherRange == null) {
      return false;
    }
    return (otherRange.contains(minimum)) || 
      (otherRange.contains(maximum)) || 
      (contains(minimum));
  }
  








  public boolean isBeforeRange(Range<T> otherRange)
  {
    if (otherRange == null) {
      return false;
    }
    return isBefore(minimum);
  }
  






  public Range<T> intersectionWith(Range<T> other)
  {
    if (!isOverlappedBy(other)) {
      throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", new Object[] { other }));
    }
    
    if (equals(other)) {
      return this;
    }
    T min = getComparator().compare(minimum, minimum) < 0 ? minimum : minimum;
    T max = getComparator().compare(maximum, maximum) < 0 ? maximum : maximum;
    return between(min, max, getComparator());
  }
  












  public boolean equals(Object obj)
  {
    if (obj == this)
      return true;
    if ((obj == null) || (obj.getClass() != getClass())) {
      return false;
    }
    

    Range<T> range = (Range)obj;
    return (minimum.equals(minimum)) && 
      (maximum.equals(maximum));
  }
  






  public int hashCode()
  {
    int result = hashCode;
    if (hashCode == 0) {
      result = 17;
      result = 37 * result + getClass().hashCode();
      result = 37 * result + minimum.hashCode();
      result = 37 * result + maximum.hashCode();
      hashCode = result;
    }
    return result;
  }
  







  public String toString()
  {
    if (toString == null) {
      toString = ("[" + minimum + ".." + maximum + "]");
    }
    return toString;
  }
  











  public String toString(String format)
  {
    return String.format(format, new Object[] { minimum, maximum, comparator });
  }
  
  private static enum ComparableComparator
    implements Comparator
  {
    INSTANCE;
    


    private ComparableComparator() {}
    


    public int compare(Object obj1, Object obj2)
    {
      return ((Comparable)obj1).compareTo(obj2);
    }
  }
}
