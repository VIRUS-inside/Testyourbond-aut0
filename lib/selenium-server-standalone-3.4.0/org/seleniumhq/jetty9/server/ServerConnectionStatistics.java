package org.seleniumhq.jetty9.server;

import org.seleniumhq.jetty9.io.ConnectionStatistics;
import org.seleniumhq.jetty9.util.component.Container;
















public class ServerConnectionStatistics
  extends ConnectionStatistics
{
  public ServerConnectionStatistics() {}
  
  public static void addToAllConnectors(Server server)
  {
    for (Connector connector : server.getConnectors())
    {
      if ((connector instanceof Container)) {
        ((Container)connector).addBean(new ConnectionStatistics());
      }
    }
  }
}
