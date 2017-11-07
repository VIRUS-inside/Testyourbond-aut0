package org.apache.xerces.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public final class Latin1Reader
  extends Reader
{
  public static final int DEFAULT_BUFFER_SIZE = 2048;
  protected final InputStream fInputStream;
  protected final byte[] fBuffer;
  
  public Latin1Reader(InputStream paramInputStream)
  {
    this(paramInputStream, 2048);
  }
  
  public Latin1Reader(InputStream paramInputStream, int paramInt)
  {
    this(paramInputStream, new byte[paramInt]);
  }
  
  public Latin1Reader(InputStream paramInputStream, byte[] paramArrayOfByte)
  {
    fInputStream = paramInputStream;
    fBuffer = paramArrayOfByte;
  }
  
  public int read()
    throws IOException
  {
    return fInputStream.read();
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 > fBuffer.length) {
      paramInt2 = fBuffer.length;
    }
    int i = fInputStream.read(fBuffer, 0, paramInt2);
    for (int j = 0; j < i; j++) {
      paramArrayOfChar[(paramInt1 + j)] = ((char)(fBuffer[j] & 0xFF));
    }
    return i;
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    return fInputStream.skip(paramLong);
  }
  
  public boolean ready()
    throws IOException
  {
    return false;
  }
  
  public boolean markSupported()
  {
    return fInputStream.markSupported();
  }
  
  public void mark(int paramInt)
    throws IOException
  {
    fInputStream.mark(paramInt);
  }
  
  public void reset()
    throws IOException
  {
    fInputStream.reset();
  }
  
  public void close()
    throws IOException
  {
    fInputStream.close();
  }
}
