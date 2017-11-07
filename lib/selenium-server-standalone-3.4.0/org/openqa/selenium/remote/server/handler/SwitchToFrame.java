package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.internal.ArgumentConverter;














public class SwitchToFrame
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private volatile Object id;
  
  public SwitchToFrame(Session session)
  {
    super(session);
  }
  
  public void setId(Object id) {
    this.id = id;
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    setId(new ArgumentConverter(getKnownElements()).apply(allParameters.get("id")));
  }
  
  public Void call() throws Exception
  {
    if (id == null) {
      getDriver().switchTo().defaultContent();
    } else if ((id instanceof Number)) {
      getDriver().switchTo().frame(((Number)id).intValue());
    } else if ((id instanceof WebElement)) {
      getDriver().switchTo().frame((WebElement)id);
    } else if ((id instanceof String)) {
      getDriver().switchTo().frame((String)id);
    } else {
      throw new IllegalArgumentException("Unsupported frame locator: " + id.getClass().getName());
    }
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[switch to frame: %s]", new Object[] { id == null ? "default" : id });
  }
}
