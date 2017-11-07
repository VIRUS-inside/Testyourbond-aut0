package org.openqa.selenium.safari;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverCommandExecutor;


























public class SafariDriver
  extends RemoteWebDriver
{
  public SafariDriver()
  {
    this(new SafariOptions());
  }
  






  public SafariDriver(Capabilities desiredCapabilities)
  {
    this(SafariOptions.fromCapabilities(desiredCapabilities));
  }
  




  public SafariDriver(SafariOptions safariOptions)
  {
    super(getExecutor(safariOptions), safariOptions.toCapabilities());
  }
  
  private static CommandExecutor getExecutor(SafariOptions options) {
    SafariDriverService service = SafariDriverService.createDefaultService(options);
    if (service == null) {
      throw new WebDriverException("SafariDriver requires Safari 10 running on OSX El Capitan or greater.");
    }
    return new DriverCommandExecutor(service);
  }
  
  public void setFileDetector(FileDetector detector)
  {
    throw new WebDriverException("Setting the file detector only works on remote webdriver instances obtained via RemoteWebDriver");
  }
}
