package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


































public class CharSet
  implements Serializable
{
  private static final long serialVersionUID = 5947847346149275958L;
  public static final CharSet EMPTY = new CharSet(new String[] { (String)null });
  




  public static final CharSet ASCII_ALPHA = new CharSet(new String[] { "a-zA-Z" });
  




  public static final CharSet ASCII_ALPHA_LOWER = new CharSet(new String[] { "a-z" });
  




  public static final CharSet ASCII_ALPHA_UPPER = new CharSet(new String[] { "A-Z" });
  




  public static final CharSet ASCII_NUMERIC = new CharSet(new String[] { "0-9" });
  





  protected static final Map<String, CharSet> COMMON = Collections.synchronizedMap(new HashMap());
  
  static {
    COMMON.put(null, EMPTY);
    COMMON.put("", EMPTY);
    COMMON.put("a-zA-Z", ASCII_ALPHA);
    COMMON.put("A-Za-z", ASCII_ALPHA);
    COMMON.put("a-z", ASCII_ALPHA_LOWER);
    COMMON.put("A-Z", ASCII_ALPHA_UPPER);
    COMMON.put("0-9", ASCII_NUMERIC);
  }
  

  private final Set<CharRange> set = Collections.synchronizedSet(new HashSet());
  





























































  public static CharSet getInstance(String... setStrs)
  {
    if (setStrs == null) {
      return null;
    }
    if (setStrs.length == 1) {
      CharSet common = (CharSet)COMMON.get(setStrs[0]);
      if (common != null) {
        return common;
      }
    }
    return new CharSet(setStrs);
  }
  








  protected CharSet(String... set)
  {
    int sz = set.length;
    for (int i = 0; i < sz; i++) {
      add(set[i]);
    }
  }
  





  protected void add(String str)
  {
    if (str == null) {
      return;
    }
    
    int len = str.length();
    int pos = 0;
    while (pos < len) {
      int remainder = len - pos;
      if ((remainder >= 4) && (str.charAt(pos) == '^') && (str.charAt(pos + 2) == '-'))
      {
        set.add(CharRange.isNotIn(str.charAt(pos + 1), str.charAt(pos + 3)));
        pos += 4;
      } else if ((remainder >= 3) && (str.charAt(pos + 1) == '-'))
      {
        set.add(CharRange.isIn(str.charAt(pos), str.charAt(pos + 2)));
        pos += 3;
      } else if ((remainder >= 2) && (str.charAt(pos) == '^'))
      {
        set.add(CharRange.isNot(str.charAt(pos + 1)));
        pos += 2;
      }
      else {
        set.add(CharRange.is(str.charAt(pos)));
        pos++;
      }
    }
  }
  








  CharRange[] getCharRanges()
  {
    return (CharRange[])set.toArray(new CharRange[set.size()]);
  }
  







  public boolean contains(char ch)
  {
    for (CharRange range : set) {
      if (range.contains(ch)) {
        return true;
      }
    }
    return false;
  }
  













  public boolean equals(Object obj)
  {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CharSet)) {
      return false;
    }
    CharSet other = (CharSet)obj;
    return set.equals(set);
  }
  






  public int hashCode()
  {
    return 89 + set.hashCode();
  }
  





  public String toString()
  {
    return set.toString();
  }
}
