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















public class SingleTapOnElement
  extends WebElementHandler<Void>
  implements JsonParametersAware
{
  private static final String ELEMENT = "element";
  private String elementId;
  
  public SingleTapOnElement(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    TouchScreen touchScreen = ((HasTouchScreen)getDriver()).getTouch();
    WebElement element = getKnownElements().get(elementId);
    Coordinates elementLocation = ((Locatable)element).getCoordinates();
    
    touchScreen.singleTap(elementLocation);
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[singleTap: %s]", new Object[] { elementId });
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    if ((allParameters.containsKey("element")) && (allParameters.get("element") != null)) {
      elementId = ((String)allParameters.get("element"));
    }
  }
}
