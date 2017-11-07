package org.seleniumhq.jetty9.io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.component.Dumpable;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.Scheduler;

























public abstract class SelectorManager
  extends ContainerLifeCycle
  implements Dumpable
{
  public static final int DEFAULT_CONNECT_TIMEOUT = 15000;
  protected static final Logger LOG = Log.getLogger(SelectorManager.class);
  
  private final Executor executor;
  private final Scheduler scheduler;
  private final ManagedSelector[] _selectors;
  private long _connectTimeout = 15000L;
  private long _selectorIndex;
  
  protected SelectorManager(Executor executor, Scheduler scheduler)
  {
    this(executor, scheduler, (Runtime.getRuntime().availableProcessors() + 1) / 2);
  }
  
  protected SelectorManager(Executor executor, Scheduler scheduler, int selectors)
  {
    if (selectors <= 0)
      throw new IllegalArgumentException("No selectors");
    this.executor = executor;
    this.scheduler = scheduler;
    _selectors = new ManagedSelector[selectors];
  }
  
  public Executor getExecutor()
  {
    return executor;
  }
  
  public Scheduler getScheduler()
  {
    return scheduler;
  }
  





  public long getConnectTimeout()
  {
    return _connectTimeout;
  }
  





  public void setConnectTimeout(long milliseconds)
  {
    _connectTimeout = milliseconds;
  }
  





  protected void execute(Runnable task)
  {
    executor.execute(task);
  }
  



  public int getSelectorCount()
  {
    return _selectors.length;
  }
  





  private ManagedSelector chooseSelector(SelectableChannel channel)
  {
    ManagedSelector candidate1 = null;
    if (channel != null)
    {
      try
      {
        if ((channel instanceof SocketChannel))
        {
          SocketAddress remote = ((SocketChannel)channel).getRemoteAddress();
          if ((remote instanceof InetSocketAddress))
          {
            byte[] addr = ((InetSocketAddress)remote).getAddress().getAddress();
            if (addr != null)
            {
              int s = addr[(addr.length - 1)] & 0xFF;
              candidate1 = _selectors[(s % getSelectorCount())];
            }
          }
        }
      }
      catch (IOException x)
      {
        LOG.ignore(x);
      }
    }
    



    long s = _selectorIndex++;
    int index = (int)(s % getSelectorCount());
    ManagedSelector candidate2 = _selectors[index];
    
    if ((candidate1 == null) || (candidate1.size() >= candidate2.size() * 2))
      return candidate2;
    return candidate1;
  }
  










  public void connect(SelectableChannel channel, Object attachment)
  {
    ManagedSelector set = chooseSelector(channel); ManagedSelector 
      tmp12_11 = set;tmp12_11.getClass();set.submit(new ManagedSelector.Connect(tmp12_11, channel, attachment));
  }
  




  public void accept(SelectableChannel channel)
  {
    accept(channel, null);
  }
  










  public void accept(SelectableChannel channel, Object attachment)
  {
    ManagedSelector selector = chooseSelector(channel); ManagedSelector 
      tmp12_11 = selector;tmp12_11.getClass();selector.submit(new ManagedSelector.Accept(tmp12_11, channel, attachment));
  }
  








  public void acceptor(SelectableChannel server)
  {
    ManagedSelector selector = chooseSelector(null); ManagedSelector 
      tmp12_11 = selector;tmp12_11.getClass();selector.submit(new ManagedSelector.Acceptor(tmp12_11, server));
  }
  








  protected void accepted(SelectableChannel channel)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  
  protected void doStart()
    throws Exception
  {
    for (int i = 0; i < _selectors.length; i++)
    {
      ManagedSelector selector = newSelector(i);
      _selectors[i] = selector;
      addBean(selector);
    }
    super.doStart();
  }
  






  protected ManagedSelector newSelector(int id)
  {
    return new ManagedSelector(this, id);
  }
  
  protected void doStop()
    throws Exception
  {
    super.doStop();
    for (ManagedSelector selector : _selectors) {
      removeBean(selector);
    }
  }
  






  protected void endPointOpened(EndPoint endpoint) {}
  






  protected void endPointClosed(EndPoint endpoint) {}
  






  public void connectionOpened(Connection connection)
  {
    try
    {
      connection.onOpen();
    }
    catch (Throwable x)
    {
      if (isRunning()) {
        LOG.warn("Exception while notifying connection " + connection, x);
      } else
        LOG.debug("Exception while notifying connection " + connection, x);
      throw x;
    }
  }
  





  public void connectionClosed(Connection connection)
  {
    try
    {
      connection.onClose();
    }
    catch (Throwable x)
    {
      LOG.debug("Exception while notifying connection " + connection, x);
    }
  }
  
  protected boolean doFinishConnect(SelectableChannel channel) throws IOException
  {
    return ((SocketChannel)channel).finishConnect();
  }
  
  protected boolean isConnectionPending(SelectableChannel channel)
  {
    return ((SocketChannel)channel).isConnectionPending();
  }
  
  protected SelectableChannel doAccept(SelectableChannel server) throws IOException
  {
    return ((ServerSocketChannel)server).accept();
  }
  









  protected void connectionFailed(SelectableChannel channel, Throwable ex, Object attachment)
  {
    LOG.warn(String.format("%s - %s", new Object[] { channel, attachment }), ex);
  }
  
  protected Selector newSelector() throws IOException
  {
    return Selector.open();
  }
  
  protected abstract EndPoint newEndPoint(SelectableChannel paramSelectableChannel, ManagedSelector paramManagedSelector, SelectionKey paramSelectionKey)
    throws IOException;
  
  public abstract Connection newConnection(SelectableChannel paramSelectableChannel, EndPoint paramEndPoint, Object paramObject)
    throws IOException;
}
