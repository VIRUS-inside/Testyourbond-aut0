package org.seleniumhq.jetty9.http;

import java.nio.ByteBuffer;
import org.seleniumhq.jetty9.util.ArrayTrie;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.Trie;
























public enum HttpMethod
{
  GET, 
  POST, 
  HEAD, 
  PUT, 
  OPTIONS, 
  DELETE, 
  TRACE, 
  CONNECT, 
  MOVE, 
  PROXY, 
  PRI;
  

  public static final Trie<HttpMethod> CACHE;
  
  private final ByteBuffer _buffer;
  
  private final byte[] _bytes;
  

  public static HttpMethod lookAheadGet(byte[] bytes, int position, int limit)
  {
    int length = limit - position;
    if (length < 4)
      return null;
    switch (bytes[position])
    {
    case 71: 
      if ((bytes[(position + 1)] == 69) && (bytes[(position + 2)] == 84) && (bytes[(position + 3)] == 32))
        return GET;
      break;
    case 80: 
      if ((bytes[(position + 1)] == 79) && (bytes[(position + 2)] == 83) && (bytes[(position + 3)] == 84) && (length >= 5) && (bytes[(position + 4)] == 32))
        return POST;
      if ((bytes[(position + 1)] == 82) && (bytes[(position + 2)] == 79) && (bytes[(position + 3)] == 88) && (length >= 6) && (bytes[(position + 4)] == 89) && (bytes[(position + 5)] == 32))
        return PROXY;
      if ((bytes[(position + 1)] == 85) && (bytes[(position + 2)] == 84) && (bytes[(position + 3)] == 32))
        return PUT;
      if ((bytes[(position + 1)] == 82) && (bytes[(position + 2)] == 73) && (bytes[(position + 3)] == 32))
        return PRI;
      break;
    case 72: 
      if ((bytes[(position + 1)] == 69) && (bytes[(position + 2)] == 65) && (bytes[(position + 3)] == 68) && (length >= 5) && (bytes[(position + 4)] == 32))
        return HEAD;
      break;
    case 79: 
      if ((bytes[(position + 1)] == 80) && (bytes[(position + 2)] == 84) && (bytes[(position + 3)] == 73) && (length >= 8) && (bytes[(position + 4)] == 79) && (bytes[(position + 5)] == 78) && (bytes[(position + 6)] == 83) && (bytes[(position + 7)] == 32))
      {
        return OPTIONS; }
      break;
    case 68: 
      if ((bytes[(position + 1)] == 69) && (bytes[(position + 2)] == 76) && (bytes[(position + 3)] == 69) && (length >= 7) && (bytes[(position + 4)] == 84) && (bytes[(position + 5)] == 69) && (bytes[(position + 6)] == 32))
      {
        return DELETE; }
      break;
    case 84: 
      if ((bytes[(position + 1)] == 82) && (bytes[(position + 2)] == 65) && (bytes[(position + 3)] == 67) && (length >= 6) && (bytes[(position + 4)] == 69) && (bytes[(position + 5)] == 32))
      {
        return TRACE; }
      break;
    case 67: 
      if ((bytes[(position + 1)] == 79) && (bytes[(position + 2)] == 78) && (bytes[(position + 3)] == 78) && (length >= 8) && (bytes[(position + 4)] == 69) && (bytes[(position + 5)] == 67) && (bytes[(position + 6)] == 84) && (bytes[(position + 7)] == 32))
      {
        return CONNECT; }
      break;
    case 77: 
      if ((bytes[(position + 1)] == 79) && (bytes[(position + 2)] == 86) && (bytes[(position + 3)] == 69) && (length >= 5) && (bytes[(position + 4)] == 32)) {
        return MOVE;
      }
      
      break;
    }
    
    return null;
  }
  






  public static HttpMethod lookAheadGet(ByteBuffer buffer)
  {
    if (buffer.hasArray()) {
      return lookAheadGet(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.arrayOffset() + buffer.limit());
    }
    int l = buffer.remaining();
    if (l >= 4)
    {
      HttpMethod m = (HttpMethod)CACHE.getBest(buffer, 0, l);
      if (m != null)
      {
        int ml = m.asString().length();
        if ((l > ml) && (buffer.get(buffer.position() + ml) == 32))
          return m;
      }
    }
    return null;
  }
  
  static {
    CACHE = new ArrayTrie();
    

    for (HttpMethod method : values()) {
      CACHE.put(method.toString(), method);
    }
  }
  




  private HttpMethod()
  {
    _bytes = StringUtil.getBytes(toString());
    _buffer = ByteBuffer.wrap(_bytes);
  }
  

  public byte[] getBytes()
  {
    return _bytes;
  }
  

  public boolean is(String s)
  {
    return toString().equalsIgnoreCase(s);
  }
  

  public ByteBuffer asBuffer()
  {
    return _buffer.asReadOnlyBuffer();
  }
  

  public String asString()
  {
    return toString();
  }
  






  public static HttpMethod fromString(String method)
  {
    return (HttpMethod)CACHE.get(method);
  }
}
