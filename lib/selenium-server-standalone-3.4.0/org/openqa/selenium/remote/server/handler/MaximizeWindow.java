package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.remote.server.Session;













public class MaximizeWindow
  extends WebDriverHandler<Void>
{
  public MaximizeWindow(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getDriver().manage().window().maximize();
    return null;
  }
  
  public String toString()
  {
    return "[maximise window]";
  }
}
