package org.openqa.selenium.remote.server.handler.html5;

import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;














public class GetLocalStorageSize
  extends WebDriverHandler<Integer>
{
  public GetLocalStorageSize(Session session)
  {
    super(session);
  }
  
  public Integer call() throws Exception
  {
    return Integer.valueOf(Utils.getWebStorage(getUnwrappedDriver()).getLocalStorage().size());
  }
  
  public String toString()
  {
    return "[get local storage size]";
  }
}
