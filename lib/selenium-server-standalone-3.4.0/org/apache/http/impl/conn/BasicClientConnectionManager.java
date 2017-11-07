package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;















































@Deprecated
@Contract(threading=ThreadingBehavior.SAFE)
public class BasicClientConnectionManager
  implements ClientConnectionManager
{
  private final Log log = LogFactory.getLog(getClass());
  
  private static final AtomicLong COUNTER = new AtomicLong();
  


  public static final String MISUSE_MESSAGE = "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
  


  private final SchemeRegistry schemeRegistry;
  


  private final ClientConnectionOperator connOperator;
  


  private HttpPoolEntry poolEntry;
  

  private ManagedClientConnectionImpl conn;
  

  private volatile boolean shutdown;
  


  public BasicClientConnectionManager(SchemeRegistry schreg)
  {
    Args.notNull(schreg, "Scheme registry");
    schemeRegistry = schreg;
    connOperator = createConnectionOperator(schreg);
  }
  
  public BasicClientConnectionManager() {
    this(SchemeRegistryFactory.createDefault());
  }
  
  protected void finalize() throws Throwable
  {
    try {
      shutdown();
    } finally {
      super.finalize();
    }
  }
  
  public SchemeRegistry getSchemeRegistry()
  {
    return schemeRegistry;
  }
  
  protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
    return new DefaultClientConnectionOperator(schreg);
  }
  



  public final ClientConnectionRequest requestConnection(final HttpRoute route, final Object state)
  {
    new ClientConnectionRequest()
    {
      public void abortRequest() {}
      




      public ManagedClientConnection getConnection(long timeout, TimeUnit tunit)
      {
        return getConnection(route, state);
      }
    };
  }
  

  private void assertNotShutdown()
  {
    Asserts.check(!shutdown, "Connection manager has been shut down");
  }
  
  ManagedClientConnection getConnection(HttpRoute route, Object state) {
    Args.notNull(route, "Route");
    synchronized (this) {
      assertNotShutdown();
      if (log.isDebugEnabled()) {
        log.debug("Get connection for route " + route);
      }
      Asserts.check(conn == null, "Invalid use of BasicClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
      if ((poolEntry != null) && (!poolEntry.getPlannedRoute().equals(route))) {
        poolEntry.close();
        poolEntry = null;
      }
      if (poolEntry == null) {
        String id = Long.toString(COUNTER.getAndIncrement());
        OperatedClientConnection opconn = connOperator.createConnection();
        poolEntry = new HttpPoolEntry(log, id, route, opconn, 0L, TimeUnit.MILLISECONDS);
      }
      long now = System.currentTimeMillis();
      if (poolEntry.isExpired(now)) {
        poolEntry.close();
        poolEntry.getTracker().reset();
      }
      conn = new ManagedClientConnectionImpl(this, connOperator, poolEntry);
      return conn;
    }
  }
  
  private void shutdownConnection(HttpClientConnection conn) {
    try {
      conn.shutdown();
    } catch (IOException iox) {
      if (log.isDebugEnabled()) {
        log.debug("I/O exception shutting down connection", iox);
      }
    }
  }
  
  public void releaseConnection(ManagedClientConnection conn, long keepalive, TimeUnit tunit)
  {
    Args.check(conn instanceof ManagedClientConnectionImpl, "Connection class mismatch, connection not obtained from this manager");
    
    ManagedClientConnectionImpl managedConn = (ManagedClientConnectionImpl)conn;
    synchronized (managedConn) {
      if (log.isDebugEnabled()) {
        log.debug("Releasing connection " + conn);
      }
      if (managedConn.getPoolEntry() == null) {
        return;
      }
      ClientConnectionManager manager = managedConn.getManager();
      Asserts.check(manager == this, "Connection not obtained from this manager");
      synchronized (this) {
        if (shutdown) {
          shutdownConnection(managedConn);
          return;
        }
        try {
          if ((managedConn.isOpen()) && (!managedConn.isMarkedReusable())) {
            shutdownConnection(managedConn);
          }
          if (managedConn.isMarkedReusable()) {
            poolEntry.updateExpiry(keepalive, tunit != null ? tunit : TimeUnit.MILLISECONDS);
            if (log.isDebugEnabled()) { String s;
              String s;
              if (keepalive > 0L) {
                s = "for " + keepalive + " " + tunit;
              } else {
                s = "indefinitely";
              }
              log.debug("Connection can be kept alive " + s);
            }
          }
        } finally {
          managedConn.detach();
          this.conn = null;
          if (poolEntry.isClosed()) {
            poolEntry = null;
          }
        }
      }
    }
  }
  
  public void closeExpiredConnections()
  {
    synchronized (this) {
      assertNotShutdown();
      long now = System.currentTimeMillis();
      if ((poolEntry != null) && (poolEntry.isExpired(now))) {
        poolEntry.close();
        poolEntry.getTracker().reset();
      }
    }
  }
  
  public void closeIdleConnections(long idletime, TimeUnit tunit)
  {
    Args.notNull(tunit, "Time unit");
    synchronized (this) {
      assertNotShutdown();
      long time = tunit.toMillis(idletime);
      if (time < 0L) {
        time = 0L;
      }
      long deadline = System.currentTimeMillis() - time;
      if ((poolEntry != null) && (poolEntry.getUpdated() <= deadline)) {
        poolEntry.close();
        poolEntry.getTracker().reset();
      }
    }
  }
  
  public void shutdown()
  {
    synchronized (this) {
      shutdown = true;
      try {
        if (poolEntry != null) {
          poolEntry.close();
        }
      } finally {
        poolEntry = null;
        conn = null;
      }
    }
  }
}
