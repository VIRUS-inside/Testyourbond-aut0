package com.google.common.util.concurrent;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;























@GwtIncompatible
final class ListenerCallQueue<L>
  implements Runnable
{
  private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
  private final L listener;
  private final Executor executor;
  
  static abstract class Callback<L> { private final String methodCall;
    
    Callback(String methodCall) { this.methodCall = methodCall; }
    

    abstract void call(L paramL);
    
    void enqueueOn(Iterable<ListenerCallQueue<L>> queues)
    {
      for (ListenerCallQueue<L> queue : queues) {
        queue.add(this);
      }
    }
  }
  



  @GuardedBy("this")
  private final Queue<Callback<L>> waitQueue = Queues.newArrayDeque();
  @GuardedBy("this")
  private boolean isThreadScheduled;
  
  ListenerCallQueue(L listener, Executor executor)
  {
    this.listener = Preconditions.checkNotNull(listener);
    this.executor = ((Executor)Preconditions.checkNotNull(executor));
  }
  
  synchronized void add(Callback<L> callback)
  {
    waitQueue.add(callback);
  }
  
  void execute()
  {
    boolean scheduleTaskRunner = false;
    synchronized (this) {
      if (!isThreadScheduled) {
        isThreadScheduled = true;
        scheduleTaskRunner = true;
      }
    }
    if (scheduleTaskRunner) {
      try {
        executor.execute(this);
      }
      catch (RuntimeException e) {
        synchronized (this) {
          isThreadScheduled = false;
        }
        
        logger.log(Level.SEVERE, "Exception while running callbacks for " + listener + " on " + executor, e);
        


        throw e;
      }
    }
  }
  
  public void run()
  {
    boolean stillRunning = true;
    try
    {
      for (;;) {
        synchronized (this) {
          Preconditions.checkState(isThreadScheduled);
          Callback<L> nextToRun = (Callback)waitQueue.poll();
          if (nextToRun == null) {
            isThreadScheduled = false;
            stillRunning = false;
            break;
          }
        }
        
        try
        {
          nextToRun.call(listener);
        } catch (RuntimeException e) {
          Callback<L> nextToRun;
          logger.log(Level.SEVERE, "Exception while executing callback: " + listener + "." + 
          
            methodCall, e);
        }
      }
    }
    finally {
      if (stillRunning)
      {

        synchronized (this) {
          isThreadScheduled = false;
        }
      }
    }
  }
}
