package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.Session;
















public class GetCurrentUrl
  extends WebDriverHandler<String>
{
  public GetCurrentUrl(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    return getDriver().getCurrentUrl();
  }
  
  public String toString()
  {
    return "[get current url]";
  }
}
