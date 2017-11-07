package org.openqa.selenium.remote.server;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

public abstract interface DriverProvider
{
  public abstract Capabilities getProvidedCapabilities();
  
  public abstract boolean canCreateDriverInstances();
  
  public abstract boolean canCreateDriverInstanceFor(Capabilities paramCapabilities);
  
  public abstract WebDriver newInstance(Capabilities paramCapabilities);
}
