package org.apache.http.protocol;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;












































@Contract(threading=ThreadingBehavior.SAFE)
public class UriPatternMatcher<T>
{
  private final Map<String, T> map;
  
  public UriPatternMatcher()
  {
    map = new HashMap();
  }
  





  public synchronized void register(String pattern, T obj)
  {
    Args.notNull(pattern, "URI request pattern");
    map.put(pattern, obj);
  }
  




  public synchronized void unregister(String pattern)
  {
    if (pattern == null) {
      return;
    }
    map.remove(pattern);
  }
  


  @Deprecated
  public synchronized void setHandlers(Map<String, T> map)
  {
    Args.notNull(map, "Map of handlers");
    this.map.clear();
    this.map.putAll(map);
  }
  


  @Deprecated
  public synchronized void setObjects(Map<String, T> map)
  {
    Args.notNull(map, "Map of handlers");
    this.map.clear();
    this.map.putAll(map);
  }
  


  @Deprecated
  public synchronized Map<String, T> getObjects()
  {
    return map;
  }
  





  public synchronized T lookup(String path)
  {
    Args.notNull(path, "Request path");
    
    T obj = map.get(path);
    String bestMatch; if (obj == null)
    {
      bestMatch = null;
      for (String pattern : map.keySet()) {
        if (matchUriRequestPattern(pattern, path))
        {
          if ((bestMatch == null) || (bestMatch.length() < pattern.length()) || ((bestMatch.length() == pattern.length()) && (pattern.endsWith("*"))))
          {

            obj = map.get(pattern);
            bestMatch = pattern;
          }
        }
      }
    }
    return obj;
  }
  







  protected boolean matchUriRequestPattern(String pattern, String path)
  {
    if (pattern.equals("*")) {
      return true;
    }
    return ((pattern.endsWith("*")) && (path.startsWith(pattern.substring(0, pattern.length() - 1)))) || ((pattern.startsWith("*")) && (path.endsWith(pattern.substring(1, pattern.length()))));
  }
  



  public String toString()
  {
    return map.toString();
  }
}
