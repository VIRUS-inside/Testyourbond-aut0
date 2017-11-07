package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;
















public class GetElementRect
  extends WebElementHandler<Rectangle>
{
  public GetElementRect(Session session)
  {
    super(session);
  }
  
  public Rectangle call() throws Exception
  {
    WebElement element = getElement();
    return element.getRect();
  }
  

  public String toString()
  {
    return String.format("[get element rect: %s]", new Object[] { getElementAsString() });
  }
}
