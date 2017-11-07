package org.openqa.selenium.remote.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;





















public class FirefoxDriverProvider
  implements DriverProvider
{
  private static final Logger LOG = Logger.getLogger(FirefoxDriverProvider.class.getName());
  
  public FirefoxDriverProvider() {}
  
  public Capabilities getProvidedCapabilities() { return DesiredCapabilities.firefox(); }
  




  public boolean canCreateDriverInstances()
  {
    return true;
  }
  






  public boolean canCreateDriverInstanceFor(Capabilities capabilities)
  {
    return "firefox".equals(capabilities.getBrowserName());
  }
  
  public WebDriver newInstance(Capabilities capabilities)
  {
    LOG.info("Creating a new session for " + capabilities);
    return callConstructor("org.openqa.selenium.firefox.FirefoxDriver", capabilities);
  }
  
  private Class<? extends WebDriver> getDriverClass(String driverClassName) {
    try {
      return Class.forName(driverClassName).asSubclass(WebDriver.class);
    } catch (ClassNotFoundException|NoClassDefFoundError e) {
      throw new WebDriverException("Driver class not found: " + driverClassName);
    } catch (UnsupportedClassVersionError e) {
      throw new WebDriverException("Driver class is built for higher Java version: " + driverClassName);
    }
  }
  
  private WebDriver callConstructor(String driverClassName, Capabilities capabilities)
  {
    Class<? extends WebDriver> from = getDriverClass(driverClassName);
    try {
      Constructor<? extends WebDriver> constructor = from.getConstructor(new Class[] { Capabilities.class });
      return (WebDriver)constructor.newInstance(new Object[] { capabilities });
    } catch (NoSuchMethodException e) {
      try {
        return (WebDriver)from.newInstance();
      } catch (InstantiationException|IllegalAccessException e1) {
        throw new WebDriverException(e);
      }
    } catch (InvocationTargetException|InstantiationException|IllegalAccessException e) {
      throw new WebDriverException(e);
    }
  }
  
  public String toString()
  {
    return "Firefox/Marionette driver";
  }
}
