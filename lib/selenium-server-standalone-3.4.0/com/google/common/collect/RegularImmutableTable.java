package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

























@GwtCompatible
abstract class RegularImmutableTable<R, C, V>
  extends ImmutableTable<R, C, V>
{
  RegularImmutableTable() {}
  
  abstract Table.Cell<R, C, V> getCell(int paramInt);
  
  final ImmutableSet<Table.Cell<R, C, V>> createCellSet() { return isEmpty() ? ImmutableSet.of() : new CellSet(null); }
  
  abstract V getValue(int paramInt);
  
  private final class CellSet extends ImmutableSet.Indexed<Table.Cell<R, C, V>> {
    private CellSet() {}
    
    public int size() { return RegularImmutableTable.this.size(); }
    

    Table.Cell<R, C, V> get(int index)
    {
      return getCell(index);
    }
    
    public boolean contains(@Nullable Object object)
    {
      if ((object instanceof Table.Cell)) {
        Table.Cell<?, ?, ?> cell = (Table.Cell)object;
        Object value = get(cell.getRowKey(), cell.getColumnKey());
        return (value != null) && (value.equals(cell.getValue()));
      }
      return false;
    }
    
    boolean isPartialView()
    {
      return false;
    }
  }
  


  final ImmutableCollection<V> createValues()
  {
    return isEmpty() ? ImmutableList.of() : new Values(null);
  }
  
  private final class Values extends ImmutableList<V> {
    private Values() {}
    
    public int size() {
      return RegularImmutableTable.this.size();
    }
    
    public V get(int index)
    {
      return getValue(index);
    }
    
    boolean isPartialView()
    {
      return true;
    }
  }
  


  static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator)
  {
    Preconditions.checkNotNull(cells);
    if ((rowComparator != null) || (columnComparator != null))
    {






      Comparator<Table.Cell<R, C, V>> comparator = new Comparator()
      {


        public int compare(Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2)
        {

          int rowCompare = val$rowComparator == null ? 0 : val$rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
          if (rowCompare != 0) {
            return rowCompare;
          }
          return columnComparator == null ? 0 : columnComparator
          
            .compare(cell1.getColumnKey(), cell2.getColumnKey());
        }
      };
      Collections.sort(cells, comparator);
    }
    return forCellsInternal(cells, rowComparator, columnComparator);
  }
  
  static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
    return forCellsInternal(cells, null, null);
  }
  


  private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable Comparator<? super C> columnComparator)
  {
    Set<R> rowSpaceBuilder = new LinkedHashSet();
    Set<C> columnSpaceBuilder = new LinkedHashSet();
    ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
    for (Table.Cell<R, C, V> cell : cells) {
      rowSpaceBuilder.add(cell.getRowKey());
      columnSpaceBuilder.add(cell.getColumnKey());
    }
    



    Object rowSpace = rowComparator == null ? ImmutableSet.copyOf(rowSpaceBuilder) : ImmutableSet.copyOf(ImmutableList.sortedCopyOf(rowComparator, rowSpaceBuilder));
    


    ImmutableSet<C> columnSpace = columnComparator == null ? ImmutableSet.copyOf(columnSpaceBuilder) : ImmutableSet.copyOf(ImmutableList.sortedCopyOf(columnComparator, columnSpaceBuilder));
    
    return forOrderedComponents(cellList, (ImmutableSet)rowSpace, columnSpace);
  }
  





  static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
  {
    return cellList.size() > rowSpace.size() * columnSpace.size() / 2L ? new DenseImmutableTable(cellList, rowSpace, columnSpace) : new SparseImmutableTable(cellList, rowSpace, columnSpace);
  }
}
