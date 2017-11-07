package org.seleniumhq.jetty9.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

























public class MultiMap<V>
  extends HashMap<String, List<V>>
{
  public MultiMap() {}
  
  public MultiMap(Map<String, List<V>> map)
  {
    super(map);
  }
  
  public MultiMap(MultiMap<V> map)
  {
    super(map);
  }
  







  public List<V> getValues(String name)
  {
    List<V> vals = (List)super.get(name);
    if ((vals == null) || (vals.isEmpty())) {
      return null;
    }
    return vals;
  }
  








  public V getValue(String name, int i)
  {
    List<V> vals = getValues(name);
    if (vals == null) {
      return null;
    }
    if ((i == 0) && (vals.isEmpty())) {
      return null;
    }
    return vals.get(i);
  }
  









  public String getString(String name)
  {
    List<V> vals = (List)get(name);
    if ((vals == null) || (vals.isEmpty()))
    {
      return null;
    }
    
    if (vals.size() == 1)
    {

      return vals.get(0).toString();
    }
    

    StringBuilder values = new StringBuilder(128);
    for (V e : vals)
    {
      if (e != null)
      {
        if (values.length() > 0)
          values.append(',');
        values.append(e.toString());
      }
    }
    return values.toString();
  }
  






  public List<V> put(String name, V value)
  {
    if (value == null) {
      return (List)super.put(name, null);
    }
    List<V> vals = new ArrayList();
    vals.add(value);
    return (List)put(name, vals);
  }
  




  public void putAllValues(Map<String, V> input)
  {
    for (Map.Entry<String, V> entry : input.entrySet())
    {
      put((String)entry.getKey(), entry.getValue());
    }
  }
  






  public List<V> putValues(String name, List<V> values)
  {
    return (List)super.put(name, values);
  }
  






  @SafeVarargs
  public final List<V> putValues(String name, V... values)
  {
    List<V> list = new ArrayList();
    list.addAll(Arrays.asList(values));
    return (List)super.put(name, list);
  }
  








  public void add(String name, V value)
  {
    List<V> lo = (List)get(name);
    if (lo == null) {
      lo = new ArrayList();
    }
    lo.add(value);
    super.put(name, lo);
  }
  







  public void addValues(String name, List<V> values)
  {
    List<V> lo = (List)get(name);
    if (lo == null) {
      lo = new ArrayList();
    }
    lo.addAll(values);
    put(name, lo);
  }
  







  public void addValues(String name, V[] values)
  {
    List<V> lo = (List)get(name);
    if (lo == null) {
      lo = new ArrayList();
    }
    lo.addAll(Arrays.asList(values));
    put(name, lo);
  }
  







  public boolean addAllValues(MultiMap<V> map)
  {
    boolean merged = false;
    
    if ((map == null) || (map.isEmpty()))
    {

      return merged;
    }
    
    for (Map.Entry<String, List<V>> entry : map.entrySet())
    {
      String name = (String)entry.getKey();
      List<V> values = (List)entry.getValue();
      
      if (containsKey(name))
      {
        merged = true;
      }
      
      addValues(name, values);
    }
    
    return merged;
  }
  






  public boolean removeValue(String name, V value)
  {
    List<V> lo = (List)get(name);
    if ((lo == null) || (lo.isEmpty())) {
      return false;
    }
    boolean ret = lo.remove(value);
    if (lo.isEmpty()) {
      remove(name);
    } else {
      put(name, lo);
    }
    return ret;
  }
  







  public boolean containsSimpleValue(V value)
  {
    for (List<V> vals : values())
    {
      if ((vals.size() == 1) && (vals.contains(value)))
      {
        return true;
      }
    }
    return false;
  }
  

  public String toString()
  {
    Iterator<Map.Entry<String, List<V>>> iter = entrySet().iterator();
    StringBuilder sb = new StringBuilder();
    sb.append('{');
    boolean delim = false;
    while (iter.hasNext())
    {
      Map.Entry<String, List<V>> e = (Map.Entry)iter.next();
      if (delim)
      {
        sb.append(", ");
      }
      String key = (String)e.getKey();
      List<V> vals = (List)e.getValue();
      sb.append(key);
      sb.append('=');
      if (vals.size() == 1)
      {
        sb.append(vals.get(0));
      }
      else
      {
        sb.append(vals);
      }
      delim = true;
    }
    sb.append('}');
    return sb.toString();
  }
  




  public Map<String, String[]> toStringArrayMap()
  {
    HashMap<String, String[]> map = new HashMap(size() * 3 / 2)
    {

      public String toString()
      {
        StringBuilder b = new StringBuilder();
        b.append('{');
        for (String k : super.keySet())
        {
          if (b.length() > 1)
            b.append(',');
          b.append(k);
          b.append('=');
          b.append(Arrays.asList((Object[])super.get(k)));
        }
        
        b.append('}');
        return b.toString();
      }
    };
    
    for (Map.Entry<String, List<V>> entry : entrySet())
    {
      String[] a = null;
      if (entry.getValue() != null)
      {
        a = new String[((List)entry.getValue()).size()];
        a = (String[])((List)entry.getValue()).toArray(a);
      }
      map.put(entry.getKey(), a);
    }
    return map;
  }
}
