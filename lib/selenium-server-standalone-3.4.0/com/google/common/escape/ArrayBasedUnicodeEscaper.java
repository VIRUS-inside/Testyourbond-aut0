package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.Nullable;























































@Beta
@GwtCompatible
public abstract class ArrayBasedUnicodeEscaper
  extends UnicodeEscaper
{
  private final char[][] replacements;
  private final int replacementsLength;
  private final int safeMin;
  private final int safeMax;
  private final char safeMinChar;
  private final char safeMaxChar;
  
  protected ArrayBasedUnicodeEscaper(Map<Character, String> replacementMap, int safeMin, int safeMax, @Nullable String unsafeReplacement)
  {
    this(ArrayBasedEscaperMap.create(replacementMap), safeMin, safeMax, unsafeReplacement);
  }
  


















  protected ArrayBasedUnicodeEscaper(ArrayBasedEscaperMap escaperMap, int safeMin, int safeMax, @Nullable String unsafeReplacement)
  {
    Preconditions.checkNotNull(escaperMap);
    replacements = escaperMap.getReplacementArray();
    replacementsLength = replacements.length;
    if (safeMax < safeMin)
    {

      safeMax = -1;
      safeMin = Integer.MAX_VALUE;
    }
    this.safeMin = safeMin;
    this.safeMax = safeMax;
    













    if (safeMin >= 55296)
    {

      safeMinChar = 65535;
      safeMaxChar = '\000';
    }
    else
    {
      safeMinChar = ((char)safeMin);
      safeMaxChar = ((char)Math.min(safeMax, 55295));
    }
  }
  




  public final String escape(String s)
  {
    Preconditions.checkNotNull(s);
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (((c < replacementsLength) && (replacements[c] != null)) || (c > safeMaxChar) || (c < safeMinChar))
      {

        return escapeSlow(s, i);
      }
    }
    return s;
  }
  

  protected final int nextEscapeIndex(CharSequence csq, int index, int end)
  {
    while (index < end) {
      char c = csq.charAt(index);
      if (((c < replacementsLength) && (replacements[c] != null)) || (c > safeMaxChar) || (c < safeMinChar)) {
        break;
      }
      

      index++;
    }
    return index;
  }
  





  protected final char[] escape(int cp)
  {
    if (cp < replacementsLength) {
      char[] chars = replacements[cp];
      if (chars != null) {
        return chars;
      }
    }
    if ((cp >= safeMin) && (cp <= safeMax)) {
      return null;
    }
    return escapeUnsafe(cp);
  }
  
  protected abstract char[] escapeUnsafe(int paramInt);
}
