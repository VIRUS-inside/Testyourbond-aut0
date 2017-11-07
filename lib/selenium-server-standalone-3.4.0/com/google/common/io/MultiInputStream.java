package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.annotation.Nullable;

























@GwtIncompatible
final class MultiInputStream
  extends InputStream
{
  private Iterator<? extends ByteSource> it;
  private InputStream in;
  
  public MultiInputStream(Iterator<? extends ByteSource> it)
    throws IOException
  {
    this.it = ((Iterator)Preconditions.checkNotNull(it));
    advance();
  }
  
  public void close() throws IOException
  {
    if (in != null) {
      try {
        in.close();
        
        in = null; } finally { in = null;
      }
    }
  }
  

  private void advance()
    throws IOException
  {
    close();
    if (it.hasNext()) {
      in = ((ByteSource)it.next()).openStream();
    }
  }
  
  public int available() throws IOException
  {
    if (in == null) {
      return 0;
    }
    return in.available();
  }
  
  public boolean markSupported()
  {
    return false;
  }
  
  public int read() throws IOException
  {
    if (in == null) {
      return -1;
    }
    int result = in.read();
    if (result == -1) {
      advance();
      return read();
    }
    return result;
  }
  
  public int read(@Nullable byte[] b, int off, int len) throws IOException
  {
    if (in == null) {
      return -1;
    }
    int result = in.read(b, off, len);
    if (result == -1) {
      advance();
      return read(b, off, len);
    }
    return result;
  }
  
  public long skip(long n) throws IOException
  {
    if ((in == null) || (n <= 0L)) {
      return 0L;
    }
    long result = in.skip(n);
    if (result != 0L) {
      return result;
    }
    if (read() == -1) {
      return 0L;
    }
    return 1L + in.skip(n - 1L);
  }
}
