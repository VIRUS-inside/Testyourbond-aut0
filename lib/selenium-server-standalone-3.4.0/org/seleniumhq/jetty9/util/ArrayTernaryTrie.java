package org.seleniumhq.jetty9.util;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;


















































public class ArrayTernaryTrie<V>
  extends AbstractTrie<V>
{
  private static int LO = 1;
  private static int EQ = 2;
  private static int HI = 3;
  




  private static final int ROW_SIZE = 4;
  




  private final char[] _tree;
  




  private final String[] _key;
  




  private final V[] _value;
  



  private char _rows;
  




  public ArrayTernaryTrie()
  {
    this(128);
  }
  




  public ArrayTernaryTrie(boolean insensitive)
  {
    this(insensitive, 128);
  }
  









  public ArrayTernaryTrie(int capacity)
  {
    this(true, capacity);
  }
  










  public ArrayTernaryTrie(boolean insensitive, int capacity)
  {
    super(insensitive);
    _value = ((Object[])new Object[capacity]);
    _tree = new char[capacity * 4];
    _key = new String[capacity];
  }
  





  public ArrayTernaryTrie(ArrayTernaryTrie<V> trie, double factor)
  {
    super(trie.isCaseInsensitive());
    int capacity = (int)(_value.length * factor);
    _rows = _rows;
    _value = Arrays.copyOf(_value, capacity);
    _tree = Arrays.copyOf(_tree, capacity * 4);
    _key = ((String[])Arrays.copyOf(_key, capacity));
  }
  


  public void clear()
  {
    _rows = '\000';
    Arrays.fill(_value, null);
    Arrays.fill(_tree, '\000');
    Arrays.fill(_key, null);
  }
  


  public boolean put(String s, V v)
  {
    int t = 0;
    int limit = s.length();
    int last = 0;
    for (int k = 0; k < limit; k++)
    {
      char c = s.charAt(k);
      if ((isCaseInsensitive()) && (c < '')) {
        c = StringUtil.lowercases[c];
      }
      for (;;)
      {
        int row = 4 * t;
        

        if (t == _rows)
        {
          _rows = ((char)(_rows + '\001'));
          if (_rows >= _key.length)
          {
            _rows = ((char)(_rows - '\001'));
            return false;
          }
          _tree[row] = c;
        }
        
        char n = _tree[row];
        int diff = n - c;
        if (diff == 0) {
          t = _tree[(last = row + EQ)];
        } else if (diff < 0) {
          t = _tree[(last = row + LO)];
        } else {
          t = _tree[(last = row + HI)];
        }
        
        if (t == 0)
        {
          t = _rows;
          _tree[last] = ((char)t);
        }
        
        if (diff == 0) {
          break;
        }
      }
    }
    
    if (t == _rows)
    {
      _rows = ((char)(_rows + '\001'));
      if (_rows >= _key.length)
      {
        _rows = ((char)(_rows - '\001'));
        return false;
      }
    }
    

    _key[t] = (v == null ? null : s);
    _value[t] = v;
    
    return true;
  }
  



  public V get(String s, int offset, int len)
  {
    int t = 0;
    for (int i = 0; i < len;)
    {
      char c = s.charAt(offset + i++);
      if ((isCaseInsensitive()) && (c < '')) {
        c = StringUtil.lowercases[c];
      }
      for (;;)
      {
        int row = 4 * t;
        char n = _tree[row];
        int diff = n - c;
        
        if (diff == 0)
        {
          t = _tree[(row + EQ)];
          if (t != 0) break;
          return null;
        }
        

        t = _tree[(row + hilo(diff))];
        if (t == 0) {
          return null;
        }
      }
    }
    return _value[t];
  }
  


  public V get(ByteBuffer b, int offset, int len)
  {
    int t = 0;
    offset += b.position();
    
    for (int i = 0; i < len;)
    {
      byte c = (byte)(b.get(offset + i++) & 0x7F);
      if (isCaseInsensitive()) {
        c = (byte)StringUtil.lowercases[c];
      }
      for (;;)
      {
        int row = 4 * t;
        char n = _tree[row];
        int diff = n - c;
        
        if (diff == 0)
        {
          t = _tree[(row + EQ)];
          if (t != 0) break;
          return null;
        }
        

        t = _tree[(row + hilo(diff))];
        if (t == 0) {
          return null;
        }
      }
    }
    return _value[t];
  }
  


  public V getBest(String s)
  {
    return getBest(0, s, 0, s.length());
  }
  


  public V getBest(String s, int offset, int length)
  {
    return getBest(0, s, offset, length);
  }
  

  private V getBest(int t, String s, int offset, int len)
  {
    int node = t;
    int end = offset + len;
    while (offset < end)
    {
      char c = s.charAt(offset++);
      len--;
      if ((isCaseInsensitive()) && (c < '')) {
        c = StringUtil.lowercases[c];
      }
      for (;;)
      {
        int row = 4 * t;
        char n = _tree[row];
        int diff = n - c;
        
        if (diff == 0)
        {
          t = _tree[(row + EQ)];
          if (t == 0) {
            break label157;
          }
          
          if (_key[t] == null)
            break;
          node = t;
          V better = getBest(t, s, offset, len);
          if (better != null)
            return better;
          break;
        }
        

        t = _tree[(row + hilo(diff))];
        if (t == 0) break label157;
      }
    }
    label157:
    return _value[node];
  }
  



  public V getBest(ByteBuffer b, int offset, int len)
  {
    if (b.hasArray())
      return getBest(0, b.array(), b.arrayOffset() + b.position() + offset, len);
    return getBest(0, b, offset, len);
  }
  

  private V getBest(int t, byte[] b, int offset, int len)
  {
    int node = t;
    int end = offset + len;
    while (offset < end)
    {
      byte c = (byte)(b[(offset++)] & 0x7F);
      len--;
      if (isCaseInsensitive()) {
        c = (byte)StringUtil.lowercases[c];
      }
      for (;;)
      {
        int row = 4 * t;
        char n = _tree[row];
        int diff = n - c;
        
        if (diff == 0)
        {
          t = _tree[(row + EQ)];
          if (t == 0) {
            break label152;
          }
          
          if (_key[t] == null)
            break;
          node = t;
          V better = getBest(t, b, offset, len);
          if (better != null)
            return better;
          break;
        }
        

        t = _tree[(row + hilo(diff))];
        if (t == 0) break label152;
      }
    }
    label152:
    return _value[node];
  }
  

  private V getBest(int t, ByteBuffer b, int offset, int len)
  {
    int node = t;
    int o = offset + b.position();
    
    for (int i = 0; i < len; i++)
    {
      byte c = (byte)(b.get(o + i) & 0x7F);
      if (isCaseInsensitive()) {
        c = (byte)StringUtil.lowercases[c];
      }
      for (;;)
      {
        int row = 4 * t;
        char n = _tree[row];
        int diff = n - c;
        
        if (diff == 0)
        {
          t = _tree[(row + EQ)];
          if (t == 0) {
            break label171;
          }
          
          if (_key[t] == null)
            break;
          node = t;
          V best = getBest(t, b, offset + i + 1, len - i - 1);
          if (best != null)
            return best;
          break;
        }
        

        t = _tree[(row + hilo(diff))];
        if (t == 0) break label171;
      }
    }
    label171:
    return _value[node];
  }
  

  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    for (int r = 0; r <= _rows; r++)
    {
      if ((_key[r] != null) && (_value[r] != null))
      {
        buf.append(',');
        buf.append(_key[r]);
        buf.append('=');
        buf.append(_value[r].toString());
      }
    }
    if (buf.length() == 0) {
      return "{}";
    }
    buf.setCharAt(0, '{');
    buf.append('}');
    return buf.toString();
  }
  



  public Set<String> keySet()
  {
    Set<String> keys = new HashSet();
    
    for (int r = 0; r <= _rows; r++)
    {
      if ((_key[r] != null) && (_value[r] != null))
        keys.add(_key[r]);
    }
    return keys;
  }
  
  public int size()
  {
    int s = 0;
    for (int r = 0; r <= _rows; r++)
    {
      if ((_key[r] != null) && (_value[r] != null))
        s++;
    }
    return s;
  }
  
  public boolean isEmpty()
  {
    for (int r = 0; r <= _rows; r++)
    {
      if ((_key[r] != null) && (_value[r] != null))
        return false;
    }
    return true;
  }
  

  public Set<Map.Entry<String, V>> entrySet()
  {
    Set<Map.Entry<String, V>> entries = new HashSet();
    for (int r = 0; r <= _rows; r++)
    {
      if ((_key[r] != null) && (_value[r] != null))
        entries.add(new AbstractMap.SimpleEntry(_key[r], _value[r]));
    }
    return entries;
  }
  

  public boolean isFull()
  {
    return _rows + '\001' == _key.length;
  }
  


  public static int hilo(int diff)
  {
    return 1 + (diff | 0x7FFFFFFF) / 1073741823;
  }
  
  public void dump()
  {
    for (int r = 0; r < _rows; r++)
    {
      char c = _tree[(r * 4 + 0)];
      System.err.printf("%4d [%s,%d,%d,%d] '%s':%s%n", new Object[] {
        Integer.valueOf(r), "'" + c + "'", 
        
        Integer.valueOf(_tree[(r * 4 + LO)]), 
        Integer.valueOf(_tree[(r * 4 + EQ)]), 
        Integer.valueOf(_tree[(r * 4 + HI)]), _key[r], _value[r] });
    }
  }
  

  public static class Growing<V>
    implements Trie<V>
  {
    private final int _growby;
    
    private ArrayTernaryTrie<V> _trie;
    
    public Growing()
    {
      this(1024, 1024);
    }
    
    public Growing(int capacity, int growby)
    {
      _growby = growby;
      _trie = new ArrayTernaryTrie(capacity);
    }
    
    public Growing(boolean insensitive, int capacity, int growby)
    {
      _growby = growby;
      _trie = new ArrayTernaryTrie(insensitive, capacity);
    }
    
    public boolean put(V v)
    {
      return put(v.toString(), v);
    }
    
    public int hashCode()
    {
      return _trie.hashCode();
    }
    
    public V remove(String s)
    {
      return _trie.remove(s);
    }
    
    public V get(String s)
    {
      return _trie.get(s);
    }
    
    public V get(ByteBuffer b)
    {
      return _trie.get(b);
    }
    
    public V getBest(byte[] b, int offset, int len)
    {
      return _trie.getBest(b, offset, len);
    }
    
    public boolean isCaseInsensitive()
    {
      return _trie.isCaseInsensitive();
    }
    
    public boolean equals(Object obj)
    {
      return _trie.equals(obj);
    }
    
    public void clear()
    {
      _trie.clear();
    }
    
    public boolean put(String s, V v)
    {
      boolean added = _trie.put(s, v);
      while (!added)
      {
        ArrayTernaryTrie<V> bigger = new ArrayTernaryTrie(_trie._key.length + _growby);
        for (Map.Entry<String, V> entry : _trie.entrySet())
          bigger.put((String)entry.getKey(), entry.getValue());
        added = _trie.put(s, v);
      }
      
      return true;
    }
    
    public V get(String s, int offset, int len)
    {
      return _trie.get(s, offset, len);
    }
    
    public V get(ByteBuffer b, int offset, int len)
    {
      return _trie.get(b, offset, len);
    }
    
    public V getBest(String s)
    {
      return _trie.getBest(s);
    }
    
    public V getBest(String s, int offset, int length)
    {
      return _trie.getBest(s, offset, length);
    }
    
    public V getBest(ByteBuffer b, int offset, int len)
    {
      return _trie.getBest(b, offset, len);
    }
    
    public String toString()
    {
      return _trie.toString();
    }
    
    public Set<String> keySet()
    {
      return _trie.keySet();
    }
    
    public boolean isFull()
    {
      return false;
    }
    
    public void dump()
    {
      _trie.dump();
    }
    
    public boolean isEmpty()
    {
      return _trie.isEmpty();
    }
    
    public int size()
    {
      return _trie.size();
    }
  }
}
