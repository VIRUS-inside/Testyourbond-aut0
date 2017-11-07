package org.openqa.grid.internal.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.openqa.selenium.Platform;

























public class DefaultCapabilityMatcher
  implements CapabilityMatcher
{
  private static final Logger log = Logger.getLogger(DefaultCapabilityMatcher.class.getName());
  
  private static final String GRID_TOKEN = "_";
  protected final List<String> toConsider = new ArrayList();
  
  public DefaultCapabilityMatcher() {
    toConsider.add("platform");
    toConsider.add("browserName");
    toConsider.add("version");
    toConsider.add("browserVersion");
    toConsider.add("applicationName");
  }
  



  public void addToConsider(String capabilityName)
  {
    toConsider.add(capabilityName);
  }
  
  public boolean matches(Map<String, Object> nodeCapability, Map<String, Object> requestedCapability) {
    if ((nodeCapability == null) || (requestedCapability == null)) {
      return false;
    }
    for (String key : requestedCapability.keySet())
    {

      if ((!key.startsWith("_")) && (toConsider.contains(key)) && 
        (requestedCapability.get(key) != null)) {
        String value = requestedCapability.get(key).toString();
        
        if ((!"ANY".equalsIgnoreCase(value)) && (!"".equals(value)) && (!"*".equals(value))) {
          switch (key) {
          case "platform": 
            Platform requested = extractPlatform(requestedCapability.get(key));
            if (requested != null) {
              Platform node = extractPlatform(nodeCapability.get(key));
              if (node == null) {
                return false;
              }
              if (!node.is(requested))
                return false;
            }
            break;
          



          case "version": 
          case "browserVersion": 
            Object nodeVersion = nodeCapability.getOrDefault("browserVersion", nodeCapability.get("version"));
            if (!value.equals(nodeVersion)) {
              return false;
            }
            
            break;
          default: 
            if (!requestedCapability.get(key).equals(nodeCapability.get(key))) {
              return false;
            }
            break;
          }
        }
      }
    }
    return true;
  }
  
  Platform extractPlatform(Object o) {
    if (o == null) {
      return null;
    }
    if ((o instanceof Platform))
      return (Platform)o;
    if ((o instanceof String)) {
      String name = o.toString();
      try {
        return Platform.valueOf(name);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        for (Platform os : Platform.values()) {
          for (String matcher : os.getPartOfOsName()) {
            if (!"".equals(matcher))
            {
              if (name.equalsIgnoreCase(matcher))
                return os;
            }
          }
        }
        return null;
      } }
    return null;
  }
}
