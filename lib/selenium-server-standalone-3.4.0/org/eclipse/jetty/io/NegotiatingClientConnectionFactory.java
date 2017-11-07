package org.eclipse.jetty.io;










public abstract class NegotiatingClientConnectionFactory
  implements ClientConnectionFactory
{
  private final ClientConnectionFactory connectionFactory;
  









  protected NegotiatingClientConnectionFactory(ClientConnectionFactory connectionFactory)
  {
    this.connectionFactory = connectionFactory;
  }
  
  public ClientConnectionFactory getClientConnectionFactory()
  {
    return connectionFactory;
  }
}
