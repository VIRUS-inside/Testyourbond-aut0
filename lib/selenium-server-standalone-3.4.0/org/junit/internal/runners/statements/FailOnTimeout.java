package org.junit.internal.runners.statements;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestTimedOutException;

public class FailOnTimeout extends Statement
{
  private final Statement originalStatement;
  private final TimeUnit timeUnit;
  private final long timeout;
  private final boolean lookForStuckThread;
  private volatile ThreadGroup threadGroup = null;
  




  public static Builder builder()
  {
    return new Builder(null);
  }
  






  @Deprecated
  public FailOnTimeout(Statement statement, long timeoutMillis)
  {
    this(builder().withTimeout(timeoutMillis, TimeUnit.MILLISECONDS), statement);
  }
  
  private FailOnTimeout(Builder builder, Statement statement) {
    originalStatement = statement;
    timeout = timeout;
    timeUnit = unit;
    lookForStuckThread = lookForStuckThread;
  }
  




  public static class Builder
  {
    private boolean lookForStuckThread = false;
    private long timeout = 0L;
    private TimeUnit unit = TimeUnit.SECONDS;
    







    private Builder() {}
    






    public Builder withTimeout(long timeout, TimeUnit unit)
    {
      if (timeout < 0L) {
        throw new IllegalArgumentException("timeout must be non-negative");
      }
      if (unit == null) {
        throw new NullPointerException("TimeUnit cannot be null");
      }
      this.timeout = timeout;
      this.unit = unit;
      return this;
    }
    








    public Builder withLookingForStuckThread(boolean enable)
    {
      lookForStuckThread = enable;
      return this;
    }
    





    public FailOnTimeout build(Statement statement)
    {
      if (statement == null) {
        throw new NullPointerException("statement cannot be null");
      }
      return new FailOnTimeout(this, statement, null);
    }
  }
  
  public void evaluate() throws Throwable
  {
    CallableStatement callable = new CallableStatement(null);
    FutureTask<Throwable> task = new FutureTask(callable);
    threadGroup = new ThreadGroup("FailOnTimeoutGroup");
    Thread thread = new Thread(threadGroup, task, "Time-limited test");
    thread.setDaemon(true);
    thread.start();
    callable.awaitStarted();
    Throwable throwable = getResult(task, thread);
    if (throwable != null) {
      throw throwable;
    }
  }
  



  private Throwable getResult(FutureTask<Throwable> task, Thread thread)
  {
    try
    {
      if (timeout > 0L) {
        return (Throwable)task.get(timeout, timeUnit);
      }
      return (Throwable)task.get();
    }
    catch (InterruptedException e) {
      return e;
    }
    catch (ExecutionException e) {
      return e.getCause();
    } catch (TimeoutException e) {}
    return createTimeoutException(thread);
  }
  
  private Exception createTimeoutException(Thread thread)
  {
    StackTraceElement[] stackTrace = thread.getStackTrace();
    Thread stuckThread = lookForStuckThread ? getStuckThread(thread) : null;
    Exception currThreadException = new TestTimedOutException(timeout, timeUnit);
    if (stackTrace != null) {
      currThreadException.setStackTrace(stackTrace);
      thread.interrupt();
    }
    if (stuckThread != null) {
      Exception stuckThreadException = new Exception("Appears to be stuck in thread " + stuckThread.getName());
      

      stuckThreadException.setStackTrace(getStackTrace(stuckThread));
      return new MultipleFailureException(Arrays.asList(new Throwable[] { currThreadException, stuckThreadException }));
    }
    
    return currThreadException;
  }
  





  private StackTraceElement[] getStackTrace(Thread thread)
  {
    try
    {
      return thread.getStackTrace();
    } catch (SecurityException e) {}
    return new StackTraceElement[0];
  }
  










  private Thread getStuckThread(Thread mainThread)
  {
    if (threadGroup == null) {
      return null;
    }
    Thread[] threadsInGroup = getThreadArray(threadGroup);
    if (threadsInGroup == null) {
      return null;
    }
    





    Thread stuckThread = null;
    long maxCpuTime = 0L;
    for (Thread thread : threadsInGroup) {
      if (thread.getState() == Thread.State.RUNNABLE) {
        long threadCpuTime = cpuTime(thread);
        if ((stuckThread == null) || (threadCpuTime > maxCpuTime)) {
          stuckThread = thread;
          maxCpuTime = threadCpuTime;
        }
      }
    }
    return stuckThread == mainThread ? null : stuckThread;
  }
  







  private Thread[] getThreadArray(ThreadGroup group)
  {
    int count = group.activeCount();
    int enumSize = Math.max(count * 2, 100);
    

    int loopCount = 0;
    Thread[] threads;
    int enumCount; do { threads = new Thread[enumSize];
      enumCount = group.enumerate(threads);
      if (enumCount < enumSize) {
        break;
      }
      


      enumSize += 100;
      loopCount++; } while (loopCount < 5);
    return null;
    



    return copyThreads(threads, enumCount);
  }
  






  private Thread[] copyThreads(Thread[] threads, int count)
  {
    int length = Math.min(count, threads.length);
    Thread[] result = new Thread[length];
    for (int i = 0; i < length; i++) {
      result[i] = threads[i];
    }
    return result;
  }
  




  private long cpuTime(Thread thr)
  {
    ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
    if (mxBean.isThreadCpuTimeSupported()) {
      try {
        return mxBean.getThreadCpuTime(thr.getId());
      }
      catch (UnsupportedOperationException e) {}
    }
    return 0L;
  }
  
  private class CallableStatement implements Callable<Throwable> {
    private final CountDownLatch startLatch = new CountDownLatch(1);
    
    private CallableStatement() {}
    
    public Throwable call() throws Exception { try { startLatch.countDown();
        originalStatement.evaluate();
      } catch (Exception e) {
        throw e;
      } catch (Throwable e) {
        return e;
      }
      return null;
    }
    
    public void awaitStarted() throws InterruptedException {
      startLatch.await();
    }
  }
}
