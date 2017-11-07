package org.openqa.selenium.opera;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.html5.RemoteLocationContext;
import org.openqa.selenium.remote.html5.RemoteWebStorage;
import org.openqa.selenium.remote.service.DriverCommandExecutor;



























































































public class OperaDriver
  extends RemoteWebDriver
  implements LocationContext, WebStorage
{
  private RemoteLocationContext locationContext;
  private RemoteWebStorage webStorage;
  
  public OperaDriver()
  {
    this(OperaDriverService.createDefaultService(), new OperaOptions());
  }
  






  public OperaDriver(OperaDriverService service)
  {
    this(service, new OperaOptions());
  }
  






  public OperaDriver(Capabilities capabilities)
  {
    this(OperaDriverService.createDefaultService(), capabilities);
  }
  





  public OperaDriver(OperaOptions options)
  {
    this(OperaDriverService.createDefaultService(), options);
  }
  






  public OperaDriver(OperaDriverService service, OperaOptions options)
  {
    this(service, options.toCapabilities());
  }
  






  public OperaDriver(OperaDriverService service, Capabilities capabilities)
  {
    super(new DriverCommandExecutor(service), capabilities);
    locationContext = new RemoteLocationContext(getExecuteMethod());
    webStorage = new RemoteWebStorage(getExecuteMethod());
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
}
