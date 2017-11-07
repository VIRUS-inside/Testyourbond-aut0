package org.openqa.selenium.remote;

import java.util.Map;
















public class RemoteStatus
{
  private Map<String, Object> status;
  private Map<String, Object> buildInfo;
  private Map<String, Object> osInfo;
  
  public RemoteStatus(Map<String, Object> status)
  {
    this.status = status;
    buildInfo = ((Map)status.get("build"));
    osInfo = ((Map)status.get("os"));
  }
  
  public String getReleaseLabel()
  {
    return (String)buildInfo.get("version");
  }
  
  public String getBuildRevision()
  {
    return (String)buildInfo.get("revision");
  }
  
  public String getBuildTime()
  {
    return (String)buildInfo.get("time");
  }
  
  public String getOsArch()
  {
    return (String)osInfo.get("arch");
  }
  
  public String getOsName()
  {
    return (String)osInfo.get("name");
  }
  
  public String getOsVersion()
  {
    return (String)osInfo.get("version");
  }
  
  public String toString() {
    return String.format("Build info: version: '%s', revision: '%s', time: '%s'\nOS info: arch: '%s', name: '%s', version: '%s'", new Object[] {
    
      getReleaseLabel(), getBuildRevision(), getBuildTime(), 
      getOsArch(), getOsName(), getOsVersion() });
  }
}
