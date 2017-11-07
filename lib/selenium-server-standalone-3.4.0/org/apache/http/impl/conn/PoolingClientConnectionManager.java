package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolStats;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;















































@Deprecated
@Contract(threading=ThreadingBehavior.SAFE_CONDITIONAL)
public class PoolingClientConnectionManager
  implements ClientConnectionManager, ConnPoolControl<HttpRoute>
{
  private final Log log = LogFactory.getLog(getClass());
  
  private final SchemeRegistry schemeRegistry;
  
  private final HttpConnPool pool;
  
  private final ClientConnectionOperator operator;
  
  private final DnsResolver dnsResolver;
  
  public PoolingClientConnectionManager(SchemeRegistry schreg)
  {
    this(schreg, -1L, TimeUnit.MILLISECONDS);
  }
  
  public PoolingClientConnectionManager(SchemeRegistry schreg, DnsResolver dnsResolver) {
    this(schreg, -1L, TimeUnit.MILLISECONDS, dnsResolver);
  }
  
  public PoolingClientConnectionManager() {
    this(SchemeRegistryFactory.createDefault());
  }
  

  public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long timeToLive, TimeUnit tunit)
  {
    this(schemeRegistry, timeToLive, tunit, new SystemDefaultDnsResolver());
  }
  


  public PoolingClientConnectionManager(SchemeRegistry schemeRegistry, long timeToLive, TimeUnit tunit, DnsResolver dnsResolver)
  {
    Args.notNull(schemeRegistry, "Scheme registry");
    Args.notNull(dnsResolver, "DNS resolver");
    this.schemeRegistry = schemeRegistry;
    this.dnsResolver = dnsResolver;
    operator = createConnectionOperator(schemeRegistry);
    pool = new HttpConnPool(log, operator, 2, 20, timeToLive, tunit);
  }
  
  protected void finalize() throws Throwable
  {
    try {
      shutdown();
    } finally {
      super.finalize();
    }
  }
  











  protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg)
  {
    return new DefaultClientConnectionOperator(schreg, dnsResolver);
  }
  
  public SchemeRegistry getSchemeRegistry()
  {
    return schemeRegistry;
  }
  
  private String format(HttpRoute route, Object state) {
    StringBuilder buf = new StringBuilder();
    buf.append("[route: ").append(route).append("]");
    if (state != null) {
      buf.append("[state: ").append(state).append("]");
    }
    return buf.toString();
  }
  
  private String formatStats(HttpRoute route) {
    StringBuilder buf = new StringBuilder();
    PoolStats totals = pool.getTotalStats();
    PoolStats stats = pool.getStats(route);
    buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
    buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
    buf.append(" of ").append(stats.getMax()).append("; ");
    buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
    buf.append(" of ").append(totals.getMax()).append("]");
    return buf.toString();
  }
  
  private String format(HttpPoolEntry entry) {
    StringBuilder buf = new StringBuilder();
    buf.append("[id: ").append(entry.getId()).append("]");
    buf.append("[route: ").append(entry.getRoute()).append("]");
    Object state = entry.getState();
    if (state != null) {
      buf.append("[state: ").append(state).append("]");
    }
    return buf.toString();
  }
  


  public ClientConnectionRequest requestConnection(HttpRoute route, Object state)
  {
    Args.notNull(route, "HTTP route");
    if (log.isDebugEnabled()) {
      log.debug("Connection request: " + format(route, state) + formatStats(route));
    }
    final Future<HttpPoolEntry> future = pool.lease(route, state);
    
    new ClientConnectionRequest()
    {
      public void abortRequest()
      {
        future.cancel(true);
      }
      

      public ManagedClientConnection getConnection(long timeout, TimeUnit tunit)
        throws InterruptedException, ConnectionPoolTimeoutException
      {
        return leaseConnection(future, timeout, tunit);
      }
    };
  }
  



  ManagedClientConnection leaseConnection(Future<HttpPoolEntry> future, long timeout, TimeUnit tunit)
    throws InterruptedException, ConnectionPoolTimeoutException
  {
    try
    {
      HttpPoolEntry entry = (HttpPoolEntry)future.get(timeout, tunit);
      if ((entry == null) || (future.isCancelled())) {
        throw new InterruptedException();
      }
      Asserts.check(entry.getConnection() != null, "Pool entry with no connection");
      if (log.isDebugEnabled()) {
        log.debug("Connection leased: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
      }
      return new ManagedClientConnectionImpl(this, operator, entry);
    } catch (ExecutionException ex) {
      Throwable cause = ex.getCause();
      if (cause == null) {
        cause = ex;
      }
      log.error("Unexpected exception leasing connection from pool", cause);
      
      throw new InterruptedException();
    } catch (TimeoutException ex) {
      throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
    }
  }
  


  public void releaseConnection(ManagedClientConnection conn, long keepalive, TimeUnit tunit)
  {
    Args.check(conn instanceof ManagedClientConnectionImpl, "Connection class mismatch, connection not obtained from this manager");
    
    ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl)conn;
    Asserts.check(managedConn.getManager() == this, "Connection not obtained from this manager");
    synchronized (managedConn) {
      HttpPoolEntry entry = managedConn.detach();
      if (entry == null) {
        return;
      }
      try {
        if ((managedConn.isOpen()) && (!managedConn.isMarkedReusable())) {
          try {
            managedConn.shutdown();
          } catch (IOException iox) {
            if (log.isDebugEnabled()) {
              log.debug("I/O exception shutting down released connection", iox);
            }
          }
        }
        
        if (managedConn.isMarkedReusable()) {
          entry.updateExpiry(keepalive, tunit != null ? tunit : TimeUnit.MILLISECONDS);
          if (log.isDebugEnabled()) { String s;
            String s;
            if (keepalive > 0L) {
              s = "for " + keepalive + " " + tunit;
            } else {
              s = "indefinitely";
            }
            log.debug("Connection " + format(entry) + " can be kept alive " + s);
          }
        }
      } finally {
        pool.release(entry, managedConn.isMarkedReusable());
      }
      if (log.isDebugEnabled()) {
        log.debug("Connection released: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
      }
    }
  }
  
  public void shutdown()
  {
    log.debug("Connection manager is shutting down");
    try {
      pool.shutdown();
    } catch (IOException ex) {
      log.debug("I/O exception shutting down connection manager", ex);
    }
    log.debug("Connection manager shut down");
  }
  
  public void closeIdleConnections(long idleTimeout, TimeUnit tunit)
  {
    if (log.isDebugEnabled()) {
      log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
    }
    pool.closeIdle(idleTimeout, tunit);
  }
  
  public void closeExpiredConnections()
  {
    log.debug("Closing expired connections");
    pool.closeExpired();
  }
  
  public int getMaxTotal()
  {
    return pool.getMaxTotal();
  }
  
  public void setMaxTotal(int max)
  {
    pool.setMaxTotal(max);
  }
  
  public int getDefaultMaxPerRoute()
  {
    return pool.getDefaultMaxPerRoute();
  }
  
  public void setDefaultMaxPerRoute(int max)
  {
    pool.setDefaultMaxPerRoute(max);
  }
  
  public int getMaxPerRoute(HttpRoute route)
  {
    return pool.getMaxPerRoute(route);
  }
  
  public void setMaxPerRoute(HttpRoute route, int max)
  {
    pool.setMaxPerRoute(route, max);
  }
  
  public PoolStats getTotalStats()
  {
    return pool.getTotalStats();
  }
  
  public PoolStats getStats(HttpRoute route)
  {
    return pool.getStats(route);
  }
}
