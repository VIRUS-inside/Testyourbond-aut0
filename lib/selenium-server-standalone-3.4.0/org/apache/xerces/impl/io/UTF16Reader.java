package org.apache.xerces.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import org.apache.xerces.impl.msg.XMLMessageFormatter;
import org.apache.xerces.util.MessageFormatter;

public final class UTF16Reader
  extends Reader
{
  public static final int DEFAULT_BUFFER_SIZE = 4096;
  protected final InputStream fInputStream;
  protected final byte[] fBuffer;
  protected final boolean fIsBigEndian;
  private final MessageFormatter fFormatter;
  private final Locale fLocale;
  
  public UTF16Reader(InputStream paramInputStream, boolean paramBoolean)
  {
    this(paramInputStream, 4096, paramBoolean, new XMLMessageFormatter(), Locale.getDefault());
  }
  
  public UTF16Reader(InputStream paramInputStream, boolean paramBoolean, MessageFormatter paramMessageFormatter, Locale paramLocale)
  {
    this(paramInputStream, 4096, paramBoolean, paramMessageFormatter, paramLocale);
  }
  
  public UTF16Reader(InputStream paramInputStream, int paramInt, boolean paramBoolean, MessageFormatter paramMessageFormatter, Locale paramLocale)
  {
    this(paramInputStream, new byte[paramInt], paramBoolean, paramMessageFormatter, paramLocale);
  }
  
  public UTF16Reader(InputStream paramInputStream, byte[] paramArrayOfByte, boolean paramBoolean, MessageFormatter paramMessageFormatter, Locale paramLocale)
  {
    fInputStream = paramInputStream;
    fBuffer = paramArrayOfByte;
    fIsBigEndian = paramBoolean;
    fFormatter = paramMessageFormatter;
    fLocale = paramLocale;
  }
  
  public int read()
    throws IOException
  {
    int i = fInputStream.read();
    if (i == -1) {
      return -1;
    }
    int j = fInputStream.read();
    if (j == -1) {
      expectedTwoBytes();
    }
    if (fIsBigEndian) {
      return i << 8 | j;
    }
    return j << 8 | i;
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = paramInt2 << 1;
    if (i > fBuffer.length) {
      i = fBuffer.length;
    }
    int j = fInputStream.read(fBuffer, 0, i);
    if (j == -1) {
      return -1;
    }
    if ((j & 0x1) != 0)
    {
      k = fInputStream.read();
      if (k == -1) {
        expectedTwoBytes();
      }
      fBuffer[(j++)] = ((byte)k);
    }
    int k = j >> 1;
    if (fIsBigEndian) {
      processBE(paramArrayOfChar, paramInt1, k);
    } else {
      processLE(paramArrayOfChar, paramInt1, k);
    }
    return k;
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    long l = fInputStream.skip(paramLong << 1);
    if ((l & 1L) != 0L)
    {
      int i = fInputStream.read();
      if (i == -1) {
        expectedTwoBytes();
      }
      l += 1L;
    }
    return l >> 1;
  }
  
  public boolean ready()
    throws IOException
  {
    return false;
  }
  
  public boolean markSupported()
  {
    return false;
  }
  
  public void mark(int paramInt)
    throws IOException
  {
    throw new IOException(fFormatter.formatMessage(fLocale, "OperationNotSupported", new Object[] { "mark()", "UTF-16" }));
  }
  
  public void reset()
    throws IOException
  {}
  
  public void close()
    throws IOException
  {
    fInputStream.close();
  }
  
  private void processBE(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = 0; j < paramInt2; j++)
    {
      int k = fBuffer[(i++)] & 0xFF;
      int m = fBuffer[(i++)] & 0xFF;
      paramArrayOfChar[(paramInt1++)] = ((char)(k << 8 | m));
    }
  }
  
  private void processLE(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = 0;
    for (int j = 0; j < paramInt2; j++)
    {
      int k = fBuffer[(i++)] & 0xFF;
      int m = fBuffer[(i++)] & 0xFF;
      paramArrayOfChar[(paramInt1++)] = ((char)(m << 8 | k));
    }
  }
  
  private void expectedTwoBytes()
    throws MalformedByteSequenceException
  {
    throw new MalformedByteSequenceException(fFormatter, fLocale, "http://www.w3.org/TR/1998/REC-xml-19980210", "ExpectedByte", new Object[] { "2", "2" });
  }
}
