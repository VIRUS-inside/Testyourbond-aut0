package org.openqa.selenium.remote.server.handler.interactions;

import java.util.Map;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;
















public class ClickInSession
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  volatile boolean leftMouseButton = true;
  
  public ClickInSession(Session session) {
    super(session);
  }
  
  public Void call() throws Exception
  {
    Mouse mouse = ((HasInputDevices)getDriver()).getMouse();
    
    if (leftMouseButton) {
      mouse.click(null);
    } else {
      mouse.contextClick(null);
    }
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[click: %s]", new Object[] { "nothing" });
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    if (allParameters.containsKey("button")) {
      int button = ((Long)allParameters.get("button")).intValue();
      switch (button)
      {
      case 0: 
        leftMouseButton = true;
        break;
      case 2: 
        leftMouseButton = false;
      }
    }
  }
}
