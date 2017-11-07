package org.apache.commons.io.output;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.input.ClosedInputStream;











































public class ByteArrayOutputStream
  extends OutputStream
{
  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  

  private final List<byte[]> buffers = new ArrayList();
  
  private int currentBufferIndex;
  
  private int filledBufferSum;
  
  private byte[] currentBuffer;
  
  private int count;
  
  private boolean reuseBuffers = true;
  



  public ByteArrayOutputStream()
  {
    this(1024);
  }
  






  public ByteArrayOutputStream(int size)
  {
    if (size < 0) {
      throw new IllegalArgumentException("Negative initial size: " + size);
    }
    
    synchronized (this) {
      needNewBuffer(size);
    }
  }
  





  private void needNewBuffer(int newcount)
  {
    if (currentBufferIndex < buffers.size() - 1)
    {
      filledBufferSum += currentBuffer.length;
      
      currentBufferIndex += 1;
      currentBuffer = ((byte[])buffers.get(currentBufferIndex));
    }
    else {
      int newBufferSize;
      if (currentBuffer == null) {
        int newBufferSize = newcount;
        filledBufferSum = 0;
      } else {
        newBufferSize = Math.max(currentBuffer.length << 1, newcount - filledBufferSum);
        

        filledBufferSum += currentBuffer.length;
      }
      
      currentBufferIndex += 1;
      currentBuffer = new byte[newBufferSize];
      buffers.add(currentBuffer);
    }
  }
  






  public void write(byte[] b, int off, int len)
  {
    if ((off < 0) || (off > b.length) || (len < 0) || (off + len > b.length) || (off + len < 0))
    {



      throw new IndexOutOfBoundsException(); }
    if (len == 0) {
      return;
    }
    synchronized (this) {
      int newcount = count + len;
      int remaining = len;
      int inBufferPos = count - filledBufferSum;
      while (remaining > 0) {
        int part = Math.min(remaining, currentBuffer.length - inBufferPos);
        System.arraycopy(b, off + len - remaining, currentBuffer, inBufferPos, part);
        remaining -= part;
        if (remaining > 0) {
          needNewBuffer(newcount);
          inBufferPos = 0;
        }
      }
      count = newcount;
    }
  }
  




  public synchronized void write(int b)
  {
    int inBufferPos = count - filledBufferSum;
    if (inBufferPos == currentBuffer.length) {
      needNewBuffer(count + 1);
      inBufferPos = 0;
    }
    currentBuffer[inBufferPos] = ((byte)b);
    count += 1;
  }
  









  public synchronized int write(InputStream in)
    throws IOException
  {
    int readCount = 0;
    int inBufferPos = count - filledBufferSum;
    int n = in.read(currentBuffer, inBufferPos, currentBuffer.length - inBufferPos);
    while (n != -1) {
      readCount += n;
      inBufferPos += n;
      count += n;
      if (inBufferPos == currentBuffer.length) {
        needNewBuffer(currentBuffer.length);
        inBufferPos = 0;
      }
      n = in.read(currentBuffer, inBufferPos, currentBuffer.length - inBufferPos);
    }
    return readCount;
  }
  



  public synchronized int size()
  {
    return count;
  }
  






  public void close()
    throws IOException
  {}
  





  public synchronized void reset()
  {
    count = 0;
    filledBufferSum = 0;
    currentBufferIndex = 0;
    if (reuseBuffers) {
      currentBuffer = ((byte[])buffers.get(currentBufferIndex));
    }
    else {
      currentBuffer = null;
      int size = ((byte[])buffers.get(0)).length;
      buffers.clear();
      needNewBuffer(size);
      reuseBuffers = true;
    }
  }
  






  public synchronized void writeTo(OutputStream out)
    throws IOException
  {
    int remaining = count;
    for (byte[] buf : buffers) {
      int c = Math.min(buf.length, remaining);
      out.write(buf, 0, c);
      remaining -= c;
      if (remaining == 0) {
        break;
      }
    }
  }
  




















  public static InputStream toBufferedInputStream(InputStream input)
    throws IOException
  {
    return toBufferedInputStream(input, 1024);
  }
  























  public static InputStream toBufferedInputStream(InputStream input, int size)
    throws IOException
  {
    ByteArrayOutputStream output = new ByteArrayOutputStream(size);
    output.write(input);
    return output.toInputStream();
  }
  









  public synchronized InputStream toInputStream()
  {
    int remaining = count;
    if (remaining == 0) {
      return new ClosedInputStream();
    }
    List<ByteArrayInputStream> list = new ArrayList(buffers.size());
    for (byte[] buf : buffers) {
      int c = Math.min(buf.length, remaining);
      list.add(new ByteArrayInputStream(buf, 0, c));
      remaining -= c;
      if (remaining == 0) {
        break;
      }
    }
    reuseBuffers = false;
    return new SequenceInputStream(Collections.enumeration(list));
  }
  






  public synchronized byte[] toByteArray()
  {
    int remaining = count;
    if (remaining == 0) {
      return EMPTY_BYTE_ARRAY;
    }
    byte[] newbuf = new byte[remaining];
    int pos = 0;
    for (byte[] buf : buffers) {
      int c = Math.min(buf.length, remaining);
      System.arraycopy(buf, 0, newbuf, pos, c);
      pos += c;
      remaining -= c;
      if (remaining == 0) {
        break;
      }
    }
    return newbuf;
  }
  








  @Deprecated
  public String toString()
  {
    return new String(toByteArray(), Charset.defaultCharset());
  }
  







  public String toString(String enc)
    throws UnsupportedEncodingException
  {
    return new String(toByteArray(), enc);
  }
  








  public String toString(Charset charset)
  {
    return new String(toByteArray(), charset);
  }
}
