package org.openqa.selenium.chrome;

import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.mobile.NetworkConnection;
import org.openqa.selenium.mobile.NetworkConnection.ConnectionType;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.RemoteTouchScreen;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.html5.RemoteLocationContext;
import org.openqa.selenium.remote.html5.RemoteWebStorage;
import org.openqa.selenium.remote.mobile.RemoteNetworkConnection;




























































































public class ChromeDriver
  extends RemoteWebDriver
  implements LocationContext, WebStorage, HasTouchScreen, NetworkConnection
{
  private RemoteLocationContext locationContext;
  private RemoteWebStorage webStorage;
  private TouchScreen touchScreen;
  private RemoteNetworkConnection networkConnection;
  
  public ChromeDriver()
  {
    this(ChromeDriverService.createDefaultService(), new ChromeOptions());
  }
  







  @Deprecated
  public ChromeDriver(ChromeDriverService service)
  {
    this(service, new ChromeOptions());
  }
  






  public ChromeDriver(Capabilities capabilities)
  {
    this(ChromeDriverService.createDefaultService(), capabilities);
  }
  





  public ChromeDriver(ChromeOptions options)
  {
    this(ChromeDriverService.createDefaultService(), options);
  }
  







  @Deprecated
  public ChromeDriver(ChromeDriverService service, ChromeOptions options)
  {
    this(service, options.toCapabilities());
  }
  







  @Deprecated
  public ChromeDriver(ChromeDriverService service, Capabilities capabilities)
  {
    super(new ChromeDriverCommandExecutor(service), capabilities);
    locationContext = new RemoteLocationContext(getExecuteMethod());
    webStorage = new RemoteWebStorage(getExecuteMethod());
    touchScreen = new RemoteTouchScreen(getExecuteMethod());
    networkConnection = new RemoteNetworkConnection(getExecuteMethod());
  }
  
  public void setFileDetector(FileDetector detector)
  {
    throw new WebDriverException("Setting the file detector only works on remote webdriver instances obtained via RemoteWebDriver");
  }
  


  public LocalStorage getLocalStorage()
  {
    return webStorage.getLocalStorage();
  }
  
  public SessionStorage getSessionStorage()
  {
    return webStorage.getSessionStorage();
  }
  
  public Location location()
  {
    return locationContext.location();
  }
  
  public void setLocation(Location location)
  {
    locationContext.setLocation(location);
  }
  
  public TouchScreen getTouch()
  {
    return touchScreen;
  }
  
  public NetworkConnection.ConnectionType getNetworkConnection()
  {
    return networkConnection.getNetworkConnection();
  }
  
  public NetworkConnection.ConnectionType setNetworkConnection(NetworkConnection.ConnectionType type)
  {
    return networkConnection.setNetworkConnection(type);
  }
  




  public void launchApp(String id)
  {
    execute("launchApp", ImmutableMap.of("id", id));
  }
}
