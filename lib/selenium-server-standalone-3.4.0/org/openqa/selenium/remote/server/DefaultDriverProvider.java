package org.openqa.selenium.remote.server;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;





















public class DefaultDriverProvider
  implements DriverProvider
{
  private static final Logger LOG = Logger.getLogger(DefaultDriverProvider.class.getName());
  private Capabilities capabilities;
  private Class<? extends WebDriver> driverClass;
  private String driverClassName;
  
  public DefaultDriverProvider(Capabilities capabilities, Class<? extends WebDriver> driverClass)
  {
    this.capabilities = new DesiredCapabilities(capabilities);
    this.driverClass = driverClass;
  }
  
  public DefaultDriverProvider(Capabilities capabilities, String driverClassName) {
    this.capabilities = new DesiredCapabilities(capabilities);
    this.driverClassName = driverClassName;
  }
  
  public Capabilities getProvidedCapabilities()
  {
    return capabilities;
  }
  



  public boolean canCreateDriverInstances()
  {
    return getDriverClass() != null;
  }
  






  public boolean canCreateDriverInstanceFor(Capabilities capabilities)
  {
    return this.capabilities.getBrowserName().equals(capabilities.getBrowserName());
  }
  
  private Class<? extends WebDriver> getDriverClass() {
    if (driverClass != null) {
      return driverClass;
    }
    try {
      return Class.forName(driverClassName).asSubclass(WebDriver.class);
    } catch (ClassNotFoundException e) {
      LOG.log(Level.INFO, "Driver class not found: " + driverClassName);
      return null;
    } catch (NoClassDefFoundError e) {
      LOG.log(Level.INFO, "Driver class not found: " + driverClassName);
      return null;
    } catch (UnsupportedClassVersionError e) {
      LOG.log(Level.INFO, "Driver class is built for higher Java version: " + driverClassName); }
    return null;
  }
  

  public WebDriver newInstance(Capabilities capabilities)
  {
    LOG.info("Creating a new session for " + capabilities);
    
    return callConstructor(getDriverClass(), capabilities);
  }
  
  private WebDriver callConstructor(Class<? extends WebDriver> from, Capabilities capabilities) {
    try {
      Constructor<? extends WebDriver> constructor = from.getConstructor(new Class[] { Capabilities.class });
      return (WebDriver)constructor.newInstance(new Object[] { capabilities });
    } catch (NoSuchMethodException e) {
      try {
        return (WebDriver)from.newInstance();
      } catch (ReflectiveOperationException e1) {
        throw new WebDriverException(e);
      }
    } catch (ReflectiveOperationException e) {
      throw new WebDriverException(e);
    }
  }
  
  public String toString()
  {
    return driverClass != null ? driverClass.toString() : driverClassName;
  }
}
