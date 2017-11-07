package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.concurrent.Immutable;













@GwtCompatible
@Immutable
final class SparseImmutableTable<R, C, V>
  extends RegularImmutableTable<R, C, V>
{
  static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(
  
    ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
  

  private final ImmutableMap<R, Map<C, V>> rowMap;
  
  private final ImmutableMap<C, Map<R, V>> columnMap;
  
  private final int[] cellRowIndices;
  
  private final int[] cellColumnInRowIndices;
  

  SparseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
  {
    Map<R, Integer> rowIndex = Maps.indexMap(rowSpace);
    Map<R, Map<C, V>> rows = Maps.newLinkedHashMap();
    for (UnmodifiableIterator localUnmodifiableIterator = rowSpace.iterator(); localUnmodifiableIterator.hasNext();) { row = localUnmodifiableIterator.next();
      rows.put(row, new LinkedHashMap());
    }
    Object columns = Maps.newLinkedHashMap();
    for (R row = columnSpace.iterator(); row.hasNext();) { C col = row.next();
      ((Map)columns).put(col, new LinkedHashMap());
    }
    int[] cellRowIndices = new int[cellList.size()];
    int[] cellColumnInRowIndices = new int[cellList.size()];
    for (int i = 0; i < cellList.size(); i++) {
      cell = (Table.Cell)cellList.get(i);
      R rowKey = cell.getRowKey();
      C columnKey = cell.getColumnKey();
      V value = cell.getValue();
      
      cellRowIndices[i] = ((Integer)rowIndex.get(rowKey)).intValue();
      Map<C, V> thisRow = (Map)rows.get(rowKey);
      cellColumnInRowIndices[i] = thisRow.size();
      V oldValue = thisRow.put(columnKey, value);
      if (oldValue != null) {
        throw new IllegalArgumentException("Duplicate value for row=" + rowKey + ", column=" + columnKey + ": " + value + ", " + oldValue);
      }
      







      ((Map)((Map)columns).get(columnKey)).put(rowKey, value);
    }
    this.cellRowIndices = cellRowIndices;
    this.cellColumnInRowIndices = cellColumnInRowIndices;
    
    ImmutableMap.Builder<R, Map<C, V>> rowBuilder = new ImmutableMap.Builder(rows.size());
    for (Table.Cell<R, C, V> cell = rows.entrySet().iterator(); cell.hasNext();) { row = (Map.Entry)cell.next();
      rowBuilder.put(row.getKey(), ImmutableMap.copyOf((Map)row.getValue())); }
    Map.Entry<R, Map<C, V>> row;
    rowMap = rowBuilder.build();
    

    ImmutableMap.Builder<C, Map<R, V>> columnBuilder = new ImmutableMap.Builder(((Map)columns).size());
    for (Map.Entry<C, Map<R, V>> col : ((Map)columns).entrySet()) {
      columnBuilder.put(col.getKey(), ImmutableMap.copyOf((Map)col.getValue()));
    }
    columnMap = columnBuilder.build();
  }
  
  public ImmutableMap<C, Map<R, V>> columnMap()
  {
    return columnMap;
  }
  
  public ImmutableMap<R, Map<C, V>> rowMap()
  {
    return rowMap;
  }
  
  public int size()
  {
    return cellRowIndices.length;
  }
  
  Table.Cell<R, C, V> getCell(int index)
  {
    int rowIndex = cellRowIndices[index];
    Map.Entry<R, Map<C, V>> rowEntry = (Map.Entry)rowMap.entrySet().asList().get(rowIndex);
    ImmutableMap<C, V> row = (ImmutableMap)rowEntry.getValue();
    int columnIndex = cellColumnInRowIndices[index];
    Map.Entry<C, V> colEntry = (Map.Entry)row.entrySet().asList().get(columnIndex);
    return cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
  }
  
  V getValue(int index)
  {
    int rowIndex = cellRowIndices[index];
    ImmutableMap<C, V> row = (ImmutableMap)rowMap.values().asList().get(rowIndex);
    int columnIndex = cellColumnInRowIndices[index];
    return row.values().asList().get(columnIndex);
  }
  
  ImmutableTable.SerializedForm createSerializedForm()
  {
    Map<C, Integer> columnKeyToIndex = Maps.indexMap(columnKeySet());
    int[] cellColumnIndices = new int[cellSet().size()];
    int i = 0;
    for (UnmodifiableIterator localUnmodifiableIterator = cellSet().iterator(); localUnmodifiableIterator.hasNext();) { Table.Cell<R, C, V> cell = (Table.Cell)localUnmodifiableIterator.next();
      cellColumnIndices[(i++)] = ((Integer)columnKeyToIndex.get(cell.getColumnKey())).intValue();
    }
    return ImmutableTable.SerializedForm.create(this, cellRowIndices, cellColumnIndices);
  }
}
