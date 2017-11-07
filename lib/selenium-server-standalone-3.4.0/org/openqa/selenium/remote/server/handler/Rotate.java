package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.Rotatable;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;















public class Rotate
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private volatile ScreenOrientation orientation;
  
  public Rotate(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    ((Rotatable)getUnwrappedDriver()).rotate(orientation);
    return null;
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    orientation = ScreenOrientation.valueOf((String)allParameters.get("orientation"));
  }
  
  public String toString()
  {
    return String.format("[set screen orientation: %s]", new Object[] { orientation.toString() });
  }
}
