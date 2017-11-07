package org.openqa.selenium.remote.html5;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.remote.ExecuteMethod;





















public class RemoteSessionStorage
  implements SessionStorage
{
  private final ExecuteMethod executeMethod;
  
  public RemoteSessionStorage(ExecuteMethod executeMethod)
  {
    this.executeMethod = executeMethod;
  }
  
  public String getItem(String key)
  {
    Map<String, String> args = ImmutableMap.of("key", key);
    return (String)executeMethod.execute("getSessionStorageItem", args);
  }
  


  public Set<String> keySet()
  {
    Collection<String> result = (Collection)executeMethod.execute("getSessionStorageKey", null);
    return new HashSet(result);
  }
  
  public void setItem(String key, String value)
  {
    Map<String, String> args = ImmutableMap.of("key", key, "value", value);
    executeMethod.execute("setSessionStorageItem", args);
  }
  
  public String removeItem(String key)
  {
    Map<String, String> args = ImmutableMap.of("key", key);
    return (String)executeMethod.execute("removeSessionStorageItem", args);
  }
  
  public void clear()
  {
    executeMethod.execute("clearSessionStorage", null);
  }
  
  public int size()
  {
    Object response = executeMethod.execute("getSessionStorageSize", null);
    return Integer.parseInt(response.toString());
  }
}
