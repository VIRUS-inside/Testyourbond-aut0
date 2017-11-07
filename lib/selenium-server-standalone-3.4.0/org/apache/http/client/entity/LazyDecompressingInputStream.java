package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;































class LazyDecompressingInputStream
  extends InputStream
{
  private final InputStream wrappedStream;
  private final InputStreamFactory inputStreamFactory;
  private InputStream wrapperStream;
  
  public LazyDecompressingInputStream(InputStream wrappedStream, InputStreamFactory inputStreamFactory)
  {
    this.wrappedStream = wrappedStream;
    this.inputStreamFactory = inputStreamFactory;
  }
  
  private void initWrapper() throws IOException {
    if (wrapperStream == null) {
      wrapperStream = inputStreamFactory.create(wrappedStream);
    }
  }
  
  public int read() throws IOException
  {
    initWrapper();
    return wrapperStream.read();
  }
  
  public int read(byte[] b) throws IOException
  {
    initWrapper();
    return wrapperStream.read(b);
  }
  
  public int read(byte[] b, int off, int len) throws IOException
  {
    initWrapper();
    return wrapperStream.read(b, off, len);
  }
  
  public long skip(long n) throws IOException
  {
    initWrapper();
    return wrapperStream.skip(n);
  }
  
  public boolean markSupported()
  {
    return false;
  }
  
  public int available() throws IOException
  {
    initWrapper();
    return wrapperStream.available();
  }
  
  public void close() throws IOException
  {
    try {
      if (wrapperStream != null) {
        wrapperStream.close();
      }
    } finally {
      wrappedStream.close();
    }
  }
}
