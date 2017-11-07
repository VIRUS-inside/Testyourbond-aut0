package org.eclipse.jetty.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


































public class TreeTrie<V>
  extends AbstractTrie<V>
{
  private static final int[] __lookup = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, -1, 27, 30, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 28, 29, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1 };
  



  private static final int INDEX = 32;
  


  private final TreeTrie<V>[] _nextIndex;
  


  private final List<TreeTrie<V>> _nextOther = new ArrayList();
  private final char _c;
  private String _key;
  private V _value;
  
  public TreeTrie()
  {
    super(true);
    _nextIndex = new TreeTrie[32];
    _c = '\000';
  }
  
  private TreeTrie(char c)
  {
    super(true);
    _nextIndex = new TreeTrie[32];
    _c = c;
  }
  

  public void clear()
  {
    Arrays.fill(_nextIndex, null);
    _nextOther.clear();
    _key = null;
    _value = null;
  }
  

  public boolean put(String s, V v)
  {
    TreeTrie<V> t = this;
    int limit = s.length();
    for (int k = 0; k < limit; k++)
    {
      char c = s.charAt(k);
      
      int index = (c >= 0) && (c < '') ? __lookup[c] : -1;
      if (index >= 0)
      {
        if (_nextIndex[index] == null)
          _nextIndex[index] = new TreeTrie(c);
        t = _nextIndex[index];
      }
      else
      {
        TreeTrie<V> n = null;
        for (int i = _nextOther.size(); i-- > 0;)
        {
          n = (TreeTrie)_nextOther.get(i);
          if (_c == c)
            break;
          n = null;
        }
        if (n == null)
        {
          n = new TreeTrie(c);
          _nextOther.add(n);
        }
        t = n;
      }
    }
    _key = (v == null ? null : s);
    _value = v;
    return true;
  }
  

  public V get(String s, int offset, int len)
  {
    TreeTrie<V> t = this;
    for (int i = 0; i < len; i++)
    {
      char c = s.charAt(offset + i);
      int index = (c >= 0) && (c < '') ? __lookup[c] : -1;
      if (index >= 0)
      {
        if (_nextIndex[index] == null)
          return null;
        t = _nextIndex[index];
      }
      else
      {
        TreeTrie<V> n = null;
        for (int j = _nextOther.size(); j-- > 0;)
        {
          n = (TreeTrie)_nextOther.get(j);
          if (_c == c)
            break;
          n = null;
        }
        if (n == null)
          return null;
        t = n;
      }
    }
    return _value;
  }
  

  public V get(ByteBuffer b, int offset, int len)
  {
    TreeTrie<V> t = this;
    for (int i = 0; i < len; i++)
    {
      byte c = b.get(offset + i);
      int index = (c >= 0) && (c < Byte.MAX_VALUE) ? __lookup[c] : -1;
      if (index >= 0)
      {
        if (_nextIndex[index] == null)
          return null;
        t = _nextIndex[index];
      }
      else
      {
        TreeTrie<V> n = null;
        for (int j = _nextOther.size(); j-- > 0;)
        {
          n = (TreeTrie)_nextOther.get(j);
          if (_c == c)
            break;
          n = null;
        }
        if (n == null)
          return null;
        t = n;
      }
    }
    return _value;
  }
  

  public V getBest(byte[] b, int offset, int len)
  {
    TreeTrie<V> t = this;
    for (int i = 0; i < len; i++)
    {
      byte c = b[(offset + i)];
      int index = (c >= 0) && (c < Byte.MAX_VALUE) ? __lookup[c] : -1;
      if (index >= 0)
      {
        if (_nextIndex[index] == null)
          break;
        t = _nextIndex[index];
      }
      else
      {
        TreeTrie<V> n = null;
        for (int j = _nextOther.size(); j-- > 0;)
        {
          n = (TreeTrie)_nextOther.get(j);
          if (_c == c)
            break;
          n = null;
        }
        if (n == null)
          break;
        t = n;
      }
      

      if (_key != null)
      {

        V best = t.getBest(b, offset + i + 1, len - i - 1);
        if (best == null) break;
        return best;
      }
    }
    
    return _value;
  }
  

  public V getBest(String s, int offset, int len)
  {
    TreeTrie<V> t = this;
    for (int i = 0; i < len; i++)
    {
      byte c = (byte)(0xFF & s.charAt(offset + i));
      int index = (c >= 0) && (c < Byte.MAX_VALUE) ? __lookup[c] : -1;
      if (index >= 0)
      {
        if (_nextIndex[index] == null)
          break;
        t = _nextIndex[index];
      }
      else
      {
        TreeTrie<V> n = null;
        for (int j = _nextOther.size(); j-- > 0;)
        {
          n = (TreeTrie)_nextOther.get(j);
          if (_c == c)
            break;
          n = null;
        }
        if (n == null)
          break;
        t = n;
      }
      

      if (_key != null)
      {

        V best = t.getBest(s, offset + i + 1, len - i - 1);
        if (best == null) break;
        return best;
      }
    }
    
    return _value;
  }
  

  public V getBest(ByteBuffer b, int offset, int len)
  {
    if (b.hasArray())
      return getBest(b.array(), b.arrayOffset() + b.position() + offset, len);
    return getBestByteBuffer(b, offset, len);
  }
  
  private V getBestByteBuffer(ByteBuffer b, int offset, int len)
  {
    TreeTrie<V> t = this;
    int pos = b.position() + offset;
    for (int i = 0; i < len; i++)
    {
      byte c = b.get(pos++);
      int index = (c >= 0) && (c < Byte.MAX_VALUE) ? __lookup[c] : -1;
      if (index >= 0)
      {
        if (_nextIndex[index] == null)
          break;
        t = _nextIndex[index];
      }
      else
      {
        TreeTrie<V> n = null;
        for (int j = _nextOther.size(); j-- > 0;)
        {
          n = (TreeTrie)_nextOther.get(j);
          if (_c == c)
            break;
          n = null;
        }
        if (n == null)
          break;
        t = n;
      }
      

      if (_key != null)
      {

        V best = t.getBest(b, offset + i + 1, len - i - 1);
        if (best == null) break;
        return best;
      }
    }
    
    return _value;
  }
  


  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    toString(buf, this);
    
    if (buf.length() == 0) {
      return "{}";
    }
    buf.setCharAt(0, '{');
    buf.append('}');
    return buf.toString();
  }
  
  private static <V> void toString(Appendable out, TreeTrie<V> t) {
    int i;
    if (t != null)
    {
      if (_value != null)
      {
        try
        {
          out.append(',');
          out.append(_key);
          out.append('=');
          out.append(_value.toString());
        }
        catch (IOException e)
        {
          throw new RuntimeException(e);
        }
      }
      
      for (int i = 0; i < 32; i++)
      {
        if (_nextIndex[i] != null)
          toString(out, _nextIndex[i]);
      }
      for (i = _nextOther.size(); i-- > 0;) {
        toString(out, (TreeTrie)_nextOther.get(i));
      }
    }
  }
  
  public Set<String> keySet()
  {
    Set<String> keys = new HashSet();
    keySet(keys, this);
    return keys;
  }
  
  private static <V> void keySet(Set<String> set, TreeTrie<V> t) {
    int i;
    if (t != null)
    {
      if (_key != null) {
        set.add(_key);
      }
      for (int i = 0; i < 32; i++)
      {
        if (_nextIndex[i] != null)
          keySet(set, _nextIndex[i]);
      }
      for (i = _nextOther.size(); i-- > 0;) {
        keySet(set, (TreeTrie)_nextOther.get(i));
      }
    }
  }
  
  public boolean isFull()
  {
    return false;
  }
}
