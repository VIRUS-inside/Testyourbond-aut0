package org.openqa.selenium.remote.server.handler.html5;

import java.util.Set;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;















public class GetSessionStorageKeys
  extends WebDriverHandler<Set<String>>
{
  public GetSessionStorageKeys(Session session)
  {
    super(session);
  }
  
  public Set<String> call() throws Exception
  {
    return 
      Utils.getWebStorage(getUnwrappedDriver()).getSessionStorage().keySet();
  }
  
  public String toString()
  {
    return "[get session storage key set]";
  }
}
