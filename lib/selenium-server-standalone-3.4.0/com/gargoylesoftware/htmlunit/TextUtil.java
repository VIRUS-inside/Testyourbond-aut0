package com.gargoylesoftware.htmlunit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;




























public final class TextUtil
{
  @Deprecated
  public static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;
  



  private TextUtil() {}
  


  public static InputStream toInputStream(String content)
  {
    return toInputStream(content, StandardCharsets.ISO_8859_1);
  }
  







  public static InputStream toInputStream(String content, Charset charset)
  {
    try
    {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(content.length() * 2);
      OutputStreamWriter writer = new OutputStreamWriter(byteArrayOutputStream, charset);
      writer.write(content);
      writer.flush();
      
      byte[] byteArray = byteArrayOutputStream.toByteArray();
      return new ByteArrayInputStream(byteArray);

    }
    catch (IOException e)
    {
      throw new IllegalStateException("Exception when converting a string to an input stream: '" + e + "'", e);
    }
  }
  






  public static byte[] stringToByteArray(String content, Charset charset)
  {
    if ((content == null) || (content.isEmpty()) || (charset == null)) {
      return new byte[0];
    }
    
    return content.getBytes(charset);
  }
}
