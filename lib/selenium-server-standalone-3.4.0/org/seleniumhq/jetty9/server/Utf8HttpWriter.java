package org.seleniumhq.jetty9.server;

import java.io.IOException;
import org.seleniumhq.jetty9.util.ByteArrayOutputStream2;

























public class Utf8HttpWriter
  extends HttpWriter
{
  int _surrogate = 0;
  

  public Utf8HttpWriter(HttpOutput out)
  {
    super(out);
  }
  

  public void write(char[] s, int offset, int length)
    throws IOException
  {
    HttpOutput out = _out;
    if ((length == 0) && (out.isAllContentWritten()))
    {
      close();
      return;
    }
    
    while (length > 0)
    {
      _bytes.reset();
      int chars = length > 512 ? 512 : length;
      
      byte[] buffer = _bytes.getBuf();
      int bytes = _bytes.getCount();
      
      if (bytes + chars > buffer.length) {
        chars = buffer.length - bytes;
      }
      for (int i = 0; i < chars; i++)
      {
        int code = s[(offset + i)];
        

        if (_surrogate == 0)
        {

          if (Character.isHighSurrogate((char)code))
          {
            _surrogate = code;
            continue;
          }
          
        }
        else if (Character.isLowSurrogate((char)code))
        {
          code = Character.toCodePoint((char)_surrogate, (char)code);

        }
        else
        {
          code = _surrogate;
          _surrogate = 0;
          i--;
        }
        
        if ((code & 0xFFFFFF80) == 0)
        {

          if (bytes >= buffer.length)
          {
            chars = i;
            break;
          }
          buffer[(bytes++)] = ((byte)code);
        }
        else
        {
          if ((code & 0xF800) == 0)
          {

            if (bytes + 2 > buffer.length)
            {
              chars = i;
              break;
            }
            buffer[(bytes++)] = ((byte)(0xC0 | code >> 6));
            buffer[(bytes++)] = ((byte)(0x80 | code & 0x3F));
          }
          else if ((code & 0xFFFF0000) == 0)
          {

            if (bytes + 3 > buffer.length)
            {
              chars = i;
              break;
            }
            buffer[(bytes++)] = ((byte)(0xE0 | code >> 12));
            buffer[(bytes++)] = ((byte)(0x80 | code >> 6 & 0x3F));
            buffer[(bytes++)] = ((byte)(0x80 | code & 0x3F));
          }
          else if ((code & 0xFF200000) == 0)
          {

            if (bytes + 4 > buffer.length)
            {
              chars = i;
              break;
            }
            buffer[(bytes++)] = ((byte)(0xF0 | code >> 18));
            buffer[(bytes++)] = ((byte)(0x80 | code >> 12 & 0x3F));
            buffer[(bytes++)] = ((byte)(0x80 | code >> 6 & 0x3F));
            buffer[(bytes++)] = ((byte)(0x80 | code & 0x3F));
          }
          else if ((code & 0xF4000000) == 0)
          {

            if (bytes + 5 > buffer.length)
            {
              chars = i;
              break;
            }
            buffer[(bytes++)] = ((byte)(0xF8 | code >> 24));
            buffer[(bytes++)] = ((byte)(0x80 | code >> 18 & 0x3F));
            buffer[(bytes++)] = ((byte)(0x80 | code >> 12 & 0x3F));
            buffer[(bytes++)] = ((byte)(0x80 | code >> 6 & 0x3F));
            buffer[(bytes++)] = ((byte)(0x80 | code & 0x3F));
          }
          else if ((code & 0x80000000) == 0)
          {

            if (bytes + 6 > buffer.length)
            {
              chars = i;
              break;
            }
            buffer[(bytes++)] = ((byte)(0xFC | code >> 30));
            buffer[(bytes++)] = ((byte)(0x80 | code >> 24 & 0x3F));
            buffer[(bytes++)] = ((byte)(0x80 | code >> 18 & 0x3F));
            buffer[(bytes++)] = ((byte)(0x80 | code >> 12 & 0x3F));
            buffer[(bytes++)] = ((byte)(0x80 | code >> 6 & 0x3F));
            buffer[(bytes++)] = ((byte)(0x80 | code & 0x3F));
          }
          else
          {
            buffer[(bytes++)] = 63;
          }
          
          _surrogate = 0;
          
          if (bytes == buffer.length)
          {
            chars = i + 1;
            break;
          }
        }
      }
      _bytes.setCount(bytes);
      
      _bytes.writeTo(out);
      length -= chars;
      offset += chars;
    }
  }
}
