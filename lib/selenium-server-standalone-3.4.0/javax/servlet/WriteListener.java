package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

public abstract interface WriteListener
  extends EventListener
{
  public abstract void onWritePossible()
    throws IOException;
  
  public abstract void onError(Throwable paramThrowable);
}
