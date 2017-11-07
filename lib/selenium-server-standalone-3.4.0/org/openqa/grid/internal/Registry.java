package org.openqa.grid.internal;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jcip.annotations.ThreadSafe;
import org.openqa.grid.internal.listeners.RegistrationListener;
import org.openqa.grid.internal.listeners.SelfHealingProxy;
import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.web.Hub;
import org.openqa.grid.web.servlet.handler.RequestHandler;
import org.openqa.grid.web.servlet.handler.SeleniumBasedRequest;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.internal.HttpClientFactory;
import org.openqa.selenium.remote.server.log.LoggingManager;
import org.openqa.selenium.remote.server.log.PerSessionLogHandler;























@ThreadSafe
public class Registry
{
  public static final String KEY = Registry.class.getName();
  private static final Logger LOG = Logger.getLogger(Registry.class.getName());
  


  private final ReentrantLock lock = new ReentrantLock();
  private final Condition testSessionAvailable = lock.newCondition();
  private final ProxySet proxies;
  private final ActiveTestSessions activeTestSessions = new ActiveTestSessions();
  private final GridHubConfiguration configuration;
  private final HttpClientFactory httpClientFactory;
  private final NewSessionRequestQueue newSessionQueue;
  private final Matcher matcherThread = new Matcher();
  private final List<RemoteProxy> registeringProxies = new CopyOnWriteArrayList();
  
  private volatile boolean stop = false;
  private volatile Hub hub;
  
  private Registry(Hub hub, GridHubConfiguration config)
  {
    this.hub = hub;
    newSessionQueue = new NewSessionRequestQueue();
    configuration = config;
    httpClientFactory = new HttpClientFactory();
    proxies = new ProxySet(throwOnCapabilityNotPresent.booleanValue());
    matcherThread.setUncaughtExceptionHandler(new UncaughtExceptionHandler(null));
  }
  

  public static Registry newInstance()
  {
    return newInstance(null, new GridHubConfiguration());
  }
  
