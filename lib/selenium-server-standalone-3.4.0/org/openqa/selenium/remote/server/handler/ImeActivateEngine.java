package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;














public class ImeActivateEngine
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private String engine = null;
  
  public ImeActivateEngine(Session session) {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception
  {
    engine = ((String)allParameters.get("engine"));
  }
  
  public Void call() throws Exception
  {
    getDriver().manage().ime().activateEngine(engine);
    return null;
  }
}
