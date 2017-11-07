package org.openqa.selenium.remote.server;

import com.google.common.annotations.VisibleForTesting;
import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Rotatable;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.html5.ApplicationCache;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.openqa.selenium.mobile.NetworkConnection;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.events.EventFiringWebDriver;




































public class DefaultSession
  implements Session
{
  private static final String QUIET_EXCEPTIONS_KEY = "webdriver.remote.quietExceptions";
  private final SessionId sessionId;
  private final WebDriver driver;
  private final KnownElements knownElements;
  private final ThreadPoolExecutor executor;
  private final Capabilities capabilities;
  private final Clock clock;
  private volatile String base64EncodedImage;
  private volatile long lastAccess;
  private volatile Thread inUseWithThread = null;
  private TemporaryFilesystem tempFs;
  
  public static Session createSession(DriverFactory factory, SessionId sessionId, Capabilities capabilities)
    throws Exception
  {
    return createSession(factory, new SystemClock(), sessionId, capabilities);
  }
  
  public static Session createSession(DriverFactory factory, Clock clock, SessionId sessionId, Capabilities capabilities)
    throws Exception
  {
    File tmpDir = new File(System.getProperty("java.io.tmpdir"), sessionId.toString());
    if (!tmpDir.mkdir()) {
      throw new WebDriverException("Cannot create temp directory: " + tmpDir);
    }
    TemporaryFilesystem tempFs = TemporaryFilesystem.getTmpFsBasedOn(tmpDir);
    
    return new DefaultSession(factory, tempFs, clock, sessionId, capabilities);
  }
  
  @VisibleForTesting
  public static Session createSession(DriverFactory factory, TemporaryFilesystem tempFs, Clock clock, SessionId sessionId, Capabilities capabilities)
    throws Exception
  {
    return new DefaultSession(factory, tempFs, clock, sessionId, capabilities);
  }
  
  private DefaultSession(DriverFactory factory, TemporaryFilesystem tempFs, Clock clock, SessionId sessionId, Capabilities capabilities) throws Exception
  {
    knownElements = new KnownElements();
    this.sessionId = sessionId;
    this.tempFs = tempFs;
    this.clock = clock;
    BrowserCreator browserCreator = new BrowserCreator(factory, capabilities);
    FutureTask<EventFiringWebDriver> webDriverFutureTask = new FutureTask(browserCreator);
    
    executor = new ThreadPoolExecutor(1, 1, 600L, TimeUnit.SECONDS, new LinkedBlockingQueue());
    



    EventFiringWebDriver initialDriver = (EventFiringWebDriver)execute(webDriverFutureTask);
    
    if (!isQuietModeEnabled(browserCreator, capabilities))
    {
      initialDriver.register(new SnapshotScreenListener(this));
    }
    
    driver = initialDriver;
    this.capabilities = browserCreator.getCapabilityDescription();
    updateLastAccessTime();
  }
  
  private static boolean isQuietModeEnabled(BrowserCreator browserCreator, Capabilities capabilities) {
    if (browserCreator.isAndroid()) {
      return true;
    }
    boolean propertySaysQuiet = "true".equalsIgnoreCase(System.getProperty("webdriver.remote.quietExceptions"));
    if (capabilities == null) {
      return propertySaysQuiet;
    }
    if (capabilities.is("webdriver.remote.quietExceptions")) {
      return true;
    }
    Object cap = capabilities.asMap().get("webdriver.remote.quietExceptions");
    boolean isExplicitlyDisabledByCapability = (cap != null) && ("false".equalsIgnoreCase(cap.toString()));
    return (propertySaysQuiet) && (!isExplicitlyDisabledByCapability);
  }
  


  public void updateLastAccessTime()
  {
    lastAccess = clock.now();
  }
  
  public boolean isTimedOut(long timeout) {
    return (timeout > 0L) && (lastAccess + timeout < clock.now());
  }
  
  public void close() {
    executor.shutdown();
    if (tempFs != null) {
      tempFs.deleteTemporaryFiles();
      tempFs.deleteBaseDir();
    }
  }
  


  public <X> X execute(final FutureTask<X> future)
    throws Exception
  {
    executor.execute(new Runnable() {
      public void run() {
        inUseWithThread = Thread.currentThread();
        inUseWithThread.setName("Session " + sessionId + " processing inside browser");
        try {
          future.run();
          
          inUseWithThread = null;
          Thread.currentThread().setName("Session " + sessionId + " awaiting client");
        }
        finally
        {
          inUseWithThread = null;
          Thread.currentThread().setName("Session " + sessionId + " awaiting client");
        }
      }
    });
    return future.get();
  }
  
  public WebDriver getDriver() {
    updateLastAccessTime();
    return driver;
  }
  
  public KnownElements getKnownElements() {
    return knownElements;
  }
  
  public Capabilities getCapabilities() {
    return capabilities;
  }
  
  public void attachScreenshot(String base64EncodedImage) {
    this.base64EncodedImage = base64EncodedImage;
  }
  
  public String getAndClearScreenshot() {
    String temp = base64EncodedImage;
    base64EncodedImage = null;
    return temp;
  }
  
  private class BrowserCreator implements Callable<EventFiringWebDriver>
  {
    private final DriverFactory factory;
    private final Capabilities capabilities;
    private volatile Capabilities describedCapabilities;
    private volatile boolean isAndroid = false;
    
    BrowserCreator(DriverFactory factory, Capabilities capabilities) {
      this.factory = factory;
      this.capabilities = capabilities;
    }
    
    public EventFiringWebDriver call() throws Exception {
      WebDriver rawDriver = factory.newInstance(capabilities);
      Capabilities actualCapabilities = capabilities;
      if ((rawDriver instanceof HasCapabilities)) {
        actualCapabilities = ((HasCapabilities)rawDriver).getCapabilities();
        isAndroid = actualCapabilities.getPlatform().is(Platform.ANDROID);
      }
      describedCapabilities = getDescription(rawDriver, actualCapabilities);
      return new EventFiringWebDriver(rawDriver);
    }
    
    public Capabilities getCapabilityDescription() {
      return describedCapabilities;
    }
    
    public boolean isAndroid() {
      return isAndroid;
    }
    
    private DesiredCapabilities getDescription(WebDriver instance, Capabilities capabilities) {
      DesiredCapabilities caps = new DesiredCapabilities(capabilities.asMap());
      caps.setJavascriptEnabled(instance instanceof JavascriptExecutor);
      if ((instance instanceof TakesScreenshot)) {
        caps.setCapability("takesScreenshot", true);
      }
      if ((instance instanceof LocationContext)) {
        caps.setCapability("locationContextEnabled", true);
      }
      if ((instance instanceof ApplicationCache)) {
        caps.setCapability("applicationCacheEnabled", true);
      }
      if ((instance instanceof NetworkConnection)) {
        caps.setCapability("networkConnectionEnabled", true);
      }
      if ((instance instanceof WebStorage)) {
        caps.setCapability("webStorageEnabled", true);
      }
      if ((instance instanceof FindsByCssSelector)) {
        caps.setCapability("cssSelectorsEnabled", true);
      }
      if ((instance instanceof Rotatable)) {
        caps.setCapability("rotatable", true);
      }
      if ((instance instanceof HasTouchScreen)) {
        caps.setCapability("hasTouchScreen", true);
      }
      return caps;
    }
  }
  
  public SessionId getSessionId() {
    return sessionId;
  }
  
  public TemporaryFilesystem getTemporaryFileSystem() {
    return tempFs;
  }
  
  public boolean isInUse() {
    return inUseWithThread != null;
  }
  
  public void interrupt() {
    Thread threadToStop = inUseWithThread;
    if (threadToStop != null) {
      synchronized (threadToStop) {
        threadToStop.interrupt();
      }
    }
  }
}
