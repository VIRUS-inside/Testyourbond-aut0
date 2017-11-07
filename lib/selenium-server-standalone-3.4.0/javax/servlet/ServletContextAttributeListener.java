package javax.servlet;

import java.util.EventListener;

public abstract interface ServletContextAttributeListener
  extends EventListener
{
  public abstract void attributeAdded(ServletContextAttributeEvent paramServletContextAttributeEvent);
  
  public abstract void attributeRemoved(ServletContextAttributeEvent paramServletContextAttributeEvent);
  
  public abstract void attributeReplaced(ServletContextAttributeEvent paramServletContextAttributeEvent);
}
