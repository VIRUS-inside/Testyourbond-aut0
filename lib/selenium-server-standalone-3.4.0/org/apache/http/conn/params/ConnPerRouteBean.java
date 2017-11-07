package org.apache.http.conn.params;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;








































@Deprecated
@Contract(threading=ThreadingBehavior.SAFE)
public final class ConnPerRouteBean
  implements ConnPerRoute
{
  public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 2;
  private final ConcurrentHashMap<HttpRoute, Integer> maxPerHostMap;
  private volatile int defaultMax;
  
  public ConnPerRouteBean(int defaultMax)
  {
    maxPerHostMap = new ConcurrentHashMap();
    setDefaultMaxPerRoute(defaultMax);
  }
  
  public ConnPerRouteBean() {
    this(2);
  }
  
  public int getDefaultMax() {
    return defaultMax;
  }
  


  public int getDefaultMaxPerRoute()
  {
    return defaultMax;
  }
  
  public void setDefaultMaxPerRoute(int max) {
    Args.positive(max, "Default max per route");
    defaultMax = max;
  }
  
  public void setMaxForRoute(HttpRoute route, int max) {
    Args.notNull(route, "HTTP route");
    Args.positive(max, "Max per route");
    maxPerHostMap.put(route, Integer.valueOf(max));
  }
  
  public int getMaxForRoute(HttpRoute route)
  {
    Args.notNull(route, "HTTP route");
    Integer max = (Integer)maxPerHostMap.get(route);
    if (max != null) {
      return max.intValue();
    }
    return defaultMax;
  }
  
  public void setMaxForRoutes(Map<HttpRoute, Integer> map)
  {
    if (map == null) {
      return;
    }
    maxPerHostMap.clear();
    maxPerHostMap.putAll(map);
  }
  
  public String toString()
  {
    return maxPerHostMap.toString();
  }
}
