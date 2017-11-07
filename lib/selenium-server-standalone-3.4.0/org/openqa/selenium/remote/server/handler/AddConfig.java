package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.server.DriverSessions;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.rest.RestishHandler;
















public class AddConfig
  implements RestishHandler<Void>, JsonParametersAware
{
  private final DriverSessions allSessions;
  private volatile Capabilities desiredCapabilities;
  private volatile String className;
  
  public AddConfig(DriverSessions allSessions)
  {
    this.allSessions = allSessions;
  }
  
  public void setJsonParameters(Map<String, Object> allParameters)
    throws Exception
  {
    Map<String, Object> capabilitiesMap = (Map)allParameters.get("capabilities");
    desiredCapabilities = new DesiredCapabilities(capabilitiesMap);
    className = ((String)allParameters.get("class"));
  }
  
  public Void handle()
    throws Exception
  {
    Class<? extends WebDriver> clazz = Class.forName(className).asSubclass(WebDriver.class);
    
    allSessions.registerDriver(desiredCapabilities, clazz);
    
    return null;
  }
}
