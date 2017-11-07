package org.apache.commons.codec.binary;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;





























public class BaseNCodecInputStream
  extends FilterInputStream
{
  private final BaseNCodec baseNCodec;
  private final boolean doEncode;
  private final byte[] singleByte = new byte[1];
  
  private final BaseNCodec.Context context = new BaseNCodec.Context();
  
  protected BaseNCodecInputStream(InputStream in, BaseNCodec baseNCodec, boolean doEncode) {
    super(in);
    this.doEncode = doEncode;
    this.baseNCodec = baseNCodec;
  }
  











  public int available()
    throws IOException
  {
    return context.eof ? 0 : 1;
  }
  







  public synchronized void mark(int readLimit) {}
  







  public boolean markSupported()
  {
    return false;
  }
  






  public int read()
    throws IOException
  {
    int r = read(singleByte, 0, 1);
    while (r == 0) {
      r = read(singleByte, 0, 1);
    }
    if (r > 0) {
      byte b = singleByte[0];
      return b < 0 ? 256 + b : b;
    }
    return -1;
  }
  


















  public int read(byte[] b, int offset, int len)
    throws IOException
  {
    if (b == null)
      throw new NullPointerException();
    if ((offset < 0) || (len < 0))
      throw new IndexOutOfBoundsException();
    if ((offset > b.length) || (offset + len > b.length))
      throw new IndexOutOfBoundsException();
    if (len == 0) {
      return 0;
    }
    int readLen = 0;
    















    while (readLen == 0) {
      if (!baseNCodec.hasData(context)) {
        byte[] buf = new byte[doEncode ? 'က' : ' '];
        int c = in.read(buf);
        if (doEncode) {
          baseNCodec.encode(buf, 0, c, context);
        } else {
          baseNCodec.decode(buf, 0, c, context);
        }
      }
      readLen = baseNCodec.readResults(b, offset, len, context);
    }
    return readLen;
  }
  








  public synchronized void reset()
    throws IOException
  {
    throw new IOException("mark/reset not supported");
  }
  





  public long skip(long n)
    throws IOException
  {
    if (n < 0L) {
      throw new IllegalArgumentException("Negative skip length: " + n);
    }
    

    byte[] b = new byte['Ȁ'];
    long todo = n;
    
    while (todo > 0L) {
      int len = (int)Math.min(b.length, todo);
      len = read(b, 0, len);
      if (len == -1) {
        break;
      }
      todo -= len;
    }
    
    return n - todo;
  }
}
