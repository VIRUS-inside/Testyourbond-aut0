package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.Maps;
import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.server.Session;
















public class GetSessionCapabilities
  extends WebDriverHandler<Map<String, Object>>
{
  public GetSessionCapabilities(Session session)
  {
    super(session);
  }
  
  public Map<String, Object> call()
  {
    Session session = getSession();
    Map<String, Object> capabilities = session.getCapabilities().asMap();
    capabilities = Maps.newHashMap(capabilities);
    


    capabilities.put("webdriver.remote.sessionid", session.getSessionId().toString());
    
    return describeSession(capabilities);
  }
  
  protected Map<String, Object> describeSession(Map<String, Object> capabilities) {
    return capabilities;
  }
}
