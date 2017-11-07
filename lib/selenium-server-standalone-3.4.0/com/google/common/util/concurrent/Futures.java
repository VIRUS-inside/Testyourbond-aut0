package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Queues;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;



































































































@Beta
@GwtCompatible(emulated=true)
public final class Futures
  extends GwtFuturesCatchingSpecialization
{
  private Futures() {}
  
  @GwtIncompatible
  public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<? super Exception, X> mapper)
  {
    return new MappingCheckedFuture((ListenableFuture)Preconditions.checkNotNull(future), mapper);
  }
  




  public static <V> ListenableFuture<V> immediateFuture(@Nullable V value)
  {
    if (value == null)
    {

      ListenableFuture<V> typedNull = ImmediateFuture.ImmediateSuccessfulFuture.NULL;
      return typedNull;
    }
    return new ImmediateFuture.ImmediateSuccessfulFuture(value);
  }
  







  @GwtIncompatible
  public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value)
  {
    return new ImmediateFuture.ImmediateSuccessfulCheckedFuture(value);
  }
  






  public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable)
  {
    Preconditions.checkNotNull(throwable);
    return new ImmediateFuture.ImmediateFailedFuture(throwable);
  }
  





  public static <V> ListenableFuture<V> immediateCancelledFuture()
  {
    return new ImmediateFuture.ImmediateCancelledFuture();
  }
  








  @GwtIncompatible
  public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception)
  {
    Preconditions.checkNotNull(exception);
    return new ImmediateFuture.ImmediateFailedCheckedFuture(exception);
  }
  











































  @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
  public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback)
  {
    return AbstractCatchingFuture.create(input, exceptionType, fallback);
  }
  













































  @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
  public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor)
  {
    return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
  }
  































































  @CanIgnoreReturnValue
  @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
  public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback)
  {
    return AbstractCatchingFuture.create(input, exceptionType, fallback);
  }
  

































































  @CanIgnoreReturnValue
  @Partially.GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
  public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor)
  {
    return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
  }
  
















  @GwtIncompatible
  public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor)
  {
    return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
  }
  







































  public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function)
  {
    return AbstractTransformFuture.create(input, function);
  }
  










































  public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor)
  {
    return AbstractTransformFuture.create(input, function, executor);
  }
  



































  public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function)
  {
    return AbstractTransformFuture.create(input, function);
  }
  




































  public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor)
  {
    return AbstractTransformFuture.create(input, function, executor);
  }
  




















  @GwtIncompatible
  public static <I, O> Future<O> lazyTransform(Future<I> input, final Function<? super I, ? extends O> function)
  {
    Preconditions.checkNotNull(input);
    Preconditions.checkNotNull(function);
    new Future()
    {
      public boolean cancel(boolean mayInterruptIfRunning)
      {
        return val$input.cancel(mayInterruptIfRunning);
      }
      
      public boolean isCancelled()
      {
        return val$input.isCancelled();
      }
      
      public boolean isDone()
      {
        return val$input.isDone();
      }
      
      public O get() throws InterruptedException, ExecutionException
      {
        return applyTransformation(val$input.get());
      }
      
      public O get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException
      {
        return applyTransformation(val$input.get(timeout, unit));
      }
      
      private O applyTransformation(I input) throws ExecutionException {
        try {
          return function.apply(input);
        } catch (Throwable t) {
          throw new ExecutionException(t);
        }
      }
    };
  }
  

























  public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> nested)
  {
    return transformAsync(nested, DEREFERENCER);
  }
  



  private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction()
  {
    public ListenableFuture<Object> apply(ListenableFuture<Object> input)
    {
      return input;
    }
  };
  












  @SafeVarargs
  @Beta
  public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures)
  {
    return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
  }
  













  @Beta
  public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures)
  {
    return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), true);
  }
  





  @SafeVarargs
  public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures)
  {
    return new FutureCombiner(false, ImmutableList.copyOf(futures), null);
  }
  






  public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures)
  {
    return new FutureCombiner(false, ImmutableList.copyOf(futures), null);
  }
  






  @SafeVarargs
  public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures)
  {
    return new FutureCombiner(true, ImmutableList.copyOf(futures), null);
  }
  







  public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures)
  {
    return new FutureCombiner(true, ImmutableList.copyOf(futures), null);
  }
  









  @Beta
  @CanIgnoreReturnValue
  @GwtCompatible
  public static final class FutureCombiner<V>
  {
    private final boolean allMustSucceed;
    







    private final ImmutableList<ListenableFuture<? extends V>> futures;
    








    private FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures)
    {
      this.allMustSucceed = allMustSucceed;
      this.futures = futures;
    }
    













    public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor)
    {
      return new CombinedFuture(futures, allMustSucceed, executor, combiner);
    }
    



    public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner)
    {
      return callAsync(combiner, MoreExecutors.directExecutor());
    }
    













    @CanIgnoreReturnValue
    public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor)
    {
      return new CombinedFuture(futures, allMustSucceed, executor, combiner);
    }
    



    @CanIgnoreReturnValue
    public <C> ListenableFuture<C> call(Callable<C> combiner)
    {
      return call(combiner, MoreExecutors.directExecutor());
    }
  }
  












  public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future)
  {
    if (future.isDone()) {
      return future;
    }
    return new NonCancellationPropagatingFuture(future);
  }
  

  private static final class NonCancellationPropagatingFuture<V>
    extends AbstractFuture.TrustedFuture<V>
  {
    NonCancellationPropagatingFuture(final ListenableFuture<V> delegate)
    {
      delegate.addListener(new Runnable()
      {

        public void run()
        {

          setFuture(delegate);
        }
        
      }, MoreExecutors.directExecutor());
    }
  }
  













  @SafeVarargs
  @Beta
  public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures)
  {
    return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
  }
  













  @Beta
  public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures)
  {
    return new CollectionFuture.ListFuture(ImmutableList.copyOf(futures), false);
  }
  














  @Beta
  @GwtIncompatible
  public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures)
  {
    ConcurrentLinkedQueue<SettableFuture<T>> delegates = Queues.newConcurrentLinkedQueue();
    ImmutableList.Builder<ListenableFuture<T>> listBuilder = ImmutableList.builder();
    









    SerializingExecutor executor = new SerializingExecutor(MoreExecutors.directExecutor());
    for (final ListenableFuture<? extends T> future : futures) {
      SettableFuture<T> delegate = SettableFuture.create();
      
      delegates.add(delegate);
      future.addListener(new Runnable()
      {

        public void run() {
          ((SettableFuture)val$delegates.remove()).setFuture(future); } }, executor);
      


      listBuilder.add(delegate);
    }
    return listBuilder.build();
  }
  































  public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback)
  {
    addCallback(future, callback, MoreExecutors.directExecutor());
  }
  




































  public static <V> void addCallback(ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor)
  {
    Preconditions.checkNotNull(callback);
    Runnable callbackListener = new Runnable()
    {
      public void run()
      {
        try
        {
          value = Futures.getDone(val$future);
        } catch (ExecutionException e) { V value;
          callback.onFailure(e.getCause());
          return;
        } catch (RuntimeException e) {
          callback.onFailure(e);
          return;
        } catch (Error e) {
          callback.onFailure(e); return;
        }
        V value;
        callback.onSuccess(value);
      }
    };
    future.addListener(callbackListener, executor);
  }
  




























  @CanIgnoreReturnValue
  public static <V> V getDone(Future<V> future)
    throws ExecutionException
  {
    Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
    return Uninterruptibles.getUninterruptibly(future);
  }
  








































  @CanIgnoreReturnValue
  @GwtIncompatible
  public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass)
    throws Exception
  {
    return FuturesGetChecked.getChecked(future, exceptionClass);
  }
  









































  @CanIgnoreReturnValue
  @GwtIncompatible
  public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit)
    throws Exception
  {
    return FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
  }
  
































  @CanIgnoreReturnValue
  @GwtIncompatible
  public static <V> V getUnchecked(Future<V> future)
  {
    Preconditions.checkNotNull(future);
    try {
      return Uninterruptibles.getUninterruptibly(future);
    } catch (ExecutionException e) {
      wrapAndThrowUnchecked(e.getCause());
      throw new AssertionError();
    }
  }
  
  @GwtIncompatible
  private static void wrapAndThrowUnchecked(Throwable cause) {
    if ((cause instanceof Error)) {
      throw new ExecutionError((Error)cause);
    }
    




    throw new UncheckedExecutionException(cause);
  }
  







  @GwtIncompatible
  private static class MappingCheckedFuture<V, X extends Exception>
    extends AbstractCheckedFuture<V, X>
  {
    final Function<? super Exception, X> mapper;
    







    MappingCheckedFuture(ListenableFuture<V> delegate, Function<? super Exception, X> mapper)
    {
      super();
      
      this.mapper = ((Function)Preconditions.checkNotNull(mapper));
    }
    
    protected X mapException(Exception e)
    {
      return (Exception)mapper.apply(e);
    }
  }
}
