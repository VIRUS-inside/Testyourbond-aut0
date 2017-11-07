package org.apache.regexp;
































public final class StringCharacterIterator
  implements CharacterIterator
{
  private final String src;
  































  public StringCharacterIterator(String paramString)
  {
    src = paramString;
  }
  

  public String substring(int paramInt1, int paramInt2)
  {
    return src.substring(paramInt1, paramInt2);
  }
  

  public String substring(int paramInt)
  {
    return src.substring(paramInt);
  }
  

  public char charAt(int paramInt)
  {
    return src.charAt(paramInt);
  }
  

  public boolean isEnd(int paramInt)
  {
    return paramInt >= src.length();
  }
}
