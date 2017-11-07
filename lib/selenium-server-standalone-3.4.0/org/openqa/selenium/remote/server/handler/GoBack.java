package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.remote.server.Session;














public class GoBack
  extends WebDriverHandler<Void>
{
  public GoBack(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getDriver().navigate().back();
    
    return null;
  }
  
  public String toString()
  {
    return "[go back]";
  }
}
