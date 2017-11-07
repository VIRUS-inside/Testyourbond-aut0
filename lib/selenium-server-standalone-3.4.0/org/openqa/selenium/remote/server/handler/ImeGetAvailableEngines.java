package org.openqa.selenium.remote.server.handler;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.server.Session;














public class ImeGetAvailableEngines
  extends WebDriverHandler<List<String>>
{
  public ImeGetAvailableEngines(Session session)
  {
    super(session);
  }
  
  public List<String> call() throws Exception
  {
    return getDriver().manage().ime().getAvailableEngines();
  }
}
