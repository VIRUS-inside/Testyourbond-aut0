package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;































@GwtCompatible(serializable=true, emulated=true)
final class SingletonImmutableSet<E>
  extends ImmutableSet<E>
{
  final transient E element;
  @LazyInit
  private transient int cachedHashCode;
  
  SingletonImmutableSet(E element)
  {
    this.element = Preconditions.checkNotNull(element);
  }
  
  SingletonImmutableSet(E element, int hashCode)
  {
    this.element = element;
    cachedHashCode = hashCode;
  }
  
  public int size()
  {
    return 1;
  }
  
  public boolean contains(Object target)
  {
    return element.equals(target);
  }
  
  public UnmodifiableIterator<E> iterator()
  {
    return Iterators.singletonIterator(element);
  }
  
  ImmutableList<E> createAsList()
  {
    return ImmutableList.of(element);
  }
  
  boolean isPartialView()
  {
    return false;
  }
  
  int copyIntoArray(Object[] dst, int offset)
  {
    dst[offset] = element;
    return offset + 1;
  }
  

  public final int hashCode()
  {
    int code = cachedHashCode;
    if (code == 0) {
      cachedHashCode = (code = element.hashCode());
    }
    return code;
  }
  
  boolean isHashCodeFast()
  {
    return cachedHashCode != 0;
  }
  
  public String toString()
  {
    return '[' + element.toString() + ']';
  }
}
