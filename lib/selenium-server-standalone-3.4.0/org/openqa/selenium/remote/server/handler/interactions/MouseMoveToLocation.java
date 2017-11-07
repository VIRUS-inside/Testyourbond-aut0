package org.openqa.selenium.remote.server.handler.interactions;

import java.util.Map;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;















public class MouseMoveToLocation
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private static final String XOFFSET = "xoffset";
  private static final String YOFFSET = "yoffset";
  private static final String ELEMENT = "element";
  String elementId;
  boolean elementProvided = false;
  int xOffset = 0;
  int yOffset = 0;
  boolean offsetsProvided = false;
  
  public MouseMoveToLocation(Session session) {
    super(session);
  }
  
  public Void call() throws Exception
  {
    Mouse mouse = ((HasInputDevices)getDriver()).getMouse();
    
    Coordinates elementLocation = null;
    if (elementProvided) {
      WebElement element = getKnownElements().get(elementId);
      elementLocation = ((Locatable)element).getCoordinates();
    }
    
    if (offsetsProvided) {
      mouse.mouseMove(elementLocation, xOffset, yOffset);
    } else {
      mouse.mouseMove(elementLocation);
    }
    return null;
  }
  
  public String toString()
  {
    return String.format("[mousemove: %s %b]", new Object[] { elementId, Boolean.valueOf(offsetsProvided) });
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    if ((allParameters.containsKey("element")) && (allParameters.get("element") != null)) {
      elementId = ((String)allParameters.get("element"));
      elementProvided = true;
    } else {
      elementProvided = false;
    }
    
    if ((allParameters.containsKey("xoffset")) && (allParameters.containsKey("yoffset"))) {
      try {
        xOffset = ((Number)allParameters.get("xoffset")).intValue();
      } catch (ClassCastException ex) {
        throw new WebDriverException("Illegal (non-numeric) x offset value for mouse move passed: " + allParameters.get("xoffset"), ex);
      }
      try {
        yOffset = ((Number)allParameters.get("yoffset")).intValue();
      } catch (ClassCastException ex) {
        throw new WebDriverException("Illegal (non-numeric) y offset value for mouse move passed: " + allParameters.get("yoffset"), ex);
      }
      offsetsProvided = true;
    } else {
      offsetsProvided = false;
    }
  }
}
