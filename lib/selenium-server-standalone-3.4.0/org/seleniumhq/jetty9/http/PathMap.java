package org.seleniumhq.jetty9.http;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Predicate;
import org.seleniumhq.jetty9.util.ArrayTernaryTrie;
import org.seleniumhq.jetty9.util.Trie;






























































@Deprecated
public class PathMap<O>
  extends HashMap<String, O>
{
  private static String __pathSpecSeparators = ":,";
  







  public static void setPathSpecSeparators(String s)
  {
    __pathSpecSeparators = s;
  }
  

  Trie<MappedEntry<O>> _prefixMap = new ArrayTernaryTrie(false);
  Trie<MappedEntry<O>> _suffixMap = new ArrayTernaryTrie(false);
  final Map<String, MappedEntry<O>> _exactMap = new HashMap();
  
  List<MappedEntry<O>> _defaultSingletonList = null;
  MappedEntry<O> _prefixDefault = null;
  MappedEntry<O> _default = null;
  boolean _nodefault = false;
  

  public PathMap()
  {
    this(11);
  }
  

  public PathMap(boolean noDefault)
  {
    this(11, noDefault);
  }
  

  public PathMap(int capacity)
  {
    this(capacity, false);
  }
  

  private PathMap(int capacity, boolean noDefault)
  {
    super(capacity);
    _nodefault = noDefault;
  }
  





  public PathMap(Map<String, ? extends O> dictMap)
  {
    putAll(dictMap);
  }
  







  public O put(String pathSpec, O object)
  {
    if ("".equals(pathSpec.trim()))
    {
      MappedEntry<O> entry = new MappedEntry("", object);
      entry.setMapped("");
      _exactMap.put("", entry);
      return super.put("", object);
    }
    
    StringTokenizer tok = new StringTokenizer(pathSpec, __pathSpecSeparators);
    O old = null;
    
    while (tok.hasMoreTokens())
    {
      String spec = tok.nextToken();
      
      if ((!spec.startsWith("/")) && (!spec.startsWith("*."))) {
        throw new IllegalArgumentException("PathSpec " + spec + ". must start with '/' or '*.'");
      }
      old = super.put(spec, object);
      

      MappedEntry<O> entry = new MappedEntry(spec, object);
      
      if (entry.getKey().equals(spec))
      {
        if (spec.equals("/*")) {
          _prefixDefault = entry;
        } else if (spec.endsWith("/*"))
        {
          String mapped = spec.substring(0, spec.length() - 2);
          entry.setMapped(mapped);
          while (!_prefixMap.put(mapped, entry)) {
            _prefixMap = new ArrayTernaryTrie((ArrayTernaryTrie)_prefixMap, 1.5D);
          }
        } else if (spec.startsWith("*."))
        {
          String suffix = spec.substring(2);
          while (!_suffixMap.put(suffix, entry)) {
            _suffixMap = new ArrayTernaryTrie((ArrayTernaryTrie)_suffixMap, 1.5D);
          }
        } else if (spec.equals("/"))
        {
          if (_nodefault) {
            _exactMap.put(spec, entry);
          }
          else {
            _default = entry;
            _defaultSingletonList = Collections.singletonList(_default);
          }
        }
        else
        {
          entry.setMapped(spec);
          _exactMap.put(spec, entry);
        }
      }
    }
    
    return old;
  }
  





  public O match(String path)
  {
    MappedEntry<O> entry = getMatch(path);
    if (entry != null)
      return entry.getValue();
    return null;
  }
  






  public MappedEntry<O> getMatch(String path)
  {
    if (path == null) {
      return null;
    }
    int l = path.length();
    
    MappedEntry<O> entry = null;
    

    if ((l == 1) && (path.charAt(0) == '/'))
    {
      entry = (MappedEntry)_exactMap.get("");
      if (entry != null) {
        return entry;
      }
    }
    
    entry = (MappedEntry)_exactMap.get(path);
    if (entry != null) {
      return entry;
    }
    
    int i = l;
    Trie<MappedEntry<O>> prefix_map = _prefixMap;
    while (i >= 0)
    {
      entry = (MappedEntry)prefix_map.getBest(path, 0, i);
      if (entry == null)
        break;
      String key = entry.getKey();
      if ((key.length() - 2 >= path.length()) || (path.charAt(key.length() - 2) == '/'))
        return entry;
      i = key.length() - 3;
    }
    

    if (_prefixDefault != null) {
      return _prefixDefault;
    }
    
    i = 0;
    Trie<MappedEntry<O>> suffix_map = _suffixMap;
    while ((i = path.indexOf('.', i + 1)) > 0)
    {
      entry = (MappedEntry)suffix_map.get(path, i + 1, l - i - 1);
      if (entry != null) {
        return entry;
      }
    }
    
    return _default;
  }
  







  public List<? extends Map.Entry<String, O>> getMatches(String path)
  {
    List<MappedEntry<O>> entries = new ArrayList();
    
    if (path == null)
      return entries;
    if (path.length() == 0) {
      return _defaultSingletonList;
    }
    
    MappedEntry<O> entry = (MappedEntry)_exactMap.get(path);
    if (entry != null) {
      entries.add(entry);
    }
    
    int l = path.length();
    int i = l;
    Trie<MappedEntry<O>> prefix_map = _prefixMap;
    while (i >= 0)
    {
      entry = (MappedEntry)prefix_map.getBest(path, 0, i);
      if (entry == null)
        break;
      String key = entry.getKey();
      if ((key.length() - 2 >= path.length()) || (path.charAt(key.length() - 2) == '/')) {
        entries.add(entry);
      }
      i = key.length() - 3;
    }
    

    if (_prefixDefault != null) {
      entries.add(_prefixDefault);
    }
    
    i = 0;
    Trie<MappedEntry<O>> suffix_map = _suffixMap;
    while ((i = path.indexOf('.', i + 1)) > 0)
    {
      entry = (MappedEntry)suffix_map.get(path, i + 1, l - i - 1);
      if (entry != null) {
        entries.add(entry);
      }
    }
    
    if ("/".equals(path))
    {
      entry = (MappedEntry)_exactMap.get("");
      if (entry != null) {
        entries.add(entry);
      }
    }
    
    if (_default != null) {
      entries.add(_default);
    }
    return entries;
  }
  







  public boolean containsMatch(String path)
  {
    MappedEntry<?> match = getMatch(path);
    return (match != null) && (!match.equals(_default));
  }
  


  public O remove(Object pathSpec)
  {
    if (pathSpec != null)
    {
      String spec = (String)pathSpec;
      if (spec.equals("/*")) {
        _prefixDefault = null;
      } else if (spec.endsWith("/*")) {
        _prefixMap.remove(spec.substring(0, spec.length() - 2));
      } else if (spec.startsWith("*.")) {
        _suffixMap.remove(spec.substring(2));
      } else if (spec.equals("/"))
      {
        _default = null;
        _defaultSingletonList = null;
      }
      else {
        _exactMap.remove(spec);
      } }
    return super.remove(pathSpec);
  }
  


  public void clear()
  {
    _exactMap.clear();
    _prefixMap = new ArrayTernaryTrie(false);
    _suffixMap = new ArrayTernaryTrie(false);
    _default = null;
    _defaultSingletonList = null;
    _prefixDefault = null;
    super.clear();
  }
  






  public static boolean match(String pathSpec, String path)
  {
    return match(pathSpec, path, false);
  }
  







  public static boolean match(String pathSpec, String path, boolean noDefault)
  {
    if (pathSpec.length() == 0) {
      return "/".equals(path);
    }
    char c = pathSpec.charAt(0);
    if (c == '/')
    {
      if (((!noDefault) && (pathSpec.length() == 1)) || (pathSpec.equals(path))) {
        return true;
      }
      if (isPathWildcardMatch(pathSpec, path)) {
        return true;
      }
    } else if (c == '*') {
      return path.regionMatches(path.length() - pathSpec.length() + 1, pathSpec, 1, pathSpec
        .length() - 1); }
    return false;
  }
  


  private static boolean isPathWildcardMatch(String pathSpec, String path)
  {
    int cpl = pathSpec.length() - 2;
    if ((pathSpec.endsWith("/*")) && (path.regionMatches(0, pathSpec, 0, cpl)))
    {
      if ((path.length() == cpl) || ('/' == path.charAt(cpl)))
        return true;
    }
    return false;
  }
  







  public static String pathMatch(String pathSpec, String path)
  {
    char c = pathSpec.charAt(0);
    
    if (c == '/')
    {
      if (pathSpec.length() == 1) {
        return path;
      }
      if (pathSpec.equals(path)) {
        return path;
      }
      if (isPathWildcardMatch(pathSpec, path)) {
        return path.substring(0, pathSpec.length() - 2);
      }
    } else if (c == '*')
    {
      if (path.regionMatches(path.length() - (pathSpec.length() - 1), pathSpec, 1, pathSpec
        .length() - 1))
        return path;
    }
    return null;
  }
  






  public static String pathInfo(String pathSpec, String path)
  {
    if ("".equals(pathSpec)) {
      return path;
    }
    char c = pathSpec.charAt(0);
    
    if (c == '/')
    {
      if (pathSpec.length() == 1) {
        return null;
      }
      boolean wildcard = isPathWildcardMatch(pathSpec, path);
      

      if ((pathSpec.equals(path)) && (!wildcard)) {
        return null;
      }
      if (wildcard)
      {
        if (path.length() == pathSpec.length() - 2)
          return null;
        return path.substring(pathSpec.length() - 2);
      }
    }
    return null;
  }
  










  public static String relativePath(String base, String pathSpec, String path)
  {
    String info = pathInfo(pathSpec, path);
    if (info == null) {
      info = path;
    }
    if (info.startsWith("./"))
      info = info.substring(2);
    if (base.endsWith("/")) {
      if (info.startsWith("/")) {
        path = base + info.substring(1);
      } else {
        path = base + info;
      }
    } else if (info.startsWith("/")) {
      path = base + info;
    } else
      path = base + "/" + info;
    return path;
  }
  

  public static class MappedEntry<O>
    implements Map.Entry<String, O>
  {
    private final String key;
    
    private final O value;
    private String mapped;
    
    MappedEntry(String key, O value)
    {
      this.key = key;
      this.value = value;
    }
    

    public String getKey()
    {
      return key;
    }
    

    public O getValue()
    {
      return value;
    }
    

    public O setValue(O o)
    {
      throw new UnsupportedOperationException();
    }
    

    public String toString()
    {
      return key + "=" + value;
    }
    
    public String getMapped()
    {
      return mapped;
    }
    
    void setMapped(String mapped)
    {
      this.mapped = mapped;
    }
  }
  
  public static class PathSet extends AbstractSet<String> implements Predicate<String>
  {
    private final PathMap<Boolean> _map = new PathMap();
    
    public PathSet() {}
    
    public Iterator<String> iterator() {
      return _map.keySet().iterator();
    }
    

    public int size()
    {
      return _map.size();
    }
    

    public boolean add(String item)
    {
      return _map.put(item, Boolean.TRUE) == null;
    }
    

    public boolean remove(Object item)
    {
      return _map.remove(item) != null;
    }
    

    public boolean contains(Object o)
    {
      return _map.containsKey(o);
    }
    

    public boolean test(String s)
    {
      return _map.containsMatch(s);
    }
    
    public boolean containsMatch(String s)
    {
      return _map.containsMatch(s);
    }
  }
}
