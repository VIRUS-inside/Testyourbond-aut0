package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;









































@GwtCompatible(emulated=true)
public final class TreeMultiset<E>
  extends AbstractSortedMultiset<E>
  implements Serializable
{
  private final transient Reference<AvlNode<E>> rootReference;
  private final transient GeneralRange<E> range;
  private final transient AvlNode<E> header;
  @GwtIncompatible
  private static final long serialVersionUID = 1L;
  
  public static <E extends Comparable> TreeMultiset<E> create()
  {
    return new TreeMultiset(Ordering.natural());
  }
  











  public static <E> TreeMultiset<E> create(@Nullable Comparator<? super E> comparator)
  {
    return comparator == null ? new TreeMultiset(
      Ordering.natural()) : new TreeMultiset(comparator);
  }
  









  public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> elements)
  {
    TreeMultiset<E> multiset = create();
    Iterables.addAll(multiset, elements);
    return multiset;
  }
  



  TreeMultiset(Reference<AvlNode<E>> rootReference, GeneralRange<E> range, AvlNode<E> endLink)
  {
    super(range.comparator());
    this.rootReference = rootReference;
    this.range = range;
    header = endLink;
  }
  
  TreeMultiset(Comparator<? super E> comparator) {
    super(comparator);
    range = GeneralRange.all(comparator);
    header = new AvlNode(null, 1);
    successor(header, header);
    rootReference = new Reference(null);
  }
  


  private static abstract enum Aggregate
  {
    SIZE, 
    









    DISTINCT;
    


    private Aggregate() {}
    


    abstract int nodeAggregate(TreeMultiset.AvlNode<?> paramAvlNode);
    


    abstract long treeAggregate(@Nullable TreeMultiset.AvlNode<?> paramAvlNode);
  }
  

  private long aggregateForEntries(Aggregate aggr)
  {
    AvlNode<E> root = (AvlNode)rootReference.get();
    long total = aggr.treeAggregate(root);
    if (range.hasLowerBound()) {
      total -= aggregateBelowRange(aggr, root);
    }
    if (range.hasUpperBound()) {
      total -= aggregateAboveRange(aggr, root);
    }
    return total;
  }
  
  private long aggregateBelowRange(Aggregate aggr, @Nullable AvlNode<E> node) {
    if (node == null) {
      return 0L;
    }
    int cmp = comparator().compare(range.getLowerEndpoint(), elem);
    if (cmp < 0)
      return aggregateBelowRange(aggr, left);
    if (cmp == 0) {
      switch (4.$SwitchMap$com$google$common$collect$BoundType[range.getLowerBoundType().ordinal()]) {
      case 1: 
        return aggr.nodeAggregate(node) + aggr.treeAggregate(left);
      case 2: 
        return aggr.treeAggregate(left);
      }
      throw new AssertionError();
    }
    
    return 
    
      aggr.treeAggregate(left) + aggr.nodeAggregate(node) + aggregateBelowRange(aggr, right);
  }
  
  private long aggregateAboveRange(Aggregate aggr, @Nullable AvlNode<E> node)
  {
    if (node == null) {
      return 0L;
    }
    int cmp = comparator().compare(range.getUpperEndpoint(), elem);
    if (cmp > 0)
      return aggregateAboveRange(aggr, right);
    if (cmp == 0) {
      switch (4.$SwitchMap$com$google$common$collect$BoundType[range.getUpperBoundType().ordinal()]) {
      case 1: 
        return aggr.nodeAggregate(node) + aggr.treeAggregate(right);
      case 2: 
        return aggr.treeAggregate(right);
      }
      throw new AssertionError();
    }
    
    return 
    
      aggr.treeAggregate(right) + aggr.nodeAggregate(node) + aggregateAboveRange(aggr, left);
  }
  

  public int size()
  {
    return Ints.saturatedCast(aggregateForEntries(Aggregate.SIZE));
  }
  
  int distinctElements()
  {
    return Ints.saturatedCast(aggregateForEntries(Aggregate.DISTINCT));
  }
  
  public int count(@Nullable Object element)
  {
    try
    {
      E e = element;
      AvlNode<E> root = (AvlNode)rootReference.get();
      if ((!range.contains(e)) || (root == null)) {
        return 0;
      }
      return root.count(comparator(), e);
    } catch (ClassCastException e) {
      return 0;
    } catch (NullPointerException e) {}
    return 0;
  }
  

  @CanIgnoreReturnValue
  public int add(@Nullable E element, int occurrences)
  {
    CollectPreconditions.checkNonnegative(occurrences, "occurrences");
    if (occurrences == 0) {
      return count(element);
    }
    Preconditions.checkArgument(range.contains(element));
    AvlNode<E> root = (AvlNode)rootReference.get();
    if (root == null) {
      comparator().compare(element, element);
      AvlNode<E> newRoot = new AvlNode(element, occurrences);
      successor(header, newRoot, header);
      rootReference.checkAndSet(root, newRoot);
      return 0;
    }
    int[] result = new int[1];
    AvlNode<E> newRoot = root.add(comparator(), element, occurrences, result);
    rootReference.checkAndSet(root, newRoot);
    return result[0];
  }
  
  @CanIgnoreReturnValue
  public int remove(@Nullable Object element, int occurrences)
  {
    CollectPreconditions.checkNonnegative(occurrences, "occurrences");
    if (occurrences == 0) {
      return count(element);
    }
    AvlNode<E> root = (AvlNode)rootReference.get();
    int[] result = new int[1];
    
    try
    {
      E e = element;
      if ((!range.contains(e)) || (root == null)) {
        return 0;
      }
      newRoot = root.remove(comparator(), e, occurrences, result);
    } catch (ClassCastException e) { AvlNode<E> newRoot;
      return 0;
    } catch (NullPointerException e) {
      return 0; }
    AvlNode<E> newRoot;
    rootReference.checkAndSet(root, newRoot);
    return result[0];
  }
  
  @CanIgnoreReturnValue
  public int setCount(@Nullable E element, int count)
  {
    CollectPreconditions.checkNonnegative(count, "count");
    if (!range.contains(element)) {
      Preconditions.checkArgument(count == 0);
      return 0;
    }
    
    AvlNode<E> root = (AvlNode)rootReference.get();
    if (root == null) {
      if (count > 0) {
        add(element, count);
      }
      return 0;
    }
    int[] result = new int[1];
    AvlNode<E> newRoot = root.setCount(comparator(), element, count, result);
    rootReference.checkAndSet(root, newRoot);
    return result[0];
  }
  
  @CanIgnoreReturnValue
  public boolean setCount(@Nullable E element, int oldCount, int newCount)
  {
    CollectPreconditions.checkNonnegative(newCount, "newCount");
    CollectPreconditions.checkNonnegative(oldCount, "oldCount");
    Preconditions.checkArgument(range.contains(element));
    
    AvlNode<E> root = (AvlNode)rootReference.get();
    if (root == null) {
      if (oldCount == 0) {
        if (newCount > 0) {
          add(element, newCount);
        }
        return true;
      }
      return false;
    }
    
    int[] result = new int[1];
    AvlNode<E> newRoot = root.setCount(comparator(), element, oldCount, newCount, result);
    rootReference.checkAndSet(root, newRoot);
    return result[0] == oldCount;
  }
  
  private Multiset.Entry<E> wrapEntry(final AvlNode<E> baseEntry) {
    new Multisets.AbstractEntry()
    {
      public E getElement() {
        return baseEntry.getElement();
      }
      
      public int getCount()
      {
        int result = baseEntry.getCount();
        if (result == 0) {
          return count(getElement());
        }
        return result;
      }
    };
  }
  



  @Nullable
  private AvlNode<E> firstNode()
  {
    AvlNode<E> root = (AvlNode)rootReference.get();
    if (root == null) {
      return null;
    }
    AvlNode<E> node;
    if (range.hasLowerBound()) {
      E endpoint = range.getLowerEndpoint();
      AvlNode<E> node = ((AvlNode)rootReference.get()).ceiling(comparator(), endpoint);
      if (node == null) {
        return null;
      }
      if ((range.getLowerBoundType() == BoundType.OPEN) && 
        (comparator().compare(endpoint, node.getElement()) == 0)) {
        node = succ;
      }
    } else {
      node = header.succ;
    }
    return (node == header) || (!range.contains(node.getElement())) ? null : node;
  }
  
  @Nullable
  private AvlNode<E> lastNode() {
    AvlNode<E> root = (AvlNode)rootReference.get();
    if (root == null) {
      return null;
    }
    AvlNode<E> node;
    if (range.hasUpperBound()) {
      E endpoint = range.getUpperEndpoint();
      AvlNode<E> node = ((AvlNode)rootReference.get()).floor(comparator(), endpoint);
      if (node == null) {
        return null;
      }
      if ((range.getUpperBoundType() == BoundType.OPEN) && 
        (comparator().compare(endpoint, node.getElement()) == 0)) {
        node = pred;
      }
    } else {
      node = header.pred;
    }
    return (node == header) || (!range.contains(node.getElement())) ? null : node;
  }
  
  Iterator<Multiset.Entry<E>> entryIterator()
  {
    new Iterator() {
      TreeMultiset.AvlNode<E> current = TreeMultiset.this.firstNode();
      Multiset.Entry<E> prevEntry;
      
      public boolean hasNext()
      {
        if (current == null)
          return false;
        if (range.tooHigh(current.getElement())) {
          current = null;
          return false;
        }
        return true;
      }
      

      public Multiset.Entry<E> next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(current);
        prevEntry = result;
        if (current.succ == header) {
          current = null;
        } else {
          current = current.succ;
        }
        return result;
      }
      
      public void remove()
      {
        CollectPreconditions.checkRemove(prevEntry != null);
        setCount(prevEntry.getElement(), 0);
        prevEntry = null;
      }
    };
  }
  
  Iterator<Multiset.Entry<E>> descendingEntryIterator()
  {
    new Iterator() {
      TreeMultiset.AvlNode<E> current = TreeMultiset.this.lastNode();
      Multiset.Entry<E> prevEntry = null;
      
      public boolean hasNext()
      {
        if (current == null)
          return false;
        if (range.tooLow(current.getElement())) {
          current = null;
          return false;
        }
        return true;
      }
      

      public Multiset.Entry<E> next()
      {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Multiset.Entry<E> result = TreeMultiset.this.wrapEntry(current);
        prevEntry = result;
        if (current.pred == header) {
          current = null;
        } else {
          current = current.pred;
        }
        return result;
      }
      
      public void remove()
      {
        CollectPreconditions.checkRemove(prevEntry != null);
        setCount(prevEntry.getElement(), 0);
        prevEntry = null;
      }
    };
  }
  
  public SortedMultiset<E> headMultiset(@Nullable E upperBound, BoundType boundType)
  {
    return new TreeMultiset(rootReference, range
    
      .intersect(GeneralRange.upTo(comparator(), upperBound, boundType)), header);
  }
  

  public SortedMultiset<E> tailMultiset(@Nullable E lowerBound, BoundType boundType)
  {
    return new TreeMultiset(rootReference, range
    
      .intersect(GeneralRange.downTo(comparator(), lowerBound, boundType)), header);
  }
  


  static int distinctElements(@Nullable AvlNode<?> node) { return node == null ? 0 : distinctElements; }
  
  private static final class Reference<T> { @Nullable
    private T value;
    
    private Reference() {}
    
    @Nullable
    public T get() { return value; }
    
    public void checkAndSet(@Nullable T expected, T newValue)
    {
      if (value != expected) {
        throw new ConcurrentModificationException();
      }
      value = newValue;
    }
  }
  
  private static final class AvlNode<E> extends Multisets.AbstractEntry<E>
  {
    @Nullable
    private final E elem;
    private int elemCount;
    private int distinctElements;
    private long totalCount;
    private int height;
    private AvlNode<E> left;
    private AvlNode<E> right;
    private AvlNode<E> pred;
    private AvlNode<E> succ;
    
    AvlNode(@Nullable E elem, int elemCount)
    {
      Preconditions.checkArgument(elemCount > 0);
      this.elem = elem;
      this.elemCount = elemCount;
      totalCount = elemCount;
      distinctElements = 1;
      height = 1;
      left = null;
      right = null;
    }
    
    public int count(Comparator<? super E> comparator, E e) {
      int cmp = comparator.compare(e, elem);
      if (cmp < 0)
        return left == null ? 0 : left.count(comparator, e);
      if (cmp > 0) {
        return right == null ? 0 : right.count(comparator, e);
      }
      return elemCount;
    }
    
    private AvlNode<E> addRightChild(E e, int count)
    {
      right = new AvlNode(e, count);
      TreeMultiset.successor(this, right, succ);
      height = Math.max(2, height);
      distinctElements += 1;
      totalCount += count;
      return this;
    }
    
    private AvlNode<E> addLeftChild(E e, int count) {
      left = new AvlNode(e, count);
      TreeMultiset.successor(pred, left, this);
      height = Math.max(2, height);
      distinctElements += 1;
      totalCount += count;
      return this;
    }
    



    AvlNode<E> add(Comparator<? super E> comparator, @Nullable E e, int count, int[] result)
    {
      int cmp = comparator.compare(e, elem);
      if (cmp < 0) {
        AvlNode<E> initLeft = left;
        if (initLeft == null) {
          result[0] = 0;
          return addLeftChild(e, count);
        }
        int initHeight = height;
        
        left = initLeft.add(comparator, e, count, result);
        if (result[0] == 0) {
          distinctElements += 1;
        }
        totalCount += count;
        return left.height == initHeight ? this : rebalance(); }
      if (cmp > 0) {
        AvlNode<E> initRight = right;
        if (initRight == null) {
          result[0] = 0;
          return addRightChild(e, count);
        }
        int initHeight = height;
        
        right = initRight.add(comparator, e, count, result);
        if (result[0] == 0) {
          distinctElements += 1;
        }
        totalCount += count;
        return right.height == initHeight ? this : rebalance();
      }
      

      result[0] = elemCount;
      long resultCount = elemCount + count;
      Preconditions.checkArgument(resultCount <= 2147483647L);
      elemCount += count;
      totalCount += count;
      return this;
    }
    
    AvlNode<E> remove(Comparator<? super E> comparator, @Nullable E e, int count, int[] result) {
      int cmp = comparator.compare(e, elem);
      if (cmp < 0) {
        AvlNode<E> initLeft = left;
        if (initLeft == null) {
          result[0] = 0;
          return this;
        }
        
        left = initLeft.remove(comparator, e, count, result);
        
        if (result[0] > 0) {
          if (count >= result[0]) {
            distinctElements -= 1;
            totalCount -= result[0];
          } else {
            totalCount -= count;
          }
        }
        return result[0] == 0 ? this : rebalance(); }
      if (cmp > 0) {
        AvlNode<E> initRight = right;
        if (initRight == null) {
          result[0] = 0;
          return this;
        }
        
        right = initRight.remove(comparator, e, count, result);
        
        if (result[0] > 0) {
          if (count >= result[0]) {
            distinctElements -= 1;
            totalCount -= result[0];
          } else {
            totalCount -= count;
          }
        }
        return rebalance();
      }
      

      result[0] = elemCount;
      if (count >= elemCount) {
        return deleteMe();
      }
      elemCount -= count;
      totalCount -= count;
      return this;
    }
    
    AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int count, int[] result)
    {
      int cmp = comparator.compare(e, elem);
      if (cmp < 0) {
        AvlNode<E> initLeft = left;
        if (initLeft == null) {
          result[0] = 0;
          return count > 0 ? addLeftChild(e, count) : this;
        }
        
        left = initLeft.setCount(comparator, e, count, result);
        
        if ((count == 0) && (result[0] != 0)) {
          distinctElements -= 1;
        } else if ((count > 0) && (result[0] == 0)) {
          distinctElements += 1;
        }
        
        totalCount += count - result[0];
        return rebalance(); }
      if (cmp > 0) {
        AvlNode<E> initRight = right;
        if (initRight == null) {
          result[0] = 0;
          return count > 0 ? addRightChild(e, count) : this;
        }
        
        right = initRight.setCount(comparator, e, count, result);
        
        if ((count == 0) && (result[0] != 0)) {
          distinctElements -= 1;
        } else if ((count > 0) && (result[0] == 0)) {
          distinctElements += 1;
        }
        
        totalCount += count - result[0];
        return rebalance();
      }
      

      result[0] = elemCount;
      if (count == 0) {
        return deleteMe();
      }
      totalCount += count - elemCount;
      elemCount = count;
      return this;
    }
    




    AvlNode<E> setCount(Comparator<? super E> comparator, @Nullable E e, int expectedCount, int newCount, int[] result)
    {
      int cmp = comparator.compare(e, elem);
      if (cmp < 0) {
        AvlNode<E> initLeft = left;
        if (initLeft == null) {
          result[0] = 0;
          if ((expectedCount == 0) && (newCount > 0)) {
            return addLeftChild(e, newCount);
          }
          return this;
        }
        
        left = initLeft.setCount(comparator, e, expectedCount, newCount, result);
        
        if (result[0] == expectedCount) {
          if ((newCount == 0) && (result[0] != 0)) {
            distinctElements -= 1;
          } else if ((newCount > 0) && (result[0] == 0)) {
            distinctElements += 1;
          }
          totalCount += newCount - result[0];
        }
        return rebalance(); }
      if (cmp > 0) {
        AvlNode<E> initRight = right;
        if (initRight == null) {
          result[0] = 0;
          if ((expectedCount == 0) && (newCount > 0)) {
            return addRightChild(e, newCount);
          }
          return this;
        }
        
        right = initRight.setCount(comparator, e, expectedCount, newCount, result);
        
        if (result[0] == expectedCount) {
          if ((newCount == 0) && (result[0] != 0)) {
            distinctElements -= 1;
          } else if ((newCount > 0) && (result[0] == 0)) {
            distinctElements += 1;
          }
          totalCount += newCount - result[0];
        }
        return rebalance();
      }
      

      result[0] = elemCount;
      if (expectedCount == elemCount) {
        if (newCount == 0) {
          return deleteMe();
        }
        totalCount += newCount - elemCount;
        elemCount = newCount;
      }
      return this;
    }
    
    private AvlNode<E> deleteMe() {
      int oldElemCount = elemCount;
      elemCount = 0;
      TreeMultiset.successor(pred, succ);
      if (left == null)
        return right;
      if (right == null)
        return left;
      if (left.height >= right.height) {
        AvlNode<E> newTop = pred;
        
        left = left.removeMax(newTop);
        right = right;
        distinctElements -= 1;
        totalCount -= oldElemCount;
        return newTop.rebalance();
      }
      AvlNode<E> newTop = succ;
      right = right.removeMin(newTop);
      left = left;
      distinctElements -= 1;
      totalCount -= oldElemCount;
      return newTop.rebalance();
    }
    

    private AvlNode<E> removeMin(AvlNode<E> node)
    {
      if (left == null) {
        return right;
      }
      left = left.removeMin(node);
      distinctElements -= 1;
      totalCount -= elemCount;
      return rebalance();
    }
    

    private AvlNode<E> removeMax(AvlNode<E> node)
    {
      if (right == null) {
        return left;
      }
      right = right.removeMax(node);
      distinctElements -= 1;
      totalCount -= elemCount;
      return rebalance();
    }
    

    private void recomputeMultiset()
    {
      distinctElements = (1 + TreeMultiset.distinctElements(left) + TreeMultiset.distinctElements(right));
      totalCount = (elemCount + totalCount(left) + totalCount(right));
    }
    
    private void recomputeHeight() {
      height = (1 + Math.max(height(left), height(right)));
    }
    
    private void recompute() {
      recomputeMultiset();
      recomputeHeight();
    }
    
    private AvlNode<E> rebalance() {
      switch (balanceFactor()) {
      case -2: 
        if (right.balanceFactor() > 0) {
          right = right.rotateRight();
        }
        return rotateLeft();
      case 2: 
        if (left.balanceFactor() < 0) {
          left = left.rotateLeft();
        }
        return rotateRight();
      }
      recomputeHeight();
      return this;
    }
    
    private int balanceFactor()
    {
      return height(left) - height(right);
    }
    
    private AvlNode<E> rotateLeft() {
      Preconditions.checkState(right != null);
      AvlNode<E> newTop = right;
      right = left;
      left = this;
      totalCount = totalCount;
      distinctElements = distinctElements;
      recompute();
      newTop.recomputeHeight();
      return newTop;
    }
    
    private AvlNode<E> rotateRight() {
      Preconditions.checkState(left != null);
      AvlNode<E> newTop = left;
      left = right;
      right = this;
      totalCount = totalCount;
      distinctElements = distinctElements;
      recompute();
      newTop.recomputeHeight();
      return newTop;
    }
    
    private static long totalCount(@Nullable AvlNode<?> node) {
      return node == null ? 0L : totalCount;
    }
    
    private static int height(@Nullable AvlNode<?> node) {
      return node == null ? 0 : height;
    }
    
    @Nullable
    private AvlNode<E> ceiling(Comparator<? super E> comparator, E e) {
      int cmp = comparator.compare(e, elem);
      if (cmp < 0)
        return left == null ? this : (AvlNode)MoreObjects.firstNonNull(left.ceiling(comparator, e), this);
      if (cmp == 0) {
        return this;
      }
      return right == null ? null : right.ceiling(comparator, e);
    }
    
    @Nullable
    private AvlNode<E> floor(Comparator<? super E> comparator, E e)
    {
      int cmp = comparator.compare(e, elem);
      if (cmp > 0)
        return right == null ? this : (AvlNode)MoreObjects.firstNonNull(right.floor(comparator, e), this);
      if (cmp == 0) {
        return this;
      }
      return left == null ? null : left.floor(comparator, e);
    }
    

    public E getElement()
    {
      return elem;
    }
    
    public int getCount()
    {
      return elemCount;
    }
    
    public String toString()
    {
      return Multisets.immutableEntry(getElement(), getCount()).toString();
    }
  }
  
  private static <T> void successor(AvlNode<T> a, AvlNode<T> b) {
    succ = b;
    pred = a;
  }
  
  private static <T> void successor(AvlNode<T> a, AvlNode<T> b, AvlNode<T> c) {
    successor(a, b);
    successor(b, c);
  }
  








  @GwtIncompatible
  private void writeObject(ObjectOutputStream stream)
    throws IOException
  {
    stream.defaultWriteObject();
    stream.writeObject(elementSet().comparator());
    Serialization.writeMultiset(this, stream);
  }
  
  @GwtIncompatible
  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    

    Comparator<? super E> comparator = (Comparator)stream.readObject();
    Serialization.getFieldSetter(AbstractSortedMultiset.class, "comparator").set(this, comparator);
    Serialization.getFieldSetter(TreeMultiset.class, "range")
      .set(this, GeneralRange.all(comparator));
    Serialization.getFieldSetter(TreeMultiset.class, "rootReference")
      .set(this, new Reference(null));
    AvlNode<E> header = new AvlNode(null, 1);
    Serialization.getFieldSetter(TreeMultiset.class, "header").set(this, header);
    successor(header, header);
    Serialization.populateMultiset(this, stream);
  }
}
