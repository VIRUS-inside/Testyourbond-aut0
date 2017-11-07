package org.openqa.selenium.net;

import java.io.PrintStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;

















public class NetworkUtils
{
  private final NetworkInterfaceProvider networkInterfaceProvider;
  
  NetworkUtils(NetworkInterfaceProvider networkInterfaceProvider)
  {
    this.networkInterfaceProvider = networkInterfaceProvider;
  }
  
  public NetworkUtils() {
    this(new DefaultNetworkInterfaceProvider());
  }
  
  public String getPrivateLocalAddress() {
    List<InetAddress> addresses = getLocalInterfaceAddress();
    if (addresses.isEmpty()) {
      return "127.0.0.1";
    }
    
    return ((InetAddress)addresses.get(0)).getHostAddress();
  }
  






  public String getNonLoopbackAddressOfThisMachine()
  {
    return getIp4NonLoopbackAddressOfThisMachine().getHostName();
  }
  




  public InetAddress getIp4NonLoopbackAddressOfThisMachine()
  {
    for (NetworkInterface iface : networkInterfaceProvider.getNetworkInterfaces()) {
      InetAddress ip4NonLoopback = iface.getIp4NonLoopBackOnly();
      if (ip4NonLoopback != null) {
        return ip4NonLoopback;
      }
    }
    throw new WebDriverException("Could not find a non-loopback ip4 address for this machine");
  }
  






  public String obtainLoopbackIp4Address()
  {
    NetworkInterface networkInterface = getLoopBackAndIp4Only();
    if (networkInterface != null) {
      return networkInterface.getIp4LoopbackOnly().getHostName();
    }
    
    String ipOfIp4LoopBack = getIpOfLoopBackIp4();
    if (ipOfIp4LoopBack != null) {
      return ipOfIp4LoopBack;
    }
    
    if (Platform.getCurrent().is(Platform.UNIX)) {
      NetworkInterface linuxLoopback = networkInterfaceProvider.getLoInterface();
      if (linuxLoopback != null) {
        InetAddress netAddress = linuxLoopback.getIp4LoopbackOnly();
        if (netAddress != null) {
          return netAddress.getHostAddress();
        }
      }
    }
    



    throw new WebDriverException("Unable to resolve local loopback address, please file an issue with the full message of this error:\n" + getNetWorkDiags() + "\n==== End of error message");
  }
  

  private InetAddress grabFirstNetworkAddress()
  {
    NetworkInterface firstInterface = (NetworkInterface)networkInterfaceProvider.getNetworkInterfaces().iterator().next();
    InetAddress firstAddress = null;
    if (firstInterface != null) {
      firstAddress = (InetAddress)firstInterface.getInetAddresses().iterator().next();
    }
    
    if (firstAddress == null) {
      throw new WebDriverException("Unable to find any network address for localhost");
    }
    
    return firstAddress;
  }
  
  public String getIpOfLoopBackIp4() {
    for (NetworkInterface iface : networkInterfaceProvider.getNetworkInterfaces()) {
      InetAddress netAddress = iface.getIp4LoopbackOnly();
      if (netAddress != null) {
        return netAddress.getHostAddress();
      }
    }
    return null;
  }
  
  private NetworkInterface getLoopBackAndIp4Only() {
    for (NetworkInterface iface : networkInterfaceProvider.getNetworkInterfaces()) {
      if ((iface.isIp4AddressBindingOnly()) && (iface.isLoopBack())) {
        return iface;
      }
    }
    return null;
  }
  
  private List<InetAddress> getLocalInterfaceAddress() {
    List<InetAddress> localAddresses = new ArrayList();
    
    for (Iterator localIterator1 = networkInterfaceProvider.getNetworkInterfaces().iterator(); localIterator1.hasNext();) { iface = (NetworkInterface)localIterator1.next();
      for (InetAddress addr : iface.getInetAddresses())
      {
        if ((addr.isLoopbackAddress()) && (!NetworkInterface.isIpv6(addr))) {
          localAddresses.add(addr);
        }
      }
    }
    

    NetworkInterface iface;
    
    if (Platform.getCurrent().is(Platform.UNIX)) {
      NetworkInterface linuxLoopback = networkInterfaceProvider.getLoInterface();
      if (linuxLoopback != null) {
        for (InetAddress inetAddress : linuxLoopback.getInetAddresses()) {
          if (!NetworkInterface.isIpv6(inetAddress)) {
            localAddresses.add(inetAddress);
          }
        }
      }
    }
    
    if (localAddresses.isEmpty()) {
      return Collections.singletonList(grabFirstNetworkAddress());
    }
    
    return localAddresses;
  }
  
  public static String getNetWorkDiags() {
    StringBuilder result = new StringBuilder();
    DefaultNetworkInterfaceProvider defaultNetworkInterfaceProvider = new DefaultNetworkInterfaceProvider();
    
    for (NetworkInterface networkInterface : defaultNetworkInterfaceProvider
      .getNetworkInterfaces()) {
      dumpToConsole(result, networkInterface);
    }
    
    NetworkInterface byName = defaultNetworkInterfaceProvider.getLoInterface();
    if (byName != null) {
      result.append("Loopback interface LO:\n");
      dumpToConsole(result, byName);
    }
    return result.toString();
  }
  
  private static void dumpToConsole(StringBuilder result, NetworkInterface inNetworkInterface) {
    if (inNetworkInterface == null) {
      return;
    }
    result.append(inNetworkInterface.getName());
    result.append("\n");
    dumpAddresses(result, inNetworkInterface.getInetAddresses());
  }
  
  private static void dumpAddresses(StringBuilder result, Iterable<InetAddress> inetAddresses) {
    for (InetAddress address : inetAddresses) {
      result.append("   address.getHostName() = ");
      result.append(address.getHostName());
      result.append("\n");
      result.append("   address.getHostAddress() = ");
      result.append(address.getHostAddress());
      result.append("\n");
      result.append("   address.isLoopbackAddress() = ");
      result.append(address.isLoopbackAddress());
      result.append("\n");
    }
  }
  
  public static void main(String[] args)
  {
    System.out.println(getNetWorkDiags());
  }
}
