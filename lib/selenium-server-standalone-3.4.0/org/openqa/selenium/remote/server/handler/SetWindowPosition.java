package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;













public class SetWindowPosition
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private Point position;
  
  public SetWindowPosition(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception
  {
    try {
      x = ((Number)allParameters.get("x")).intValue();
    } catch (ClassCastException ex) { int x;
      throw new WebDriverException("Illegal (non-numeric) x window position value passed: " + allParameters.get("x"), ex);
    }
    int x;
    try { y = ((Number)allParameters.get("y")).intValue();
    } catch (ClassCastException ex) { int y;
      throw new WebDriverException("Illegal (non-numeric) y window position value passed: " + allParameters.get("y"), ex);
    }
    int y;
    position = new Point(x, y);
  }
  
  public Void call() throws Exception
  {
    getDriver().manage().window().setPosition(position);
    return null;
  }
  
  public String toString()
  {
    return "[set window position]";
  }
}
