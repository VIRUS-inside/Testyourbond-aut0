package org.openqa.selenium;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;





















public abstract interface Capabilities
{
  public String getBrowserName()
  {
    return String.valueOf(Optional.ofNullable(getCapability("browserName")).orElse(""));
  }
  
  public Platform getPlatform() {
    Object rawPlatform = getCapability("platform");
    
    if (rawPlatform == null) {
      return null;
    }
    
    if ((rawPlatform instanceof String))
      return Platform.valueOf((String)rawPlatform);
    if ((rawPlatform instanceof Platform)) {
      return (Platform)rawPlatform;
    }
    
    throw new IllegalStateException("Platform was neither a string or a Platform: " + rawPlatform);
  }
  
  public String getVersion() {
    return String.valueOf(Optional.ofNullable(getCapability("version")).orElse(""));
  }
  




  @Deprecated
  public boolean isJavascriptEnabled()
  {
    return is("javascriptEnabled");
  }
  




  public abstract Map<String, ?> asMap();
  




  public abstract Object getCapability(String paramString);
  




  public boolean is(String capabilityName)
  {
    Object cap = getCapability(capabilityName);
    if (cap == null)
    {
      return "javascriptEnabled".equals(capabilityName);
    }
    return (cap instanceof Boolean) ? ((Boolean)cap).booleanValue() : Boolean.parseBoolean(String.valueOf(cap));
  }
  




  public Capabilities merge(Capabilities other)
  {
    HashMap<String, Object> map = new HashMap();
    map.putAll(asMap());
    if (other != null) {
      map.putAll(other.asMap());
    }
    return new ImmutableCapabilities(map);
  }
}
