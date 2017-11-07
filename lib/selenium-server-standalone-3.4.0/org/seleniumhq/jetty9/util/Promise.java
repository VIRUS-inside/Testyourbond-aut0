package org.seleniumhq.jetty9.util;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;










































public abstract interface Promise<C>
{
  public void succeeded(C result) {}
  
  public void failed(Throwable x) {}
  
  public static class Adapter<U>
    implements Promise<U>
  {
    public Adapter() {}
    
    public void failed(Throwable x)
    {
      Log.getLogger(getClass()).warn(x);
    }
  }
  











  public static <T> Promise<T> from(CompletableFuture<? super T> completable)
  {
    if ((completable instanceof Promise)) {
      return (Promise)completable;
    }
    new Promise()
    {

      public void succeeded(T result)
      {
        val$completable.complete(result);
      }
      

      public void failed(Throwable x)
      {
        val$completable.completeExceptionally(x);
      }
    };
  }
  

  public static class Completable<S>
    extends CompletableFuture<S>
    implements Promise<S>
  {
    public Completable() {}
    

    public void succeeded(S result)
    {
      complete(result);
    }
    

    public void failed(Throwable x)
    {
      completeExceptionally(x);
    }
  }
  
  public static class Wrapper<W> implements Promise<W>
  {
    private final Promise<W> promise;
    
    public Wrapper(Promise<W> promise)
    {
      this.promise = ((Promise)Objects.requireNonNull(promise));
    }
    

    public void succeeded(W result)
    {
      promise.succeeded(result);
    }
    

    public void failed(Throwable x)
    {
      promise.failed(x);
    }
    
    public Promise<W> getPromise()
    {
      return promise;
    }
    
    public Promise<W> unwrap()
    {
      Promise<W> result = promise;
      

      while ((result instanceof Wrapper)) {
        result = ((Wrapper)result).unwrap();
      }
      

      return result;
    }
  }
}
