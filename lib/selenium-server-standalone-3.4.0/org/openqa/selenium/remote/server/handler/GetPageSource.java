package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.Session;















public class GetPageSource
  extends WebDriverHandler<String>
{
  public GetPageSource(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    return getDriver().getPageSource();
  }
  
  public String toString()
  {
    return "[get page source]";
  }
}
