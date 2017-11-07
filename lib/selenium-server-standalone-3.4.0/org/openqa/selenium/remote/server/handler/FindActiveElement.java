package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.Session;















public class FindActiveElement
  extends WebDriverHandler<Map<String, String>>
{
  public FindActiveElement(Session session)
  {
    super(session);
  }
  
  public Map<String, String> call()
    throws Exception
  {
    WebElement element = getDriver().switchTo().activeElement();
    String elementId = getKnownElements().add(element);
    return ImmutableMap.of("ELEMENT", elementId);
  }
  
  public String toString()
  {
    return "[find active element]";
  }
}
