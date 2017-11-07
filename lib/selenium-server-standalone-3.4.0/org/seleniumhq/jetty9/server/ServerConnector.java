package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.io.ChannelEndPoint;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.io.ManagedSelector;
import org.seleniumhq.jetty9.io.SelectorManager;
import org.seleniumhq.jetty9.io.SocketChannelEndPoint;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.Name;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.ssl.SslContextFactory;
import org.seleniumhq.jetty9.util.thread.Scheduler;

















































@ManagedObject("HTTP connector using NIO ByteChannels and Selectors")
public class ServerConnector
  extends AbstractNetworkConnector
{
  private final SelectorManager _manager;
  private volatile ServerSocketChannel _acceptChannel;
  private volatile boolean _inheritChannel = false;
  private volatile int _localPort = -1;
  private volatile int _acceptQueueSize = 0;
  private volatile boolean _reuseAddress = true;
  private volatile int _lingerTime = -1;
  





  public ServerConnector(@Name("server") Server server)
  {
    this(server, null, null, null, -1, -1, new ConnectionFactory[] { new HttpConnectionFactory() });
  }
  












  public ServerConnector(@Name("server") Server server, @Name("acceptors") int acceptors, @Name("selectors") int selectors)
  {
    this(server, null, null, null, acceptors, selectors, new ConnectionFactory[] { new HttpConnectionFactory() });
  }
  














  public ServerConnector(@Name("server") Server server, @Name("acceptors") int acceptors, @Name("selectors") int selectors, @Name("factories") ConnectionFactory... factories)
  {
    this(server, null, null, null, acceptors, selectors, factories);
  }
  







  public ServerConnector(@Name("server") Server server, @Name("factories") ConnectionFactory... factories)
  {
    this(server, null, null, null, -1, -1, factories);
  }
  








  public ServerConnector(@Name("server") Server server, @Name("sslContextFactory") SslContextFactory sslContextFactory)
  {
    this(server, null, null, null, -1, -1, AbstractConnectionFactory.getFactories(sslContextFactory, new ConnectionFactory[] { new HttpConnectionFactory() }));
  }
  















  public ServerConnector(@Name("server") Server server, @Name("acceptors") int acceptors, @Name("selectors") int selectors, @Name("sslContextFactory") SslContextFactory sslContextFactory)
  {
    this(server, null, null, null, acceptors, selectors, AbstractConnectionFactory.getFactories(sslContextFactory, new ConnectionFactory[] { new HttpConnectionFactory() }));
  }
  









  public ServerConnector(@Name("server") Server server, @Name("sslContextFactory") SslContextFactory sslContextFactory, @Name("factories") ConnectionFactory... factories)
  {
    this(server, null, null, null, -1, -1, AbstractConnectionFactory.getFactories(sslContextFactory, factories));
  }
  

























  public ServerConnector(@Name("server") Server server, @Name("executor") Executor executor, @Name("scheduler") Scheduler scheduler, @Name("bufferPool") ByteBufferPool bufferPool, @Name("acceptors") int acceptors, @Name("selectors") int selectors, @Name("factories") ConnectionFactory... factories)
  {
    super(server, executor, scheduler, bufferPool, acceptors, factories);
    _manager = newSelectorManager(getExecutor(), getScheduler(), selectors > 0 ? selectors : 
      Math.max(1, Math.min(4, Runtime.getRuntime().availableProcessors() / 2)));
    addBean(_manager, true);
    setAcceptorPriorityDelta(-2);
  }
  
  protected SelectorManager newSelectorManager(Executor executor, Scheduler scheduler, int selectors)
  {
    return new ServerConnectorManager(executor, scheduler, selectors);
  }
  
  protected void doStart()
    throws Exception
  {
    super.doStart();
    
    if (getAcceptors() == 0)
    {
      _acceptChannel.configureBlocking(false);
      _manager.acceptor(_acceptChannel);
    }
  }
  

  public boolean isOpen()
  {
    ServerSocketChannel channel = _acceptChannel;
    return (channel != null) && (channel.isOpen());
  }
  




  public boolean isInheritChannel()
  {
    return _inheritChannel;
  }
  











  public void setInheritChannel(boolean inheritChannel)
  {
    _inheritChannel = inheritChannel;
  }
  
  public void open()
    throws IOException
  {
    if (_acceptChannel == null)
    {
      ServerSocketChannel serverChannel = null;
      if (isInheritChannel())
      {
        Channel channel = System.inheritedChannel();
        if ((channel instanceof ServerSocketChannel)) {
          serverChannel = (ServerSocketChannel)channel;
        } else {
          LOG.warn("Unable to use System.inheritedChannel() [{}]. Trying a new ServerSocketChannel at {}:{}", new Object[] { channel, getHost(), Integer.valueOf(getPort()) });
        }
      }
      if (serverChannel == null)
      {
        serverChannel = ServerSocketChannel.open();
        
        InetSocketAddress bindAddress = getHost() == null ? new InetSocketAddress(getPort()) : new InetSocketAddress(getHost(), getPort());
        serverChannel.socket().setReuseAddress(getReuseAddress());
        serverChannel.socket().bind(bindAddress, getAcceptQueueSize());
        
        _localPort = serverChannel.socket().getLocalPort();
        if (_localPort <= 0) {
          throw new IOException("Server channel not bound");
        }
        addBean(serverChannel);
      }
      
      serverChannel.configureBlocking(true);
      addBean(serverChannel);
      
      _acceptChannel = serverChannel;
    }
  }
  


  public Future<Void> shutdown()
  {
    return super.shutdown();
  }
  

  public void close()
  {
    ServerSocketChannel serverChannel = _acceptChannel;
    _acceptChannel = null;
    
    if (serverChannel != null)
    {
      removeBean(serverChannel);
      

      if (serverChannel.isOpen())
      {
        try
        {
          serverChannel.close();
        }
        catch (IOException e)
        {
          LOG.warn(e);
        }
      }
    }
    
    _localPort = -2;
  }
  
  public void accept(int acceptorID)
    throws IOException
  {
    ServerSocketChannel serverChannel = _acceptChannel;
    if ((serverChannel != null) && (serverChannel.isOpen()))
    {
      SocketChannel channel = serverChannel.accept();
      accepted(channel);
    }
  }
  
  private void accepted(SocketChannel channel) throws IOException
  {
    channel.configureBlocking(false);
    Socket socket = channel.socket();
    configure(socket);
    _manager.accept(channel);
  }
  
  protected void configure(Socket socket)
  {
    try
    {
      socket.setTcpNoDelay(true);
      if (_lingerTime >= 0) {
        socket.setSoLinger(true, _lingerTime / 1000);
      } else {
        socket.setSoLinger(false, 0);
      }
    }
    catch (SocketException e) {
      LOG.ignore(e);
    }
  }
  
  public SelectorManager getSelectorManager()
  {
    return _manager;
  }
  

  public Object getTransport()
  {
    return _acceptChannel;
  }
  

  @ManagedAttribute("local port")
  public int getLocalPort()
  {
    return _localPort;
  }
  
  protected ChannelEndPoint newEndPoint(SocketChannel channel, ManagedSelector selectSet, SelectionKey key) throws IOException
  {
    SocketChannelEndPoint endpoint = new SocketChannelEndPoint(channel, selectSet, key, getScheduler());
    endpoint.setIdleTimeout(getIdleTimeout());
    return endpoint;
  }
  




  @ManagedAttribute("TCP/IP solinger time or -1 to disable")
  public int getSoLingerTime()
  {
    return _lingerTime;
  }
  




  public void setSoLingerTime(int lingerTime)
  {
    _lingerTime = lingerTime;
  }
  



  @ManagedAttribute("Accept Queue size")
  public int getAcceptQueueSize()
  {
    return _acceptQueueSize;
  }
  



  public void setAcceptQueueSize(int acceptQueueSize)
  {
    _acceptQueueSize = acceptQueueSize;
  }
  




  public boolean getReuseAddress()
  {
    return _reuseAddress;
  }
  




  public void setReuseAddress(boolean reuseAddress)
  {
    _reuseAddress = reuseAddress;
  }
  
  protected class ServerConnectorManager extends SelectorManager
  {
    public ServerConnectorManager(Executor executor, Scheduler scheduler, int selectors)
    {
      super(scheduler, selectors);
    }
    
    protected void accepted(SelectableChannel channel)
      throws IOException
    {
      ServerConnector.this.accepted((SocketChannel)channel);
    }
    
    protected ChannelEndPoint newEndPoint(SelectableChannel channel, ManagedSelector selectSet, SelectionKey selectionKey)
      throws IOException
    {
      return newEndPoint((SocketChannel)channel, selectSet, selectionKey);
    }
    
    public Connection newConnection(SelectableChannel channel, EndPoint endpoint, Object attachment)
      throws IOException
    {
      return getDefaultConnectionFactory().newConnection(ServerConnector.this, endpoint);
    }
    

    protected void endPointOpened(EndPoint endpoint)
    {
      super.endPointOpened(endpoint);
      onEndPointOpened(endpoint);
    }
    

    protected void endPointClosed(EndPoint endpoint)
    {
      onEndPointClosed(endpoint);
      super.endPointClosed(endpoint);
    }
  }
}
