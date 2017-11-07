package org.seleniumhq.jetty9.util.component;

import java.util.concurrent.CopyOnWriteArrayList;
import org.seleniumhq.jetty9.util.Uptime;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;






















@ManagedObject("Abstract Implementation of LifeCycle")
public abstract class AbstractLifeCycle
  implements LifeCycle
{
  private static final Logger LOG = Log.getLogger(AbstractLifeCycle.class);
  
  public static final String STOPPED = "STOPPED";
  
  public static final String FAILED = "FAILED";
  public static final String STARTING = "STARTING";
  public static final String STARTED = "STARTED";
  public static final String STOPPING = "STOPPING";
  public static final String RUNNING = "RUNNING";
  private final CopyOnWriteArrayList<LifeCycle.Listener> _listeners = new CopyOnWriteArrayList();
  private final Object _lock = new Object();
  private final int __FAILED = -1; private final int __STOPPED = 0; private final int __STARTING = 1; private final int __STARTED = 2; private final int __STOPPING = 3;
  private volatile int _state = 0;
  private long _stopTimeout = 30000L;
  
  public AbstractLifeCycle() {}
  
  protected void doStart()
    throws Exception
  {}
  
  protected void doStop() throws Exception
  {}
  
  public final void start() throws Exception
  {
    synchronized (_lock)
    {
      try
      {
        if ((_state == 2) || (_state == 1))
          return;
        setStarting();
        doStart();
        setStarted();
      }
      catch (Throwable e)
      {
        setFailed(e);
        throw e;
      }
    }
  }
  
  public final void stop()
    throws Exception
  {
    synchronized (_lock)
    {
      try
      {
        if ((_state == 3) || (_state == 0))
          return;
        setStopping();
        doStop();
        setStopped();
      }
      catch (Throwable e)
      {
        setFailed(e);
        throw e;
      }
    }
  }
  

  public boolean isRunning()
  {
    int state = _state;
    
    return (state == 2) || (state == 1);
  }
  

  public boolean isStarted()
  {
    return _state == 2;
  }
  

  public boolean isStarting()
  {
    return _state == 1;
  }
  

  public boolean isStopping()
  {
    return _state == 3;
  }
  

  public boolean isStopped()
  {
    return _state == 0;
  }
  

  public boolean isFailed()
  {
    return _state == -1;
  }
  

  public void addLifeCycleListener(LifeCycle.Listener listener)
  {
    _listeners.add(listener);
  }
  

  public void removeLifeCycleListener(LifeCycle.Listener listener)
  {
    _listeners.remove(listener);
  }
  
  @ManagedAttribute(value="Lifecycle State for this instance", readonly=true)
  public String getState()
  {
    switch (_state) {
    case -1: 
      return "FAILED";
    case 1:  return "STARTING";
    case 2:  return "STARTED";
    case 3:  return "STOPPING";
    case 0:  return "STOPPED";
    }
    return null;
  }
  
  public static String getState(LifeCycle lc)
  {
    if (lc.isStarting()) return "STARTING";
    if (lc.isStarted()) return "STARTED";
    if (lc.isStopping()) return "STOPPING";
    if (lc.isStopped()) return "STOPPED";
    return "FAILED";
  }
  
  private void setStarted()
  {
    _state = 2;
    if (LOG.isDebugEnabled())
      LOG.debug("STARTED @{}ms {}", new Object[] { Long.valueOf(Uptime.getUptime()), this });
    for (LifeCycle.Listener listener : _listeners) {
      listener.lifeCycleStarted(this);
    }
  }
  
  private void setStarting() {
    if (LOG.isDebugEnabled())
      LOG.debug("starting {}", new Object[] { this });
    _state = 1;
    for (LifeCycle.Listener listener : _listeners) {
      listener.lifeCycleStarting(this);
    }
  }
  
  private void setStopping() {
    if (LOG.isDebugEnabled())
      LOG.debug("stopping {}", new Object[] { this });
    _state = 3;
    for (LifeCycle.Listener listener : _listeners) {
      listener.lifeCycleStopping(this);
    }
  }
  
  private void setStopped() {
    _state = 0;
    if (LOG.isDebugEnabled())
      LOG.debug("{} {}", new Object[] { "STOPPED", this });
    for (LifeCycle.Listener listener : _listeners) {
      listener.lifeCycleStopped(this);
    }
  }
  
  private void setFailed(Throwable th) {
    _state = -1;
    if (LOG.isDebugEnabled())
      LOG.warn("FAILED " + this + ": " + th, th);
    for (LifeCycle.Listener listener : _listeners) {
      listener.lifeCycleFailure(this, th);
    }
  }
  
  @ManagedAttribute("The stop timeout in milliseconds")
  public long getStopTimeout() {
    return _stopTimeout;
  }
  
  public void setStopTimeout(long stopTimeout)
  {
    _stopTimeout = stopTimeout;
  }
  
  public static abstract class AbstractLifeCycleListener
    implements LifeCycle.Listener
  {
    public AbstractLifeCycleListener() {}
    
    public void lifeCycleFailure(LifeCycle event, Throwable cause) {}
    
    public void lifeCycleStarted(LifeCycle event) {}
    
    public void lifeCycleStarting(LifeCycle event) {}
    
    public void lifeCycleStopped(LifeCycle event) {}
    
    public void lifeCycleStopping(LifeCycle event) {}
  }
}
