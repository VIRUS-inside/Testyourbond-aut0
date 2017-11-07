package org.openqa.selenium.remote.mobile;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.openqa.selenium.mobile.NetworkConnection;
import org.openqa.selenium.mobile.NetworkConnection.ConnectionType;
import org.openqa.selenium.remote.ExecuteMethod;


















public class RemoteNetworkConnection
  implements NetworkConnection
{
  private final ExecuteMethod executeMethod;
  
  public RemoteNetworkConnection(ExecuteMethod executeMethod)
  {
    this.executeMethod = executeMethod;
  }
  
  public NetworkConnection.ConnectionType getNetworkConnection()
  {
    return new NetworkConnection.ConnectionType(((Number)executeMethod.execute("getNetworkConnection", null))
      .intValue());
  }
  

  public NetworkConnection.ConnectionType setNetworkConnection(NetworkConnection.ConnectionType type)
  {
    Map<String, NetworkConnection.ConnectionType> mode = ImmutableMap.of("type", type);
    return new NetworkConnection.ConnectionType(((Number)executeMethod.execute("setNetworkConnection", 
    
      ImmutableMap.of("parameters", mode)))
      .intValue());
  }
}
