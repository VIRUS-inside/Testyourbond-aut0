package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;




















































































public abstract class BackgroundInitializer<T>
  implements ConcurrentInitializer<T>
{
  private ExecutorService externalExecutor;
  private ExecutorService executor;
  private Future<T> future;
  
  protected BackgroundInitializer()
  {
    this(null);
  }
  









  protected BackgroundInitializer(ExecutorService exec)
  {
    setExternalExecutor(exec);
  }
  




  public final synchronized ExecutorService getExternalExecutor()
  {
    return externalExecutor;
  }
  






  public synchronized boolean isStarted()
  {
    return future != null;
  }
  














  public final synchronized void setExternalExecutor(ExecutorService externalExecutor)
  {
    if (isStarted()) {
      throw new IllegalStateException("Cannot set ExecutorService after start()!");
    }
    

    this.externalExecutor = externalExecutor;
  }
  










  public synchronized boolean start()
  {
    if (!isStarted())
    {



      executor = getExternalExecutor();
      ExecutorService tempExec; if (executor == null) { ExecutorService tempExec;
        executor = (tempExec = createExecutor());
      } else {
        tempExec = null;
      }
      
      future = executor.submit(createTask(tempExec));
      
      return true;
    }
    
    return false;
  }
  












  public T get()
    throws ConcurrentException
  {
    try
    {
      return getFuture().get();
    } catch (ExecutionException execex) {
      ConcurrentUtils.handleCause(execex);
      return null;
    }
    catch (InterruptedException iex) {
      Thread.currentThread().interrupt();
      throw new ConcurrentException(iex);
    }
  }
  







  public synchronized Future<T> getFuture()
  {
    if (future == null) {
      throw new IllegalStateException("start() must be called first!");
    }
    
    return future;
  }
  








  protected final synchronized ExecutorService getActiveExecutor()
  {
    return executor;
  }
  










  protected int getTaskCount()
  {
    return 1;
  }
  










  protected abstract T initialize()
    throws Exception;
  









  private Callable<T> createTask(ExecutorService execDestroy)
  {
    return new InitializationTask(execDestroy);
  }
  





  private ExecutorService createExecutor()
  {
    return Executors.newFixedThreadPool(getTaskCount());
  }
  


  private class InitializationTask
    implements Callable<T>
  {
    private final ExecutorService execFinally;
    


    public InitializationTask(ExecutorService exec)
    {
      execFinally = exec;
    }
    




    public T call()
      throws Exception
    {
      try
      {
        return initialize();
      } finally {
        if (execFinally != null) {
          execFinally.shutdown();
        }
      }
    }
  }
}
