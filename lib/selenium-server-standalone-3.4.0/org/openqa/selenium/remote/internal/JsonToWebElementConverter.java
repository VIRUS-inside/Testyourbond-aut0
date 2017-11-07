package org.openqa.selenium.remote.internal;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import org.openqa.selenium.remote.Dialect;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;























public class JsonToWebElementConverter
  implements Function<Object, Object>
{
  private final RemoteWebDriver driver;
  
  public JsonToWebElementConverter(RemoteWebDriver driver)
  {
    this.driver = driver;
  }
  
  public Object apply(Object result) {
    if ((result instanceof Collection)) {
      Collection<?> results = (Collection)result;
      return Lists.newArrayList(Iterables.transform(results, this));
    }
    
    if ((result instanceof Map)) {
      Map<?, ?> resultAsMap = (Map)result;
      if (resultAsMap.containsKey(Dialect.OSS.getEncodedElementKey())) {
        RemoteWebElement element = newRemoteWebElement();
        element.setId(String.valueOf(resultAsMap.get(Dialect.OSS.getEncodedElementKey())));
        return element; }
      if (resultAsMap.containsKey(Dialect.W3C.getEncodedElementKey())) {
        RemoteWebElement element = newRemoteWebElement();
        element.setId(String.valueOf(resultAsMap.get(Dialect.W3C.getEncodedElementKey())));
        return element;
      }
      return Maps.transformValues(resultAsMap, this);
    }
    

    if ((result instanceof Number)) {
      if (((result instanceof Float)) || ((result instanceof Double))) {
        return Double.valueOf(((Number)result).doubleValue());
      }
      return Long.valueOf(((Number)result).longValue());
    }
    
    return result;
  }
  
  private RemoteWebElement newRemoteWebElement() {
    RemoteWebElement toReturn = new RemoteWebElement();
    toReturn.setParent(driver);
    toReturn.setFileDetector(driver.getFileDetector());
    return toReturn;
  }
}
