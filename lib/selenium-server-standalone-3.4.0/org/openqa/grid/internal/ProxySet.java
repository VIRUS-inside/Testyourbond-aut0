package org.openqa.grid.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;
import net.jcip.annotations.ThreadSafe;
import org.openqa.grid.common.exception.CapabilityNotPresentOnTheGridException;
import org.openqa.grid.common.exception.GridException;
import org.openqa.selenium.remote.DesiredCapabilities;
























@ThreadSafe
public class ProxySet
  implements Iterable<RemoteProxy>
{
  private final Set<RemoteProxy> proxies = new CopyOnWriteArraySet();
  
  private static final Logger log = Logger.getLogger(ProxySet.class.getName());
  private volatile boolean throwOnCapabilityNotPresent = true;
  
  public ProxySet(boolean throwOnCapabilityNotPresent) {
    this.throwOnCapabilityNotPresent = throwOnCapabilityNotPresent;
  }
  


  public void teardown()
  {
    for (RemoteProxy proxy : proxies) {
      proxy.teardown();
    }
  }
  
  public boolean hasCapability(Map<String, Object> requestedCapability) {
    for (RemoteProxy proxy : proxies) {
      if (proxy.hasCapability(requestedCapability)) {
        return true;
      }
    }
    return false;
  }
  







  public RemoteProxy remove(RemoteProxy proxy)
  {
    for (RemoteProxy p : proxies) {
      if (p.equals(proxy)) {
        proxies.remove(p);
        return p;
      }
    }
    throw new IllegalStateException("Did not contain proxy" + proxy);
  }
  
  public void add(RemoteProxy proxy) {
    proxies.add(proxy);
  }
  
  public boolean contains(RemoteProxy o) {
    return proxies.contains(o);
  }
  
  public List<RemoteProxy> getBusyProxies() {
    List<RemoteProxy> res = new ArrayList();
    for (RemoteProxy proxy : proxies) {
      if (proxy.isBusy()) {
        res.add(proxy);
      }
    }
    return res;
  }
  
  public RemoteProxy getProxyById(String id) {
    if (id == null) {
      return null;
    }
    for (RemoteProxy p : proxies) {
      if (id.equals(p.getId())) {
        return p;
      }
    }
    return null;
  }
  
  public boolean isEmpty()
  {
    return proxies.isEmpty();
  }
  
  public List<RemoteProxy> getSorted() {
    List<RemoteProxy> sorted = new ArrayList(proxies);
    Collections.sort(sorted, proxyComparator);
    return sorted;
  }
  
  private Comparator<RemoteProxy> proxyComparator = new Comparator()
  {
    public int compare(RemoteProxy o1, RemoteProxy o2) {
      double p1used = o1.getResourceUsageInPercent();
      double p2used = o2.getResourceUsageInPercent();
      
      if (p1used == p2used) {
        long time1lastUsed = o1.getLastSessionStart();
        long time2lastUsed = o2.getLastSessionStart();
        if (time1lastUsed == time2lastUsed) return 0;
        return time1lastUsed < time2lastUsed ? -1 : 1;
      }
      return p1used < p2used ? -1 : 1;
    }
  };
  


  public TestSession getNewSession(Map<String, Object> desiredCapabilities)
  {
    List<RemoteProxy> sorted = getSorted();
    log.fine("Available nodes: " + sorted);
    
    for (RemoteProxy proxy : sorted) {
      TestSession session = proxy.getNewSession(desiredCapabilities);
      if (session != null) {
        return session;
      }
    }
    return null;
  }
  
  public Iterator<RemoteProxy> iterator() {
    return proxies.iterator();
  }
  
  public int size() {
    return proxies.size();
  }
  
  public void verifyAbilityToHandleDesiredCapabilities(Map<String, Object> desiredCapabilities) {
    if (proxies.isEmpty()) {
      if (throwOnCapabilityNotPresent) {
        throw new GridException("Empty pool of VM for setup " + new DesiredCapabilities(desiredCapabilities));
      }
      
      log.warning("Empty pool of nodes.");
    }
    if (!hasCapability(desiredCapabilities)) {
      if (throwOnCapabilityNotPresent) {
        throw new CapabilityNotPresentOnTheGridException(desiredCapabilities);
      }
      log.warning("grid doesn't contain " + new DesiredCapabilities(desiredCapabilities) + " at the moment.");
    }
  }
  
  public void setThrowOnCapabilityNotPresent(boolean throwOnCapabilityNotPresent)
  {
    this.throwOnCapabilityNotPresent = throwOnCapabilityNotPresent;
  }
}
