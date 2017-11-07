package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.remote.server.Session;













public class DismissAlert
  extends WebDriverHandler<Void>
{
  public DismissAlert(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getDriver().switchTo().alert().dismiss();
    return null;
  }
  
  public String toString()
  {
    return "[dismiss alert]";
  }
}
