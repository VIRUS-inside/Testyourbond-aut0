package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.server.Session;















public class GetCookie
  extends WebDriverHandler<Cookie>
{
  private String name;
  
  public GetCookie(Session session)
  {
    super(session);
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    setName((String)allParameters.get("name"));
  }
  
  public Cookie call() throws Exception
  {
    return getDriver().manage().getCookieNamed(name);
  }
  
  public String toString()
  {
    return String.format("[get cookie named %s]", new Object[] { name });
  }
}
