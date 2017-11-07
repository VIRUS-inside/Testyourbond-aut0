package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;
















public class GetElementDisplayed
  extends WebElementHandler<Boolean>
{
  public GetElementDisplayed(Session session)
  {
    super(session);
  }
  
  public Boolean call() throws Exception
  {
    WebElement element = getElement();
    return Boolean.valueOf(element.isDisplayed());
  }
  
  public String toString()
  {
    return String.format("[is displayed: %s]", new Object[] { getElementAsString() });
  }
}
