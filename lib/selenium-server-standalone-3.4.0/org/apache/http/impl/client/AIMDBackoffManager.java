package org.apache.http.impl.client;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.BackoffManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.util.Args;















































public class AIMDBackoffManager
  implements BackoffManager
{
  private final ConnPoolControl<HttpRoute> connPerRoute;
  private final Clock clock;
  private final Map<HttpRoute, Long> lastRouteProbes;
  private final Map<HttpRoute, Long> lastRouteBackoffs;
  private long coolDown = 5000L;
  private double backoffFactor = 0.5D;
  private int cap = 2;
  






  public AIMDBackoffManager(ConnPoolControl<HttpRoute> connPerRoute)
  {
    this(connPerRoute, new SystemClock());
  }
  
  AIMDBackoffManager(ConnPoolControl<HttpRoute> connPerRoute, Clock clock) {
    this.clock = clock;
    this.connPerRoute = connPerRoute;
    lastRouteProbes = new HashMap();
    lastRouteBackoffs = new HashMap();
  }
  
  public void backOff(HttpRoute route)
  {
    synchronized (connPerRoute) {
      int curr = connPerRoute.getMaxPerRoute(route);
      Long lastUpdate = getLastUpdate(lastRouteBackoffs, route);
      long now = clock.getCurrentTime();
      if (now - lastUpdate.longValue() < coolDown) {
        return;
      }
      connPerRoute.setMaxPerRoute(route, getBackedOffPoolSize(curr));
      lastRouteBackoffs.put(route, Long.valueOf(now));
    }
  }
  
  private int getBackedOffPoolSize(int curr) {
    if (curr <= 1) {
      return 1;
    }
    return (int)Math.floor(backoffFactor * curr);
  }
  
  public void probe(HttpRoute route)
  {
    synchronized (connPerRoute) {
      int curr = connPerRoute.getMaxPerRoute(route);
      int max = curr >= cap ? cap : curr + 1;
      Long lastProbe = getLastUpdate(lastRouteProbes, route);
      Long lastBackoff = getLastUpdate(lastRouteBackoffs, route);
      long now = clock.getCurrentTime();
      if ((now - lastProbe.longValue() < coolDown) || (now - lastBackoff.longValue() < coolDown)) {
        return;
      }
      connPerRoute.setMaxPerRoute(route, max);
      lastRouteProbes.put(route, Long.valueOf(now));
    }
  }
  
  private Long getLastUpdate(Map<HttpRoute, Long> updates, HttpRoute route) {
    Long lastUpdate = (Long)updates.get(route);
    if (lastUpdate == null) {
      lastUpdate = Long.valueOf(0L);
    }
    return lastUpdate;
  }
  








  public void setBackoffFactor(double d)
  {
    Args.check((d > 0.0D) && (d < 1.0D), "Backoff factor must be 0.0 < f < 1.0");
    backoffFactor = d;
  }
  






  public void setCooldownMillis(long l)
  {
    Args.positive(coolDown, "Cool down");
    coolDown = l;
  }
  




  public void setPerHostConnectionCap(int cap)
  {
    Args.positive(cap, "Per host connection cap");
    this.cap = cap;
  }
}
