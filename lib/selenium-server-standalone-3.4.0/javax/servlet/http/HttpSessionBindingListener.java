package javax.servlet.http;

import java.util.EventListener;

public abstract interface HttpSessionBindingListener
  extends EventListener
{
  public abstract void valueBound(HttpSessionBindingEvent paramHttpSessionBindingEvent);
  
  public abstract void valueUnbound(HttpSessionBindingEvent paramHttpSessionBindingEvent);
}
