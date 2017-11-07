package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.security.UserAndPassword;












public class SetAlertCredentials
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private String username;
  private String password;
  
  public SetAlertCredentials(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    username = ((String)allParameters.get("username"));
    password = ((String)allParameters.get("password"));
  }
  
  public Void call() throws Exception
  {
    Credentials credentials = new UserAndPassword(username, password);
    getDriver().switchTo().alert().setCredentials(credentials);
    return null;
  }
  
  public String toString()
  {
    return "[set alert credentials]";
  }
}
