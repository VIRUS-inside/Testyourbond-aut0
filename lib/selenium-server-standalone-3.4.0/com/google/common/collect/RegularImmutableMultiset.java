package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Collection;
import javax.annotation.Nullable;























@GwtCompatible(serializable=true)
class RegularImmutableMultiset<E>
  extends ImmutableMultiset<E>
{
  static final RegularImmutableMultiset<Object> EMPTY = new RegularImmutableMultiset(
    ImmutableList.of());
  
  private final transient Multisets.ImmutableEntry<E>[] entries;
  private final transient Multisets.ImmutableEntry<E>[] hashTable;
  private final transient int size;
  private final transient int hashCode;
  @LazyInit
  private transient ImmutableSet<E> elementSet;
  
  RegularImmutableMultiset(Collection<? extends Multiset.Entry<? extends E>> entries)
  {
    int distinct = entries.size();
    
    Multisets.ImmutableEntry<E>[] entryArray = new Multisets.ImmutableEntry[distinct];
    if (distinct == 0) {
      this.entries = entryArray;
      this.hashTable = null;
      this.size = 0;
      this.hashCode = 0;
      elementSet = ImmutableSet.of();
    } else {
      int tableSize = Hashing.closedTableSize(distinct, 1.0D);
      int mask = tableSize - 1;
      
      Multisets.ImmutableEntry<E>[] hashTable = new Multisets.ImmutableEntry[tableSize];
      
      int index = 0;
      int hashCode = 0;
      long size = 0L;
      for (Multiset.Entry<? extends E> entry : entries) {
        E element = Preconditions.checkNotNull(entry.getElement());
        int count = entry.getCount();
        int hash = element.hashCode();
        int bucket = Hashing.smear(hash) & mask;
        Multisets.ImmutableEntry<E> bucketHead = hashTable[bucket];
        Multisets.ImmutableEntry<E> newEntry;
        Multisets.ImmutableEntry<E> newEntry; if (bucketHead == null) {
          boolean canReuseEntry = ((entry instanceof Multisets.ImmutableEntry)) && (!(entry instanceof NonTerminalEntry));
          
          newEntry = canReuseEntry ? (Multisets.ImmutableEntry)entry : new Multisets.ImmutableEntry(element, count);

        }
        else
        {
          newEntry = new NonTerminalEntry(element, count, bucketHead);
        }
        hashCode += (hash ^ count);
        entryArray[(index++)] = newEntry;
        hashTable[bucket] = newEntry;
        size += count;
      }
      this.entries = entryArray;
      this.hashTable = hashTable;
      this.size = Ints.saturatedCast(size);
      this.hashCode = hashCode;
    }
  }
  
  private static final class NonTerminalEntry<E> extends Multisets.ImmutableEntry<E> {
    private final Multisets.ImmutableEntry<E> nextInBucket;
    
    NonTerminalEntry(E element, int count, Multisets.ImmutableEntry<E> nextInBucket) {
      super(count);
      this.nextInBucket = nextInBucket;
    }
    
    public Multisets.ImmutableEntry<E> nextInBucket()
    {
      return nextInBucket;
    }
  }
  
  boolean isPartialView()
  {
    return false;
  }
  
  public int count(@Nullable Object element)
  {
    Multisets.ImmutableEntry<E>[] hashTable = this.hashTable;
    if ((element == null) || (hashTable == null)) {
      return 0;
    }
    int hash = Hashing.smearedHash(element);
    int mask = hashTable.length - 1;
    for (Multisets.ImmutableEntry<E> entry = hashTable[(hash & mask)]; 
        entry != null; 
        entry = entry.nextInBucket()) {
      if (Objects.equal(element, entry.getElement())) {
        return entry.getCount();
      }
    }
    return 0;
  }
  
  public int size()
  {
    return size;
  }
  
  public ImmutableSet<E> elementSet()
  {
    ImmutableSet<E> result = elementSet;
    return result == null ? (this.elementSet = new ElementSet(null)) : result;
  }
  
  private final class ElementSet extends ImmutableSet.Indexed<E>
  {
    private ElementSet() {}
    
    E get(int index) {
      return entries[index].getElement();
    }
    
    public boolean contains(@Nullable Object object)
    {
      return RegularImmutableMultiset.this.contains(object);
    }
    
    boolean isPartialView()
    {
      return true;
    }
    
    public int size()
    {
      return entries.length;
    }
  }
  
  Multiset.Entry<E> getEntry(int index)
  {
    return entries[index];
  }
  
  public int hashCode()
  {
    return hashCode;
  }
}
