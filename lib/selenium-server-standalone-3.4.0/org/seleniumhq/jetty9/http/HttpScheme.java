package org.seleniumhq.jetty9.http;

import java.nio.ByteBuffer;
import org.seleniumhq.jetty9.util.ArrayTrie;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Trie;























public enum HttpScheme
{
  HTTP("http"), 
  HTTPS("https"), 
  WS("ws"), 
  WSS("wss");
  
  static {
    CACHE = new ArrayTrie();
    

    for (HttpScheme version : values()) {
      CACHE.put(version.asString(), version);
    }
  }
  
  public static final Trie<HttpScheme> CACHE;
  private final String _string;
  private final ByteBuffer _buffer;
  private HttpScheme(String s)
  {
    _string = s;
    _buffer = BufferUtil.toBuffer(s);
  }
  

  public ByteBuffer asByteBuffer()
  {
    return _buffer.asReadOnlyBuffer();
  }
  

  public boolean is(String s)
  {
    return (s != null) && (_string.equalsIgnoreCase(s));
  }
  
  public String asString()
  {
    return _string;
  }
  


  public String toString()
  {
    return _string;
  }
}
