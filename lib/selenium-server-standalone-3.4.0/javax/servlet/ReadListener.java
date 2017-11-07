package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

public abstract interface ReadListener
  extends EventListener
{
  public abstract void onDataAvailable()
    throws IOException;
  
  public abstract void onAllDataRead()
    throws IOException;
  
  public abstract void onError(Throwable paramThrowable);
}
