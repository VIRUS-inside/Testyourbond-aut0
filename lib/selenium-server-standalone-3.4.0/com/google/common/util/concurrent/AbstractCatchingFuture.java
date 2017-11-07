package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.ForOverride;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
















@GwtCompatible
abstract class AbstractCatchingFuture<V, X extends Throwable, F, T>
  extends AbstractFuture.TrustedFuture<V>
  implements Runnable
{
  @Nullable
  ListenableFuture<? extends V> inputFuture;
  @Nullable
  Class<X> exceptionType;
  @Nullable
  F fallback;
  
  static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback)
  {
    CatchingFuture<V, X> future = new CatchingFuture(input, exceptionType, fallback);
    input.addListener(future, MoreExecutors.directExecutor());
    return future;
  }
  



  static <V, X extends Throwable> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor)
  {
    CatchingFuture<V, X> future = new CatchingFuture(input, exceptionType, fallback);
    input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
    return future;
  }
  


  static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback)
  {
    AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture(input, exceptionType, fallback);
    
    input.addListener(future, MoreExecutors.directExecutor());
    return future;
  }
  



  static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor)
  {
    AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture(input, exceptionType, fallback);
    
    input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
    return future;
  }
  








  AbstractCatchingFuture(ListenableFuture<? extends V> inputFuture, Class<X> exceptionType, F fallback)
  {
    this.inputFuture = ((ListenableFuture)Preconditions.checkNotNull(inputFuture));
    this.exceptionType = ((Class)Preconditions.checkNotNull(exceptionType));
    this.fallback = Preconditions.checkNotNull(fallback);
  }
  
  public final void run()
  {
    ListenableFuture<? extends V> localInputFuture = inputFuture;
    Class<X> localExceptionType = exceptionType;
    F localFallback = fallback;
    


    if (((localInputFuture == null ? 1 : 0) | (localExceptionType == null ? 1 : 0) | (localFallback == null ? 1 : 0) | isCancelled()) != 0) {
      return;
    }
    inputFuture = null;
    exceptionType = null;
    fallback = null;
    

    V sourceResult = null;
    Throwable throwable = null;
    try {
      sourceResult = Futures.getDone(localInputFuture);
    } catch (ExecutionException e) {
      throwable = (Throwable)Preconditions.checkNotNull(e.getCause());
    } catch (Throwable e) {
      throwable = e;
    }
    
    if (throwable == null) {
      set(sourceResult);
      return;
    }
    
    if (!Platform.isInstanceOfThrowableClass(throwable, localExceptionType)) {
      setException(throwable);
      
      return;
    }
    

    X castThrowable = throwable;
    try
    {
      fallbackResult = doFallback(localFallback, castThrowable);
    } catch (Throwable t) { T fallbackResult;
      setException(t); return;
    }
    
    T fallbackResult;
    setResult(fallbackResult);
  }
  

  @Nullable
  @ForOverride
  abstract T doFallback(F paramF, X paramX)
    throws Exception;
  
  @ForOverride
  abstract void setResult(@Nullable T paramT);
  
  protected final void afterDone()
  {
    maybePropagateCancellation(inputFuture);
    inputFuture = null;
    exceptionType = null;
    fallback = null;
  }
  






  private static final class AsyncCatchingFuture<V, X extends Throwable>
    extends AbstractCatchingFuture<V, X, AsyncFunction<? super X, ? extends V>, ListenableFuture<? extends V>>
  {
    AsyncCatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback)
    {
      super(exceptionType, fallback);
    }
    
    ListenableFuture<? extends V> doFallback(AsyncFunction<? super X, ? extends V> fallback, X cause)
      throws Exception
    {
      ListenableFuture<? extends V> replacement = fallback.apply(cause);
      Preconditions.checkNotNull(replacement, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)?");
      


      return replacement;
    }
    
    void setResult(ListenableFuture<? extends V> result)
    {
      setFuture(result);
    }
  }
  





  private static final class CatchingFuture<V, X extends Throwable>
    extends AbstractCatchingFuture<V, X, Function<? super X, ? extends V>, V>
  {
    CatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback)
    {
      super(exceptionType, fallback);
    }
    
    @Nullable
    V doFallback(Function<? super X, ? extends V> fallback, X cause) throws Exception
    {
      return fallback.apply(cause);
    }
    
    void setResult(@Nullable V result)
    {
      set(result);
    }
  }
}
