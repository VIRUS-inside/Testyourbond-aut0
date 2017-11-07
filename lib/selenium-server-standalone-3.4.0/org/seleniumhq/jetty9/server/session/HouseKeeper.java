package org.seleniumhq.jetty9.server.session;

import java.util.concurrent.TimeUnit;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.server.SessionIdManager;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.ScheduledExecutorScheduler;
import org.seleniumhq.jetty9.util.thread.Scheduler;
import org.seleniumhq.jetty9.util.thread.Scheduler.Task;
























public class HouseKeeper
  extends AbstractLifeCycle
{
  private static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  
  public static final long DEFAULT_PERIOD_MS = 600000L;
  protected SessionIdManager _sessionIdManager;
  protected Scheduler _scheduler;
  protected Scheduler.Task _task;
  protected Runner _runner;
  protected boolean _ownScheduler = false;
  private long _intervalMs = 600000L;
  

  public HouseKeeper() {}
  


  protected class Runner
    implements Runnable
  {
    protected Runner() {}
    


    public void run()
    {
      try
      {
        scavenge();
        


        if ((_scheduler != null) && (_scheduler.isRunning())) {
          _task = _scheduler.schedule(this, _intervalMs, TimeUnit.MILLISECONDS);
        }
      }
      finally
      {
        if ((_scheduler != null) && (_scheduler.isRunning())) {
          _task = _scheduler.schedule(this, _intervalMs, TimeUnit.MILLISECONDS);
        }
      }
    }
  }
  






  public void setSessionIdManager(SessionIdManager sessionIdManager)
  {
    _sessionIdManager = sessionIdManager;
  }
  




  protected void doStart()
    throws Exception
  {
    if (_sessionIdManager == null) {
      throw new IllegalStateException("No SessionIdManager for Housekeeper");
    }
    setIntervalSec(getIntervalSec());
    
    super.doStart();
  }
  






  protected void findScheduler()
    throws Exception
  {
    if (_scheduler == null)
    {
      if ((_sessionIdManager instanceof DefaultSessionIdManager))
      {

        _scheduler = ((Scheduler)((DefaultSessionIdManager)_sessionIdManager).getServer().getBean(Scheduler.class));
      }
      
      if (_scheduler == null)
      {
        _scheduler = new ScheduledExecutorScheduler();
        _ownScheduler = true;
        _scheduler.start();
        if (LOG.isDebugEnabled()) LOG.debug("Using own scheduler for scavenging", new Object[0]);
      }
      else if (!_scheduler.isStarted()) {
        throw new IllegalStateException("Shared scheduler not started");
      }
    }
  }
  


  protected void startScavenging()
    throws Exception
  {
    synchronized (this)
    {
      if (_scheduler != null)
      {

        if (_task != null)
          _task.cancel();
        if (_runner == null)
          _runner = new Runner();
        LOG.info("Scavenging every {}ms", new Object[] { Long.valueOf(_intervalMs) });
        _task = _scheduler.schedule(_runner, _intervalMs, TimeUnit.MILLISECONDS);
      }
    }
  }
  




  protected void stopScavenging()
    throws Exception
  {
    synchronized (this)
    {
      if (_task != null)
      {
        _task.cancel();
        LOG.info("Stopped scavenging", new Object[0]);
      }
      _task = null;
      if (_ownScheduler)
      {
        _scheduler.stop();
        _scheduler = null;
      }
    }
    _runner = null;
  }
  




  protected void doStop()
    throws Exception
  {
    synchronized (this)
    {
      stopScavenging();
      _scheduler = null;
    }
    super.doStop();
  }
  





  public void setIntervalSec(long sec)
    throws Exception
  {
    if ((isStarted()) || (isStarting()))
    {
      if (sec <= 0L)
      {
        _intervalMs = 0L;
        LOG.info("Scavenging disabled", new Object[0]);
        stopScavenging();
      }
      else
      {
        if (sec < 10L) {
          LOG.warn("Short interval of {}sec for session scavenging.", new Object[] { Long.valueOf(sec) });
        }
        _intervalMs = (sec * 1000L);
        


        long tenPercent = _intervalMs / 10L;
        if (System.currentTimeMillis() % 2L == 0L) {
          _intervalMs += tenPercent;
        }
        if ((isStarting()) || (isStarted()))
        {
          findScheduler();
          startScavenging();
        }
        
      }
    }
    else {
      _intervalMs = (sec * 1000L);
    }
  }
  








  public long getIntervalSec()
  {
    return _intervalMs / 1000L;
  }
  








  public void scavenge()
  {
    if ((isStopping()) || (isStopped())) {
      return;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Scavenging sessions", new Object[0]);
    }
    
    for (SessionHandler manager : _sessionIdManager.getSessionHandlers())
    {
      if (manager != null)
      {
        try
        {
          manager.scavenge();
        }
        catch (Exception e)
        {
          LOG.warn(e);
        }
      }
    }
  }
  





  public String toString()
  {
    return super.toString() + "[interval=" + _intervalMs + ", ownscheduler=" + _ownScheduler + "]";
  }
}
