package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.remote.server.KnownElements;
import org.openqa.selenium.remote.server.KnownElements.ProxiedElement;
import org.openqa.selenium.remote.server.Session;














public class ElementEquality
  extends WebElementHandler<Boolean>
{
  private volatile String otherId;
  
  public ElementEquality(Session session)
  {
    super(session);
  }
  
  public void setOther(String otherId) {
    this.otherId = otherId;
  }
  
  public Boolean call()
    throws Exception
  {
    WebElement one = getElement();
    WebElement two = getKnownElements().get(otherId);
    

    if ((one instanceof WrapsElement)) {
      one = ((WrapsElement)one).getWrappedElement();
    }
    if ((two instanceof KnownElements.ProxiedElement)) {
      two = ((KnownElements.ProxiedElement)two).getWrappedElement();
    }
    
    return Boolean.valueOf(one.equals(two));
  }
  
  public String toString()
  {
    return String.format("[equals: %s, %s]", new Object[] { getElementAsString(), otherId });
  }
}
