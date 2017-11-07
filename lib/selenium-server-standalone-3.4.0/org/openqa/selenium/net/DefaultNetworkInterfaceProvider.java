package org.openqa.selenium.net;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;






















public class DefaultNetworkInterfaceProvider
  implements NetworkInterfaceProvider
{
  private final List<NetworkInterface> cachedInterfaces;
  
  public Iterable<NetworkInterface> getNetworkInterfaces()
  {
    return cachedInterfaces;
  }
  
  public DefaultNetworkInterfaceProvider() {
    Enumeration<java.net.NetworkInterface> interfaces = null;
    try {
      interfaces = java.net.NetworkInterface.getNetworkInterfaces();
    } catch (SocketException e) {
      throw new WebDriverException(e);
    }
    
    List<NetworkInterface> result = new ArrayList();
    while (interfaces.hasMoreElements()) {
      result.add(new NetworkInterface((java.net.NetworkInterface)interfaces.nextElement()));
    }
    cachedInterfaces = Collections.unmodifiableList(result);
  }
  
  private String getLocalInterfaceName() {
    if (Platform.getCurrent().is(Platform.MAC)) {
      return "lo0";
    }
    
    return "lo";
  }
  
  public NetworkInterface getLoInterface() {
    String localIF = getLocalInterfaceName();
    try {
      java.net.NetworkInterface byName = java.net.NetworkInterface.getByName(localIF);
      return byName != null ? new NetworkInterface(byName) : null;
    } catch (SocketException e) {
      throw new WebDriverException(e);
    }
  }
}
