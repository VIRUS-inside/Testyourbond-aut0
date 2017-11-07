package org.apache.xerces.util;

public class SymbolTable
{
  protected static final int TABLE_SIZE = 101;
  protected Entry[] fBuckets = null;
  protected int fTableSize;
  protected transient int fCount;
  protected int fThreshold;
  protected float fLoadFactor;
  
  public SymbolTable(int paramInt, float paramFloat)
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
    fBuckets = new Entry[fTableSize];
    fThreshold = ((int)(fTableSize * paramFloat));
    fCount = 0;
  }
  
  public SymbolTable(int paramInt)
  {
    this(paramInt, 0.75F);
  }
  
  public SymbolTable()
  {
    this(101, 0.75F);
  }
  
  public String addSymbol(String paramString)
  {
    int i = hash(paramString) % fTableSize;
    for (Entry localEntry1 = fBuckets[i]; localEntry1 != null; localEntry1 = next) {
      if (symbol.equals(paramString)) {
        return symbol;
      }
    }
    if (fCount >= fThreshold)
    {
      rehash();
      i = hash(paramString) % fTableSize;
    }
    Entry localEntry2 = new Entry(paramString, fBuckets[i]);
    fBuckets[i] = localEntry2;
    fCount += 1;
    return symbol;
  }
  
  public String addSymbol(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = hash(paramArrayOfChar, paramInt1, paramInt2) % fTableSize;
    for (Entry localEntry1 = fBuckets[i]; localEntry1 != null; localEntry1 = next) {
      if (paramInt2 == characters.length)
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
    Entry localEntry2 = new Entry(paramArrayOfChar, paramInt1, paramInt2, fBuckets[i]);
    fBuckets[i] = localEntry2;
    fCount += 1;
    return symbol;
  }
  
  public int hash(String paramString)
  {
    return paramString.hashCode() & 0x7FFFFFFF;
  }
  
  public int hash(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = 0; j < paramInt2; j++) {
      i = i * 31 + paramArrayOfChar[(paramInt1 + j)];
    }
    return i & 0x7FFFFFFF;
  }
  
  protected void rehash()
  {
    int i = fBuckets.length;
    Entry[] arrayOfEntry1 = fBuckets;
    int j = i * 2 + 1;
    Entry[] arrayOfEntry2 = new Entry[j];
    fThreshold = ((int)(j * fLoadFactor));
    fBuckets = arrayOfEntry2;
    fTableSize = fBuckets.length;
    int k = i;
    while (k-- > 0)
    {
      Entry localEntry1 = arrayOfEntry1[k];
      while (localEntry1 != null)
      {
        Entry localEntry2 = localEntry1;
        localEntry1 = next;
        int m = hash(characters, 0, characters.length) % j;
        next = arrayOfEntry2[m];
        arrayOfEntry2[m] = localEntry2;
      }
    }
  }
  
  public boolean containsSymbol(String paramString)
  {
    int i = hash(paramString) % fTableSize;
    int j = paramString.length();
    for (Entry localEntry = fBuckets[i]; localEntry != null; localEntry = next) {
      if (j == characters.length)
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
    for (Entry localEntry = fBuckets[i]; localEntry != null; localEntry = next) {
      if (paramInt2 == characters.length)
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
  
  protected static final class Entry
  {
    public final String symbol;
    public final char[] characters;
    public Entry next;
    
    public Entry(String paramString, Entry paramEntry)
    {
      symbol = paramString.intern();
      characters = new char[paramString.length()];
      paramString.getChars(0, characters.length, characters, 0);
      next = paramEntry;
    }
    
    public Entry(char[] paramArrayOfChar, int paramInt1, int paramInt2, Entry paramEntry)
    {
      characters = new char[paramInt2];
      System.arraycopy(paramArrayOfChar, paramInt1, characters, 0, paramInt2);
      symbol = new String(characters).intern();
      next = paramEntry;
    }
  }
}
