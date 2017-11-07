package org.apache.http.util;

import java.io.Serializable;
import java.nio.CharBuffer;
import org.apache.http.protocol.HTTP;








































public final class CharArrayBuffer
  implements CharSequence, Serializable
{
  private static final long serialVersionUID = -6208952725094867135L;
  private char[] buffer;
  private int len;
  
  public CharArrayBuffer(int capacity)
  {
    Args.notNegative(capacity, "Buffer capacity");
    buffer = new char[capacity];
  }
  
  private void expand(int newlen) {
    char[] newbuffer = new char[Math.max(buffer.length << 1, newlen)];
    System.arraycopy(buffer, 0, newbuffer, 0, len);
    buffer = newbuffer;
  }
  











  public void append(char[] b, int off, int len)
  {
    if (b == null) {
      return;
    }
    if ((off < 0) || (off > b.length) || (len < 0) || (off + len < 0) || (off + len > b.length))
    {
      throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
    }
    if (len == 0) {
      return;
    }
    int newlen = this.len + len;
    if (newlen > buffer.length) {
      expand(newlen);
    }
    System.arraycopy(b, off, buffer, this.len, len);
    this.len = newlen;
  }
  





  public void append(String str)
  {
    String s = str != null ? str : "null";
    int strlen = s.length();
    int newlen = len + strlen;
    if (newlen > buffer.length) {
      expand(newlen);
    }
    s.getChars(0, strlen, buffer, len);
    len = newlen;
  }
  












  public void append(CharArrayBuffer b, int off, int len)
  {
    if (b == null) {
      return;
    }
    append(buffer, off, len);
  }
  






  public void append(CharArrayBuffer b)
  {
    if (b == null) {
      return;
    }
    append(buffer, 0, len);
  }
  





  public void append(char ch)
  {
    int newlen = len + 1;
    if (newlen > buffer.length) {
      expand(newlen);
    }
    buffer[len] = ch;
    len = newlen;
  }
  













  public void append(byte[] b, int off, int len)
  {
    if (b == null) {
      return;
    }
    if ((off < 0) || (off > b.length) || (len < 0) || (off + len < 0) || (off + len > b.length))
    {
      throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
    }
    if (len == 0) {
      return;
    }
    int oldlen = this.len;
    int newlen = oldlen + len;
    if (newlen > buffer.length) {
      expand(newlen);
    }
    int i1 = off; for (int i2 = oldlen; i2 < newlen; i2++) {
      buffer[i2] = ((char)(b[i1] & 0xFF));i1++;
    }
    this.len = newlen;
  }
  













  public void append(ByteArrayBuffer b, int off, int len)
  {
    if (b == null) {
      return;
    }
    append(b.buffer(), off, len);
  }
  






  public void append(Object obj)
  {
    append(String.valueOf(obj));
  }
  


  public void clear()
  {
    len = 0;
  }
  




  public char[] toCharArray()
  {
    char[] b = new char[len];
    if (len > 0) {
      System.arraycopy(buffer, 0, b, 0, len);
    }
    return b;
  }
  









  public char charAt(int i)
  {
    return buffer[i];
  }
  




  public char[] buffer()
  {
    return buffer;
  }
  






  public int capacity()
  {
    return buffer.length;
  }
  




  public int length()
  {
    return len;
  }
  







  public void ensureCapacity(int required)
  {
    if (required <= 0) {
      return;
    }
    int available = buffer.length - len;
    if (required > available) {
      expand(len + required);
    }
  }
  









  public void setLength(int len)
  {
    if ((len < 0) || (len > buffer.length)) {
      throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + buffer.length);
    }
    this.len = len;
  }
  





  public boolean isEmpty()
  {
    return len == 0;
  }
  





  public boolean isFull()
  {
    return len == buffer.length;
  }
  




















  public int indexOf(int ch, int from, int to)
  {
    int beginIndex = from;
    if (beginIndex < 0) {
      beginIndex = 0;
    }
    int endIndex = to;
    if (endIndex > len) {
      endIndex = len;
    }
    if (beginIndex > endIndex) {
      return -1;
    }
    for (int i = beginIndex; i < endIndex; i++) {
      if (buffer[i] == ch) {
        return i;
      }
    }
    return -1;
  }
  









  public int indexOf(int ch)
  {
    return indexOf(ch, 0, len);
  }
  













  public String substring(int beginIndex, int endIndex)
  {
    if (beginIndex < 0) {
      throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
    }
    if (endIndex > len) {
      throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + len);
    }
    if (beginIndex > endIndex) {
      throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
    }
    return new String(buffer, beginIndex, endIndex - beginIndex);
  }
  















  public String substringTrimmed(int beginIndex, int endIndex)
  {
    if (beginIndex < 0) {
      throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
    }
    if (endIndex > len) {
      throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + len);
    }
    if (beginIndex > endIndex) {
      throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
    }
    int beginIndex0 = beginIndex;
    int endIndex0 = endIndex;
    while ((beginIndex0 < endIndex) && (HTTP.isWhitespace(buffer[beginIndex0]))) {
      beginIndex0++;
    }
    while ((endIndex0 > beginIndex0) && (HTTP.isWhitespace(buffer[(endIndex0 - 1)]))) {
      endIndex0--;
    }
    return new String(buffer, beginIndex0, endIndex0 - beginIndex0);
  }
  




  public CharSequence subSequence(int beginIndex, int endIndex)
  {
    if (beginIndex < 0) {
      throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
    }
    if (endIndex > len) {
      throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + len);
    }
    if (beginIndex > endIndex) {
      throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
    }
    return CharBuffer.wrap(buffer, beginIndex, endIndex);
  }
  
  public String toString()
  {
    return new String(buffer, 0, len);
  }
}
