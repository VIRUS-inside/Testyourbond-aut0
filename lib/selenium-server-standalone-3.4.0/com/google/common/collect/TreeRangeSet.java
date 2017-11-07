package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.annotation.Nullable;



















@Beta
@GwtIncompatible
public class TreeRangeSet<C extends Comparable<?>>
  extends AbstractRangeSet<C>
  implements Serializable
{
  @VisibleForTesting
  final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
  private transient Set<Range<C>> asRanges;
  private transient Set<Range<C>> asDescendingSetOfRanges;
  private transient RangeSet<C> complement;
  
  public static <C extends Comparable<?>> TreeRangeSet<C> create()
  {
    return new TreeRangeSet(new TreeMap());
  }
  


  public static <C extends Comparable<?>> TreeRangeSet<C> create(RangeSet<C> rangeSet)
  {
    TreeRangeSet<C> result = create();
    result.addAll(rangeSet);
    return result;
  }
  








  public static <C extends Comparable<?>> TreeRangeSet<C> create(Iterable<Range<C>> ranges)
  {
    TreeRangeSet<C> result = create();
    result.addAll(ranges);
    return result;
  }
  
  private TreeRangeSet(NavigableMap<Cut<C>, Range<C>> rangesByLowerCut) {
    rangesByLowerBound = rangesByLowerCut;
  }
  



  public Set<Range<C>> asRanges()
  {
    Set<Range<C>> result = asRanges;
    return result == null ? (this.asRanges = new AsRanges(rangesByLowerBound.values())) : result;
  }
  
  public Set<Range<C>> asDescendingSetOfRanges()
  {
    Set<Range<C>> result = asDescendingSetOfRanges;
    return result == null ? 
      (this.asDescendingSetOfRanges = new AsRanges(rangesByLowerBound.descendingMap().values())) : result;
  }
  
  final class AsRanges extends ForwardingCollection<Range<C>> implements Set<Range<C>>
  {
    final Collection<Range<C>> delegate;
    
    AsRanges()
    {
      this.delegate = delegate;
    }
    
    protected Collection<Range<C>> delegate()
    {
      return delegate;
    }
    
    public int hashCode()
    {
      return Sets.hashCodeImpl(this);
    }
    
    public boolean equals(@Nullable Object o)
    {
      return Sets.equalsImpl(this, o);
    }
  }
  
  @Nullable
  public Range<C> rangeContaining(C value)
  {
    Preconditions.checkNotNull(value);
    Map.Entry<Cut<C>, Range<C>> floorEntry = rangesByLowerBound.floorEntry(Cut.belowValue(value));
    if ((floorEntry != null) && (((Range)floorEntry.getValue()).contains(value))) {
      return (Range)floorEntry.getValue();
    }
    
    return null;
  }
  

  public boolean intersects(Range<C> range)
  {
    Preconditions.checkNotNull(range);
    Map.Entry<Cut<C>, Range<C>> ceilingEntry = rangesByLowerBound.ceilingEntry(lowerBound);
    if ((ceilingEntry != null) && 
      (((Range)ceilingEntry.getValue()).isConnected(range)) && 
      (!((Range)ceilingEntry.getValue()).intersection(range).isEmpty())) {
      return true;
    }
    Map.Entry<Cut<C>, Range<C>> priorEntry = rangesByLowerBound.lowerEntry(lowerBound);
    return (priorEntry != null) && 
      (((Range)priorEntry.getValue()).isConnected(range)) && 
      (!((Range)priorEntry.getValue()).intersection(range).isEmpty());
  }
  
  public boolean encloses(Range<C> range)
  {
    Preconditions.checkNotNull(range);
    Map.Entry<Cut<C>, Range<C>> floorEntry = rangesByLowerBound.floorEntry(lowerBound);
    return (floorEntry != null) && (((Range)floorEntry.getValue()).encloses(range));
  }
  
  @Nullable
  private Range<C> rangeEnclosing(Range<C> range) {
    Preconditions.checkNotNull(range);
    Map.Entry<Cut<C>, Range<C>> floorEntry = rangesByLowerBound.floorEntry(lowerBound);
    return (floorEntry != null) && (((Range)floorEntry.getValue()).encloses(range)) ? 
      (Range)floorEntry.getValue() : null;
  }
  

  public Range<C> span()
  {
    Map.Entry<Cut<C>, Range<C>> firstEntry = rangesByLowerBound.firstEntry();
    Map.Entry<Cut<C>, Range<C>> lastEntry = rangesByLowerBound.lastEntry();
    if (firstEntry == null) {
      throw new NoSuchElementException();
    }
    return Range.create(getValuelowerBound, getValueupperBound);
  }
  
  public void add(Range<C> rangeToAdd)
  {
    Preconditions.checkNotNull(rangeToAdd);
    
    if (rangeToAdd.isEmpty()) {
      return;
    }
    


    Cut<C> lbToAdd = lowerBound;
    Cut<C> ubToAdd = upperBound;
    
    Map.Entry<Cut<C>, Range<C>> entryBelowLB = rangesByLowerBound.lowerEntry(lbToAdd);
    if (entryBelowLB != null)
    {
      Range<C> rangeBelowLB = (Range)entryBelowLB.getValue();
      if (upperBound.compareTo(lbToAdd) >= 0)
      {
        if (upperBound.compareTo(ubToAdd) >= 0)
        {
          ubToAdd = upperBound;
        }
        



        lbToAdd = lowerBound;
      }
    }
    
    Map.Entry<Cut<C>, Range<C>> entryBelowUB = rangesByLowerBound.floorEntry(ubToAdd);
    if (entryBelowUB != null)
    {
      Range<C> rangeBelowUB = (Range)entryBelowUB.getValue();
      if (upperBound.compareTo(ubToAdd) >= 0)
      {
        ubToAdd = upperBound;
      }
    }
    

    rangesByLowerBound.subMap(lbToAdd, ubToAdd).clear();
    
    replaceRangeWithSameLowerBound(Range.create(lbToAdd, ubToAdd));
  }
  
  public void remove(Range<C> rangeToRemove)
  {
    Preconditions.checkNotNull(rangeToRemove);
    
    if (rangeToRemove.isEmpty()) {
      return;
    }
    



    Map.Entry<Cut<C>, Range<C>> entryBelowLB = rangesByLowerBound.lowerEntry(lowerBound);
    if (entryBelowLB != null)
    {
      Range<C> rangeBelowLB = (Range)entryBelowLB.getValue();
      if (upperBound.compareTo(lowerBound) >= 0)
      {
        if ((rangeToRemove.hasUpperBound()) && 
          (upperBound.compareTo(upperBound) >= 0))
        {
          replaceRangeWithSameLowerBound(
            Range.create(upperBound, upperBound));
        }
        replaceRangeWithSameLowerBound(
          Range.create(lowerBound, lowerBound));
      }
    }
    
    Map.Entry<Cut<C>, Range<C>> entryBelowUB = rangesByLowerBound.floorEntry(upperBound);
    if (entryBelowUB != null)
    {
      Range<C> rangeBelowUB = (Range)entryBelowUB.getValue();
      if ((rangeToRemove.hasUpperBound()) && 
        (upperBound.compareTo(upperBound) >= 0))
      {
        replaceRangeWithSameLowerBound(
          Range.create(upperBound, upperBound));
      }
    }
    
    rangesByLowerBound.subMap(lowerBound, upperBound).clear();
  }
  
  private void replaceRangeWithSameLowerBound(Range<C> range) {
    if (range.isEmpty()) {
      rangesByLowerBound.remove(lowerBound);
    } else {
      rangesByLowerBound.put(lowerBound, range);
    }
  }
  


  public RangeSet<C> complement()
  {
    RangeSet<C> result = complement;
    return result == null ? (this.complement = new Complement()) : result;
  }
  

  @VisibleForTesting
  static final class RangesByUpperBound<C extends Comparable<?>>
    extends AbstractNavigableMap<Cut<C>, Range<C>>
  {
    private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
    
    private final Range<Cut<C>> upperBoundWindow;
    

    RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound)
    {
      this.rangesByLowerBound = rangesByLowerBound;
      upperBoundWindow = Range.all();
    }
    
    private RangesByUpperBound(NavigableMap<Cut<C>, Range<C>> rangesByLowerBound, Range<Cut<C>> upperBoundWindow)
    {
      this.rangesByLowerBound = rangesByLowerBound;
      this.upperBoundWindow = upperBoundWindow;
    }
    
    private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
      if (window.isConnected(upperBoundWindow)) {
        return new RangesByUpperBound(rangesByLowerBound, window.intersection(upperBoundWindow));
      }
      return ImmutableSortedMap.of();
    }
    


    public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive)
    {
      return subMap(
        Range.range(fromKey, 
        BoundType.forBoolean(fromInclusive), toKey, 
        BoundType.forBoolean(toInclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive)
    {
      return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive)
    {
      return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
    }
    
    public Comparator<? super Cut<C>> comparator()
    {
      return Ordering.natural();
    }
    
    public boolean containsKey(@Nullable Object key)
    {
      return get(key) != null;
    }
    
    public Range<C> get(@Nullable Object key)
    {
      if ((key instanceof Cut)) {
        try
        {
          Cut<C> cut = (Cut)key;
          if (!upperBoundWindow.contains(cut)) {
            return null;
          }
          Map.Entry<Cut<C>, Range<C>> candidate = rangesByLowerBound.lowerEntry(cut);
          if ((candidate != null) && (getValueupperBound.equals(cut))) {
            return (Range)candidate.getValue();
          }
        } catch (ClassCastException e) {
          return null;
        }
      }
      return null;
    }
    

    Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator()
    {
      Iterator<Range<C>> backingItr;
      
      final Iterator<Range<C>> backingItr;
      
      if (!upperBoundWindow.hasLowerBound()) {
        backingItr = rangesByLowerBound.values().iterator();
      }
      else {
        Map.Entry<Cut<C>, Range<C>> lowerEntry = rangesByLowerBound.lowerEntry(upperBoundWindow.lowerEndpoint());
        Iterator<Range<C>> backingItr; if (lowerEntry == null) {
          backingItr = rangesByLowerBound.values().iterator(); } else { Iterator<Range<C>> backingItr;
          if (upperBoundWindow.lowerBound.isLessThan(getValueupperBound)) {
            backingItr = rangesByLowerBound.tailMap(lowerEntry.getKey(), true).values().iterator();

          }
          else
          {

            backingItr = rangesByLowerBound.tailMap(upperBoundWindow.lowerEndpoint(), true).values().iterator(); }
        }
      }
      new AbstractIterator()
      {
        protected Map.Entry<Cut<C>, Range<C>> computeNext() {
          if (!backingItr.hasNext()) {
            return (Map.Entry)endOfData();
          }
          Range<C> range = (Range)backingItr.next();
          if (upperBoundWindow.upperBound.isLessThan(upperBound)) {
            return (Map.Entry)endOfData();
          }
          return Maps.immutableEntry(upperBound, range);
        }
      };
    }
    
    Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator()
    {
      Collection<Range<C>> candidates;
      Collection<Range<C>> candidates;
      if (upperBoundWindow.hasUpperBound())
      {



        candidates = rangesByLowerBound.headMap(upperBoundWindow.upperEndpoint(), false).descendingMap().values();
      } else {
        candidates = rangesByLowerBound.descendingMap().values();
      }
      final PeekingIterator<Range<C>> backingItr = Iterators.peekingIterator(candidates.iterator());
      if ((backingItr.hasNext()) && 
        (upperBoundWindow.upperBound.isLessThan(peekupperBound))) {
        backingItr.next();
      }
      new AbstractIterator()
      {
        protected Map.Entry<Cut<C>, Range<C>> computeNext() {
          if (!backingItr.hasNext()) {
            return (Map.Entry)endOfData();
          }
          Range<C> range = (Range)backingItr.next();
          return upperBoundWindow.lowerBound.isLessThan(upperBound) ? 
            Maps.immutableEntry(upperBound, range) : 
            (Map.Entry)endOfData();
        }
      };
    }
    
    public int size()
    {
      if (upperBoundWindow.equals(Range.all())) {
        return rangesByLowerBound.size();
      }
      return Iterators.size(entryIterator());
    }
    
    public boolean isEmpty()
    {
      return 
      
        !entryIterator().hasNext() ? true : upperBoundWindow.equals(Range.all()) ? rangesByLowerBound.isEmpty() : false;
    }
  }
  

  private static final class ComplementRangesByLowerBound<C extends Comparable<?>>
    extends AbstractNavigableMap<Cut<C>, Range<C>>
  {
    private final NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound;
    
    private final NavigableMap<Cut<C>, Range<C>> positiveRangesByUpperBound;
    
    private final Range<Cut<C>> complementLowerBoundWindow;
    

    ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound)
    {
      this(positiveRangesByLowerBound, Range.all());
    }
    
    private ComplementRangesByLowerBound(NavigableMap<Cut<C>, Range<C>> positiveRangesByLowerBound, Range<Cut<C>> window)
    {
      this.positiveRangesByLowerBound = positiveRangesByLowerBound;
      positiveRangesByUpperBound = new TreeRangeSet.RangesByUpperBound(positiveRangesByLowerBound);
      complementLowerBoundWindow = window;
    }
    
    private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> subWindow) {
      if (!complementLowerBoundWindow.isConnected(subWindow)) {
        return ImmutableSortedMap.of();
      }
      subWindow = subWindow.intersection(complementLowerBoundWindow);
      return new ComplementRangesByLowerBound(positiveRangesByLowerBound, subWindow);
    }
    


    public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive)
    {
      return subMap(
        Range.range(fromKey, 
        BoundType.forBoolean(fromInclusive), toKey, 
        BoundType.forBoolean(toInclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive)
    {
      return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive)
    {
      return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
    }
    
    public Comparator<? super Cut<C>> comparator()
    {
      return Ordering.natural();
    }
    



    Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator()
    {
      Collection<Range<C>> positiveRanges;
      


      Collection<Range<C>> positiveRanges;
      

      if (complementLowerBoundWindow.hasLowerBound())
      {




        positiveRanges = positiveRangesByUpperBound.tailMap(complementLowerBoundWindow.lowerEndpoint(), complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values();
      } else {
        positiveRanges = positiveRangesByUpperBound.values();
      }
      
      final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(positiveRanges.iterator());
      Cut<C> firstComplementRangeLowerBound;
      if ((complementLowerBoundWindow.contains(Cut.belowAll())) && (
        (!positiveItr.hasNext()) || (peeklowerBound != Cut.belowAll()))) {
        firstComplementRangeLowerBound = Cut.belowAll(); } else { Cut<C> firstComplementRangeLowerBound;
        if (positiveItr.hasNext()) {
          firstComplementRangeLowerBound = nextupperBound;
        } else
          return Iterators.emptyIterator(); }
      final Cut<C> firstComplementRangeLowerBound;
      new AbstractIterator() {
        Cut<C> nextComplementRangeLowerBound = firstComplementRangeLowerBound;
        
        protected Map.Entry<Cut<C>, Range<C>> computeNext()
        {
          if ((complementLowerBoundWindow.upperBound.isLessThan(nextComplementRangeLowerBound)) || 
            (nextComplementRangeLowerBound == Cut.aboveAll())) {
            return (Map.Entry)endOfData();
          }
          Range<C> negativeRange;
          if (positiveItr.hasNext()) {
            Range<C> positiveRange = (Range)positiveItr.next();
            Range<C> negativeRange = Range.create(nextComplementRangeLowerBound, lowerBound);
            nextComplementRangeLowerBound = upperBound;
          } else {
            negativeRange = Range.create(nextComplementRangeLowerBound, Cut.aboveAll());
            nextComplementRangeLowerBound = Cut.aboveAll();
          }
          return Maps.immutableEntry(lowerBound, negativeRange);
        }
      };
    }
    











    Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator()
    {
      Cut<C> startingPoint = complementLowerBoundWindow.hasUpperBound() ? (Cut)complementLowerBoundWindow.upperEndpoint() : Cut.aboveAll();
      

      boolean inclusive = (complementLowerBoundWindow.hasUpperBound()) && (complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED);
      
      final PeekingIterator<Range<C>> positiveItr = Iterators.peekingIterator(positiveRangesByUpperBound
      
        .headMap(startingPoint, inclusive)
        .descendingMap()
        .values()
        .iterator());
      Cut<C> cut;
      Cut<C> cut; if (positiveItr.hasNext())
      {


        cut = peekupperBound == Cut.aboveAll() ? nextlowerBound : (Cut)positiveRangesByLowerBound.higherKey(peekupperBound);
      } else { if ((!complementLowerBoundWindow.contains(Cut.belowAll())) || 
          (positiveRangesByLowerBound.containsKey(Cut.belowAll()))) {
          return Iterators.emptyIterator();
        }
        cut = (Cut)positiveRangesByLowerBound.higherKey(Cut.belowAll());
      }
      
      final Cut<C> firstComplementRangeUpperBound = (Cut)MoreObjects.firstNonNull(cut, Cut.aboveAll());
      new AbstractIterator() {
        Cut<C> nextComplementRangeUpperBound = firstComplementRangeUpperBound;
        
        protected Map.Entry<Cut<C>, Range<C>> computeNext()
        {
          if (nextComplementRangeUpperBound == Cut.belowAll())
            return (Map.Entry)endOfData();
          if (positiveItr.hasNext()) {
            Range<C> positiveRange = (Range)positiveItr.next();
            
            Range<C> negativeRange = Range.create(upperBound, nextComplementRangeUpperBound);
            nextComplementRangeUpperBound = lowerBound;
            if (complementLowerBoundWindow.lowerBound.isLessThan(lowerBound)) {
              return Maps.immutableEntry(lowerBound, negativeRange);
            }
          } else if (complementLowerBoundWindow.lowerBound.isLessThan(Cut.belowAll())) {
            Range<C> negativeRange = Range.create(Cut.belowAll(), nextComplementRangeUpperBound);
            nextComplementRangeUpperBound = Cut.belowAll();
            return Maps.immutableEntry(Cut.belowAll(), negativeRange);
          }
          return (Map.Entry)endOfData();
        }
      };
    }
    
    public int size()
    {
      return Iterators.size(entryIterator());
    }
    
    @Nullable
    public Range<C> get(Object key)
    {
      if ((key instanceof Cut)) {
        try
        {
          Cut<C> cut = (Cut)key;
          
          Map.Entry<Cut<C>, Range<C>> firstEntry = tailMap(cut, true).firstEntry();
          if ((firstEntry != null) && (((Cut)firstEntry.getKey()).equals(cut))) {
            return (Range)firstEntry.getValue();
          }
        } catch (ClassCastException e) {
          return null;
        }
      }
      return null;
    }
    
    public boolean containsKey(Object key)
    {
      return get(key) != null;
    }
  }
  
  private final class Complement extends TreeRangeSet<C> {
    Complement() {
      super(null);
    }
    
    public void add(Range<C> rangeToAdd)
    {
      TreeRangeSet.this.remove(rangeToAdd);
    }
    
    public void remove(Range<C> rangeToRemove)
    {
      TreeRangeSet.this.add(rangeToRemove);
    }
    
    public boolean contains(C value)
    {
      return !TreeRangeSet.this.contains(value);
    }
    
    public RangeSet<C> complement()
    {
      return TreeRangeSet.this;
    }
  }
  



  private static final class SubRangeSetRangesByLowerBound<C extends Comparable<?>>
    extends AbstractNavigableMap<Cut<C>, Range<C>>
  {
    private final Range<Cut<C>> lowerBoundWindow;
    

    private final Range<C> restriction;
    

    private final NavigableMap<Cut<C>, Range<C>> rangesByLowerBound;
    

    private final NavigableMap<Cut<C>, Range<C>> rangesByUpperBound;
    


    private SubRangeSetRangesByLowerBound(Range<Cut<C>> lowerBoundWindow, Range<C> restriction, NavigableMap<Cut<C>, Range<C>> rangesByLowerBound)
    {
      this.lowerBoundWindow = ((Range)Preconditions.checkNotNull(lowerBoundWindow));
      this.restriction = ((Range)Preconditions.checkNotNull(restriction));
      this.rangesByLowerBound = ((NavigableMap)Preconditions.checkNotNull(rangesByLowerBound));
      rangesByUpperBound = new TreeRangeSet.RangesByUpperBound(rangesByLowerBound);
    }
    
    private NavigableMap<Cut<C>, Range<C>> subMap(Range<Cut<C>> window) {
      if (!window.isConnected(lowerBoundWindow)) {
        return ImmutableSortedMap.of();
      }
      return new SubRangeSetRangesByLowerBound(lowerBoundWindow
        .intersection(window), restriction, rangesByLowerBound);
    }
    


    public NavigableMap<Cut<C>, Range<C>> subMap(Cut<C> fromKey, boolean fromInclusive, Cut<C> toKey, boolean toInclusive)
    {
      return subMap(
        Range.range(fromKey, 
        
        BoundType.forBoolean(fromInclusive), toKey, 
        
        BoundType.forBoolean(toInclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> headMap(Cut<C> toKey, boolean inclusive)
    {
      return subMap(Range.upTo(toKey, BoundType.forBoolean(inclusive)));
    }
    
    public NavigableMap<Cut<C>, Range<C>> tailMap(Cut<C> fromKey, boolean inclusive)
    {
      return subMap(Range.downTo(fromKey, BoundType.forBoolean(inclusive)));
    }
    
    public Comparator<? super Cut<C>> comparator()
    {
      return Ordering.natural();
    }
    
    public boolean containsKey(@Nullable Object key)
    {
      return get(key) != null;
    }
    
    @Nullable
    public Range<C> get(@Nullable Object key)
    {
      if ((key instanceof Cut)) {
        try
        {
          Cut<C> cut = (Cut)key;
          if ((!lowerBoundWindow.contains(cut)) || 
            (cut.compareTo(restriction.lowerBound) < 0) || 
            (cut.compareTo(restriction.upperBound) >= 0))
            return null;
          if (cut.equals(restriction.lowerBound))
          {
            Range<C> candidate = (Range)Maps.valueOrNull(rangesByLowerBound.floorEntry(cut));
            if ((candidate != null) && (upperBound.compareTo(restriction.lowerBound) > 0)) {
              return candidate.intersection(restriction);
            }
          } else {
            Range<C> result = (Range)rangesByLowerBound.get(cut);
            if (result != null) {
              return result.intersection(restriction);
            }
          }
        } catch (ClassCastException e) {
          return null;
        }
      }
      return null;
    }
    
    Iterator<Map.Entry<Cut<C>, Range<C>>> entryIterator()
    {
      if (restriction.isEmpty()) {
        return Iterators.emptyIterator();
      }
      
      if (lowerBoundWindow.upperBound.isLessThan(restriction.lowerBound))
        return Iterators.emptyIterator();
      Iterator<Range<C>> completeRangeItr; final Iterator<Range<C>> completeRangeItr; if (lowerBoundWindow.lowerBound.isLessThan(restriction.lowerBound))
      {

        completeRangeItr = rangesByUpperBound.tailMap(restriction.lowerBound, false).values().iterator();



      }
      else
      {


        completeRangeItr = rangesByLowerBound.tailMap(lowerBoundWindow.lowerBound.endpoint(), lowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values().iterator();
      }
      

      final Cut<Cut<C>> upperBoundOnLowerBounds = (Cut)Ordering.natural().min(lowerBoundWindow.upperBound, Cut.belowValue(restriction.upperBound));
      new AbstractIterator()
      {
        protected Map.Entry<Cut<C>, Range<C>> computeNext() {
          if (!completeRangeItr.hasNext()) {
            return (Map.Entry)endOfData();
          }
          Range<C> nextRange = (Range)completeRangeItr.next();
          if (upperBoundOnLowerBounds.isLessThan(lowerBound)) {
            return (Map.Entry)endOfData();
          }
          nextRange = nextRange.intersection(restriction);
          return Maps.immutableEntry(lowerBound, nextRange);
        }
      };
    }
    

    Iterator<Map.Entry<Cut<C>, Range<C>>> descendingEntryIterator()
    {
      if (restriction.isEmpty()) {
        return Iterators.emptyIterator();
      }
      

      Cut<Cut<C>> upperBoundOnLowerBounds = (Cut)Ordering.natural().min(lowerBoundWindow.upperBound, Cut.belowValue(restriction.upperBound));
      






      final Iterator<Range<C>> completeRangeItr = rangesByLowerBound.headMap(upperBoundOnLowerBounds.endpoint(), upperBoundOnLowerBounds.typeAsUpperBound() == BoundType.CLOSED).descendingMap().values().iterator();
      new AbstractIterator()
      {
        protected Map.Entry<Cut<C>, Range<C>> computeNext() {
          if (!completeRangeItr.hasNext()) {
            return (Map.Entry)endOfData();
          }
          Range<C> nextRange = (Range)completeRangeItr.next();
          if (restriction.lowerBound.compareTo(upperBound) >= 0) {
            return (Map.Entry)endOfData();
          }
          nextRange = nextRange.intersection(restriction);
          if (lowerBoundWindow.contains(lowerBound)) {
            return Maps.immutableEntry(lowerBound, nextRange);
          }
          return (Map.Entry)endOfData();
        }
      };
    }
    

    public int size()
    {
      return Iterators.size(entryIterator());
    }
  }
  
  public RangeSet<C> subRangeSet(Range<C> view)
  {
    return view.equals(Range.all()) ? this : new SubRangeSet(view);
  }
  
  private final class SubRangeSet extends TreeRangeSet<C> {
    private final Range<C> restriction;
    
    SubRangeSet() {
      super(
      
        null);
      this.restriction = restriction;
    }
    
    public boolean encloses(Range<C> range)
    {
      if ((!restriction.isEmpty()) && (restriction.encloses(range))) {
        Range<C> enclosing = TreeRangeSet.this.rangeEnclosing(range);
        return (enclosing != null) && (!enclosing.intersection(restriction).isEmpty());
      }
      return false;
    }
    
    @Nullable
    public Range<C> rangeContaining(C value)
    {
      if (!restriction.contains(value)) {
        return null;
      }
      Range<C> result = TreeRangeSet.this.rangeContaining(value);
      return result == null ? null : result.intersection(restriction);
    }
    
    public void add(Range<C> rangeToAdd)
    {
      Preconditions.checkArgument(
        restriction.encloses(rangeToAdd), "Cannot add range %s to subRangeSet(%s)", rangeToAdd, restriction);
      


      super.add(rangeToAdd);
    }
    
    public void remove(Range<C> rangeToRemove)
    {
      if (rangeToRemove.isConnected(restriction)) {
        TreeRangeSet.this.remove(rangeToRemove.intersection(restriction));
      }
    }
    
    public boolean contains(C value)
    {
      return (restriction.contains(value)) && (TreeRangeSet.this.contains(value));
    }
    
    public void clear()
    {
      TreeRangeSet.this.remove(restriction);
    }
    
    public RangeSet<C> subRangeSet(Range<C> view)
    {
      if (view.encloses(restriction))
        return this;
      if (view.isConnected(restriction)) {
        return new SubRangeSet(this, restriction.intersection(view));
      }
      return ImmutableRangeSet.of();
    }
  }
}
