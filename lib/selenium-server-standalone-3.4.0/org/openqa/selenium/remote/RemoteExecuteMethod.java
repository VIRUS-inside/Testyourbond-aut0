package org.openqa.selenium.remote;

import java.util.Map;















public class RemoteExecuteMethod
  implements ExecuteMethod
{
  private final RemoteWebDriver driver;
  
  public RemoteExecuteMethod(RemoteWebDriver driver)
  {
    this.driver = driver;
  }
  
  public Object execute(String commandName, Map<String, ?> parameters) {
    Response response;
    Response response;
    if ((parameters == null) || (parameters.isEmpty())) {
      response = driver.execute(commandName);
    } else {
      response = driver.execute(commandName, parameters);
    }
    
    return response.getValue();
  }
}
