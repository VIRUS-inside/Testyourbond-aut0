package org.apache.http.pool;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;














































@Contract(threading=ThreadingBehavior.SAFE_CONDITIONAL)
public abstract class AbstractConnPool<T, C, E extends PoolEntry<T, C>>
  implements ConnPool<T, E>, ConnPoolControl<T>
{
  private final Lock lock;
  private final Condition condition;
  private final ConnFactory<T, C> connFactory;
  private final Map<T, RouteSpecificPool<T, C, E>> routeToPool;
  private final Set<E> leased;
  private final LinkedList<E> available;
  private final LinkedList<Future<E>> pending;
  private final Map<T, Integer> maxPerRoute;
  private volatile boolean isShutDown;
  private volatile int defaultMaxPerRoute;
  private volatile int maxTotal;
  private volatile int validateAfterInactivity;
  
  public AbstractConnPool(ConnFactory<T, C> connFactory, int defaultMaxPerRoute, int maxTotal)
  {
    this.connFactory = ((ConnFactory)Args.notNull(connFactory, "Connection factory"));
    this.defaultMaxPerRoute = Args.positive(defaultMaxPerRoute, "Max per route value");
    this.maxTotal = Args.positive(maxTotal, "Max total value");
    lock = new ReentrantLock();
    condition = lock.newCondition();
    routeToPool = new HashMap();
    leased = new HashSet();
    available = new LinkedList();
    pending = new LinkedList();
    maxPerRoute = new HashMap();
  }
  




  protected abstract E createEntry(T paramT, C paramC);
  




  protected void onLease(E entry) {}
  



  protected void onRelease(E entry) {}
  



  protected void onReuse(E entry) {}
  



  protected boolean validate(E entry)
  {
    return true;
  }
  
  public boolean isShutdown() {
    return isShutDown;
  }
  

  public void shutdown()
    throws IOException
  {
    if (isShutDown) {
      return;
    }
    isShutDown = true;
    lock.lock();
    try {
      for (E entry : available) {
        entry.close();
      }
      for (E entry : leased) {
        entry.close();
      }
      for (RouteSpecificPool<T, C, E> pool : routeToPool.values()) {
        pool.shutdown();
      }
      routeToPool.clear();
      leased.clear();
      available.clear();
    } finally {
      lock.unlock();
    }
  }
  
  private RouteSpecificPool<T, C, E> getPool(final T route) {
    RouteSpecificPool<T, C, E> pool = (RouteSpecificPool)routeToPool.get(route);
    if (pool == null) {
      pool = new RouteSpecificPool(route)
      {
        protected E createEntry(C conn)
        {
          return createEntry(route, conn);
        }
        
      };
      routeToPool.put(route, pool);
    }
    return pool;
  }
  








  public Future<E> lease(final T route, final Object state, final FutureCallback<E> callback)
  {
    Args.notNull(route, "Route");
    Asserts.check(!isShutDown, "Connection pool shut down");
    
    new Future()
    {
      private volatile boolean cancelled;
      private volatile boolean done;
      private volatile E entry;
      
      public boolean cancel(boolean mayInterruptIfRunning)
      {
        cancelled = true;
        lock.lock();
        try {
          condition.signalAll();
        } finally {
          lock.unlock();
        }
        synchronized (this) {
          boolean result = !done;
          done = true;
          if (callback != null) {
            callback.cancelled();
          }
          return result;
        }
      }
      
      public boolean isCancelled()
      {
        return cancelled;
      }
      
      public boolean isDone()
      {
        return done;
      }
      
      public E get() throws InterruptedException, ExecutionException
      {
        try {
          return get(0L, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex) {
          throw new ExecutionException(ex);
        }
      }
      
      public E get(long timeout, TimeUnit tunit) throws InterruptedException, ExecutionException, TimeoutException
      {
        if (entry != null) {
          return entry;
        }
        synchronized (this) {
          try {
            E leasedEntry;
            for (;;) { leasedEntry = AbstractConnPool.this.getPoolEntryBlocking(route, state, timeout, tunit, this);
              if ((validateAfterInactivity <= 0) || 
                (leasedEntry.getUpdated() + validateAfterInactivity > System.currentTimeMillis()) || 
                (validate(leasedEntry))) break;
              leasedEntry.close();
              release(leasedEntry, false);
            }
            


            entry = leasedEntry;
            done = true;
            onLease(entry);
            if (callback != null) {
              callback.completed(entry);
            }
            return entry;
          }
          catch (IOException ex) {
            done = true;
            if (callback != null) {
              callback.failed(ex);
            }
            throw new ExecutionException(ex);
          }
        }
      }
    };
  }
  
















  public Future<E> lease(T route, Object state)
  {
    return lease(route, state, null);
  }
  


  private E getPoolEntryBlocking(T route, Object state, long timeout, TimeUnit tunit, Future<E> future)
    throws IOException, InterruptedException, TimeoutException
  {
    Date deadline = null;
    if (timeout > 0L) {
      deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
    }
    lock.lock();
    try {
      RouteSpecificPool<T, C, E> pool = getPool(route);
      for (;;)
      {
        Asserts.check(!isShutDown, "Connection pool shut down");
        E entry;
        for (;;) { entry = pool.getFree(state);
          if (entry == null) {
            break;
          }
          if (entry.isExpired(System.currentTimeMillis())) {
            entry.close();
          }
          if (!entry.isClosed()) break;
          available.remove(entry);
          pool.free(entry, false);
        }
        


        if (entry != null) {
          available.remove(entry);
          leased.add(entry);
          onReuse(entry);
          return entry;
        }
        

        int maxPerRoute = getMax(route);
        
        int excess = Math.max(0, pool.getAllocatedCount() + 1 - maxPerRoute);
        if (excess > 0) {
          for (int i = 0; i < excess; i++) {
            E lastUsed = pool.getLastUsed();
            if (lastUsed == null) {
              break;
            }
            lastUsed.close();
            available.remove(lastUsed);
            pool.remove(lastUsed);
          }
        }
        
        if (pool.getAllocatedCount() < maxPerRoute) {
          int totalUsed = leased.size();
          int freeCapacity = Math.max(maxTotal - totalUsed, 0);
          if (freeCapacity > 0) {
            int totalAvailable = available.size();
            RouteSpecificPool<T, C, E> otherpool; if ((totalAvailable > freeCapacity - 1) && 
              (!available.isEmpty())) {
              E lastUsed = (PoolEntry)available.removeLast();
              lastUsed.close();
              otherpool = getPool(lastUsed.getRoute());
              otherpool.remove(lastUsed);
            }
            
            C conn = connFactory.create(route);
            entry = pool.add(conn);
            leased.add(entry);
            return entry;
          }
        }
        
        boolean success = false;
        try {
          if (future.isCancelled()) {
            throw new InterruptedException("Operation interrupted");
          }
          pool.queue(future);
          pending.add(future);
          if (deadline != null) {
            success = condition.awaitUntil(deadline);
          } else {
            condition.await();
            success = true;
          }
          if (future.isCancelled()) {
            throw new InterruptedException("Operation interrupted");
          }
          

        }
        finally
        {
          pool.unqueue(future);
          pending.remove(future);
        }
        
        if ((!success) && (deadline != null) && (deadline.getTime() <= System.currentTimeMillis())) {
          break;
        }
      }
      throw new TimeoutException("Timeout waiting for connection");
    } finally {
      lock.unlock();
    }
  }
  
  public void release(E entry, boolean reusable)
  {
    lock.lock();
    try {
      if (leased.remove(entry)) {
        RouteSpecificPool<T, C, E> pool = getPool(entry.getRoute());
        pool.free(entry, reusable);
        if ((reusable) && (!isShutDown)) {
          available.addFirst(entry);
        } else {
          entry.close();
        }
        onRelease(entry);
        Future<E> future = pool.nextPending();
        if (future != null) {
          pending.remove(future);
        } else {
          future = (Future)pending.poll();
        }
        if (future != null) {
          condition.signalAll();
        }
      }
    } finally {
      lock.unlock();
    }
  }
  
  private int getMax(T route) {
    Integer v = (Integer)maxPerRoute.get(route);
    if (v != null) {
      return v.intValue();
    }
    return defaultMaxPerRoute;
  }
  

  public void setMaxTotal(int max)
  {
    Args.positive(max, "Max value");
    lock.lock();
    try {
      maxTotal = max;
    } finally {
      lock.unlock();
    }
  }
  
  public int getMaxTotal()
  {
    lock.lock();
    try {
      return maxTotal;
    } finally {
      lock.unlock();
    }
  }
  
  public void setDefaultMaxPerRoute(int max)
  {
    Args.positive(max, "Max per route value");
    lock.lock();
    try {
      defaultMaxPerRoute = max;
    } finally {
      lock.unlock();
    }
  }
  
  public int getDefaultMaxPerRoute()
  {
    lock.lock();
    try {
      return defaultMaxPerRoute;
    } finally {
      lock.unlock();
    }
  }
  
  public void setMaxPerRoute(T route, int max)
  {
    Args.notNull(route, "Route");
    Args.positive(max, "Max per route value");
    lock.lock();
    try {
      maxPerRoute.put(route, Integer.valueOf(max));
    } finally {
      lock.unlock();
    }
  }
  
  public int getMaxPerRoute(T route)
  {
    Args.notNull(route, "Route");
    lock.lock();
    try {
      return getMax(route);
    } finally {
      lock.unlock();
    }
  }
  
  public PoolStats getTotalStats()
  {
    lock.lock();
    try {
      return new PoolStats(leased.size(), pending.size(), available.size(), maxTotal);

    }
    finally
    {

      lock.unlock();
    }
  }
  
  public PoolStats getStats(T route)
  {
    Args.notNull(route, "Route");
    lock.lock();
    try {
      RouteSpecificPool<T, C, E> pool = getPool(route);
      return new PoolStats(pool.getLeasedCount(), pool.getPendingCount(), pool.getAvailableCount(), getMax(route));

    }
    finally
    {

      lock.unlock();
    }
  }
  





  public Set<T> getRoutes()
  {
    lock.lock();
    try {
      return new HashSet(routeToPool.keySet());
    } finally {
      lock.unlock();
    }
  }
  




  protected void enumAvailable(PoolEntryCallback<T, C> callback)
  {
    lock.lock();
    try {
      Iterator<E> it = available.iterator();
      while (it.hasNext()) {
        E entry = (PoolEntry)it.next();
        callback.process(entry);
        if (entry.isClosed()) {
          RouteSpecificPool<T, C, E> pool = getPool(entry.getRoute());
          pool.remove(entry);
          it.remove();
        }
      }
      purgePoolMap();
    } finally {
      lock.unlock();
    }
  }
  




  protected void enumLeased(PoolEntryCallback<T, C> callback)
  {
    lock.lock();
    try {
      Iterator<E> it = leased.iterator();
      while (it.hasNext()) {
        E entry = (PoolEntry)it.next();
        callback.process(entry);
      }
    } finally {
      lock.unlock();
    }
  }
  
  private void purgePoolMap() {
    Iterator<Map.Entry<T, RouteSpecificPool<T, C, E>>> it = routeToPool.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<T, RouteSpecificPool<T, C, E>> entry = (Map.Entry)it.next();
      RouteSpecificPool<T, C, E> pool = (RouteSpecificPool)entry.getValue();
      if (pool.getPendingCount() + pool.getAllocatedCount() == 0) {
        it.remove();
      }
    }
  }
  






  public void closeIdle(long idletime, TimeUnit tunit)
  {
    Args.notNull(tunit, "Time unit");
    long time = tunit.toMillis(idletime);
    if (time < 0L) {
      time = 0L;
    }
    final long deadline = System.currentTimeMillis() - time;
    enumAvailable(new PoolEntryCallback()
    {
      public void process(PoolEntry<T, C> entry)
      {
        if (entry.getUpdated() <= deadline) {
          entry.close();
        }
      }
    });
  }
  



  public void closeExpired()
  {
    final long now = System.currentTimeMillis();
    enumAvailable(new PoolEntryCallback()
    {
      public void process(PoolEntry<T, C> entry)
      {
        if (entry.isExpired(now)) {
          entry.close();
        }
      }
    });
  }
  




  public int getValidateAfterInactivity()
  {
    return validateAfterInactivity;
  }
  



  public void setValidateAfterInactivity(int ms)
  {
    validateAfterInactivity = ms;
  }
  
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();
    buffer.append("[leased: ");
    buffer.append(leased);
    buffer.append("][available: ");
    buffer.append(available);
    buffer.append("][pending: ");
    buffer.append(pending);
    buffer.append("]");
    return buffer.toString();
  }
}
