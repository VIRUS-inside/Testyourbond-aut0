package org.seleniumhq.jetty9.util.thread;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;






















public class TimerScheduler
  extends AbstractLifeCycle
  implements Scheduler, Runnable
{
  private static final Logger LOG = Log.getLogger(TimerScheduler.class);
  

  private final String _name;
  

  private final boolean _daemon;
  

  private Timer _timer;
  


  public TimerScheduler()
  {
    this(null, false);
  }
  
  public TimerScheduler(String name, boolean daemon)
  {
    _name = name;
    _daemon = daemon;
  }
  
  protected void doStart()
    throws Exception
  {
    _timer = (_name == null ? new Timer() : new Timer(_name, _daemon));
    run();
    super.doStart();
  }
  
  protected void doStop()
    throws Exception
  {
    _timer.cancel();
    super.doStop();
    _timer = null;
  }
  

  public Scheduler.Task schedule(Runnable task, long delay, TimeUnit units)
  {
    Timer timer = _timer;
    if (timer == null)
      throw new RejectedExecutionException("STOPPED: " + this);
    SimpleTask t = new SimpleTask(task, null);
    timer.schedule(t, units.toMillis(delay));
    return t;
  }
  

  public void run()
  {
    Timer timer = _timer;
    if (timer != null)
    {
      timer.purge();
      schedule(this, 1L, TimeUnit.SECONDS);
    }
  }
  
  private static class SimpleTask extends TimerTask implements Scheduler.Task
  {
    private final Runnable _task;
    
    private SimpleTask(Runnable runnable)
    {
      _task = runnable;
    }
    

    public void run()
    {
      try
      {
        _task.run();
      }
      catch (Throwable x)
      {
        TimerScheduler.LOG.warn("Exception while executing task " + _task, x);
      }
    }
    

    public String toString()
    {
      return String.format("%s.%s@%x", new Object[] {TimerScheduler.class
        .getSimpleName(), SimpleTask.class
        .getSimpleName(), 
        Integer.valueOf(hashCode()) });
    }
  }
}
