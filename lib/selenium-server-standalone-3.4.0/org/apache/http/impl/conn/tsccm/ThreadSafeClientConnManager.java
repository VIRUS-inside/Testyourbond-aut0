package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;



























































@Deprecated
@Contract(threading=ThreadingBehavior.SAFE_CONDITIONAL)
public class ThreadSafeClientConnManager
  implements ClientConnectionManager
{
  private final Log log;
  protected final SchemeRegistry schemeRegistry;
  protected final AbstractConnPool connectionPool;
  protected final ConnPoolByRoute pool;
  protected final ClientConnectionOperator connOperator;
  protected final ConnPerRouteBean connPerRoute;
  
  public ThreadSafeClientConnManager(SchemeRegistry schreg)
  {
    this(schreg, -1L, TimeUnit.MILLISECONDS);
  }
  


  public ThreadSafeClientConnManager()
  {
    this(SchemeRegistryFactory.createDefault());
  }
  









  public ThreadSafeClientConnManager(SchemeRegistry schreg, long connTTL, TimeUnit connTTLTimeUnit)
  {
    this(schreg, connTTL, connTTLTimeUnit, new ConnPerRouteBean());
  }
  













  public ThreadSafeClientConnManager(SchemeRegistry schreg, long connTTL, TimeUnit connTTLTimeUnit, ConnPerRouteBean connPerRoute)
  {
    Args.notNull(schreg, "Scheme registry");
    log = LogFactory.getLog(getClass());
    schemeRegistry = schreg;
    this.connPerRoute = connPerRoute;
    connOperator = createConnectionOperator(schreg);
    pool = createConnectionPool(connTTL, connTTLTimeUnit);
    connectionPool = pool;
  }
  








  @Deprecated
  public ThreadSafeClientConnManager(HttpParams params, SchemeRegistry schreg)
  {
    Args.notNull(schreg, "Scheme registry");
    log = LogFactory.getLog(getClass());
    schemeRegistry = schreg;
    connPerRoute = new ConnPerRouteBean();
    connOperator = createConnectionOperator(schreg);
    pool = ((ConnPoolByRoute)createConnectionPool(params));
    connectionPool = pool;
  }
  
  protected void finalize() throws Throwable
  {
    try {
      shutdown();
    } finally {
      super.finalize();
    }
  }
  






  @Deprecated
  protected AbstractConnPool createConnectionPool(HttpParams params)
  {
    return new ConnPoolByRoute(connOperator, params);
  }
  






  protected ConnPoolByRoute createConnectionPool(long connTTL, TimeUnit connTTLTimeUnit)
  {
    return new ConnPoolByRoute(connOperator, connPerRoute, 20, connTTL, connTTLTimeUnit);
  }
  













  protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg)
  {
    return new DefaultClientConnectionOperator(schreg);
  }
  
  public SchemeRegistry getSchemeRegistry()
  {
    return schemeRegistry;
  }
  



  public ClientConnectionRequest requestConnection(final HttpRoute route, Object state)
  {
    final PoolEntryRequest poolRequest = pool.requestPoolEntry(route, state);
    

    new ClientConnectionRequest()
    {
      public void abortRequest()
      {
        poolRequest.abortRequest();
      }
      

      public ManagedClientConnection getConnection(long timeout, TimeUnit tunit)
        throws InterruptedException, ConnectionPoolTimeoutException
      {
        Args.notNull(route, "Route");
        
        if (log.isDebugEnabled()) {
          log.debug("Get connection: " + route + ", timeout = " + timeout);
        }
        
        BasicPoolEntry entry = poolRequest.getPoolEntry(timeout, tunit);
        return new BasicPooledConnAdapter(ThreadSafeClientConnManager.this, entry);
      }
    };
  }
  


  public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit)
  {
    Args.check(conn instanceof BasicPooledConnAdapter, "Connection class mismatch, connection not obtained from this manager");
    
    BasicPooledConnAdapter hca = (BasicPooledConnAdapter)conn;
    if (hca.getPoolEntry() != null) {
      Asserts.check(hca.getManager() == this, "Connection not obtained from this manager");
    }
    synchronized (hca) {
      BasicPoolEntry entry = (BasicPoolEntry)hca.getPoolEntry();
      if (entry == null) {
        return;
      }
      try
      {
        if ((hca.isOpen()) && (!hca.isMarkedReusable()))
        {







          hca.shutdown(); }
      } catch (IOException iox) {
        boolean reusable;
        if (log.isDebugEnabled()) {
          log.debug("Exception shutting down released connection.", iox);
        }
      } finally {
        boolean reusable;
        boolean reusable = hca.isMarkedReusable();
        if (log.isDebugEnabled()) {
          if (reusable) {
            log.debug("Released connection is reusable.");
          } else {
            log.debug("Released connection is not reusable.");
          }
        }
        hca.detach();
        pool.freeEntry(entry, reusable, validDuration, timeUnit);
      }
    }
  }
  
  public void shutdown()
  {
    log.debug("Shutting down");
    pool.shutdown();
  }
  









  public int getConnectionsInPool(HttpRoute route)
  {
    return pool.getConnectionsInPool(route);
  }
  







  public int getConnectionsInPool()
  {
    return pool.getConnectionsInPool();
  }
  
  public void closeIdleConnections(long idleTimeout, TimeUnit tunit)
  {
    if (log.isDebugEnabled()) {
      log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
    }
    pool.closeIdleConnections(idleTimeout, tunit);
  }
  
  public void closeExpiredConnections()
  {
    log.debug("Closing expired connections");
    pool.closeExpiredConnections();
  }
  


  public int getMaxTotal()
  {
    return pool.getMaxTotalConnections();
  }
  


  public void setMaxTotal(int max)
  {
    pool.setMaxTotalConnections(max);
  }
  


  public int getDefaultMaxPerRoute()
  {
    return connPerRoute.getDefaultMaxPerRoute();
  }
  


  public void setDefaultMaxPerRoute(int max)
  {
    connPerRoute.setDefaultMaxPerRoute(max);
  }
  


  public int getMaxForRoute(HttpRoute route)
  {
    return connPerRoute.getMaxForRoute(route);
  }
  


  public void setMaxForRoute(HttpRoute route, int max)
  {
    connPerRoute.setMaxForRoute(route, max);
  }
}
