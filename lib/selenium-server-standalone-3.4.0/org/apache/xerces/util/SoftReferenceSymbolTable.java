package org.apache.xerces.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class SoftReferenceSymbolTable
  extends SymbolTable
{
  protected SREntry[] fBuckets = null;
  private final ReferenceQueue fReferenceQueue;
  
  public SoftReferenceSymbolTable(int paramInt, float paramFloat)
  {
    if (paramInt < 0) {
      throw new IllegalArgumentException("Illegal Capacity: " + paramInt);
    }
    if ((paramFloat <= 0.0F) || (Float.isNaN(paramFloat))) {
      throw new IllegalArgumentException("Illegal Load: " + paramFloat);
    }
    if (paramInt == 0) {
      paramInt = 1;
    }
    fLoadFactor = paramFloat;
    fTableSize = paramInt;
    fBuckets = new SREntry[fTableSize];
    fThreshold = ((int)(fTableSize * paramFloat));
    fCount = 0;
    fReferenceQueue = new ReferenceQueue();
  }
  
  public SoftReferenceSymbolTable(int paramInt)
  {
    this(paramInt, 0.75F);
  }
  
  public SoftReferenceSymbolTable()
  {
    this(101, 0.75F);
  }
  
  public String addSymbol(String paramString)
  {
    clean();
    int i = hash(paramString) % fTableSize;
    for (SREntry localSREntry = fBuckets[i]; localSREntry != null; localSREntry = next)
    {
      localObject = (SREntryData)localSREntry.get();
      if ((localObject != null) && (symbol.equals(paramString))) {
        return symbol;
      }
    }
    if (fCount >= fThreshold)
    {
      rehash();
      i = hash(paramString) % fTableSize;
    }
    paramString = paramString.intern();
    Object localObject = new SREntry(paramString, fBuckets[i], i, fReferenceQueue);
    fBuckets[i] = localObject;
    fCount += 1;
    return paramString;
  }
  
  public String addSymbol(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    clean();
    int i = hash(paramArrayOfChar, paramInt1, paramInt2) % fTableSize;
    for (SREntry localSREntry1 = fBuckets[i]; localSREntry1 != null; localSREntry1 = next)
    {
      localObject = (SREntryData)localSREntry1.get();
      if ((localObject != null) && (paramInt2 == characters.length))
      {
        int j = 0;
        while (paramArrayOfChar[(paramInt1 + j)] == characters[j])
        {
          j++;
          if (j >= paramInt2) {
            return symbol;
          }
        }
      }
    }
    if (fCount >= fThreshold)
    {
      rehash();
      i = hash(paramArrayOfChar, paramInt1, paramInt2) % fTableSize;
    }
    Object localObject = new String(paramArrayOfChar, paramInt1, paramInt2).intern();
    SREntry localSREntry2 = new SREntry((String)localObject, paramArrayOfChar, paramInt1, paramInt2, fBuckets[i], i, fReferenceQueue);
    fBuckets[i] = localSREntry2;
    fCount += 1;
    return localObject;
  }
  
  protected void rehash()
  {
    int i = fBuckets.length;
    SREntry[] arrayOfSREntry1 = fBuckets;
    int j = i * 2 + 1;
    SREntry[] arrayOfSREntry2 = new SREntry[j];
    fThreshold = ((int)(j * fLoadFactor));
    fBuckets = arrayOfSREntry2;
    fTableSize = fBuckets.length;
    int k = i;
    while (k-- > 0)
    {
      SREntry localSREntry1 = arrayOfSREntry1[k];
      while (localSREntry1 != null)
      {
        SREntry localSREntry2 = localSREntry1;
        localSREntry1 = next;
        SREntryData localSREntryData = (SREntryData)localSREntry2.get();
        if (localSREntryData != null)
        {
          int m = hash(characters, 0, characters.length) % j;
          if (arrayOfSREntry2[m] != null) {
            prev = localSREntry2;
          }
          next = arrayOfSREntry2[m];
          prev = null;
          arrayOfSREntry2[m] = localSREntry2;
        }
        else
        {
          fCount -= 1;
        }
      }
    }
  }
  
  public boolean containsSymbol(String paramString)
  {
    int i = hash(paramString) % fTableSize;
    int j = paramString.length();
    for (SREntry localSREntry = fBuckets[i]; localSREntry != null; localSREntry = next)
    {
      SREntryData localSREntryData = (SREntryData)localSREntry.get();
      if ((localSREntryData != null) && (j == characters.length))
      {
        int k = 0;
        while (paramString.charAt(k) == characters[k])
        {
          k++;
          if (k >= j) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  public boolean containsSymbol(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = hash(paramArrayOfChar, paramInt1, paramInt2) % fTableSize;
    for (SREntry localSREntry = fBuckets[i]; localSREntry != null; localSREntry = next)
    {
      SREntryData localSREntryData = (SREntryData)localSREntry.get();
      if ((localSREntryData != null) && (paramInt2 == characters.length))
      {
        int j = 0;
        while (paramArrayOfChar[(paramInt1 + j)] == characters[j])
        {
          j++;
          if (j >= paramInt2) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  private void removeEntry(SREntry paramSREntry)
  {
    if (next != null) {
      next.prev = prev;
    }
    if (prev != null) {
      prev.next = next;
    } else {
      fBuckets[bucket] = next;
    }
    fCount -= 1;
  }
  
  private void clean()
  {
    for (SREntry localSREntry = (SREntry)fReferenceQueue.poll(); localSREntry != null; localSREntry = (SREntry)fReferenceQueue.poll()) {
      removeEntry(localSREntry);
    }
  }
  
  protected static final class SREntryData
  {
    public final String symbol;
    public final char[] characters;
    
    public SREntryData(String paramString)
    {
      symbol = paramString;
      characters = new char[symbol.length()];
      symbol.getChars(0, characters.length, characters, 0);
    }
    
    public SREntryData(String paramString, char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      symbol = paramString;
      characters = new char[paramInt2];
      System.arraycopy(paramArrayOfChar, paramInt1, characters, 0, paramInt2);
    }
  }
  
  protected static final class SREntry
    extends SoftReference
  {
    public SREntry next;
    public SREntry prev;
    public int bucket;
    
    public SREntry(String paramString, SREntry paramSREntry, int paramInt, ReferenceQueue paramReferenceQueue)
    {
      super(paramReferenceQueue);
      initialize(paramSREntry, paramInt);
    }
    
    public SREntry(String paramString, char[] paramArrayOfChar, int paramInt1, int paramInt2, SREntry paramSREntry, int paramInt3, ReferenceQueue paramReferenceQueue)
    {
      super(paramReferenceQueue);
      initialize(paramSREntry, paramInt3);
    }
    
    private void initialize(SREntry paramSREntry, int paramInt)
    {
      next = paramSREntry;
      if (paramSREntry != null) {
        prev = this;
      }
      prev = null;
      bucket = paramInt;
    }
  }
}
