package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;






















@Beta
@GwtCompatible
public final class CharEscaperBuilder
{
  private final Map<Character, String> map;
  
  private static class CharArrayDecorator
    extends CharEscaper
  {
    private final char[][] replacements;
    private final int replaceLength;
    
    CharArrayDecorator(char[][] replacements)
    {
      this.replacements = replacements;
      replaceLength = replacements.length;
    }
    




    public String escape(String s)
    {
      int slen = s.length();
      for (int index = 0; index < slen; index++) {
        char c = s.charAt(index);
        if ((c < replacements.length) && (replacements[c] != null)) {
          return escapeSlow(s, index);
        }
      }
      return s;
    }
    
    protected char[] escape(char c)
    {
      return c < replaceLength ? replacements[c] : null;
    }
  }
  




  private int max = -1;
  


  public CharEscaperBuilder()
  {
    map = new HashMap();
  }
  


  @CanIgnoreReturnValue
  public CharEscaperBuilder addEscape(char c, String r)
  {
    map.put(Character.valueOf(c), Preconditions.checkNotNull(r));
    if (c > max) {
      max = c;
    }
    return this;
  }
  


  @CanIgnoreReturnValue
  public CharEscaperBuilder addEscapes(char[] cs, String r)
  {
    Preconditions.checkNotNull(r);
    for (char c : cs) {
      addEscape(c, r);
    }
    return this;
  }
  






  public char[][] toArray()
  {
    char[][] result = new char[max + 1][];
    for (Map.Entry<Character, String> entry : map.entrySet()) {
      result[((Character)entry.getKey()).charValue()] = ((String)entry.getValue()).toCharArray();
    }
    return result;
  }
  





  public Escaper toEscaper()
  {
    return new CharArrayDecorator(toArray());
  }
}
