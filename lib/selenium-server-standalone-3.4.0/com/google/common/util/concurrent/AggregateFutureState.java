package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.logging.Logger;



























@GwtCompatible(emulated=true)
abstract class AggregateFutureState
{
  private volatile Set<Throwable> seenExceptions = null;
  
  private volatile int remaining;
  
  private static final AtomicHelper ATOMIC_HELPER;
  
  private static final Logger log = Logger.getLogger(AggregateFutureState.class.getName());
  

  static
  {
    AtomicHelper helper;
    try
    {
      helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remaining"));
    }
    catch (Throwable reflectionFailure)
    {
      AtomicHelper helper;
      
      log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
      helper = new SynchronizedAtomicHelper(null);
    }
    ATOMIC_HELPER = helper;
  }
  
  AggregateFutureState(int remainingFutures) {
    remaining = remainingFutures;
  }
  















  final Set<Throwable> getOrInitSeenExceptions()
  {
    Set<Throwable> seenExceptionsLocal = seenExceptions;
    if (seenExceptionsLocal == null) {
      seenExceptionsLocal = Sets.newConcurrentHashSet();
      




      addInitialException(seenExceptionsLocal);
      
      ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, seenExceptionsLocal);
      





      seenExceptionsLocal = seenExceptions;
    }
    return seenExceptionsLocal;
  }
  


  final int decrementRemainingAndGet()
  {
    return ATOMIC_HELPER.decrementAndGetRemainingCount(this);
  }
  
  abstract void addInitialException(Set<Throwable> paramSet);
  
  private static abstract class AtomicHelper
  {
    private AtomicHelper() {}
    
    abstract void compareAndSetSeenExceptions(AggregateFutureState paramAggregateFutureState, Set<Throwable> paramSet1, Set<Throwable> paramSet2);
    
    abstract int decrementAndGetRemainingCount(AggregateFutureState paramAggregateFutureState);
  }
  
  private static final class SafeAtomicHelper extends AggregateFutureState.AtomicHelper {
    final AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>> seenExceptionsUpdater;
    final AtomicIntegerFieldUpdater<AggregateFutureState> remainingCountUpdater;
    
    SafeAtomicHelper(AtomicReferenceFieldUpdater seenExceptionsUpdater, AtomicIntegerFieldUpdater remainingCountUpdater) {
      super();
      this.seenExceptionsUpdater = seenExceptionsUpdater;
      this.remainingCountUpdater = remainingCountUpdater;
    }
    

    void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update)
    {
      seenExceptionsUpdater.compareAndSet(state, expect, update);
    }
    


    int decrementAndGetRemainingCount(AggregateFutureState state) { return remainingCountUpdater.decrementAndGet(state); }
  }
  
  private static final class SynchronizedAtomicHelper extends AggregateFutureState.AtomicHelper {
    private SynchronizedAtomicHelper() { super(); }
    
    void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update)
    {
      synchronized (state) {
        if (seenExceptions == expect) {
          seenExceptions = update;
        }
      }
    }
    
    int decrementAndGetRemainingCount(AggregateFutureState state)
    {
      synchronized (state) {
        AggregateFutureState.access$310(state);
        return remaining;
      }
    }
  }
}
