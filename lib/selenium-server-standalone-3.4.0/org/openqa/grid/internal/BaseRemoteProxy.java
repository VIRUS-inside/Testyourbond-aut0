package org.openqa.grid.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.util.EntityUtils;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.common.SeleniumProtocol;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.listeners.TimeoutListener;
import org.openqa.grid.internal.utils.CapabilityMatcher;
import org.openqa.grid.internal.utils.DefaultHtmlRenderer;
import org.openqa.grid.internal.utils.HtmlRenderer;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.internal.HttpClientFactory;


















public class BaseRemoteProxy
  implements RemoteProxy
{
  private final RegistrationRequest request;
  private static final Logger log = Logger.getLogger(BaseRemoteProxy.class.getName());
  

  protected volatile URL remoteHost;
  

  protected final GridNodeConfiguration config;
  

  private final List<TestSlot> testSlots;
  
  private final Registry registry;
  
  private final String id;
  
  private volatile boolean stop = false;
  private CleanUpThread cleanUpThread;
  
  public List<TestSlot> getTestSlots() {
    return testSlots;
  }
  
  public Registry getRegistry() {
    return registry;
  }
  
  public CapabilityMatcher getCapabilityHelper() {
    return registry.getConfiguration().capabilityMatcher;
  }
  









  public BaseRemoteProxy(RegistrationRequest request, Registry registry)
  {
    this.request = request;
    this.registry = registry;
    config = new GridNodeConfiguration();
    
    config.merge(registry.getConfiguration());
    

    config.merge(request.getConfiguration());
    
    config.host = getConfigurationhost;
    config.port = getConfigurationport;
    
    String url = config.getRemoteHost();
    String id = config.id;
    
    if ((url == null) && (id == null)) {
      throw new GridException("The registration request needs to specify either the remote host, or a valid id.");
    }
    

    if (url != null) {
      try {
        remoteHost = new URL(url);
      }
      catch (MalformedURLException e) {
        throw new GridException("Not a correct url to register a remote : " + url);
      }
    }
    

    if (id != null) {
      this.id = id;
    }
    else {
      this.id = remoteHost.toExternalForm();
    }
    
    List<DesiredCapabilities> capabilities = getConfigurationcapabilities;
    
    List<TestSlot> slots = new ArrayList();
    for (DesiredCapabilities capability : capabilities) {
      Object maxInstance = capability.getCapability("maxInstances");
      
      SeleniumProtocol protocol = SeleniumProtocol.fromCapabilitiesMap(capability.asMap());
      
      if (maxInstance == null) {
        log.warning("Max instance not specified. Using default = 1 instance");
        maxInstance = "1";
      }
      
      int value = Integer.parseInt(maxInstance.toString());
      for (int i = 0; i < value; i++) {
        Map<String, Object> c = new HashMap();
        for (String k : capability.asMap().keySet()) {
          c.put(k, capability.getCapability(k));
        }
        slots.add(createTestSlot(protocol, c));
      }
    }
    
    testSlots = Collections.unmodifiableList(slots);
  }
  
  public void setupTimeoutListener() {
    cleanUpThread = null;
    if (((this instanceof TimeoutListener)) && 
      (config.cleanUpCycle.intValue() > 0) && (config.timeout.intValue() > 0)) {
      log.fine("starting cleanup thread");
      cleanUpThread = new CleanUpThread(this);
      new Thread(cleanUpThread, "RemoteProxy CleanUpThread for " + getId())
        .start();
    }
  }
  
  public String getId()
  {
    if (id == null) {
      throw new RuntimeException("Bug. Trying to use the id on a proxy but it hasn't been set.");
    }
    
    return id;
  }
  
  public void teardown() {
    stop = true;
  }
  


  public void forceSlotCleanerRun()
  {
    cleanUpThread.cleanUpAllSlots();
  }
  
  class CleanUpThread implements Runnable
  {
    private BaseRemoteProxy proxy;
    
    public CleanUpThread(BaseRemoteProxy proxy) {
      this.proxy = proxy;
    }
    
    public void run()
    {
      BaseRemoteProxy.log.fine("cleanup thread starting...");
      while (!proxy.stop) {
        try {
          Thread.sleep(config.cleanUpCycle.intValue());
        } catch (InterruptedException e) {
          BaseRemoteProxy.log.severe("clean up thread died. " + e.getMessage());
        }
        
        cleanUpAllSlots();
      }
    }
    
    void cleanUpAllSlots() {
      for (TestSlot slot : getTestSlots()) {
        try {
          cleanUpSlot(slot);
        } catch (Throwable t) {
          BaseRemoteProxy.log.warning("Error executing the timeout when cleaning up slot " + slot + t
            .getMessage());
        }
      }
    }
    
    private void cleanUpSlot(TestSlot slot) {
      TestSession session = slot.getSession();
      if (session != null) {
        long inactivity = session.getInactivityTime();
        boolean hasTimedOut = inactivity > getTimeOut();
        if ((hasTimedOut) && 
          (!session.isForwardingRequest())) {
          BaseRemoteProxy.log.logp(Level.WARNING, "SessionCleanup", null, "session " + session + " has TIMED OUT due to client inactivity and will be released.");
          
          try
          {
            ((TimeoutListener)proxy).beforeRelease(session);
          } catch (IllegalStateException ignore) {
            BaseRemoteProxy.log.log(Level.WARNING, ignore.getMessage());
          }
          registry.terminate(session, SessionTerminationReason.TIMEOUT);
        }
        

        if (session.isOrphaned()) {
          BaseRemoteProxy.log.logp(Level.WARNING, "SessionCleanup", null, "session " + session + " has been ORPHANED and will be released");
          try
          {
            ((TimeoutListener)proxy).beforeRelease(session);
          } catch (IllegalStateException ignore) {
            BaseRemoteProxy.log.log(Level.WARNING, ignore.getMessage());
          }
          registry.terminate(session, SessionTerminationReason.ORPHAN);
        }
      }
    }
  }
  
  public GridNodeConfiguration getConfig() {
    return config;
  }
  
  public RegistrationRequest getOriginalRegistrationRequest() {
    return request;
  }
  
  public int getMaxNumberOfConcurrentTestSessions() {
    return config.maxSession.intValue();
  }
  
  public URL getRemoteHost() {
    return remoteHost;
  }
  
  public TestSession getNewSession(Map<String, Object> requestedCapability) {
    log.fine("Trying to create a new session on node " + this);
    
    if (!hasCapability(requestedCapability)) {
      log.fine("Node " + this + " has no matching capability");
      return null;
    }
    
    if (getTotalUsed() >= config.maxSession.intValue()) {
      log.fine("Node " + this + " has no free slots");
      return null;
    }
    
    for (TestSlot testslot : getTestSlots()) {
      TestSession session = testslot.getNewSession(requestedCapability);
      
      if (session != null) {
        return session;
      }
    }
    return null;
  }
  
  public int getTotalUsed() {
    int totalUsed = 0;
    
    for (TestSlot slot : getTestSlots()) {
      if (slot.getSession() != null) {
        totalUsed++;
      }
    }
    
    return totalUsed;
  }
  
  public boolean hasCapability(Map<String, Object> requestedCapability) {
    for (TestSlot slot : getTestSlots()) {
      if (slot.matches(requestedCapability)) {
        return true;
      }
    }
    
    return false;
  }
  
  public boolean isBusy() {
    return getTotalUsed() != 0;
  }
  









  public static <T extends RemoteProxy> T getNewInstance(RegistrationRequest request, Registry registry)
  {
    try
    {
      String proxyClass = getConfigurationproxy;
      if (proxyClass == null) {
        log.fine("No proxy class. Using default");
        proxyClass = BaseRemoteProxy.class.getCanonicalName();
      }
      Class<?> clazz = Class.forName(proxyClass);
      log.fine("Using class " + clazz.getName());
      Object[] args = { request, registry };
      Class<?>[] argsClass = { RegistrationRequest.class, Registry.class };
      Constructor<?> c = clazz.getConstructor(argsClass);
      Object proxy = c.newInstance(args);
      if ((proxy instanceof RemoteProxy)) {
        ((RemoteProxy)proxy).setupTimeoutListener();
        return (RemoteProxy)proxy;
      }
      throw new InvalidParameterException("Error: " + proxy.getClass() + " isn't a remote proxy");
    } catch (InvocationTargetException e) {
      throw new InvalidParameterException("Error: " + e.getTargetException().getMessage());
    } catch (Exception e) {
      throw new InvalidParameterException("Error: " + e.getMessage());
    }
  }
  
  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (id == null ? 0 : id.hashCode());
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    
    if (obj == null) {
      return false;
    }
    
    if (getClass() != obj.getClass()) {
      return false;
    }
    
    RemoteProxy other = (RemoteProxy)obj;
    if (getId() == null) {
      if (other.getId() != null) {
        return false;
      }
    } else if (!getId().equals(other.getId())) {
      return false;
    }
    
    return true;
  }
  
  public int compareTo(RemoteProxy o)
  {
    if (o == null) {
      return -1;
    }
    return (int)(getResourceUsageInPercent() - o.getResourceUsageInPercent());
  }
  
  public String toString()
  {
    return getRemoteHost() != null ? getRemoteHost().toString() : "<detached>";
  }
  
  private final HtmlRenderer renderer = new DefaultHtmlRenderer(this);
  
  public HtmlRenderer getHtmlRender() {
    return renderer;
  }
  
  public int getTimeOut() {
    return config.timeout.intValue() * 1000;
  }
  
  public HttpClientFactory getHttpClientFactory()
  {
    return getRegistry().getHttpClientFactory();
  }
  

  public JsonObject getStatus()
    throws GridException
  {
    String url = getRemoteHost().toExternalForm() + "/wd/hub/status";
    BasicHttpRequest r = new BasicHttpRequest("GET", url);
    HttpClient client = getHttpClientFactory().getGridHttpClient(config.nodeStatusCheckTimeout.intValue(), config.nodeStatusCheckTimeout.intValue());
    HttpHost host = new HttpHost(getRemoteHost().getHost(), getRemoteHost().getPort(), getRemoteHost().getProtocol());
    
    String existingName = Thread.currentThread().getName();
    HttpEntity entity = null;
    try {
      Thread.currentThread().setName("Probing status of " + url);
      HttpResponse response = client.execute(host, r);
      entity = response.getEntity();
      int code = response.getStatusLine().getStatusCode();
      
      if (code == 200) {
        JsonObject status = new JsonObject();
        try {
          status = extractObject(response);
        }
        catch (Exception localException1) {}
        
        EntityUtils.consume(response.getEntity());
        return status; }
      if (code == 404) {
        JsonObject status = new JsonObject();
        EntityUtils.consume(response.getEntity());
        return status;
      }
      EntityUtils.consume(response.getEntity());
      throw new GridException("server response code : " + code);
    }
    catch (Exception e)
    {
      throw new GridException(e.getMessage(), e);
    } finally {
      Thread.currentThread().setName(existingName);
      try {
        EntityUtils.consume(entity);
      } catch (IOException e) {
        log.info("Exception thrown when consume entity");
      }
    }
  }
  
  private JsonObject extractObject(HttpResponse resp) throws IOException
  {
    BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
    StringBuilder s = new StringBuilder();
    
    String line;
    while ((line = rd.readLine()) != null) {
      s.append(line);
    }
    rd.close();
    
    return new JsonParser().parse(s.toString()).getAsJsonObject();
  }
  
  public float getResourceUsageInPercent()
  {
    return 100.0F * getTotalUsed() / getMaxNumberOfConcurrentTestSessions();
  }
  
  public long getLastSessionStart() {
    long last = -1L;
    for (TestSlot slot : getTestSlots()) {
      last = Math.max(last, slot.getLastSessionStart());
    }
    return last;
  }
}
