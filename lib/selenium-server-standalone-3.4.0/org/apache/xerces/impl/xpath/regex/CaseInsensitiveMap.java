package org.apache.xerces.impl.xpath.regex;

final class CaseInsensitiveMap
{
  private static int CHUNK_SHIFT = 10;
  private static int CHUNK_SIZE = 1 << CHUNK_SHIFT;
  private static int CHUNK_MASK = CHUNK_SIZE - 1;
  private static int INITIAL_CHUNK_COUNT = 64;
  private static int[][][] caseInsensitiveMap;
  private static int LOWER_CASE_MATCH = 1;
  private static int UPPER_CASE_MATCH = 2;
  
  CaseInsensitiveMap() {}
  
  public static int[] get(int paramInt)
  {
    return paramInt < 65536 ? getMapping(paramInt) : null;
  }
  
  private static int[] getMapping(int paramInt)
  {
    int i = paramInt >>> CHUNK_SHIFT;
    int j = paramInt & CHUNK_MASK;
    return caseInsensitiveMap[i][j];
  }
  
  private static void buildCaseInsensitiveMap()
  {
    caseInsensitiveMap = new int[INITIAL_CHUNK_COUNT][CHUNK_SIZE][];
    for (int k = 0; k < 65536; k++)
    {
      int i = Character.toLowerCase((char)k);
      int j = Character.toUpperCase((char)k);
      if ((i != j) || (i != k))
      {
        int[] arrayOfInt1 = new int[2];
        int m = 0;
        int[] arrayOfInt2;
        if (i != k)
        {
          arrayOfInt1[(m++)] = i;
          arrayOfInt1[(m++)] = LOWER_CASE_MATCH;
          arrayOfInt2 = getMapping(i);
          if (arrayOfInt2 != null) {
            arrayOfInt1 = updateMap(k, arrayOfInt1, i, arrayOfInt2, LOWER_CASE_MATCH);
          }
        }
        if (j != k)
        {
          if (m == arrayOfInt1.length) {
            arrayOfInt1 = expandMap(arrayOfInt1, 2);
          }
          arrayOfInt1[(m++)] = j;
          arrayOfInt1[(m++)] = UPPER_CASE_MATCH;
          arrayOfInt2 = getMapping(j);
          if (arrayOfInt2 != null) {
            arrayOfInt1 = updateMap(k, arrayOfInt1, j, arrayOfInt2, UPPER_CASE_MATCH);
          }
        }
        set(k, arrayOfInt1);
      }
    }
  }
  
  private static int[] expandMap(int[] paramArrayOfInt, int paramInt)
  {
    int i = paramArrayOfInt.length;
    int[] arrayOfInt = new int[i + paramInt];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, i);
    return arrayOfInt;
  }
  
  private static void set(int paramInt, int[] paramArrayOfInt)
  {
    int i = paramInt >>> CHUNK_SHIFT;
    int j = paramInt & CHUNK_MASK;
    caseInsensitiveMap[i][j] = paramArrayOfInt;
  }
  
  private static int[] updateMap(int paramInt1, int[] paramArrayOfInt1, int paramInt2, int[] paramArrayOfInt2, int paramInt3)
  {
    for (int i = 0; i < paramArrayOfInt2.length; i += 2)
    {
      int j = paramArrayOfInt2[i];
      int[] arrayOfInt = getMapping(j);
      if ((arrayOfInt != null) && (contains(arrayOfInt, paramInt2, paramInt3)))
      {
        if (!contains(arrayOfInt, paramInt1))
        {
          arrayOfInt = expandAndAdd(arrayOfInt, paramInt1, paramInt3);
          set(j, arrayOfInt);
        }
        if (!contains(paramArrayOfInt1, j)) {
          paramArrayOfInt1 = expandAndAdd(paramArrayOfInt1, j, paramInt3);
        }
      }
    }
    if (!contains(paramArrayOfInt2, paramInt1))
    {
      paramArrayOfInt2 = expandAndAdd(paramArrayOfInt2, paramInt1, paramInt3);
      set(paramInt2, paramArrayOfInt2);
    }
    return paramArrayOfInt1;
  }
  
  private static boolean contains(int[] paramArrayOfInt, int paramInt)
  {
    for (int i = 0; i < paramArrayOfInt.length; i += 2) {
      if (paramArrayOfInt[i] == paramInt) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean contains(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    for (int i = 0; i < paramArrayOfInt.length; i += 2) {
      if ((paramArrayOfInt[i] == paramInt1) && (paramArrayOfInt[(i + 1)] == paramInt2)) {
        return true;
      }
    }
    return false;
  }
  
  private static int[] expandAndAdd(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    int i = paramArrayOfInt.length;
    int[] arrayOfInt = new int[i + 2];
    System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, i);
    arrayOfInt[i] = paramInt1;
    arrayOfInt[(i + 1)] = paramInt2;
    return arrayOfInt;
  }
  
  static
  {
    buildCaseInsensitiveMap();
  }
}
