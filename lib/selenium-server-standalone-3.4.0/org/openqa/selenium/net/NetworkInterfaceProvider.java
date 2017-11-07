package org.openqa.selenium.net;

public abstract interface NetworkInterfaceProvider
{
  public abstract Iterable<NetworkInterface> getNetworkInterfaces();
  
  public abstract NetworkInterface getLoInterface();
}
