package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;























@GwtCompatible
@Immutable
final class DenseImmutableTable<R, C, V>
  extends RegularImmutableTable<R, C, V>
{
  private final ImmutableMap<R, Integer> rowKeyToIndex;
  private final ImmutableMap<C, Integer> columnKeyToIndex;
  private final ImmutableMap<R, Map<C, V>> rowMap;
  private final ImmutableMap<C, Map<R, V>> columnMap;
  private final int[] rowCounts;
  private final int[] columnCounts;
  private final V[][] values;
  private final int[] cellRowIndices;
  private final int[] cellColumnIndices;
  
  DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
  {
    V[][] array = (Object[][])new Object[rowSpace.size()][columnSpace.size()];
    values = array;
    rowKeyToIndex = Maps.indexMap(rowSpace);
    columnKeyToIndex = Maps.indexMap(columnSpace);
    rowCounts = new int[rowKeyToIndex.size()];
    columnCounts = new int[columnKeyToIndex.size()];
    int[] cellRowIndices = new int[cellList.size()];
    int[] cellColumnIndices = new int[cellList.size()];
    for (int i = 0; i < cellList.size(); i++) {
      Table.Cell<R, C, V> cell = (Table.Cell)cellList.get(i);
      R rowKey = cell.getRowKey();
      C columnKey = cell.getColumnKey();
      int rowIndex = ((Integer)rowKeyToIndex.get(rowKey)).intValue();
      int columnIndex = ((Integer)columnKeyToIndex.get(columnKey)).intValue();
      V existingValue = values[rowIndex][columnIndex];
      Preconditions.checkArgument(existingValue == null, "duplicate key: (%s, %s)", rowKey, columnKey);
      values[rowIndex][columnIndex] = cell.getValue();
      rowCounts[rowIndex] += 1;
      columnCounts[columnIndex] += 1;
      cellRowIndices[i] = rowIndex;
      cellColumnIndices[i] = columnIndex;
    }
    this.cellRowIndices = cellRowIndices;
    this.cellColumnIndices = cellColumnIndices;
    rowMap = new RowMap(null);
    columnMap = new ColumnMap(null);
  }
  
  private static abstract class ImmutableArrayMap<K, V>
    extends ImmutableMap.IteratorBasedImmutableMap<K, V>
  {
    private final int size;
    
    ImmutableArrayMap(int size)
    {
      this.size = size;
    }
    
    abstract ImmutableMap<K, Integer> keyToIndex();
    
    private boolean isFull()
    {
      return size == keyToIndex().size();
    }
    
    K getKey(int index) {
      return keyToIndex().keySet().asList().get(index);
    }
    
    @Nullable
    abstract V getValue(int paramInt);
    
    ImmutableSet<K> createKeySet()
    {
      return isFull() ? keyToIndex().keySet() : super.createKeySet();
    }
    
    public int size()
    {
      return size;
    }
    
    public V get(@Nullable Object key)
    {
      Integer keyIndex = (Integer)keyToIndex().get(key);
      return keyIndex == null ? null : getValue(keyIndex.intValue());
    }
    
    UnmodifiableIterator<Map.Entry<K, V>> entryIterator()
    {
      new AbstractIterator() {
        private int index = -1;
        private final int maxIndex = keyToIndex().size();
        
        protected Map.Entry<K, V> computeNext()
        {
          for (index += 1; index < maxIndex; index += 1) {
            V value = getValue(index);
            if (value != null) {
              return Maps.immutableEntry(getKey(index), value);
            }
          }
          return (Map.Entry)endOfData();
        }
      };
    }
  }
  
  private final class Row extends DenseImmutableTable.ImmutableArrayMap<C, V> {
    private final int rowIndex;
    
    Row(int rowIndex) {
      super();
      this.rowIndex = rowIndex;
    }
    
    ImmutableMap<C, Integer> keyToIndex()
    {
      return columnKeyToIndex;
    }
    
    V getValue(int keyIndex)
    {
      return values[rowIndex][keyIndex];
    }
    
    boolean isPartialView()
    {
      return true;
    }
  }
  
  private final class Column extends DenseImmutableTable.ImmutableArrayMap<R, V> {
    private final int columnIndex;
    
    Column(int columnIndex) {
      super();
      this.columnIndex = columnIndex;
    }
    
    ImmutableMap<R, Integer> keyToIndex()
    {
      return rowKeyToIndex;
    }
    
    V getValue(int keyIndex)
    {
      return values[keyIndex][columnIndex];
    }
    
    boolean isPartialView()
    {
      return true;
    }
  }
  
  private final class RowMap extends DenseImmutableTable.ImmutableArrayMap<R, Map<C, V>>
  {
    private RowMap() {
      super();
    }
    
    ImmutableMap<R, Integer> keyToIndex()
    {
      return rowKeyToIndex;
    }
    
    Map<C, V> getValue(int keyIndex)
    {
      return new DenseImmutableTable.Row(DenseImmutableTable.this, keyIndex);
    }
    
    boolean isPartialView()
    {
      return false;
    }
  }
  
  private final class ColumnMap extends DenseImmutableTable.ImmutableArrayMap<C, Map<R, V>>
  {
    private ColumnMap() {
      super();
    }
    
    ImmutableMap<C, Integer> keyToIndex()
    {
      return columnKeyToIndex;
    }
    
    Map<R, V> getValue(int keyIndex)
    {
      return new DenseImmutableTable.Column(DenseImmutableTable.this, keyIndex);
    }
    
    boolean isPartialView()
    {
      return false;
    }
  }
  
  public ImmutableMap<C, Map<R, V>> columnMap()
  {
    return columnMap;
  }
  
  public ImmutableMap<R, Map<C, V>> rowMap()
  {
    return rowMap;
  }
  
  public V get(@Nullable Object rowKey, @Nullable Object columnKey)
  {
    Integer rowIndex = (Integer)rowKeyToIndex.get(rowKey);
    Integer columnIndex = (Integer)columnKeyToIndex.get(columnKey);
    return (rowIndex == null) || (columnIndex == null) ? null : values[rowIndex.intValue()][columnIndex.intValue()];
  }
  
  public int size()
  {
    return cellRowIndices.length;
  }
  
  Table.Cell<R, C, V> getCell(int index)
  {
    int rowIndex = cellRowIndices[index];
    int columnIndex = cellColumnIndices[index];
    R rowKey = rowKeySet().asList().get(rowIndex);
    C columnKey = columnKeySet().asList().get(columnIndex);
    V value = values[rowIndex][columnIndex];
    return cellOf(rowKey, columnKey, value);
  }
  
  V getValue(int index)
  {
    return values[cellRowIndices[index]][cellColumnIndices[index]];
  }
  
  ImmutableTable.SerializedForm createSerializedForm()
  {
    return ImmutableTable.SerializedForm.create(this, cellRowIndices, cellColumnIndices);
  }
}
