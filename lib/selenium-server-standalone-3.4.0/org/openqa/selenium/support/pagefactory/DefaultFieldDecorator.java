package org.openqa.selenium.support.pagefactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;























public class DefaultFieldDecorator
  implements FieldDecorator
{
  protected ElementLocatorFactory factory;
  
  public DefaultFieldDecorator(ElementLocatorFactory factory)
  {
    this.factory = factory;
  }
  
  public Object decorate(ClassLoader loader, Field field) {
    if ((!WebElement.class.isAssignableFrom(field.getType())) && 
      (!isDecoratableList(field))) {
      return null;
    }
    
    ElementLocator locator = factory.createLocator(field);
    if (locator == null) {
      return null;
    }
    
    if (WebElement.class.isAssignableFrom(field.getType()))
      return proxyForLocator(loader, locator);
    if (List.class.isAssignableFrom(field.getType())) {
      return proxyForListLocator(loader, locator);
    }
    return null;
  }
  
  protected boolean isDecoratableList(Field field)
  {
    if (!List.class.isAssignableFrom(field.getType())) {
      return false;
    }
    


    Type genericType = field.getGenericType();
    if (!(genericType instanceof ParameterizedType)) {
      return false;
    }
    
    Type listType = ((ParameterizedType)genericType).getActualTypeArguments()[0];
    
    if (!WebElement.class.equals(listType)) {
      return false;
    }
    
    if ((field.getAnnotation(FindBy.class) == null) && 
      (field.getAnnotation(FindBys.class) == null) && 
      (field.getAnnotation(FindAll.class) == null)) {
      return false;
    }
    
    return true;
  }
  
  protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
    InvocationHandler handler = new LocatingElementHandler(locator);
    

    WebElement proxy = (WebElement)Proxy.newProxyInstance(loader, new Class[] { WebElement.class, WrapsElement.class, Locatable.class }, handler);
    
    return proxy;
  }
  
  protected List<WebElement> proxyForListLocator(ClassLoader loader, ElementLocator locator)
  {
    InvocationHandler handler = new LocatingElementListHandler(locator);
    

    List<WebElement> proxy = (List)Proxy.newProxyInstance(loader, new Class[] { List.class }, handler);
    
    return proxy;
  }
}
