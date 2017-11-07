package org.seleniumhq.jetty9.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;























public class ReadLineInputStream
  extends BufferedInputStream
{
  boolean _seenCRLF;
  boolean _skipLF;
  
  public ReadLineInputStream(InputStream in)
  {
    super(in);
  }
  
  public ReadLineInputStream(InputStream in, int size)
  {
    super(in, size);
  }
  
  public String readLine() throws IOException
  {
    mark(buf.length);
    
    for (;;)
    {
      int b = super.read();
      
      if (markpos < 0) {
        throw new IOException("Buffer size exceeded: no line terminator");
      }
      if (b == -1)
      {
        int m = markpos;
        markpos = -1;
        if (pos > m) {
          return new String(buf, m, pos - m, StandardCharsets.UTF_8);
        }
        return null;
      }
      
      if (b == 13)
      {
        int p = pos;
        

        if ((_seenCRLF) && (pos < count))
        {
          if (buf[pos] == 10) {
            pos += 1;
          }
        } else
          _skipLF = true;
        int m = markpos;
        markpos = -1;
        return new String(buf, m, p - m - 1, StandardCharsets.UTF_8);
      }
      
      if (b == 10)
      {
        if (_skipLF)
        {
          _skipLF = false;
          _seenCRLF = true;
          markpos += 1;
        }
        else {
          int m = markpos;
          markpos = -1;
          return new String(buf, m, pos - m - 1, StandardCharsets.UTF_8);
        }
      }
    }
  }
  
  public synchronized int read() throws IOException
  {
    int b = super.read();
    if (_skipLF)
    {
      _skipLF = false;
      if ((_seenCRLF) && (b == 10))
        b = super.read();
    }
    return b;
  }
  
  public synchronized int read(byte[] buf, int off, int len)
    throws IOException
  {
    if ((_skipLF) && (len > 0))
    {
      _skipLF = false;
      if (_seenCRLF)
      {
        int b = super.read();
        if (b == -1) {
          return -1;
        }
        if (b != 10)
        {
          buf[off] = ((byte)(0xFF & b));
          return 1 + super.read(buf, off + 1, len - 1);
        }
      }
    }
    
    return super.read(buf, off, len);
  }
}
