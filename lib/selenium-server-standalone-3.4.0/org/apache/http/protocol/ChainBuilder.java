package org.apache.http.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;



































final class ChainBuilder<E>
{
  private final LinkedList<E> list;
  private final Map<Class<?>, E> uniqueClasses;
  
  public ChainBuilder()
  {
    list = new LinkedList();
    uniqueClasses = new HashMap();
  }
  
  private void ensureUnique(E e) {
    E previous = uniqueClasses.remove(e.getClass());
    if (previous != null) {
      list.remove(previous);
    }
    uniqueClasses.put(e.getClass(), e);
  }
  
  public ChainBuilder<E> addFirst(E e) {
    if (e == null) {
      return this;
    }
    ensureUnique(e);
    list.addFirst(e);
    return this;
  }
  
  public ChainBuilder<E> addLast(E e) {
    if (e == null) {
      return this;
    }
    ensureUnique(e);
    list.addLast(e);
    return this;
  }
  
  public ChainBuilder<E> addAllFirst(Collection<E> c) {
    if (c == null) {
      return this;
    }
    for (E e : c) {
      addFirst(e);
    }
    return this;
  }
  
  public ChainBuilder<E> addAllFirst(E... c) {
    if (c == null) {
      return this;
    }
    for (E e : c) {
      addFirst(e);
    }
    return this;
  }
  
  public ChainBuilder<E> addAllLast(Collection<E> c) {
    if (c == null) {
      return this;
    }
    for (E e : c) {
      addLast(e);
    }
    return this;
  }
  
  public ChainBuilder<E> addAllLast(E... c) {
    if (c == null) {
      return this;
    }
    for (E e : c) {
      addLast(e);
    }
    return this;
  }
  
  public LinkedList<E> build() {
    return new LinkedList(list);
  }
}
