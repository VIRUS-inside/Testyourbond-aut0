package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.remote.server.Session;














public class GoForward
  extends WebDriverHandler<Void>
{
  public GoForward(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getDriver().navigate().forward();
    
    return null;
  }
  
  public String toString()
  {
    return "[go forward]";
  }
}
