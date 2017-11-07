package org.seleniumhq.jetty9.util;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
















































public class LeakDetector<T>
  extends AbstractLifeCycle
  implements Runnable
{
  private static final Logger LOG = Log.getLogger(LeakDetector.class);
  
  private final ReferenceQueue<T> queue = new ReferenceQueue();
  private final ConcurrentMap<String, LeakDetector<T>.LeakInfo> resources = new ConcurrentHashMap();
  

  private Thread thread;
  


  public LeakDetector() {}
  


  public boolean acquired(T resource)
  {
    String id = id(resource);
    LeakDetector<T>.LeakInfo info = (LeakInfo)resources.putIfAbsent(id, new LeakInfo(resource, id, null));
    if (info != null)
    {

      return false;
    }
    
    return true;
  }
  








  public boolean released(T resource)
  {
    String id = id(resource);
    LeakDetector<T>.LeakInfo info = (LeakInfo)resources.remove(id);
    if (info != null)
    {

      return true;
    }
    

    return false;
  }
  






  public String id(T resource)
  {
    return String.valueOf(System.identityHashCode(resource));
  }
  
  protected void doStart()
    throws Exception
  {
    super.doStart();
    thread = new Thread(this, getClass().getSimpleName());
    thread.setDaemon(true);
    thread.start();
  }
  
  protected void doStop()
    throws Exception
  {
    super.doStop();
    thread.interrupt();
  }
  

  public void run()
  {
    try
    {
      while (isRunning())
      {

        LeakDetector<T>.LeakInfo leakInfo = (LeakInfo)queue.remove();
        if (LOG.isDebugEnabled())
          LOG.debug("Resource GC'ed: {}", new Object[] { leakInfo });
        if (resources.remove(id) != null) {
          leaked(leakInfo);
        }
      }
    }
    catch (InterruptedException localInterruptedException) {}
  }
  







  protected void leaked(LeakDetector<T>.LeakInfo leakInfo)
  {
    LOG.warn("Resource leaked: " + description, stackFrames);
  }
  

  public class LeakInfo
    extends PhantomReference<T>
  {
    private final String id;
    
    private final String description;
    private final Throwable stackFrames;
    
    private LeakInfo(String referent)
    {
      super(queue);
      this.id = id;
      description = referent.toString();
      stackFrames = new Throwable();
    }
    



    public String getResourceDescription()
    {
      return description;
    }
    



    public Throwable getStackFrames()
    {
      return stackFrames;
    }
    

    public String toString()
    {
      return description;
    }
  }
}
