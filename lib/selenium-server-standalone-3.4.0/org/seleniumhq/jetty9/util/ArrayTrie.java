package org.seleniumhq.jetty9.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;




















































public class ArrayTrie<V>
  extends AbstractTrie<V>
{
  private static final int ROW_SIZE = 32;
  private static final int[] __lookup = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, -1, 27, 30, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 28, 29, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1 };
  







  private final char[] _rowIndex;
  






  private final String[] _key;
  






  private final V[] _value;
  






  private char[][] _bigIndex;
  






  private char _rows;
  







  public ArrayTrie()
  {
    this(128);
  }
  










  public ArrayTrie(int capacity)
  {
    super(true);
    _value = ((Object[])new Object[capacity]);
    _rowIndex = new char[capacity * 32];
    _key = new String[capacity];
  }
  


  public void clear()
  {
    _rows = '\000';
    Arrays.fill(_value, null);
    Arrays.fill(_rowIndex, '\000');
    Arrays.fill(_key, null);
  }
  


  public boolean put(String s, V v)
  {
    int t = 0;
    
    int limit = s.length();
    for (int k = 0; k < limit; k++)
    {
      char c = s.charAt(k);
      
      int index = __lookup[(c & 0x7F)];
      if (index >= 0)
      {
        int idx = t * 32 + index;
        t = _rowIndex[idx];
        if (t == 0)
        {
          if ((this._rows = (char)(_rows + '\001')) >= _value.length)
            return false;
          t = _rowIndex[idx] = _rows;
        }
      } else {
        if (c > '') {
          throw new IllegalArgumentException("non ascii character");
        }
        
        if (_bigIndex == null)
          _bigIndex = new char[_value.length][];
        if (t >= _bigIndex.length)
          return false;
        char[] big = _bigIndex[t];
        if (big == null)
          big = _bigIndex[t] =  = new char[''];
        t = big[c];
        if (t == 0)
        {
          if (_rows == _value.length)
            return false;
          t = big[c] = this._rows = (char)(_rows + '\001');
        }
      }
    }
    
    if (t >= _key.length)
    {
      _rows = ((char)_key.length);
      return false;
    }
    
    _key[t] = (v == null ? null : s);
    _value[t] = v;
    return true;
  }
  


  public V get(String s, int offset, int len)
  {
    int t = 0;
    for (int i = 0; i < len; i++)
    {
      char c = s.charAt(offset + i);
      int index = __lookup[(c & 0x7F)];
      if (index >= 0)
      {
        int idx = t * 32 + index;
        t = _rowIndex[idx];
        if (t == 0) {
          return null;
        }
      }
      else {
        char[] big = _bigIndex == null ? null : _bigIndex[t];
        if (big == null)
          return null;
        t = big[c];
        if (t == 0)
          return null;
      }
    }
    return _value[t];
  }
  


  public V get(ByteBuffer b, int offset, int len)
  {
    int t = 0;
    for (int i = 0; i < len; i++)
    {
      byte c = b.get(offset + i);
      int index = __lookup[(c & 0x7F)];
      if (index >= 0)
      {
        int idx = t * 32 + index;
        t = _rowIndex[idx];
        if (t == 0) {
          return null;
        }
      }
      else {
        char[] big = _bigIndex == null ? null : _bigIndex[t];
        if (big == null)
          return null;
        t = big[c];
        if (t == 0)
          return null;
      }
    }
    return _value[t];
  }
  


  public V getBest(byte[] b, int offset, int len)
  {
    return getBest(0, b, offset, len);
  }
  


  public V getBest(ByteBuffer b, int offset, int len)
  {
    if (b.hasArray())
      return getBest(0, b.array(), b.arrayOffset() + b.position() + offset, len);
    return getBest(0, b, offset, len);
  }
  


  public V getBest(String s, int offset, int len)
  {
    return getBest(0, s, offset, len);
  }
  

  private V getBest(int t, String s, int offset, int len)
  {
    int pos = offset;
    for (int i = 0; i < len; i++)
    {
      char c = s.charAt(pos++);
      int index = __lookup[(c & 0x7F)];
      if (index >= 0)
      {
        int idx = t * 32 + index;
        int nt = _rowIndex[idx];
        if (nt == 0)
          break;
        t = nt;
      }
      else
      {
        char[] big = _bigIndex == null ? null : _bigIndex[t];
        if (big == null)
          return null;
        int nt = big[c];
        if (nt == 0)
          break;
        t = nt;
      }
      

      if (_key[t] != null)
      {

        V best = getBest(t, s, offset + i + 1, len - i - 1);
        if (best != null)
          return best;
        return _value[t];
      }
    }
    return _value[t];
  }
  

  private V getBest(int t, byte[] b, int offset, int len)
  {
    for (int i = 0; i < len; i++)
    {
      byte c = b[(offset + i)];
      int index = __lookup[(c & 0x7F)];
      if (index >= 0)
      {
        int idx = t * 32 + index;
        int nt = _rowIndex[idx];
        if (nt == 0)
          break;
        t = nt;
      }
      else
      {
        char[] big = _bigIndex == null ? null : _bigIndex[t];
        if (big == null)
          return null;
        int nt = big[c];
        if (nt == 0)
          break;
        t = nt;
      }
      

      if (_key[t] != null)
      {

        V best = getBest(t, b, offset + i + 1, len - i - 1);
        if (best == null) break;
        return best;
      }
    }
    
    return _value[t];
  }
  
  private V getBest(int t, ByteBuffer b, int offset, int len)
  {
    int pos = b.position() + offset;
    for (int i = 0; i < len; i++)
    {
      byte c = b.get(pos++);
      int index = __lookup[(c & 0x7F)];
      if (index >= 0)
      {
        int idx = t * 32 + index;
        int nt = _rowIndex[idx];
        if (nt == 0)
          break;
        t = nt;
      }
      else
      {
        char[] big = _bigIndex == null ? null : _bigIndex[t];
        if (big == null)
          return null;
        int nt = big[c];
        if (nt == 0)
          break;
        t = nt;
      }
      

      if (_key[t] != null)
      {

        V best = getBest(t, b, offset + i + 1, len - i - 1);
        if (best == null) break;
        return best;
      }
    }
    
    return _value[t];
  }
  




  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    toString(buf, 0);
    
    if (buf.length() == 0) {
      return "{}";
    }
    buf.setCharAt(0, '{');
    buf.append('}');
    return buf.toString();
  }
  

  private void toString(Appendable out, int t)
  {
    if (_value[t] != null)
    {
      try
      {
        out.append(',');
        out.append(_key[t]);
        out.append('=');
        out.append(_value[t].toString());
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }
    int idx;
    for (int i = 0; i < 32; i++)
    {
      idx = t * 32 + i;
      if (_rowIndex[idx] != 0) {
        toString(out, _rowIndex[idx]);
      }
    }
    char[] big = _bigIndex == null ? null : _bigIndex[t];
    if (big != null)
    {
      for (int i : big) {
        if (i != 0) {
          toString(out, i);
        }
      }
    }
  }
  
  public Set<String> keySet()
  {
    Set<String> keys = new HashSet();
    keySet(keys, 0);
    return keys;
  }
  
  private void keySet(Set<String> set, int t)
  {
    if ((t < _value.length) && (_value[t] != null))
      set.add(_key[t]);
    int idx;
    for (int i = 0; i < 32; i++)
    {
      idx = t * 32 + i;
      if ((idx < _rowIndex.length) && (_rowIndex[idx] != 0)) {
        keySet(set, _rowIndex[idx]);
      }
    }
    char[] big = (_bigIndex == null) || (t >= _bigIndex.length) ? null : _bigIndex[t];
    if (big != null)
    {
      for (int i : big) {
        if (i != 0) {
          keySet(set, i);
        }
      }
    }
  }
  
  public boolean isFull() {
    return _rows + '\001' >= _key.length;
  }
}
