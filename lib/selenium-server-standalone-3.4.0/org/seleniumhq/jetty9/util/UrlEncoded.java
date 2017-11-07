package org.seleniumhq.jetty9.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map.Entry;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;













































public class UrlEncoded
  extends MultiMap<String>
  implements Cloneable
{
  static final Logger LOG = Log.getLogger(UrlEncoded.class);
  public static final Charset ENCODING;
  
  static
  {
    Charset encoding;
    try
    {
      String charset = System.getProperty("org.seleniumhq.jetty9.util.UrlEncoding.charset");
      encoding = charset == null ? StandardCharsets.UTF_8 : Charset.forName(charset);
    }
    catch (Exception e) {
      Charset encoding;
      LOG.warn(e);
      encoding = StandardCharsets.UTF_8;
    }
    ENCODING = encoding;
  }
  

  public UrlEncoded(UrlEncoded url)
  {
    super(url);
  }
  





  public UrlEncoded(String query)
  {
    decodeTo(query, this, ENCODING);
  }
  

  public void decode(String query)
  {
    decodeTo(query, this, ENCODING);
  }
  

  public void decode(String query, Charset charset)
  {
    decodeTo(query, this, charset);
  }
  




  public String encode()
  {
    return encode(ENCODING, false);
  }
  





  public String encode(Charset charset)
  {
    return encode(charset, false);
  }
  








  public synchronized String encode(Charset charset, boolean equalsForNullValue)
  {
    return encode(this, charset, equalsForNullValue);
  }
  








  public static String encode(MultiMap<String> map, Charset charset, boolean equalsForNullValue)
  {
    if (charset == null) {
      charset = ENCODING;
    }
    StringBuilder result = new StringBuilder(128);
    
    boolean delim = false;
    for (Map.Entry<String, List<String>> entry : map.entrySet())
    {
      String key = ((String)entry.getKey()).toString();
      List<String> list = (List)entry.getValue();
      int s = list.size();
      
      if (delim)
      {
        result.append('&');
      }
      
      if (s == 0)
      {
        result.append(encodeString(key, charset));
        if (equalsForNullValue) {
          result.append('=');
        }
      }
      else {
        for (int i = 0; i < s; i++)
        {
          if (i > 0)
            result.append('&');
          String val = (String)list.get(i);
          result.append(encodeString(key, charset));
          
          if (val != null)
          {
            String str = val.toString();
            if (str.length() > 0)
            {
              result.append('=');
              result.append(encodeString(str, charset));
            }
            else if (equalsForNullValue) {
              result.append('=');
            }
          } else if (equalsForNullValue) {
            result.append('=');
          }
        } }
      delim = true;
    }
    return result.toString();
  }
  






  public static void decodeTo(String content, MultiMap<String> map, String charset)
  {
    decodeTo(content, map, charset == null ? null : Charset.forName(charset));
  }
  






  public static void decodeTo(String content, MultiMap<String> map, Charset charset)
  {
    if (charset == null) {
      charset = ENCODING;
    }
    if (charset == StandardCharsets.UTF_8)
    {
      decodeUtf8To(content, 0, content.length(), map);
      return;
    }
    
    synchronized (map)
    {
      String key = null;
      String value = null;
      int mark = -1;
      boolean encoded = false;
      for (int i = 0; i < content.length(); i++)
      {
        char c = content.charAt(i);
        switch (c)
        {
        case '&': 
          int l = i - mark - 1;
          
          value = encoded ? decodeString(content, mark + 1, l, charset) : l == 0 ? "" : content.substring(mark + 1, i);
          mark = i;
          encoded = false;
          if (key != null)
          {
            map.add(key, value);
          }
          else if ((value != null) && (value.length() > 0))
          {
            map.add(value, "");
          }
          key = null;
          value = null;
          break;
        case '=': 
          if (key == null)
          {
            key = encoded ? decodeString(content, mark + 1, i - mark - 1, charset) : content.substring(mark + 1, i);
            mark = i;
            encoded = false; }
          break;
        case '+': 
          encoded = true;
          break;
        case '%': 
          encoded = true;
        }
        
      }
      
      if (key != null)
      {
        int l = content.length() - mark - 1;
        value = encoded ? decodeString(content, mark + 1, l, charset) : l == 0 ? "" : content.substring(mark + 1);
        map.add(key, value);
      }
      else if (mark < content.length())
      {


        key = encoded ? decodeString(content, mark + 1, content.length() - mark - 1, charset) : content.substring(mark + 1);
        if ((key != null) && (key.length() > 0))
        {
          map.add(key, "");
        }
      }
    }
  }
  

  public static void decodeUtf8To(String query, MultiMap<String> map)
  {
    decodeUtf8To(query, 0, query.length(), map);
  }
  







  public static void decodeUtf8To(String query, int offset, int length, MultiMap<String> map)
  {
    Utf8StringBuilder buffer = new Utf8StringBuilder();
    synchronized (map)
    {
      String key = null;
      String value = null;
      
      int end = offset + length;
      for (int i = offset; i < end; i++)
      {
        char c = query.charAt(i);
        try
        {
          switch (c)
          {
          case '&': 
            value = buffer.toReplacedString();
            buffer.reset();
            if (key != null)
            {
              map.add(key, value);
            }
            else if ((value != null) && (value.length() > 0))
            {
              map.add(value, "");
            }
            key = null;
            value = null;
            break;
          
          case '=': 
            if (key != null)
            {
              buffer.append(c);
            }
            else {
              key = buffer.toReplacedString();
              buffer.reset(); }
            break;
          
          case '+': 
            buffer.append((byte)32);
            break;
          
          case '%': 
            if (i + 2 < end)
            {
              if ('u' == query.charAt(i + 1))
              {
                i++;
                if (i + 4 < end)
                {
                  char top = query.charAt(++i);
                  char hi = query.charAt(++i);
                  char lo = query.charAt(++i);
                  char bot = query.charAt(++i);
                  buffer.getStringBuilder().append(Character.toChars((TypeUtil.convertHexDigit(top) << 12) + (TypeUtil.convertHexDigit(hi) << 8) + (TypeUtil.convertHexDigit(lo) << 4) + TypeUtil.convertHexDigit(bot)));
                }
                else
                {
                  buffer.getStringBuilder().append(65533);
                  i = end;
                }
              }
              else
              {
                char hi = query.charAt(++i);
                char lo = query.charAt(++i);
                buffer.append((byte)((TypeUtil.convertHexDigit(hi) << 4) + TypeUtil.convertHexDigit(lo)));
              }
            }
            else
            {
              buffer.getStringBuilder().append(65533);
              i = end;
            }
            break;
          
          default: 
            buffer.append(c);
          }
          
        }
        catch (Utf8Appendable.NotUtf8Exception e)
        {
          LOG.warn(e.toString(), new Object[0]);
          LOG.debug(e);
        }
        catch (NumberFormatException e)
        {
          buffer.append(Utf8Appendable.REPLACEMENT_UTF8, 0, 3);
          LOG.warn(e.toString(), new Object[0]);
          LOG.debug(e);
        }
      }
      
      if (key != null)
      {
        value = buffer.toReplacedString();
        buffer.reset();
        map.add(key, value);
      }
      else if (buffer.length() > 0)
      {
        map.add(buffer.toReplacedString(), "");
      }
    }
  }
  









  public static void decode88591To(InputStream in, MultiMap<String> map, int maxLength, int maxKeys)
    throws IOException
  {
    synchronized (map)
    {
      StringBuffer buffer = new StringBuffer();
      String key = null;
      String value = null;
      


      int totalLength = 0;
      int b; while ((b = in.read()) >= 0)
      {
        switch ((char)b)
        {
        case '&': 
          value = buffer.length() == 0 ? "" : buffer.toString();
          buffer.setLength(0);
          if (key != null)
          {
            map.add(key, value);
          }
          else if ((value != null) && (value.length() > 0))
          {
            map.add(value, "");
          }
          key = null;
          value = null;
          if ((maxKeys > 0) && (map.size() > maxKeys)) {
            throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", new Object[] { Integer.valueOf(map.size()), Integer.valueOf(maxKeys) }));
          }
          break;
        case '=': 
          if (key != null)
          {
            buffer.append((char)b);
          }
          else {
            key = buffer.toString();
            buffer.setLength(0); }
          break;
        
        case '+': 
          buffer.append(' ');
          break;
        
        case '%': 
          int code0 = in.read();
          if (117 == code0)
          {
            int code1 = in.read();
            if (code1 >= 0)
            {
              int code2 = in.read();
              if (code2 >= 0)
              {
                int code3 = in.read();
                if (code3 >= 0) {
                  buffer.append(Character.toChars((TypeUtil.convertHexDigit(code0) << 12) + (TypeUtil.convertHexDigit(code1) << 8) + (TypeUtil.convertHexDigit(code2) << 4) + TypeUtil.convertHexDigit(code3)));
                }
              }
            }
          } else if (code0 >= 0)
          {
            int code1 = in.read();
            if (code1 >= 0)
              buffer.append((char)((TypeUtil.convertHexDigit(code0) << 4) + TypeUtil.convertHexDigit(code1))); }
          break;
        

        default: 
          buffer.append((char)b);
        }
        
        if (maxLength >= 0) { totalLength++; if (totalLength > maxLength)
            throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", new Object[] { Integer.valueOf(map.size()), Integer.valueOf(maxKeys) }));
        }
      }
      if (key != null)
      {
        value = buffer.length() == 0 ? "" : buffer.toString();
        buffer.setLength(0);
        map.add(key, value);
      }
      else if (buffer.length() > 0)
      {
        map.add(buffer.toString(), "");
      }
    }
  }
  








  public static void decodeUtf8To(InputStream in, MultiMap<String> map, int maxLength, int maxKeys)
    throws IOException
  {
    synchronized (map)
    {
      Utf8StringBuilder buffer = new Utf8StringBuilder();
      String key = null;
      String value = null;
      


      int totalLength = 0;
      int b; while ((b = in.read()) >= 0)
      {
        try
        {
          switch ((char)b)
          {
          case '&': 
            value = buffer.toReplacedString();
            buffer.reset();
            if (key != null)
            {
              map.add(key, value);
            }
            else if ((value != null) && (value.length() > 0))
            {
              map.add(value, "");
            }
            key = null;
            value = null;
            if ((maxKeys > 0) && (map.size() > maxKeys)) {
              throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", new Object[] { Integer.valueOf(map.size()), Integer.valueOf(maxKeys) }));
            }
            break;
          case '=': 
            if (key != null)
            {
              buffer.append((byte)b);
            }
            else {
              key = buffer.toReplacedString();
              buffer.reset(); }
            break;
          
          case '+': 
            buffer.append((byte)32);
            break;
          
          case '%': 
            int code0 = in.read();
            boolean decoded = false;
            if (117 == code0)
            {
              code0 = in.read();
              if (code0 >= 0)
              {
                int code1 = in.read();
                if (code1 >= 0)
                {
                  int code2 = in.read();
                  if (code2 >= 0)
                  {
                    int code3 = in.read();
                    if (code3 >= 0)
                    {
                      buffer.getStringBuilder().append(
                        Character.toChars((TypeUtil.convertHexDigit(code0) << 12) + (TypeUtil.convertHexDigit(code1) << 8) + (TypeUtil.convertHexDigit(code2) << 4) + TypeUtil.convertHexDigit(code3)));
                      decoded = true;
                    }
                  }
                }
              }
            }
            else if (code0 >= 0)
            {
              int code1 = in.read();
              if (code1 >= 0)
              {
                buffer.append((byte)((TypeUtil.convertHexDigit(code0) << 4) + TypeUtil.convertHexDigit(code1)));
                decoded = true;
              }
            }
            
            if (!decoded) {
              buffer.getStringBuilder().append(65533);
            }
            
            break;
          default: 
            buffer.append((byte)b);
          }
          
        }
        catch (Utf8Appendable.NotUtf8Exception e)
        {
          LOG.warn(e.toString(), new Object[0]);
          LOG.debug(e);
        }
        catch (NumberFormatException e)
        {
          buffer.append(Utf8Appendable.REPLACEMENT_UTF8, 0, 3);
          LOG.warn(e.toString(), new Object[0]);
          LOG.debug(e);
        }
        if (maxLength >= 0) { totalLength++; if (totalLength > maxLength)
            throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", new Object[] { Integer.valueOf(map.size()), Integer.valueOf(maxKeys) }));
        }
      }
      if (key != null)
      {
        value = buffer.toReplacedString();
        buffer.reset();
        map.add(key, value);
      }
      else if (buffer.length() > 0)
      {
        map.add(buffer.toReplacedString(), "");
      }
    }
  }
  
  public static void decodeUtf16To(InputStream in, MultiMap<String> map, int maxLength, int maxKeys)
    throws IOException
  {
    InputStreamReader input = new InputStreamReader(in, StandardCharsets.UTF_16);
    StringWriter buf = new StringWriter(8192);
    IO.copy(input, buf, maxLength);
    

    decodeTo(buf.getBuffer().toString(), map, StandardCharsets.UTF_16);
  }
  









  public static void decodeTo(InputStream in, MultiMap<String> map, String charset, int maxLength, int maxKeys)
    throws IOException
  {
    if (charset == null)
    {
      if (ENCODING.equals(StandardCharsets.UTF_8)) {
        decodeUtf8To(in, map, maxLength, maxKeys);
      } else {
        decodeTo(in, map, ENCODING, maxLength, maxKeys);
      }
    } else if ("utf-8".equalsIgnoreCase(charset)) {
      decodeUtf8To(in, map, maxLength, maxKeys);
    } else if ("iso-8859-1".equalsIgnoreCase(charset)) {
      decode88591To(in, map, maxLength, maxKeys);
    } else if ("utf-16".equalsIgnoreCase(charset)) {
      decodeUtf16To(in, map, maxLength, maxKeys);
    } else {
      decodeTo(in, map, Charset.forName(charset), maxLength, maxKeys);
    }
  }
  









  public static void decodeTo(InputStream in, MultiMap<String> map, Charset charset, int maxLength, int maxKeys)
    throws IOException
  {
    if (charset == null) {
      charset = ENCODING;
    }
    if (StandardCharsets.UTF_8.equals(charset))
    {
      decodeUtf8To(in, map, maxLength, maxKeys);
      return;
    }
    
    if (StandardCharsets.ISO_8859_1.equals(charset))
    {
      decode88591To(in, map, maxLength, maxKeys);
      return;
    }
    
    if (StandardCharsets.UTF_16.equals(charset))
    {
      decodeUtf16To(in, map, maxLength, maxKeys);
      return;
    }
    
    synchronized (map)
    {
      String key = null;
      String value = null;
      


      int totalLength = 0;
      
      ByteArrayOutputStream2 output = new ByteArrayOutputStream2();Throwable localThrowable3 = null;
      try {
        int size = 0;
        int c;
        while ((c = in.read()) > 0)
        {
          switch ((char)c)
          {
          case '&': 
            size = output.size();
            value = size == 0 ? "" : output.toString(charset);
            output.setCount(0);
            if (key != null)
            {
              map.add(key, value);
            }
            else if ((value != null) && (value.length() > 0))
            {
              map.add(value, "");
            }
            key = null;
            value = null;
            if ((maxKeys > 0) && (map.size() > maxKeys))
              throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", new Object[] { Integer.valueOf(map.size()), Integer.valueOf(maxKeys) }));
            break;
          case '=': 
            if (key != null)
            {
              output.write(c);
            }
            else {
              size = output.size();
              key = size == 0 ? "" : output.toString(charset);
              output.setCount(0); }
            break;
          case '+': 
            output.write(32);
            break;
          case '%': 
            int code0 = in.read();
            if (117 == code0)
            {
              int code1 = in.read();
              if (code1 >= 0)
              {
                int code2 = in.read();
                if (code2 >= 0)
                {
                  int code3 = in.read();
                  if (code3 >= 0) {
                    output.write(new String(Character.toChars((TypeUtil.convertHexDigit(code0) << 12) + (TypeUtil.convertHexDigit(code1) << 8) + (TypeUtil.convertHexDigit(code2) << 4) + TypeUtil.convertHexDigit(code3))).getBytes(charset));
                  }
                }
              }
            }
            else if (code0 >= 0)
            {
              int code1 = in.read();
              if (code1 >= 0)
                output.write((TypeUtil.convertHexDigit(code0) << 4) + TypeUtil.convertHexDigit(code1)); }
            break;
          
          default: 
            output.write(c);
          }
          
          
          totalLength++;
          if ((maxLength >= 0) && (totalLength > maxLength)) {
            throw new IllegalStateException(String.format("Form with too many keys [%d > %d]", new Object[] { Integer.valueOf(map.size()), Integer.valueOf(maxKeys) }));
          }
        }
        size = output.size();
        if (key != null)
        {
          value = size == 0 ? "" : output.toString(charset);
          output.setCount(0);
          map.add(key, value);
        }
        else if (size > 0) {
          map.add(output.toString(charset), "");
        }
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;







































      }
      finally
      {






































        if (output != null) { if (localThrowable3 != null) try { output.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { output.close();
          }
        }
      }
    }
  }
  




  public static String decodeString(String encoded)
  {
    return decodeString(encoded, 0, encoded.length(), ENCODING);
  }
  










  public static String decodeString(String encoded, int offset, int length, Charset charset)
  {
    if ((charset == null) || (StandardCharsets.UTF_8.equals(charset)))
    {
      Utf8StringBuffer buffer = null;
      
      for (int i = 0; i < length; i++)
      {
        char c = encoded.charAt(offset + i);
        if ((c < 0) || (c > 'ÿ'))
        {
          if (buffer == null)
          {
            buffer = new Utf8StringBuffer(length);
            buffer.getStringBuffer().append(encoded, offset, offset + i + 1);
          }
          else {
            buffer.getStringBuffer().append(c);
          }
        } else if (c == '+')
        {
          if (buffer == null)
          {
            buffer = new Utf8StringBuffer(length);
            buffer.getStringBuffer().append(encoded, offset, offset + i);
          }
          
          buffer.getStringBuffer().append(' ');
        }
        else if (c == '%')
        {
          if (buffer == null)
          {
            buffer = new Utf8StringBuffer(length);
            buffer.getStringBuffer().append(encoded, offset, offset + i);
          }
          
          if (i + 2 < length)
          {
            try
            {
              if ('u' == encoded.charAt(offset + i + 1))
              {
                if (i + 5 < length)
                {
                  int o = offset + i + 2;
                  i += 5;
                  String unicode = new String(Character.toChars(TypeUtil.parseInt(encoded, o, 4, 16)));
                  buffer.getStringBuffer().append(unicode);
                }
                else
                {
                  i = length;
                  buffer.getStringBuffer().append(65533);
                }
              }
              else
              {
                int o = offset + i + 1;
                i += 2;
                byte b = (byte)TypeUtil.parseInt(encoded, o, 2, 16);
                buffer.append(b);
              }
            }
            catch (Utf8Appendable.NotUtf8Exception e)
            {
              LOG.warn(e.toString(), new Object[0]);
              LOG.debug(e);
            }
            catch (NumberFormatException e)
            {
              LOG.warn(e.toString(), new Object[0]);
              LOG.debug(e);
              buffer.getStringBuffer().append(65533);
            }
          }
          else
          {
            buffer.getStringBuffer().append(65533);
            i = length;
          }
        }
        else if (buffer != null) {
          buffer.getStringBuffer().append(c);
        }
      }
      if (buffer == null)
      {
        if ((offset == 0) && (encoded.length() == length))
          return encoded;
        return encoded.substring(offset, offset + length);
      }
      
      return buffer.toReplacedString();
    }
    

    StringBuffer buffer = null;
    
    for (int i = 0; i < length; i++)
    {
      char c = encoded.charAt(offset + i);
      if ((c < 0) || (c > 'ÿ'))
      {
        if (buffer == null)
        {
          buffer = new StringBuffer(length);
          buffer.append(encoded, offset, offset + i + 1);
        }
        else {
          buffer.append(c);
        }
      } else if (c == '+')
      {
        if (buffer == null)
        {
          buffer = new StringBuffer(length);
          buffer.append(encoded, offset, offset + i);
        }
        
        buffer.append(' ');
      }
      else if (c == '%')
      {
        if (buffer == null)
        {
          buffer = new StringBuffer(length);
          buffer.append(encoded, offset, offset + i);
        }
        
        byte[] ba = new byte[length];
        int n = 0;
        while ((c >= 0) && (c <= 'ÿ'))
        {
          if (c == '%')
          {
            if (i + 2 < length)
            {
              try
              {
                if ('u' == encoded.charAt(offset + i + 1))
                {
                  if (i + 6 < length)
                  {
                    int o = offset + i + 2;
                    i += 6;
                    String unicode = new String(Character.toChars(TypeUtil.parseInt(encoded, o, 4, 16)));
                    byte[] reencoded = unicode.getBytes(charset);
                    System.arraycopy(reencoded, 0, ba, n, reencoded.length);
                    n += reencoded.length;
                  }
                  else
                  {
                    ba[(n++)] = 63;
                    i = length;
                  }
                }
                else
                {
                  int o = offset + i + 1;
                  i += 3;
                  ba[n] = ((byte)TypeUtil.parseInt(encoded, o, 2, 16));
                  n++;
                }
              }
              catch (Exception e)
              {
                LOG.warn(e.toString(), new Object[0]);
                LOG.debug(e);
                ba[(n++)] = 63;
              }
            }
            else
            {
              ba[(n++)] = 63;
              i = length;
            }
          }
          else if (c == '+')
          {
            ba[(n++)] = 32;
            i++;
          }
          else
          {
            ba[(n++)] = ((byte)c);
            i++;
          }
          
          if (i >= length)
            break;
          c = encoded.charAt(offset + i);
        }
        
        i--;
        buffer.append(new String(ba, 0, n, charset));

      }
      else if (buffer != null) {
        buffer.append(c);
      }
    }
    if (buffer == null)
    {
      if ((offset == 0) && (encoded.length() == length))
        return encoded;
      return encoded.substring(offset, offset + length);
    }
    
    return buffer.toString();
  }
  







  public static String encodeString(String string)
  {
    return encodeString(string, ENCODING);
  }
  






  public static String encodeString(String string, Charset charset)
  {
    if (charset == null)
      charset = ENCODING;
    byte[] bytes = null;
    bytes = string.getBytes(charset);
    
    int len = bytes.length;
    byte[] encoded = new byte[bytes.length * 3];
    int n = 0;
    boolean noEncode = true;
    
    for (int i = 0; i < len; i++)
    {
      byte b = bytes[i];
      
      if (b == 32)
      {
        noEncode = false;
        encoded[(n++)] = 43;
      }
      else if (((b >= 97) && (b <= 122)) || ((b >= 65) && (b <= 90)) || ((b >= 48) && (b <= 57)))
      {


        encoded[(n++)] = b;
      }
      else
      {
        noEncode = false;
        encoded[(n++)] = 37;
        byte nibble = (byte)((b & 0xF0) >> 4);
        if (nibble >= 10) {
          encoded[(n++)] = ((byte)(65 + nibble - 10));
        } else
          encoded[(n++)] = ((byte)(48 + nibble));
        nibble = (byte)(b & 0xF);
        if (nibble >= 10) {
          encoded[(n++)] = ((byte)(65 + nibble - 10));
        } else {
          encoded[(n++)] = ((byte)(48 + nibble));
        }
      }
    }
    if (noEncode) {
      return string;
    }
    return new String(encoded, 0, n, charset);
  }
  





  public Object clone()
  {
    return new UrlEncoded(this);
  }
  
  public UrlEncoded() {}
}
