package org.apache.xerces.impl.xpath.regex;

import java.text.CharacterIterator;

public class BMPattern
{
  final char[] pattern;
  final int[] shiftTable;
  final boolean ignoreCase;
  
  public BMPattern(String paramString, boolean paramBoolean)
  {
    this(paramString, 256, paramBoolean);
  }
  
  public BMPattern(String paramString, int paramInt, boolean paramBoolean)
  {
    pattern = paramString.toCharArray();
    shiftTable = new int[paramInt];
    ignoreCase = paramBoolean;
    int i = pattern.length;
    for (int j = 0; j < shiftTable.length; j++) {
      shiftTable[j] = i;
    }
    for (int k = 0; k < i; k++)
    {
      int m = pattern[k];
      int i2 = i - k - 1;
      int i3 = m % shiftTable.length;
      if (i2 < shiftTable[i3]) {
        shiftTable[i3] = i2;
      }
      if (ignoreCase)
      {
        int n = Character.toUpperCase(m);
        i3 = n % shiftTable.length;
        if (i2 < shiftTable[i3]) {
          shiftTable[i3] = i2;
        }
        int i1 = Character.toLowerCase(n);
        i3 = i1 % shiftTable.length;
        if (i2 < shiftTable[i3]) {
          shiftTable[i3] = i2;
        }
      }
    }
  }
  
  public int matches(CharacterIterator paramCharacterIterator, int paramInt1, int paramInt2)
  {
    if (ignoreCase) {
      return matchesIgnoreCase(paramCharacterIterator, paramInt1, paramInt2);
    }
    int i = pattern.length;
    if (i == 0) {
      return paramInt1;
    }
    int j = paramInt1 + i;
    while (j <= paramInt2)
    {
      int k = i;
      int m = j + 1;
      int n;
      do
      {
        if ((n = paramCharacterIterator.setIndex(--j)) != pattern[(--k)]) {
          break;
        }
        if (k == 0) {
          return j;
        }
      } while (k > 0);
      j += shiftTable[(n % shiftTable.length)] + 1;
      if (j < m) {
        j = m;
      }
    }
    return -1;
  }
  
  public int matches(String paramString, int paramInt1, int paramInt2)
  {
    if (ignoreCase) {
      return matchesIgnoreCase(paramString, paramInt1, paramInt2);
    }
    int i = pattern.length;
    if (i == 0) {
      return paramInt1;
    }
    int j = paramInt1 + i;
    while (j <= paramInt2)
    {
      int k = i;
      int m = j + 1;
      int n;
      do
      {
        if ((n = paramString.charAt(--j)) != pattern[(--k)]) {
          break;
        }
        if (k == 0) {
          return j;
        }
      } while (k > 0);
      j += shiftTable[(n % shiftTable.length)] + 1;
      if (j < m) {
        j = m;
      }
    }
    return -1;
  }
  
  public int matches(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if (ignoreCase) {
      return matchesIgnoreCase(paramArrayOfChar, paramInt1, paramInt2);
    }
    int i = pattern.length;
    if (i == 0) {
      return paramInt1;
    }
    int j = paramInt1 + i;
    while (j <= paramInt2)
    {
      int k = i;
      int m = j + 1;
      int n;
      do
      {
        if ((n = paramArrayOfChar[(--j)]) != pattern[(--k)]) {
          break;
        }
        if (k == 0) {
          return j;
        }
      } while (k > 0);
      j += shiftTable[(n % shiftTable.length)] + 1;
      if (j < m) {
        j = m;
      }
    }
    return -1;
  }
  
  int matchesIgnoreCase(CharacterIterator paramCharacterIterator, int paramInt1, int paramInt2)
  {
    int i = pattern.length;
    if (i == 0) {
      return paramInt1;
    }
    int j = paramInt1 + i;
    while (j <= paramInt2)
    {
      int k = i;
      int m = j + 1;
      int n;
      do
      {
        char c1 = n = paramCharacterIterator.setIndex(--j);
        char c2 = pattern[(--k)];
        if (c1 != c2)
        {
          c1 = Character.toUpperCase(c1);
          c2 = Character.toUpperCase(c2);
          if ((c1 != c2) && (Character.toLowerCase(c1) != Character.toLowerCase(c2))) {
            break;
          }
        }
        if (k == 0) {
          return j;
        }
      } while (k > 0);
      j += shiftTable[(n % shiftTable.length)] + 1;
      if (j < m) {
        j = m;
      }
    }
    return -1;
  }
  
  int matchesIgnoreCase(String paramString, int paramInt1, int paramInt2)
  {
    int i = pattern.length;
    if (i == 0) {
      return paramInt1;
    }
    int j = paramInt1 + i;
    while (j <= paramInt2)
    {
      int k = i;
      int m = j + 1;
      int n;
      do
      {
        char c1 = n = paramString.charAt(--j);
        char c2 = pattern[(--k)];
        if (c1 != c2)
        {
          c1 = Character.toUpperCase(c1);
          c2 = Character.toUpperCase(c2);
          if ((c1 != c2) && (Character.toLowerCase(c1) != Character.toLowerCase(c2))) {
            break;
          }
        }
        if (k == 0) {
          return j;
        }
      } while (k > 0);
      j += shiftTable[(n % shiftTable.length)] + 1;
      if (j < m) {
        j = m;
      }
    }
    return -1;
  }
  
  int matchesIgnoreCase(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = pattern.length;
    if (i == 0) {
      return paramInt1;
    }
    int j = paramInt1 + i;
    while (j <= paramInt2)
    {
      int k = i;
      int m = j + 1;
      int n;
      do
      {
        char c1 = n = paramArrayOfChar[(--j)];
        char c2 = pattern[(--k)];
        if (c1 != c2)
        {
          c1 = Character.toUpperCase(c1);
          c2 = Character.toUpperCase(c2);
          if ((c1 != c2) && (Character.toLowerCase(c1) != Character.toLowerCase(c2))) {
            break;
          }
        }
        if (k == 0) {
          return j;
        }
      } while (k > 0);
      j += shiftTable[(n % shiftTable.length)] + 1;
      if (j < m) {
        j = m;
      }
    }
    return -1;
  }
}
