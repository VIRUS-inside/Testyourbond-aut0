package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.ForOverride;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;















@GwtCompatible
abstract class AbstractTransformFuture<I, O, F, T>
  extends AbstractFuture.TrustedFuture<O>
  implements Runnable
{
  @Nullable
  ListenableFuture<? extends I> inputFuture;
  @Nullable
  F function;
  
  static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function)
  {
    AsyncTransformFuture<I, O> output = new AsyncTransformFuture(input, function);
    input.addListener(output, MoreExecutors.directExecutor());
    return output;
  }
  


  static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor)
  {
    Preconditions.checkNotNull(executor);
    AsyncTransformFuture<I, O> output = new AsyncTransformFuture(input, function);
    input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
    return output;
  }
  
  static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function)
  {
    Preconditions.checkNotNull(function);
    TransformFuture<I, O> output = new TransformFuture(input, function);
    input.addListener(output, MoreExecutors.directExecutor());
    return output;
  }
  
  static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor)
  {
    Preconditions.checkNotNull(function);
    TransformFuture<I, O> output = new TransformFuture(input, function);
    input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
    return output;
  }
  






  AbstractTransformFuture(ListenableFuture<? extends I> inputFuture, F function)
  {
    this.inputFuture = ((ListenableFuture)Preconditions.checkNotNull(inputFuture));
    this.function = Preconditions.checkNotNull(function);
  }
  
  public final void run()
  {
    ListenableFuture<? extends I> localInputFuture = inputFuture;
    F localFunction = function;
    if ((isCancelled() | localInputFuture == null | localFunction == null)) {
      return;
    }
    inputFuture = null;
    function = null;
    









    try
    {
      sourceResult = Futures.getDone(localInputFuture);
    }
    catch (CancellationException e)
    {
      I sourceResult;
      cancel(false);
      return;
    }
    catch (ExecutionException e) {
      setException(e.getCause());
      return;
    }
    catch (RuntimeException e) {
      setException(e);
      return;


    }
    catch (Error e)
    {

      setException(e);
      return;
    }
    try
    {
      I sourceResult;
      transformResult = doTransform(localFunction, sourceResult);
    } catch (UndeclaredThrowableException e) {
      T transformResult;
      setException(e.getCause());
      return;
    }
    catch (Throwable t) {
      setException(t); return;
    }
    


















    T transformResult;
    

















    setResult(transformResult);
  }
  

  @Nullable
  @ForOverride
  abstract T doTransform(F paramF, @Nullable I paramI)
    throws Exception;
  
  @ForOverride
  abstract void setResult(@Nullable T paramT);
  
  protected final void afterDone()
  {
    maybePropagateCancellation(inputFuture);
    inputFuture = null;
    function = null;
  }
  




  private static final class AsyncTransformFuture<I, O>
    extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>>
  {
    AsyncTransformFuture(ListenableFuture<? extends I> inputFuture, AsyncFunction<? super I, ? extends O> function)
    {
      super(function);
    }
    
    ListenableFuture<? extends O> doTransform(AsyncFunction<? super I, ? extends O> function, @Nullable I input)
      throws Exception
    {
      ListenableFuture<? extends O> outputFuture = function.apply(input);
      Preconditions.checkNotNull(outputFuture, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)?");
      


      return outputFuture;
    }
    
    void setResult(ListenableFuture<? extends O> result)
    {
      setFuture(result);
    }
  }
  



  private static final class TransformFuture<I, O>
    extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O>
  {
    TransformFuture(ListenableFuture<? extends I> inputFuture, Function<? super I, ? extends O> function)
    {
      super(function);
    }
    
    @Nullable
    O doTransform(Function<? super I, ? extends O> function, @Nullable I input)
    {
      return function.apply(input);
    }
    

    void setResult(@Nullable O result)
    {
      set(result);
    }
  }
}
