package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.io.ChannelEndPoint;
import org.seleniumhq.jetty9.io.ManagedSelector;
import org.seleniumhq.jetty9.io.NetworkTrafficListener;
import org.seleniumhq.jetty9.io.NetworkTrafficSelectChannelEndPoint;
import org.seleniumhq.jetty9.util.ssl.SslContextFactory;
import org.seleniumhq.jetty9.util.thread.Scheduler;
























public class NetworkTrafficServerConnector
  extends ServerConnector
{
  private final List<NetworkTrafficListener> listeners = new CopyOnWriteArrayList();
  
  public NetworkTrafficServerConnector(Server server)
  {
    this(server, null, null, null, 0, 0, new ConnectionFactory[] { new HttpConnectionFactory() });
  }
  
  public NetworkTrafficServerConnector(Server server, ConnectionFactory connectionFactory, SslContextFactory sslContextFactory)
  {
    super(server, sslContextFactory, new ConnectionFactory[] { connectionFactory });
  }
  
  public NetworkTrafficServerConnector(Server server, ConnectionFactory connectionFactory)
  {
    super(server, new ConnectionFactory[] { connectionFactory });
  }
  
  public NetworkTrafficServerConnector(Server server, Executor executor, Scheduler scheduler, ByteBufferPool pool, int acceptors, int selectors, ConnectionFactory... factories)
  {
    super(server, executor, scheduler, pool, acceptors, selectors, factories);
  }
  
  public NetworkTrafficServerConnector(Server server, SslContextFactory sslContextFactory)
  {
    super(server, sslContextFactory);
  }
  



  public void addNetworkTrafficListener(NetworkTrafficListener listener)
  {
    listeners.add(listener);
  }
  



  public void removeNetworkTrafficListener(NetworkTrafficListener listener)
  {
    listeners.remove(listener);
  }
  
  protected ChannelEndPoint newEndPoint(SocketChannel channel, ManagedSelector selectSet, SelectionKey key)
    throws IOException
  {
    NetworkTrafficSelectChannelEndPoint endPoint = new NetworkTrafficSelectChannelEndPoint(channel, selectSet, key, getScheduler(), getIdleTimeout(), listeners);
    return endPoint;
  }
}
