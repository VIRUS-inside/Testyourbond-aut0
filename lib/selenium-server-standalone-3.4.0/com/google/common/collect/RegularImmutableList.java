package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Spliterator;
import java.util.Spliterators;





















@GwtCompatible(serializable=true, emulated=true)
class RegularImmutableList<E>
  extends ImmutableList<E>
{
  static final ImmutableList<Object> EMPTY = new RegularImmutableList(ObjectArrays.EMPTY_ARRAY);
  
  private final transient Object[] array;
  
  RegularImmutableList(Object[] array)
  {
    this.array = array;
  }
  
  public int size()
  {
    return array.length;
  }
  
  boolean isPartialView()
  {
    return false;
  }
  
  int copyIntoArray(Object[] dst, int dstOff)
  {
    System.arraycopy(array, 0, dst, dstOff, array.length);
    return dstOff + array.length;
  }
  


  public E get(int index)
  {
    return array[index];
  }
  



  public UnmodifiableListIterator<E> listIterator(int index)
  {
    return Iterators.forArray(array, 0, array.length, index);
  }
  
  public Spliterator<E> spliterator()
  {
    return Spliterators.spliterator(array, 1296);
  }
}
