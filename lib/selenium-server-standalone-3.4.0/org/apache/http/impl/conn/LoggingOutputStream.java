package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.OutputStream;
































class LoggingOutputStream
  extends OutputStream
{
  private final OutputStream out;
  private final Wire wire;
  
  public LoggingOutputStream(OutputStream out, Wire wire)
  {
    this.out = out;
    this.wire = wire;
  }
  
  public void write(int b) throws IOException
  {
    try {
      wire.output(b);
    } catch (IOException ex) {
      wire.output("[write] I/O error: " + ex.getMessage());
      throw ex;
    }
  }
  
  public void write(byte[] b) throws IOException
  {
    try {
      wire.output(b);
      out.write(b);
    } catch (IOException ex) {
      wire.output("[write] I/O error: " + ex.getMessage());
      throw ex;
    }
  }
  
  public void write(byte[] b, int off, int len) throws IOException
  {
    try {
      wire.output(b, off, len);
      out.write(b, off, len);
    } catch (IOException ex) {
      wire.output("[write] I/O error: " + ex.getMessage());
      throw ex;
    }
  }
  
  public void flush() throws IOException
  {
    try {
      out.flush();
    } catch (IOException ex) {
      wire.output("[flush] I/O error: " + ex.getMessage());
      throw ex;
    }
  }
  
  public void close() throws IOException
  {
    try {
      out.close();
    } catch (IOException ex) {
      wire.output("[close] I/O error: " + ex.getMessage());
      throw ex;
    }
  }
}
