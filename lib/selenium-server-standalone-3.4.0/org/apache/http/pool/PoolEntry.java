package org.apache.http.pool;

import java.util.concurrent.TimeUnit;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;


























































@Contract(threading=ThreadingBehavior.SAFE_CONDITIONAL)
public abstract class PoolEntry<T, C>
{
  private final String id;
  private final T route;
  private final C conn;
  private final long created;
  private final long validityDeadline;
  private long updated;
  private long expiry;
  private volatile Object state;
  
  public PoolEntry(String id, T route, C conn, long timeToLive, TimeUnit tunit)
  {
    Args.notNull(route, "Route");
    Args.notNull(conn, "Connection");
    Args.notNull(tunit, "Time unit");
    this.id = id;
    this.route = route;
    this.conn = conn;
    created = System.currentTimeMillis();
    updated = created;
    if (timeToLive > 0L) {
      validityDeadline = (created + tunit.toMillis(timeToLive));
    } else {
      validityDeadline = Long.MAX_VALUE;
    }
    expiry = validityDeadline;
  }
  






  public PoolEntry(String id, T route, C conn)
  {
    this(id, route, conn, 0L, TimeUnit.MILLISECONDS);
  }
  
  public String getId() {
    return id;
  }
  
  public T getRoute() {
    return route;
  }
  
  public C getConnection() {
    return conn;
  }
  
  public long getCreated() {
    return created;
  }
  


  public long getValidityDeadline()
  {
    return validityDeadline;
  }
  


  @Deprecated
  public long getValidUnit()
  {
    return validityDeadline;
  }
  
  public Object getState() {
    return state;
  }
  
  public void setState(Object state) {
    this.state = state;
  }
  
  public synchronized long getUpdated() {
    return updated;
  }
  
  public synchronized long getExpiry() {
    return expiry;
  }
  
  public synchronized void updateExpiry(long time, TimeUnit tunit) {
    Args.notNull(tunit, "Time unit");
    updated = System.currentTimeMillis();
    long newExpiry;
    long newExpiry; if (time > 0L) {
      newExpiry = updated + tunit.toMillis(time);
    } else {
      newExpiry = Long.MAX_VALUE;
    }
    expiry = Math.min(newExpiry, validityDeadline);
  }
  
  public synchronized boolean isExpired(long now) {
    return now >= expiry;
  }
  



  public abstract void close();
  


  public abstract boolean isClosed();
  


  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("[id:");
    buffer.append(id);
    buffer.append("][route:");
    buffer.append(route);
    buffer.append("][state:");
    buffer.append(state);
    buffer.append("]");
    return buffer.toString();
  }
}
