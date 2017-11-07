package org.openqa.selenium.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;















































public class PageFactory
{
  public PageFactory() {}
  
  public static <T> T initElements(WebDriver driver, Class<T> pageClassToProxy)
  {
    T page = instantiatePage(driver, pageClassToProxy);
    initElements(driver, page);
    return page;
  }
  







  public static void initElements(WebDriver driver, Object page)
  {
    WebDriver driverRef = driver;
    initElements(new DefaultElementLocatorFactory(driverRef), page);
  }
  







  public static void initElements(ElementLocatorFactory factory, Object page)
  {
    ElementLocatorFactory factoryRef = factory;
    initElements(new DefaultFieldDecorator(factoryRef), page);
  }
  






  public static void initElements(FieldDecorator decorator, Object page)
  {
    Class<?> proxyIn = page.getClass();
    while (proxyIn != Object.class) {
      proxyFields(decorator, page, proxyIn);
      proxyIn = proxyIn.getSuperclass();
    }
  }
  
  private static void proxyFields(FieldDecorator decorator, Object page, Class<?> proxyIn) {
    Field[] fields = proxyIn.getDeclaredFields();
    for (Field field : fields) {
      Object value = decorator.decorate(page.getClass().getClassLoader(), field);
      if (value != null) {
        try {
          field.setAccessible(true);
          field.set(page, value);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
  
  private static <T> T instantiatePage(WebDriver driver, Class<T> pageClassToProxy)
  {
    try {
      Constructor<T> constructor = pageClassToProxy.getConstructor(new Class[] { WebDriver.class });
      return constructor.newInstance(new Object[] { driver });
    } catch (NoSuchMethodException e) {
      return pageClassToProxy.newInstance();
    }
    catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
