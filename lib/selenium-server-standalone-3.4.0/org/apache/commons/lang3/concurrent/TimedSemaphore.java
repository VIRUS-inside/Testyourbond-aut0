package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.Validate;











































































































































































public class TimedSemaphore
{
  public static final int NO_LIMIT = 0;
  private static final int THREAD_POOL_SIZE = 1;
  private final ScheduledExecutorService executorService;
  private final long period;
  private final TimeUnit unit;
  private final boolean ownExecutor;
  private ScheduledFuture<?> task;
  private long totalAcquireCount;
  private long periodCount;
  private int limit;
  private int acquireCount;
  private int lastCallsPerPeriod;
  private boolean shutdown;
  
  public TimedSemaphore(long timePeriod, TimeUnit timeUnit, int limit)
  {
    this(null, timePeriod, timeUnit, limit);
  }
  












  public TimedSemaphore(ScheduledExecutorService service, long timePeriod, TimeUnit timeUnit, int limit)
  {
    Validate.inclusiveBetween(1L, Long.MAX_VALUE, timePeriod, "Time period must be greater than 0!");
    
    period = timePeriod;
    unit = timeUnit;
    
    if (service != null) {
      executorService = service;
      ownExecutor = false;
    } else {
      ScheduledThreadPoolExecutor s = new ScheduledThreadPoolExecutor(1);
      
      s.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
      s.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
      executorService = s;
      ownExecutor = true;
    }
    
    setLimit(limit);
  }
  






  public final synchronized int getLimit()
  {
    return limit;
  }
  









  public final synchronized void setLimit(int limit)
  {
    this.limit = limit;
  }
  




  public synchronized void shutdown()
  {
    if (!shutdown)
    {
      if (ownExecutor)
      {

        getExecutorService().shutdownNow();
      }
      if (task != null) {
        task.cancel(false);
      }
      
      shutdown = true;
    }
  }
  






  public synchronized boolean isShutdown()
  {
    return shutdown;
  }
  









  public synchronized void acquire()
    throws InterruptedException
  {
    prepareAcquire();
    boolean canPass;
    do
    {
      canPass = acquirePermit();
      if (!canPass) {
        wait();
      }
    } while (!canPass);
  }
  










  public synchronized boolean tryAcquire()
  {
    prepareAcquire();
    return acquirePermit();
  }
  









  public synchronized int getLastAcquiresPerPeriod()
  {
    return lastCallsPerPeriod;
  }
  





  public synchronized int getAcquireCount()
  {
    return acquireCount;
  }
  










  public synchronized int getAvailablePermits()
  {
    return getLimit() - getAcquireCount();
  }
  








  public synchronized double getAverageCallsPerPeriod()
  {
    return periodCount == 0L ? 0.0D : totalAcquireCount / periodCount;
  }
  







  public long getPeriod()
  {
    return period;
  }
  




  public TimeUnit getUnit()
  {
    return unit;
  }
  




  protected ScheduledExecutorService getExecutorService()
  {
    return executorService;
  }
  






  protected ScheduledFuture<?> startTimer()
  {
    getExecutorService().scheduleAtFixedRate(new Runnable()
    {
      public void run() {
        endOfPeriod();
      }
    }, getPeriod(), getPeriod(), getUnit());
  }
  




  synchronized void endOfPeriod()
  {
    lastCallsPerPeriod = acquireCount;
    totalAcquireCount += acquireCount;
    periodCount += 1L;
    acquireCount = 0;
    notifyAll();
  }
  



  private void prepareAcquire()
  {
    if (isShutdown()) {
      throw new IllegalStateException("TimedSemaphore is shut down!");
    }
    
    if (task == null) {
      task = startTimer();
    }
  }
  







  private boolean acquirePermit()
  {
    if ((getLimit() <= 0) || (acquireCount < getLimit())) {
      acquireCount += 1;
      return true;
    }
    return false;
  }
}
