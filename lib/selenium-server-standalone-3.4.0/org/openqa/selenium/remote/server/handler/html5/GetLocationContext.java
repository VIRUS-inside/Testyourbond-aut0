package org.openqa.selenium.remote.server.handler.html5;

import org.openqa.selenium.html5.Location;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;















public class GetLocationContext
  extends WebDriverHandler<Location>
{
  public GetLocationContext(Session session)
  {
    super(session);
  }
  
  public Location call() throws Exception
  {
    return Utils.getLocationContext(getUnwrappedDriver()).location();
  }
  
  public String toString()
  {
    return "[get location context]";
  }
}
