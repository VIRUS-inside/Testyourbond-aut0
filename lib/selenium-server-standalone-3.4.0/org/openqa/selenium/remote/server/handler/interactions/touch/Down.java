package org.openqa.selenium.remote.server.handler.interactions.touch;

import java.util.Map;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebElementHandler;
















public class Down
  extends WebElementHandler<Void>
  implements JsonParametersAware
{
  private static final String X = "x";
  private static final String Y = "y";
  private int x;
  private int y;
  
  public Down(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    TouchScreen touchScreen = ((HasTouchScreen)getDriver()).getTouch();
    
    touchScreen.down(x, y);
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[Down]", new Object[0]);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    try {
      x = ((Number)allParameters.get("x")).intValue();
    } catch (ClassCastException ex) {
      throw new WebDriverException("Illegal (non-numeric) x touch down position value passed: " + allParameters.get("x"), ex);
    }
    try {
      y = ((Number)allParameters.get("y")).intValue();
    } catch (ClassCastException ex) {
      throw new WebDriverException("Illegal (non-numeric) y touch down position value passed: " + allParameters.get("y"), ex);
    }
  }
}
