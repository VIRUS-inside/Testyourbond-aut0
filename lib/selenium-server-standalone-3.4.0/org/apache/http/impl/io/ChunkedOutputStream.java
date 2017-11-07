package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.io.SessionOutputBuffer;











































public class ChunkedOutputStream
  extends OutputStream
{
  private final SessionOutputBuffer out;
  private final byte[] cache;
  private int cachePosition = 0;
  
  private boolean wroteLastChunk = false;
  

  private boolean closed = false;
  








  @Deprecated
  public ChunkedOutputStream(SessionOutputBuffer out, int bufferSize)
    throws IOException
  {
    this(bufferSize, out);
  }
  








  @Deprecated
  public ChunkedOutputStream(SessionOutputBuffer out)
    throws IOException
  {
    this(2048, out);
  }
  






  public ChunkedOutputStream(int bufferSize, SessionOutputBuffer out)
  {
    cache = new byte[bufferSize];
    this.out = out;
  }
  

  protected void flushCache()
    throws IOException
  {
    if (cachePosition > 0) {
      out.writeLine(Integer.toHexString(cachePosition));
      out.write(cache, 0, cachePosition);
      out.writeLine("");
      cachePosition = 0;
    }
  }
  


  protected void flushCacheWithAppend(byte[] bufferToAppend, int off, int len)
    throws IOException
  {
    out.writeLine(Integer.toHexString(cachePosition + len));
    out.write(cache, 0, cachePosition);
    out.write(bufferToAppend, off, len);
    out.writeLine("");
    cachePosition = 0;
  }
  
  protected void writeClosingChunk() throws IOException
  {
    out.writeLine("0");
    out.writeLine("");
  }
  




  public void finish()
    throws IOException
  {
    if (!wroteLastChunk) {
      flushCache();
      writeClosingChunk();
      wroteLastChunk = true;
    }
  }
  
  public void write(int b)
    throws IOException
  {
    if (closed) {
      throw new IOException("Attempted write to closed stream.");
    }
    cache[cachePosition] = ((byte)b);
    cachePosition += 1;
    if (cachePosition == cache.length) {
      flushCache();
    }
  }
  



  public void write(byte[] b)
    throws IOException
  {
    write(b, 0, b.length);
  }
  



  public void write(byte[] src, int off, int len)
    throws IOException
  {
    if (closed) {
      throw new IOException("Attempted write to closed stream.");
    }
    if (len >= cache.length - cachePosition) {
      flushCacheWithAppend(src, off, len);
    } else {
      System.arraycopy(src, off, cache, cachePosition, len);
      cachePosition += len;
    }
  }
  


  public void flush()
    throws IOException
  {
    flushCache();
    out.flush();
  }
  


  public void close()
    throws IOException
  {
    if (!closed) {
      closed = true;
      finish();
      out.flush();
    }
  }
}
