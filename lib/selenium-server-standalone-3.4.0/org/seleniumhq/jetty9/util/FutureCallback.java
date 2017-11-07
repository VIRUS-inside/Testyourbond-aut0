package org.seleniumhq.jetty9.util;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;


















public class FutureCallback
  implements Future<Void>, Callback
{
  private static Throwable COMPLETED = new ConstantThrowable();
  private final AtomicBoolean _done = new AtomicBoolean(false);
  private final CountDownLatch _latch = new CountDownLatch(1);
  
  private Throwable _cause;
  
  public FutureCallback() {}
  
  public FutureCallback(boolean completed)
  {
    if (completed)
    {
      _cause = COMPLETED;
      _done.set(true);
      _latch.countDown();
    }
  }
  
  public FutureCallback(Throwable failed)
  {
    _cause = failed;
    _done.set(true);
    _latch.countDown();
  }
  

  public void succeeded()
  {
    if (_done.compareAndSet(false, true))
    {
      _cause = COMPLETED;
      _latch.countDown();
    }
  }
  

  public void failed(Throwable cause)
  {
    if (_done.compareAndSet(false, true))
    {
      _cause = cause;
      _latch.countDown();
    }
  }
  

  public boolean cancel(boolean mayInterruptIfRunning)
  {
    if (_done.compareAndSet(false, true))
    {
      _cause = new CancellationException();
      _latch.countDown();
      return true;
    }
    return false;
  }
  

  public boolean isCancelled()
  {
    if (_done.get())
    {
      try
      {
        _latch.await();
      }
      catch (InterruptedException e)
      {
        throw new RuntimeException(e);
      }
      return _cause instanceof CancellationException;
    }
    return false;
  }
  

  public boolean isDone()
  {
    return (_done.get()) && (_latch.getCount() == 0L);
  }
  
  public Void get()
    throws InterruptedException, ExecutionException
  {
    _latch.await();
    if (_cause == COMPLETED)
      return null;
    if ((_cause instanceof CancellationException))
      throw ((CancellationException)new CancellationException().initCause(_cause));
    throw new ExecutionException(_cause);
  }
  
  public Void get(long timeout, TimeUnit unit)
    throws InterruptedException, ExecutionException, TimeoutException
  {
    if (!_latch.await(timeout, unit)) {
      throw new TimeoutException();
    }
    if (_cause == COMPLETED)
      return null;
    if ((_cause instanceof TimeoutException))
      throw ((TimeoutException)_cause);
    if ((_cause instanceof CancellationException))
      throw ((CancellationException)new CancellationException().initCause(_cause));
    throw new ExecutionException(_cause);
  }
  
  public static void rethrow(ExecutionException e) throws IOException
  {
    Throwable cause = e.getCause();
    if ((cause instanceof IOException))
      throw ((IOException)cause);
    if ((cause instanceof Error))
      throw ((Error)cause);
    if ((cause instanceof RuntimeException))
      throw ((RuntimeException)cause);
    throw new RuntimeException(cause);
  }
  

  public String toString()
  {
    return String.format("FutureCallback@%x{%b,%b}", new Object[] { Integer.valueOf(hashCode()), Boolean.valueOf(_done.get()), Boolean.valueOf(_cause == COMPLETED ? 1 : false) });
  }
}
