package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.Session;
















public class FindElement
  extends WebDriverHandler<Map<String, String>>
  implements JsonParametersAware
{
  private static Logger log = Logger.getLogger(FindElement.class.getName());
  private volatile By by;
  
  public FindElement(Session session) {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    by = newBySelector().pickFromJsonParameters(allParameters);
  }
  
  public Map<String, String> call() throws Exception
  {
    try {
      WebElement element = getDriver().findElement(by);
      String elementId = getKnownElements().add(element);
      return ImmutableMap.of("ELEMENT", elementId);
    }
    catch (RuntimeException e) {
      if (!(e instanceof NoSuchElementException)) {
        log.log(Level.SEVERE, "Unexpected exception during findElement", e);
      }
      throw e;
    }
  }
  
  public String toString()
  {
    return String.format("[find element: %s]", new Object[] { by });
  }
}
