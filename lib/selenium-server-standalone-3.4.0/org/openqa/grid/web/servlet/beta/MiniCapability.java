package org.openqa.grid.web.servlet.beta;

import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.TestSlot;
import org.openqa.grid.web.utils.BrowserNameUtils;
import org.openqa.selenium.remote.DesiredCapabilities;





















public class MiniCapability
{
  private String browser;
  private String version;
  private DesiredCapabilities capabilities;
  private RemoteProxy proxy;
  
  public MiniCapability(TestSlot slot)
  {
    DesiredCapabilities cap = new DesiredCapabilities(slot.getCapabilities());
    browser = cap.getBrowserName();
    version = cap.getVersion();
    capabilities = cap;
    proxy = slot.getProxy();
  }
  
  public String getVersion()
  {
    return version;
  }
  
  public String getIcon() {
    return BrowserNameUtils.getConsoleIconPath(new DesiredCapabilities(capabilities), proxy
      .getRegistry());
  }
  
  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (browser == null ? 0 : browser.hashCode());
    result = 31 * result + (version == null ? 0 : version.hashCode());
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    MiniCapability other = (MiniCapability)obj;
    if (browser == null) {
      if (browser != null) return false;
    } else if (!browser.equals(browser)) return false;
    if (version == null) {
      if (version != null) return false;
    } else if (!version.equals(version)) return false;
    return true;
  }
}
