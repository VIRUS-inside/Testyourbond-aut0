package org.seleniumhq.jetty9.util.thread;

import java.util.concurrent.TimeUnit;
import org.seleniumhq.jetty9.util.component.LifeCycle;

public abstract interface Scheduler
  extends LifeCycle
{
  public abstract Task schedule(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit);
  
  public static abstract interface Task
  {
    public abstract boolean cancel();
  }
}
