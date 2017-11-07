package org.openqa.selenium.net;

import com.google.common.collect.Iterables;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


















public class NetworkInterface
{
  private final String name;
  private java.net.NetworkInterface networkInterface;
  private final Iterable<InetAddress> inetAddresses;
  private Boolean isLoopback;
  
  public NetworkInterface(java.net.NetworkInterface networkInterface)
  {
    this(networkInterface.getName(), Collections.list(networkInterface.getInetAddresses()));
    this.networkInterface = networkInterface;
  }
  
  NetworkInterface(String name, Iterable<InetAddress> inetAddresses) {
    this.name = name;
    this.inetAddresses = Iterables.unmodifiableIterable(inetAddresses);
  }
  
  NetworkInterface(String name, InetAddress... inetAddresses) {
    this(name, Arrays.asList(inetAddresses));
    isLoopback = Boolean.valueOf(isLoopBackFromINetAddresses(this.inetAddresses));
  }
  
  public boolean isIp4AddressBindingOnly() {
    return getIp6Address() == null;
  }
  
  public boolean isLoopBack() {
    if (isLoopback == null) {
      if (networkInterface != null)
      {
        try
        {
          isLoopback = Boolean.valueOf(networkInterface.isLoopback());
        } catch (SocketException ex) {
          Logger.getLogger(NetworkInterface.class.getName()).log(Level.WARNING, null, ex);
        }
      }
      

      if (isLoopback == null) {
        isLoopback = Boolean.valueOf(isLoopBackFromINetAddresses(Collections.list(networkInterface.getInetAddresses())));
      }
    }
    return isLoopback.booleanValue();
  }
  
  private boolean isLoopBackFromINetAddresses(Iterable<InetAddress> inetAddresses)
  {
    Iterator<InetAddress> iterator = inetAddresses.iterator();
    return (iterator.hasNext()) && (((InetAddress)iterator.next()).isLoopbackAddress());
  }
  






  public InetAddress getIp4LoopbackOnly()
  {
    if (!isLoopBack()) {
      return null;
    }
    InetAddress lastFound = null;
    for (InetAddress inetAddress : inetAddresses) {
      if ((inetAddress.isLoopbackAddress()) && (!isIpv6(inetAddress))) {
        lastFound = inetAddress;
      }
    }
    return lastFound;
  }
  
  static boolean isIpv6(InetAddress address) {
    return address instanceof Inet6Address;
  }
  
  public InetAddress getIp4NonLoopBackOnly() {
    for (InetAddress inetAddress : inetAddresses) {
      if ((!inetAddress.isLoopbackAddress()) && (!isIpv6(inetAddress))) {
        return inetAddress;
      }
    }
    return null;
  }
  
  public InetAddress getIp6Address() {
    for (InetAddress inetAddress : inetAddresses) {
      if (isIpv6(inetAddress)) {
        return inetAddress;
      }
    }
    return null;
  }
  
  public Iterable<InetAddress> getInetAddresses() {
    return inetAddresses;
  }
  
  public String getName() {
    return name;
  }
}
