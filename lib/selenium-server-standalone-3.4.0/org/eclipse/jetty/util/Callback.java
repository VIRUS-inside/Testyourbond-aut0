package org.eclipse.jetty.util;

import java.util.concurrent.CompletableFuture;
import org.eclipse.jetty.util.thread.Invocable;
import org.eclipse.jetty.util.thread.Invocable.InvocationType;




























public abstract interface Callback
  extends Invocable
{
  public static final Callback NOOP = new Callback() {};
  









  public void succeeded() {}
  








  public void failed(Throwable x) {}
  








  public static Callback from(CompletableFuture<?> completable)
  {
    return from(completable, Invocable.InvocationType.NON_BLOCKING);
  }
  








  public static Callback from(CompletableFuture<?> completable, final Invocable.InvocationType invocation)
  {
    if ((completable instanceof Callback)) {
      return (Callback)completable;
    }
    new Callback()
    {

      public void succeeded()
      {
        val$completable.complete(null);
      }
      

      public void failed(Throwable x)
      {
        val$completable.completeExceptionally(x);
      }
      

      public Invocable.InvocationType getInvocationType()
      {
        return invocation;
      }
    };
  }
  
  public static class Nested implements Callback
  {
    private final Callback callback;
    
    public Nested(Callback callback)
    {
      this.callback = callback;
    }
    
    public Nested(Nested nested)
    {
      callback = callback;
    }
    

    public void succeeded()
    {
      callback.succeeded();
    }
    

    public void failed(Throwable x)
    {
      callback.failed(x);
    }
    

    public Invocable.InvocationType getInvocationType()
    {
      return callback.getInvocationType();
    }
  }
  
  public static class Completable
    extends CompletableFuture<Void>
    implements Callback
  {
    private final Invocable.InvocationType invocation;
    
    public Completable()
    {
      this(Invocable.InvocationType.NON_BLOCKING);
    }
    
    public Completable(Invocable.InvocationType invocation)
    {
      this.invocation = invocation;
    }
    

    public void succeeded()
    {
      complete(null);
    }
    

    public void failed(Throwable x)
    {
      completeExceptionally(x);
    }
    

    public Invocable.InvocationType getInvocationType()
    {
      return invocation;
    }
  }
}
