package org.seleniumhq.jetty9.util.thread;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.component.Dumpable;

























public class ScheduledExecutorScheduler
  extends AbstractLifeCycle
  implements Scheduler, Dumpable
{
  private final String name;
  private final boolean daemon;
  private final ClassLoader classloader;
  private final ThreadGroup threadGroup;
  private volatile ScheduledThreadPoolExecutor scheduler;
  private volatile Thread thread;
  
  public ScheduledExecutorScheduler()
  {
    this(null, false);
  }
  
  public ScheduledExecutorScheduler(String name, boolean daemon)
  {
    this(name, daemon, Thread.currentThread().getContextClassLoader());
  }
  
  public ScheduledExecutorScheduler(String name, boolean daemon, ClassLoader threadFactoryClassLoader)
  {
    this(name, daemon, threadFactoryClassLoader, null);
  }
  
  public ScheduledExecutorScheduler(String name, boolean daemon, ClassLoader threadFactoryClassLoader, ThreadGroup threadGroup)
  {
    this.name = (name == null ? "Scheduler-" + hashCode() : name);
    this.daemon = daemon;
    classloader = (threadFactoryClassLoader == null ? Thread.currentThread().getContextClassLoader() : threadFactoryClassLoader);
    this.threadGroup = threadGroup;
  }
  
  protected void doStart()
    throws Exception
  {
    scheduler = new ScheduledThreadPoolExecutor(1, new ThreadFactory()
    {

      public Thread newThread(Runnable r)
      {
        Thread thread = ScheduledExecutorScheduler.this.thread = new Thread(threadGroup, r, name);
        thread.setDaemon(daemon);
        thread.setContextClassLoader(classloader);
        return thread;
      }
    });
    scheduler.setRemoveOnCancelPolicy(true);
    super.doStart();
  }
  
  protected void doStop()
    throws Exception
  {
    scheduler.shutdownNow();
    super.doStop();
    scheduler = null;
  }
  

  public Scheduler.Task schedule(Runnable task, long delay, TimeUnit unit)
  {
    ScheduledThreadPoolExecutor s = scheduler;
    if (s == null)
      new Scheduler.Task()
      {
        public boolean cancel()
        {
          return false;
        }
      };
    ScheduledFuture<?> result = s.schedule(task, delay, unit);
    return new ScheduledFutureTask(result);
  }
  

  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    ContainerLifeCycle.dumpObject(out, this);
    Thread thread = this.thread;
    if (thread != null)
    {
      List<StackTraceElement> frames = Arrays.asList(thread.getStackTrace());
      ContainerLifeCycle.dump(out, indent, new Collection[] { frames });
    }
  }
  
  private static class ScheduledFutureTask implements Scheduler.Task
  {
    private final ScheduledFuture<?> scheduledFuture;
    
    ScheduledFutureTask(ScheduledFuture<?> scheduledFuture)
    {
      this.scheduledFuture = scheduledFuture;
    }
    

    public boolean cancel()
    {
      return scheduledFuture.cancel(false);
    }
  }
}
