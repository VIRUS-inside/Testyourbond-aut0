package org.apache.http.impl.conn.tsccm;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;












































@Deprecated
public class ConnPoolByRoute
  extends AbstractConnPool
{
  private final Log log = LogFactory.getLog(getClass());
  

  private final Lock poolLock;
  

  protected final ClientConnectionOperator operator;
  

  protected final ConnPerRoute connPerRoute;
  

  protected final Set<BasicPoolEntry> leasedConnections;
  

  protected final Queue<BasicPoolEntry> freeConnections;
  

  protected final Queue<WaitingThread> waitingThreads;
  

  protected final Map<HttpRoute, RouteSpecificPool> routeToPool;
  

  private final long connTTL;
  

  private final TimeUnit connTTLTimeUnit;
  

  protected volatile boolean shutdown;
  

  protected volatile int maxTotalConnections;
  

  protected volatile int numConnections;
  

  public ConnPoolByRoute(ClientConnectionOperator operator, ConnPerRoute connPerRoute, int maxTotalConnections)
  {
    this(operator, connPerRoute, maxTotalConnections, -1L, TimeUnit.MILLISECONDS);
  }
  








  public ConnPoolByRoute(ClientConnectionOperator operator, ConnPerRoute connPerRoute, int maxTotalConnections, long connTTL, TimeUnit connTTLTimeUnit)
  {
    Args.notNull(operator, "Connection operator");
    Args.notNull(connPerRoute, "Connections per route");
    poolLock = poolLock;
    leasedConnections = leasedConnections;
    this.operator = operator;
    this.connPerRoute = connPerRoute;
    this.maxTotalConnections = maxTotalConnections;
    freeConnections = createFreeConnQueue();
    waitingThreads = createWaitingThreadQueue();
    routeToPool = createRouteToPoolMap();
    this.connTTL = connTTL;
    this.connTTLTimeUnit = connTTLTimeUnit;
  }
  
  protected Lock getLock() {
    return poolLock;
  }
  




  @Deprecated
  public ConnPoolByRoute(ClientConnectionOperator operator, HttpParams params)
  {
    this(operator, ConnManagerParams.getMaxConnectionsPerRoute(params), ConnManagerParams.getMaxTotalConnections(params));
  }
  







  protected Queue<BasicPoolEntry> createFreeConnQueue()
  {
    return new LinkedList();
  }
  





  protected Queue<WaitingThread> createWaitingThreadQueue()
  {
    return new LinkedList();
  }
  





  protected Map<HttpRoute, RouteSpecificPool> createRouteToPoolMap()
  {
    return new HashMap();
  }
  








  protected RouteSpecificPool newRouteSpecificPool(HttpRoute route)
  {
    return new RouteSpecificPool(route, connPerRoute);
  }
  










  protected WaitingThread newWaitingThread(Condition cond, RouteSpecificPool rospl)
  {
    return new WaitingThread(cond, rospl);
  }
  
  private void closeConnection(BasicPoolEntry entry) {
    OperatedClientConnection conn = entry.getConnection();
    if (conn != null) {
      try {
        conn.close();
      } catch (IOException ex) {
        log.debug("I/O error closing connection", ex);
      }
    }
  }
  









  protected RouteSpecificPool getRoutePool(HttpRoute route, boolean create)
  {
    RouteSpecificPool rospl = null;
    poolLock.lock();
    try
    {
      rospl = (RouteSpecificPool)routeToPool.get(route);
      if ((rospl == null) && (create))
      {
        rospl = newRouteSpecificPool(route);
        routeToPool.put(route, rospl);
      }
    }
    finally {
      poolLock.unlock();
    }
    
    return rospl;
  }
  
  public int getConnectionsInPool(HttpRoute route) {
    poolLock.lock();
    try
    {
      RouteSpecificPool rospl = getRoutePool(route, false);
      return rospl != null ? rospl.getEntryCount() : 0;
    }
    finally {
      poolLock.unlock();
    }
  }
  
  public int getConnectionsInPool() {
    poolLock.lock();
    try {
      return numConnections;
    } finally {
      poolLock.unlock();
    }
  }
  



  public PoolEntryRequest requestPoolEntry(final HttpRoute route, final Object state)
  {
    final WaitingThreadAborter aborter = new WaitingThreadAborter();
    
    new PoolEntryRequest()
    {
      public void abortRequest()
      {
        poolLock.lock();
        try {
          aborter.abort();
        } finally {
          poolLock.unlock();
        }
      }
      


      public BasicPoolEntry getPoolEntry(long timeout, TimeUnit tunit)
        throws InterruptedException, ConnectionPoolTimeoutException
      {
        return getEntryBlocking(route, state, timeout, tunit, aborter);
      }
    };
  }
  






















  protected BasicPoolEntry getEntryBlocking(HttpRoute route, Object state, long timeout, TimeUnit tunit, WaitingThreadAborter aborter)
    throws ConnectionPoolTimeoutException, InterruptedException
  {
    Date deadline = null;
    if (timeout > 0L) {
      deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
    }
    

    BasicPoolEntry entry = null;
    poolLock.lock();
    try
    {
      RouteSpecificPool rospl = getRoutePool(route, true);
      WaitingThread waitingThread = null;
      
      while (entry == null) {
        Asserts.check(!shutdown, "Connection pool shut down");
        
        if (log.isDebugEnabled()) {
          log.debug("[" + route + "] total kept alive: " + freeConnections.size() + ", total issued: " + leasedConnections.size() + ", total allocated: " + numConnections + " out of " + maxTotalConnections);
        }
        








        entry = getFreeEntry(rospl, state);
        if (entry != null) {
          break;
        }
        
        boolean hasCapacity = rospl.getCapacity() > 0;
        
        if (log.isDebugEnabled()) {
          log.debug("Available capacity: " + rospl.getCapacity() + " out of " + rospl.getMaxEntries() + " [" + route + "][" + state + "]");
        }
        


        if ((hasCapacity) && (numConnections < maxTotalConnections))
        {
          entry = createEntry(rospl, operator);
        }
        else if ((hasCapacity) && (!freeConnections.isEmpty()))
        {
          deleteLeastUsedEntry();
          

          rospl = getRoutePool(route, true);
          entry = createEntry(rospl, operator);
        }
        else
        {
          if (log.isDebugEnabled()) {
            log.debug("Need to wait for connection [" + route + "][" + state + "]");
          }
          

          if (waitingThread == null) {
            waitingThread = newWaitingThread(poolLock.newCondition(), rospl);
            
            aborter.setWaitingThread(waitingThread);
          }
          
          boolean success = false;
          try {
            rospl.queueThread(waitingThread);
            waitingThreads.add(waitingThread);
            success = waitingThread.await(deadline);


          }
          finally
          {

            rospl.removeThread(waitingThread);
            waitingThreads.remove(waitingThread);
          }
          

          if ((!success) && (deadline != null) && (deadline.getTime() <= System.currentTimeMillis()))
          {
            throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
          }
        }
      }
    }
    finally
    {
      poolLock.unlock();
    }
    return entry;
  }
  

  public void freeEntry(BasicPoolEntry entry, boolean reusable, long validDuration, TimeUnit timeUnit)
  {
    HttpRoute route = entry.getPlannedRoute();
    if (log.isDebugEnabled()) {
      log.debug("Releasing connection [" + route + "][" + entry.getState() + "]");
    }
    

    poolLock.lock();
    try {
      if (shutdown)
      {

        closeConnection(entry);

      }
      else
      {
        leasedConnections.remove(entry);
        
        RouteSpecificPool rospl = getRoutePool(route, true);
        
        if ((reusable) && (rospl.getCapacity() >= 0)) {
          if (log.isDebugEnabled()) { String s;
            String s;
            if (validDuration > 0L) {
              s = "for " + validDuration + " " + timeUnit;
            } else {
              s = "indefinitely";
            }
            log.debug("Pooling connection [" + route + "][" + entry.getState() + "]; keep alive " + s);
          }
          
          rospl.freeEntry(entry);
          entry.updateExpiry(validDuration, timeUnit);
          freeConnections.add(entry);
        } else {
          closeConnection(entry);
          rospl.dropEntry();
          numConnections -= 1;
        }
        
        notifyWaitingThread(rospl);
      }
    } finally {
      poolLock.unlock();
    }
  }
  








  protected BasicPoolEntry getFreeEntry(RouteSpecificPool rospl, Object state)
  {
    BasicPoolEntry entry = null;
    poolLock.lock();
    try {
      boolean done = false;
      while (!done)
      {
        entry = rospl.allocEntry(state);
        
        if (entry != null) {
          if (log.isDebugEnabled()) {
            log.debug("Getting free connection [" + rospl.getRoute() + "][" + state + "]");
          }
          

          freeConnections.remove(entry);
          if (entry.isExpired(System.currentTimeMillis()))
          {

            if (log.isDebugEnabled()) {
              log.debug("Closing expired free connection [" + rospl.getRoute() + "][" + state + "]");
            }
            
            closeConnection(entry);
            


            rospl.dropEntry();
            numConnections -= 1;
          } else {
            leasedConnections.add(entry);
            done = true;
          }
        }
        else {
          done = true;
          if (log.isDebugEnabled()) {
            log.debug("No free connections [" + rospl.getRoute() + "][" + state + "]");
          }
        }
      }
    }
    finally {
      poolLock.unlock();
    }
    return entry;
  }
  












  protected BasicPoolEntry createEntry(RouteSpecificPool rospl, ClientConnectionOperator op)
  {
    if (log.isDebugEnabled()) {
      log.debug("Creating new connection [" + rospl.getRoute() + "]");
    }
    

    BasicPoolEntry entry = new BasicPoolEntry(op, rospl.getRoute(), connTTL, connTTLTimeUnit);
    
    poolLock.lock();
    try {
      rospl.createdEntry(entry);
      numConnections += 1;
      leasedConnections.add(entry);
    } finally {
      poolLock.unlock();
    }
    
    return entry;
  }
  












  protected void deleteEntry(BasicPoolEntry entry)
  {
    HttpRoute route = entry.getPlannedRoute();
    
    if (log.isDebugEnabled()) {
      log.debug("Deleting connection [" + route + "][" + entry.getState() + "]");
    }
    

    poolLock.lock();
    try
    {
      closeConnection(entry);
      
      RouteSpecificPool rospl = getRoutePool(route, true);
      rospl.deleteEntry(entry);
      numConnections -= 1;
      if (rospl.isUnused()) {
        routeToPool.remove(route);
      }
    }
    finally {
      poolLock.unlock();
    }
  }
  




  protected void deleteLeastUsedEntry()
  {
    poolLock.lock();
    try
    {
      BasicPoolEntry entry = (BasicPoolEntry)freeConnections.remove();
      
      if (entry != null) {
        deleteEntry(entry);
      } else if (log.isDebugEnabled()) {
        log.debug("No free connection to delete");
      }
    }
    finally {
      poolLock.unlock();
    }
  }
  

  protected void handleLostEntry(HttpRoute route)
  {
    poolLock.lock();
    try
    {
      RouteSpecificPool rospl = getRoutePool(route, true);
      rospl.dropEntry();
      if (rospl.isUnused()) {
        routeToPool.remove(route);
      }
      
      numConnections -= 1;
      notifyWaitingThread(rospl);
    }
    finally {
      poolLock.unlock();
    }
  }
  













  protected void notifyWaitingThread(RouteSpecificPool rospl)
  {
    WaitingThread waitingThread = null;
    
    poolLock.lock();
    try
    {
      if ((rospl != null) && (rospl.hasThread())) {
        if (log.isDebugEnabled()) {
          log.debug("Notifying thread waiting on pool [" + rospl.getRoute() + "]");
        }
        
        waitingThread = rospl.nextThread();
      } else if (!waitingThreads.isEmpty()) {
        if (log.isDebugEnabled()) {
          log.debug("Notifying thread waiting on any pool");
        }
        waitingThread = (WaitingThread)waitingThreads.remove();
      } else if (log.isDebugEnabled()) {
        log.debug("Notifying no-one, there are no waiting threads");
      }
      
      if (waitingThread != null) {
        waitingThread.wakeup();
      }
    }
    finally {
      poolLock.unlock();
    }
  }
  

  public void deleteClosedConnections()
  {
    poolLock.lock();
    try {
      Iterator<BasicPoolEntry> iter = freeConnections.iterator();
      while (iter.hasNext()) {
        BasicPoolEntry entry = (BasicPoolEntry)iter.next();
        if (!entry.getConnection().isOpen()) {
          iter.remove();
          deleteEntry(entry);
        }
      }
    } finally {
      poolLock.unlock();
    }
  }
  







  public void closeIdleConnections(long idletime, TimeUnit tunit)
  {
    Args.notNull(tunit, "Time unit");
    long t = idletime > 0L ? idletime : 0L;
    if (log.isDebugEnabled()) {
      log.debug("Closing connections idle longer than " + t + " " + tunit);
    }
    
    long deadline = System.currentTimeMillis() - tunit.toMillis(t);
    poolLock.lock();
    try {
      Iterator<BasicPoolEntry> iter = freeConnections.iterator();
      while (iter.hasNext()) {
        BasicPoolEntry entry = (BasicPoolEntry)iter.next();
        if (entry.getUpdated() <= deadline) {
          if (log.isDebugEnabled()) {
            log.debug("Closing connection last used @ " + new Date(entry.getUpdated()));
          }
          iter.remove();
          deleteEntry(entry);
        }
      }
    } finally {
      poolLock.unlock();
    }
  }
  
  public void closeExpiredConnections()
  {
    log.debug("Closing expired connections");
    long now = System.currentTimeMillis();
    
    poolLock.lock();
    try {
      Iterator<BasicPoolEntry> iter = freeConnections.iterator();
      while (iter.hasNext()) {
        BasicPoolEntry entry = (BasicPoolEntry)iter.next();
        if (entry.isExpired(now)) {
          if (log.isDebugEnabled()) {
            log.debug("Closing connection expired @ " + new Date(entry.getExpiry()));
          }
          iter.remove();
          deleteEntry(entry);
        }
      }
    } finally {
      poolLock.unlock();
    }
  }
  
  public void shutdown()
  {
    poolLock.lock();
    try {
      if (shutdown) {
        return;
      }
      shutdown = true;
      

      Iterator<BasicPoolEntry> iter1 = leasedConnections.iterator();
      while (iter1.hasNext()) {
        BasicPoolEntry entry = (BasicPoolEntry)iter1.next();
        iter1.remove();
        closeConnection(entry);
      }
      

      Iterator<BasicPoolEntry> iter2 = freeConnections.iterator();
      while (iter2.hasNext()) {
        BasicPoolEntry entry = (BasicPoolEntry)iter2.next();
        iter2.remove();
        
        if (log.isDebugEnabled()) {
          log.debug("Closing connection [" + entry.getPlannedRoute() + "][" + entry.getState() + "]");
        }
        
        closeConnection(entry);
      }
      

      Iterator<WaitingThread> iwth = waitingThreads.iterator();
      while (iwth.hasNext()) {
        WaitingThread waiter = (WaitingThread)iwth.next();
        iwth.remove();
        waiter.wakeup();
      }
      
      routeToPool.clear();
    }
    finally {
      poolLock.unlock();
    }
  }
  


  public void setMaxTotalConnections(int max)
  {
    poolLock.lock();
    try {
      maxTotalConnections = max;
    } finally {
      poolLock.unlock();
    }
  }
  



  public int getMaxTotalConnections()
  {
    return maxTotalConnections;
  }
}
