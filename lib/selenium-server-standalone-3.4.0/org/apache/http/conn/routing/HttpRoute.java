package org.apache.http.conn.routing;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;















































@Contract(threading=ThreadingBehavior.IMMUTABLE)
public final class HttpRoute
  implements RouteInfo, Cloneable
{
  private final HttpHost targetHost;
  private final InetAddress localAddress;
  private final List<HttpHost> proxyChain;
  private final RouteInfo.TunnelType tunnelled;
  private final RouteInfo.LayerType layered;
  private final boolean secure;
  
  private HttpRoute(HttpHost target, InetAddress local, List<HttpHost> proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered)
  {
    Args.notNull(target, "Target host");
    targetHost = normalize(target);
    localAddress = local;
    if ((proxies != null) && (!proxies.isEmpty())) {
      proxyChain = new ArrayList(proxies);
    } else {
      proxyChain = null;
    }
    if (tunnelled == RouteInfo.TunnelType.TUNNELLED) {
      Args.check(proxyChain != null, "Proxy required if tunnelled");
    }
    this.secure = secure;
    this.tunnelled = (tunnelled != null ? tunnelled : RouteInfo.TunnelType.PLAIN);
    this.layered = (layered != null ? layered : RouteInfo.LayerType.PLAIN);
  }
  
  private static int getDefaultPort(String schemeName)
  {
    if ("http".equalsIgnoreCase(schemeName))
      return 80;
    if ("https".equalsIgnoreCase(schemeName)) {
      return 443;
    }
    return -1;
  }
  


  private static HttpHost normalize(HttpHost target)
  {
    if (target.getPort() >= 0) {
      return target;
    }
    InetAddress address = target.getAddress();
    String schemeName = target.getSchemeName();
    if (address != null) {
      return new HttpHost(address, getDefaultPort(schemeName), schemeName);
    }
    String hostName = target.getHostName();
    return new HttpHost(hostName, getDefaultPort(schemeName), schemeName);
  }
  















  public HttpRoute(HttpHost target, InetAddress local, HttpHost[] proxies, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered)
  {
    this(target, local, proxies != null ? Arrays.asList(proxies) : null, secure, tunnelled, layered);
  }
  


















  public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure, RouteInfo.TunnelType tunnelled, RouteInfo.LayerType layered)
  {
    this(target, local, proxy != null ? Collections.singletonList(proxy) : null, secure, tunnelled, layered);
  }
  










  public HttpRoute(HttpHost target, InetAddress local, boolean secure)
  {
    this(target, local, Collections.emptyList(), secure, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
  }
  





  public HttpRoute(HttpHost target)
  {
    this(target, null, Collections.emptyList(), false, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
  }
  














  public HttpRoute(HttpHost target, InetAddress local, HttpHost proxy, boolean secure)
  {
    this(target, local, Collections.singletonList(Args.notNull(proxy, "Proxy host")), secure, secure ? RouteInfo.TunnelType.TUNNELLED : RouteInfo.TunnelType.PLAIN, secure ? RouteInfo.LayerType.LAYERED : RouteInfo.LayerType.PLAIN);
  }
  









  public HttpRoute(HttpHost target, HttpHost proxy)
  {
    this(target, null, proxy, false);
  }
  
  public final HttpHost getTargetHost()
  {
    return targetHost;
  }
  
  public final InetAddress getLocalAddress()
  {
    return localAddress;
  }
  
  public final InetSocketAddress getLocalSocketAddress() {
    return localAddress != null ? new InetSocketAddress(localAddress, 0) : null;
  }
  
  public final int getHopCount()
  {
    return proxyChain != null ? proxyChain.size() + 1 : 1;
  }
  
  public final HttpHost getHopTarget(int hop)
  {
    Args.notNegative(hop, "Hop index");
    int hopcount = getHopCount();
    Args.check(hop < hopcount, "Hop index exceeds tracked route length");
    if (hop < hopcount - 1) {
      return (HttpHost)proxyChain.get(hop);
    }
    return targetHost;
  }
  

  public final HttpHost getProxyHost()
  {
    return (proxyChain != null) && (!proxyChain.isEmpty()) ? (HttpHost)proxyChain.get(0) : null;
  }
  
  public final RouteInfo.TunnelType getTunnelType()
  {
    return tunnelled;
  }
  
  public final boolean isTunnelled()
  {
    return tunnelled == RouteInfo.TunnelType.TUNNELLED;
  }
  
  public final RouteInfo.LayerType getLayerType()
  {
    return layered;
  }
  
  public final boolean isLayered()
  {
    return layered == RouteInfo.LayerType.LAYERED;
  }
  
  public final boolean isSecure()
  {
    return secure;
  }
  








  public final boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if ((obj instanceof HttpRoute)) {
      HttpRoute that = (HttpRoute)obj;
      return (secure == secure) && (tunnelled == tunnelled) && (layered == layered) && (LangUtils.equals(targetHost, targetHost)) && (LangUtils.equals(localAddress, localAddress)) && (LangUtils.equals(proxyChain, proxyChain));
    }
    






    return false;
  }
  







  public final int hashCode()
  {
    int hash = 17;
    hash = LangUtils.hashCode(hash, targetHost);
    hash = LangUtils.hashCode(hash, localAddress);
    if (proxyChain != null) {
      for (HttpHost element : proxyChain) {
        hash = LangUtils.hashCode(hash, element);
      }
    }
    hash = LangUtils.hashCode(hash, secure);
    hash = LangUtils.hashCode(hash, tunnelled);
    hash = LangUtils.hashCode(hash, layered);
    return hash;
  }
  





  public final String toString()
  {
    StringBuilder cab = new StringBuilder(50 + getHopCount() * 30);
    if (localAddress != null) {
      cab.append(localAddress);
      cab.append("->");
    }
    cab.append('{');
    if (tunnelled == RouteInfo.TunnelType.TUNNELLED) {
      cab.append('t');
    }
    if (layered == RouteInfo.LayerType.LAYERED) {
      cab.append('l');
    }
    if (secure) {
      cab.append('s');
    }
    cab.append("}->");
    if (proxyChain != null) {
      for (HttpHost aProxyChain : proxyChain) {
        cab.append(aProxyChain);
        cab.append("->");
      }
    }
    cab.append(targetHost);
    return cab.toString();
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
}
