package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Supplier;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;






















@Beta
@GwtIncompatible
public abstract class AbstractExecutionThreadService
  implements Service
{
  private static final Logger logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
  

  private final Service delegate = new AbstractService()
  {

    protected final void doStart()
    {
      Executor executor = MoreExecutors.renamingDecorator(
        executor(), new Supplier()
        {
          public String get()
          {
            return serviceName();
          }
        });
      executor.execute(new Runnable()
      {
        public void run()
        {
          try {
            startUp();
            notifyStarted();
            

            if (isRunning()) {
              try {
                AbstractExecutionThreadService.this.run();
              } catch (Throwable t) {
                try {
                  shutDown();

                }
                catch (Exception ignored)
                {
                  AbstractExecutionThreadService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
                }
                


                notifyFailed(t);
                return;
              }
            }
            
            shutDown();
            notifyStopped();
          } catch (Throwable t) {
            notifyFailed(t);
          }
        }
      });
    }
    
    protected void doStop()
    {
      triggerShutdown();
    }
    
    public String toString()
    {
      return AbstractExecutionThreadService.this.toString();
    }
  };
  







  protected AbstractExecutionThreadService() {}
  







  protected void startUp()
    throws Exception
  {}
  






  protected abstract void run()
    throws Exception;
  






  protected void shutDown()
    throws Exception
  {}
  






  protected void triggerShutdown() {}
  






  protected Executor executor()
  {
    new Executor()
    {
      public void execute(Runnable command) {
        MoreExecutors.newThread(serviceName(), command).start();
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
