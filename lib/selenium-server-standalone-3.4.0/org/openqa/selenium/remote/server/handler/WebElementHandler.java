package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.Session;















public abstract class WebElementHandler<T>
  extends WebDriverHandler<T>
{
  private volatile String elementId;
  
  protected WebElementHandler(Session session)
  {
    super(session);
  }
  
  public void setId(String elementId) {
    this.elementId = elementId;
  }
  
  protected WebElement getElement() {
    return getKnownElements().get(elementId);
  }
  
  protected String getElementId() {
    return elementId;
  }
  
  protected String getElementAsString() {
    try {
      return elementId + " " + String.valueOf(getElement());
    }
    catch (RuntimeException localRuntimeException) {}
    

    return elementId + " unknown";
  }
}
