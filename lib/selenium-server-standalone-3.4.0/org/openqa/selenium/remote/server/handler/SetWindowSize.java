package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;













public class SetWindowSize
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private Dimension size;
  
  public SetWindowSize(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception
  {
    try {
      width = ((Number)allParameters.get("width")).intValue();
    } catch (ClassCastException ex) { int width;
      throw new WebDriverException("Illegal (non-numeric) window width value passed: " + allParameters.get("width"), ex);
    }
    int width;
    try { height = ((Number)allParameters.get("height")).intValue();
    } catch (ClassCastException ex) { int height;
      throw new WebDriverException("Illegal (non-numeric) window height value passed: " + allParameters.get("height"), ex);
    }
    int height;
    size = new Dimension(width, height);
  }
  
  public Void call() throws Exception
  {
    getDriver().manage().window().setSize(size);
    return null;
  }
  
  public String toString()
  {
    return "[set window size]";
  }
}
