package org.seleniumhq.jetty9.servlet.listener;

import java.beans.Introspector;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class IntrospectorCleaner
  implements ServletContextListener
{
  public IntrospectorCleaner() {}
  
  public void contextInitialized(ServletContextEvent sce) {}
  
  public void contextDestroyed(ServletContextEvent sce) {}
}
