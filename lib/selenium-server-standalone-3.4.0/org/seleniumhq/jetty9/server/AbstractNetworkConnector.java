package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.thread.Scheduler;
























@ManagedObject("AbstractNetworkConnector")
public abstract class AbstractNetworkConnector
  extends AbstractConnector
  implements NetworkConnector
{
  private volatile String _host;
  private volatile int _port = 0;
  
  public AbstractNetworkConnector(Server server, Executor executor, Scheduler scheduler, ByteBufferPool pool, int acceptors, ConnectionFactory... factories)
  {
    super(server, executor, scheduler, pool, acceptors, factories);
  }
  
  public void setHost(String host)
  {
    _host = host;
  }
  

  @ManagedAttribute("The network interface this connector binds to as an IP address or a hostname.  If null or 0.0.0.0, then bind to all interfaces.")
  public String getHost()
  {
    return _host;
  }
  
  public void setPort(int port)
  {
    _port = port;
  }
  

  @ManagedAttribute("Port this connector listens on. If set the 0 a random port is assigned which may be obtained with getLocalPort()")
  public int getPort()
  {
    return _port;
  }
  

  public int getLocalPort()
  {
    return -1;
  }
  
  protected void doStart()
    throws Exception
  {
    open();
    super.doStart();
  }
  
  protected void doStop()
    throws Exception
  {
    close();
    super.doStop();
  }
  


  public void open()
    throws IOException
  {}
  

  public void close()
  {
    interruptAcceptors();
  }
  


  public Future<Void> shutdown()
  {
    close();
    return super.shutdown();
  }
  

  protected boolean isAccepting()
  {
    return (super.isAccepting()) && (isOpen());
  }
  

  public String toString()
  {
    return String.format("%s{%s:%d}", new Object[] {
      super.toString(), 
      getHost() == null ? "0.0.0.0" : getHost(), 
      Integer.valueOf(getLocalPort() <= 0 ? getPort() : getLocalPort()) });
  }
}
