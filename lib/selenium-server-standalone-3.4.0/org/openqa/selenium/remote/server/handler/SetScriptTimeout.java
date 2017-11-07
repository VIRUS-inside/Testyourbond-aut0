package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;













public class SetScriptTimeout
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private volatile long millis;
  
  public SetScriptTimeout(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    try {
      millis = ((Number)allParameters.get("ms")).longValue();
    } catch (ClassCastException ex) {
      throw new WebDriverException("Illegal (non-numeric) timeout value passed: " + allParameters.get("ms"), ex);
    }
  }
  
  public Void call() throws Exception
  {
    getDriver().manage().timeouts().setScriptTimeout(millis, TimeUnit.MILLISECONDS);
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[set script timeout: %s]", new Object[] { Long.valueOf(millis) });
  }
}
