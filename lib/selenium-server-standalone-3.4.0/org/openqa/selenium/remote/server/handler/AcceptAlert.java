package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.remote.server.Session;












public class AcceptAlert
  extends WebDriverHandler<Void>
{
  public AcceptAlert(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception {
    getDriver().switchTo().alert().accept();
    return null;
  }
  
  public String toString()
  {
    return "[accept alert]";
  }
}
