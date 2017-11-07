package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import java.util.Spliterator;
import java.util.Spliterators;
import javax.annotation.Nullable;





















@GwtCompatible(serializable=true, emulated=true)
final class RegularImmutableSet<E>
  extends ImmutableSet.Indexed<E>
{
  static final RegularImmutableSet<Object> EMPTY = new RegularImmutableSet(ObjectArrays.EMPTY_ARRAY, 0, null, 0);
  
  private final transient Object[] elements;
  
  @VisibleForTesting
  final transient Object[] table;
  private final transient int mask;
  private final transient int hashCode;
  
  RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask)
  {
    this.elements = elements;
    this.table = table;
    this.mask = mask;
    this.hashCode = hashCode;
  }
  
  public boolean contains(@Nullable Object target)
  {
    Object[] table = this.table;
    if ((target == null) || (table == null)) {
      return false;
    }
    for (int i = Hashing.smearedHash(target);; i++) {
      i &= mask;
      Object candidate = table[i];
      if (candidate == null)
        return false;
      if (candidate.equals(target)) {
        return true;
      }
    }
  }
  
  public int size()
  {
    return elements.length;
  }
  
  E get(int i) {
    return elements[i];
  }
  
  public Spliterator<E> spliterator()
  {
    return Spliterators.spliterator(elements, 1297);
  }
  
  int copyIntoArray(Object[] dst, int offset)
  {
    System.arraycopy(elements, 0, dst, offset, elements.length);
    return offset + elements.length;
  }
  
  ImmutableList<E> createAsList()
  {
    return table == null ? ImmutableList.of() : new RegularImmutableAsList(this, elements);
  }
  
  boolean isPartialView()
  {
    return false;
  }
  
  public int hashCode()
  {
    return hashCode;
  }
  
  boolean isHashCodeFast()
  {
    return true;
  }
}
