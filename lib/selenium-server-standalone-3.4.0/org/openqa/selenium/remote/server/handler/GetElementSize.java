package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;
















public class GetElementSize
  extends WebElementHandler<Dimension>
{
  public GetElementSize(Session session)
  {
    super(session);
  }
  
  public Dimension call() throws Exception
  {
    WebElement element = getElement();
    return element.getSize();
  }
  

  public String toString()
  {
    return String.format("[get element size: %s]", new Object[] { getElementAsString() });
  }
}
