package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import javax.annotation.Nullable;





















































































































































@GwtCompatible(emulated=true)
public abstract class ImmutableCollection<E>
  extends AbstractCollection<E>
  implements Serializable
{
  static final int SPLITERATOR_CHARACTERISTICS = 1296;
  
  ImmutableCollection() {}
  
  public abstract UnmodifiableIterator<E> iterator();
  
  public Spliterator<E> spliterator()
  {
    return Spliterators.spliterator(this, 1296);
  }
  
  public final Object[] toArray()
  {
    int size = size();
    if (size == 0) {
      return ObjectArrays.EMPTY_ARRAY;
    }
    Object[] result = new Object[size];
    copyIntoArray(result, 0);
    return result;
  }
  
  @CanIgnoreReturnValue
  public final <T> T[] toArray(T[] other)
  {
    Preconditions.checkNotNull(other);
    int size = size();
    if (other.length < size) {
      other = ObjectArrays.newArray(other, size);
    } else if (other.length > size) {
      other[size] = null;
    }
    copyIntoArray(other, 0);
    return other;
  }
  




  public abstract boolean contains(@Nullable Object paramObject);
  



  @Deprecated
  @CanIgnoreReturnValue
  public final boolean add(E e)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  @CanIgnoreReturnValue
  public final boolean remove(Object object)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  @CanIgnoreReturnValue
  public final boolean addAll(Collection<? extends E> newElements)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  @CanIgnoreReturnValue
  public final boolean removeAll(Collection<?> oldElements)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  @CanIgnoreReturnValue
  public final boolean removeIf(Predicate<? super E> filter)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public final boolean retainAll(Collection<?> elementsToKeep)
  {
    throw new UnsupportedOperationException();
  }
  






  @Deprecated
  public final void clear()
  {
    throw new UnsupportedOperationException();
  }
  









  public ImmutableList<E> asList()
  {
    switch (size()) {
    case 0: 
      return ImmutableList.of();
    case 1: 
      return ImmutableList.of(iterator().next());
    }
    return new RegularImmutableAsList(this, toArray());
  }
  





  abstract boolean isPartialView();
  





  @CanIgnoreReturnValue
  int copyIntoArray(Object[] dst, int offset)
  {
    for (UnmodifiableIterator localUnmodifiableIterator = iterator(); localUnmodifiableIterator.hasNext();) { E e = localUnmodifiableIterator.next();
      dst[(offset++)] = e;
    }
    return offset;
  }
  
  Object writeReplace()
  {
    return new ImmutableList.SerializedForm(toArray());
  }
  


  public static abstract class Builder<E>
  {
    static final int DEFAULT_INITIAL_CAPACITY = 4;
    

    static int expandedCapacity(int oldCapacity, int minCapacity)
    {
      if (minCapacity < 0) {
        throw new AssertionError("cannot store more than MAX_VALUE elements");
      }
      
      int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
      if (newCapacity < minCapacity) {
        newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
      }
      if (newCapacity < 0) {
        newCapacity = Integer.MAX_VALUE;
      }
      
      return newCapacity;
    }
    







    Builder() {}
    







    @CanIgnoreReturnValue
    public abstract Builder<E> add(E paramE);
    







    @CanIgnoreReturnValue
    public Builder<E> add(E... elements)
    {
      for (E element : elements) {
        add(element);
      }
      return this;
    }
    











    @CanIgnoreReturnValue
    public Builder<E> addAll(Iterable<? extends E> elements)
    {
      for (E element : elements) {
        add(element);
      }
      return this;
    }
    











    @CanIgnoreReturnValue
    public Builder<E> addAll(Iterator<? extends E> elements)
    {
      while (elements.hasNext()) {
        add(elements.next());
      }
      return this;
    }
    

    public abstract ImmutableCollection<E> build();
  }
  

  static abstract class ArrayBasedBuilder<E>
    extends ImmutableCollection.Builder<E>
  {
    Object[] contents;
    
    int size;
    

    ArrayBasedBuilder(int initialCapacity)
    {
      CollectPreconditions.checkNonnegative(initialCapacity, "initialCapacity");
      contents = new Object[initialCapacity];
      size = 0;
    }
    



    private void ensureCapacity(int minCapacity)
    {
      if (contents.length < minCapacity)
      {
        contents = Arrays.copyOf(contents, 
          expandedCapacity(contents.length, minCapacity));
      }
    }
    
    @CanIgnoreReturnValue
    public ArrayBasedBuilder<E> add(E element)
    {
      Preconditions.checkNotNull(element);
      ensureCapacity(size + 1);
      contents[(size++)] = element;
      return this;
    }
    
    @CanIgnoreReturnValue
    public ImmutableCollection.Builder<E> add(E... elements)
    {
      ObjectArrays.checkElementsNotNull(elements);
      ensureCapacity(size + elements.length);
      System.arraycopy(elements, 0, contents, size, elements.length);
      size += elements.length;
      return this;
    }
    
    @CanIgnoreReturnValue
    public ImmutableCollection.Builder<E> addAll(Iterable<? extends E> elements)
    {
      if ((elements instanceof Collection)) {
        Collection<?> collection = (Collection)elements;
        ensureCapacity(size + collection.size());
      }
      super.addAll(elements);
      return this;
    }
    
    @CanIgnoreReturnValue
    ArrayBasedBuilder<E> combine(ArrayBasedBuilder<E> builder) {
      Preconditions.checkNotNull(builder);
      ensureCapacity(size + size);
      System.arraycopy(contents, 0, contents, size, size);
      size += size;
      return this;
    }
  }
}
