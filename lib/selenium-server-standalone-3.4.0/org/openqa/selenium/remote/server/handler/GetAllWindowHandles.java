package org.openqa.selenium.remote.server.handler;

import java.util.Set;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.Session;
















public class GetAllWindowHandles
  extends WebDriverHandler<Set<String>>
{
  public GetAllWindowHandles(Session session)
  {
    super(session);
  }
  
  public Set<String> call() throws Exception
  {
    return getDriver().getWindowHandles();
  }
  
  public String toString()
  {
    return "[get window handles]";
  }
}
