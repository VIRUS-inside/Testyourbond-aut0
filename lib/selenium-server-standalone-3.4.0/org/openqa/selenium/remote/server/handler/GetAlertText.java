package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.remote.server.Session;













public class GetAlertText
  extends WebDriverHandler<String>
{
  public GetAlertText(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    return getDriver().switchTo().alert().getText();
  }
  

  public String toString()
  {
    return "[get alert text]";
  }
}
