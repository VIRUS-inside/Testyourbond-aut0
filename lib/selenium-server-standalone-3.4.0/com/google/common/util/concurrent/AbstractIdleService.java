package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Supplier;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
























@Beta
@GwtIncompatible
public abstract class AbstractIdleService
  implements Service
{
  private final Supplier<String> threadNameSupplier = new ThreadNameSupplier(null);
  protected AbstractIdleService() {}
  
  private final class ThreadNameSupplier implements Supplier<String> {
    private ThreadNameSupplier() {}
    
    public String get() { return serviceName() + " " + state(); }
  }
  


  private final Service delegate = new DelegateService(null);
  
  private final class DelegateService extends AbstractService
  {
    private DelegateService() {}
    
    protected final void doStart() {
      MoreExecutors.renamingDecorator(executor(), threadNameSupplier).execute(new Runnable()
      {
        public void run()
        {
          try {
            startUp();
            notifyStarted();
          } catch (Throwable t) {
            notifyFailed(t);
          }
        }
      });
    }
    

    protected final void doStop()
    {
      MoreExecutors.renamingDecorator(executor(), threadNameSupplier).execute(new Runnable()
      {
        public void run()
        {
          try {
            shutDown();
            notifyStopped();
          } catch (Throwable t) {
            notifyFailed(t);
          }
        }
      });
    }
    
    public String toString()
    {
      return AbstractIdleService.this.toString();
    }
  }
  



  protected abstract void startUp()
    throws Exception;
  



  protected abstract void shutDown()
    throws Exception;
  



  protected Executor executor()
  {
    new Executor()
    {
      public void execute(Runnable command) {
        MoreExecutors.newThread((String)threadNameSupplier.get(), command).start();
      }
    };
  }
  
  public String toString()
  {
    return serviceName() + " [" + state() + "]";
  }
  
  public final boolean isRunning()
  {
    return delegate.isRunning();
  }
  
  public final Service.State state()
  {
    return delegate.state();
  }
  



  public final void addListener(Service.Listener listener, Executor executor)
  {
    delegate.addListener(listener, executor);
  }
  



  public final Throwable failureCause()
  {
    return delegate.failureCause();
  }
  



  @CanIgnoreReturnValue
  public final Service startAsync()
  {
    delegate.startAsync();
    return this;
  }
  



  @CanIgnoreReturnValue
  public final Service stopAsync()
  {
    delegate.stopAsync();
    return this;
  }
  



  public final void awaitRunning()
  {
    delegate.awaitRunning();
  }
  


  public final void awaitRunning(long timeout, TimeUnit unit)
    throws TimeoutException
  {
    delegate.awaitRunning(timeout, unit);
  }
  



  public final void awaitTerminated()
  {
    delegate.awaitTerminated();
  }
  


  public final void awaitTerminated(long timeout, TimeUnit unit)
    throws TimeoutException
  {
    delegate.awaitTerminated(timeout, unit);
  }
  





  protected String serviceName()
  {
    return getClass().getSimpleName();
  }
}
