package org.openqa.selenium.remote.server.handler;

import java.util.Set;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.server.Session;
















public class GetAllCookies
  extends WebDriverHandler<Set<Cookie>>
{
  public GetAllCookies(Session session)
  {
    super(session);
  }
  
  public Set<Cookie> call() throws Exception
  {
    return getDriver().manage().getCookies();
  }
  
  public String toString()
  {
    return "[get all cookies]";
  }
}
