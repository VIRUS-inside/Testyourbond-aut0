package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import org.seleniumhq.jetty9.io.AbstractConnection;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.util.BufferUtil;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;




















public abstract class NegotiatingServerConnection
  extends AbstractConnection
{
  private static final Logger LOG = Log.getLogger(NegotiatingServerConnection.class);
  
  private final Connector connector;
  
  private final SSLEngine engine;
  
  private final List<String> protocols;
  
  private final String defaultProtocol;
  
  private String protocol;
  

  protected NegotiatingServerConnection(Connector connector, EndPoint endPoint, SSLEngine engine, List<String> protocols, String defaultProtocol)
  {
    super(endPoint, connector.getExecutor());
    this.connector = connector;
    this.protocols = protocols;
    this.defaultProtocol = defaultProtocol;
    this.engine = engine;
  }
  
  protected List<String> getProtocols()
  {
    return protocols;
  }
  
  protected String getDefaultProtocol()
  {
    return defaultProtocol;
  }
  
  protected Connector getConnector()
  {
    return connector;
  }
  
  protected SSLEngine getSSLEngine()
  {
    return engine;
  }
  
  protected String getProtocol()
  {
    return protocol;
  }
  
  protected void setProtocol(String protocol)
  {
    this.protocol = protocol;
  }
  

  public void onOpen()
  {
    super.onOpen();
    fillInterested();
  }
  

  public void onFillable()
  {
    int filled = fill();
    
    if (filled == 0)
    {
      if (protocol == null)
      {
        if (engine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)
        {

          if (LOG.isDebugEnabled())
            LOG.debug("{} could not negotiate protocol, SSLEngine: {}", new Object[] { this, engine });
          close();

        }
        else
        {

          fillInterested();
        }
      }
      else
      {
        ConnectionFactory connectionFactory = connector.getConnectionFactory(protocol);
        if (connectionFactory == null)
        {
          LOG.info("{} application selected protocol '{}', but no correspondent {} has been configured", new Object[] { this, protocol, ConnectionFactory.class
            .getName() });
          close();
        }
        else
        {
          EndPoint endPoint = getEndPoint();
          Connection newConnection = connectionFactory.newConnection(connector, endPoint);
          endPoint.upgrade(newConnection);
        }
      }
    }
    else if (filled < 0)
    {

      if (LOG.isDebugEnabled())
        LOG.debug("{} detected close on client side", new Object[] { this });
      close();

    }
    else
    {
      throw new IllegalStateException();
    }
  }
  
  private int fill()
  {
    try
    {
      return getEndPoint().fill(BufferUtil.EMPTY_BUFFER);
    }
    catch (IOException x)
    {
      LOG.debug(x);
      close(); }
    return -1;
  }
  



  public void close()
  {
    getEndPoint().shutdownOutput();
    super.close();
  }
  
  public static abstract interface CipherDiscriminator
  {
    public abstract boolean isAcceptable(String paramString1, String paramString2, String paramString3);
  }
}
