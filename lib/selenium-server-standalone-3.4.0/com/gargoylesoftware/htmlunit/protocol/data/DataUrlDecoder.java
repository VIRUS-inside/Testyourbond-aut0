package com.gargoylesoftware.htmlunit.protocol.data;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;























public class DataUrlDecoder
{
  private static final Charset DEFAULT_CHARSET = StandardCharsets.US_ASCII;
  
  private static final String DEFAULT_MEDIA_TYPE = "text/plain";
  
  private final String mediaType_;
  
  private final Charset charset_;
  
  private byte[] content_;
  

  protected DataUrlDecoder(byte[] data, String mediaType, Charset charset)
  {
    content_ = data;
    mediaType_ = mediaType;
    charset_ = charset;
  }
  






  public static DataUrlDecoder decode(URL url)
    throws UnsupportedEncodingException, DecoderException
  {
    return decodeDataURL(url.toExternalForm());
  }
  







  public static DataUrlDecoder decodeDataURL(String url)
    throws UnsupportedEncodingException, DecoderException
  {
    if (!url.startsWith("data")) {
      throw new IllegalArgumentException("Not a data url: " + url);
    }
    int comma = url.indexOf(',');
    String beforeData = url.substring("data:".length(), comma);
    
    boolean base64 = beforeData.endsWith(";base64");
    if (base64) {
      beforeData = beforeData.substring(0, beforeData.length() - 7);
    }
    String mediaType = extractMediaType(beforeData);
    Charset charset = extractCharset(beforeData);
    
    byte[] data = url.substring(comma + 1).getBytes(charset);
    if (base64) {
      data = Base64.decodeBase64(decodeUrl(data));
    }
    else {
      data = URLCodec.decodeUrl(data);
    }
    
    return new DataUrlDecoder(data, mediaType, charset);
  }
  
  private static Charset extractCharset(String beforeData) {
    if (beforeData.contains(";")) {
      String charsetName = StringUtils.substringAfter(beforeData, ";");
      charsetName = charsetName.trim();
      if (charsetName.startsWith("charset=")) {
        charsetName = charsetName.substring(8);
      }
      try {
        return Charset.forName(charsetName);
      }
      catch (UnsupportedCharsetException|IllegalCharsetNameException e) {
        return DEFAULT_CHARSET;
      }
    }
    return DEFAULT_CHARSET;
  }
  
  private static String extractMediaType(String beforeData) {
    if (beforeData.contains("/")) {
      if (beforeData.contains(";")) {
        return StringUtils.substringBefore(beforeData, ";");
      }
      return beforeData;
    }
    return "text/plain";
  }
  



  public String getMediaType()
  {
    return mediaType_;
  }
  



  public String getCharset()
  {
    return charset_.name();
  }
  



  public byte[] getBytes()
  {
    return content_;
  }
  




  public String getDataAsString()
    throws UnsupportedEncodingException
  {
    return new String(content_, charset_);
  }
  
  private static byte[] decodeUrl(byte[] bytes) throws DecoderException
  {
    if (bytes == null) {
      return null;
    }
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    for (int i = 0; i < bytes.length; i++) {
      int b = bytes[i];
      if (b == 37) {
        try {
          int u = digit16(bytes[(++i)]);
          int l = digit16(bytes[(++i)]);
          buffer.write((char)((u << 4) + l));
        }
        catch (ArrayIndexOutOfBoundsException e) {
          throw new DecoderException("Invalid URL encoding: ", e);
        }
        
      } else {
        buffer.write(b);
      }
    }
    return buffer.toByteArray();
  }
  
  private static int digit16(byte b) throws DecoderException {
    int i = Character.digit((char)b, 16);
    if (i == -1) {
      throw new DecoderException("Invalid URL encoding: not a valid digit (radix 16): " + b);
    }
    return i;
  }
}
