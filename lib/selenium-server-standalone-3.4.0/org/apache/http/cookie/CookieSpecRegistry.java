package org.apache.http.cookie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.config.Lookup;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;






































@Deprecated
@Contract(threading=ThreadingBehavior.SAFE)
public final class CookieSpecRegistry
  implements Lookup<CookieSpecProvider>
{
  private final ConcurrentHashMap<String, CookieSpecFactory> registeredSpecs;
  
  public CookieSpecRegistry()
  {
    registeredSpecs = new ConcurrentHashMap();
  }
  










  public void register(String name, CookieSpecFactory factory)
  {
    Args.notNull(name, "Name");
    Args.notNull(factory, "Cookie spec factory");
    registeredSpecs.put(name.toLowerCase(Locale.ENGLISH), factory);
  }
  




  public void unregister(String id)
  {
    Args.notNull(id, "Id");
    registeredSpecs.remove(id.toLowerCase(Locale.ENGLISH));
  }
  











  public CookieSpec getCookieSpec(String name, HttpParams params)
    throws IllegalStateException
  {
    Args.notNull(name, "Name");
    CookieSpecFactory factory = (CookieSpecFactory)registeredSpecs.get(name.toLowerCase(Locale.ENGLISH));
    if (factory != null) {
      return factory.newInstance(params);
    }
    throw new IllegalStateException("Unsupported cookie spec: " + name);
  }
  









  public CookieSpec getCookieSpec(String name)
    throws IllegalStateException
  {
    return getCookieSpec(name, null);
  }
  








  public List<String> getSpecNames()
  {
    return new ArrayList(registeredSpecs.keySet());
  }
  





  public void setItems(Map<String, CookieSpecFactory> map)
  {
    if (map == null) {
      return;
    }
    registeredSpecs.clear();
    registeredSpecs.putAll(map);
  }
  
  public CookieSpecProvider lookup(final String name)
  {
    new CookieSpecProvider()
    {
      public CookieSpec create(HttpContext context)
      {
        HttpRequest request = (HttpRequest)context.getAttribute("http.request");
        
        return getCookieSpec(name, request.getParams());
      }
    };
  }
}
