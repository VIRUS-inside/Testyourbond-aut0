package org.openqa.selenium.remote.server.handler.html5;

import java.util.Map;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;













public class SetLocalStorageItem
  extends WebDriverHandler<Void>
  implements JsonParametersAware
{
  private volatile String key;
  private volatile String value;
  
  public SetLocalStorageItem(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    Utils.getWebStorage(getUnwrappedDriver()).getLocalStorage().setItem(key, value);
    return null;
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception
  {
    key = ((String)allParameters.get("key"));
    value = ((String)allParameters.get("value"));
  }
  
  public String toString()
  {
    return String.format("[Set local storage item pair: (%s, %s)]", new Object[] { key, value });
  }
}
