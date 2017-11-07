package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;






























@Beta
@GwtCompatible
public final class ArrayBasedEscaperMap
{
  private final char[][] replacementArray;
  
  public static ArrayBasedEscaperMap create(Map<Character, String> replacements)
  {
    return new ArrayBasedEscaperMap(createReplacementArray(replacements));
  }
  



  private ArrayBasedEscaperMap(char[][] replacementArray)
  {
    this.replacementArray = replacementArray;
  }
  
  char[][] getReplacementArray()
  {
    return replacementArray;
  }
  


  @VisibleForTesting
  static char[][] createReplacementArray(Map<Character, String> map)
  {
    Preconditions.checkNotNull(map);
    if (map.isEmpty()) {
      return EMPTY_REPLACEMENT_ARRAY;
    }
    char max = ((Character)Collections.max(map.keySet())).charValue();
    char[][] replacements = new char[max + '\001'][];
    for (Iterator localIterator = map.keySet().iterator(); localIterator.hasNext();) { char c = ((Character)localIterator.next()).charValue();
      replacements[c] = ((String)map.get(Character.valueOf(c))).toCharArray();
    }
    return replacements;
  }
  

  private static final char[][] EMPTY_REPLACEMENT_ARRAY = new char[0][0];
}
