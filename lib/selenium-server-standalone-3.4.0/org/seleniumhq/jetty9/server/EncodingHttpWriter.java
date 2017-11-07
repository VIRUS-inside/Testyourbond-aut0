package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import org.seleniumhq.jetty9.util.ByteArrayOutputStream2;





















public class EncodingHttpWriter
  extends HttpWriter
{
  final Writer _converter;
  
  public EncodingHttpWriter(HttpOutput out, String encoding)
  {
    super(out);
    try
    {
      _converter = new OutputStreamWriter(_bytes, encoding);
    }
    catch (UnsupportedEncodingException e)
    {
      throw new RuntimeException(e);
    }
  }
  

  public void write(char[] s, int offset, int length)
    throws IOException
  {
    HttpOutput out = _out;
    if ((length == 0) && (out.isAllContentWritten()))
    {
      out.close();
      return;
    }
    
    while (length > 0)
    {
      _bytes.reset();
      int chars = length > 512 ? 512 : length;
      
      _converter.write(s, offset, chars);
      _converter.flush();
      _bytes.writeTo(out);
      length -= chars;
      offset += chars;
    }
  }
}
