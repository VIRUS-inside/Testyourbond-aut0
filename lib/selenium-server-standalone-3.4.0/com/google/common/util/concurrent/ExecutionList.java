package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
































@GwtIncompatible
public final class ExecutionList
{
  private static final Logger log = Logger.getLogger(ExecutionList.class.getName());
  



  @GuardedBy("this")
  private RunnableExecutorPair runnables;
  



  @GuardedBy("this")
  private boolean executed;
  




  public ExecutionList() {}
  




  public void add(Runnable runnable, Executor executor)
  {
    Preconditions.checkNotNull(runnable, "Runnable was null.");
    Preconditions.checkNotNull(executor, "Executor was null.");
    



    synchronized (this) {
      if (!executed) {
        runnables = new RunnableExecutorPair(runnable, executor, runnables);
        return;
      }
    }
    



    executeListener(runnable, executor);
  }
  













  public void execute()
  {
    synchronized (this) {
      if (executed) {
        return;
      }
      executed = true;
      RunnableExecutorPair list = runnables;
      runnables = null;
    }
    



    RunnableExecutorPair list;
    



    RunnableExecutorPair reversedList = null;
    while (list != null) {
      RunnableExecutorPair tmp = list;
      list = next;
      next = reversedList;
      reversedList = tmp;
    }
    while (reversedList != null) {
      executeListener(runnable, executor);
      reversedList = next;
    }
  }
  


  private static void executeListener(Runnable runnable, Executor executor)
  {
    try
    {
      executor.execute(runnable);

    }
    catch (RuntimeException e)
    {
      log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
    }
  }
  
  private static final class RunnableExecutorPair
  {
    final Runnable runnable;
    final Executor executor;
    @Nullable
    RunnableExecutorPair next;
    
    RunnableExecutorPair(Runnable runnable, Executor executor, RunnableExecutorPair next)
    {
      this.runnable = runnable;
      this.executor = executor;
      this.next = next;
    }
  }
}
