package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

public abstract interface AsyncListener
  extends EventListener
{
  public abstract void onComplete(AsyncEvent paramAsyncEvent)
    throws IOException;
  
  public abstract void onTimeout(AsyncEvent paramAsyncEvent)
    throws IOException;
  
  public abstract void onError(AsyncEvent paramAsyncEvent)
    throws IOException;
  
  public abstract void onStartAsync(AsyncEvent paramAsyncEvent)
    throws IOException;
}
