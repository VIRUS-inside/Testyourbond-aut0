package org.openqa.selenium.remote.server.handler.html5;

import org.openqa.selenium.html5.AppCacheStatus;
import org.openqa.selenium.html5.ApplicationCache;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.handler.WebDriverHandler;















public class GetAppCacheStatus
  extends WebDriverHandler<AppCacheStatus>
{
  public GetAppCacheStatus(Session session)
  {
    super(session);
  }
  
  public AppCacheStatus call() throws Exception
  {
    return Utils.getApplicationCache(getUnwrappedDriver()).getStatus();
  }
  
  public String toString()
  {
    return "[get application cache status]";
  }
}
