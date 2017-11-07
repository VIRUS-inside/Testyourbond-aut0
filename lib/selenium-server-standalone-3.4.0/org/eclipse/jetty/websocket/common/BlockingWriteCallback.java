package org.eclipse.jetty.websocket.common;

import java.io.IOException;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.SharedBlockingCallback;
import org.eclipse.jetty.util.SharedBlockingCallback.Blocker;
import org.eclipse.jetty.util.thread.Invocable.InvocationType;
import org.eclipse.jetty.websocket.api.WriteCallback;






















public class BlockingWriteCallback
  extends SharedBlockingCallback
{
  public BlockingWriteCallback() {}
  
  public WriteBlocker acquireWriteBlocker()
    throws IOException
  {
    return new WriteBlocker(acquire());
  }
  
  public static class WriteBlocker implements WriteCallback, Callback, AutoCloseable
  {
    private final SharedBlockingCallback.Blocker blocker;
    
    protected WriteBlocker(SharedBlockingCallback.Blocker blocker)
    {
      this.blocker = blocker;
    }
    


    public Invocable.InvocationType getInvocationType()
    {
      return Invocable.InvocationType.NON_BLOCKING;
    }
    

    public void writeFailed(Throwable x)
    {
      blocker.failed(x);
    }
    

    public void writeSuccess()
    {
      blocker.succeeded();
    }
    

    public void succeeded()
    {
      blocker.succeeded();
    }
    

    public void failed(Throwable x)
    {
      blocker.failed(x);
    }
    

    public void close()
    {
      blocker.close();
    }
    
    public void block() throws IOException
    {
      blocker.block();
    }
  }
}
