package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.io.Writer;
import org.seleniumhq.jetty9.util.ByteArrayOutputStream2;
























public abstract class HttpWriter
  extends Writer
{
  public static final int MAX_OUTPUT_CHARS = 512;
  final HttpOutput _out;
  final ByteArrayOutputStream2 _bytes;
  final char[] _chars;
  
  public HttpWriter(HttpOutput out)
  {
    _out = out;
    _chars = new char['Ȁ'];
    _bytes = new ByteArrayOutputStream2(512);
  }
  

  public void close()
    throws IOException
  {
    _out.close();
  }
  

  public void flush()
    throws IOException
  {
    _out.flush();
  }
  

  public void write(String s, int offset, int length)
    throws IOException
  {
    while (length > 512)
    {
      write(s, offset, 512);
      offset += 512;
      length -= 512;
    }
    
    s.getChars(offset, offset + length, _chars, 0);
    write(_chars, 0, length);
  }
  

  public void write(char[] s, int offset, int length)
    throws IOException
  {
    throw new AbstractMethodError();
  }
}
