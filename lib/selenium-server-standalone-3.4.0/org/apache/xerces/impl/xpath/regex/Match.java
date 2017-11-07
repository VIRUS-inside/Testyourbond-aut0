package org.apache.xerces.impl.xpath.regex;

import java.text.CharacterIterator;

public class Match
  implements Cloneable
{
  int[] beginpos = null;
  int[] endpos = null;
  int nofgroups = 0;
  CharacterIterator ciSource = null;
  String strSource = null;
  char[] charSource = null;
  
  public Match() {}
  
  public synchronized Object clone()
  {
    Match localMatch = new Match();
    if (nofgroups > 0)
    {
      localMatch.setNumberOfGroups(nofgroups);
      if (ciSource != null) {
        localMatch.setSource(ciSource);
      }
      if (strSource != null) {
        localMatch.setSource(strSource);
      }
      for (int i = 0; i < nofgroups; i++)
      {
        localMatch.setBeginning(i, getBeginning(i));
        localMatch.setEnd(i, getEnd(i));
      }
    }
    return localMatch;
  }
  
  protected void setNumberOfGroups(int paramInt)
  {
    int i = nofgroups;
    nofgroups = paramInt;
    if ((i <= 0) || (i < paramInt) || (paramInt * 2 < i))
    {
      beginpos = new int[paramInt];
      endpos = new int[paramInt];
    }
    for (int j = 0; j < paramInt; j++)
    {
      beginpos[j] = -1;
      endpos[j] = -1;
    }
  }
  
  protected void setSource(CharacterIterator paramCharacterIterator)
  {
    ciSource = paramCharacterIterator;
    strSource = null;
    charSource = null;
  }
  
  protected void setSource(String paramString)
  {
    ciSource = null;
    strSource = paramString;
    charSource = null;
  }
  
  protected void setSource(char[] paramArrayOfChar)
  {
    ciSource = null;
    strSource = null;
    charSource = paramArrayOfChar;
  }
  
  protected void setBeginning(int paramInt1, int paramInt2)
  {
    beginpos[paramInt1] = paramInt2;
  }
  
  protected void setEnd(int paramInt1, int paramInt2)
  {
    endpos[paramInt1] = paramInt2;
  }
  
  public int getNumberOfGroups()
  {
    if (nofgroups <= 0) {
      throw new IllegalStateException("A result is not set.");
    }
    return nofgroups;
  }
  
  public int getBeginning(int paramInt)
  {
    if (beginpos == null) {
      throw new IllegalStateException("A result is not set.");
    }
    if ((paramInt < 0) || (nofgroups <= paramInt)) {
      throw new IllegalArgumentException("The parameter must be less than " + nofgroups + ": " + paramInt);
    }
    return beginpos[paramInt];
  }
  
  public int getEnd(int paramInt)
  {
    if (endpos == null) {
      throw new IllegalStateException("A result is not set.");
    }
    if ((paramInt < 0) || (nofgroups <= paramInt)) {
      throw new IllegalArgumentException("The parameter must be less than " + nofgroups + ": " + paramInt);
    }
    return endpos[paramInt];
  }
  
  public String getCapturedText(int paramInt)
  {
    if (beginpos == null) {
      throw new IllegalStateException("match() has never been called.");
    }
    if ((paramInt < 0) || (nofgroups <= paramInt)) {
      throw new IllegalArgumentException("The parameter must be less than " + nofgroups + ": " + paramInt);
    }
    int i = beginpos[paramInt];
    int j = endpos[paramInt];
    if ((i < 0) || (j < 0)) {
      return null;
    }
    String str;
    if (ciSource != null) {
      str = REUtil.substring(ciSource, i, j);
    } else if (strSource != null) {
      str = strSource.substring(i, j);
    } else {
      str = new String(charSource, i, j - i);
    }
    return str;
  }
}
