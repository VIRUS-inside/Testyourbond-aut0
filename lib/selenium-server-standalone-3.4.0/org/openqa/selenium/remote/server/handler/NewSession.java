package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.Maps;
import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.remote.server.DriverSessions;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.Session;
import org.openqa.selenium.remote.server.log.LoggingManager;
import org.openqa.selenium.remote.server.log.PerSessionLogHandler;
import org.openqa.selenium.remote.server.rest.RestishHandler;

















public class NewSession
  implements RestishHandler<Response>, JsonParametersAware
{
  private volatile DriverSessions allSessions;
  private volatile Capabilities desiredCapabilities;
  private volatile SessionId sessionId;
  
  public NewSession(DriverSessions allSession)
  {
    allSessions = allSession;
  }
  
  public Capabilities getCapabilities() {
    return desiredCapabilities;
  }
  

  public void setJsonParameters(Map<String, Object> allParameters)
    throws Exception
  {
    desiredCapabilities = new DesiredCapabilities((Map)allParameters.get("desiredCapabilities"));
  }
  
  public Response handle()
    throws Exception
  {
    sessionId = allSessions.newSession(desiredCapabilities != null ? desiredCapabilities : new DesiredCapabilities());
    


    Map<String, Object> capabilities = Maps.newHashMap(allSessions.get(sessionId).getCapabilities().asMap());
    


    capabilities.put("webdriver.remote.sessionid", sessionId.toString());
    
    if (desiredCapabilities != null) {
      LoggingManager.perSessionLogHandler().configureLogging(
        (LoggingPreferences)desiredCapabilities.getCapability("loggingPrefs"));
    }
    LoggingManager.perSessionLogHandler().attachToCurrentThread(sessionId);
    
    Response response = new Response();
    response.setSessionId(sessionId.toString());
    response.setValue(capabilities);
    

    response.setStatus(Integer.valueOf(0));
    return response;
  }
  
  public String getSessionId() {
    return sessionId.toString();
  }
  
  public String toString()
  {
    return String.format("[new session: %s]", new Object[] { desiredCapabilities });
  }
}
