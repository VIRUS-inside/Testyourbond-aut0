package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import javax.annotation.Nullable;











































@GwtCompatible(emulated=true)
public final class Iterators
{
  static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR = new UnmodifiableListIterator()
  {
    public boolean hasNext()
    {
      return false;
    }
    
    public Object next()
    {
      throw new NoSuchElementException();
    }
    
    public boolean hasPrevious()
    {
      return false;
    }
    
    public Object previous()
    {
      throw new NoSuchElementException();
    }
    
    public int nextIndex()
    {
      return 0;
    }
    
    public int previousIndex()
    {
      return -1;
    }
  };
  


  private Iterators() {}
  

  static <T> UnmodifiableIterator<T> emptyIterator()
  {
    return emptyListIterator();
  }
  







  static <T> UnmodifiableListIterator<T> emptyListIterator()
  {
    return EMPTY_LIST_ITERATOR;
  }
  
  private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator()
  {
    public boolean hasNext()
    {
      return false;
    }
    
    public Object next()
    {
      throw new NoSuchElementException();
    }
    
    public void remove()
    {
      CollectPreconditions.checkRemove(false);
    }
  };
  







  static <T> Iterator<T> emptyModifiableIterator()
  {
    return EMPTY_MODIFIABLE_ITERATOR;
  }
  

  public static <T> UnmodifiableIterator<T> unmodifiableIterator(Iterator<? extends T> iterator)
  {
    Preconditions.checkNotNull(iterator);
    if ((iterator instanceof UnmodifiableIterator))
    {
      UnmodifiableIterator<T> result = (UnmodifiableIterator)iterator;
      return result;
    }
    new UnmodifiableIterator()
    {
      public boolean hasNext() {
        return val$iterator.hasNext();
      }
      
      public T next()
      {
        return val$iterator.next();
      }
    };
  }
  





