package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Spliterator;
import java.util.function.Consumer;


















@GwtCompatible(serializable=true, emulated=true)
final class ImmutableEnumSet<E extends Enum<E>>
  extends ImmutableSet<E>
{
  private final transient EnumSet<E> delegate;
  @LazyInit
  private transient int hashCode;
  
  static ImmutableSet asImmutable(EnumSet set)
  {
    switch (set.size()) {
    case 0: 
      return ImmutableSet.of();
    case 1: 
      return ImmutableSet.of(Iterables.getOnlyElement(set));
    }
    return new ImmutableEnumSet(set);
  }
  










  private ImmutableEnumSet(EnumSet<E> delegate)
  {
    this.delegate = delegate;
  }
  
  boolean isPartialView()
  {
    return false;
  }
  
  public UnmodifiableIterator<E> iterator()
  {
    return Iterators.unmodifiableIterator(delegate.iterator());
  }
  
  public Spliterator<E> spliterator()
  {
    return delegate.spliterator();
  }
  
  public void forEach(Consumer<? super E> action)
  {
    delegate.forEach(action);
  }
  
  public int size()
  {
    return delegate.size();
  }
  
  public boolean contains(Object object)
  {
    return delegate.contains(object);
  }
  
  public boolean containsAll(Collection<?> collection)
  {
    if ((collection instanceof ImmutableEnumSet)) {
      collection = delegate;
    }
    return delegate.containsAll(collection);
  }
  
  public boolean isEmpty()
  {
    return delegate.isEmpty();
  }
  
  public boolean equals(Object object)
  {
    if (object == this) {
      return true;
    }
    if ((object instanceof ImmutableEnumSet)) {
      object = delegate;
    }
    return delegate.equals(object);
  }
  
  boolean isHashCodeFast()
  {
    return true;
  }
  



  public int hashCode()
  {
    int result = hashCode;
    return result == 0 ? (this.hashCode = delegate.hashCode()) : result;
  }
  
  public String toString()
  {
    return delegate.toString();
  }
  

  Object writeReplace()
  {
    return new EnumSerializedForm(delegate);
  }
  
  private static class EnumSerializedForm<E extends Enum<E>> implements Serializable
  {
    final EnumSet<E> delegate;
    private static final long serialVersionUID = 0L;
    
    EnumSerializedForm(EnumSet<E> delegate)
    {
      this.delegate = delegate;
    }
    
    Object readResolve()
    {
      return new ImmutableEnumSet(delegate.clone(), null);
    }
  }
}
