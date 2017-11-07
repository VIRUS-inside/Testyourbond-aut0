package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.remote.Command;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.ErrorHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;



















public class W3CActions
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private Map<String, Object> allParameters;
  
  public W3CActions(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception
  {
    this.allParameters = allParameters;
  }
  
  public Void call() throws Exception
  {
    RemoteWebDriver driver = (RemoteWebDriver)getUnwrappedDriver();
    CommandExecutor executor = driver.getCommandExecutor();
    
    long start = System.currentTimeMillis();
    Command command = new Command(driver.getSessionId(), "actions", allParameters);
    Response response = executor.execute(command);
    
    new ErrorHandler(true)
      .throwIfResponseFailed(response, System.currentTimeMillis() - start);
    
    return null;
  }
}
