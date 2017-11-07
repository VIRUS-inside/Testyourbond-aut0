package org.openqa.selenium.remote.server.handler.html5;

import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;













public class GetLocalStorageItem
  extends WebDriverHandler<String>
{
  private volatile String key;
  
  public GetLocalStorageItem(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    return 
      Utils.getWebStorage(getUnwrappedDriver()).getLocalStorage().getItem(key);
  }
  
  public void setKey(String key) {
    this.key = key;
  }
  
  public String toString()
  {
    return String.format("[get local storage item for key: %s]", new Object[] { key });
  }
}
