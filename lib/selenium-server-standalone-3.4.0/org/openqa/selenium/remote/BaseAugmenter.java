package org.openqa.selenium.remote;

import com.google.common.collect.Maps;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.html5.AddApplicationCache;
import org.openqa.selenium.remote.html5.AddLocationContext;
import org.openqa.selenium.remote.html5.AddWebStorage;
import org.openqa.selenium.remote.mobile.AddNetworkConnection;


































public abstract class BaseAugmenter
{
  private final Map<String, AugmenterProvider> driverAugmentors = Maps.newHashMap();
  private final Map<String, AugmenterProvider> elementAugmentors = Maps.newHashMap();
  
  public BaseAugmenter() {
    addDriverAugmentation("cssSelectorsEnabled", new AddFindsByCss());
    addDriverAugmentation("locationContextEnabled", new AddLocationContext());
    addDriverAugmentation("applicationCacheEnabled", new AddApplicationCache());
    addDriverAugmentation("networkConnectionEnabled", new AddNetworkConnection());
    addDriverAugmentation("webStorageEnabled", new AddWebStorage());
    addDriverAugmentation("rotatable", new AddRotatable());
    addDriverAugmentation("hasTouchScreen", new AddRemoteTouchScreen());
    
    addElementAugmentation("cssSelectorsEnabled", new AddFindsChildByCss());
  }
  











  public void addDriverAugmentation(String capabilityName, AugmenterProvider handlerClass)
  {
    driverAugmentors.put(capabilityName, handlerClass);
  }
  











  public void addElementAugmentation(String capabilityName, AugmenterProvider handlerClass)
  {
    elementAugmentors.put(capabilityName, handlerClass);
  }
  










  public WebDriver augment(WebDriver driver)
  {
    RemoteWebDriver remoteDriver = extractRemoteWebDriver(driver);
    if (remoteDriver == null) {
      return driver;
    }
    return (WebDriver)create(remoteDriver, driverAugmentors, driver);
  }
  










  public WebElement augment(RemoteWebElement element)
  {
    RemoteWebDriver parent = (RemoteWebDriver)element.getWrappedDriver();
    if (parent == null) {
      return element;
    }
    
    return (WebElement)create(parent, elementAugmentors, element);
  }
  
  protected abstract <X> X create(RemoteWebDriver paramRemoteWebDriver, Map<String, AugmenterProvider> paramMap, X paramX);
  
  protected abstract RemoteWebDriver extractRemoteWebDriver(WebDriver paramWebDriver);
}
