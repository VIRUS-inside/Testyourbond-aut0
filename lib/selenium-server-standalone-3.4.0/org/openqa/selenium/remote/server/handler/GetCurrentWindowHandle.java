package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.Session;















public class GetCurrentWindowHandle
  extends WebDriverHandler<String>
{
  public GetCurrentWindowHandle(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    return getDriver().getWindowHandle();
  }
  
  public String toString()
  {
    return "[get current window handle]";
  }
}
