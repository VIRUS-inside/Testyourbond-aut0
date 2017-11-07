package org.openqa.selenium.support.pagefactory.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
















public class LocatingElementHandler
  implements InvocationHandler
{
  private final ElementLocator locator;
  
  public LocatingElementHandler(ElementLocator locator)
  {
    this.locator = locator;
  }
  
  public Object invoke(Object object, Method method, Object[] objects) throws Throwable
  {
    try {
      element = locator.findElement();
    } catch (NoSuchElementException e) { WebElement element;
      if ("toString".equals(method.getName())) {
        return "Proxy element for: " + locator.toString();
      }
      throw e;
    }
    WebElement element;
    if ("getWrappedElement".equals(method.getName())) {
      return element;
    }
    try
    {
      return method.invoke(element, objects);
    }
    catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}
