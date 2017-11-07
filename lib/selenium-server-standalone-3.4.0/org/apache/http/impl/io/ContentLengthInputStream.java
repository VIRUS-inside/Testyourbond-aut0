package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ConnectionClosedException;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;


















































public class ContentLengthInputStream
  extends InputStream
{
  private static final int BUFFER_SIZE = 2048;
  private final long contentLength;
  private long pos = 0L;
  

  private boolean closed = false;
  



  private SessionInputBuffer in = null;
  








  public ContentLengthInputStream(SessionInputBuffer in, long contentLength)
  {
    this.in = ((SessionInputBuffer)Args.notNull(in, "Session input buffer"));
    this.contentLength = Args.notNegative(contentLength, "Content length");
  }
  






  public void close()
    throws IOException
  {
    if (!closed) {
      try {
        if (pos < contentLength) {
          byte[] buffer = new byte['ࠀ'];
          while (read(buffer) >= 0) {}
        }
        
      }
      finally
      {
        closed = true;
      }
    }
  }
  
  public int available() throws IOException
  {
    if ((in instanceof BufferInfo)) {
      int len = ((BufferInfo)in).length();
      return Math.min(len, (int)(contentLength - pos));
    }
    return 0;
  }
  






  public int read()
    throws IOException
  {
    if (closed) {
      throw new IOException("Attempted read from closed stream.");
    }
    
    if (pos >= contentLength) {
      return -1;
    }
    int b = in.read();
    if (b == -1) {
      if (pos < contentLength) {
        throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + contentLength + "; received: " + pos);
      }
      
    }
    else {
      pos += 1L;
    }
    return b;
  }
  











  public int read(byte[] b, int off, int len)
    throws IOException
  {
    if (closed) {
      throw new IOException("Attempted read from closed stream.");
    }
    
    if (pos >= contentLength) {
      return -1;
    }
    
    int chunk = len;
    if (pos + len > contentLength) {
      chunk = (int)(contentLength - pos);
    }
    int count = in.read(b, off, chunk);
    if ((count == -1) && (pos < contentLength)) {
      throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + contentLength + "; received: " + pos);
    }
    

    if (count > 0) {
      pos += count;
    }
    return count;
  }
  







  public int read(byte[] b)
    throws IOException
  {
    return read(b, 0, b.length);
  }
  







  public long skip(long n)
    throws IOException
  {
    if (n <= 0L) {
      return 0L;
    }
    byte[] buffer = new byte['ࠀ'];
    

    long remaining = Math.min(n, contentLength - pos);
    
    long count = 0L;
    while (remaining > 0L) {
      int l = read(buffer, 0, (int)Math.min(2048L, remaining));
      if (l == -1) {
        break;
      }
      count += l;
      remaining -= l;
    }
    return count;
  }
}
