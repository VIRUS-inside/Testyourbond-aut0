package org.openqa.selenium.edge;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverCommandExecutor;





















































































public class EdgeDriver
  extends RemoteWebDriver
{
  public EdgeDriver()
  {
    this(EdgeDriverService.createDefaultService(), new EdgeOptions());
  }
  






  public EdgeDriver(EdgeDriverService service)
  {
    this(service, new EdgeOptions());
  }
  






  public EdgeDriver(Capabilities capabilities)
  {
    this(EdgeDriverService.createDefaultService(), capabilities);
  }
  





  public EdgeDriver(EdgeOptions options)
  {
    this(EdgeDriverService.createDefaultService(), options);
  }
  






  public EdgeDriver(EdgeDriverService service, EdgeOptions options)
  {
    this(service, options.toCapabilities());
  }
  






  public EdgeDriver(EdgeDriverService service, Capabilities capabilities)
  {
    super(new DriverCommandExecutor(service), capabilities);
  }
}
