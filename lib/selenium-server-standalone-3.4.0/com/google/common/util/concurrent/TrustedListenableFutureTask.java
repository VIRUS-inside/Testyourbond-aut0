package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import javax.annotation.Nullable;

























@GwtCompatible
class TrustedListenableFutureTask<V>
  extends AbstractFuture.TrustedFuture<V>
  implements RunnableFuture<V>
{
  private TrustedListenableFutureTask<V>.TrustedFutureInterruptibleTask task;
  
  static <V> TrustedListenableFutureTask<V> create(Callable<V> callable)
  {
    return new TrustedListenableFutureTask(callable);
  }
  










  static <V> TrustedListenableFutureTask<V> create(Runnable runnable, @Nullable V result)
  {
    return new TrustedListenableFutureTask(Executors.callable(runnable, result));
  }
  





  TrustedListenableFutureTask(Callable<V> callable)
  {
    task = new TrustedFutureInterruptibleTask(callable);
  }
  
  public void run()
  {
    TrustedListenableFutureTask<V>.TrustedFutureInterruptibleTask localTask = task;
    if (localTask != null) {
      localTask.run();
    }
  }
  
  protected void afterDone()
  {
    super.afterDone();
    
    if (wasInterrupted()) {
      TrustedListenableFutureTask<V>.TrustedFutureInterruptibleTask localTask = task;
      if (localTask != null) {
        localTask.interruptTask();
      }
    }
    
    task = null;
  }
  
  public String toString()
  {
    return super.toString() + " (delegate = " + task + ")";
  }
  
  private final class TrustedFutureInterruptibleTask extends InterruptibleTask
  {
    private final Callable<V> callable;
    
    TrustedFutureInterruptibleTask() {
      this.callable = ((Callable)Preconditions.checkNotNull(callable));
    }
    

    void runInterruptibly()
    {
      if (!isDone()) {
        try {
          set(callable.call());
        } catch (Throwable t) {
          setException(t);
        }
      }
    }
    
    boolean wasInterrupted()
    {
      return TrustedListenableFutureTask.this.wasInterrupted();
    }
    
    public String toString()
    {
      return callable.toString();
    }
  }
}
