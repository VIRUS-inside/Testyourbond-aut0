package org.openqa.selenium.support.pagefactory.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
















public class LocatingElementListHandler
  implements InvocationHandler
{
  private final ElementLocator locator;
  
  public LocatingElementListHandler(ElementLocator locator)
  {
    this.locator = locator;
  }
  
  public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
    List<WebElement> elements = locator.findElements();
    try
    {
      return method.invoke(elements, objects);
    }
    catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}
