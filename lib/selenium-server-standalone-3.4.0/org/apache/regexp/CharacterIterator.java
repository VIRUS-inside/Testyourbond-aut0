package org.apache.regexp;

public abstract interface CharacterIterator
{
  public abstract char charAt(int paramInt);
  
  public abstract boolean isEnd(int paramInt);
  
  public abstract String substring(int paramInt);
  
  public abstract String substring(int paramInt1, int paramInt2);
}
