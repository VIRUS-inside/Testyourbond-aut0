package org.eclipse.jetty.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;



























public abstract class AbstractTrie<V>
  implements Trie<V>
{
  final boolean _caseInsensitive;
  
  protected AbstractTrie(boolean insensitive)
  {
    _caseInsensitive = insensitive;
  }
  

  public boolean put(V v)
  {
    return put(v.toString(), v);
  }
  

  public V remove(String s)
  {
    V o = get(s);
    put(s, null);
    return o;
  }
  

  public V get(String s)
  {
    return get(s, 0, s.length());
  }
  

  public V get(ByteBuffer b)
  {
    return get(b, 0, b.remaining());
  }
  

  public V getBest(String s)
  {
    return getBest(s, 0, s.length());
  }
  

  public V getBest(byte[] b, int offset, int len)
  {
    return getBest(new String(b, offset, len, StandardCharsets.ISO_8859_1));
  }
  

  public boolean isCaseInsensitive()
  {
    return _caseInsensitive;
  }
}
