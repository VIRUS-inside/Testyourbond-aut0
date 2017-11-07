package org.openqa.selenium.remote.server.handler.html5;

import java.util.Set;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;















public class GetLocalStorageKeys
  extends WebDriverHandler<Set<String>>
{
  public GetLocalStorageKeys(Session session)
  {
    super(session);
  }
  
  public Set<String> call() throws Exception
  {
    return 
      Utils.getWebStorage(getUnwrappedDriver()).getLocalStorage().keySet();
  }
  
  public String toString()
  {
    return "[get local storage key set]";
  }
}
