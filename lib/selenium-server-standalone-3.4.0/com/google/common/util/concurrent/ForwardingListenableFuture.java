package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Executor;


























@CanIgnoreReturnValue
@GwtCompatible
public abstract class ForwardingListenableFuture<V>
  extends ForwardingFuture<V>
  implements ListenableFuture<V>
{
  protected ForwardingListenableFuture() {}
  
  protected abstract ListenableFuture<? extends V> delegate();
  
  public void addListener(Runnable listener, Executor exec)
  {
    delegate().addListener(listener, exec);
  }
  



  public static abstract class SimpleForwardingListenableFuture<V>
    extends ForwardingListenableFuture<V>
  {
    private final ListenableFuture<V> delegate;
    


    protected SimpleForwardingListenableFuture(ListenableFuture<V> delegate)
    {
      this.delegate = ((ListenableFuture)Preconditions.checkNotNull(delegate));
    }
    
    protected final ListenableFuture<V> delegate()
    {
      return delegate;
    }
  }
}
