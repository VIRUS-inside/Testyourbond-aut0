package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.server.Session;













public class ImeDeactivate
  extends WebDriverHandler<Void>
{
  public ImeDeactivate(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getDriver().manage().ime().deactivate();
    return null;
  }
}
