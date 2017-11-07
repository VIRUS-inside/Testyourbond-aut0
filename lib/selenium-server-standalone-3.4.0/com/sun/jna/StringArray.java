package com.sun.jna;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;















public class StringArray
  extends Memory
  implements Function.PostCallRead
{
  private String encoding;
  private List natives = new ArrayList();
  private Object[] original;
  
  public StringArray(String[] strings) {
    this(strings, false);
  }
  
  public StringArray(String[] strings, boolean wide) {
    this((Object[])strings, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
  }
  
  public StringArray(String[] strings, String encoding) {
    this((Object[])strings, encoding);
  }
  

  public StringArray(WString[] strings) { this(strings, "--WIDE-STRING--"); }
  
  private StringArray(Object[] strings, String encoding) {
    super((strings.length + 1) * Pointer.SIZE);
    original = strings;
    this.encoding = encoding;
    for (int i = 0; i < strings.length; i++) {
      Pointer p = null;
      if (strings[i] != null) {
        NativeString ns = new NativeString(strings[i].toString(), encoding);
        natives.add(ns);
        p = ns.getPointer();
      }
      setPointer(Pointer.SIZE * i, p);
    }
    setPointer(Pointer.SIZE * strings.length, null);
  }
  
  public void read() {
    boolean returnWide = original instanceof WString[];
    boolean wide = encoding == "--WIDE-STRING--";
    for (int si = 0; si < original.length; si++) {
      Pointer p = getPointer(si * Pointer.SIZE);
      Object s = null;
      if (p != null) {
        s = wide ? p.getWideString(0L) : p.getString(0L, encoding);
        if (returnWide) s = new WString((String)s);
      }
      original[si] = s;
    }
  }
  
  public String toString() {
    boolean wide = encoding == "--WIDE-STRING--";
    String s = wide ? "const wchar_t*[]" : "const char*[]";
    s = s + Arrays.asList(original);
    return s;
  }
}
