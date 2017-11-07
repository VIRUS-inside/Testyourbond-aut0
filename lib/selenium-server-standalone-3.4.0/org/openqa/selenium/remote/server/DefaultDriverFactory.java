package org.openqa.selenium.remote.server;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;




















public class DefaultDriverFactory
  implements DriverFactory
{
  private static final Logger LOG = Logger.getLogger(DefaultDriverFactory.class.getName());
  
  private Map<Capabilities, DriverProvider> capabilitiesToDriverProvider = new ConcurrentHashMap();
  
  public DefaultDriverFactory() {}
  
  public void registerDriverProvider(DriverProvider driverProvider) { if (driverProvider.canCreateDriverInstances()) {
      capabilitiesToDriverProvider.put(driverProvider.getProvidedCapabilities(), driverProvider);
    } else {
      LOG.info(String.format("Driver provider %s is not registered", new Object[] { driverProvider }));
    }
  }
  
  @VisibleForTesting
  DriverProvider getProviderMatching(Capabilities desired)
  {
    Preconditions.checkState(!capabilitiesToDriverProvider.isEmpty(), "No drivers have been registered, will be unable to match %s", desired);
    

    Capabilities bestMatchingCapabilities = CapabilitiesComparator.getBestMatch(desired, capabilitiesToDriverProvider.keySet());
    return (DriverProvider)capabilitiesToDriverProvider.get(bestMatchingCapabilities);
  }
  
  public WebDriver newInstance(Capabilities capabilities) {
    DriverProvider provider = getProviderMatching(capabilities);
    if (provider.canCreateDriverInstanceFor(capabilities)) {
      return provider.newInstance(capabilities);
    }
    throw new WebDriverException(String.format("The best matching driver provider %s can't create a new driver instance for %s", new Object[] { provider, capabilities }));
  }
  

  public boolean hasMappingFor(Capabilities capabilities)
  {
    return capabilitiesToDriverProvider.containsKey(capabilities);
  }
}
