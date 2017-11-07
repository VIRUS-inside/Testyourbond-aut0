package org.apache.xerces.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import org.apache.xerces.util.MessageFormatter;

public final class ASCIIReader
  extends Reader
{
  public static final int DEFAULT_BUFFER_SIZE = 2048;
  protected final InputStream fInputStream;
  protected final byte[] fBuffer;
  private final MessageFormatter fFormatter;
  private final Locale fLocale;
  
  public ASCIIReader(InputStream paramInputStream, MessageFormatter paramMessageFormatter, Locale paramLocale)
  {
    this(paramInputStream, 2048, paramMessageFormatter, paramLocale);
  }
  
  public ASCIIReader(InputStream paramInputStream, int paramInt, MessageFormatter paramMessageFormatter, Locale paramLocale)
  {
    this(paramInputStream, new byte[paramInt], paramMessageFormatter, paramLocale);
  }
  
  public ASCIIReader(InputStream paramInputStream, byte[] paramArrayOfByte, MessageFormatter paramMessageFormatter, Locale paramLocale)
  {
    fInputStream = paramInputStream;
    fBuffer = paramArrayOfByte;
    fFormatter = paramMessageFormatter;
    fLocale = paramLocale;
  }
  
  public int read()
    throws IOException
  {
    int i = fInputStream.read();
    if (i >= 128) {
      throw new MalformedByteSequenceException(fFormatter, fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidASCII", new Object[] { Integer.toString(i) });
    }
    return i;
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 > fBuffer.length) {
      paramInt2 = fBuffer.length;
    }
    int i = fInputStream.read(fBuffer, 0, paramInt2);
    for (int j = 0; j < i; j++)
    {
      int k = fBuffer[j];
      if (k < 0) {
        throw new MalformedByteSequenceException(fFormatter, fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "InvalidASCII", new Object[] { Integer.toString(k & 0xFF) });
      }
      paramArrayOfChar[(paramInt1 + j)] = ((char)k);
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
