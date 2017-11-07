package org.apache.xerces.util;

public class SymbolHash
{
  protected int fTableSize = 101;
  protected Entry[] fBuckets;
  protected int fNum = 0;
  
  public SymbolHash()
  {
    fBuckets = new Entry[fTableSize];
  }
  
  public SymbolHash(int paramInt)
  {
    fTableSize = paramInt;
    fBuckets = new Entry[fTableSize];
  }
  
  public void put(Object paramObject1, Object paramObject2)
  {
    int i = (paramObject1.hashCode() & 0x7FFFFFFF) % fTableSize;
    Entry localEntry = search(paramObject1, i);
    if (localEntry != null)
    {
      value = paramObject2;
    }
    else
    {
      localEntry = new Entry(paramObject1, paramObject2, fBuckets[i]);
      fBuckets[i] = localEntry;
      fNum += 1;
    }
  }
  
  public Object get(Object paramObject)
  {
    int i = (paramObject.hashCode() & 0x7FFFFFFF) % fTableSize;
    Entry localEntry = search(paramObject, i);
    if (localEntry != null) {
      return value;
    }
    return null;
  }
  
  public int getLength()
  {
    return fNum;
  }
  
  public int getValues(Object[] paramArrayOfObject, int paramInt)
  {
    int i = 0;
    int j = 0;
    while ((i < fTableSize) && (j < fNum))
    {
      for (Entry localEntry = fBuckets[i]; localEntry != null; localEntry = next)
      {
        paramArrayOfObject[(paramInt + j)] = value;
        j++;
      }
      i++;
    }
    return fNum;
  }
  
  public Object[] getEntries()
  {
    Object[] arrayOfObject = new Object[fNum << 1];
    int i = 0;
    int j = 0;
    while ((i < fTableSize) && (j < fNum << 1))
    {
      for (Entry localEntry = fBuckets[i]; localEntry != null; localEntry = next)
      {
        arrayOfObject[j] = key;
        arrayOfObject[(++j)] = value;
        j++;
      }
      i++;
    }
    return arrayOfObject;
  }
  
  public SymbolHash makeClone()
  {
    SymbolHash localSymbolHash = new SymbolHash(fTableSize);
    fNum = fNum;
    for (int i = 0; i < fTableSize; i++) {
      if (fBuckets[i] != null) {
        fBuckets[i] = fBuckets[i].makeClone();
      }
    }
    return localSymbolHash;
  }
  
  public void clear()
  {
    for (int i = 0; i < fTableSize; i++) {
      fBuckets[i] = null;
    }
    fNum = 0;
  }
  
  protected Entry search(Object paramObject, int paramInt)
  {
    for (Entry localEntry = fBuckets[paramInt]; localEntry != null; localEntry = next) {
      if (paramObject.equals(key)) {
        return localEntry;
      }
    }
    return null;
  }
  
  protected static final class Entry
  {
    public Object key;
    public Object value;
    public Entry next;
    
    public Entry()
    {
      key = null;
      value = null;
      next = null;
    }
    
    public Entry(Object paramObject1, Object paramObject2, Entry paramEntry)
    {
      key = paramObject1;
      value = paramObject2;
      next = paramEntry;
    }
    
    public Entry makeClone()
    {
      Entry localEntry = new Entry();
      key = key;
      value = value;
      if (next != null) {
        next = next.makeClone();
      }
      return localEntry;
    }
  }
}
