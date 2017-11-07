package com.steadystate.css.parser;
















public final class ParserUtils
{
  private ParserUtils() {}
  














  public static String trimBy(StringBuilder s, int left, int right)
  {
    return s.substring(left, s.length() - right);
  }
  





  public static String trimUrl(StringBuilder s)
  {
    String s1 = trimBy(s, 4, 1).trim();
    if (s1.length() == 0) {
      return s1;
    }
    
    int end = s1.length() - 1;
    char c0 = s1.charAt(0);
    if (((c0 == '"') && (s1.charAt(end) == '"')) || ((c0 == '\'') && 
      (s1.charAt(end) == '\''))) {
      return s1.substring(1, end);
    }
    
    return s1;
  }
}
