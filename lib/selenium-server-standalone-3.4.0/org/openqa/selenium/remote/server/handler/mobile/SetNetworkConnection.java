package org.openqa.selenium.remote.server.handler.mobile;

import java.util.Map;
import org.openqa.selenium.mobile.NetworkConnection;
import org.openqa.selenium.mobile.NetworkConnection.ConnectionType;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;
import org.openqa.selenium.remote.server.handler.html5.Utils;














public class SetNetworkConnection
  extends WebDriverHandler<Number>
  implements JsonParametersAware
{
  private volatile NetworkConnection.ConnectionType type;
  
  public SetNetworkConnection(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters)
    throws Exception
  {
    Map<String, Object> parameters = (Map)allParameters.get("parameters");
    Number bitmask = (Number)parameters.get("type");
    type = new NetworkConnection.ConnectionType(bitmask.intValue());
  }
  
  public Number call() throws Exception
  {
    return Integer.valueOf(Integer.parseInt(Utils.getNetworkConnection(getUnwrappedDriver()).setNetworkConnection(type).toString()));
  }
  
  public String toString()
  {
    return String.format("[set network connection : %s]", new Object[] { type.toString() });
  }
}
