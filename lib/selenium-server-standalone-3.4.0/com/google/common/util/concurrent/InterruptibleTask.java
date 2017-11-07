package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;




















@GwtCompatible(emulated=true)
abstract class InterruptibleTask
  implements Runnable
{
  private volatile Thread runner;
  private volatile boolean doneInterrupting;
  private static final AtomicHelper ATOMIC_HELPER;
  private static final Logger log = Logger.getLogger(InterruptibleTask.class.getName());
  
  static
  {
    AtomicHelper helper;
    try {
      helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(InterruptibleTask.class, Thread.class, "runner"));
    }
    catch (Throwable reflectionFailure)
    {
      AtomicHelper helper;
      
      log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
      helper = new SynchronizedAtomicHelper(null);
    }
    ATOMIC_HELPER = helper;
  }
  
  public final void run()
  {
    if (!ATOMIC_HELPER.compareAndSetRunner(this, null, Thread.currentThread())) {
      return;
    }
    try {
      runInterruptibly(); return;
    } finally {
      if (wasInterrupted())
      {





        while (!doneInterrupting) {
          Thread.yield();
        }
      }
    }
  }
  






  final void interruptTask()
  {
    Thread currentRunner = runner;
    if (currentRunner != null) {
      currentRunner.interrupt();
    }
    doneInterrupting = true; }
  
  InterruptibleTask() {}
  
  abstract void runInterruptibly();
  
  abstract boolean wasInterrupted();
  
  private static abstract class AtomicHelper { private AtomicHelper() {}
    
    abstract boolean compareAndSetRunner(InterruptibleTask paramInterruptibleTask, Thread paramThread1, Thread paramThread2); }
  
  private static final class SafeAtomicHelper extends InterruptibleTask.AtomicHelper { final AtomicReferenceFieldUpdater<InterruptibleTask, Thread> runnerUpdater;
    
    SafeAtomicHelper(AtomicReferenceFieldUpdater runnerUpdater) { super();
      this.runnerUpdater = runnerUpdater;
    }
    


    boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) { return runnerUpdater.compareAndSet(task, expect, update); }
  }
  
  private static final class SynchronizedAtomicHelper extends InterruptibleTask.AtomicHelper {
    private SynchronizedAtomicHelper() { super(); }
    
    boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) {
      synchronized (task) {
        if (runner == expect) {
          runner = update;
        }
      }
      return true;
    }
  }
}
