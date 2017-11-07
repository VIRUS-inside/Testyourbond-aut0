package org.openqa.selenium.remote.server.handler;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.Session;
















public class FindChildElement
  extends WebElementHandler<Map<String, String>>
  implements JsonParametersAware
{
  private volatile By by;
  
  public FindChildElement(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    by = newBySelector().pickFromJsonParameters(allParameters);
  }
  
  public Map<String, String> call() throws Exception
  {
    WebElement element = getElement().findElement(by);
    String elementId = getKnownElements().add(element);
    
    return ImmutableMap.of("ELEMENT", elementId);
  }
  
  public String toString()
  {
    return String.format("[find child element: %s, %s]", new Object[] { getElementAsString(), by });
  }
}
