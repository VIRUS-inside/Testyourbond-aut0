package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;















public class ClickElement
  extends WebElementHandler<Void>
{
  public ClickElement(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getElement().click();
    return null;
  }
  
  public String toString()
  {
    return String.format("[click: %s]", new Object[] { getElementAsString() });
  }
}
