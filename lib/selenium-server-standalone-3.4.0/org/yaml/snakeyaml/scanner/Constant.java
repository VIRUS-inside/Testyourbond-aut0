package org.yaml.snakeyaml.scanner;

import java.util.Arrays;

















public final class Constant
{
  private static final String ALPHA_S = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
  private static final String LINEBR_S = "\n  ";
  private static final String FULL_LINEBR_S = "\r\n  ";
  private static final String NULL_OR_LINEBR_S = "\000\r\n  ";
  private static final String NULL_BL_LINEBR_S = " \000\r\n  ";
  private static final String NULL_BL_T_LINEBR_S = "\t \000\r\n  ";
  private static final String NULL_BL_T_S = "\000 \t";
  private static final String URI_CHARS_S = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_-;/?:@&=+$,_.!~*'()[]%";
  public static final Constant LINEBR = new Constant("\n  ");
  public static final Constant FULL_LINEBR = new Constant("\r\n  ");
  public static final Constant NULL_OR_LINEBR = new Constant("\000\r\n  ");
  public static final Constant NULL_BL_LINEBR = new Constant(" \000\r\n  ");
  public static final Constant NULL_BL_T_LINEBR = new Constant("\t \000\r\n  ");
  public static final Constant NULL_BL_T = new Constant("\000 \t");
  public static final Constant URI_CHARS = new Constant("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_-;/?:@&=+$,_.!~*'()[]%");
  
  public static final Constant ALPHA = new Constant("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_");
  
  private String content;
  boolean[] contains = new boolean[''];
  boolean noASCII = false;
  
  private Constant(String content) {
    Arrays.fill(contains, false);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < content.length(); i++) {
      char ch = content.charAt(i);
      if (ch < '') {
        contains[ch] = true;
      } else
        sb.append(ch);
    }
    if (sb.length() > 0) {
      noASCII = true;
      this.content = sb.toString();
    }
  }
  
  public boolean has(char ch) {
    return (noASCII) && (content.indexOf(ch, 0) != -1) ? true : ch < '' ? contains[ch] : false;
  }
  
  public boolean hasNo(char ch) {
    return !has(ch);
  }
  
  public boolean has(char ch, String additional) {
    return (has(ch)) || (additional.indexOf(ch, 0) != -1);
  }
  
  public boolean hasNo(char ch, String additional) {
    return !has(ch, additional);
  }
}
