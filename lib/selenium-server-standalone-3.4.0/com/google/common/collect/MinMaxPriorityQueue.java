package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.j2objc.annotations.Weak;
import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;










































































@Beta
@GwtCompatible
public final class MinMaxPriorityQueue<E>
  extends AbstractQueue<E>
{
  private final MinMaxPriorityQueue<E>.Heap minHeap;
  private final MinMaxPriorityQueue<E>.Heap maxHeap;
  @VisibleForTesting
  final int maximumSize;
  private Object[] queue;
  private int size;
  private int modCount;
  private static final int EVEN_POWERS_OF_TWO = 1431655765;
  private static final int ODD_POWERS_OF_TWO = -1431655766;
  private static final int DEFAULT_CAPACITY = 11;
  
  public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create()
  {
    return new Builder(Ordering.natural(), null).create();
  }
  




  public static <E extends Comparable<E>> MinMaxPriorityQueue<E> create(Iterable<? extends E> initialContents)
  {
    return new Builder(Ordering.natural(), null).create(initialContents);
  }
  




  public static <B> Builder<B> orderedBy(Comparator<B> comparator)
  {
    return new Builder(comparator, null);
  }
  




  public static Builder<Comparable> expectedSize(int expectedSize)
  {
    return new Builder(Ordering.natural(), null).expectedSize(expectedSize);
  }
  






  public static Builder<Comparable> maximumSize(int maximumSize)
  {
    return new Builder(Ordering.natural(), null).maximumSize(maximumSize);
  }
  






  @Beta
  public static final class Builder<B>
  {
    private static final int UNSET_EXPECTED_SIZE = -1;
    





    private final Comparator<B> comparator;
    




    private int expectedSize = -1;
    private int maximumSize = Integer.MAX_VALUE;
    
    private Builder(Comparator<B> comparator) {
      this.comparator = ((Comparator)Preconditions.checkNotNull(comparator));
    }
    



    @CanIgnoreReturnValue
    public Builder<B> expectedSize(int expectedSize)
    {
      Preconditions.checkArgument(expectedSize >= 0);
      this.expectedSize = expectedSize;
      return this;
    }
    





    @CanIgnoreReturnValue
    public Builder<B> maximumSize(int maximumSize)
    {
      Preconditions.checkArgument(maximumSize > 0);
      this.maximumSize = maximumSize;
      return this;
    }
    



    public <T extends B> MinMaxPriorityQueue<T> create()
    {
      return create(Collections.emptySet());
    }
    





    public <T extends B> MinMaxPriorityQueue<T> create(Iterable<? extends T> initialContents)
    {
      MinMaxPriorityQueue<T> queue = new MinMaxPriorityQueue(this, MinMaxPriorityQueue.initialQueueSize(expectedSize, maximumSize, initialContents), null);
      for (T element : initialContents) {
        queue.offer(element);
      }
      return queue;
    }
    
    private <T extends B> Ordering<T> ordering()
    {
      return Ordering.from(comparator);
    }
  }
  






  private MinMaxPriorityQueue(Builder<? super E> builder, int queueSize)
  {
    Ordering<E> ordering = builder.ordering();
    minHeap = new Heap(ordering);
    maxHeap = new Heap(ordering.reverse());
    minHeap.otherHeap = maxHeap;
    maxHeap.otherHeap = minHeap;
    
    maximumSize = maximumSize;
    
    queue = new Object[queueSize];
  }
  
  public int size()
  {
    return size;
  }
  








  @CanIgnoreReturnValue
  public boolean add(E element)
  {
    offer(element);
    return true;
  }
  
  @CanIgnoreReturnValue
  public boolean addAll(Collection<? extends E> newElements)
  {
    boolean modified = false;
    for (E element : newElements) {
      offer(element);
      modified = true;
    }
    return modified;
  }
  






  @CanIgnoreReturnValue
  public boolean offer(E element)
  {
    Preconditions.checkNotNull(element);
    modCount += 1;
    int insertIndex = size++;
    
    growIfNeeded();
    


    heapForIndex(insertIndex).bubbleUp(insertIndex, element);
    return (size <= maximumSize) || (pollLast() != element);
  }
  
  @CanIgnoreReturnValue
  public E poll()
  {
    return isEmpty() ? null : removeAndGet(0);
  }
  
  E elementData(int index)
  {
    return queue[index];
  }
  
  public E peek()
  {
    return isEmpty() ? null : elementData(0);
  }
  


  private int getMaxElementIndex()
  {
    switch (size) {
    case 1: 
      return 0;
    case 2: 
      return 1;
    }
    
    
    return maxHeap.compareElements(1, 2) <= 0 ? 1 : 2;
  }
  




  @CanIgnoreReturnValue
  public E pollFirst()
  {
    return poll();
  }
  




  @CanIgnoreReturnValue
  public E removeFirst()
  {
    return remove();
  }
  



  public E peekFirst()
  {
    return peek();
  }
  



  @CanIgnoreReturnValue
  public E pollLast()
  {
    return isEmpty() ? null : removeAndGet(getMaxElementIndex());
  }
  




  @CanIgnoreReturnValue
  public E removeLast()
  {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return removeAndGet(getMaxElementIndex());
  }
  



  public E peekLast()
  {
    return isEmpty() ? null : elementData(getMaxElementIndex());
  }
  














  @VisibleForTesting
  @CanIgnoreReturnValue
  MoveDesc<E> removeAt(int index)
  {
    Preconditions.checkPositionIndex(index, size);
    modCount += 1;
    size -= 1;
    if (size == index) {
      queue[size] = null;
      return null;
    }
    E actualLastElement = elementData(size);
    int lastElementAt = heapForIndex(size).swapWithConceptuallyLastElement(actualLastElement);
    if (lastElementAt == index)
    {


      queue[size] = null;
      return null;
    }
    E toTrickle = elementData(size);
    queue[size] = null;
    MoveDesc<E> changes = fillHole(index, toTrickle);
    if (lastElementAt < index)
    {
      if (changes == null)
      {
        return new MoveDesc(actualLastElement, toTrickle);
      }
      

      return new MoveDesc(actualLastElement, replaced);
    }
    

    return changes;
  }
  
  private MoveDesc<E> fillHole(int index, E toTrickle) {
    MinMaxPriorityQueue<E>.Heap heap = heapForIndex(index);
    






    int vacated = heap.fillHoleAt(index);
    
    int bubbledTo = heap.bubbleUpAlternatingLevels(vacated, toTrickle);
    if (bubbledTo == vacated)
    {


      return heap.tryCrossOverAndBubbleUp(index, vacated, toTrickle);
    }
    return bubbledTo < index ? new MoveDesc(toTrickle, elementData(index)) : null;
  }
  
  static class MoveDesc<E>
  {
    final E toTrickle;
    final E replaced;
    
    MoveDesc(E toTrickle, E replaced)
    {
      this.toTrickle = toTrickle;
      this.replaced = replaced;
    }
  }
  


  private E removeAndGet(int index)
  {
    E value = elementData(index);
    removeAt(index);
    return value;
  }
  
  private MinMaxPriorityQueue<E>.Heap heapForIndex(int i) {
    return isEvenLevel(i) ? minHeap : maxHeap;
  }
  


  @VisibleForTesting
  static boolean isEvenLevel(int index)
  {
    int oneBased = index + 1 ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
    Preconditions.checkState(oneBased > 0, "negative index");
    return (oneBased & 0x55555555) > (oneBased & 0xAAAAAAAA);
  }
  





  @VisibleForTesting
  boolean isIntact()
  {
    for (int i = 1; i < size; i++) {
      if (!heapForIndex(i).verifyIndex(i)) {
        return false;
      }
    }
    return true;
  }
  


  private class Heap
  {
    final Ordering<E> ordering;
    
    @Weak
    MinMaxPriorityQueue<E>.Heap otherHeap;
    

    Heap()
    {
      this.ordering = ordering;
    }
    
    int compareElements(int a, int b) {
      return ordering.compare(elementData(a), elementData(b));
    }
    




    MinMaxPriorityQueue.MoveDesc<E> tryCrossOverAndBubbleUp(int removeIndex, int vacated, E toTrickle)
    {
      int crossOver = crossOver(vacated, toTrickle);
      if (crossOver == vacated) {
        return null;
      }
      

      E parent;
      
      E parent;
      
      if (crossOver < removeIndex)
      {

        parent = elementData(removeIndex);
      } else {
        parent = elementData(getParentIndex(removeIndex));
      }
      
      if (otherHeap.bubbleUpAlternatingLevels(crossOver, toTrickle) < removeIndex) {
        return new MinMaxPriorityQueue.MoveDesc(toTrickle, parent);
      }
      return null;
    }
    



    void bubbleUp(int index, E x)
    {
      int crossOver = crossOverUp(index, x);
      MinMaxPriorityQueue<E>.Heap heap;
      MinMaxPriorityQueue<E>.Heap heap;
      if (crossOver == index) {
        heap = this;
      } else {
        index = crossOver;
        heap = otherHeap;
      }
      heap.bubbleUpAlternatingLevels(index, x);
    }
    



    @CanIgnoreReturnValue
    int bubbleUpAlternatingLevels(int index, E x)
    {
      while (index > 2) {
        int grandParentIndex = getGrandparentIndex(index);
        E e = elementData(grandParentIndex);
        if (ordering.compare(e, x) <= 0) {
          break;
        }
        queue[index] = e;
        index = grandParentIndex;
      }
      queue[index] = x;
      return index;
    }
    




    int findMin(int index, int len)
    {
      if (index >= size) {
        return -1;
      }
      Preconditions.checkState(index > 0);
      int limit = Math.min(index, size - len) + len;
      int minIndex = index;
      for (int i = index + 1; i < limit; i++) {
        if (compareElements(i, minIndex) < 0) {
          minIndex = i;
        }
      }
      return minIndex;
    }
    


    int findMinChild(int index)
    {
      return findMin(getLeftChildIndex(index), 2);
    }
    


    int findMinGrandChild(int index)
    {
      int leftChildIndex = getLeftChildIndex(index);
      if (leftChildIndex < 0) {
        return -1;
      }
      return findMin(getLeftChildIndex(leftChildIndex), 4);
    }
    




    int crossOverUp(int index, E x)
    {
      if (index == 0) {
        queue[0] = x;
        return 0;
      }
      int parentIndex = getParentIndex(index);
      E parentElement = elementData(parentIndex);
      if (parentIndex != 0)
      {



        int grandparentIndex = getParentIndex(parentIndex);
        int uncleIndex = getRightChildIndex(grandparentIndex);
        if ((uncleIndex != parentIndex) && (getLeftChildIndex(uncleIndex) >= size)) {
          E uncleElement = elementData(uncleIndex);
          if (ordering.compare(uncleElement, parentElement) < 0) {
            parentIndex = uncleIndex;
            parentElement = uncleElement;
          }
        }
      }
      if (ordering.compare(parentElement, x) < 0) {
        queue[index] = parentElement;
        queue[parentIndex] = x;
        return parentIndex;
      }
      queue[index] = x;
      return index;
    }
    









    int swapWithConceptuallyLastElement(E actualLastElement)
    {
      int parentIndex = getParentIndex(size);
      if (parentIndex != 0) {
        int grandparentIndex = getParentIndex(parentIndex);
        int uncleIndex = getRightChildIndex(grandparentIndex);
        if ((uncleIndex != parentIndex) && (getLeftChildIndex(uncleIndex) >= size)) {
          E uncleElement = elementData(uncleIndex);
          if (ordering.compare(uncleElement, actualLastElement) < 0) {
            queue[uncleIndex] = actualLastElement;
            queue[size] = uncleElement;
            return uncleIndex;
          }
        }
      }
      return size;
    }
    





    int crossOver(int index, E x)
    {
      int minChildIndex = findMinChild(index);
      

      if ((minChildIndex > 0) && (ordering.compare(elementData(minChildIndex), x) < 0)) {
        queue[index] = elementData(minChildIndex);
        queue[minChildIndex] = x;
        return minChildIndex;
      }
      return crossOverUp(index, x);
    }
    




    int fillHoleAt(int index)
    {
      int minGrandchildIndex;
      


      while ((minGrandchildIndex = findMinGrandChild(index)) > 0) {
        queue[index] = elementData(minGrandchildIndex);
        index = minGrandchildIndex;
      }
      return index;
    }
    
    private boolean verifyIndex(int i) {
      if ((getLeftChildIndex(i) < size) && (compareElements(i, getLeftChildIndex(i)) > 0)) {
        return false;
      }
      if ((getRightChildIndex(i) < size) && (compareElements(i, getRightChildIndex(i)) > 0)) {
        return false;
      }
      if ((i > 0) && (compareElements(i, getParentIndex(i)) > 0)) {
        return false;
      }
      if ((i > 2) && (compareElements(getGrandparentIndex(i), i) > 0)) {
        return false;
      }
      return true;
    }
    

    private int getLeftChildIndex(int i)
    {
      return i * 2 + 1;
    }
    
    private int getRightChildIndex(int i) {
      return i * 2 + 2;
    }
    
    private int getParentIndex(int i) {
      return (i - 1) / 2;
    }
    
    private int getGrandparentIndex(int i) {
      return getParentIndex(getParentIndex(i));
    }
  }
  




  private class QueueIterator
    implements Iterator<E>
  {
    private int cursor = -1;
    private int nextCursor = -1;
    private int expectedModCount = modCount;
    private Queue<E> forgetMeNot;
    private List<E> skipMe;
    private E lastFromForgetMeNot;
    private boolean canRemove;
    
    private QueueIterator() {}
    
    public boolean hasNext()
    {
      checkModCount();
      nextNotInSkipMe(cursor + 1);
      return (nextCursor < size()) || ((forgetMeNot != null) && 
        (!forgetMeNot.isEmpty()));
    }
    
    public E next()
    {
      checkModCount();
      nextNotInSkipMe(cursor + 1);
      if (nextCursor < size()) {
        cursor = nextCursor;
        canRemove = true;
        return elementData(cursor); }
      if (forgetMeNot != null) {
        cursor = size();
        lastFromForgetMeNot = forgetMeNot.poll();
        if (lastFromForgetMeNot != null) {
          canRemove = true;
          return lastFromForgetMeNot;
        }
      }
      throw new NoSuchElementException("iterator moved past last element in queue.");
    }
    
    public void remove()
    {
      CollectPreconditions.checkRemove(canRemove);
      checkModCount();
      canRemove = false;
      expectedModCount += 1;
      if (cursor < size()) {
        MinMaxPriorityQueue.MoveDesc<E> moved = removeAt(cursor);
        if (moved != null) {
          if (forgetMeNot == null) {
            forgetMeNot = new ArrayDeque();
            skipMe = new ArrayList(3);
          }
          if (!foundAndRemovedExactReference(skipMe, toTrickle)) {
            forgetMeNot.add(toTrickle);
          }
          if (!foundAndRemovedExactReference(forgetMeNot, replaced)) {
            skipMe.add(replaced);
          }
        }
        cursor -= 1;
        nextCursor -= 1;
      } else {
        Preconditions.checkState(removeExact(lastFromForgetMeNot));
        lastFromForgetMeNot = null;
      }
    }
    
    private boolean foundAndRemovedExactReference(Iterable<E> elements, E target)
    {
      for (Iterator<E> it = elements.iterator(); it.hasNext();) {
        E element = it.next();
        if (element == target) {
          it.remove();
          return true;
        }
      }
      return false;
    }
    
    private boolean removeExact(Object target)
    {
      for (int i = 0; i < size; i++) {
        if (queue[i] == target) {
          removeAt(i);
          return true;
        }
      }
      return false;
    }
    
    private void checkModCount() {
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
    



    private void nextNotInSkipMe(int c)
    {
      if (nextCursor < c) {
        if (skipMe != null) {
          while ((c < size()) && (foundAndRemovedExactReference(skipMe, elementData(c)))) {
            c++;
          }
        }
        nextCursor = c;
      }
    }
  }
  






















  public Iterator<E> iterator()
  {
    return new QueueIterator(null);
  }
  
  public void clear()
  {
    for (int i = 0; i < size; i++) {
      queue[i] = null;
    }
    size = 0;
  }
  
  public Object[] toArray()
  {
    Object[] copyTo = new Object[size];
    System.arraycopy(queue, 0, copyTo, 0, size);
    return copyTo;
  }
  




  public Comparator<? super E> comparator()
  {
    return minHeap.ordering;
  }
  
  @VisibleForTesting
  int capacity() {
    return queue.length;
  }
  





  @VisibleForTesting
  static int initialQueueSize(int configuredExpectedSize, int maximumSize, Iterable<?> initialContents)
  {
    int result = configuredExpectedSize == -1 ? 11 : configuredExpectedSize;
    




    if ((initialContents instanceof Collection)) {
      int initialSize = ((Collection)initialContents).size();
      result = Math.max(result, initialSize);
    }
    

    return capAtMaximumSize(result, maximumSize);
  }
  
  private void growIfNeeded() {
    if (size > queue.length) {
      int newCapacity = calculateNewCapacity();
      Object[] newQueue = new Object[newCapacity];
      System.arraycopy(queue, 0, newQueue, 0, queue.length);
      queue = newQueue;
    }
  }
  
  private int calculateNewCapacity()
  {
    int oldCapacity = queue.length;
    


    int newCapacity = oldCapacity < 64 ? (oldCapacity + 1) * 2 : IntMath.checkedMultiply(oldCapacity / 2, 3);
    return capAtMaximumSize(newCapacity, maximumSize);
  }
  
  private static int capAtMaximumSize(int queueSize, int maximumSize)
  {
    return Math.min(queueSize - 1, maximumSize) + 1;
  }
}
