package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;


















@GwtCompatible(emulated=true)
abstract class ImmediateFuture<V>
  implements ListenableFuture<V>
{
  private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());
  
  ImmediateFuture() {}
  
  public void addListener(Runnable listener, Executor executor) { Preconditions.checkNotNull(listener, "Runnable was null.");
    Preconditions.checkNotNull(executor, "Executor was null.");
    try {
      executor.execute(listener);
    }
    catch (RuntimeException e)
    {
      log.log(Level.SEVERE, "RuntimeException while executing runnable " + listener + " with executor " + executor, e);
    }
  }
  



  public boolean cancel(boolean mayInterruptIfRunning)
  {
    return false;
  }
  
  public abstract V get()
    throws ExecutionException;
  
  public V get(long timeout, TimeUnit unit) throws ExecutionException
  {
    Preconditions.checkNotNull(unit);
    return get();
  }
  
  public boolean isCancelled()
  {
    return false;
  }
  
  public boolean isDone()
  {
    return true;
  }
  
  static class ImmediateSuccessfulFuture<V> extends ImmediateFuture<V> {
    static final ImmediateSuccessfulFuture<Object> NULL = new ImmediateSuccessfulFuture(null);
    @Nullable
    private final V value;
    
    ImmediateSuccessfulFuture(@Nullable V value) {
      this.value = value;
    }
    

    public V get()
    {
      return value;
    }
  }
  
  @GwtIncompatible
  static class ImmediateSuccessfulCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> {
    @Nullable
    private final V value;
    
    ImmediateSuccessfulCheckedFuture(@Nullable V value) {
      this.value = value;
    }
    
    public V get()
    {
      return value;
    }
    
    public V checkedGet()
    {
      return value;
    }
    
    public V checkedGet(long timeout, TimeUnit unit)
    {
      Preconditions.checkNotNull(unit);
      return value;
    }
  }
  
  static final class ImmediateFailedFuture<V> extends AbstractFuture.TrustedFuture<V> {
    ImmediateFailedFuture(Throwable thrown) {
      setException(thrown);
    }
  }
  
  static final class ImmediateCancelledFuture<V> extends AbstractFuture.TrustedFuture<V> {
    ImmediateCancelledFuture() {
      cancel(false);
    }
  }
  
  @GwtIncompatible
  static class ImmediateFailedCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X>
  {
    private final X thrown;
    
    ImmediateFailedCheckedFuture(X thrown) {
      this.thrown = thrown;
    }
    
    public V get() throws ExecutionException
    {
      throw new ExecutionException(thrown);
    }
    
    public V checkedGet() throws Exception
    {
      throw thrown;
    }
    
    public V checkedGet(long timeout, TimeUnit unit) throws Exception
    {
      Preconditions.checkNotNull(unit);
      throw thrown;
    }
  }
}
