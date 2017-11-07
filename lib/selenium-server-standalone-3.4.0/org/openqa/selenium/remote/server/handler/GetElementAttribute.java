package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;















public class GetElementAttribute
  extends WebElementHandler<String>
{
  private volatile String name;
  
  public GetElementAttribute(Session session)
  {
    super(session);
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String call() throws Exception
  {
    return getElement().getAttribute(name);
  }
  
  public String toString()
  {
    return String.format("[get element attribute: %s, %s]", new Object[] { getElementAsString(), name });
  }
}
