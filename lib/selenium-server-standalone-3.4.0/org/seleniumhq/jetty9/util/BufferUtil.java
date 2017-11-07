package org.seleniumhq.jetty9.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;














































































public class BufferUtil
{
  static final int TEMP_BUFFER_SIZE = 4096;
  static final byte SPACE = 32;
  static final byte MINUS = 45;
  static final byte[] DIGIT = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
  


  public static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(new byte[0]);
  



  public BufferUtil() {}
  


  public static ByteBuffer allocate(int capacity)
  {
    ByteBuffer buf = ByteBuffer.allocate(capacity);
    buf.limit(0);
    return buf;
  }
  







  public static ByteBuffer allocateDirect(int capacity)
  {
    ByteBuffer buf = ByteBuffer.allocateDirect(capacity);
    buf.limit(0);
    return buf;
  }
  






  public static void clear(ByteBuffer buffer)
  {
    if (buffer != null)
    {
      buffer.position(0);
      buffer.limit(0);
    }
  }
  





  public static void clearToFill(ByteBuffer buffer)
  {
    if (buffer != null)
    {
      buffer.position(0);
      buffer.limit(buffer.capacity());
    }
  }
  














  public static int flipToFill(ByteBuffer buffer)
  {
    int position = buffer.position();
    int limit = buffer.limit();
    if (position == limit)
    {
      buffer.position(0);
      buffer.limit(buffer.capacity());
      return 0;
    }
    
    int capacity = buffer.capacity();
    if (limit == capacity)
    {
      buffer.compact();
      return 0;
    }
    
    buffer.position(limit);
    buffer.limit(capacity);
    return position;
  }
  











  public static void flipToFlush(ByteBuffer buffer, int position)
  {
    buffer.limit(buffer.position());
    buffer.position(position);
  }
  






  public static byte[] toArray(ByteBuffer buffer)
  {
    if (buffer.hasArray())
    {
      byte[] array = buffer.array();
      int from = buffer.arrayOffset() + buffer.position();
      return Arrays.copyOfRange(array, from, from + buffer.remaining());
    }
    

    byte[] to = new byte[buffer.remaining()];
    buffer.slice().get(to);
    return to;
  }
  






  public static boolean isEmpty(ByteBuffer buf)
  {
    return (buf == null) || (buf.remaining() == 0);
  }
  





  public static boolean hasContent(ByteBuffer buf)
  {
    return (buf != null) && (buf.remaining() > 0);
  }
  





  public static boolean isFull(ByteBuffer buf)
  {
    return (buf != null) && (buf.limit() == buf.capacity());
  }
  





  public static int length(ByteBuffer buffer)
  {
    return buffer == null ? 0 : buffer.remaining();
  }
  





  public static int space(ByteBuffer buffer)
  {
    if (buffer == null)
      return 0;
    return buffer.capacity() - buffer.limit();
  }
  





  public static boolean compact(ByteBuffer buffer)
  {
    if (buffer.position() == 0)
      return false;
    boolean full = buffer.limit() == buffer.capacity();
    buffer.compact().flip();
    return (full) && (buffer.limit() < buffer.capacity());
  }
  








  public static int put(ByteBuffer from, ByteBuffer to)
  {
    int remaining = from.remaining();
    int put; if (remaining > 0)
    {
      if (remaining <= to.remaining())
      {
        to.put(from);
        int put = remaining;
        from.position(from.limit());
      }
      else if (from.hasArray())
      {
        int put = to.remaining();
        to.put(from.array(), from.arrayOffset() + from.position(), put);
        from.position(from.position() + put);
      }
      else
      {
        int put = to.remaining();
        ByteBuffer slice = from.slice();
        slice.limit(put);
        to.put(slice);
        from.position(from.position() + put);
      }
    }
    else {
      put = 0;
    }
    return put;
  }
  





  /**
   * @deprecated
   */
  public static int flipPutFlip(ByteBuffer from, ByteBuffer to)
  {
    return append(to, from);
  }
  







  public static void append(ByteBuffer to, byte[] b, int off, int len)
    throws BufferOverflowException
  {
    int pos = flipToFill(to);
    try
    {
      to.put(b, off, len);
    }
    finally
    {
      flipToFlush(to, pos);
    }
  }
  





