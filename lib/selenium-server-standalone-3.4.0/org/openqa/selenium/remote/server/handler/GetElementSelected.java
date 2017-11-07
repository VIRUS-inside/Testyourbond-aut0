package org.openqa.selenium.remote.server.handler;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.server.Session;















public class GetElementSelected
  extends WebElementHandler<Boolean>
{
  public GetElementSelected(Session session)
  {
    super(session);
  }
  
  public Boolean call() throws Exception
  {
    return Boolean.valueOf(getElement().isSelected());
  }
  
  public String toString()
  {
    return String.format("[is selected: %s]", new Object[] { getElementAsString() });
  }
}
