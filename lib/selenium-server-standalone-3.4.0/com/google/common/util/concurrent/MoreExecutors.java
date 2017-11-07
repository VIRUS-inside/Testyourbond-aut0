package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.concurrent.GuardedBy;







































@GwtCompatible(emulated=true)
public final class MoreExecutors
{
  private MoreExecutors() {}
  
  @Beta
  @GwtIncompatible
  public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)
  {
    return new Application().getExitingExecutorService(executor, terminationTimeout, timeUnit);
  }
  













  @Beta
  @GwtIncompatible
  public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)
  {
    return 
      new Application().getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
  }
  











  @Beta
  @GwtIncompatible
  public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit)
  {
    new Application().addDelayedShutdownHook(service, terminationTimeout, timeUnit);
  }
  












  @Beta
  @GwtIncompatible
  public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor)
  {
    return new Application().getExitingExecutorService(executor);
  }
  













  @Beta
  @GwtIncompatible
  public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor)
  {
    return new Application().getExitingScheduledExecutorService(executor);
  }
  
  @GwtIncompatible
  @VisibleForTesting
  static class Application
  {
    Application() {}
    
    final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
      MoreExecutors.useDaemonThreadFactory(executor);
      ExecutorService service = Executors.unconfigurableExecutorService(executor);
      addDelayedShutdownHook(service, terminationTimeout, timeUnit);
      return service;
    }
    
    final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)
    {
      MoreExecutors.useDaemonThreadFactory(executor);
      ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
      addDelayedShutdownHook(service, terminationTimeout, timeUnit);
      return service;
    }
    
    final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, TimeUnit timeUnit)
    {
      Preconditions.checkNotNull(service);
      Preconditions.checkNotNull(timeUnit);
      addShutdownHook(
        MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable()
        {


          public void run()
          {


            try
            {


              service.shutdown();
              service.awaitTermination(terminationTimeout, val$timeUnit);
            }
            catch (InterruptedException localInterruptedException) {}
          }
        }));
    }
    
    final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor)
    {
      return getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
    }
    
    final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor)
    {
      return getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
    }
    
    @VisibleForTesting
    void addShutdownHook(Thread hook) {
      Runtime.getRuntime().addShutdownHook(hook);
    }
  }
  
  @GwtIncompatible
  private static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
    executor.setThreadFactory(new ThreadFactoryBuilder()
    
      .setDaemon(true)
      .setThreadFactory(executor.getThreadFactory())
      .build());
  }
  


  @GwtIncompatible
  private static final class DirectExecutorService
    extends AbstractListeningExecutorService
  {
    private final Object lock = new Object();
    






    @GuardedBy("lock")
    private int runningTasks = 0;
    
    @GuardedBy("lock")
    private boolean shutdown = false;
    
    private DirectExecutorService() {}
    
    public void execute(Runnable command) {
      startTask();
      try {
        command.run();
        
        endTask(); } finally { endTask();
      }
    }
    
    public boolean isShutdown()
    {
      synchronized (lock) {
        return shutdown;
      }
    }
    
    public void shutdown()
    {
      synchronized (lock) {
        shutdown = true;
        if (runningTasks == 0) {
          lock.notifyAll();
        }
      }
    }
    

    public List<Runnable> shutdownNow()
    {
      shutdown();
      return Collections.emptyList();
    }
    
    public boolean isTerminated()
    {
      synchronized (lock) {
        return (shutdown) && (runningTasks == 0);
      }
    }
    
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
    {
      long nanos = unit.toNanos(timeout);
      synchronized (lock)
      {
        if ((shutdown) && (runningTasks == 0))
          return true;
        if (nanos <= 0L) {
          return false;
        }
        long now = System.nanoTime();
        TimeUnit.NANOSECONDS.timedWait(lock, nanos);
        nanos -= System.nanoTime() - now;
      }
    }
    






    private void startTask()
    {
      synchronized (lock) {
        if (shutdown) {
          throw new RejectedExecutionException("Executor already shutdown");
        }
        runningTasks += 1;
      }
    }
    


    private void endTask()
    {
      synchronized (lock) {
        int numRunning = --runningTasks;
        if (numRunning == 0) {
          lock.notifyAll();
        }
      }
    }
  }
  

























  @GwtIncompatible
  public static ListeningExecutorService newDirectExecutorService()
  {
    return new DirectExecutorService(null);
  }
  















  public static Executor directExecutor()
  {
    return DirectExecutor.INSTANCE;
  }
  
  private static enum DirectExecutor implements Executor
  {
    INSTANCE;
    
    private DirectExecutor() {}
    
    public void execute(Runnable command) { command.run(); }
    

    public String toString()
    {
      return "MoreExecutors.directExecutor()";
    }
  }
  

















  @GwtIncompatible
  public static ListeningExecutorService listeningDecorator(ExecutorService delegate)
  {
    return (delegate instanceof ScheduledExecutorService) ? new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : (delegate instanceof ListeningExecutorService) ? (ListeningExecutorService)delegate : new ListeningDecorator(delegate);
  }
  




















  @GwtIncompatible
  public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate)
  {
    return (delegate instanceof ListeningScheduledExecutorService) ? (ListeningScheduledExecutorService)delegate : new ScheduledListeningDecorator(delegate);
  }
  
  @GwtIncompatible
  private static class ListeningDecorator extends AbstractListeningExecutorService
  {
    private final ExecutorService delegate;
    
    ListeningDecorator(ExecutorService delegate)
    {
      this.delegate = ((ExecutorService)Preconditions.checkNotNull(delegate));
    }
    
    public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
    {
      return delegate.awaitTermination(timeout, unit);
    }
    
    public final boolean isShutdown()
    {
      return delegate.isShutdown();
    }
    
    public final boolean isTerminated()
    {
      return delegate.isTerminated();
    }
    
    public final void shutdown()
    {
      delegate.shutdown();
    }
    
    public final List<Runnable> shutdownNow()
    {
      return delegate.shutdownNow();
    }
    
    public final void execute(Runnable command)
    {
      delegate.execute(command);
    }
  }
  
  @GwtIncompatible
  private static final class ScheduledListeningDecorator extends MoreExecutors.ListeningDecorator implements ListeningScheduledExecutorService
  {
    final ScheduledExecutorService delegate;
    
    ScheduledListeningDecorator(ScheduledExecutorService delegate)
    {
      super();
      this.delegate = ((ScheduledExecutorService)Preconditions.checkNotNull(delegate));
    }
    
    public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)
    {
      TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(command, null);
      ScheduledFuture<?> scheduled = delegate.schedule(task, delay, unit);
      return new ListenableScheduledTask(task, scheduled);
    }
    

    public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)
    {
      TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(callable);
      ScheduledFuture<?> scheduled = delegate.schedule(task, delay, unit);
      return new ListenableScheduledTask(task, scheduled);
    }
    

    public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)
    {
      NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
      ScheduledFuture<?> scheduled = delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
      return new ListenableScheduledTask(task, scheduled);
    }
    

    public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
    {
      NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
      
      ScheduledFuture<?> scheduled = delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
      return new ListenableScheduledTask(task, scheduled);
    }
    
    private static final class ListenableScheduledTask<V>
      extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V>
    {
      private final ScheduledFuture<?> scheduledDelegate;
      
      public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate)
      {
        super();
        this.scheduledDelegate = scheduledDelegate;
      }
      
      public boolean cancel(boolean mayInterruptIfRunning)
      {
        boolean cancelled = super.cancel(mayInterruptIfRunning);
        if (cancelled)
        {
          scheduledDelegate.cancel(mayInterruptIfRunning);
        }
        

        return cancelled;
      }
      
      public long getDelay(TimeUnit unit)
      {
        return scheduledDelegate.getDelay(unit);
      }
      
      public int compareTo(Delayed other)
      {
        return scheduledDelegate.compareTo(other);
      }
    }
    
    @GwtIncompatible
    private static final class NeverSuccessfulListenableFutureTask extends AbstractFuture<Void> implements Runnable
    {
      private final Runnable delegate;
      
      public NeverSuccessfulListenableFutureTask(Runnable delegate) {
        this.delegate = ((Runnable)Preconditions.checkNotNull(delegate));
      }
      
      public void run()
      {
        try {
          delegate.run();
        } catch (Throwable t) {
          setException(t);
          throw Throwables.propagate(t);
        }
      }
    }
  }
  


















  @GwtIncompatible
  static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, long timeout, TimeUnit unit)
    throws InterruptedException, ExecutionException, TimeoutException
  {
    Preconditions.checkNotNull(executorService);
    Preconditions.checkNotNull(unit);
    int ntasks = tasks.size();
    Preconditions.checkArgument(ntasks > 0);
    List<Future<T>> futures = Lists.newArrayListWithCapacity(ntasks);
    BlockingQueue<Future<T>> futureQueue = Queues.newLinkedBlockingQueue();
    long timeoutNanos = unit.toNanos(timeout);
    







    try
    {
      ExecutionException ee = null;
      long lastTime = timed ? System.nanoTime() : 0L;
      Iterator<? extends Callable<T>> it = tasks.iterator();
      
      futures.add(submitAndAddQueueListener(executorService, (Callable)it.next(), futureQueue));
      ntasks--;
      int active = 1;
      for (;;)
      {
        Future<T> f = (Future)futureQueue.poll();
        long now; if (f == null)
          if (ntasks > 0) {
            ntasks--;
            futures.add(submitAndAddQueueListener(executorService, (Callable)it.next(), futureQueue));
            active++;
          } else { if (active == 0)
              break;
            if (timed) {
              f = (Future)futureQueue.poll(timeoutNanos, TimeUnit.NANOSECONDS);
              if (f == null) {
                throw new TimeoutException();
              }
              now = System.nanoTime();
              timeoutNanos -= now - lastTime;
              lastTime = now;
            } else {
              f = (Future)futureQueue.take();
            }
          }
        if (f != null) {
          active--;
          try { Iterator localIterator1;
            Future<T> f; return f.get();
          } catch (ExecutionException eex) {
            ee = eex;
          } catch (RuntimeException rex) {
            ee = new ExecutionException(rex);
          }
        }
      }
      
      if (ee == null) {
        ee = new ExecutionException(null);
      }
      throw ee;
    } finally {
      for (Future<T> f : futures) {
        f.cancel(true);
      }
    }
  }
  





  @GwtIncompatible
  private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, BlockingQueue<Future<T>> queue)
  {
    final ListenableFuture<T> future = executorService.submit(task);
    future.addListener(new Runnable()
    {
      public void run()
      {
        val$queue.add(future);
      }
      
    }, directExecutor());
    return future;
  }
  







  @Beta
  @GwtIncompatible
  public static ThreadFactory platformThreadFactory()
  {
    if (!isAppEngine()) {
      return Executors.defaultThreadFactory();
    }
    try {
      return 
      

        (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", new Class[0]).invoke(null, new Object[0]);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
    } catch (InvocationTargetException e) {
      throw Throwables.propagate(e.getCause());
    }
  }
  
  @GwtIncompatible
  private static boolean isAppEngine() {
    if (System.getProperty("com.google.appengine.runtime.environment") == null) {
      return false;
    }
    try
    {
      return 
      
        Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null;
    }
    catch (ClassNotFoundException e)
    {
      return false;
    }
    catch (InvocationTargetException e) {
      return false;
    }
    catch (IllegalAccessException e) {
      return false;
    }
    catch (NoSuchMethodException e) {}
    return false;
  }
  




  @GwtIncompatible
  static Thread newThread(String name, Runnable runnable)
  {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(runnable);
    Thread result = platformThreadFactory().newThread(runnable);
    try {
      result.setName(name);
    }
    catch (SecurityException localSecurityException) {}
    
    return result;
  }
  














  @GwtIncompatible
  static Executor renamingDecorator(Executor executor, final Supplier<String> nameSupplier)
  {
    Preconditions.checkNotNull(executor);
    Preconditions.checkNotNull(nameSupplier);
    if (isAppEngine())
    {
      return executor;
    }
    new Executor()
    {
      public void execute(Runnable command) {
        val$executor.execute(Callables.threadRenaming(command, nameSupplier));
      }
    };
  }
  












  @GwtIncompatible
  static ExecutorService renamingDecorator(ExecutorService service, final Supplier<String> nameSupplier)
  {
    Preconditions.checkNotNull(service);
    Preconditions.checkNotNull(nameSupplier);
    if (isAppEngine())
    {
      return service;
    }
    new WrappingExecutorService(service)
    {
      protected <T> Callable<T> wrapTask(Callable<T> callable) {
        return Callables.threadRenaming(callable, nameSupplier);
      }
      
      protected Runnable wrapTask(Runnable command)
      {
        return Callables.threadRenaming(command, nameSupplier);
      }
    };
  }
  












  @GwtIncompatible
  static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, final Supplier<String> nameSupplier)
  {
    Preconditions.checkNotNull(service);
    Preconditions.checkNotNull(nameSupplier);
    if (isAppEngine())
    {
      return service;
    }
    new WrappingScheduledExecutorService(service)
    {
      protected <T> Callable<T> wrapTask(Callable<T> callable) {
        return Callables.threadRenaming(callable, nameSupplier);
      }
      
      protected Runnable wrapTask(Runnable command)
      {
        return Callables.threadRenaming(command, nameSupplier);
      }
    };
  }
  























  @Beta
  @CanIgnoreReturnValue
  @GwtIncompatible
  public static boolean shutdownAndAwaitTermination(ExecutorService service, long timeout, TimeUnit unit)
  {
    long halfTimeoutNanos = unit.toNanos(timeout) / 2L;
    
    service.shutdown();
    try
    {
      if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS))
      {
        service.shutdownNow();
        
        service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
      }
    }
    catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      
      service.shutdownNow();
    }
    return service.isTerminated();
  }
  






  static Executor rejectionPropagatingExecutor(Executor delegate, final AbstractFuture<?> future)
  {
    Preconditions.checkNotNull(delegate);
    Preconditions.checkNotNull(future);
    if (delegate == directExecutor())
    {
      return delegate;
    }
    new Executor() {
      volatile boolean thrownFromDelegate = true;
      
      public void execute(final Runnable command)
      {
        try {
          val$delegate.execute(new Runnable()
          {
            public void run()
            {
              thrownFromDelegate = false;
              command.run();
            }
          });
        } catch (RejectedExecutionException e) {
          if (thrownFromDelegate)
          {
            future.setException(e);
          }
        }
      }
    };
  }
}
