package javax.servlet.http;

import java.util.EventListener;

public abstract interface HttpSessionActivationListener
  extends EventListener
{
  public abstract void sessionWillPassivate(HttpSessionEvent paramHttpSessionEvent);
  
  public abstract void sessionDidActivate(HttpSessionEvent paramHttpSessionEvent);
}
