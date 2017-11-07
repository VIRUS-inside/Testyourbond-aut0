package org.seleniumhq.jetty9.server;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.seleniumhq.jetty9.io.AbstractConnection;
import org.seleniumhq.jetty9.io.Connection.Listener;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.util.ArrayUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.ssl.SslContextFactory;































@ManagedObject
public abstract class AbstractConnectionFactory
  extends ContainerLifeCycle
  implements ConnectionFactory
{
  private final String _protocol;
  private final List<String> _protocols;
  private int _inputbufferSize = 8192;
  
  protected AbstractConnectionFactory(String protocol)
  {
    _protocol = protocol;
    _protocols = Collections.unmodifiableList(Arrays.asList(new String[] { protocol }));
  }
  
  protected AbstractConnectionFactory(String... protocols)
  {
    _protocol = protocols[0];
    _protocols = Collections.unmodifiableList(Arrays.asList(protocols));
  }
  

  @ManagedAttribute(value="The protocol name", readonly=true)
  public String getProtocol()
  {
    return _protocol;
  }
  

  public List<String> getProtocols()
  {
    return _protocols;
  }
  
  @ManagedAttribute("The buffer size used to read from the network")
  public int getInputBufferSize()
  {
    return _inputbufferSize;
  }
  
  public void setInputBufferSize(int size)
  {
    _inputbufferSize = size;
  }
  
  protected AbstractConnection configure(AbstractConnection connection, Connector connector, EndPoint endPoint)
  {
    connection.setInputBufferSize(getInputBufferSize());
    
    ContainerLifeCycle aggregate;
    if ((connector instanceof ContainerLifeCycle))
    {
      aggregate = (ContainerLifeCycle)connector;
      for (Connection.Listener listener : aggregate.getBeans(Connection.Listener.class)) {
        connection.addListener(listener);
      }
    }
    for (Connection.Listener listener : getBeans(Connection.Listener.class)) {
      connection.addListener(listener);
    }
    return connection;
  }
  

  public String toString()
  {
    return String.format("%s@%x%s", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), getProtocols() });
  }
  
  public static ConnectionFactory[] getFactories(SslContextFactory sslContextFactory, ConnectionFactory... factories)
  {
    factories = (ConnectionFactory[])ArrayUtil.removeNulls(factories);
    
    if (sslContextFactory == null) {
      return factories;
    }
    for (ConnectionFactory factory : factories)
    {
      if ((factory instanceof HttpConfiguration.ConnectionFactory))
      {
        HttpConfiguration config = ((HttpConfiguration.ConnectionFactory)factory).getHttpConfiguration();
        if (config.getCustomizer(SecureRequestCustomizer.class) == null)
          config.addCustomizer(new SecureRequestCustomizer());
      }
    }
    return (ConnectionFactory[])ArrayUtil.prependToArray(new SslConnectionFactory(sslContextFactory, factories[0].getProtocol()), factories, ConnectionFactory.class);
  }
}
