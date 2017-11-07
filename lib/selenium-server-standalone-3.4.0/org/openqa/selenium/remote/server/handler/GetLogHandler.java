package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.log.LoggingManager;
import org.openqa.selenium.remote.server.log.PerSessionLogHandler;















public class GetLogHandler
  extends WebDriverHandler<LogEntries>
  implements JsonParametersAware
{
  private volatile String type;
  
  public GetLogHandler(Session session)
  {
    super(session);
  }
  
  public LogEntries call() throws Exception
  {
    if ("server".equals(type)) {
      return LoggingManager.perSessionLogHandler().getSessionLog(getSessionId());
    }
    return getDriver().manage().logs().get(type);
  }
  
  public String toString()
  {
    return String.format("[fetching logs for: %s]", new Object[] { type });
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) {
    type = ((String)allParameters.get("type"));
  }
}
