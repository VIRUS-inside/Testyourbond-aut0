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















public class Flick
  extends WebElementHandler<Void>
  implements JsonParametersAware
{
  private static final String ELEMENT = "element";
  private static final String XOFFSET = "xoffset";
  private static final String YOFFSET = "yoffset";
  private static final String SPEED = "speed";
  private static final String XSPEED = "xspeed";
  private static final String YSPEED = "yspeed";
  private String elementId;
  private int xOffset;
  private int yOffset;
  private int speed;
  private int xSpeed;
  private int ySpeed;
  
  public Flick(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    TouchScreen touchScreen = ((HasTouchScreen)getDriver()).getTouch();
    
    if (elementId != null) {
      WebElement element = getKnownElements().get(elementId);
      Coordinates elementLocation = ((Locatable)element).getCoordinates();
      touchScreen.flick(elementLocation, xOffset, yOffset, speed);
    } else {
      touchScreen.flick(xSpeed, ySpeed);
    }
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[Flick]", new Object[0]);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    if ((allParameters.containsKey("element")) && (allParameters.get("element") != null)) {
      elementId = ((String)allParameters.get("element"));
      try {
        xOffset = ((Number)allParameters.get("xoffset")).intValue();
      } catch (ClassCastException ex) {
        throw new WebDriverException("Illegal (non-numeric) x offset value for flick passed: " + allParameters.get("xoffset"), ex);
      }
      try {
        yOffset = ((Number)allParameters.get("yoffset")).intValue();
      } catch (ClassCastException ex) {
        throw new WebDriverException("Illegal (non-numeric) y offset value for flick passed: " + allParameters.get("yoffset"), ex);
      }
      try {
        speed = ((Number)allParameters.get("speed")).intValue();
      } catch (ClassCastException ex) {
        throw new WebDriverException("Illegal (non-numeric) speed value for flick passed: " + allParameters.get("speed"), ex);
      }
    } else if ((allParameters.containsKey("xspeed")) && (allParameters.containsKey("yspeed"))) {
      try {
        xSpeed = ((Number)allParameters.get("xspeed")).intValue();
      } catch (ClassCastException ex) {
        throw new WebDriverException("Illegal (non-numeric) x speed value for flick passed: " + allParameters.get("xspeed"), ex);
      }
      try {
        ySpeed = ((Number)allParameters.get("yspeed")).intValue();
      } catch (ClassCastException ex) {
        throw new WebDriverException("Illegal (non-numeric) y speed value for flick passed: " + allParameters.get("yspeed"), ex);
      }
    }
  }
}
