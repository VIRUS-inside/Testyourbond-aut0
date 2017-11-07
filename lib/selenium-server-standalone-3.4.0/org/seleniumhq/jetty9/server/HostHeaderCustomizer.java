package org.seleniumhq.jetty9.server;

import java.util.Objects;






































public class HostHeaderCustomizer
  implements HttpConfiguration.Customizer
{
  private final String serverName;
  private final int serverPort;
  
  public HostHeaderCustomizer(String serverName)
  {
    this(serverName, 0);
  }
  




  public HostHeaderCustomizer(String serverName, int serverPort)
  {
    this.serverName = ((String)Objects.requireNonNull(serverName));
    this.serverPort = serverPort;
  }
  

  public void customize(Connector connector, HttpConfiguration channelConfig, Request request)
  {
    if (request.getHeader("Host") == null) {
      request.setAuthority(serverName, serverPort);
    }
  }
}
