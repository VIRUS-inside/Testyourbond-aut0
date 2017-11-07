package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;









































public class IdentityInputStream
  extends InputStream
{
  private final SessionInputBuffer in;
  private boolean closed = false;
  





  public IdentityInputStream(SessionInputBuffer in)
  {
    this.in = ((SessionInputBuffer)Args.notNull(in, "Session input buffer"));
  }
  
  public int available() throws IOException
  {
    if ((in instanceof BufferInfo)) {
      return ((BufferInfo)in).length();
    }
    return 0;
  }
  
  public void close()
    throws IOException
  {
    closed = true;
  }
  
  public int read() throws IOException
  {
    if (closed) {
      return -1;
    }
    return in.read();
  }
  
  public int read(byte[] b, int off, int len)
    throws IOException
  {
    if (closed) {
      return -1;
    }
    return in.read(b, off, len);
  }
}
