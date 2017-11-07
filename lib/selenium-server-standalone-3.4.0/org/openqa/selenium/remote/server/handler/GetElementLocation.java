package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;
















public class GetElementLocation
  extends WebElementHandler<Point>
{
  public GetElementLocation(Session session)
  {
    super(session);
  }
  
  public Point call() throws Exception
  {
    WebElement element = getElement();
    return element.getLocation();
  }
  
  public String toString()
  {
    return String.format("[get location: %s]", new Object[] { getElementAsString() });
  }
}
