package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InputStream;
































class LoggingInputStream
  extends InputStream
{
  private final InputStream in;
  private final Wire wire;
  
  public LoggingInputStream(InputStream in, Wire wire)
  {
    this.in = in;
    this.wire = wire;
  }
  
  public int read() throws IOException
  {
    try {
      int b = in.read();
      if (b == -1) {
        wire.input("end of stream");
      } else {
        wire.input(b);
      }
      return b;
    } catch (IOException ex) {
      wire.input("[read] I/O error: " + ex.getMessage());
      throw ex;
    }
  }
  
  public int read(byte[] b) throws IOException
  {
    try {
      int bytesRead = in.read(b);
      if (bytesRead == -1) {
        wire.input("end of stream");
      } else if (bytesRead > 0) {
        wire.input(b, 0, bytesRead);
      }
      return bytesRead;
    } catch (IOException ex) {
      wire.input("[read] I/O error: " + ex.getMessage());
      throw ex;
    }
  }
  
  public int read(byte[] b, int off, int len) throws IOException
  {
    try {
      int bytesRead = in.read(b, off, len);
      if (bytesRead == -1) {
        wire.input("end of stream");
      } else if (bytesRead > 0) {
        wire.input(b, off, bytesRead);
      }
      return bytesRead;
    } catch (IOException ex) {
      wire.input("[read] I/O error: " + ex.getMessage());
      throw ex;
    }
  }
  
  public long skip(long n) throws IOException
  {
    try {
      return super.skip(n);
    } catch (IOException ex) {
      wire.input("[skip] I/O error: " + ex.getMessage());
      throw ex;
    }
  }
  
  public int available() throws IOException
  {
    try {
      return in.available();
    } catch (IOException ex) {
      wire.input("[available] I/O error : " + ex.getMessage());
      throw ex;
    }
  }
  
  public void mark(int readlimit)
  {
    super.mark(readlimit);
  }
  
  public void reset() throws IOException
  {
    super.reset();
  }
  
  public boolean markSupported()
  {
    return false;
  }
  
  public void close() throws IOException
  {
    try {
      in.close();
    } catch (IOException ex) {
      wire.input("[close] I/O error: " + ex.getMessage());
      throw ex;
    }
  }
}
