package org.eclipse.jetty.io;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.Scheduler;



















public class NetworkTrafficSelectChannelEndPoint
  extends SelectChannelEndPoint
{
  private static final Logger LOG = Log.getLogger(NetworkTrafficSelectChannelEndPoint.class);
  private final List<NetworkTrafficListener> listeners;
  
  public NetworkTrafficSelectChannelEndPoint(SocketChannel channel, ManagedSelector selectSet, SelectionKey key, Scheduler scheduler, long idleTimeout, List<NetworkTrafficListener> listeners)
    throws IOException
  {
    super(channel, selectSet, key, scheduler, idleTimeout);
    this.listeners = listeners;
  }
  
  public int fill(ByteBuffer buffer)
    throws IOException
  {
    int read = super.fill(buffer);
    notifyIncoming(buffer, read);
    return read;
  }
  
  public boolean flush(ByteBuffer... buffers)
    throws IOException
  {
    boolean flushed = true;
    for (ByteBuffer b : buffers)
    {
      if (b.hasRemaining())
      {
        int position = b.position();
        ByteBuffer view = b.slice();
        flushed &= super.flush(new ByteBuffer[] { b });
        int l = b.position() - position;
        view.limit(view.position() + l);
        notifyOutgoing(view);
        if (!flushed)
          break;
      }
    }
    return flushed;
  }
  



  public void onOpen()
  {
    super.onOpen();
    if ((listeners != null) && (!listeners.isEmpty()))
    {
      for (NetworkTrafficListener listener : listeners)
      {
        try
        {
          listener.opened(getSocket());
        }
        catch (Exception x)
        {
          LOG.warn(x);
        }
      }
    }
  }
  

  public void onClose()
  {
    super.onClose();
    if ((listeners != null) && (!listeners.isEmpty()))
    {
      for (NetworkTrafficListener listener : listeners)
      {
        try
        {
          listener.closed(getSocket());
        }
        catch (Exception x)
        {
          LOG.warn(x);
        }
      }
    }
  }
  

  public void notifyIncoming(ByteBuffer buffer, int read)
  {
    if ((listeners != null) && (!listeners.isEmpty()) && (read > 0))
    {
      for (NetworkTrafficListener listener : listeners)
      {
        try
        {
          ByteBuffer view = buffer.asReadOnlyBuffer();
          listener.incoming(getSocket(), view);
        }
        catch (Exception x)
        {
          LOG.warn(x);
        }
      }
    }
  }
  
  public void notifyOutgoing(ByteBuffer view) {
    Socket socket;
    if ((listeners != null) && (!listeners.isEmpty()) && (view.hasRemaining()))
    {
      socket = getSocket();
      for (NetworkTrafficListener listener : listeners)
      {
        try
        {
          listener.outgoing(socket, view);
        }
        catch (Exception x)
        {
          LOG.warn(x);
        }
      }
    }
  }
}
