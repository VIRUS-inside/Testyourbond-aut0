package org.openqa.selenium.remote.server.handler.mobile;

import org.openqa.selenium.mobile.NetworkConnection;
import org.openqa.selenium.mobile.NetworkConnection.ConnectionType;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;
import org.openqa.selenium.remote.server.handler.html5.Utils;















public class GetNetworkConnection
  extends WebDriverHandler<NetworkConnection.ConnectionType>
{
  public GetNetworkConnection(Session session)
  {
    super(session);
  }
  
  public NetworkConnection.ConnectionType call() throws Exception
  {
    return Utils.getNetworkConnection(getUnwrappedDriver()).getNetworkConnection();
  }
  
  public String toString()
  {
    return "[get network connection state]";
  }
}
