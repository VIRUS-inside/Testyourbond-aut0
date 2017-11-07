package org.eclipse.jetty.websocket.common.message;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.Utf8Appendable;


























public class Utf8CharBuffer
  extends Utf8Appendable
{
  private final CharBuffer buffer;
  
  public static Utf8CharBuffer wrap(ByteBuffer buffer)
  {
    return new Utf8CharBuffer(buffer.asCharBuffer());
  }
  


  private Utf8CharBuffer(CharBuffer buffer)
  {
    super(buffer);
    this.buffer = buffer;
  }
  
  public void append(char[] cbuf, int offset, int size)
  {
    append(BufferUtil.toDirectBuffer(new String(cbuf, offset, size), StandardCharsets.UTF_8));
  }
  
  public void append(int c)
  {
    buffer.append((char)c);
  }
  
  public void clear()
  {
    buffer.position(0);
    buffer.limit(buffer.capacity());
  }
  

  public ByteBuffer getByteBuffer()
  {
    int limit = buffer.limit();
    int position = buffer.position();
    

    buffer.limit(buffer.position());
    buffer.position(0);
    

    ByteBuffer bb = StandardCharsets.UTF_8.encode(buffer);
    

    buffer.limit(limit);
    buffer.position(position);
    
    return bb;
  }
  

  public int length()
  {
    return buffer.capacity();
  }
  
  public int remaining()
  {
    return buffer.remaining();
  }
  

  public String toString()
  {
    StringBuilder str = new StringBuilder();
    str.append("Utf8CharBuffer@").append(hashCode());
    str.append("[p=").append(buffer.position());
    str.append(",l=").append(buffer.limit());
    str.append(",c=").append(buffer.capacity());
    str.append(",r=").append(buffer.remaining());
    str.append("]");
    return str.toString();
  }
}
