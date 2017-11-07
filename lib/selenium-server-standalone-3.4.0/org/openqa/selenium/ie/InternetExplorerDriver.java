package org.openqa.selenium.ie;

import com.google.common.base.Preconditions;
import java.io.File;
import java.util.HashMap;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ImmutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.service.DriverCommandExecutor;














































































































public class InternetExplorerDriver
  extends RemoteWebDriver
{
  public static final String IGNORE_ZOOM_SETTING = "ignoreZoomSetting";
  public static final String NATIVE_EVENTS = "nativeEvents";
  public static final String INITIAL_BROWSER_URL = "initialBrowserUrl";
  public static final String ELEMENT_SCROLL_BEHAVIOR = "elementScrollBehavior";
  public static final String UNEXPECTED_ALERT_BEHAVIOR = "unexpectedAlertBehaviour";
  public static final String ENABLE_ELEMENT_CACHE_CLEANUP = "enableElementCacheCleanup";
  public static final String BROWSER_ATTACH_TIMEOUT = "browserAttachTimeout";
  public static final String INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS = "ignoreProtectedModeSettings";
  public static final String ENABLE_PERSISTENT_HOVERING = "enablePersistentHover";
  public static final String REQUIRE_WINDOW_FOCUS = "requireWindowFocus";
  public static final String LOG_FILE = "logFile";
  public static final String LOG_LEVEL = "logLevel";
  public static final String HOST = "host";
  public static final String EXTRACT_PATH = "extractPath";
  public static final String SILENT = "silent";
  public static final String FORCE_CREATE_PROCESS = "ie.forceCreateProcessApi";
  public static final String IE_ENSURE_CLEAN_SESSION = "ie.ensureCleanSession";
  public static final String IE_USE_PER_PROCESS_PROXY = "ie.usePerProcessProxy";
  @Deprecated
  public static final String IE_USE_PRE_PROCESS_PROXY = "ie.usePerProcessProxy";
  public static final String IE_SWITCHES = "ie.browserCommandLineSwitches";
  private static final int DEFAULT_PORT = 0;
  
  public InternetExplorerDriver()
  {
    this(null, null, 0);
  }
  
  public InternetExplorerDriver(Capabilities capabilities) {
    this(null, capabilities, 0);
  }
  




  @Deprecated
  public InternetExplorerDriver(int port)
  {
    this(null, null, port);
  }
  




  @Deprecated
  public InternetExplorerDriver(InternetExplorerDriverService service)
  {
    this(service, null, 0);
  }
  




  @Deprecated
  public InternetExplorerDriver(InternetExplorerDriverService service, Capabilities capabilities)
  {
    this(service, capabilities, 0);
  }
  







  @Deprecated
  public InternetExplorerDriver(InternetExplorerDriverService service, Capabilities capabilities, int port)
  {
    if (capabilities == null) {
      capabilities = DesiredCapabilities.internetExplorer();
    }
    
    Preconditions.checkNotNull(capabilities);
    
    capabilities = new InternetExplorerOptions(capabilities).merge(new ImmutableCapabilities(new HashMap()));
    
    if (service == null) {
      service = setupService(capabilities, port);
    }
    run(service, capabilities);
  }
  
  private void run(InternetExplorerDriverService service, Capabilities capabilities) {
    assertOnWindows();
    
    setCommandExecutor(new DriverCommandExecutor(service));
    
    startSession(capabilities);
  }
  
  public void setFileDetector(FileDetector detector)
  {
    throw new WebDriverException("Setting the file detector only works on remote webdriver instances obtained via RemoteWebDriver");
  }
  


  public <X> X getScreenshotAs(OutputType<X> target)
  {
    String base64 = execute("screenshot").getValue().toString();
    

    return target.convertFromBase64Png(base64);
  }
  
  protected void assertOnWindows() {
    Platform current = Platform.getCurrent();
    if (!current.is(Platform.WINDOWS))
    {
      throw new WebDriverException(String.format("You appear to be running %s. The IE driver only runs on Windows.", new Object[] { current }));
    }
  }
  
  private InternetExplorerDriverService setupService(Capabilities caps, int port)
  {
    InternetExplorerDriverService.Builder builder = new InternetExplorerDriverService.Builder();
    builder.usingPort(port);
    
    if (caps != null) {
      if (caps.getCapability("logFile") != null) {
        String value = (String)caps.getCapability("logFile");
        if (value != null) {
          builder.withLogFile(new File(value));
        }
      }
      
      if (caps.getCapability("logLevel") != null) {
        String value = (String)caps.getCapability("logLevel");
        if (value != null) {
          builder.withLogLevel(InternetExplorerDriverLogLevel.valueOf(value));
        }
      }
      
      if (caps.getCapability("host") != null) {
        String value = (String)caps.getCapability("host");
        if (value != null) {
          builder.withHost(value);
        }
      }
      
      if (caps.getCapability("extractPath") != null) {
        String value = (String)caps.getCapability("extractPath");
        if (value != null) {
          builder.withExtractPath(new File(value));
        }
      }
      
      if (caps.getCapability("silent") != null) {
        Boolean value = (Boolean)caps.getCapability("silent");
        if (value != null) {
          builder.withSilent(value);
        }
      }
    }
    
    return (InternetExplorerDriverService)builder.build();
  }
}
