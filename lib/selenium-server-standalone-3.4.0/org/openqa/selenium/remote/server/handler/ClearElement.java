package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;















public class ClearElement
  extends WebElementHandler<Void>
{
  public ClearElement(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getElement().clear();
    return null;
  }
  
  public String toString()
  {
    return String.format("[clear: %s]", new Object[] { getElementAsString() });
  }
}
