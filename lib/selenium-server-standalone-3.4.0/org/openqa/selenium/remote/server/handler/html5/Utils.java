package org.openqa.selenium.remote.server.handler.html5;

import com.google.common.base.Throwables;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.html5.ApplicationCache;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.mobile.NetworkConnection;
import org.openqa.selenium.remote.ExecuteMethod;
import org.openqa.selenium.remote.html5.RemoteApplicationCache;
import org.openqa.selenium.remote.html5.RemoteLocationContext;
import org.openqa.selenium.remote.html5.RemoteWebStorage;
import org.openqa.selenium.remote.mobile.RemoteNetworkConnection;





















public class Utils
{
  public Utils() {}
  
  static ApplicationCache getApplicationCache(WebDriver driver)
  {
    return (ApplicationCache)convert(driver, ApplicationCache.class, "applicationCacheEnabled", RemoteApplicationCache.class);
  }
  
  public static NetworkConnection getNetworkConnection(WebDriver driver)
  {
    return (NetworkConnection)convert(driver, NetworkConnection.class, "networkConnectionEnabled", RemoteNetworkConnection.class);
  }
  
  static LocationContext getLocationContext(WebDriver driver)
  {
    return (LocationContext)convert(driver, LocationContext.class, "locationContextEnabled", RemoteLocationContext.class);
  }
  
  static WebStorage getWebStorage(WebDriver driver)
  {
    return (WebStorage)convert(driver, WebStorage.class, "webStorageEnabled", RemoteWebStorage.class);
  }
  


  private static <T> T convert(WebDriver driver, Class<T> interfaceClazz, String capability, Class<? extends T> remoteImplementationClazz)
  {
    if (interfaceClazz.isInstance(driver)) {
      return interfaceClazz.cast(driver);
    }
    
    if (((driver instanceof ExecuteMethod)) && ((driver instanceof HasCapabilities)))
    {
      if (((HasCapabilities)driver).getCapabilities().is(capability)) {
        try {
          return 
          
            remoteImplementationClazz.getConstructor(new Class[] { ExecuteMethod.class }).newInstance(new Object[] { (ExecuteMethod)driver });
        } catch (InstantiationException e) {
          throw new WebDriverException(e);
        } catch (IllegalAccessException e) {
          throw new WebDriverException(e);
        } catch (InvocationTargetException e) {
          throw Throwables.propagate(e.getCause());
        } catch (NoSuchMethodException e) {
          throw new WebDriverException(e);
        }
      }
    }
    

    throw new UnsupportedCommandException("driver (" + driver.getClass().getName() + ") does not support " + interfaceClazz.getName());
  }
}
