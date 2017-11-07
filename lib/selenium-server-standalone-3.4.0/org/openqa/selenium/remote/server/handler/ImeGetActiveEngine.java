package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.server.Session;













public class ImeGetActiveEngine
  extends WebDriverHandler<String>
{
  public ImeGetActiveEngine(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    return getDriver().manage().ime().getActiveEngine();
  }
}
