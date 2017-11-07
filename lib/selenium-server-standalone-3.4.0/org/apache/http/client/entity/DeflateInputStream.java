package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;





























public class DeflateInputStream
  extends InputStream
{
  private final InputStream sourceStream;
  
  public DeflateInputStream(InputStream wrapped)
    throws IOException
  {
    PushbackInputStream pushback = new PushbackInputStream(wrapped, 2);
    int i1 = pushback.read();
    int i2 = pushback.read();
    if ((i1 == -1) || (i2 == -1)) {
      throw new ZipException("Unexpected end of stream");
    }
    
    pushback.unread(i2);
    pushback.unread(i1);
    
    boolean nowrap = true;
    int b1 = i1 & 0xFF;
    int compressionMethod = b1 & 0xF;
    int compressionInfo = b1 >> 4 & 0xF;
    int b2 = i2 & 0xFF;
    if ((compressionMethod == 8) && (compressionInfo <= 7) && ((b1 << 8 | b2) % 31 == 0)) {
      nowrap = false;
    }
    sourceStream = new DeflateStream(pushback, new Inflater(nowrap));
  }
  


  public int read()
    throws IOException
  {
    return sourceStream.read();
  }
  


  public int read(byte[] b)
    throws IOException
  {
    return sourceStream.read(b);
  }
  


  public int read(byte[] b, int off, int len)
    throws IOException
  {
    return sourceStream.read(b, off, len);
  }
  


  public long skip(long n)
    throws IOException
  {
    return sourceStream.skip(n);
  }
  


  public int available()
    throws IOException
  {
    return sourceStream.available();
  }
  



  public void mark(int readLimit)
  {
    sourceStream.mark(readLimit);
  }
  


  public void reset()
    throws IOException
  {
    sourceStream.reset();
  }
  



  public boolean markSupported()
  {
    return sourceStream.markSupported();
  }
  


  public void close()
    throws IOException
  {
    sourceStream.close();
  }
  
  static class DeflateStream extends InflaterInputStream
  {
    private boolean closed = false;
    
    public DeflateStream(InputStream in, Inflater inflater) {
      super(inflater);
    }
    
    public void close() throws IOException
    {
      if (closed) {
        return;
      }
      closed = true;
      inf.end();
      super.close();
    }
  }
}
