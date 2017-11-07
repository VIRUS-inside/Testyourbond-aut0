package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;
































@GwtIncompatible
final class SerializingExecutor
  implements Executor
{
  private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
  
  private final Executor executor;
  
  @GuardedBy("internalLock")
  private final Deque<Runnable> queue = new ArrayDeque();
  
  @GuardedBy("internalLock")
  private boolean isWorkerRunning = false;
  
  @GuardedBy("internalLock")
  private int suspensions = 0;
  

  private final Object internalLock = new Object();
  
  public SerializingExecutor(Executor executor) {
    this.executor = ((Executor)Preconditions.checkNotNull(executor));
  }
  






  public void execute(Runnable task)
  {
    synchronized (internalLock) {
      queue.add(task);
    }
    startQueueWorker();
  }
  



  public void executeFirst(Runnable task)
  {
    synchronized (internalLock) {
      queue.addFirst(task);
    }
    startQueueWorker();
  }
  







  public void suspend()
  {
    synchronized (internalLock) {
      suspensions += 1;
    }
  }
  










  public void resume()
  {
    synchronized (internalLock) {
      Preconditions.checkState(suspensions > 0);
      suspensions -= 1;
    }
    startQueueWorker();
  }
  
  private void startQueueWorker() {
    synchronized (internalLock)
    {
      if (queue.peek() == null) {
        return;
      }
      if (suspensions > 0) {
        return;
      }
      if (isWorkerRunning) {
        return;
      }
      isWorkerRunning = true;
    }
    boolean executionRejected = true;
    try {
      executor.execute(new QueueWorker(null));
      executionRejected = false;
    } finally {
      if (executionRejected)
      {

        synchronized (internalLock) {
          isWorkerRunning = false;
        }
      }
    }
  }
  
  private final class QueueWorker implements Runnable
  {
    private QueueWorker() {}
    
    public void run()
    {
      try {
        workOnQueue();
      } catch (Error e) {
        synchronized (internalLock) {
          isWorkerRunning = false;
        }
        throw e;
      }
    }
    

    private void workOnQueue()
    {
      for (;;)
      {
        Runnable task = null;
        synchronized (internalLock)
        {
          if (suspensions == 0) {
            task = (Runnable)queue.poll();
          }
          if (task == null) {
            isWorkerRunning = false;
            return;
          }
        }
        try {
          task.run();
        } catch (RuntimeException e) {
          SerializingExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + task, e);
        }
      }
    }
  }
}
