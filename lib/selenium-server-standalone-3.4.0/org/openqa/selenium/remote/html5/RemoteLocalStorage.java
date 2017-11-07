package org.openqa.selenium.remote.html5;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.remote.ExecuteMethod;





















public class RemoteLocalStorage
  implements LocalStorage
{
  private final ExecuteMethod executeMethod;
  
  public RemoteLocalStorage(ExecuteMethod executeMethod)
  {
    this.executeMethod = executeMethod;
  }
  
  public String getItem(String key)
  {
    Map<String, String> args = ImmutableMap.of("key", key);
    return (String)executeMethod.execute("getLocalStorageItem", args);
  }
  


  public Set<String> keySet()
  {
    Collection<String> result = (Collection)executeMethod.execute("getLocalStorageKeys", null);
    return new HashSet(result);
  }
  
  public void setItem(String key, String value)
  {
    Map<String, String> args = ImmutableMap.of("key", key, "value", value);
    executeMethod.execute("setLocalStorageItem", args);
  }
  
  public String removeItem(String key)
  {
    Map<String, String> args = ImmutableMap.of("key", key);
    return (String)executeMethod.execute("removeLocalStorageItem", args);
  }
  
  public void clear()
  {
    executeMethod.execute("clearLocalStorage", null);
  }
  
  public int size()
  {
    Object response = executeMethod.execute("getLocalStorageSize", null);
    return Integer.parseInt(response.toString());
  }
}
