package org.openqa.selenium.remote.server;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

public abstract interface DriverFactory
{
  public abstract void registerDriverProvider(DriverProvider paramDriverProvider);
  
  public abstract WebDriver newInstance(Capabilities paramCapabilities);
  
  @Deprecated
  public abstract boolean hasMappingFor(Capabilities paramCapabilities);
}
