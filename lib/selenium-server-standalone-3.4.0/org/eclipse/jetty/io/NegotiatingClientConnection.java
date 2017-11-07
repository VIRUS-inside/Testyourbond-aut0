package org.eclipse.jetty.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.net.ssl.SSLEngine;
import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;



















public abstract class NegotiatingClientConnection
  extends AbstractConnection
{
  private static final Logger LOG = Log.getLogger(NegotiatingClientConnection.class);
  
  private final SSLEngine engine;
  private final ClientConnectionFactory connectionFactory;
  private final Map<String, Object> context;
  private volatile boolean completed;
  
  protected NegotiatingClientConnection(EndPoint endp, Executor executor, SSLEngine sslEngine, ClientConnectionFactory connectionFactory, Map<String, Object> context)
  {
    super(endp, executor);
    engine = sslEngine;
    this.connectionFactory = connectionFactory;
    this.context = context;
  }
  
  protected SSLEngine getSSLEngine()
  {
    return engine;
  }
  
  protected void completed()
  {
    completed = true;
  }
  

  public void onOpen()
  {
    super.onOpen();
    try
    {
      getEndPoint().flush(new ByteBuffer[] { BufferUtil.EMPTY_BUFFER });
      if (completed) {
        replaceConnection();
      } else {
        fillInterested();
      }
    }
    catch (IOException x) {
      close();
      throw new RuntimeIOException(x);
    }
  }
  

  public void onFillable()
  {
    for (;;)
    {
      int filled = fill();
      if ((filled == 0) && (!completed))
        fillInterested();
      if ((filled <= 0) || (completed))
        break;
    }
    if (completed) {
      replaceConnection();
    }
  }
  
  private int fill()
  {
    try {
      return getEndPoint().fill(BufferUtil.EMPTY_BUFFER);
    }
    catch (IOException x)
    {
      LOG.debug(x);
      close(); }
    return -1;
  }
  

  private void replaceConnection()
  {
    EndPoint endPoint = getEndPoint();
    try
    {
      endPoint.upgrade(connectionFactory.newConnection(endPoint, context));
    }
    catch (Throwable x)
    {
      LOG.debug(x);
      close();
    }
  }
  


  public void close()
  {
    getEndPoint().shutdownOutput();
    super.close();
  }
}
