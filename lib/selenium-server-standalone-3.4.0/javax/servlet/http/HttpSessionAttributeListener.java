package javax.servlet.http;

import java.util.EventListener;

public abstract interface HttpSessionAttributeListener
  extends EventListener
{
  public abstract void attributeAdded(HttpSessionBindingEvent paramHttpSessionBindingEvent);
  
  public abstract void attributeRemoved(HttpSessionBindingEvent paramHttpSessionBindingEvent);
  
  public abstract void attributeReplaced(HttpSessionBindingEvent paramHttpSessionBindingEvent);
}
