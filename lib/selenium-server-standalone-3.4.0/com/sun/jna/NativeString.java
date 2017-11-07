package com.sun.jna;

import java.nio.CharBuffer;





















class NativeString
  implements CharSequence, Comparable
{
  static final String WIDE_STRING = "--WIDE-STRING--";
  private Pointer pointer;
  private String encoding;
  
  public NativeString(String string)
  {
    this(string, Native.getDefaultStringEncoding());
  }
  







  public NativeString(String string, boolean wide)
  {
    this(string, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
  }
  


  public NativeString(WString string)
  {
    this(string.toString(), "--WIDE-STRING--");
  }
  


  public NativeString(String string, String encoding)
  {
    if (string == null) {
      throw new NullPointerException("String must not be null");
    }
    


    this.encoding = encoding;
    if (this.encoding == "--WIDE-STRING--") {
      int len = (string.length() + 1) * Native.WCHAR_SIZE;
      pointer = new Memory(len);
      pointer.setWideString(0L, string);
    }
    else {
      byte[] data = Native.getBytes(string, encoding);
      pointer = new Memory(data.length + 1);
      pointer.write(0L, data, 0, data.length);
      pointer.setByte(data.length, (byte)0);
    }
  }
  
  public int hashCode() {
    return toString().hashCode();
  }
  
  public boolean equals(Object other)
  {
    if ((other instanceof CharSequence)) {
      return compareTo(other) == 0;
    }
    return false;
  }
  
  public String toString() {
    boolean wide = encoding == "--WIDE-STRING--";
    String s = wide ? "const wchar_t*" : "const char*";
    s = s + "(" + (wide ? pointer.getWideString(0L) : pointer.getString(0L, encoding)) + ")";
    return s;
  }
  
  public Pointer getPointer() {
    return pointer;
  }
  
  public char charAt(int index) {
    return toString().charAt(index);
  }
  
  public int length() {
    return toString().length();
  }
  
  public CharSequence subSequence(int start, int end) {
    return CharBuffer.wrap(toString()).subSequence(start, end);
  }
  
  public int compareTo(Object other)
  {
    if (other == null) {
      return 1;
    }
    return toString().compareTo(other.toString());
  }
}
