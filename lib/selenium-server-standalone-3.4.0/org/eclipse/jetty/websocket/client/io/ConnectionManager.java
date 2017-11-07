package org.eclipse.jetty.websocket.client.io;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Locale;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.websocket.client.WebSocketClient;
























@Deprecated
public class ConnectionManager
  extends ContainerLifeCycle
{
  public static InetSocketAddress toSocketAddress(URI uri)
  {
    if (!uri.isAbsolute())
    {
      throw new IllegalArgumentException("Cannot get InetSocketAddress of non-absolute URIs");
    }
    
    int port = uri.getPort();
    String scheme = uri.getScheme().toLowerCase(Locale.ENGLISH);
    if ("ws".equals(scheme))
    {
      if (port == -1)
      {
        port = 80;
      }
    }
    else if ("wss".equals(scheme))
    {
      if (port == -1)
      {
        port = 443;
      }
      
    }
    else {
      throw new IllegalArgumentException("Only support ws:// and wss:// URIs");
    }
    
    return new InetSocketAddress(uri.getHost(), port);
  }
  
  public ConnectionManager(WebSocketClient client) {}
}
