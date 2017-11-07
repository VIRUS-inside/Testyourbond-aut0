package org.seleniumhq.jetty9.server.nio;

import java.util.concurrent.Executor;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.server.ConnectionFactory;
import org.seleniumhq.jetty9.server.NetworkTrafficServerConnector;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.util.ssl.SslContextFactory;
import org.seleniumhq.jetty9.util.thread.Scheduler;






















@Deprecated
public class NetworkTrafficSelectChannelConnector
  extends NetworkTrafficServerConnector
{
  public NetworkTrafficSelectChannelConnector(Server server)
  {
    super(server);
  }
  
  public NetworkTrafficSelectChannelConnector(Server server, ConnectionFactory connectionFactory, SslContextFactory sslContextFactory)
  {
    super(server, connectionFactory, sslContextFactory);
  }
  
  public NetworkTrafficSelectChannelConnector(Server server, ConnectionFactory connectionFactory)
  {
    super(server, connectionFactory);
  }
  
  public NetworkTrafficSelectChannelConnector(Server server, Executor executor, Scheduler scheduler, ByteBufferPool pool, int acceptors, int selectors, ConnectionFactory... factories)
  {
    super(server, executor, scheduler, pool, acceptors, selectors, factories);
  }
  
  public NetworkTrafficSelectChannelConnector(Server server, SslContextFactory sslContextFactory)
  {
    super(server, sslContextFactory);
  }
}
