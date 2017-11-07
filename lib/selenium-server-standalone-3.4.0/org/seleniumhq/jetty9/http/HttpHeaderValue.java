package org.seleniumhq.jetty9.http;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import org.seleniumhq.jetty9.util.ArrayTrie;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.Trie;
























public enum HttpHeaderValue
{
  CLOSE("close"), 
  CHUNKED("chunked"), 
  GZIP("gzip"), 
  IDENTITY("identity"), 
  KEEP_ALIVE("keep-alive"), 
  CONTINUE("100-continue"), 
  PROCESSING("102-processing"), 
  TE("TE"), 
  BYTES("bytes"), 
  NO_CACHE("no-cache"), 
  UPGRADE("Upgrade"), 
  UNKNOWN("::UNKNOWN::");
  
  static {
    CACHE = new ArrayTrie();
    

    for (HttpHeaderValue value : values()) {
      if (value != UNKNOWN) {
        CACHE.put(value.toString(), value);
      }
    }
  }
  
  public static final Trie<HttpHeaderValue> CACHE;
  private final String _string;
  private final ByteBuffer _buffer;
  private HttpHeaderValue(String s) {
    _string = s;
    _buffer = BufferUtil.toBuffer(s);
  }
  

  public ByteBuffer toBuffer()
  {
    return _buffer.asReadOnlyBuffer();
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
  


  private static EnumSet<HttpHeader> __known = EnumSet.of(HttpHeader.CONNECTION, HttpHeader.TRANSFER_ENCODING, HttpHeader.CONTENT_ENCODING);
  



  public static boolean hasKnownValues(HttpHeader header)
  {
    if (header == null)
      return false;
    return __known.contains(header);
  }
}
