package org.openqa.selenium.remote.server.handler.interactions.touch;

import java.util.Map;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebElementHandler;














public class LongPressOnElement
  extends WebElementHandler<Void>
  implements JsonParametersAware
{
  private static final String ELEMENT = "element";
  private String elementId;
  
  public LongPressOnElement(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    TouchScreen touchScreen = ((HasTouchScreen)getDriver()).getTouch();
    WebElement element = getKnownElements().get(elementId);
    Coordinates elementLocation = ((Locatable)element).getCoordinates();
    touchScreen.longPress(elementLocation);
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[Long press: %s]", new Object[] { elementId });
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    elementId = ((String)allParameters.get("element"));
  }
}
