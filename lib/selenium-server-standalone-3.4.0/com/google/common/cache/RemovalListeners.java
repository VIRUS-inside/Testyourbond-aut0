package com.google.common.cache;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;





























@GwtIncompatible
public final class RemovalListeners
{
  private RemovalListeners() {}
  
  public static <K, V> RemovalListener<K, V> asynchronous(final RemovalListener<K, V> listener, Executor executor)
  {
    Preconditions.checkNotNull(listener);
    Preconditions.checkNotNull(executor);
    new RemovalListener()
    {
      public void onRemoval(final RemovalNotification<K, V> notification) {
        val$executor.execute(new Runnable()
        {
          public void run()
          {
            val$listener.onRemoval(notification);
          }
        });
      }
    };
  }
}
