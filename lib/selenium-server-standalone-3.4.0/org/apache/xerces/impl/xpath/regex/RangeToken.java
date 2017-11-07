package org.apache.xerces.impl.xpath.regex;

import java.io.PrintStream;
import java.io.Serializable;

final class RangeToken
  extends Token
  implements Serializable
{
  private static final long serialVersionUID = -553983121197679934L;
  int[] ranges;
  boolean sorted;
  boolean compacted;
  RangeToken icaseCache = null;
  int[] map = null;
  int nonMapIndex;
  private static final int MAPSIZE = 256;
  
  RangeToken(int paramInt)
  {
    super(paramInt);
    setSorted(false);
  }
  
  protected void addRange(int paramInt1, int paramInt2)
  {
    icaseCache = null;
    int i;
    int j;
    if (paramInt1 <= paramInt2)
    {
      i = paramInt1;
      j = paramInt2;
    }
    else
    {
      i = paramInt2;
      j = paramInt1;
    }
    int k = 0;
    if (ranges == null)
    {
      ranges = new int[2];
      ranges[0] = i;
      ranges[1] = j;
      setSorted(true);
    }
    else
    {
      k = ranges.length;
      if (ranges[(k - 1)] + 1 == i)
      {
        ranges[(k - 1)] = j;
        return;
      }
      int[] arrayOfInt = new int[k + 2];
      System.arraycopy(ranges, 0, arrayOfInt, 0, k);
      ranges = arrayOfInt;
      if (ranges[(k - 1)] >= i) {
        setSorted(false);
      }
      ranges[(k++)] = i;
      ranges[k] = j;
      if (!sorted) {
        sortRanges();
      }
    }
  }
  
  private final boolean isSorted()
  {
    return sorted;
  }
  
  private final void setSorted(boolean paramBoolean)
  {
    sorted = paramBoolean;
    if (!paramBoolean) {
      compacted = false;
    }
  }
  
  private final boolean isCompacted()
  {
    return compacted;
  }
  
  private final void setCompacted()
  {
    compacted = true;
  }
  
  protected void sortRanges()
  {
    if (isSorted()) {
      return;
    }
    if (ranges == null) {
      return;
    }
    for (int i = ranges.length - 4; i >= 0; i -= 2) {
      for (int j = 0; j <= i; j += 2) {
        if ((ranges[j] > ranges[(j + 2)]) || ((ranges[j] == ranges[(j + 2)]) && (ranges[(j + 1)] > ranges[(j + 3)])))
        {
          int k = ranges[(j + 2)];
          ranges[(j + 2)] = ranges[j];
          ranges[j] = k;
          k = ranges[(j + 3)];
          ranges[(j + 3)] = ranges[(j + 1)];
          ranges[(j + 1)] = k;
        }
      }
    }
    setSorted(true);
  }
  
  protected void compactRanges()
  {
    int i = 0;
    if ((ranges == null) || (ranges.length <= 2)) {
      return;
    }
    if (isCompacted()) {
      return;
    }
    int j = 0;
    int k = 0;
    while (k < ranges.length)
    {
      if (j != k)
      {
        ranges[j] = ranges[(k++)];
        ranges[(j + 1)] = ranges[(k++)];
      }
      else
      {
        k += 2;
      }
      int m = ranges[(j + 1)];
      while (k < ranges.length)
      {
        if (m + 1 < ranges[k]) {
          break;
        }
        if (m + 1 == ranges[k])
        {
          if (i != 0) {
            System.err.println("Token#compactRanges(): Compaction: [" + ranges[j] + ", " + ranges[(j + 1)] + "], [" + ranges[k] + ", " + ranges[(k + 1)] + "] -> [" + ranges[j] + ", " + ranges[(k + 1)] + "]");
          }
          ranges[(j + 1)] = ranges[(k + 1)];
          m = ranges[(j + 1)];
          k += 2;
        }
        else if (m >= ranges[(k + 1)])
        {
          if (i != 0) {
            System.err.println("Token#compactRanges(): Compaction: [" + ranges[j] + ", " + ranges[(j + 1)] + "], [" + ranges[k] + ", " + ranges[(k + 1)] + "] -> [" + ranges[j] + ", " + ranges[(j + 1)] + "]");
          }
          k += 2;
        }
        else if (m < ranges[(k + 1)])
        {
          if (i != 0) {
            System.err.println("Token#compactRanges(): Compaction: [" + ranges[j] + ", " + ranges[(j + 1)] + "], [" + ranges[k] + ", " + ranges[(k + 1)] + "] -> [" + ranges[j] + ", " + ranges[(k + 1)] + "]");
          }
          ranges[(j + 1)] = ranges[(k + 1)];
          m = ranges[(j + 1)];
          k += 2;
        }
        else
        {
          throw new RuntimeException("Token#compactRanges(): Internel Error: [" + ranges[j] + "," + ranges[(j + 1)] + "] [" + ranges[k] + "," + ranges[(k + 1)] + "]");
        }
      }
      j += 2;
    }
    if (j != ranges.length)
    {
      int[] arrayOfInt = new int[j];
      System.arraycopy(ranges, 0, arrayOfInt, 0, j);
      ranges = arrayOfInt;
    }
    setCompacted();
  }
  
  protected void mergeRanges(Token paramToken)
  {
    RangeToken localRangeToken = (RangeToken)paramToken;
    sortRanges();
    localRangeToken.sortRanges();
    if (ranges == null) {
      return;
    }
    icaseCache = null;
    setSorted(true);
    if (ranges == null)
    {
      ranges = new int[ranges.length];
      System.arraycopy(ranges, 0, ranges, 0, ranges.length);
      return;
    }
    int[] arrayOfInt = new int[ranges.length + ranges.length];
    int i = 0;
    int j = 0;
    int k = 0;
    while ((i < ranges.length) || (j < ranges.length)) {
      if (i >= ranges.length)
      {
        arrayOfInt[(k++)] = ranges[(j++)];
        arrayOfInt[(k++)] = ranges[(j++)];
      }
      else if (j >= ranges.length)
      {
        arrayOfInt[(k++)] = ranges[(i++)];
        arrayOfInt[(k++)] = ranges[(i++)];
      }
      else if ((ranges[j] < ranges[i]) || ((ranges[j] == ranges[i]) && (ranges[(j + 1)] < ranges[(i + 1)])))
      {
        arrayOfInt[(k++)] = ranges[(j++)];
        arrayOfInt[(k++)] = ranges[(j++)];
      }
      else
      {
        arrayOfInt[(k++)] = ranges[(i++)];
        arrayOfInt[(k++)] = ranges[(i++)];
      }
    }
    ranges = arrayOfInt;
  }
  
  protected void subtractRanges(Token paramToken)
  {
    if (type == 5)
    {
      intersectRanges(paramToken);
      return;
    }
    RangeToken localRangeToken = (RangeToken)paramToken;
    if ((ranges == null) || (ranges == null)) {
      return;
    }
    icaseCache = null;
    sortRanges();
    compactRanges();
    localRangeToken.sortRanges();
    localRangeToken.compactRanges();
    int[] arrayOfInt = new int[ranges.length + ranges.length];
    int i = 0;
    int j = 0;
    int k = 0;
    do
    {
      int m = ranges[j];
      int n = ranges[(j + 1)];
      int i1 = ranges[k];
      int i2 = ranges[(k + 1)];
      if (n < i1)
      {
        arrayOfInt[(i++)] = ranges[(j++)];
        arrayOfInt[(i++)] = ranges[(j++)];
      }
      else if ((n >= i1) && (m <= i2))
      {
        if ((i1 <= m) && (n <= i2))
        {
          j += 2;
        }
        else if (i1 <= m)
        {
          ranges[j] = (i2 + 1);
          k += 2;
        }
        else if (n <= i2)
        {
          arrayOfInt[(i++)] = m;
          arrayOfInt[(i++)] = (i1 - 1);
          j += 2;
        }
        else
        {
          arrayOfInt[(i++)] = m;
          arrayOfInt[(i++)] = (i1 - 1);
          ranges[j] = (i2 + 1);
          k += 2;
        }
      }
      else if (i2 < m)
      {
        k += 2;
      }
      else
      {
        throw new RuntimeException("Token#subtractRanges(): Internal Error: [" + ranges[j] + "," + ranges[(j + 1)] + "] - [" + ranges[k] + "," + ranges[(k + 1)] + "]");
      }
      if (j >= ranges.length) {
        break;
      }
    } while (k < ranges.length);
    while (j < ranges.length)
    {
      arrayOfInt[(i++)] = ranges[(j++)];
      arrayOfInt[(i++)] = ranges[(j++)];
    }
    ranges = new int[i];
    System.arraycopy(arrayOfInt, 0, ranges, 0, i);
  }
  
  protected void intersectRanges(Token paramToken)
  {
    RangeToken localRangeToken = (RangeToken)paramToken;
    if ((ranges == null) || (ranges == null)) {
      return;
    }
    icaseCache = null;
    sortRanges();
    compactRanges();
    localRangeToken.sortRanges();
    localRangeToken.compactRanges();
    int[] arrayOfInt = new int[ranges.length + ranges.length];
    int i = 0;
    int j = 0;
    int k = 0;
    do
    {
      int m = ranges[j];
      int n = ranges[(j + 1)];
      int i1 = ranges[k];
      int i2 = ranges[(k + 1)];
      if (n < i1) {
        j += 2;
      } else if ((n >= i1) && (m <= i2))
      {
        if ((i1 <= m) && (n <= i2))
        {
          arrayOfInt[(i++)] = m;
          arrayOfInt[(i++)] = n;
          j += 2;
        }
        else if (i1 <= m)
        {
          arrayOfInt[(i++)] = m;
          arrayOfInt[(i++)] = i2;
          ranges[j] = (i2 + 1);
          k += 2;
        }
        else if (n <= i2)
        {
          arrayOfInt[(i++)] = i1;
          arrayOfInt[(i++)] = n;
          j += 2;
        }
        else
        {
          arrayOfInt[(i++)] = i1;
          arrayOfInt[(i++)] = i2;
          ranges[j] = (i2 + 1);
        }
      }
      else if (i2 < m) {
        k += 2;
      } else {
        throw new RuntimeException("Token#intersectRanges(): Internal Error: [" + ranges[j] + "," + ranges[(j + 1)] + "] & [" + ranges[k] + "," + ranges[(k + 1)] + "]");
      }
      if (j >= ranges.length) {
        break;
      }
    } while (k < ranges.length);
    while (j < ranges.length)
    {
      arrayOfInt[(i++)] = ranges[(j++)];
      arrayOfInt[(i++)] = ranges[(j++)];
    }
    ranges = new int[i];
    System.arraycopy(arrayOfInt, 0, ranges, 0, i);
  }
  
  static Token complementRanges(Token paramToken)
  {
    if ((type != 4) && (type != 5)) {
      throw new IllegalArgumentException("Token#complementRanges(): must be RANGE: " + type);
    }
    RangeToken localRangeToken1 = (RangeToken)paramToken;
    localRangeToken1.sortRanges();
    localRangeToken1.compactRanges();
    int i = ranges.length + 2;
    if (ranges[0] == 0) {
      i -= 2;
    }
    int j = ranges[(ranges.length - 1)];
    if (j == 1114111) {
      i -= 2;
    }
    RangeToken localRangeToken2 = Token.createRange();
    ranges = new int[i];
    int k = 0;
    if (ranges[0] > 0)
    {
      ranges[(k++)] = 0;
      ranges[(k++)] = (ranges[0] - 1);
    }
    for (int m = 1; m < ranges.length - 2; m += 2)
    {
      ranges[(k++)] = (ranges[m] + 1);
      ranges[(k++)] = (ranges[(m + 1)] - 1);
    }
    if (j != 1114111)
    {
      ranges[(k++)] = (j + 1);
      ranges[k] = 1114111;
    }
    localRangeToken2.setCompacted();
    return localRangeToken2;
  }
  
  synchronized RangeToken getCaseInsensitiveToken()
  {
    if (icaseCache != null) {
      return icaseCache;
    }
    RangeToken localRangeToken1 = type == 4 ? Token.createRange() : Token.createNRange();
    for (int i = 0; i < ranges.length; i += 2) {
      for (int j = ranges[i]; j <= ranges[(i + 1)]; j++) {
        if (j > 65535)
        {
          localRangeToken1.addRange(j, j);
        }
        else
        {
          k = Character.toUpperCase((char)j);
          localRangeToken1.addRange(k, k);
        }
      }
    }
    RangeToken localRangeToken2 = type == 4 ? Token.createRange() : Token.createNRange();
    for (int k = 0; k < ranges.length; k += 2) {
      for (int m = ranges[k]; m <= ranges[(k + 1)]; m++) {
        if (m > 65535)
        {
          localRangeToken2.addRange(m, m);
        }
        else
        {
          int n = Character.toLowerCase((char)m);
          localRangeToken2.addRange(n, n);
        }
      }
    }
    localRangeToken2.mergeRanges(localRangeToken1);
    localRangeToken2.mergeRanges(this);
    localRangeToken2.compactRanges();
    icaseCache = localRangeToken2;
    return localRangeToken2;
  }
  
  void dumpRanges()
  {
    System.err.print("RANGE: ");
    if (ranges == null)
    {
      System.err.println(" NULL");
      return;
    }
    for (int i = 0; i < ranges.length; i += 2) {
      System.err.print("[" + ranges[i] + "," + ranges[(i + 1)] + "] ");
    }
    System.err.println("");
  }
  
  boolean match(int paramInt)
  {
    if (map == null) {
      createMap();
    }
    boolean bool;
    int i;
    if (type == 4)
    {
      if (paramInt < 256) {
        return (map[(paramInt / 32)] & 1 << (paramInt & 0x1F)) != 0;
      }
      bool = false;
      for (i = nonMapIndex; i < ranges.length; i += 2) {
        if ((ranges[i] <= paramInt) && (paramInt <= ranges[(i + 1)])) {
          return true;
        }
      }
    }
    else
    {
      if (paramInt < 256) {
        return (map[(paramInt / 32)] & 1 << (paramInt & 0x1F)) == 0;
      }
      bool = true;
      for (i = nonMapIndex; i < ranges.length; i += 2) {
        if ((ranges[i] <= paramInt) && (paramInt <= ranges[(i + 1)])) {
          return false;
        }
      }
    }
    return bool;
  }
  
  private void createMap()
  {
    int i = 8;
    int[] arrayOfInt = new int[i];
    int j = ranges.length;
    for (int k = 0; k < i; k++) {
      arrayOfInt[k] = 0;
    }
    for (int m = 0; m < ranges.length; m += 2)
    {
      int n = ranges[m];
      int i1 = ranges[(m + 1)];
      if (n < 256)
      {
        int i2 = n;
        do
        {
          arrayOfInt[(i2 / 32)] |= 1 << (i2 & 0x1F);
          i2++;
          if (i2 > i1) {
            break;
          }
        } while (i2 < 256);
      }
      else
      {
        j = m;
        break;
      }
      if (i1 >= 256)
      {
        j = m;
        break;
      }
    }
    map = arrayOfInt;
    nonMapIndex = j;
  }
  
  public String toString(int paramInt)
  {
    String str;
    StringBuffer localStringBuffer;
    int i;
    if (type == 4)
    {
      if (this == Token.token_dot)
      {
        str = ".";
      }
      else if (this == Token.token_0to9)
      {
        str = "\\d";
      }
      else if (this == Token.token_wordchars)
      {
        str = "\\w";
      }
      else if (this == Token.token_spaces)
      {
        str = "\\s";
      }
      else
      {
        localStringBuffer = new StringBuffer();
        localStringBuffer.append('[');
        for (i = 0; i < ranges.length; i += 2)
        {
          if (((paramInt & 0x400) != 0) && (i > 0)) {
            localStringBuffer.append(',');
          }
          if (ranges[i] == ranges[(i + 1)])
          {
            localStringBuffer.append(escapeCharInCharClass(ranges[i]));
          }
          else
          {
            localStringBuffer.append(escapeCharInCharClass(ranges[i]));
            localStringBuffer.append('-');
            localStringBuffer.append(escapeCharInCharClass(ranges[(i + 1)]));
          }
        }
        localStringBuffer.append(']');
        str = localStringBuffer.toString();
      }
    }
    else if (this == Token.token_not_0to9)
    {
      str = "\\D";
    }
    else if (this == Token.token_not_wordchars)
    {
      str = "\\W";
    }
    else if (this == Token.token_not_spaces)
    {
      str = "\\S";
    }
    else
    {
      localStringBuffer = new StringBuffer();
      localStringBuffer.append("[^");
      for (i = 0; i < ranges.length; i += 2)
      {
        if (((paramInt & 0x400) != 0) && (i > 0)) {
          localStringBuffer.append(',');
        }
        if (ranges[i] == ranges[(i + 1)])
        {
          localStringBuffer.append(escapeCharInCharClass(ranges[i]));
        }
        else
        {
          localStringBuffer.append(escapeCharInCharClass(ranges[i]));
          localStringBuffer.append('-');
          localStringBuffer.append(escapeCharInCharClass(ranges[(i + 1)]));
        }
      }
      localStringBuffer.append(']');
      str = localStringBuffer.toString();
    }
    return str;
  }
  
  private static String escapeCharInCharClass(int paramInt)
  {
    String str1;
    switch (paramInt)
    {
    case 44: 
    case 45: 
    case 91: 
    case 92: 
    case 93: 
    case 94: 
      str1 = "\\" + (char)paramInt;
      break;
    case 12: 
      str1 = "\\f";
      break;
    case 10: 
      str1 = "\\n";
      break;
    case 13: 
      str1 = "\\r";
      break;
    case 9: 
      str1 = "\\t";
      break;
    case 27: 
      str1 = "\\e";
      break;
    default: 
      String str2;
      if (paramInt < 32)
      {
        str2 = "0" + Integer.toHexString(paramInt);
        str1 = "\\x" + str2.substring(str2.length() - 2, str2.length());
      }
      else if (paramInt >= 65536)
      {
        str2 = "0" + Integer.toHexString(paramInt);
        str1 = "\\v" + str2.substring(str2.length() - 6, str2.length());
      }
      else
      {
        str1 = "" + (char)paramInt;
      }
      break;
    }
    return str1;
  }
}
