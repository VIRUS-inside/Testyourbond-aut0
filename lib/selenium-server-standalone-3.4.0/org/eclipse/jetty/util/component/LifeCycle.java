package org.eclipse.jetty.util.component;

import java.util.EventListener;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.annotation.ManagedOperation;

@ManagedObject("Lifecycle Interface for startable components")
public abstract interface LifeCycle
{
  @ManagedOperation(value="Starts the instance", impact="ACTION")
  public abstract void start()
    throws Exception;
  
  @ManagedOperation(value="Stops the instance", impact="ACTION")
  public abstract void stop()
    throws Exception;
  
  public abstract boolean isRunning();
  
  public abstract boolean isStarted();
  
  public abstract boolean isStarting();
  
  public abstract boolean isStopping();
  
  public abstract boolean isStopped();
  
  public abstract boolean isFailed();
  
  public abstract void addLifeCycleListener(Listener paramListener);
  
  public abstract void removeLifeCycleListener(Listener paramListener);
  
  public static abstract interface Listener
    extends EventListener
  {
    public abstract void lifeCycleStarting(LifeCycle paramLifeCycle);
    
    public abstract void lifeCycleStarted(LifeCycle paramLifeCycle);
    
    public abstract void lifeCycleFailure(LifeCycle paramLifeCycle, Throwable paramThrowable);
    
    public abstract void lifeCycleStopping(LifeCycle paramLifeCycle);
    
    public abstract void lifeCycleStopped(LifeCycle paramLifeCycle);
  }
}
