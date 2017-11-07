package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;




































@Beta
@GwtIncompatible
public abstract class AbstractService
  implements Service
{
  private static final ListenerCallQueue.Callback<Service.Listener> STARTING_CALLBACK = new ListenerCallQueue.Callback("starting()")
  {
    void call(Service.Listener listener)
    {
      listener.starting();
    }
  };
  private static final ListenerCallQueue.Callback<Service.Listener> RUNNING_CALLBACK = new ListenerCallQueue.Callback("running()")
  {
    void call(Service.Listener listener)
    {
      listener.running();
    }
  };
  
  private static final ListenerCallQueue.Callback<Service.Listener> STOPPING_FROM_STARTING_CALLBACK = stoppingCallback(Service.State.STARTING);
  
  private static final ListenerCallQueue.Callback<Service.Listener> STOPPING_FROM_RUNNING_CALLBACK = stoppingCallback(Service.State.RUNNING);
  
  private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_NEW_CALLBACK = terminatedCallback(Service.State.NEW);
  
  private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_RUNNING_CALLBACK = terminatedCallback(Service.State.RUNNING);
  
  private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_STOPPING_CALLBACK = terminatedCallback(Service.State.STOPPING);
  
  private static ListenerCallQueue.Callback<Service.Listener> terminatedCallback(final Service.State from) {
    new ListenerCallQueue.Callback("terminated({from = " + from + "})")
    {
      void call(Service.Listener listener) {
        listener.terminated(from);
      }
    };
  }
  
  private static ListenerCallQueue.Callback<Service.Listener> stoppingCallback(final Service.State from) {
    new ListenerCallQueue.Callback("stopping({from = " + from + "})")
    {
      void call(Service.Listener listener) {
        listener.stopping(from);
      }
    };
  }
  
  private final Monitor monitor = new Monitor();
  
  private final Monitor.Guard isStartable = new IsStartableGuard();
  protected AbstractService() {}
  
  private final class IsStartableGuard extends Monitor.Guard {
    IsStartableGuard() {
      super();
    }
    
    public boolean isSatisfied()
    {
      return state() == Service.State.NEW;
    }
  }
  
  private final Monitor.Guard isStoppable = new IsStoppableGuard();
  
  private final class IsStoppableGuard extends Monitor.Guard
  {
    IsStoppableGuard() {
      super();
    }
    
    public boolean isSatisfied()
    {
      return state().compareTo(Service.State.RUNNING) <= 0;
    }
  }
  
  private final Monitor.Guard hasReachedRunning = new HasReachedRunningGuard();
  
  private final class HasReachedRunningGuard extends Monitor.Guard
  {
    HasReachedRunningGuard() {
      super();
    }
    
    public boolean isSatisfied()
    {
      return state().compareTo(Service.State.RUNNING) >= 0;
    }
  }
  
  private final Monitor.Guard isStopped = new IsStoppedGuard();
  
  private final class IsStoppedGuard extends Monitor.Guard
  {
    IsStoppedGuard() {
      super();
    }
    
    public boolean isSatisfied()
    {
      return state().isTerminal();
    }
  }
  




  @GuardedBy("monitor")
  private final List<ListenerCallQueue<Service.Listener>> listeners = Collections.synchronizedList(new ArrayList());
  








  @GuardedBy("monitor")
  private volatile StateSnapshot snapshot = new StateSnapshot(Service.State.NEW);
  








  protected abstract void doStart();
  








  protected abstract void doStop();
  








  @CanIgnoreReturnValue
  public final Service startAsync()
  {
    if (monitor.enterIf(isStartable)) {
      try {
        snapshot = new StateSnapshot(Service.State.STARTING);
        starting();
        doStart();
      } catch (Throwable startupFailure) {
        notifyFailed(startupFailure);
      } finally {
        monitor.leave();
        executeListeners();
      }
    } else {
      throw new IllegalStateException("Service " + this + " has already been started");
    }
    return this;
  }
  
  @CanIgnoreReturnValue
  public final Service stopAsync()
  {
    if (monitor.enterIf(isStoppable)) {
      try {
        Service.State previous = state();
        switch (6.$SwitchMap$com$google$common$util$concurrent$Service$State[previous.ordinal()]) {
        case 1: 
          snapshot = new StateSnapshot(Service.State.TERMINATED);
          terminated(Service.State.NEW);
          break;
        case 2: 
          snapshot = new StateSnapshot(Service.State.STARTING, true, null);
          stopping(Service.State.STARTING);
          break;
        case 3: 
          snapshot = new StateSnapshot(Service.State.STOPPING);
          stopping(Service.State.RUNNING);
          doStop();
          break;
        
        case 4: 
        case 5: 
        case 6: 
          throw new AssertionError("isStoppable is incorrectly implemented, saw: " + previous);
        default: 
          throw new AssertionError("Unexpected state: " + previous);
        }
      } catch (Throwable shutdownFailure) {
        notifyFailed(shutdownFailure);
      } finally {
        monitor.leave();
        executeListeners();
      }
    }
    return this;
  }
  
  public final void awaitRunning()
  {
    monitor.enterWhenUninterruptibly(hasReachedRunning);
    try {
      checkCurrentState(Service.State.RUNNING);
      
      monitor.leave(); } finally { monitor.leave();
    }
  }
  
  public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException
  {
    if (monitor.enterWhenUninterruptibly(hasReachedRunning, timeout, unit)) {
      try {
        checkCurrentState(Service.State.RUNNING);
      } finally {
        monitor.leave();

      }
      
    }
    else
    {
      throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state.");
    }
  }
  
  public final void awaitTerminated()
  {
    monitor.enterWhenUninterruptibly(isStopped);
    try {
      checkCurrentState(Service.State.TERMINATED);
      
      monitor.leave(); } finally { monitor.leave();
    }
  }
  
  public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException
  {
    if (monitor.enterWhenUninterruptibly(isStopped, timeout, unit)) {
      try {
        checkCurrentState(Service.State.TERMINATED);
      } finally {
        monitor.leave();


      }
      


    }
    else
    {


      throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. Current state: " + state());
    }
  }
  
  @GuardedBy("monitor")
  private void checkCurrentState(Service.State expected)
  {
    Service.State actual = state();
    if (actual != expected) {
      if (actual == Service.State.FAILED)
      {


        throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but the service has FAILED", failureCause());
      }
      throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but was " + actual);
    }
  }
  






  protected final void notifyStarted()
  {
    monitor.enter();
    
    try
    {
      if (snapshot.state != Service.State.STARTING) {
        IllegalStateException failure = new IllegalStateException("Cannot notifyStarted() when the service is " + snapshot.state);
        

        notifyFailed(failure);
        throw failure;
      }
      
      if (snapshot.shutdownWhenStartupFinishes) {
        snapshot = new StateSnapshot(Service.State.STOPPING);
        

        doStop();
      } else {
        snapshot = new StateSnapshot(Service.State.RUNNING);
        running();
      }
      
      monitor.leave();
      executeListeners();
    }
    finally
    {
      monitor.leave();
      executeListeners();
    }
  }
  






  protected final void notifyStopped()
  {
    monitor.enter();
    
    try
    {
      Service.State previous = snapshot.state;
      if ((previous != Service.State.STOPPING) && (previous != Service.State.RUNNING)) {
        IllegalStateException failure = new IllegalStateException("Cannot notifyStopped() when the service is " + previous);
        
        notifyFailed(failure);
        throw failure;
      }
      snapshot = new StateSnapshot(Service.State.TERMINATED);
      terminated(previous);
      
      monitor.leave();
      executeListeners();
    }
    finally
    {
      monitor.leave();
      executeListeners();
    }
  }
  




  protected final void notifyFailed(Throwable cause)
  {
    Preconditions.checkNotNull(cause);
    
    monitor.enter();
    try {
      Service.State previous = state();
      switch (6.$SwitchMap$com$google$common$util$concurrent$Service$State[previous.ordinal()]) {
      case 1: 
      case 5: 
        throw new IllegalStateException("Failed while in state:" + previous, cause);
      case 2: 
      case 3: 
      case 4: 
        snapshot = new StateSnapshot(Service.State.FAILED, false, cause);
        failed(previous, cause);
        break;
      case 6: 
        break;
      
      default: 
        throw new AssertionError("Unexpected state: " + previous);
      }
      
      monitor.leave();
      executeListeners();
    }
    finally
    {
      monitor.leave();
      executeListeners();
    }
  }
  
  public final boolean isRunning()
  {
    return state() == Service.State.RUNNING;
  }
  
  public final Service.State state()
  {
    return snapshot.externalState();
  }
  



  public final Throwable failureCause()
  {
    return snapshot.failureCause();
  }
  



  public final void addListener(Service.Listener listener, Executor executor)
  {
    Preconditions.checkNotNull(listener, "listener");
    Preconditions.checkNotNull(executor, "executor");
    monitor.enter();
    try {
      if (!state().isTerminal()) {
        listeners.add(new ListenerCallQueue(listener, executor));
      }
      
      monitor.leave(); } finally { monitor.leave();
    }
  }
  
  public String toString()
  {
    return getClass().getSimpleName() + " [" + state() + "]";
  }
  



  private void executeListeners()
  {
    if (!monitor.isOccupiedByCurrentThread())
    {
      for (int i = 0; i < listeners.size(); i++) {
        ((ListenerCallQueue)listeners.get(i)).execute();
      }
    }
  }
  
  @GuardedBy("monitor")
  private void starting() {
    STARTING_CALLBACK.enqueueOn(listeners);
  }
  
  @GuardedBy("monitor")
  private void running() {
    RUNNING_CALLBACK.enqueueOn(listeners);
  }
  
  @GuardedBy("monitor")
  private void stopping(Service.State from) {
    if (from == Service.State.STARTING) {
      STOPPING_FROM_STARTING_CALLBACK.enqueueOn(listeners);
    } else if (from == Service.State.RUNNING) {
      STOPPING_FROM_RUNNING_CALLBACK.enqueueOn(listeners);
    } else {
      throw new AssertionError();
    }
  }
  
  @GuardedBy("monitor")
  private void terminated(Service.State from) {
    switch (6.$SwitchMap$com$google$common$util$concurrent$Service$State[from.ordinal()]) {
    case 1: 
      TERMINATED_FROM_NEW_CALLBACK.enqueueOn(listeners);
      break;
    case 3: 
      TERMINATED_FROM_RUNNING_CALLBACK.enqueueOn(listeners);
      break;
    case 4: 
      TERMINATED_FROM_STOPPING_CALLBACK.enqueueOn(listeners);
      break;
    case 2: 
    case 5: 
    case 6: 
    default: 
      throw new AssertionError();
    }
    
  }
  




  @GuardedBy("monitor")
  private void failed(final Service.State from, final Throwable cause)
  {
    new ListenerCallQueue.Callback("failed({from = " + from + ", cause = " + cause + "})")
    {
      void call(Service.Listener listener)
      {
        listener.failed(from, cause); } }
    
      .enqueueOn(listeners);
  }
  




  @Immutable
  private static final class StateSnapshot
  {
    final Service.State state;
    



    final boolean shutdownWhenStartupFinishes;
    


    @Nullable
    final Throwable failure;
    



    StateSnapshot(Service.State internalState)
    {
      this(internalState, false, null);
    }
    
    StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, @Nullable Throwable failure)
    {
      Preconditions.checkArgument((!shutdownWhenStartupFinishes) || (internalState == Service.State.STARTING), "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
      


      Preconditions.checkArgument(((failure != null ? 1 : 0) ^ (internalState == Service.State.FAILED ? 1 : 0)) == 0, "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
      




      state = internalState;
      this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
      this.failure = failure;
    }
    
    Service.State externalState()
    {
      if ((shutdownWhenStartupFinishes) && (state == Service.State.STARTING)) {
        return Service.State.STOPPING;
      }
      return state;
    }
    

    Throwable failureCause()
    {
      Preconditions.checkState(state == Service.State.FAILED, "failureCause() is only valid if the service has failed, service is %s", state);
      


      return failure;
    }
  }
}