  public static void append(ByteBuffer to, byte b)
  {
    int pos = flipToFill(to);
    try
    {
      to.put(b);
      


      flipToFlush(to, pos); } finally { flipToFlush(to, pos);
    }
  }
  






  public static int append(ByteBuffer to, ByteBuffer b)
  {
    int pos = flipToFill(to);
    try
    {
      return put(b, to);
    }
    finally
    {
      flipToFlush(to, pos);
    }
  }
  









  public static int fill(ByteBuffer to, byte[] b, int off, int len)
  {
    int pos = flipToFill(to);
    try
    {
      int remaining = to.remaining();
      int take = remaining < len ? remaining : len;
      to.put(b, off, take);
      return take;
    }
    finally
    {
      flipToFlush(to, pos);
    }
  }
  

  public static void readFrom(File file, ByteBuffer buffer)
    throws IOException
  {
    RandomAccessFile raf = new RandomAccessFile(file, "r");Throwable localThrowable3 = null;
    try {
      FileChannel channel = raf.getChannel();
      long needed = raf.length();
      
      while ((needed > 0L) && (buffer.hasRemaining())) {
        needed -= channel.read(buffer);
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;


    }
    finally
    {

      if (raf != null) if (localThrowable3 != null) try { raf.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else raf.close();
    }
  }
  
  public static void readFrom(InputStream is, int needed, ByteBuffer buffer) throws IOException
  {
    ByteBuffer tmp = allocate(8192);
    
    while ((needed > 0) && (buffer.hasRemaining()))
    {
      int l = is.read(tmp.array(), 0, 8192);
      if (l < 0)
        break;
      tmp.position(0);
      tmp.limit(l);
      buffer.put(tmp);
    }
  }
  
  public static void writeTo(ByteBuffer buffer, OutputStream out)
    throws IOException
  {
    if (buffer.hasArray())
    {
      out.write(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
      
      buffer.position(buffer.position() + buffer.remaining());
    }
    else
    {
      byte[] bytes = new byte['က'];
      while (buffer.hasRemaining()) {
        int byteCountToWrite = Math.min(buffer.remaining(), 4096);
        buffer.get(bytes, 0, byteCountToWrite);
        out.write(bytes, 0, byteCountToWrite);
      }
    }
  }
  





  public static String toString(ByteBuffer buffer)
  {
    return toString(buffer, StandardCharsets.ISO_8859_1);
  }
  





  public static String toUTF8String(ByteBuffer buffer)
  {
    return toString(buffer, StandardCharsets.UTF_8);
  }
  






  public static String toString(ByteBuffer buffer, Charset charset)
  {
    if (buffer == null)
      return null;
    byte[] array = buffer.hasArray() ? buffer.array() : null;
    if (array == null)
    {
      byte[] to = new byte[buffer.remaining()];
      buffer.slice().get(to);
      return new String(to, 0, to.length, charset);
    }
    return new String(array, buffer.arrayOffset() + buffer.position(), buffer.remaining(), charset);
  }
  









  public static String toString(ByteBuffer buffer, int position, int length, Charset charset)
  {
    if (buffer == null)
      return null;
    byte[] array = buffer.hasArray() ? buffer.array() : null;
    if (array == null)
    {
      ByteBuffer ro = buffer.asReadOnlyBuffer();
      ro.position(position);
      ro.limit(position + length);
      byte[] to = new byte[length];
      ro.get(to);
      return new String(to, 0, to.length, charset);
    }
    return new String(array, buffer.arrayOffset() + position, length, charset);
  }
  








  public static int toInt(ByteBuffer buffer)
  {
    return toInt(buffer, buffer.position(), buffer.remaining());
  }
  













  public static int toInt(ByteBuffer buffer, int position, int length)
  {
    int val = 0;
    boolean started = false;
    boolean minus = false;
    
    int limit = position + length;
    
    if (length <= 0) {
      throw new NumberFormatException(toString(buffer, position, length, StandardCharsets.UTF_8));
    }
    for (int i = position; i < limit; i++)
    {
      byte b = buffer.get(i);
      if (b <= 32)
      {
        if (started) {
          break;
        }
      } else if ((b >= 48) && (b <= 57))
      {
        val = val * 10 + (b - 48);
        started = true;
      } else {
        if ((b != 45) || (started))
          break;
        minus = true;
      }
    }
    


    if (started)
      return minus ? -val : val;
    throw new NumberFormatException(toString(buffer));
  }
  








  public static int takeInt(ByteBuffer buffer)
  {
    int val = 0;
    boolean started = false;
    boolean minus = false;
    
    for (int i = buffer.position(); i < buffer.limit(); i++)
    {
      byte b = buffer.get(i);
      if (b <= 32)
      {
        if (started) {
          break;
        }
      } else if ((b >= 48) && (b <= 57))
      {
        val = val * 10 + (b - 48);
        started = true;
      } else {
        if ((b != 45) || (started))
          break;
        minus = true;
      }
    }
    


    if (started)
    {
      buffer.position(i);
      return minus ? -val : val;
    }
    throw new NumberFormatException(toString(buffer));
  }
  







  public static long toLong(ByteBuffer buffer)
  {
    long val = 0L;
    boolean started = false;
    boolean minus = false;
    
    for (int i = buffer.position(); i < buffer.limit(); i++)
    {
      byte b = buffer.get(i);
      if (b <= 32)
      {
        if (started) {
          break;
        }
      } else if ((b >= 48) && (b <= 57))
      {
        val = val * 10L + (b - 48);
        started = true;
      } else {
        if ((b != 45) || (started))
          break;
        minus = true;
      }
    }
    


    if (started)
      return minus ? -val : val;
    throw new NumberFormatException(toString(buffer));
  }
  
  public static void putHexInt(ByteBuffer buffer, int n)
  {
    if (n < 0)
    {
      buffer.put((byte)45);
      
      if (n == Integer.MIN_VALUE)
      {
        buffer.put((byte)56);
        buffer.put((byte)48);
        buffer.put((byte)48);
        buffer.put((byte)48);
        buffer.put((byte)48);
        buffer.put((byte)48);
        buffer.put((byte)48);
        buffer.put((byte)48);
        
        return;
      }
      n = -n;
    }
    
    if (n < 16)
    {
      buffer.put(DIGIT[n]);
    }
    else
    {
      boolean started = false;
      
      for (int hexDivisor : hexDivisors)
      {
        if (n < hexDivisor)
        {
          if (started) {
            buffer.put((byte)48);
          }
        }
        else {
          started = true;
          int d = n / hexDivisor;
          buffer.put(DIGIT[d]);
          n -= d * hexDivisor;
        }
      }
    }
  }
  
  public static void putDecInt(ByteBuffer buffer, int n)
  {
    if (n < 0)
    {
      buffer.put((byte)45);
      
      if (n == Integer.MIN_VALUE)
      {
        buffer.put((byte)50);
        n = 147483648;
      }
      else {
        n = -n;
      }
    }
    if (n < 10)
    {
      buffer.put(DIGIT[n]);
    }
    else
    {
      boolean started = false;
      
      for (int decDivisor : decDivisors)
      {
        if (n < decDivisor)
        {
          if (started) {
            buffer.put((byte)48);
          }
        }
        else {
          started = true;
          int d = n / decDivisor;
          buffer.put(DIGIT[d]);
          n -= d * decDivisor;
        }
      }
    }
  }
  
  public static void putDecLong(ByteBuffer buffer, long n) {
    if (n < 0L)
    {
      buffer.put((byte)45);
      
      if (n == Long.MIN_VALUE)
      {
        buffer.put((byte)57);
        n = 223372036854775808L;
      }
      else {
        n = -n;
      }
    }
    if (n < 10L)
    {
      buffer.put(DIGIT[((int)n)]);
    }
    else
    {
      boolean started = false;
      
      for (long aDecDivisorsL : decDivisorsL)
      {
        if (n < aDecDivisorsL)
        {
          if (started) {
            buffer.put((byte)48);
          }
        }
        else {
          started = true;
          long d = n / aDecDivisorsL;
          buffer.put(DIGIT[((int)d)]);
          n -= d * aDecDivisorsL;
        }
      }
    }
  }
  
  public static ByteBuffer toBuffer(int value) {
    ByteBuffer buf = ByteBuffer.allocate(32);
    putDecInt(buf, value);
    return buf;
  }
  
  public static ByteBuffer toBuffer(long value)
  {
    ByteBuffer buf = ByteBuffer.allocate(32);
    putDecLong(buf, value);
    return buf;
  }
  
  public static ByteBuffer toBuffer(String s)
  {
    return toBuffer(s, StandardCharsets.ISO_8859_1);
  }
  
  public static ByteBuffer toBuffer(String s, Charset charset)
  {
    if (s == null)
      return EMPTY_BUFFER;
    return toBuffer(s.getBytes(charset));
  }
  







  public static ByteBuffer toBuffer(byte[] array)
  {
    if (array == null)
      return EMPTY_BUFFER;
    return toBuffer(array, 0, array.length);
  }
  











  public static ByteBuffer toBuffer(byte[] array, int offset, int length)
  {
    if (array == null)
      return EMPTY_BUFFER;
    return ByteBuffer.wrap(array, offset, length);
  }
  
  public static ByteBuffer toDirectBuffer(String s)
  {
    return toDirectBuffer(s, StandardCharsets.ISO_8859_1);
  }
  
  public static ByteBuffer toDirectBuffer(String s, Charset charset)
  {
    if (s == null)
      return EMPTY_BUFFER;
    byte[] bytes = s.getBytes(charset);
    ByteBuffer buf = ByteBuffer.allocateDirect(bytes.length);
    buf.put(bytes);
    buf.flip();
    return buf;
  }
  
  public static ByteBuffer toMappedBuffer(File file) throws IOException
  {
    FileChannel channel = FileChannel.open(file.toPath(), new OpenOption[] { StandardOpenOption.READ });Throwable localThrowable3 = null;
    try {
      return channel.map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (channel != null) if (localThrowable3 != null) try { channel.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else channel.close();
    }
  }
  
  public static boolean isMappedBuffer(ByteBuffer buffer) {
    if (!(buffer instanceof MappedByteBuffer))
      return false;
    MappedByteBuffer mapped = (MappedByteBuffer)buffer;
    

    try
    {
      mapped.isLoaded();
      return true;
    }
    catch (UnsupportedOperationException e) {}
    
    return false;
  }
  

  public static ByteBuffer toBuffer(Resource resource, boolean direct)
    throws IOException
  {
    int len = (int)resource.length();
    if (len < 0) {
      throw new IllegalArgumentException("invalid resource: " + String.valueOf(resource) + " len=" + len);
    }
    ByteBuffer buffer = direct ? allocateDirect(len) : allocate(len);
    
    int pos = flipToFill(buffer);
    if (resource.getFile() != null) {
      readFrom(resource.getFile(), buffer);
    }
    else {
      InputStream is = resource.getInputStream();Throwable localThrowable3 = null;
      try {
        readFrom(is, len, buffer);
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      }
      finally {
        if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
      } }
    flipToFlush(buffer, pos);
    
    return buffer;
  }
  
  public static String toSummaryString(ByteBuffer buffer)
  {
    if (buffer == null)
      return "null";
    StringBuilder buf = new StringBuilder();
    buf.append("[p=");
    buf.append(buffer.position());
    buf.append(",l=");
    buf.append(buffer.limit());
    buf.append(",c=");
    buf.append(buffer.capacity());
    buf.append(",r=");
    buf.append(buffer.remaining());
    buf.append("]");
    return buf.toString();
  }
  
  public static String toDetailString(ByteBuffer[] buffer)
  {
    StringBuilder builder = new StringBuilder();
    builder.append('[');
    for (int i = 0; i < buffer.length; i++)
    {
      if (i > 0) builder.append(',');
      builder.append(toDetailString(buffer[i]));
    }
    builder.append(']');
    return builder.toString();
  }
  





  private static void idString(ByteBuffer buffer, StringBuilder out)
  {
    out.append(buffer.getClass().getSimpleName());
    out.append("@");
    if ((buffer.hasArray()) && (buffer.arrayOffset() == 4))
    {
      out.append('T');
      byte[] array = buffer.array();
      TypeUtil.toHex(array[0], out);
      TypeUtil.toHex(array[1], out);
      TypeUtil.toHex(array[2], out);
      TypeUtil.toHex(array[3], out);
    }
    else {
      out.append(Integer.toHexString(System.identityHashCode(buffer)));
    }
  }
  




  public static String toIDString(ByteBuffer buffer)
  {
    StringBuilder buf = new StringBuilder();
    idString(buffer, buf);
    return buf.toString();
  }
  






  public static String toDetailString(ByteBuffer buffer)
  {
    if (buffer == null) {
      return "null";
    }
    StringBuilder buf = new StringBuilder();
    idString(buffer, buf);
    buf.append("[p=");
    buf.append(buffer.position());
    buf.append(",l=");
    buf.append(buffer.limit());
    buf.append(",c=");
    buf.append(buffer.capacity());
    buf.append(",r=");
    buf.append(buffer.remaining());
    buf.append("]={");
    
    appendDebugString(buf, buffer);
    
    buf.append("}");
    
    return buf.toString();
  }
  
  private static void appendDebugString(StringBuilder buf, ByteBuffer buffer)
  {
    try
    {
      for (int i = 0; i < buffer.position(); i++)
      {
        appendContentChar(buf, buffer.get(i));
        if ((i == 16) && (buffer.position() > 32))
        {
          buf.append("...");
          i = buffer.position() - 16;
        }
      }
      buf.append("<<<");
      for (int i = buffer.position(); i < buffer.limit(); i++)
      {
        appendContentChar(buf, buffer.get(i));
        if ((i == buffer.position() + 16) && (buffer.limit() > buffer.position() + 32))
        {
          buf.append("...");
          i = buffer.limit() - 16;
        }
      }
      buf.append(">>>");
      int limit = buffer.limit();
      buffer.limit(buffer.capacity());
      for (int i = limit; i < buffer.capacity(); i++)
      {
        appendContentChar(buf, buffer.get(i));
        if ((i == limit + 16) && (buffer.capacity() > limit + 32))
        {
          buf.append("...");
          i = buffer.capacity() - 16;
        }
      }
      buffer.limit(limit);
    }
    catch (Throwable x)
    {
      Log.getRootLogger().ignore(x);
      buf.append("!!concurrent mod!!");
    }
  }
  
  private static void appendContentChar(StringBuilder buf, byte b)
  {
    if (b == 92) {
      buf.append("\\\\");
    } else if (b >= 32) {
      buf.append((char)b);
    } else if (b == 13) {
      buf.append("\\r");
    } else if (b == 10) {
      buf.append("\\n");
    } else if (b == 9) {
      buf.append("\\t");
    } else {
      buf.append("\\x").append(TypeUtil.toHexString(b));
    }
  }
  




  public static String toHexSummary(ByteBuffer buffer)
  {
    if (buffer == null)
      return "null";
    StringBuilder buf = new StringBuilder();
    
    buf.append("b[").append(buffer.remaining()).append("]=");
    for (int i = buffer.position(); i < buffer.limit(); i++)
    {
      TypeUtil.toHex(buffer.get(i), buf);
      if ((i == buffer.position() + 24) && (buffer.limit() > buffer.position() + 32))
      {
        buf.append("...");
        i = buffer.limit() - 8;
      }
    }
    return buf.toString();
  }
  





  public static String toHexString(ByteBuffer buffer)
  {
    if (buffer == null)
      return "null";
    return TypeUtil.toHexString(toArray(buffer));
  }
  

  private static final int[] decDivisors = { 1000000000, 100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1 };
  

  private static final int[] hexDivisors = { 268435456, 16777216, 1048576, 65536, 4096, 256, 16, 1 };
  

  private static final long[] decDivisorsL = { 1000000000000000000L, 100000000000000000L, 10000000000000000L, 1000000000000000L, 100000000000000L, 10000000000000L, 1000000000000L, 100000000000L, 10000000000L, 1000000000L, 100000000L, 10000000L, 1000000L, 100000L, 10000L, 1000L, 100L, 10L, 1L };
  


  public static void putCRLF(ByteBuffer buffer)
  {
    buffer.put((byte)13);
    buffer.put((byte)10);
  }
  
  public static boolean isPrefix(ByteBuffer prefix, ByteBuffer buffer)
  {
    if (prefix.remaining() > buffer.remaining())
      return false;
    int bi = buffer.position();
    for (int i = prefix.position(); i < prefix.limit(); i++)
      if (prefix.get(i) != buffer.get(bi++))
        return false;
    return true;
  }
  
  public static ByteBuffer ensureCapacity(ByteBuffer buffer, int capacity)
  {
    if (buffer == null) {
      return allocate(capacity);
    }
    if (buffer.capacity() >= capacity) {
      return buffer;
    }
    if (buffer.hasArray()) {
      return ByteBuffer.wrap(Arrays.copyOfRange(buffer.array(), buffer.arrayOffset(), buffer.arrayOffset() + capacity), buffer.position(), buffer.remaining());
    }
    throw new UnsupportedOperationException();
  }
}
