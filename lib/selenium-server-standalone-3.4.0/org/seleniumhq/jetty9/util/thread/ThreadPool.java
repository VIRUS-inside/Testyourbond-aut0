package org.seleniumhq.jetty9.util.thread;

import java.util.concurrent.Executor;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;

@ManagedObject("Pool of Threads")
public abstract interface ThreadPool
  extends Executor
{
  public abstract void join()
    throws InterruptedException;
  
  @ManagedAttribute("number of threads in pool")
  public abstract int getThreads();
  
  @ManagedAttribute("number of idle threads in pool")
  public abstract int getIdleThreads();
  
  @ManagedAttribute("indicates the pool is low on available threads")
  public abstract boolean isLowOnThreads();
  
  public static abstract interface SizedThreadPool
    extends ThreadPool
  {
    public abstract int getMinThreads();
    
    public abstract int getMaxThreads();
    
    public abstract void setMinThreads(int paramInt);
    
    public abstract void setMaxThreads(int paramInt);
  }
}
