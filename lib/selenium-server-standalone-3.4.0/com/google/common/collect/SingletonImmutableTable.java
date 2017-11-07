package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map;




















@GwtCompatible
class SingletonImmutableTable<R, C, V>
  extends ImmutableTable<R, C, V>
{
  final R singleRowKey;
  final C singleColumnKey;
  final V singleValue;
  
  SingletonImmutableTable(R rowKey, C columnKey, V value)
  {
    singleRowKey = Preconditions.checkNotNull(rowKey);
    singleColumnKey = Preconditions.checkNotNull(columnKey);
    singleValue = Preconditions.checkNotNull(value);
  }
  
  SingletonImmutableTable(Table.Cell<R, C, V> cell) {
    this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
  }
  
  public ImmutableMap<R, V> column(C columnKey)
  {
    Preconditions.checkNotNull(columnKey);
    return containsColumn(columnKey) ? 
      ImmutableMap.of(singleRowKey, singleValue) : 
      ImmutableMap.of();
  }
  
  public ImmutableMap<C, Map<R, V>> columnMap()
  {
    return ImmutableMap.of(singleColumnKey, ImmutableMap.of(singleRowKey, singleValue));
  }
  
  public ImmutableMap<R, Map<C, V>> rowMap()
  {
    return ImmutableMap.of(singleRowKey, ImmutableMap.of(singleColumnKey, singleValue));
  }
  
  public int size()
  {
    return 1;
  }
  
  ImmutableSet<Table.Cell<R, C, V>> createCellSet()
  {
    return ImmutableSet.of(cellOf(singleRowKey, singleColumnKey, singleValue));
  }
  
  ImmutableCollection<V> createValues()
  {
    return ImmutableSet.of(singleValue);
  }
  
  ImmutableTable.SerializedForm createSerializedForm()
  {
    return ImmutableTable.SerializedForm.create(this, new int[] { 0 }, new int[] { 0 });
  }
}
