package org.openqa.selenium.remote.server.handler.html5;

import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;














public class GetSessionStorageSize
  extends WebDriverHandler<Integer>
{
  public GetSessionStorageSize(Session session)
  {
    super(session);
  }
  
  public Integer call() throws Exception
  {
    return Integer.valueOf(Utils.getWebStorage(getUnwrappedDriver())
      .getSessionStorage().size());
  }
  
  public String toString()
  {
    return "[get session storage size]";
  }
}
