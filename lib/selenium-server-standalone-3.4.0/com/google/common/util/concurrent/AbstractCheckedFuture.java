package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;























@Beta
@GwtIncompatible
public abstract class AbstractCheckedFuture<V, X extends Exception>
  extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
  implements CheckedFuture<V, X>
{
  protected AbstractCheckedFuture(ListenableFuture<V> delegate)
  {
    super(delegate);
  }
  












  protected abstract X mapException(Exception paramException);
  












  @CanIgnoreReturnValue
  public V checkedGet()
    throws Exception
  {
    try
    {
      return get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw mapException(e);
    } catch (CancellationException e) {
      throw mapException(e);
    } catch (ExecutionException e) {
      throw mapException(e);
    }
  }
  











  @CanIgnoreReturnValue
  public V checkedGet(long timeout, TimeUnit unit)
    throws TimeoutException, Exception
  {
    try
    {
      return get(timeout, unit);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw mapException(e);
    } catch (CancellationException e) {
      throw mapException(e);
    } catch (ExecutionException e) {
      throw mapException(e);
    }
  }
}
