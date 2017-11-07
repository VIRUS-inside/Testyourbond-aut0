package org.seleniumhq.jetty9.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;



























public class HostMap<TYPE>
  extends HashMap<String, TYPE>
{
  public HostMap()
  {
    super(11);
  }
  





  public HostMap(int capacity)
  {
    super(capacity);
  }
  





  public TYPE put(String host, TYPE object)
    throws IllegalArgumentException
  {
    return super.put(host, object);
  }
  





  public TYPE get(Object key)
  {
    return super.get(key);
  }
  








  public Object getLazyMatches(String host)
  {
    if (host == null) {
      return LazyList.getList(super.entrySet());
    }
    int idx = 0;
    String domain = host.trim();
    HashSet<String> domains = new HashSet();
    do {
      domains.add(domain);
      if ((idx = domain.indexOf('.')) > 0)
      {
        domain = domain.substring(idx + 1);
      }
    } while (idx > 0);
    
    Object entries = null;
    for (Map.Entry<String, TYPE> entry : super.entrySet())
    {
      if (domains.contains(entry.getKey()))
      {
        entries = LazyList.add(entries, entry);
      }
    }
    
    return entries;
  }
}
