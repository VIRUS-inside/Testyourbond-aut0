package org.apache.http.util;

import java.io.Serializable;







































public final class ByteArrayBuffer
  implements Serializable
{
  private static final long serialVersionUID = 4359112959524048036L;
  private byte[] buffer;
  private int len;
  
  public ByteArrayBuffer(int capacity)
  {
    Args.notNegative(capacity, "Buffer capacity");
    buffer = new byte[capacity];
  }
  
  private void expand(int newlen) {
    byte[] newbuffer = new byte[Math.max(buffer.length << 1, newlen)];
    System.arraycopy(buffer, 0, newbuffer, 0, len);
    buffer = newbuffer;
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
    int newlen = this.len + len;
    if (newlen > buffer.length) {
      expand(newlen);
    }
    System.arraycopy(b, off, buffer, this.len, len);
    this.len = newlen;
  }
  





  public void append(int b)
  {
    int newlen = len + 1;
    if (newlen > buffer.length) {
      expand(newlen);
    }
    buffer[len] = ((byte)b);
    len = newlen;
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
    int oldlen = this.len;
    int newlen = oldlen + len;
    if (newlen > buffer.length) {
      expand(newlen);
    }
    int i1 = off; for (int i2 = oldlen; i2 < newlen; i2++) {
      buffer[i2] = ((byte)b[i1]);i1++;
    }
    this.len = newlen;
  }
  














  public void append(CharArrayBuffer b, int off, int len)
  {
    if (b == null) {
      return;
    }
    append(b.buffer(), off, len);
  }
  


  public void clear()
  {
    len = 0;
  }
  




  public byte[] toByteArray()
  {
    byte[] b = new byte[len];
    if (len > 0) {
      System.arraycopy(buffer, 0, b, 0, len);
    }
    return b;
  }
  









  public int byteAt(int i)
  {
    return buffer[i];
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
  




  public byte[] buffer()
  {
    return buffer;
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
  






















  public int indexOf(byte b, int from, int to)
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
      if (buffer[i] == b) {
        return i;
      }
    }
    return -1;
  }
  











  public int indexOf(byte b)
  {
    return indexOf(b, 0, len);
  }
}
