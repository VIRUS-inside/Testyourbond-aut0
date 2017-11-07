package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;















public class GetCssProperty
  extends WebElementHandler<String>
{
  private volatile String propertyName;
  
  public GetCssProperty(Session session)
  {
    super(session);
  }
  
  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }
  
  public String call() throws Exception
  {
    WebElement element = getElement();
    return element.getCssValue(propertyName);
  }
  
  public String toString()
  {
    return String.format("[get value of css property: %s, %s]", new Object[] { getElementAsString(), propertyName });
  }
}
