package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.remote.server.Session;















public class RefreshPage
  extends WebDriverHandler<Void>
{
  public RefreshPage(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getDriver().navigate().refresh();
    
    return null;
  }
  
  public String toString()
  {
    return "[refresh]";
  }
}
