package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.server.Session;













public class ImeIsActivated
  extends WebDriverHandler<Boolean>
{
  public ImeIsActivated(Session session)
  {
    super(session);
  }
  
  public Boolean call() throws Exception
  {
    return Boolean.valueOf(getDriver().manage().ime().isActivated());
  }
}
