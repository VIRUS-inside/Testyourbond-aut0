package org.openqa.selenium.remote.html5;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.remote.ExecuteMethod;


















public class RemoteLocationContext
  implements LocationContext
{
  private final ExecuteMethod executeMethod;
  
  public RemoteLocationContext(ExecuteMethod executeMethod)
  {
    this.executeMethod = executeMethod;
  }
  

  public Location location()
  {
    Map<String, Double> result = (Map)executeMethod.execute("getLocation", null);
    
    if (result == null) {
      return null;
    }
    return new Location(((Double)result.get("latitude")).doubleValue(), ((Double)result.get("longitude")).doubleValue(), ((Double)result.get("altitude")).doubleValue());
  }
  
  public void setLocation(Location location)
  {
    Map<String, Location> args = ImmutableMap.of("location", location);
    executeMethod.execute("setLocation", args);
  }
}
