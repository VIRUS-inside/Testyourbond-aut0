package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import java.util.Set;
import javax.annotation.Nullable;
















































































@Beta
@GwtIncompatible
public abstract interface RangeSet<C extends Comparable>
{
  public abstract boolean contains(C paramC);
  
  public abstract Range<C> rangeContaining(C paramC);
  
  public abstract boolean intersects(Range<C> paramRange);
  
  public abstract boolean encloses(Range<C> paramRange);
  
  public abstract boolean enclosesAll(RangeSet<C> paramRangeSet);
  
  public boolean enclosesAll(Iterable<Range<C>> other)
  {
    for (Range<C> range : other) {
      if (!encloses(range)) {
        return false;
      }
    }
    return true;
  }
  









  public abstract boolean isEmpty();
  









  public abstract Range<C> span();
  









  public abstract Set<Range<C>> asRanges();
  








  public abstract Set<Range<C>> asDescendingSetOfRanges();
  








  public abstract RangeSet<C> complement();
  








  public abstract RangeSet<C> subRangeSet(Range<C> paramRange);
  








  public abstract void add(Range<C> paramRange);
  








  public abstract void remove(Range<C> paramRange);
  








  public abstract void clear();
  








  public abstract void addAll(RangeSet<C> paramRangeSet);
  








  public void addAll(Iterable<Range<C>> ranges)
  {
    for (Range<C> range : ranges) {
      add(range);
    }
  }
  










  public abstract void removeAll(RangeSet<C> paramRangeSet);
  










  public void removeAll(Iterable<Range<C>> ranges)
  {
    for (Range<C> range : ranges) {
      remove(range);
    }
  }
  
  public abstract boolean equals(@Nullable Object paramObject);
  
  public abstract int hashCode();
  
  public abstract String toString();
}
