package org.openqa.grid.internal;

import com.google.common.base.Throwables;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import org.openqa.grid.common.SeleniumProtocol;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.listeners.TestSessionListener;
import org.openqa.grid.internal.utils.CapabilityMatcher;


































public class TestSlot
{
  private static final Logger log = Logger.getLogger(TestSlot.class.getName());
  
  private final Map<String, Object> capabilities;
  private final RemoteProxy proxy;
  private final SeleniumProtocol protocol;
  private final String path;
  private final CapabilityMatcher matcher;
  private final Lock lock = new ReentrantLock();
  
  private volatile TestSession currentSession;
  volatile boolean beingReleased = false;
  private boolean showWarning = false;
  private long lastSessionStart = -1L;
  







  public TestSlot(RemoteProxy proxy, SeleniumProtocol protocol, String path, Map<String, Object> capabilities)
  {
    this.proxy = proxy;
    this.protocol = protocol;
    this.path = path;
    
    CapabilityMatcher c = proxy.getCapabilityHelper();
    if (c == null) {
      throw new InvalidParameterException("the proxy needs to have a valid capabilityMatcher to support have some test slots attached to it");
    }
    
    matcher = proxy.getCapabilityHelper();
    this.capabilities = capabilities;
  }
  





  public TestSlot(RemoteProxy proxy, SeleniumProtocol protocol, Map<String, Object> capabilities)
  {
    this(proxy, protocol, protocol.getPathConsideringCapabilitiesMap(capabilities), capabilities);
  }
  


  public Map<String, Object> getCapabilities()
  {
    return Collections.unmodifiableMap(capabilities);
  }
  


  public RemoteProxy getProxy()
  {
    return proxy;
  }
  









  public TestSession getNewSession(Map<String, Object> desiredCapabilities)
  {
    try
    {
      lock.lock();
      if (currentSession != null)
        return null;
      TestSession session;
      if (matches(desiredCapabilities)) {
        log.info("Trying to create a new session on test slot " + capabilities);
        session = new TestSession(this, desiredCapabilities, new DefaultTimeSource());
        currentSession = session;
        lastSessionStart = System.currentTimeMillis();
        return session;
      }
      return null;
    } finally {
      lock.unlock();
    }
  }
  





  public SeleniumProtocol getProtocol()
  {
    return protocol;
  }
  





  public String getPath()
  {
    return path;
  }
  




  public boolean matches(Map<String, Object> desiredCapabilities)
  {
    return matcher.matches(capabilities, desiredCapabilities);
  }
  




  public TestSession getSession()
  {
    return currentSession;
  }
  










  boolean startReleaseProcess()
  {
    if (currentSession == null) {
      return false;
    }
    try
    {
      lock.lock();
      boolean bool; if (beingReleased) {
        return false;
      }
      beingReleased = true;
      return true;
    } finally {
      lock.unlock();
    }
  }
  

  void finishReleaseProcess()
  {
    try
    {
      lock.lock();
      doFinishRelease();
      
      lock.unlock(); } finally { lock.unlock();
    }
  }
  


  public void doFinishRelease()
  {
    currentSession = null;
    beingReleased = false;
  }
  


  String getInternalKey()
  {
    return currentSession == null ? null : currentSession.getInternalKey();
  }
  


  boolean performAfterSessionEvent()
  {
    try
    {
      if ((proxy instanceof TestSessionListener)) {
        if ((showWarning) && (proxy.getMaxNumberOfConcurrentTestSessions() != 1)) {
          log.warning("WARNING : using a afterSession on a proxy that can support multiple tests is risky.");
          showWarning = false;
        }
        ((TestSessionListener)proxy).afterSession(currentSession);
      }
    } catch (Throwable t) {
      log.severe(String.format("Error running afterSession for %s, the test slot is now dead: %s\n%s", new Object[] { currentSession, t
      
        .getMessage(), Throwables.getStackTraceAsString(t) }));
      return false;
    }
    return true;
  }
  
  public String toString()
  {
    return currentSession == null ? "no session" : currentSession.toString();
  }
  




  public URL getRemoteURL()
  {
    String u = getProxy().getRemoteHost() + getPath();
    try {
      return new URL(u);
    } catch (MalformedURLException e) {
      throw new GridException("Configuration error for the node." + u + " isn't a valid URL");
    }
  }
  


  public long getLastSessionStart()
  {
    return lastSessionStart;
  }
}
