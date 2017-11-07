package org.seleniumhq.jetty9.util;

import java.util.concurrent.atomic.AtomicInteger;




































public class CountingCallback
  extends Callback.Nested
{
  private final AtomicInteger count;
  
  public CountingCallback(Callback callback, int count)
  {
    super(callback);
    this.count = new AtomicInteger(count);
  }
  


  public void succeeded()
  {
    for (;;)
    {
      int current = count.get();
      

      if (current == 0) {
        return;
      }
      if (count.compareAndSet(current, current - 1))
      {
        if (current == 1)
          super.succeeded();
        return;
      }
    }
  }
  


  public void failed(Throwable failure)
  {
    for (;;)
    {
      int current = count.get();
      

      if (current == 0) {
        return;
      }
      if (count.compareAndSet(current, 0))
      {
        super.failed(failure);
        return;
      }
    }
  }
  

  public String toString()
  {
    return String.format("%s@%x", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()) });
  }
}
