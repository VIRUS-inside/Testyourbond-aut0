package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;





















@GwtIncompatible
final class TimeoutFuture<V>
  extends AbstractFuture.TrustedFuture<V>
{
  @Nullable
  private ListenableFuture<V> delegateRef;
  @Nullable
  private Future<?> timer;
  
  static <V> ListenableFuture<V> create(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor)
  {
    TimeoutFuture<V> result = new TimeoutFuture(delegate);
    Fire<V> fire = new Fire(result);
    timer = scheduledExecutor.schedule(fire, time, unit);
    delegate.addListener(fire, MoreExecutors.directExecutor());
    return result;
  }
  


























  private TimeoutFuture(ListenableFuture<V> delegate)
  {
    delegateRef = ((ListenableFuture)Preconditions.checkNotNull(delegate));
  }
  
  private static final class Fire<V> implements Runnable {
    @Nullable
    TimeoutFuture<V> timeoutFutureRef;
    
    Fire(TimeoutFuture<V> timeoutFuture) {
      timeoutFutureRef = timeoutFuture;
    }
    


    public void run()
    {
      TimeoutFuture<V> timeoutFuture = timeoutFutureRef;
      if (timeoutFuture == null) {
        return;
      }
      ListenableFuture<V> delegate = delegateRef;
      if (delegate == null) {
        return;
      }
      












      timeoutFutureRef = null;
      if (delegate.isDone()) {
        timeoutFuture.setFuture(delegate);
      }
      else {
        try
        {
          timeoutFuture.setException(new TimeoutException("Future timed out: " + delegate));
          
          delegate.cancel(true); } finally { delegate.cancel(true);
        }
      }
    }
  }
  
  protected void afterDone()
  {
    maybePropagateCancellation(delegateRef);
    
    Future<?> localTimer = timer;
    


    if (localTimer != null) {
      localTimer.cancel(false);
    }
    
    delegateRef = null;
    timer = null;
  }
}
