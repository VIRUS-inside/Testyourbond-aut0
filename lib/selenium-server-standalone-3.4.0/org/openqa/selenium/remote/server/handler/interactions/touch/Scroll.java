package org.openqa.selenium.remote.server.handler.interactions.touch;

import java.util.Map;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebElementHandler;















public class Scroll
  extends WebElementHandler<Void>
  implements JsonParametersAware
{
  private static final String ELEMENT = "element";
  private static final String XOFFSET = "xoffset";
  private static final String YOFFSET = "yoffset";
  String elementId;
  int xOffset;
  int yOffset;
  
  public Scroll(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    TouchScreen touchScreen = ((HasTouchScreen)getDriver()).getTouch();
    
    if (elementId != null) {
      WebElement element = getKnownElements().get(elementId);
      Coordinates elementLocation = ((Locatable)element).getCoordinates();
      touchScreen.scroll(elementLocation, xOffset, yOffset);
    } else {
      touchScreen.scroll(xOffset, yOffset);
    }
    return null;
  }
  
  public String toString()
  {
    return String.format("[scroll: %s]", new Object[] { elementId });
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    if (allParameters.containsKey("element")) {
      elementId = ((String)allParameters.get("element"));
    }
    try {
      xOffset = ((Number)allParameters.get("xoffset")).intValue();
    } catch (ClassCastException ex) {
      throw new WebDriverException("Illegal (non-numeric) x offset value for touch scroll passed: " + allParameters.get("xoffset"), ex);
    }
    try {
      yOffset = ((Number)allParameters.get("yoffset")).intValue();
    } catch (ClassCastException ex) {
      throw new WebDriverException("Illegal (non-numeric) y offset value for touch scroll passed: " + allParameters.get("yoffset"), ex);
    }
  }
}
