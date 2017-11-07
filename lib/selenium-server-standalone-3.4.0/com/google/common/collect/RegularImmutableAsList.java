package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.util.function.Consumer;





















@GwtCompatible(emulated=true)
class RegularImmutableAsList<E>
  extends ImmutableAsList<E>
{
  private final ImmutableCollection<E> delegate;
  private final ImmutableList<? extends E> delegateList;
  
  RegularImmutableAsList(ImmutableCollection<E> delegate, ImmutableList<? extends E> delegateList)
  {
    this.delegate = delegate;
    this.delegateList = delegateList;
  }
  
  RegularImmutableAsList(ImmutableCollection<E> delegate, Object[] array) {
    this(delegate, ImmutableList.asImmutableList(array));
  }
  
  ImmutableCollection<E> delegateCollection()
  {
    return delegate;
  }
  
  ImmutableList<? extends E> delegateList() {
    return delegateList;
  }
  

  public UnmodifiableListIterator<E> listIterator(int index)
  {
    return delegateList.listIterator(index);
  }
  
  @GwtIncompatible
  public void forEach(Consumer<? super E> action)
  {
    delegateList.forEach(action);
  }
  
  @GwtIncompatible
  int copyIntoArray(Object[] dst, int offset)
  {
    return delegateList.copyIntoArray(dst, offset);
  }
  
  public E get(int index)
  {
    return delegateList.get(index);
  }
}
