package org.openqa.selenium.remote.server.handler;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.JsonParametersAware;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.Session;

















public class FindElements
  extends WebDriverHandler<Set<Map<String, String>>>
  implements JsonParametersAware
{
  private volatile By by;
  
  public FindElements(Session session)
  {
    super(session);
  }
  
  public void setJsonParameters(Map<String, Object> allParameters) throws Exception {
    by = newBySelector().pickFromJsonParameters(allParameters);
  }
  
  public Set<Map<String, String>> call() throws Exception
  {
    List<WebElement> elements = getDriver().findElements(by);
    Sets.newLinkedHashSet(
      Iterables.transform(elements, new Function()
      {
        public Map<String, String> apply(WebElement element) {
          return ImmutableMap.of("ELEMENT", getKnownElements().add(element));
        }
      }));
  }
  
  public String toString()
  {
    return String.format("[find elements: %s]", new Object[] { by });
  }
}
