package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;



















































public class ContentLengthOutputStream
  extends OutputStream
{
  private final SessionOutputBuffer out;
  private final long contentLength;
  private long total = 0L;
  

  private boolean closed = false;
  










  public ContentLengthOutputStream(SessionOutputBuffer out, long contentLength)
  {
    this.out = ((SessionOutputBuffer)Args.notNull(out, "Session output buffer"));
    this.contentLength = Args.notNegative(contentLength, "Content length");
  }
  




  public void close()
    throws IOException
  {
    if (!closed) {
      closed = true;
      out.flush();
    }
  }
  
  public void flush() throws IOException
  {
    out.flush();
  }
  
  public void write(byte[] b, int off, int len) throws IOException
  {
    if (closed) {
      throw new IOException("Attempted write to closed stream.");
    }
    if (total < contentLength) {
      long max = contentLength - total;
      int chunk = len;
      if (chunk > max) {
        chunk = (int)max;
      }
      out.write(b, off, chunk);
      total += chunk;
    }
  }
  
  public void write(byte[] b) throws IOException
  {
    write(b, 0, b.length);
  }
  
  public void write(int b) throws IOException
  {
    if (closed) {
      throw new IOException("Attempted write to closed stream.");
    }
    if (total < contentLength) {
      out.write(b);
      total += 1L;
    }
  }
}