  @Deprecated
  public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator)
  {
    return (UnmodifiableIterator)Preconditions.checkNotNull(iterator);
  }
  




  public static int size(Iterator<?> iterator)
  {
    long count = 0L;
    while (iterator.hasNext()) {
      iterator.next();
      count += 1L;
    }
    return Ints.saturatedCast(count);
  }
  


  public static boolean contains(Iterator<?> iterator, @Nullable Object element)
  {
    return any(iterator, Predicates.equalTo(element));
  }
  








  @CanIgnoreReturnValue
  public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove)
  {
    return removeIf(removeFrom, Predicates.in(elementsToRemove));
  }
  










  @CanIgnoreReturnValue
  public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate)
  {
    Preconditions.checkNotNull(predicate);
    boolean modified = false;
    while (removeFrom.hasNext()) {
      if (predicate.apply(removeFrom.next())) {
        removeFrom.remove();
        modified = true;
      }
    }
    return modified;
  }
  








  @CanIgnoreReturnValue
  public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain)
  {
    return removeIf(removeFrom, Predicates.not(Predicates.in(elementsToRetain)));
  }
  









  public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2)
  {
    while (iterator1.hasNext()) {
      if (!iterator2.hasNext()) {
        return false;
      }
      Object o1 = iterator1.next();
      Object o2 = iterator2.next();
      if (!Objects.equal(o1, o2)) {
        return false;
      }
    }
    return !iterator2.hasNext();
  }
  




  public static String toString(Iterator<?> iterator)
  {
    return 
      ']';
  }
  








  @CanIgnoreReturnValue
  public static <T> T getOnlyElement(Iterator<T> iterator)
  {
    T first = iterator.next();
    if (!iterator.hasNext()) {
      return first;
    }
    
    StringBuilder sb = new StringBuilder().append("expected one element but was: <").append(first);
    for (int i = 0; (i < 4) && (iterator.hasNext()); i++) {
      sb.append(", ").append(iterator.next());
    }
    if (iterator.hasNext()) {
      sb.append(", ...");
    }
    sb.append('>');
    
    throw new IllegalArgumentException(sb.toString());
  }
  






  @Nullable
  @CanIgnoreReturnValue
  public static <T> T getOnlyElement(Iterator<? extends T> iterator, @Nullable T defaultValue)
  {
    return iterator.hasNext() ? getOnlyElement(iterator) : defaultValue;
  }
  








  @GwtIncompatible
  public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type)
  {
    List<T> list = Lists.newArrayList(iterator);
    return Iterables.toArray(list, type);
  }
  







  @CanIgnoreReturnValue
  public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator)
  {
    Preconditions.checkNotNull(addTo);
    Preconditions.checkNotNull(iterator);
    boolean wasModified = false;
    while (iterator.hasNext()) {
      wasModified |= addTo.add(iterator.next());
    }
    return wasModified;
  }
  






  public static int frequency(Iterator<?> iterator, @Nullable Object element)
  {
    return size(filter(iterator, Predicates.equalTo(element)));
  }
  













  public static <T> Iterator<T> cycle(Iterable<T> iterable)
  {
    Preconditions.checkNotNull(iterable);
    new Iterator() {
      Iterator<T> iterator = Iterators.emptyModifiableIterator();
      









      public boolean hasNext()
      {
        return (iterator.hasNext()) || (val$iterable.iterator().hasNext());
      }
      
      public T next()
      {
        if (!iterator.hasNext()) {
          iterator = val$iterable.iterator();
          if (!iterator.hasNext()) {
            throw new NoSuchElementException();
          }
        }
        return iterator.next();
      }
      
      public void remove()
      {
        iterator.remove();
      }
    };
  }
  












  @SafeVarargs
  public static <T> Iterator<T> cycle(T... elements)
  {
    return cycle(Lists.newArrayList(elements));
  }
  







  public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b)
  {
    Preconditions.checkNotNull(a);
    Preconditions.checkNotNull(b);
    return concat(new ConsumingQueueIterator(new Iterator[] { a, b }));
  }
  









  public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c)
  {
    Preconditions.checkNotNull(a);
    Preconditions.checkNotNull(b);
    Preconditions.checkNotNull(c);
    return concat(new ConsumingQueueIterator(new Iterator[] { a, b, c }));
  }
  












  public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d)
  {
    Preconditions.checkNotNull(a);
    Preconditions.checkNotNull(b);
    Preconditions.checkNotNull(c);
    Preconditions.checkNotNull(d);
    return concat(new ConsumingQueueIterator(new Iterator[] { a, b, c, d }));
  }
  









  public static <T> Iterator<T> concat(Iterator<? extends T>... inputs)
  {
    for (Iterator<? extends T> input : (Iterator[])Preconditions.checkNotNull(inputs)) {
      Preconditions.checkNotNull(input);
    }
    return concat(new ConsumingQueueIterator(inputs));
  }
  








  public static <T> Iterator<T> concat(Iterator<? extends Iterator<? extends T>> inputs)
  {
    return new ConcatenatedIterator(inputs);
  }
  














  public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size)
  {
    return partitionImpl(iterator, size, false);
  }
  















  public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size)
  {
    return partitionImpl(iterator, size, true);
  }
  
  private static <T> UnmodifiableIterator<List<T>> partitionImpl(Iterator<T> iterator, final int size, final boolean pad)
  {
    Preconditions.checkNotNull(iterator);
    Preconditions.checkArgument(size > 0);
    new UnmodifiableIterator()
    {
      public boolean hasNext() {
        return val$iterator.hasNext();
      }
      
      public List<T> next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Object[] array = new Object[size];
        for (int count = 0; 
            (count < size) && (val$iterator.hasNext()); count++) {
          array[count] = val$iterator.next();
        }
        for (int i = count; i < size; i++) {
          array[i] = null;
        }
        

        List<T> list = Collections.unmodifiableList(Arrays.asList(array));
        return (pad) || (count == size) ? list : list.subList(0, count);
      }
    };
  }
  




  public static <T> UnmodifiableIterator<T> filter(Iterator<T> unfiltered, final Predicate<? super T> retainIfTrue)
  {
    Preconditions.checkNotNull(unfiltered);
    Preconditions.checkNotNull(retainIfTrue);
    new AbstractIterator()
    {
      protected T computeNext() {
        while (val$unfiltered.hasNext()) {
          T element = val$unfiltered.next();
          if (retainIfTrue.apply(element)) {
            return element;
          }
        }
        return endOfData();
      }
    };
  }
  




  @GwtIncompatible
  public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> desiredType)
  {
    return filter(unfiltered, Predicates.instanceOf(desiredType));
  }
  



  public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate)
  {
    return indexOf(iterator, predicate) != -1;
  }
  




  public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate)
  {
    Preconditions.checkNotNull(predicate);
    while (iterator.hasNext()) {
      T element = iterator.next();
      if (!predicate.apply(element)) {
        return false;
      }
    }
    return true;
  }
  










  public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate)
  {
    return filter(iterator, predicate).next();
  }
  










  @Nullable
  public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, @Nullable T defaultValue)
  {
    return getNext(filter(iterator, predicate), defaultValue);
  }
  












  public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate)
  {
    UnmodifiableIterator<T> filteredIterator = filter(iterator, predicate);
    return filteredIterator.hasNext() ? Optional.of(filteredIterator.next()) : Optional.absent();
  }
  















  public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate)
  {
    Preconditions.checkNotNull(predicate, "predicate");
    for (int i = 0; iterator.hasNext(); i++) {
      T current = iterator.next();
      if (predicate.apply(current)) {
        return i;
      }
    }
    return -1;
  }
  








  public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function)
  {
    Preconditions.checkNotNull(function);
    new TransformedIterator(fromIterator)
    {
      T transform(F from) {
        return function.apply(from);
      }
    };
  }
  









  public static <T> T get(Iterator<T> iterator, int position)
  {
    checkNonnegative(position);
    int skipped = advance(iterator, position);
    if (!iterator.hasNext()) {
      throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
    }
    




    return iterator.next();
  }
  
  static void checkNonnegative(int position) {
    if (position < 0) {
      throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
    }
  }
  














  @Nullable
  public static <T> T get(Iterator<? extends T> iterator, int position, @Nullable T defaultValue)
  {
    checkNonnegative(position);
    advance(iterator, position);
    return getNext(iterator, defaultValue);
  }
  








  @Nullable
  public static <T> T getNext(Iterator<? extends T> iterator, @Nullable T defaultValue)
  {
    return iterator.hasNext() ? iterator.next() : defaultValue;
  }
  




  public static <T> T getLast(Iterator<T> iterator)
  {
    for (;;)
    {
      T current = iterator.next();
      if (!iterator.hasNext()) {
        return current;
      }
    }
  }
  







  @Nullable
  public static <T> T getLast(Iterator<? extends T> iterator, @Nullable T defaultValue)
  {
    return iterator.hasNext() ? getLast(iterator) : defaultValue;
  }
  






  @CanIgnoreReturnValue
  public static int advance(Iterator<?> iterator, int numberToAdvance)
  {
    Preconditions.checkNotNull(iterator);
    Preconditions.checkArgument(numberToAdvance >= 0, "numberToAdvance must be nonnegative");
    

    for (int i = 0; (i < numberToAdvance) && (iterator.hasNext()); i++) {
      iterator.next();
    }
    return i;
  }
  










  public static <T> Iterator<T> limit(final Iterator<T> iterator, int limitSize)
  {
    Preconditions.checkNotNull(iterator);
    Preconditions.checkArgument(limitSize >= 0, "limit is negative");
    new Iterator()
    {
      private int count;
      
      public boolean hasNext() {
        return (count < val$limitSize) && (iterator.hasNext());
      }
      
      public T next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        count += 1;
        return iterator.next();
      }
      
      public void remove()
      {
        iterator.remove();
      }
    };
  }
  












  public static <T> Iterator<T> consumingIterator(Iterator<T> iterator)
  {
    Preconditions.checkNotNull(iterator);
    new UnmodifiableIterator()
    {
      public boolean hasNext() {
        return val$iterator.hasNext();
      }
      
      public T next()
      {
        T next = val$iterator.next();
        val$iterator.remove();
        return next;
      }
      
      public String toString()
      {
        return "Iterators.consumingIterator(...)";
      }
    };
  }
  



  @Nullable
  static <T> T pollNext(Iterator<T> iterator)
  {
    if (iterator.hasNext()) {
      T result = iterator.next();
      iterator.remove();
      return result;
    }
    return null;
  }
  





  static void clear(Iterator<?> iterator)
  {
    Preconditions.checkNotNull(iterator);
    while (iterator.hasNext()) {
      iterator.next();
      iterator.remove();
    }
  }
  












  @SafeVarargs
  public static <T> UnmodifiableIterator<T> forArray(T... array)
  {
    return forArray(array, 0, array.length, 0);
  }
  







  static <T> UnmodifiableListIterator<T> forArray(final T[] array, final int offset, int length, int index)
  {
    Preconditions.checkArgument(length >= 0);
    int end = offset + length;
    

    Preconditions.checkPositionIndexes(offset, end, array.length);
    Preconditions.checkPositionIndex(index, length);
    if (length == 0) {
      return emptyListIterator();
    }
    





    new AbstractIndexedListIterator(length, index)
    {
      protected T get(int index) {
        return array[(offset + index)];
      }
    };
  }
  





  public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable T value)
  {
    new UnmodifiableIterator()
    {
      boolean done;
      
      public boolean hasNext() {
        return !done;
      }
      
      public T next()
      {
        if (done) {
          throw new NoSuchElementException();
        }
        done = true;
        return val$value;
      }
    };
  }
  







  public static <T> UnmodifiableIterator<T> forEnumeration(Enumeration<T> enumeration)
  {
    Preconditions.checkNotNull(enumeration);
    new UnmodifiableIterator()
    {
      public boolean hasNext() {
        return val$enumeration.hasMoreElements();
      }
      
      public T next()
      {
        return val$enumeration.nextElement();
      }
    };
  }
  






  public static <T> Enumeration<T> asEnumeration(Iterator<T> iterator)
  {
    Preconditions.checkNotNull(iterator);
    new Enumeration()
    {
      public boolean hasMoreElements() {
        return val$iterator.hasNext();
      }
      
      public T nextElement()
      {
        return val$iterator.next();
      }
    };
  }
  

  private static class PeekingImpl<E>
    implements PeekingIterator<E>
  {
    private final Iterator<? extends E> iterator;
    private boolean hasPeeked;
    private E peekedElement;
    
    public PeekingImpl(Iterator<? extends E> iterator)
    {
      this.iterator = ((Iterator)Preconditions.checkNotNull(iterator));
    }
    
    public boolean hasNext()
    {
      return (hasPeeked) || (iterator.hasNext());
    }
    
    public E next()
    {
      if (!hasPeeked) {
        return iterator.next();
      }
      E result = peekedElement;
      hasPeeked = false;
      peekedElement = null;
      return result;
    }
    
    public void remove()
    {
      Preconditions.checkState(!hasPeeked, "Can't remove after you've peeked at next");
      iterator.remove();
    }
    
    public E peek()
    {
      if (!hasPeeked) {
        peekedElement = iterator.next();
        hasPeeked = true;
      }
      return peekedElement;
    }
  }
  





































  public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator)
  {
    if ((iterator instanceof PeekingImpl))
    {


      PeekingImpl<T> peeking = (PeekingImpl)iterator;
      return peeking;
    }
    return new PeekingImpl(iterator);
  }
  





  @Deprecated
  public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator)
  {
    return (PeekingIterator)Preconditions.checkNotNull(iterator);
  }
  













  @Beta
  public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator)
  {
    Preconditions.checkNotNull(iterators, "iterators");
    Preconditions.checkNotNull(comparator, "comparator");
    
    return new MergingIterator(iterators, comparator);
  }
  





  private static class MergingIterator<T>
    extends UnmodifiableIterator<T>
  {
    final Queue<PeekingIterator<T>> queue;
    





    public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator)
    {
      Comparator<PeekingIterator<T>> heapComparator = new Comparator()
      {
        public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2)
        {
          return itemComparator.compare(o1.peek(), o2.peek());
        }
        
      };
      queue = new PriorityQueue(2, heapComparator);
      
      for (Iterator<? extends T> iterator : iterators) {
        if (iterator.hasNext()) {
          queue.add(Iterators.peekingIterator(iterator));
        }
      }
    }
    
    public boolean hasNext()
    {
      return !queue.isEmpty();
    }
    
    public T next()
    {
      PeekingIterator<T> nextIter = (PeekingIterator)queue.remove();
      T next = nextIter.next();
      if (nextIter.hasNext()) {
        queue.add(nextIter);
      }
      return next;
    }
  }
  
  private static class ConcatenatedIterator<T> extends MultitransformedIterator<Iterator<? extends T>, T>
  {
    public ConcatenatedIterator(Iterator<? extends Iterator<? extends T>> iterators)
    {
      super();
    }
    
    Iterator<? extends T> transform(Iterator<? extends T> iterator)
    {
      return iterator;
    }
    




    private static <T> Iterator<Iterator<? extends T>> getComponentIterators(Iterator<? extends Iterator<? extends T>> iterators)
    {
      new MultitransformedIterator(iterators)
      {
        Iterator<? extends Iterator<? extends T>> transform(Iterator<? extends T> iterator) {
          if ((iterator instanceof Iterators.ConcatenatedIterator)) {
            Iterators.ConcatenatedIterator<? extends T> concatIterator = (Iterators.ConcatenatedIterator)iterator;
            
            return Iterators.ConcatenatedIterator.getComponentIterators(backingIterator);
          }
          return Iterators.singletonIterator(iterator);
        }
      };
    }
  }
  



  static <T> ListIterator<T> cast(Iterator<T> iterator)
  {
    return (ListIterator)iterator;
  }
}
