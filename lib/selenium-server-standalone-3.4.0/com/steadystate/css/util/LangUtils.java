package com.steadystate.css.util;

import java.util.List;















public final class LangUtils
{
  public static final int HASH_SEED = 17;
  public static final int HASH_OFFSET = 37;
  
  private LangUtils() {}
  
  public static int hashCode(int seed, int hashcode)
  {
    return seed * 37 + hashcode;
  }
  
  public static int hashCode(int seed, boolean b) {
    return hashCode(seed, b ? 1 : 0);
  }
  
  public static int hashCode(int seed, Object obj) {
    return hashCode(seed, obj != null ? obj.hashCode() : 0);
  }
  
  public static String join(List<String> values, String separator) {
    if (values == null) {
      return null;
    }
    if (separator == null) {
      separator = "";
    }
    
    boolean isFirst = true;
    StringBuilder result = new StringBuilder();
    for (String part : values) {
      if ((part != null) && (part.length() > 0)) {
        if (isFirst) {
          isFirst = false;
        }
        else {
          result.append(separator);
        }
        result.append(part);
      }
    }
    return result.toString();
  }
  
  public static boolean equals(Object obj1, Object obj2) {
    return obj1 == null ? false : obj2 == null ? true : obj1.equals(obj2);
  }
}
