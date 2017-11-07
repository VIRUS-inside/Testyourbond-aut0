package javax.servlet;

import java.util.EventListener;

public abstract interface ServletRequestListener
  extends EventListener
{
  public abstract void requestDestroyed(ServletRequestEvent paramServletRequestEvent);
  
  public abstract void requestInitialized(ServletRequestEvent paramServletRequestEvent);
}
