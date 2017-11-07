package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.LangUtils;



























































public final class RouteTracker
  implements RouteInfo, Cloneable
{
  private final HttpHost targetHost;
  private final InetAddress localAddress;
  private boolean connected;
  private HttpHost[] proxyChain;
  private RouteInfo.TunnelType tunnelled;
  private RouteInfo.LayerType layered;
  private boolean secure;
  
  public RouteTracker(HttpHost target, InetAddress local)
  {
    Args.notNull(target, "Target host");
    targetHost = target;
    localAddress = local;
    tunnelled = RouteInfo.TunnelType.PLAIN;
    layered = RouteInfo.LayerType.PLAIN;
  }
  


  public void reset()
  {
    connected = false;
    proxyChain = null;
    tunnelled = RouteInfo.TunnelType.PLAIN;
    layered = RouteInfo.LayerType.PLAIN;
    secure = false;
  }
  






  public RouteTracker(HttpRoute route)
  {
    this(route.getTargetHost(), route.getLocalAddress());
  }
  





  public final void connectTarget(boolean secure)
  {
    Asserts.check(!connected, "Already connected");
    connected = true;
    this.secure = secure;
  }
  






  public final void connectProxy(HttpHost proxy, boolean secure)
  {
    Args.notNull(proxy, "Proxy host");
    Asserts.check(!connected, "Already connected");
    connected = true;
    proxyChain = new HttpHost[] { proxy };
    this.secure = secure;
  }
  





  public final void tunnelTarget(boolean secure)
  {
    Asserts.check(connected, "No tunnel unless connected");
    Asserts.notNull(proxyChain, "No tunnel without proxy");
    tunnelled = RouteInfo.TunnelType.TUNNELLED;
    this.secure = secure;
  }
  








  public final void tunnelProxy(HttpHost proxy, boolean secure)
  {
    Args.notNull(proxy, "Proxy host");
    Asserts.check(connected, "No tunnel unless connected");
    Asserts.notNull(proxyChain, "No tunnel without proxy");
    
    HttpHost[] proxies = new HttpHost[proxyChain.length + 1];
    System.arraycopy(proxyChain, 0, proxies, 0, proxyChain.length);
    
    proxies[(proxies.length - 1)] = proxy;
    
    proxyChain = proxies;
    this.secure = secure;
  }
  







  public final void layerProtocol(boolean secure)
  {
    Asserts.check(connected, "No layered protocol unless connected");
    layered = RouteInfo.LayerType.LAYERED;
    this.secure = secure;
  }
  
  public final HttpHost getTargetHost()
  {
    return targetHost;
  }
  
  public final InetAddress getLocalAddress()
  {
    return localAddress;
  }
  
  public final int getHopCount()
  {
    int hops = 0;
    if (connected) {
      if (proxyChain == null) {
        hops = 1;
      } else {
        hops = proxyChain.length + 1;
      }
    }
    return hops;
  }
  
  public final HttpHost getHopTarget(int hop)
  {
    Args.notNegative(hop, "Hop index");
    int hopcount = getHopCount();
    Args.check(hop < hopcount, "Hop index exceeds tracked route length");
    HttpHost result = null;
    if (hop < hopcount - 1) {
      result = proxyChain[hop];
    } else {
      result = targetHost;
    }
    
    return result;
  }
  
  public final HttpHost getProxyHost()
  {
    return proxyChain == null ? null : proxyChain[0];
  }
  
  public final boolean isConnected() {
    return connected;
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
  







  public final HttpRoute toRoute()
  {
    return !connected ? null : new HttpRoute(targetHost, localAddress, proxyChain, secure, tunnelled, layered);
  }
  











  public final boolean equals(Object o)
  {
    if (o == this) {
      return true;
    }
    if (!(o instanceof RouteTracker)) {
      return false;
    }
    
    RouteTracker that = (RouteTracker)o;
    return (connected == connected) && (secure == secure) && (tunnelled == tunnelled) && (layered == layered) && (LangUtils.equals(targetHost, targetHost)) && (LangUtils.equals(localAddress, localAddress)) && (LangUtils.equals(proxyChain, proxyChain));
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
    hash = LangUtils.hashCode(hash, connected);
    hash = LangUtils.hashCode(hash, secure);
    hash = LangUtils.hashCode(hash, tunnelled);
    hash = LangUtils.hashCode(hash, layered);
    return hash;
  }
  





  public final String toString()
  {
    StringBuilder cab = new StringBuilder(50 + getHopCount() * 30);
    
    cab.append("RouteTracker[");
    if (localAddress != null) {
      cab.append(localAddress);
      cab.append("->");
    }
    cab.append('{');
    if (connected) {
      cab.append('c');
    }
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
      for (HttpHost element : proxyChain) {
        cab.append(element);
        cab.append("->");
      }
    }
    cab.append(targetHost);
    cab.append(']');
    
    return cab.toString();
  }
  

  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }
}
