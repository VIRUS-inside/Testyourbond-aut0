package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;















public class GetTagName
  extends WebElementHandler<String>
{
  public GetTagName(Session session)
  {
    super(session);
  }
  
  public String call() throws Exception
  {
    return getElement().getTagName();
  }
  
  public String toString()
  {
    return String.format("[tag name: %s]", new Object[] { getElementAsString() });
  }
}
