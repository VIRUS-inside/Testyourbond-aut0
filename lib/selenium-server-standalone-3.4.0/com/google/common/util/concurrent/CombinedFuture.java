package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import javax.annotation.Nullable;






















@GwtCompatible
final class CombinedFuture<V>
  extends AggregateFuture<Object, V>
{
  CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, AsyncCallable<V> callable)
  {
    init(new CombinedFutureRunningState(futures, allMustSucceed, new AsyncCallableInterruptibleTask(callable, listenerExecutor)));
  }
  







  CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, Callable<V> callable)
  {
    init(new CombinedFutureRunningState(futures, allMustSucceed, new CallableInterruptibleTask(callable, listenerExecutor)));
  }
  

  private final class CombinedFutureRunningState
    extends AggregateFuture<Object, V>.RunningState
  {
    private CombinedFuture<V>.CombinedFutureInterruptibleTask task;
    

    CombinedFutureRunningState(boolean futures, CombinedFuture<V>.CombinedFutureInterruptibleTask allMustSucceed)
    {
      super(futures, allMustSucceed, false);
      this.task = task;
    }
    

    void collectOneValue(boolean allMustSucceed, int index, @Nullable Object returnValue) {}
    
    void handleAllCompleted()
    {
      CombinedFuture<V>.CombinedFutureInterruptibleTask localTask = task;
      if (localTask != null) {
        localTask.execute();
      } else {
        Preconditions.checkState(isDone());
      }
    }
    
    void releaseResourcesAfterFailure()
    {
      super.releaseResourcesAfterFailure();
      task = null;
    }
    
    void interruptTask()
    {
      CombinedFuture<V>.CombinedFutureInterruptibleTask localTask = task;
      if (localTask != null) {
        localTask.interruptTask();
      }
    }
  }
  
  private abstract class CombinedFutureInterruptibleTask extends InterruptibleTask
  {
    private final Executor listenerExecutor;
    volatile boolean thrownByExecute = true;
    
    public CombinedFutureInterruptibleTask(Executor listenerExecutor) {
      this.listenerExecutor = ((Executor)Preconditions.checkNotNull(listenerExecutor));
    }
    
    final void runInterruptibly()
    {
      thrownByExecute = false;
      
      if (!isDone()) {
        try {
          setValue();
        } catch (ExecutionException e) {
          setException(e.getCause());
        } catch (CancellationException e) {
          cancel(false);
        } catch (Throwable e) {
          setException(e);
        }
      }
    }
    
    final boolean wasInterrupted()
    {
      return CombinedFuture.this.wasInterrupted();
    }
    
    final void execute() {
      try {
        listenerExecutor.execute(this);
      } catch (RejectedExecutionException e) {
        if (thrownByExecute) {
          setException(e);
        }
      }
    }
    
    abstract void setValue() throws Exception;
  }
  
  private final class AsyncCallableInterruptibleTask extends CombinedFuture<V>.CombinedFutureInterruptibleTask
  {
    private final AsyncCallable<V> callable;
    
    public AsyncCallableInterruptibleTask(Executor callable) {
      super(listenerExecutor);
      this.callable = ((AsyncCallable)Preconditions.checkNotNull(callable));
    }
    
    void setValue() throws Exception
    {
      setFuture(callable.call());
    }
  }
  
  private final class CallableInterruptibleTask extends CombinedFuture<V>.CombinedFutureInterruptibleTask
  {
    private final Callable<V> callable;
    
    public CallableInterruptibleTask(Executor callable) {
      super(listenerExecutor);
      this.callable = ((Callable)Preconditions.checkNotNull(callable));
    }
    
    void setValue() throws Exception
    {
      set(callable.call());
    }
  }
}
