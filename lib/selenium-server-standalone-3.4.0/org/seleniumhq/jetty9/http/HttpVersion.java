package org.seleniumhq.jetty9.http;

import java.nio.ByteBuffer;
import org.seleniumhq.jetty9.util.ArrayTrie;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.Trie;






















public enum HttpVersion
{
  HTTP_0_9("HTTP/0.9", 9), 
  HTTP_1_0("HTTP/1.0", 10), 
  HTTP_1_1("HTTP/1.1", 11), 
  HTTP_2("HTTP/2.0", 20);
  
  static {
    CACHE = new ArrayTrie();
    

    for (HttpVersion version : values()) {
      CACHE.put(version.toString(), version);
    }
  }
  

  public static final Trie<HttpVersion> CACHE;
  
  private final String _string;
  private final byte[] _bytes;
  private final ByteBuffer _buffer;
  private final int _version;
  public static HttpVersion lookAheadGet(byte[] bytes, int position, int limit)
  {
    int length = limit - position;
    if (length < 9) {
      return null;
    }
    if ((bytes[(position + 4)] == 47) && (bytes[(position + 6)] == 46) && (Character.isWhitespace((char)bytes[(position + 8)])) && (((bytes[position] == 72) && (bytes[(position + 1)] == 84) && (bytes[(position + 2)] == 84) && (bytes[(position + 3)] == 80)) || ((bytes[position] == 104) && (bytes[(position + 1)] == 116) && (bytes[(position + 2)] == 116) && (bytes[(position + 3)] == 112))))
    {


      switch (bytes[(position + 5)])
      {
      case 49: 
        switch (bytes[(position + 7)])
        {
        case 48: 
          return HTTP_1_0;
        case 49: 
          return HTTP_1_1;
        }
        break;
      case 50: 
        switch (bytes[(position + 7)])
        {
        case 48: 
          return HTTP_2;
        }
        
        break;
      }
    }
    return null;
  }
  






  public static HttpVersion lookAheadGet(ByteBuffer buffer)
  {
    if (buffer.hasArray())
      return lookAheadGet(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.arrayOffset() + buffer.limit());
    return null;
  }
  







  private HttpVersion(String s, int version)
  {
    _string = s;
    _bytes = StringUtil.getBytes(s);
    _buffer = ByteBuffer.wrap(_bytes);
    _version = version;
  }
  

  public byte[] toBytes()
  {
    return _bytes;
  }
  

  public ByteBuffer toBuffer()
  {
    return _buffer.asReadOnlyBuffer();
  }
  

  public int getVersion()
  {
    return _version;
  }
  

  public boolean is(String s)
  {
    return _string.equalsIgnoreCase(s);
  }
  

  public String asString()
  {
    return _string;
  }
  


  public String toString()
  {
    return _string;
  }
  





  public static HttpVersion fromString(String version)
  {
    return (HttpVersion)CACHE.get(version);
  }
  

  public static HttpVersion fromVersion(int version)
  {
    switch (version) {
    case 9: 
      return HTTP_0_9;
    case 10:  return HTTP_1_0;
    case 11:  return HTTP_1_1;
    case 20:  return HTTP_2; }
    throw new IllegalArgumentException();
  }
}
