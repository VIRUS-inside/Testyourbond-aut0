package org.openqa.selenium.remote.server.handler.html5;

import java.util.Map;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;














public class SetLocationContext
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private volatile Location location;
  
  public SetLocationContext(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    Utils.getLocationContext(getUnwrappedDriver()).setLocation(location);
    return null;
  }
  
  public void setJsonParameters(Map<String, Object> allParameters)
    throws Exception
  {
    Map<Object, Object> locationMap = (Map)allParameters.get("location");
    
    try
    {
      latitude = ((Number)locationMap.get("latitude")).doubleValue();
    } catch (ClassCastException ex) { double latitude;
      throw new WebDriverException("Illegal (non-double) latitude location passed: " + locationMap.get("latitude"), ex);
    }
    double latitude;
    try
    {
      longitude = ((Number)locationMap.get("longitude")).doubleValue();
    } catch (ClassCastException ex) { double longitude;
      throw new WebDriverException("Illegal (non-double) longitude location passed: " + locationMap.get("longitude"), ex);
    }
    double longitude;
    try
    {
      altitude = ((Number)locationMap.get("altitude")).doubleValue();
    } catch (ClassCastException ex) { double altitude;
      throw new WebDriverException("Illegal (non-double) altitude location passed: " + locationMap.get("altitude"), ex);
    }
    double altitude;
    location = new Location(latitude, longitude, altitude);
  }
  
  public String toString()
  {
    return String.format("[set location context: %s]", new Object[] { location.toString() });
  }
}
