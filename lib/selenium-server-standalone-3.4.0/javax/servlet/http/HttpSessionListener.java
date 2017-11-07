package javax.servlet.http;

import java.util.EventListener;

public abstract interface HttpSessionListener
  extends EventListener
{
  public abstract void sessionCreated(HttpSessionEvent paramHttpSessionEvent);
  
  public abstract void sessionDestroyed(HttpSessionEvent paramHttpSessionEvent);
}
