package org.openqa.selenium.remote;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogLevelMapping;
import org.openqa.selenium.logging.LoggingPreferences;


























public class DesiredCapabilities
  implements Serializable, Capabilities
{
  private final Map<String, Object> capabilities = new HashMap();
  
  public DesiredCapabilities(String browser, String version, Platform platform) {
    setCapability("browserName", browser);
    setCapability("version", version);
    setCapability("platform", platform);
  }
  

  public DesiredCapabilities() {}
  
  public DesiredCapabilities(Map<String, ?> rawMap)
  {
    capabilities.putAll(rawMap);
    if (rawMap.containsKey("unexpectedAlertBehaviour")) {
      capabilities.put("unhandledPromptBehavior", rawMap.get("unexpectedAlertBehaviour"));
    }
    
    if ((rawMap.containsKey("loggingPrefs")) && ((rawMap.get("loggingPrefs") instanceof Map))) {
      LoggingPreferences prefs = new LoggingPreferences();
      Map<String, String> prefsMap = (Map)rawMap.get("loggingPrefs");
      
      for (String logType : prefsMap.keySet()) {
        prefs.enable(logType, LogLevelMapping.toLevel((String)prefsMap.get(logType)));
      }
      capabilities.put("loggingPrefs", prefs);
    }
    
    Object value = capabilities.get("platform");
    if ((value instanceof String)) {
      try {
        capabilities.put("platform", Platform.fromString((String)value));
      }
      catch (WebDriverException localWebDriverException) {}
    }
  }
  
  public DesiredCapabilities(Capabilities other)
  {
    if (other != null) {
      merge(other);
    }
  }
  
  public DesiredCapabilities(Capabilities... others) {
    for (Capabilities caps : others) {
      if (caps != null) {
        merge(caps);
      }
    }
  }
  
  public void setBrowserName(String browserName) {
    setCapability("browserName", browserName);
  }
  
  public void setVersion(String version) {
    setCapability("version", version);
  }
  
  public void setPlatform(Platform platform) {
    setCapability("platform", platform);
  }
  
  public void setJavascriptEnabled(boolean javascriptEnabled) {
    setCapability("javascriptEnabled", javascriptEnabled);
  }
  
  public boolean acceptInsecureCerts() {
    if (capabilities.containsKey("acceptInsecureCerts")) {
      Object raw = capabilities.get("acceptInsecureCerts");
      if ((raw instanceof String))
        return Boolean.parseBoolean((String)raw);
      if ((raw instanceof Boolean)) {
        return ((Boolean)raw).booleanValue();
      }
    }
    return true;
  }
  
  public void setAcceptInsecureCerts(boolean acceptInsecureCerts) {
    setCapability("acceptInsecureCerts", acceptInsecureCerts);
  }
  
  public Object getCapability(String capabilityName) {
    return capabilities.get(capabilityName);
  }
  








  public DesiredCapabilities merge(Capabilities extraCapabilities)
  {
    if (extraCapabilities != null) {
      capabilities.putAll(extraCapabilities.asMap());
      if (extraCapabilities.getCapability("unexpectedAlertBehaviour") != null) {
        capabilities.put("unhandledPromptBehavior", extraCapabilities.getCapability("unexpectedAlertBehaviour"));
      }
    }
    return this;
  }
  
  public void setCapability(String capabilityName, boolean value) {
    capabilities.put(capabilityName, Boolean.valueOf(value));
  }
  
  public void setCapability(String capabilityName, String value) {
    if ("platform".equals(capabilityName)) {
      try {
        capabilities.put(capabilityName, Platform.fromString(value));
      } catch (WebDriverException ex) {
        capabilities.put(capabilityName, value);
      }
    } else {
      if ("unexpectedAlertBehaviour".equals(capabilityName)) {
        capabilities.put("unhandledPromptBehavior", value);
      }
      capabilities.put(capabilityName, value);
    }
  }
  
  public void setCapability(String capabilityName, Platform value) {
    capabilities.put(capabilityName, value);
  }
  
  public void setCapability(String key, Object value) {
    if (("platform".equals(key)) && ((value instanceof String))) {
      capabilities.put(key, Platform.fromString((String)value));
    } else {
      capabilities.put(key, value);
    }
  }
  
  public Map<String, ?> asMap() {
    return Collections.unmodifiableMap(capabilities);
  }
  
  public static DesiredCapabilities android() {
    return new DesiredCapabilities("android", "", Platform.ANDROID);
  }
  
  public static DesiredCapabilities chrome() {
    return new DesiredCapabilities("chrome", "", Platform.ANY);
  }
  
  public static DesiredCapabilities firefox() {
    DesiredCapabilities capabilities = new DesiredCapabilities("firefox", "", Platform.ANY);
    


    capabilities.setCapability("acceptInsecureCerts", true);
    
    return capabilities;
  }
  
  public static DesiredCapabilities htmlUnit() {
    return new DesiredCapabilities("htmlunit", "", Platform.ANY);
  }
  

  public static DesiredCapabilities edge() { return new DesiredCapabilities("MicrosoftEdge", "", Platform.WINDOWS); }
  
  public static DesiredCapabilities internetExplorer() {
    DesiredCapabilities capabilities = new DesiredCapabilities("internet explorer", "", Platform.WINDOWS);
    
    capabilities.setCapability("ensureCleanSession", true);
    return capabilities;
  }
  
  public static DesiredCapabilities iphone() {
    return new DesiredCapabilities("iPhone", "", Platform.MAC);
  }
  
  public static DesiredCapabilities ipad() {
    return new DesiredCapabilities("iPad", "", Platform.MAC);
  }
  



  @Deprecated
  public static DesiredCapabilities opera()
  {
    return new DesiredCapabilities("opera", "", Platform.ANY);
  }
  
  public static DesiredCapabilities operaBlink() {
    return new DesiredCapabilities("operablink", "", Platform.ANY);
  }
  
  public static DesiredCapabilities safari() {
    return new DesiredCapabilities("safari", "", Platform.MAC);
  }
  
  public static DesiredCapabilities phantomjs() {
    return new DesiredCapabilities("phantomjs", "", Platform.ANY);
  }
  
  public String toString()
  {
    return String.format("Capabilities [%s]", new Object[] { shortenMapValues(capabilities) });
  }
  
  private Map<String, Object> shortenMapValues(Map<String, Object> map) {
    Map<String, Object> newMap = new HashMap();
    
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if ((entry.getValue() instanceof Map)) {
        newMap.put(entry.getKey(), 
          shortenMapValues((Map)entry.getValue()));
      } else {
        String value = String.valueOf(entry.getValue());
        if (value.length() > 1024) {
          value = value.substring(0, 29) + "...";
        }
        newMap.put(entry.getKey(), value);
      }
    }
    
    return newMap;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DesiredCapabilities)) {
      return false;
    }
    
    DesiredCapabilities that = (DesiredCapabilities)o;
    
    return capabilities.equals(capabilities);
  }
  
  public int hashCode()
  {
    return capabilities.hashCode();
  }
}
