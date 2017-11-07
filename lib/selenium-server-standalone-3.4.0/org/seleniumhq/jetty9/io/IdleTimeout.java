package org.seleniumhq.jetty9.io;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Scheduler;
import org.seleniumhq.jetty9.util.thread.Scheduler.Task;



























public abstract class IdleTimeout
{
  private static final Logger LOG = Log.getLogger(IdleTimeout.class);
  private final Scheduler _scheduler;
  private final AtomicReference<Scheduler.Task> _timeout = new AtomicReference();
  private volatile long _idleTimeout;
  private volatile long _idleTimestamp = System.currentTimeMillis();
  
  private final Runnable _idleTask = new Runnable()
  {

    public void run()
    {
      long idleLeft = checkIdleTimeout();
      if (idleLeft >= 0L) {
        IdleTimeout.this.scheduleIdleTimeout(idleLeft > 0L ? idleLeft : getIdleTimeout());
      }
    }
  };
  


  public IdleTimeout(Scheduler scheduler)
  {
    _scheduler = scheduler;
  }
  
  public Scheduler getScheduler()
  {
    return _scheduler;
  }
  
  public long getIdleTimestamp()
  {
    return _idleTimestamp;
  }
  
  public long getIdleFor()
  {
    return System.currentTimeMillis() - getIdleTimestamp();
  }
  
  public long getIdleTimeout()
  {
    return _idleTimeout;
  }
  
  public void setIdleTimeout(long idleTimeout)
  {
    long old = _idleTimeout;
    _idleTimeout = idleTimeout;
    

    if (old > 0L)
    {

      if (old <= idleTimeout) {
        return;
      }
      
      deactivate();
    }
    

    if (isOpen()) {
      activate();
    }
  }
  


  public void notIdle()
  {
    _idleTimestamp = System.currentTimeMillis();
  }
  
  private void scheduleIdleTimeout(long delay)
  {
    Scheduler.Task newTimeout = null;
    if ((isOpen()) && (delay > 0L) && (_scheduler != null))
      newTimeout = _scheduler.schedule(_idleTask, delay, TimeUnit.MILLISECONDS);
    Scheduler.Task oldTimeout = (Scheduler.Task)_timeout.getAndSet(newTimeout);
    if (oldTimeout != null) {
      oldTimeout.cancel();
    }
  }
  
  public void onOpen() {
    activate();
  }
  
  private void activate()
  {
    if (_idleTimeout > 0L) {
      _idleTask.run();
    }
  }
  
  public void onClose() {
    deactivate();
  }
  
  private void deactivate()
  {
    Scheduler.Task oldTimeout = (Scheduler.Task)_timeout.getAndSet(null);
    if (oldTimeout != null) {
      oldTimeout.cancel();
    }
  }
  
  protected long checkIdleTimeout() {
    if (isOpen())
    {
      long idleTimestamp = getIdleTimestamp();
      long idleTimeout = getIdleTimeout();
      long idleElapsed = System.currentTimeMillis() - idleTimestamp;
      long idleLeft = idleTimeout - idleElapsed;
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} idle timeout check, elapsed: {} ms, remaining: {} ms", new Object[] { this, Long.valueOf(idleElapsed), Long.valueOf(idleLeft) });
      }
      if ((idleTimestamp != 0L) && (idleTimeout > 0L))
      {
        if (idleLeft <= 0L)
        {
          if (LOG.isDebugEnabled()) {
            LOG.debug("{} idle timeout expired", new Object[] { this });
          }
          try {
            onIdleExpired(new TimeoutException("Idle timeout expired: " + idleElapsed + "/" + idleTimeout + " ms"));
          }
          finally
          {
            notIdle();
          }
        }
      }
      
      return idleLeft >= 0L ? idleLeft : 0L;
    }
    return -1L;
  }
  
  protected abstract void onIdleExpired(TimeoutException paramTimeoutException);
  
  public abstract boolean isOpen();
}
