package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.remote.server.Session;
















public class SwitchToParentFrame
  extends WebDriverHandler<Void>
{
  public SwitchToParentFrame(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    WebDriver driver = getDriver();
    driver.switchTo().parentFrame();
    
    return null;
  }
  
  public String toString()
  {
    return "[switch to parent frame]";
  }
}
