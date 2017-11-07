package org.openqa.grid.common;

import java.util.Arrays;
import java.util.Map;
import org.openqa.grid.common.exception.GridException;





















public enum SeleniumProtocol
{
  Selenium("/selenium-server/driver"), 
  WebDriver("/wd/hub");
  
  private String path;
  
  private SeleniumProtocol(String path) { this.path = path; }
  








  public static SeleniumProtocol fromCapabilitiesMap(Map<String, ?> capabilities)
  {
    String type = (String)capabilities.get("seleniumProtocol");
    if ((type == null) || (type.trim().isEmpty())) {
      return WebDriver;
    }
    try {
      return valueOf(type);
    }
    catch (IllegalArgumentException e) {
      throw new GridException(type + " isn't a valid protocol type for grid. Valid values :[" + Arrays.toString(values()) + "]", e);
    }
  }
  






  public String getPathConsideringCapabilitiesMap(Map<String, ?> capabilities)
  {
    String localPath = (String)capabilities.get("path");
    if (localPath != null) {
      return localPath;
    }
    return path;
  }
  




  public String getPath()
  {
    return path;
  }
  


  @Deprecated
  public boolean isSelenium()
  {
    return Selenium.equals(this);
  }
}
