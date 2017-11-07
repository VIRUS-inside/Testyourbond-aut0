package org.apache.regexp;

















public final class CharacterArrayCharacterIterator
  implements CharacterIterator
{
  private final char[] src;
  















  private final int off;
  















  private final int len;
  
















  public CharacterArrayCharacterIterator(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    src = paramArrayOfChar;
    off = paramInt1;
    len = paramInt2;
  }
  

  public String substring(int paramInt1, int paramInt2)
  {
    return new String(src, off + paramInt1, paramInt2);
  }
  

  public String substring(int paramInt)
  {
    return new String(src, off + paramInt, len);
  }
  

  public char charAt(int paramInt)
  {
    return src[(off + paramInt)];
  }
  

  public boolean isEnd(int paramInt)
  {
    return paramInt >= len;
  }
}
