package org.apache.commons.codec.binary;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;





























public class BaseNCodecOutputStream
  extends FilterOutputStream
{
  private final boolean doEncode;
  private final BaseNCodec baseNCodec;
  private final byte[] singleByte = new byte[1];
  
  private final BaseNCodec.Context context = new BaseNCodec.Context();
  
  public BaseNCodecOutputStream(OutputStream out, BaseNCodec basedCodec, boolean doEncode)
  {
    super(out);
    baseNCodec = basedCodec;
    this.doEncode = doEncode;
  }
  







  public void write(int i)
    throws IOException
  {
    singleByte[0] = ((byte)i);
    write(singleByte, 0, 1);
  }
  

















  public void write(byte[] b, int offset, int len)
    throws IOException
  {
    if (b == null)
      throw new NullPointerException();
    if ((offset < 0) || (len < 0))
      throw new IndexOutOfBoundsException();
    if ((offset > b.length) || (offset + len > b.length))
      throw new IndexOutOfBoundsException();
    if (len > 0) {
      if (doEncode) {
        baseNCodec.encode(b, offset, len, context);
      } else {
        baseNCodec.decode(b, offset, len, context);
      }
      flush(false);
    }
  }
  







  private void flush(boolean propagate)
    throws IOException
  {
    int avail = baseNCodec.available(context);
    if (avail > 0) {
      byte[] buf = new byte[avail];
      int c = baseNCodec.readResults(buf, 0, avail, context);
      if (c > 0) {
        out.write(buf, 0, c);
      }
    }
    if (propagate) {
      out.flush();
    }
  }
  





  public void flush()
    throws IOException
  {
    flush(true);
  }
  






  public void close()
    throws IOException
  {
    if (doEncode) {
      baseNCodec.encode(singleByte, 0, -1, context);
    } else {
      baseNCodec.decode(singleByte, 0, -1, context);
    }
    flush();
    out.close();
  }
}
