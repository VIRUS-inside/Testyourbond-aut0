package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;







































































@Beta
@GwtIncompatible
public abstract class AbstractScheduledService
  implements Service
{
  private static final Logger logger = Logger.getLogger(AbstractScheduledService.class.getName());
  
  protected AbstractScheduledService() {}
  

  protected abstract void runOneIteration()
    throws Exception;
  

  protected void startUp()
    throws Exception
  {}
  

  protected void shutDown()
    throws Exception
  {}
  

  protected abstract Scheduler scheduler();
  

  public static abstract class Scheduler
  {
    public static Scheduler newFixedDelaySchedule(long initialDelay, long delay, final TimeUnit unit)
    {
      Preconditions.checkNotNull(unit);
      Preconditions.checkArgument(delay > 0L, "delay must be > 0, found %s", delay);
      new Scheduler(initialDelay)
      {
        public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task)
        {
          return executor.scheduleWithFixedDelay(task, val$initialDelay, unit, val$unit);
        }
      };
    }
    








    public static Scheduler newFixedRateSchedule(long initialDelay, long period, final TimeUnit unit)
    {
      Preconditions.checkNotNull(unit);
      Preconditions.checkArgument(period > 0L, "period must be > 0, found %s", period);
      new Scheduler(initialDelay)
      {
        public Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable task)
        {
          return executor.scheduleAtFixedRate(task, val$initialDelay, unit, val$unit);
        }
      };
    }
    

    abstract Future<?> schedule(AbstractService paramAbstractService, ScheduledExecutorService paramScheduledExecutorService, Runnable paramRunnable);
    

    private Scheduler() {}
  }
  

  private final AbstractService delegate = new ServiceDelegate(null);
  



  private final class ServiceDelegate
    extends AbstractService
  {
    private volatile Future<?> runningTask;
    

    private volatile ScheduledExecutorService executorService;
    

    private final ReentrantLock lock = new ReentrantLock();
    private ServiceDelegate() {}
    
    class Task implements Runnable {
      Task() {}
      
      public void run() { lock.lock();
        try {
          if (runningTask.isCancelled())
          {
            return;
          }
          runOneIteration();
        } catch (Throwable t) {
          try {
            shutDown();
          } catch (Exception ignored) {
            AbstractScheduledService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
          }
          


          notifyFailed(t);
          runningTask.cancel(false);
        } finally {
          lock.unlock();
        }
      }
    }
    
    private final Runnable task = new Task();
    

    protected final void doStart()
    {
      executorService = MoreExecutors.renamingDecorator(
        executor(), new Supplier()
        {
          public String get()
          {
            return serviceName() + " " + state();
          }
        });
      executorService.execute(new Runnable()
      {
        public void run()
        {
          lock.lock();
          try {
            startUp();
            runningTask = scheduler().schedule(delegate, executorService, task);
            notifyStarted();
          } catch (Throwable t) {
            notifyFailed(t);
            if (runningTask != null)
            {
              runningTask.cancel(false);
            }
          } finally {
            lock.unlock();
          }
        }
      });
    }
    
    protected final void doStop()
    {
      runningTask.cancel(false);
      executorService.execute(new Runnable()
      {
        public void run()
        {
          try {
            lock.lock();
            try {
              if (state() != Service.State.STOPPING)
              {



                return;
              }
              shutDown();
            } finally {
              lock.unlock();
            }
            notifyStopped();
          } catch (Throwable t) {
            notifyFailed(t);
          }
        }
      });
    }
    
    public String toString()
    {
      return AbstractScheduledService.this.toString();
    }
  }
  




















































  protected ScheduledExecutorService executor()
  {
    final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory()
    {
      public Thread newThread(Runnable runnable)
      {
        return MoreExecutors.newThread(serviceName(), runnable);



      }
      



    });
    addListener(new Service.Listener()
    {
      public void terminated(Service.State from)
      {
        executor.shutdown();
      }
      
      public void failed(Service.State from, Throwable failure)
      {
        executor.shutdown();
      }
      
    }, MoreExecutors.directExecutor());
    return executor;
  }
  





  protected String serviceName()
  {
    return getClass().getSimpleName();
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
  



  @Beta
  public static abstract class CustomScheduler
    extends AbstractScheduledService.Scheduler
  {
    public CustomScheduler()
    {
      super();
    }
    




    private class ReschedulableCallable
      extends ForwardingFuture<Void>
      implements Callable<Void>
    {
      private final Runnable wrappedRunnable;
      



      private final ScheduledExecutorService executor;
      


      private final AbstractService service;
      


      private final ReentrantLock lock = new ReentrantLock();
      
      @GuardedBy("lock")
      private Future<Void> currentFuture;
      

      ReschedulableCallable(AbstractService service, ScheduledExecutorService executor, Runnable runnable)
      {
        wrappedRunnable = runnable;
        this.executor = executor;
        this.service = service;
      }
      
      public Void call() throws Exception
      {
        wrappedRunnable.run();
        reschedule();
        return null;
      }
      



      public void reschedule()
      {
        try
        {
          schedule = getNextSchedule();
        } catch (Throwable t) { AbstractScheduledService.CustomScheduler.Schedule schedule;
          service.notifyFailed(t); return;
        }
        

        AbstractScheduledService.CustomScheduler.Schedule schedule;
        

        Throwable scheduleFailure = null;
        lock.lock();
        try {
          if ((currentFuture == null) || (!currentFuture.isCancelled())) {
            currentFuture = executor.schedule(this, delay, unit);

          }
          


        }
        catch (Throwable e)
        {


          scheduleFailure = e;
        } finally {
          lock.unlock();
        }
        
        if (scheduleFailure != null) {
          service.notifyFailed(scheduleFailure);
        }
      }
      



      public boolean cancel(boolean mayInterruptIfRunning)
      {
        lock.lock();
        try {
          return currentFuture.cancel(mayInterruptIfRunning);
        } finally {
          lock.unlock();
        }
      }
      
      public boolean isCancelled()
      {
        lock.lock();
        try {
          return currentFuture.isCancelled();
        } finally {
          lock.unlock();
        }
      }
      
      protected Future<Void> delegate()
      {
        throw new UnsupportedOperationException("Only cancel and isCancelled is supported by this future");
      }
    }
    


    final Future<?> schedule(AbstractService service, ScheduledExecutorService executor, Runnable runnable)
    {
      ReschedulableCallable task = new ReschedulableCallable(service, executor, runnable);
      task.reschedule();
      return task;
    }
    


    protected abstract Schedule getNextSchedule()
      throws Exception;
    


    @Beta
    protected static final class Schedule
    {
      private final long delay;
      
      private final TimeUnit unit;
      

      public Schedule(long delay, TimeUnit unit)
      {
        this.delay = delay;
        this.unit = ((TimeUnit)Preconditions.checkNotNull(unit));
      }
    }
  }
}
