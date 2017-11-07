package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;





















































@Beta
@Immutable
@GwtCompatible
public final class HostAndPort
  implements Serializable
{
  private static final int NO_PORT = -1;
  private final String host;
  private final int port;
  private final boolean hasBracketlessColons;
  private static final long serialVersionUID = 0L;
  
  private HostAndPort(String host, int port, boolean hasBracketlessColons)
  {
    this.host = host;
    this.port = port;
    this.hasBracketlessColons = hasBracketlessColons;
  }
  








  public String getHost()
  {
    return host;
  }
  




  @Deprecated
  public String getHostText()
  {
    return host;
  }
  
  public boolean hasPort()
  {
    return port >= 0;
  }
  






  public int getPort()
  {
    Preconditions.checkState(hasPort());
    return port;
  }
  


  public int getPortOrDefault(int defaultPort)
  {
    return hasPort() ? port : defaultPort;
  }
  











  public static HostAndPort fromParts(String host, int port)
  {
    Preconditions.checkArgument(isValidPort(port), "Port out of range: %s", port);
    HostAndPort parsedHost = fromString(host);
    Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
    return new HostAndPort(host, port, hasBracketlessColons);
  }
  










  public static HostAndPort fromHost(String host)
  {
    HostAndPort parsedHost = fromString(host);
    Preconditions.checkArgument(!parsedHost.hasPort(), "Host has a port: %s", host);
    return parsedHost;
  }
  









  public static HostAndPort fromString(String hostPortString)
  {
    Preconditions.checkNotNull(hostPortString);
    
    String portString = null;
    boolean hasBracketlessColons = false;
    String host;
    if (hostPortString.startsWith("[")) {
      String[] hostAndPort = getHostAndPortFromBracketedHost(hostPortString);
      String host = hostAndPort[0];
      portString = hostAndPort[1];
    } else {
      int colonPos = hostPortString.indexOf(':');
      if ((colonPos >= 0) && (hostPortString.indexOf(':', colonPos + 1) == -1))
      {
        String host = hostPortString.substring(0, colonPos);
        portString = hostPortString.substring(colonPos + 1);
      }
      else {
        host = hostPortString;
        hasBracketlessColons = colonPos >= 0;
      }
    }
    
    int port = -1;
    if (!Strings.isNullOrEmpty(portString))
    {

      Preconditions.checkArgument(!portString.startsWith("+"), "Unparseable port number: %s", hostPortString);
      try {
        port = Integer.parseInt(portString);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Unparseable port number: " + hostPortString);
      }
      Preconditions.checkArgument(isValidPort(port), "Port number out of range: %s", hostPortString);
    }
    
    return new HostAndPort(host, port, hasBracketlessColons);
  }
  






  private static String[] getHostAndPortFromBracketedHost(String hostPortString)
  {
    int colonIndex = 0;
    int closeBracketIndex = 0;
    Preconditions.checkArgument(hostPortString
      .charAt(0) == '[', "Bracketed host-port string must start with a bracket: %s", hostPortString);
    

    colonIndex = hostPortString.indexOf(':');
    closeBracketIndex = hostPortString.lastIndexOf(']');
    Preconditions.checkArgument((colonIndex > -1) && (closeBracketIndex > colonIndex), "Invalid bracketed host/port: %s", hostPortString);
    



    String host = hostPortString.substring(1, closeBracketIndex);
    if (closeBracketIndex + 1 == hostPortString.length()) {
      return new String[] { host, "" };
    }
    Preconditions.checkArgument(
      hostPortString.charAt(closeBracketIndex + 1) == ':', "Only a colon may follow a close bracket: %s", hostPortString);
    

    for (int i = closeBracketIndex + 2; i < hostPortString.length(); i++) {
      Preconditions.checkArgument(
        Character.isDigit(hostPortString.charAt(i)), "Port must be numeric: %s", hostPortString);
    }
    

    return new String[] { host, hostPortString.substring(closeBracketIndex + 2) };
  }
  









  public HostAndPort withDefaultPort(int defaultPort)
  {
    Preconditions.checkArgument(isValidPort(defaultPort));
    if ((hasPort()) || (port == defaultPort)) {
      return this;
    }
    return new HostAndPort(host, defaultPort, hasBracketlessColons);
  }
  













  public HostAndPort requireBracketsForIPv6()
  {
    Preconditions.checkArgument(!hasBracketlessColons, "Possible bracketless IPv6 literal: %s", host);
    return this;
  }
  
  public boolean equals(@Nullable Object other)
  {
    if (this == other) {
      return true;
    }
    if ((other instanceof HostAndPort)) {
      HostAndPort that = (HostAndPort)other;
      return (Objects.equal(host, host)) && (port == port) && (hasBracketlessColons == hasBracketlessColons);
    }
    

    return false;
  }
  
  public int hashCode()
  {
    return Objects.hashCode(new Object[] { host, Integer.valueOf(port), Boolean.valueOf(hasBracketlessColons) });
  }
  


  public String toString()
  {
    StringBuilder builder = new StringBuilder(host.length() + 8);
    if (host.indexOf(':') >= 0) {
      builder.append('[').append(host).append(']');
    } else {
      builder.append(host);
    }
    if (hasPort()) {
      builder.append(':').append(port);
    }
    return builder.toString();
  }
  
  private static boolean isValidPort(int port)
  {
    return (port >= 0) && (port <= 65535);
  }
}
