package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;















public class SubmitElement
  extends WebElementHandler<Void>
{
  public SubmitElement(Session session)
  {
    super(session);
  }
  
  public Void call() throws Exception
  {
    getElement().submit();
    
    return null;
  }
  
  public String toString()
  {
    return String.format("[submit: %s]", new Object[] { getElementAsString() });
  }
}
