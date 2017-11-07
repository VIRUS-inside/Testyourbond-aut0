package org.seleniumhq.jetty9.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Scheduler;



















public class SocketChannelEndPoint
  extends ChannelEndPoint
{
  private static final Logger LOG = Log.getLogger(SocketChannelEndPoint.class);
  private final Socket _socket;
  private final InetSocketAddress _local;
  private final InetSocketAddress _remote;
  
  public SocketChannelEndPoint(SelectableChannel channel, ManagedSelector selector, SelectionKey key, Scheduler scheduler)
  {
    this((SocketChannel)channel, selector, key, scheduler);
  }
  
  public SocketChannelEndPoint(SocketChannel channel, ManagedSelector selector, SelectionKey key, Scheduler scheduler)
  {
    super(channel, selector, key, scheduler);
    
    _socket = channel.socket();
    _local = ((InetSocketAddress)_socket.getLocalSocketAddress());
    _remote = ((InetSocketAddress)_socket.getRemoteSocketAddress());
  }
  
  public Socket getSocket()
  {
    return _socket;
  }
  
  public InetSocketAddress getLocalAddress()
  {
    return _local;
  }
  
  public InetSocketAddress getRemoteAddress()
  {
    return _remote;
  }
  

  protected void doShutdownOutput()
  {
    try
    {
      if (!_socket.isOutputShutdown()) {
        _socket.shutdownOutput();
      }
    }
    catch (IOException e) {
      LOG.debug(e);
    }
  }
}
