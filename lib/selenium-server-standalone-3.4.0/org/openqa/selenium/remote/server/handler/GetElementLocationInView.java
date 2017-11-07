package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.server.Session;















public class GetElementLocationInView
  extends WebElementHandler<Point>
{
  public GetElementLocationInView(Session session)
  {
    super(session);
  }
  
  public Point call() throws Exception
  {
    Locatable element = (Locatable)getElement();
    return element.getCoordinates().inViewPort();
  }
  
  public String toString()
  {
    return String.format("[get location in view: %s]", new Object[] { getElementAsString() });
  }
}
