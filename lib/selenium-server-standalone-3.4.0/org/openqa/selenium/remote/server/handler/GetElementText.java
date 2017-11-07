package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;















public class GetElementText
  extends WebElementHandler<String>
{
  public GetElementText(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    return getElement().getText();
  }
  
  public String toString()
  {
    return String.format("[get text: %s]", new Object[] { getElementAsString() });
  }
}
