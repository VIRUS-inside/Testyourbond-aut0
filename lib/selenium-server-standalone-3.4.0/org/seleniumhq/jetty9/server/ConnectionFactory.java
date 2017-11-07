package org.seleniumhq.jetty9.server;

import java.util.List;
import org.seleniumhq.jetty9.http.BadMessageException;
import org.seleniumhq.jetty9.http.HttpFields;
import org.seleniumhq.jetty9.http.MetaData.Request;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.io.EndPoint;

public abstract interface ConnectionFactory
{
  public abstract String getProtocol();
  
  public abstract List<String> getProtocols();
  
  public abstract Connection newConnection(Connector paramConnector, EndPoint paramEndPoint);
  
  public static abstract interface Upgrading
    extends ConnectionFactory
  {
    public abstract Connection upgradeConnection(Connector paramConnector, EndPoint paramEndPoint, MetaData.Request paramRequest, HttpFields paramHttpFields)
      throws BadMessageException;
  }
}
