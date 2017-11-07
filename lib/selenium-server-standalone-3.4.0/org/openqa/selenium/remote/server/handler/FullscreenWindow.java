package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.remote.server.Session;













public class FullscreenWindow
  extends WebDriverHandler<Void>
{
  public FullscreenWindow(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getDriver().manage().window().fullscreen();
    return null;
  }
  
  public String toString()
  {
    return "[fullscreen window]";
  }
}
