package org.seleniumhq.jetty9.server.jmx;

import org.seleniumhq.jetty9.jmx.ObjectMBean;
import org.seleniumhq.jetty9.server.AbstractConnector;
import org.seleniumhq.jetty9.server.ConnectionFactory;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;

















@ManagedObject("MBean Wrapper for Connectors")
public class AbstractConnectorMBean
  extends ObjectMBean
{
  final AbstractConnector _connector;
  
  public AbstractConnectorMBean(Object managedObject)
  {
    super(managedObject);
    _connector = ((AbstractConnector)managedObject);
  }
  

  public String getObjectContextBasis()
  {
    StringBuilder buffer = new StringBuilder();
    for (ConnectionFactory f : _connector.getConnectionFactories())
    {
      String protocol = f.getProtocol();
      if (protocol != null)
      {
        if (buffer.length() > 0)
          buffer.append("|");
        buffer.append(protocol);
      }
    }
    
    return String.format("%s@%x", new Object[] { buffer.toString(), Integer.valueOf(_connector.hashCode()) });
  }
}