  public static Registry newInstance(Hub hub, GridHubConfiguration config) {
    Registry registry = new Registry(hub, config);
    matcherThread.start();
    


    try
    {
      Thread.sleep(250L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return registry;
  }
  
  public GridHubConfiguration getConfiguration() {
    return configuration;
  }
  












  public void terminate(final TestSession session, final SessionTerminationReason reason)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        Registry.this._release(session.getSlot(), reason);
      }
    })
    


      .start();
  }
  






  private void _release(TestSlot testSlot, SessionTerminationReason reason)
  {
    if (!testSlot.startReleaseProcess()) {
      return;
    }
    
    if (!testSlot.performAfterSessionEvent()) {
      return;
    }
    
    String internalKey = testSlot.getInternalKey();
    try
    {
      lock.lock();
      testSlot.finishReleaseProcess();
      release(internalKey, reason);
    } finally {
      lock.unlock();
    }
  }
  
  void terminateSynchronousFOR_TEST_ONLY(TestSession testSession) {
    _release(testSession.getSlot(), SessionTerminationReason.CLIENT_STOPPED_SESSION);
  }
  


  public void removeIfPresent(RemoteProxy proxy)
  {
    if (proxies.contains(proxy)) {
      LOG.warning(String.format("Cleaning up stale test sessions on the unregistered node %s", new Object[] { proxy }));
      

      RemoteProxy p = proxies.remove(proxy);
      for (TestSlot slot : p.getTestSlots()) {
        forceRelease(slot, SessionTerminationReason.PROXY_REREGISTRATION);
      }
      p.teardown();
    }
  }
  




  public void forceRelease(TestSlot testSlot, SessionTerminationReason reason)
  {
    if (testSlot.getSession() == null) {
      return;
    }
    
    String internalKey = testSlot.getInternalKey();
    release(internalKey, reason);
    testSlot.doFinishRelease();
  }
  



  class Matcher
    extends Thread
  {
    Matcher()
    {
      super();
    }
    
    public void run()
    {
      try {
        lock.lock();
        Registry.this.assignRequestToProxy();
        
        lock.unlock(); } finally { lock.unlock();
      }
    }
  }
  
  public void stop()
  {
    stop = true;
    matcherThread.interrupt();
    newSessionQueue.stop();
    proxies.teardown();
    httpClientFactory.close();
  }
  
  public Hub getHub()
  {
    return hub;
  }
  
  public void setHub(Hub hub)
  {
    this.hub = hub;
  }
  
  public void addNewSessionRequest(RequestHandler handler) {
    try {
      lock.lock();
      
      proxies.verifyAbilityToHandleDesiredCapabilities(handler.getRequest().getDesiredCapabilities());
      newSessionQueue.add(handler);
      fireMatcherStateChanged();
      
      lock.unlock(); } finally { lock.unlock();
    }
  }
  






  private void assignRequestToProxy()
  {
    while (!stop) {
      try {
        testSessionAvailable.await(5L, TimeUnit.SECONDS);
        
        newSessionQueue.processQueue(new Predicate()
        {
          public boolean apply(RequestHandler input) { return Registry.this.takeRequestHandler(input); } }, configuration.prioritizer);
        


        LoggingManager.perSessionLogHandler().clearThreadTempLogs();
      } catch (InterruptedException e) {
        LOG.info("Shutting down registry.");
      } catch (Throwable t) {
        LOG.log(Level.SEVERE, "Unhandled exception in Matcher thread.", t);
      }
    }
  }
  
  private boolean takeRequestHandler(RequestHandler handler)
  {
    TestSession session = proxies.getNewSession(handler.getRequest().getDesiredCapabilities());
    boolean sessionCreated = session != null;
    if (sessionCreated) {
      activeTestSessions.add(session);
      handler.bindSession(session);
    }
    return sessionCreated;
  }
  





  private void release(TestSession session, SessionTerminationReason reason)
  {
    try
    {
      lock.lock();
      boolean removed = activeTestSessions.remove(session, reason);
      if (removed) {
        fireMatcherStateChanged();
      }
    } finally {
      lock.unlock();
    }
  }
  
  private void release(String internalKey, SessionTerminationReason reason) {
    if (internalKey == null) {
      return;
    }
    TestSession session1 = activeTestSessions.findSessionByInternalKey(internalKey);
    if (session1 != null) {
      release(session1, reason);
      return;
    }
    LOG.warning("Tried to release session with internal key " + internalKey + " but couldn't find it.");
  }
  






  public void add(RemoteProxy proxy)
  {
    if (proxy == null) {
      return;
    }
    LOG.info("Registered a node " + proxy);
    try {
      lock.lock();
      
      removeIfPresent(proxy);
      
      if (registeringProxies.contains(proxy)) {
        LOG.warning(String.format("Proxy '%s' is already queued for registration.", new Object[] { proxy }));
        
        return;
      }
      
      registeringProxies.add(proxy);
      fireMatcherStateChanged();
    } finally {
      lock.unlock();
    }
    
    boolean listenerOk = true;
    try {
      if ((proxy instanceof RegistrationListener)) {
        ((RegistrationListener)proxy).beforeRegistration();
      }
    } catch (Throwable t) {
      LOG.severe("Error running the registration listener on " + proxy + ", " + t.getMessage());
      t.printStackTrace();
      listenerOk = false;
    }
    try
    {
      lock.lock();
      registeringProxies.remove(proxy);
      if (listenerOk) {
        if ((proxy instanceof SelfHealingProxy)) {
          ((SelfHealingProxy)proxy).startPolling();
        }
        proxies.add(proxy);
        fireMatcherStateChanged();
      }
    } finally {
      lock.unlock();
    }
  }
  








  public void setThrowOnCapabilityNotPresent(boolean throwOnCapabilityNotPresent)
  {
    proxies.setThrowOnCapabilityNotPresent(throwOnCapabilityNotPresent);
  }
  
  private void fireMatcherStateChanged() {
    testSessionAvailable.signalAll();
  }
  
  public ProxySet getAllProxies() {
    return proxies;
  }
  
  public List<RemoteProxy> getUsedProxies() {
    return proxies.getBusyProxies();
  }
  






  public TestSession getSession(ExternalSessionKey externalKey)
  {
    return activeTestSessions.findSessionByExternalKey(externalKey);
  }
  








  public TestSession getExistingSession(ExternalSessionKey externalKey)
  {
    return activeTestSessions.getExistingSession(externalKey);
  }
  


  public int getNewSessionRequestCount()
  {
    return newSessionQueue.getNewSessionRequestCount();
  }
  
  public void clearNewSessionRequests() {
    newSessionQueue.clearNewSessionRequests();
  }
  
  public boolean removeNewSessionRequest(RequestHandler request) {
    return newSessionQueue.removeNewSessionRequest(request);
  }
  
  public Iterable<DesiredCapabilities> getDesiredCapabilities() {
    return newSessionQueue.getDesiredCapabilities();
  }
  
  public Set<TestSession> getActiveSessions() {
    return activeTestSessions.unmodifiableSet();
  }
  
  public RemoteProxy getProxyById(String id) {
    return proxies.getProxyById(id);
  }
  
  HttpClientFactory getHttpClientFactory() {
    return httpClientFactory;
  }
  
  private static class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private UncaughtExceptionHandler() {}
    
    public void uncaughtException(Thread t, Throwable e) { Registry.LOG.log(Level.SEVERE, "Matcher thread dying due to unhandled exception.", e); }
  }
}
