package org.openqa.selenium.remote.html5;

import org.openqa.selenium.html5.AppCacheStatus;
import org.openqa.selenium.html5.ApplicationCache;
import org.openqa.selenium.remote.ExecuteMethod;




















public class RemoteApplicationCache
  implements ApplicationCache
{
  private final ExecuteMethod executeMethod;
  
  public RemoteApplicationCache(ExecuteMethod executeMethod)
  {
    this.executeMethod = executeMethod;
  }
  
  public AppCacheStatus getStatus()
  {
    String result = (String)executeMethod.execute("getStatus", null);
    return AppCacheStatus.valueOf(result);
  }
}
