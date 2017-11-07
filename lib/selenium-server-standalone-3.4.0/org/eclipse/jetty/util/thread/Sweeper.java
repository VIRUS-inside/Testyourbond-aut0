package org.eclipse.jetty.util.thread;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;





























































public class Sweeper
  extends AbstractLifeCycle
  implements Runnable
{
  private static final Logger LOG = Log.getLogger(Sweeper.class);
  
  private final AtomicReference<List<Sweepable>> items = new AtomicReference();
  private final AtomicReference<Scheduler.Task> task = new AtomicReference();
  private final Scheduler scheduler;
  private final long period;
  
  public Sweeper(Scheduler scheduler, long period)
  {
    this.scheduler = scheduler;
    this.period = period;
  }
  
  protected void doStart()
    throws Exception
  {
    super.doStart();
    items.set(new CopyOnWriteArrayList());
    activate();
  }
  
  protected void doStop()
    throws Exception
  {
    deactivate();
    items.set(null);
    super.doStop();
  }
  
  public int getSize()
  {
    List<Sweepable> refs = (List)items.get();
    return refs == null ? 0 : refs.size();
  }
  
  public boolean offer(Sweepable sweepable)
  {
    List<Sweepable> refs = (List)items.get();
    if (refs == null)
      return false;
    refs.add(sweepable);
    if (LOG.isDebugEnabled())
      LOG.debug("Resource offered {}", new Object[] { sweepable });
    return true;
  }
  
  public boolean remove(Sweepable sweepable)
  {
    List<Sweepable> refs = (List)items.get();
    return (refs != null) && (refs.remove(sweepable));
  }
  

  public void run()
  {
    List<Sweepable> refs = (List)items.get();
    if (refs == null)
      return;
    for (Sweepable sweepable : refs)
    {
      try
      {
        if (sweepable.sweep())
        {
          refs.remove(sweepable);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Resource swept {}", new Object[] { sweepable });
          }
        }
      }
      catch (Throwable x) {
        LOG.info("Exception while sweeping " + sweepable, x);
      }
    }
    activate();
  }
  
  private void activate()
  {
    if (isRunning())
    {
      Scheduler.Task t = scheduler.schedule(this, period, TimeUnit.MILLISECONDS);
      if (LOG.isDebugEnabled())
        LOG.debug("Scheduled in {} ms sweep task {}", new Object[] { Long.valueOf(period), t });
      task.set(t);


    }
    else if (LOG.isDebugEnabled()) {
      LOG.debug("Skipping sweep task scheduling", new Object[0]);
    }
  }
  
  private void deactivate()
  {
    Scheduler.Task t = (Scheduler.Task)task.getAndSet(null);
    if (t != null)
    {
      boolean cancelled = t.cancel();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Cancelled ({}) sweep task {}", new Object[] { Boolean.valueOf(cancelled), t });
      }
    }
  }
  
  public static abstract interface Sweepable
  {
    public abstract boolean sweep();
  }
}
