package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;












public class SetAlertText
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private String text;
  
  public SetAlertText(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    if (allParameters.containsKey("text")) {
      text = ((String)allParameters.get("text"));
    }
    else {
      text = ((String)allParameters.get("message"));
    }
  }
  
  public Void call() throws Exception
  {
    getDriver().switchTo().alert().sendKeys(text);
    return null;
  }
  
  public String toString()
  {
    return "[set alert value]";
  }
}
