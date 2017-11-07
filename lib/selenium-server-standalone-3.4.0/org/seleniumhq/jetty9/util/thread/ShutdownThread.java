package org.seleniumhq.jetty9.util.thread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.seleniumhq.jetty9.util.component.Destroyable;
import org.seleniumhq.jetty9.util.component.LifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;



























public class ShutdownThread
  extends Thread
{
  private static final Logger LOG = Log.getLogger(ShutdownThread.class);
  private static final ShutdownThread _thread = new ShutdownThread();
  
  private boolean _hooked;
  private final List<LifeCycle> _lifeCycles = new CopyOnWriteArrayList();
  





  private ShutdownThread() {}
  




  private synchronized void hook()
  {
    try
    {
      if (!_hooked)
        Runtime.getRuntime().addShutdownHook(this);
      _hooked = true;
    }
    catch (Exception e)
    {
      LOG.ignore(e);
      LOG.info("shutdown already commenced", new Object[0]);
    }
  }
  

  private synchronized void unhook()
  {
    try
    {
      _hooked = false;
      Runtime.getRuntime().removeShutdownHook(this);
    }
    catch (Exception e)
    {
      LOG.ignore(e);
      LOG.debug("shutdown already commenced", new Object[0]);
    }
  }
  






  public static ShutdownThread getInstance()
  {
    return _thread;
  }
  

  public static synchronized void register(LifeCycle... lifeCycles)
  {
    _thread_lifeCycles.addAll(Arrays.asList(lifeCycles));
    if (_thread_lifeCycles.size() > 0) {
      _thread.hook();
    }
  }
  
  public static synchronized void register(int index, LifeCycle... lifeCycles)
  {
    _thread_lifeCycles.addAll(index, Arrays.asList(lifeCycles));
    if (_thread_lifeCycles.size() > 0) {
      _thread.hook();
    }
  }
  
  public static synchronized void deregister(LifeCycle lifeCycle)
  {
    _thread_lifeCycles.remove(lifeCycle);
    if (_thread_lifeCycles.size() == 0) {
      _thread.unhook();
    }
  }
  
  public static synchronized boolean isRegistered(LifeCycle lifeCycle)
  {
    return _thread_lifeCycles.contains(lifeCycle);
  }
  


  public void run()
  {
    for (LifeCycle lifeCycle : _thread_lifeCycles)
    {
      try
      {
        if (lifeCycle.isStarted())
        {
          lifeCycle.stop();
          LOG.debug("Stopped {}", new Object[] { lifeCycle });
        }
        
        if ((lifeCycle instanceof Destroyable))
        {
          ((Destroyable)lifeCycle).destroy();
          LOG.debug("Destroyed {}", new Object[] { lifeCycle });
        }
      }
      catch (Exception ex)
      {
        LOG.debug(ex);
      }
    }
  }
}
