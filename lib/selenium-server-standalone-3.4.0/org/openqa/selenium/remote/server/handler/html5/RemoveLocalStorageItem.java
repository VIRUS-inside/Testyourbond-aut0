package org.openqa.selenium.remote.server.handler.html5;

import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;













public class RemoveLocalStorageItem
  extends WebDriverHandler<String>
{
  private volatile String key;
  
  public RemoveLocalStorageItem(Session session)
  {
    super(session);
  }
  
  public void setKey(String key) {
    this.key = key;
  }
  
  public String call() throws Exception
  {
    return 
      Utils.getWebStorage(getUnwrappedDriver()).getLocalStorage().removeItem(key);
  }
  
  public String toString()
  {
    return String.format("[remove local storage item for key: %s]", new Object[] { key });
  }
}
