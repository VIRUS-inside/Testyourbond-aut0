package org.openqa.selenium.remote.server.handler.html5;

import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;














public class ClearSessionStorage
  extends WebDriverHandler<Void>
{
  public ClearSessionStorage(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    Utils.getWebStorage(getUnwrappedDriver()).getSessionStorage().clear();
    return null;
  }
  
  public String toString()
  {
    return "[clear session storage]";
  }
}
