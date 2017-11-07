package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.Map;
import javax.annotation.Nullable;









































@Beta
public final class ElementOrder<T>
{
  private final Type type;
  @Nullable
  private final Comparator<T> comparator;
  
  public static enum Type
  {
    UNORDERED, 
    INSERTION, 
    SORTED;
    
    private Type() {} }
  
  private ElementOrder(Type type, @Nullable Comparator<T> comparator) { this.type = ((Type)Preconditions.checkNotNull(type));
    this.comparator = comparator;
    Preconditions.checkState((type == Type.SORTED ? 1 : 0) == (comparator != null ? 1 : 0));
  }
  
  public static <S> ElementOrder<S> unordered()
  {
    return new ElementOrder(Type.UNORDERED, null);
  }
  
  public static <S> ElementOrder<S> insertion()
  {
    return new ElementOrder(Type.INSERTION, null);
  }
  


  public static <S extends Comparable<? super S>> ElementOrder<S> natural()
  {
    return new ElementOrder(Type.SORTED, Ordering.natural());
  }
  



  public static <S> ElementOrder<S> sorted(Comparator<S> comparator)
  {
    return new ElementOrder(Type.SORTED, comparator);
  }
  
  public Type type()
  {
    return type;
  }
  




  public Comparator<T> comparator()
  {
    if (comparator != null) {
      return comparator;
    }
    throw new UnsupportedOperationException("This ordering does not define a comparator.");
  }
  
  public boolean equals(@Nullable Object obj)
  {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ElementOrder)) {
      return false;
    }
    
    ElementOrder<?> other = (ElementOrder)obj;
    return (type == type) && (Objects.equal(comparator, comparator));
  }
  
  public int hashCode()
  {
    return Objects.hashCode(new Object[] { type, comparator });
  }
  
  public String toString()
  {
    MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).add("type", type);
    if (comparator != null) {
      helper.add("comparator", comparator);
    }
    return helper.toString();
  }
  
  <K extends T, V> Map<K, V> createMap(int expectedSize)
  {
    switch (1.$SwitchMap$com$google$common$graph$ElementOrder$Type[type.ordinal()]) {
    case 1: 
      return Maps.newHashMapWithExpectedSize(expectedSize);
    case 2: 
      return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
    case 3: 
      return Maps.newTreeMap(comparator());
    }
    throw new AssertionError();
  }
  

  <T1 extends T> ElementOrder<T1> cast()
  {
    return this;
  }
}
