package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface Table<R, C, V>
{
  public abstract boolean contains(@CompatibleWith("R") @Nullable Object paramObject1, @CompatibleWith("C") @Nullable Object paramObject2);
  
  public abstract boolean containsRow(@CompatibleWith("R") @Nullable Object paramObject);
  
  public abstract boolean containsColumn(@CompatibleWith("C") @Nullable Object paramObject);
  
  public abstract boolean containsValue(@CompatibleWith("V") @Nullable Object paramObject);
  
  public abstract V get(@CompatibleWith("R") @Nullable Object paramObject1, @CompatibleWith("C") @Nullable Object paramObject2);
  
  public abstract boolean isEmpty();
  
  public abstract int size();
  
  public abstract boolean equals(@Nullable Object paramObject);
  
  public abstract int hashCode();
  
  public abstract void clear();
  
  @Nullable
  @CanIgnoreReturnValue
  public abstract V put(R paramR, C paramC, V paramV);
  
  public abstract void putAll(Table<? extends R, ? extends C, ? extends V> paramTable);
  
  @Nullable
  @CanIgnoreReturnValue
  public abstract V remove(@CompatibleWith("R") @Nullable Object paramObject1, @CompatibleWith("C") @Nullable Object paramObject2);
  
  public abstract Map<C, V> row(R paramR);
  
  public abstract Map<R, V> column(C paramC);
  
  public abstract Set<Cell<R, C, V>> cellSet();
  
  public abstract Set<R> rowKeySet();
  
  public abstract Set<C> columnKeySet();
  
  public abstract Collection<V> values();
  
  public abstract Map<R, Map<C, V>> rowMap();
  
  public abstract Map<C, Map<R, V>> columnMap();
  
  public static abstract interface Cell<R, C, V>
  {
    @Nullable
    public abstract R getRowKey();
    
    @Nullable
    public abstract C getColumnKey();
    
    @Nullable
    public abstract V getValue();
    
    public abstract boolean equals(@Nullable Object paramObject);
    
    public abstract int hashCode();
  }
}
