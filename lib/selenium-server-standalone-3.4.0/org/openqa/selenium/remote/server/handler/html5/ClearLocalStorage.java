package org.openqa.selenium.remote.server.handler.html5;

import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;














public class ClearLocalStorage
  extends WebDriverHandler<Void>
{
  public ClearLocalStorage(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    Utils.getWebStorage(getUnwrappedDriver()).getLocalStorage().clear();
    return null;
  }
  
  public String toString()
  {
    return "[clear local storage]";
  }
}
