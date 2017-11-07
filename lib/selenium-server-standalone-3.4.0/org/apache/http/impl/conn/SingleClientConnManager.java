package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;











































@Deprecated
@Contract(threading=ThreadingBehavior.SAFE)
public class SingleClientConnManager
  implements ClientConnectionManager
{
  private final Log log = LogFactory.getLog(getClass());
  


  public static final String MISUSE_MESSAGE = "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.";
  


  protected final SchemeRegistry schemeRegistry;
  


  protected final ClientConnectionOperator connOperator;
  


  protected final boolean alwaysShutDown;
  


  protected volatile PoolEntry uniquePoolEntry;
  


  protected volatile ConnAdapter managedConn;
  


  protected volatile long lastReleaseTime;
  


  protected volatile long connectionExpiresTime;
  

  protected volatile boolean isShutDown;
  


  @Deprecated
  public SingleClientConnManager(HttpParams params, SchemeRegistry schreg)
  {
    this(schreg);
  }
  



  public SingleClientConnManager(SchemeRegistry schreg)
  {
    Args.notNull(schreg, "Scheme registry");
    schemeRegistry = schreg;
    connOperator = createConnectionOperator(schreg);
    uniquePoolEntry = new PoolEntry();
    managedConn = null;
    lastReleaseTime = -1L;
    alwaysShutDown = false;
    isShutDown = false;
  }
  


  public SingleClientConnManager()
  {
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
  












  protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg)
  {
    return new DefaultClientConnectionOperator(schreg);
  }
  



  protected final void assertStillUp()
    throws IllegalStateException
  {
    Asserts.check(!isShutDown, "Manager is shut down");
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
  









  public ManagedClientConnection getConnection(HttpRoute route, Object state)
  {
    Args.notNull(route, "Route");
    assertStillUp();
    
    if (log.isDebugEnabled()) {
      log.debug("Get connection for route " + route);
    }
    
    synchronized (this)
    {
      Asserts.check(managedConn == null, "Invalid use of SingleClientConnManager: connection still allocated.\nMake sure to release the connection before allocating another one.");
      

      boolean recreate = false;
      boolean shutdown = false;
      

      closeExpiredConnections();
      
      if (uniquePoolEntry.connection.isOpen()) {
        RouteTracker tracker = uniquePoolEntry.tracker;
        shutdown = (tracker == null) || (!tracker.toRoute().equals(route));


      }
      else
      {


        recreate = true;
      }
      
      if (shutdown) {
        recreate = true;
        try {
          uniquePoolEntry.shutdown();
        } catch (IOException iox) {
          log.debug("Problem shutting down connection.", iox);
        }
      }
      
      if (recreate) {
        uniquePoolEntry = new PoolEntry();
      }
      
      managedConn = new ConnAdapter(uniquePoolEntry, route);
      
      return managedConn;
    }
  }
  


  public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit)
  {
    Args.check(conn instanceof ConnAdapter, "Connection class mismatch, connection not obtained from this manager");
    
    assertStillUp();
    
    if (log.isDebugEnabled()) {
      log.debug("Releasing connection " + conn);
    }
    
    ConnAdapter sca = (ConnAdapter)conn;
    synchronized (sca) {
      if (poolEntry == null)
      {
        return;
      }
      ClientConnectionManager manager = sca.getManager();
      Asserts.check(manager == this, "Connection not obtained from this manager");
      try
      {
        if ((sca.isOpen()) && ((alwaysShutDown) || (!sca.isMarkedReusable())))
        {

          if (log.isDebugEnabled()) {
            log.debug("Released connection open but not reusable.");
          }
          




          sca.shutdown();
        }
      } catch (IOException iox) {
        if (log.isDebugEnabled()) {
          log.debug("Exception shutting down released connection.", iox);
        }
      }
      finally {
        sca.detach();
        synchronized (this) {
          managedConn = null;
          lastReleaseTime = System.currentTimeMillis();
          if (validDuration > 0L) {
            connectionExpiresTime = (timeUnit.toMillis(validDuration) + lastReleaseTime);
          } else {
            connectionExpiresTime = Long.MAX_VALUE;
          }
        }
      }
    }
  }
  
  public void closeExpiredConnections()
  {
    long time = connectionExpiresTime;
    if (System.currentTimeMillis() >= time) {
      closeIdleConnections(0L, TimeUnit.MILLISECONDS);
    }
  }
  
  public void closeIdleConnections(long idletime, TimeUnit tunit)
  {
    assertStillUp();
    

    Args.notNull(tunit, "Time unit");
    
    synchronized (this) {
      if ((managedConn == null) && (uniquePoolEntry.connection.isOpen())) {
        long cutoff = System.currentTimeMillis() - tunit.toMillis(idletime);
        
        if (lastReleaseTime <= cutoff) {
          try {
            uniquePoolEntry.close();
          }
          catch (IOException iox) {
            log.debug("Problem closing idle connection.", iox);
          }
        }
      }
    }
  }
  
  public void shutdown()
  {
    isShutDown = true;
    synchronized (this) {
      try {
        if (uniquePoolEntry != null) {
          uniquePoolEntry.shutdown();
        }
      }
      catch (IOException iox) {
        log.debug("Problem while shutting down manager.", iox);
      } finally {
        uniquePoolEntry = null;
        managedConn = null;
      }
    }
  }
  
  protected void revokeConnection() {
    ConnAdapter conn = managedConn;
    if (conn == null) {
      return;
    }
    conn.detach();
    
    synchronized (this) {
      try {
        uniquePoolEntry.shutdown();
      }
      catch (IOException iox) {
        log.debug("Problem while shutting down connection.", iox);
      }
    }
  }
  





  protected class PoolEntry
    extends AbstractPoolEntry
  {
    protected PoolEntry()
    {
      super(null);
    }
    

    protected void close()
      throws IOException
    {
      shutdownEntry();
      if (connection.isOpen()) {
        connection.close();
      }
    }
    

    protected void shutdown()
      throws IOException
    {
      shutdownEntry();
      if (connection.isOpen()) {
        connection.shutdown();
      }
    }
  }
  








  protected class ConnAdapter
    extends AbstractPooledConnAdapter
  {
    protected ConnAdapter(SingleClientConnManager.PoolEntry entry, HttpRoute route)
    {
      super(entry);
      markReusable();
      route = route;
    }
  }
}
