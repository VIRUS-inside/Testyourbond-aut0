package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;























@GwtCompatible(emulated=true)
public final class Callables
{
  private Callables() {}
  
  public static <T> Callable<T> returning(@Nullable T value)
  {
    new Callable()
    {
      public T call() {
        return val$value;
      }
    };
  }
  









  @Beta
  @GwtIncompatible
  public static <T> AsyncCallable<T> asAsyncCallable(final Callable<T> callable, ListeningExecutorService listeningExecutorService)
  {
    Preconditions.checkNotNull(callable);
    Preconditions.checkNotNull(listeningExecutorService);
    new AsyncCallable()
    {
      public ListenableFuture<T> call() throws Exception {
        return val$listeningExecutorService.submit(callable);
      }
    };
  }
  









  @GwtIncompatible
  static <T> Callable<T> threadRenaming(final Callable<T> callable, Supplier<String> nameSupplier)
  {
    Preconditions.checkNotNull(nameSupplier);
    Preconditions.checkNotNull(callable);
    new Callable()
    {
      public T call() throws Exception {
        Thread currentThread = Thread.currentThread();
        String oldName = currentThread.getName();
        boolean restoreName = Callables.trySetName((String)val$nameSupplier.get(), currentThread);
        try { boolean bool1;
          return callable.call();
        } finally {
          if (restoreName) {
            boolean bool2 = Callables.trySetName(oldName, currentThread);
          }
        }
      }
    };
  }
  








  @GwtIncompatible
  static Runnable threadRenaming(final Runnable task, Supplier<String> nameSupplier)
  {
    Preconditions.checkNotNull(nameSupplier);
    Preconditions.checkNotNull(task);
    new Runnable()
    {
      public void run() {
        Thread currentThread = Thread.currentThread();
        String oldName = currentThread.getName();
        boolean restoreName = Callables.trySetName((String)val$nameSupplier.get(), currentThread);
        try {
          task.run();
        } finally { boolean bool1;
          if (restoreName) {
            boolean bool2 = Callables.trySetName(oldName, currentThread);
          }
        }
      }
    };
  }
  


  @GwtIncompatible
  private static boolean trySetName(String threadName, Thread currentThread)
  {
    try
    {
      currentThread.setName(threadName);
      return true;
    } catch (SecurityException e) {}
    return false;
  }
}
