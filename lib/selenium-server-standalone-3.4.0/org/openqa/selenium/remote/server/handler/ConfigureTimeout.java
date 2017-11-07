package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;













public class ConfigureTimeout
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private volatile String type;
  private volatile long millis;
  
  public ConfigureTimeout(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    type = ((String)allParameters.get("type"));
    try {
      millis = ((Number)allParameters.get("ms")).longValue();
    } catch (ClassCastException ex) {
      throw new WebDriverException("Illegal (non-numeric) timeout value passed: " + allParameters.get("ms"), ex);
    }
  }
  
  public Void call() throws Exception
  {
    if ("implicit".equals(type)) {
      getDriver().manage().timeouts().implicitlyWait(millis, TimeUnit.MILLISECONDS);
    } else if ("page load".equals(type)) {
      getDriver().manage().timeouts().pageLoadTimeout(millis, TimeUnit.MILLISECONDS);
    } else if ("script".equals(type)) {
      getDriver().manage().timeouts().setScriptTimeout(millis, TimeUnit.MILLISECONDS);
    } else {
      throw new WebDriverException("Unknown wait type: " + type);
    }
    return null;
  }
  
  public String toString()
  {
    return String.format("[%s wait: %s]", new Object[] { type, Long.valueOf(millis) });
  }
}
