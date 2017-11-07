package org.apache.http.impl.auth;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;


























class HttpEntityDigester
  extends OutputStream
{
  private final MessageDigest digester;
  private boolean closed;
  private byte[] digest;
  
  HttpEntityDigester(MessageDigest digester)
  {
    this.digester = digester;
    this.digester.reset();
  }
  
  public void write(int b) throws IOException
  {
    if (closed) {
      throw new IOException("Stream has been already closed");
    }
    digester.update((byte)b);
  }
  
  public void write(byte[] b, int off, int len) throws IOException
  {
    if (closed) {
      throw new IOException("Stream has been already closed");
    }
    digester.update(b, off, len);
  }
  
  public void close() throws IOException
  {
    if (closed) {
      return;
    }
    closed = true;
    digest = digester.digest();
    super.close();
  }
  
  public byte[] getDigest() {
    return digest;
  }
}
