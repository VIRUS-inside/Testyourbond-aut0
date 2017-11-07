package net.sourceforge.htmlunit.corejs.javascript;

import java.io.Serializable;





























public class ConsString
  implements CharSequence, Serializable
{
  private static final long serialVersionUID = -8432806714471372570L;
  private CharSequence s1;
  private CharSequence s2;
  private final int length;
  private int depth;
  
  public ConsString(CharSequence str1, CharSequence str2)
  {
    s1 = str1;
    s2 = str2;
    length = (str1.length() + str2.length());
    depth = 1;
    if ((str1 instanceof ConsString)) {
      depth += depth;
    }
    if ((str2 instanceof ConsString)) {
      depth += depth;
    }
    
    if (depth > 2000) {
      flatten();
    }
  }
  
  private Object writeReplace()
  {
    return toString();
  }
  
  public String toString()
  {
    return depth == 0 ? (String)s1 : flatten();
  }
  
  private synchronized String flatten() {
    if (depth > 0) {
      StringBuilder b = new StringBuilder(length);
      appendTo(b);
      s1 = b.toString();
      s2 = "";
      depth = 0;
    }
    return (String)s1;
  }
  
  private synchronized void appendTo(StringBuilder b) {
    appendFragment(s1, b);
    appendFragment(s2, b);
  }
  
  private static void appendFragment(CharSequence s, StringBuilder b) {
    if ((s instanceof ConsString)) {
      ((ConsString)s).appendTo(b);
    } else {
      b.append(s);
    }
  }
  
  public int length() {
    return length;
  }
  
  public char charAt(int index) {
    String str = depth == 0 ? (String)s1 : flatten();
    return str.charAt(index);
  }
  
  public CharSequence subSequence(int start, int end) {
    String str = depth == 0 ? (String)s1 : flatten();
    return str.substring(start, end);
  }
}
