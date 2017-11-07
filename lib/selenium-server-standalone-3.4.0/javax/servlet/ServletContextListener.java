package javax.servlet;

import java.util.EventListener;

public abstract interface ServletContextListener
  extends EventListener
{
  public abstract void contextInitialized(ServletContextEvent paramServletContextEvent);
  
  public abstract void contextDestroyed(ServletContextEvent paramServletContextEvent);
}
