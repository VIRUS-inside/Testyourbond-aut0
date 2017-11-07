package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;





















@Beta
@GwtIncompatible
public final class CountingInputStream
  extends FilterInputStream
{
  private long count;
  private long mark = -1L;
  




  public CountingInputStream(InputStream in)
  {
    super((InputStream)Preconditions.checkNotNull(in));
  }
  
  public long getCount()
  {
    return count;
  }
  
  public int read() throws IOException
  {
    int result = in.read();
    if (result != -1) {
      count += 1L;
    }
    return result;
  }
  
  public int read(byte[] b, int off, int len) throws IOException
  {
    int result = in.read(b, off, len);
    if (result != -1) {
      count += result;
    }
    return result;
  }
  
  public long skip(long n) throws IOException
  {
    long result = in.skip(n);
    count += result;
    return result;
  }
  
  public synchronized void mark(int readlimit)
  {
    in.mark(readlimit);
    mark = count;
  }
  
  public synchronized void reset()
    throws IOException
  {
    if (!in.markSupported()) {
      throw new IOException("Mark not supported");
    }
    if (mark == -1L) {
      throw new IOException("Mark not set");
    }
    
    in.reset();
    count = mark;
  }
}
