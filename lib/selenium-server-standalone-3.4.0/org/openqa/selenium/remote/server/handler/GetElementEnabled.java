package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;
















public class GetElementEnabled
  extends WebElementHandler<Boolean>
{
  public GetElementEnabled(Session session)
  {
    super(session);
  }
  
  public Boolean call() throws Exception
  {
    return Boolean.valueOf(getElement().isEnabled());
  }
  
  public String toString()
  {
    return String.format("[is enabled: %s]", new Object[] { getElementAsString() });
  }
}
