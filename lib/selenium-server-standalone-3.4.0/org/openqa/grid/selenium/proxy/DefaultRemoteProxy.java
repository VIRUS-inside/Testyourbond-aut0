package org.openqa.grid.selenium.proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.common.SeleniumProtocol;
import org.openqa.grid.common.exception.RemoteException;
import org.openqa.grid.common.exception.RemoteNotReachableException;
import org.openqa.grid.common.exception.RemoteUnregisterException;
import org.openqa.grid.internal.BaseRemoteProxy;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.internal.TestSlot;
import org.openqa.grid.internal.listeners.CommandListener;
import org.openqa.grid.internal.listeners.SelfHealingProxy;
import org.openqa.grid.internal.listeners.TestSessionListener;
import org.openqa.grid.internal.listeners.TimeoutListener;
import org.openqa.grid.internal.utils.HtmlRenderer;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;




























public class DefaultRemoteProxy
  extends BaseRemoteProxy
  implements TimeoutListener, SelfHealingProxy, CommandListener, TestSessionListener
{
  private static final Logger LOG = Logger.getLogger(DefaultRemoteProxy.class.getName());
  
  public static final int DEFAULT_POLLING_INTERVAL = 10000;
  
  public static final int DEFAULT_UNREGISTER_DELAY = 60000;
  public static final int DEFAULT_DOWN_POLLING_LIMIT = 2;
  private volatile int pollingInterval = 10000;
  private volatile int unregisterDelay = 60000;
  private volatile int downPollingLimit = 2;
  
  public DefaultRemoteProxy(RegistrationRequest request, Registry registry) {
    super(request, registry);
    
    pollingInterval = (config.nodePolling != null ? config.nodePolling.intValue() : 10000);
    unregisterDelay = (config.unregisterIfStillDownAfter != null ? config.unregisterIfStillDownAfter.intValue() : 60000);
    downPollingLimit = (config.downPollingLimit != null ? config.downPollingLimit.intValue() : 2);
  }
  
  public void beforeRelease(TestSession session)
  {
    if (session.getExternalKey() == null) {
      return;
    }
    boolean ok = session.sendDeleteSessionRequest();
    if (!ok) {
      LOG.warning("Error releasing the resources on timeout for session " + session);
    }
  }
  
  public void afterCommand(TestSession session, HttpServletRequest request, HttpServletResponse response)
  {
    session.put("lastCommand", request.getMethod() + " - " + request.getPathInfo() + " executed.");
  }
  
  public void beforeCommand(TestSession session, HttpServletRequest request, HttpServletResponse response)
  {
    session.put("lastCommand", request.getMethod() + " - " + request.getPathInfo() + " executing ...");
  }
  
  private final HtmlRenderer renderer = new WebProxyHtmlRenderer(this);
  
  public HtmlRenderer getHtmlRender()
  {
    return renderer;
  }
  



  private volatile boolean down = false;
  private volatile boolean poll = true;
  

  private List<RemoteException> errors = new CopyOnWriteArrayList();
  private Thread pollingThread = null;
  
  public boolean isAlive() {
    try {
      getStatus();
      return true;
    } catch (Exception e) {
      LOG.fine("Failed to check status of node: " + e.getMessage()); }
    return false;
  }
  



































  public void startPolling()
  {
    pollingThread = new Thread(new Runnable()
    {
      int failedPollingTries = 0;
      long downSince = 0L;
      
      public void run() {
        while (poll) {
          try {
            Thread.sleep(pollingInterval);
            if (!isAlive()) {
              if (!down) {
                failedPollingTries += 1;
                if (failedPollingTries >= downPollingLimit) {
                  downSince = System.currentTimeMillis();
                  addNewEvent(new RemoteNotReachableException(String.format("Marking the node %s as down: cannot reach the node for %s tries", new Object[] { DefaultRemoteProxy.this, 
                  
                    Integer.valueOf(failedPollingTries) })));
                }
              } else {
                long downFor = System.currentTimeMillis() - downSince;
                if (downFor > unregisterDelay) {
                  addNewEvent(new RemoteUnregisterException(String.format("Unregistering the node %s because it's been down for %s milliseconds", new Object[] { DefaultRemoteProxy.this, 
                  
                    Long.valueOf(downFor) })));
                }
              }
            } else {
              down = false;
              failedPollingTries = 0;
              downSince = 0L;
            }
            
          }
          catch (InterruptedException e) {}
        }
      }
    }, "RemoteProxy failure poller thread for " + getId());
    pollingThread.start();
  }
  
  public void stopPolling() {
    poll = false;
    pollingThread.interrupt();
  }
  
  public void addNewEvent(RemoteException event) {
    errors.add(event);
    onEvent(errors, event);
  }
  
  public void onEvent(List<RemoteException> events, RemoteException lastInserted)
  {
    for (RemoteException e : events) {
      if ((e instanceof RemoteNotReachableException)) {
        LOG.info(e.getMessage());
        down = true;
        errors.clear();
      }
      if ((e instanceof RemoteUnregisterException)) {
        LOG.info(e.getMessage());
        Registry registry = getRegistry();
        registry.removeIfPresent(this);
      }
    }
  }
  



  public TestSession getNewSession(Map<String, Object> requestedCapability)
  {
    if (down) {
      return null;
    }
    return super.getNewSession(requestedCapability);
  }
  
  public boolean isDown() {
    return down;
  }
  
















  public void beforeSession(TestSession session)
  {
    if (session.getSlot().getProtocol() == SeleniumProtocol.WebDriver) {
      Map<String, Object> cap = session.getRequestedCapabilities();
      
      if (("firefox".equals(cap.get("browserName"))) && 
        (session.getSlot().getCapabilities().get("firefox_binary") != null) && 
        (cap.get("firefox_binary") == null)) {
        session.getRequestedCapabilities().put("firefox_binary", session
          .getSlot().getCapabilities().get("firefox_binary"));
      }
      

      if (("chrome".equals(cap.get("browserName"))) && 
        (session.getSlot().getCapabilities().get("chrome_binary") != null)) {
        Map<String, Object> options = (Map)cap.get("chromeOptions");
        if (options == null) {
          options = new HashMap();
        }
        if (!options.containsKey("binary")) {
          options.put("binary", session.getSlot().getCapabilities().get("chrome_binary"));
        }
        cap.put("chromeOptions", options);
      }
    }
  }
  


  public void afterSession(TestSession session) {}
  

  public void teardown()
  {
    super.teardown();
    stopPolling();
  }
}
