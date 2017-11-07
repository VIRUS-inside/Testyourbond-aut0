package org.apache.http.conn.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;








































@Deprecated
@Contract(threading=ThreadingBehavior.SAFE)
public final class SchemeRegistry
{
  private final ConcurrentHashMap<String, Scheme> registeredSchemes;
  
  public SchemeRegistry()
  {
    registeredSchemes = new ConcurrentHashMap();
  }
  









  public final Scheme getScheme(String name)
  {
    Scheme found = get(name);
    if (found == null) {
      throw new IllegalStateException("Scheme '" + name + "' not registered.");
    }
    
    return found;
  }
  










  public final Scheme getScheme(HttpHost host)
  {
    Args.notNull(host, "Host");
    return getScheme(host.getSchemeName());
  }
  







  public final Scheme get(String name)
  {
    Args.notNull(name, "Scheme name");
    

    Scheme found = (Scheme)registeredSchemes.get(name);
    return found;
  }
  









  public final Scheme register(Scheme sch)
  {
    Args.notNull(sch, "Scheme");
    Scheme old = (Scheme)registeredSchemes.put(sch.getName(), sch);
    return old;
  }
  







  public final Scheme unregister(String name)
  {
    Args.notNull(name, "Scheme name");
    

    Scheme gone = (Scheme)registeredSchemes.remove(name);
    return gone;
  }
  




  public final List<String> getSchemeNames()
  {
    return new ArrayList(registeredSchemes.keySet());
  }
  





  public void setItems(Map<String, Scheme> map)
  {
    if (map == null) {
      return;
    }
    registeredSchemes.clear();
    registeredSchemes.putAll(map);
  }
}
