package org.seleniumhq.jetty9.server.jmx;

import org.seleniumhq.jetty9.jmx.ObjectMBean;
import org.seleniumhq.jetty9.server.Handler;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;





















@ManagedObject("MBean Wrapper for Server")
public class ServerMBean
  extends ObjectMBean
{
  private final long startupTime;
  private final Server server;
  
  public ServerMBean(Object managedObject)
  {
    super(managedObject);
    startupTime = System.currentTimeMillis();
    server = ((Server)managedObject);
  }
  
  @ManagedAttribute("contexts on this server")
  public Handler[] getContexts()
  {
    return server.getChildHandlersByClass(ContextHandler.class);
  }
  
  @ManagedAttribute("the startup time since January 1st, 1970 (in ms)")
  public long getStartupTime()
  {
    return startupTime;
  }
}
