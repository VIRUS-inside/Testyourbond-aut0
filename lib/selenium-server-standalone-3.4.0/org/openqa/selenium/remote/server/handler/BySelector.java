package org.openqa.selenium.remote.server.handler;

import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
















public class BySelector
{
  public BySelector() {}
  
  public By pickFromJsonParameters(Map<String, Object> allParameters)
  {
    String method = (String)allParameters.get("using");
    String selector = (String)allParameters.get("value");
    
    return pickFrom(method, selector);
  }
  
  public By pickFrom(String method, String selector) {
    if ("class name".equals(method))
      return By.className(selector);
    if ("css selector".equals(method))
      return By.cssSelector(selector);
    if ("id".equals(method))
      return By.id(selector);
    if ("link text".equals(method))
      return By.linkText(selector);
    if ("partial link text".equals(method))
      return By.partialLinkText(selector);
    if ("name".equals(method))
      return By.name(selector);
    if ("tag name".equals(method))
      return By.tagName(selector);
    if ("xpath".equals(method)) {
      return By.xpath(selector);
    }
    throw new WebDriverException("Cannot find matching element locator to: " + method);
  }
}
