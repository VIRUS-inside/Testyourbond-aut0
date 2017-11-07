package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.log.LoggingManager;
import org.openqa.selenium.remote.server.log.PerSessionLogHandler;




















public class DeleteSession
  extends WebDriverHandler<Void>
{
  public DeleteSession(Session session)
  {
    super(session);
  }
  
  public Void call()
    throws Exception
  {
    WebDriver driver = getDriver();
    if (driver == null) {
      return null;
    }
    try
    {
      LoggingManager.perSessionLogHandler().fetchAndStoreLogsFromDriver(getSessionId(), driver);
    }
    catch (Throwable localThrowable) {}
    


    driver.quit();
    

    PerSessionLogHandler logHandler = LoggingManager.perSessionLogHandler();
    








    logHandler.transferThreadTempLogsToSessionLogs(getSessionId());
    return null;
  }
  
  public String toString()
  {
    return String.format("[delete session: %s]", new Object[] { getRealSessionId() });
  }
}
