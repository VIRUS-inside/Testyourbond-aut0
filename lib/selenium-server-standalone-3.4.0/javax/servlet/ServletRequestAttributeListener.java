package javax.servlet;

import java.util.EventListener;

public abstract interface ServletRequestAttributeListener
  extends EventListener
{
  public abstract void attributeAdded(ServletRequestAttributeEvent paramServletRequestAttributeEvent);
  
  public abstract void attributeRemoved(ServletRequestAttributeEvent paramServletRequestAttributeEvent);
  
  public abstract void attributeReplaced(ServletRequestAttributeEvent paramServletRequestAttributeEvent);
}
