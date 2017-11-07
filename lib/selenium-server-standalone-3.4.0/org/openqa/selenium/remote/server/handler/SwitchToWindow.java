package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;














public class SwitchToWindow
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private volatile String name;
  
  public SwitchToWindow(Session session)
  {
    super(session);
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    if (allParameters.containsKey("name")) {
      setName((String)allParameters.get("name"));
    } else {
      setName((String)allParameters.get("handle"));
    }
  }
  
  public Void call() throws Exception
  {
    getDriver().switchTo().window(name);
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[switch to window: %s]", new Object[] { name });
  }
}
